package com.denfop.tiles.panels.entity;


import com.denfop.ElectricItem;
import com.denfop.Localization;
import com.denfop.api.IAdvEnergyNet;
import com.denfop.api.energy.*;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.tile.IWrenchable;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.componets.ComponentPollution;
import com.denfop.componets.ComponentTimer;
import com.denfop.componets.WirelessComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSolarPanels;
import com.denfop.container.ContainerSolarPanels1;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSolarPanels;
import com.denfop.gui.GuiSolarPanels1;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotPanel;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketChangeSolarPanel;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.*;

public class TileSolarPanel extends TileEntityInventory implements IEnergySource,
        IWrenchable, IUpdatableTileEvent {

    public static List<BlockEntityType<? extends TileSolarPanel>> list = new ArrayList<>();

    public final ComponentTimer timer;
    public final ComponentPollution pollution;
    public final WirelessComponent wirelessComponent;
    public final InvSlot slotDept;
    public double coef;
    public EnumSolarPanels solarpanels;
    public int tier;
    public InvSlotPanel inputslot;
    public Holder<Biome> biome;
    public int solarType;
    public EnumType type;

    public boolean charge;
    public GenerationState activeState = GenerationState.NONE;
    public boolean wetBiome;
    public boolean noSunWorld;
    public boolean rain;
    public boolean sunIsUp;
    public boolean skyIsVisible;
    public double generating;
    public double genDay;
    public double genNight;
    public double storage;
    public double output;
    public double maxStorage;
    public double defaultMaxStorage;
    public double defaultDay;
    public double defaultNight;
    public double defaultOutoput;
    public double defaultTier;
    public double moonPhase = 1;
    public SunCoef sunCoef;
    public byte percent = 0;
    public double debt;
    public double debtMax;
    public int levelPanel = 0;
    public boolean canRain;
    public boolean hasSky;
    public double deptPercent;
    public double deptGenerate = 0;
    public boolean addedToEnergyNet = false;
    public boolean twoContainer = false;
    protected double pastEnergy;
    protected double perenergy;
    Map<Direction, IEnergyTile> energyConductorMap = new HashMap<>();
    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();
    int hashCodeSource;
    private long id;

    public TileSolarPanel(
            final int tier, final double gDay,
            final double gOutput, final double gmaxStorage, EnumSolarPanels type, IMultiTileBlock block, BlockPos pos, BlockState state
    ) {
        super(block, pos, state);
        this.solarType = 0;
        this.genDay = gDay;
        this.genNight = gDay / 2;
        if (genNight < 1D) {
            genNight = 0;
        }
        this.storage = 0;
        this.generating = 0;
        this.maxStorage = gmaxStorage;
        this.defaultMaxStorage = gmaxStorage;
        this.defaultDay = gDay;
        this.defaultNight = gDay / 2;
        if (defaultNight < 1D) {
            defaultNight = 0;
        }
        this.debtMax = maxStorage * 4;
        this.output = gOutput;
        this.defaultOutoput = gOutput;
        this.tier = tier;
        this.defaultTier = tier;
        this.inputslot = new InvSlotPanel(this, tier, 9, InvSlot.TypeItemSlot.INPUT_OUTPUT);
        this.type = EnumType.DEFAULT;
        this.solarpanels = type;
        this.coef = 0;
        this.pastEnergy = 0;
        this.perenergy = 0;
        this.pollution = this.addComponent(new ComponentPollution(this));
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(8, 0, 0), new Timer(4, 0, 0), new Timer(4, 0, 0)) {
            @Override
            public boolean needWriteNBTToDrops() {
                return true;
            }
        });
        this.pollution.setTimer(timer);
        this.wirelessComponent = this.addComponent(new WirelessComponent(this));
        wirelessComponent.setEnergySource(this);
        this.slotDept = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof IEnergyItem && ((IEnergyItem) stack.getItem()).canProvideEnergy(stack);
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.BATTERY;
            }
        };
        if (list != null) {
            if (pos.equals(BlockPos.ZERO)) {
                list.add((BlockEntityType<? extends TileSolarPanel>) block.getBlockType());
            }
        }
    }


    public TileSolarPanel(EnumSolarPanels solarpanels, IMultiTileBlock block, BlockPos pos, BlockState state) {
        this(solarpanels.tier, solarpanels.genday, solarpanels.producing, solarpanels.maxstorage, solarpanels, block, pos, state);

    }

    @Override
    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public void RemoveTile(IEnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<IEnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<IEnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    public long getIdNetwork() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    public void AddTile(IEnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
        }
    }

    public Map<Direction, IEnergyTile> getTiles() {
        return energyConductorMap;
    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        final CustomPacketBuffer packet = super.writeUpdatePacket();
        try {
            EncoderHandler.encode(packet, this.pollution, false);
            EncoderHandler.encode(packet, this.timer, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void readUpdatePacket(final CustomPacketBuffer customPacketBuffer) {
        super.readUpdatePacket(customPacketBuffer);
        try {
            pollution.onNetworkUpdate(customPacketBuffer);
            timer.onNetworkUpdate(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean needUpdate() {
        return true;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {

            deptPercent = customPacketBuffer.readDouble();
            debt = customPacketBuffer.readDouble();
            sunIsUp = (boolean) DecoderHandler.decode(customPacketBuffer);
            skyIsVisible = (boolean) DecoderHandler.decode(customPacketBuffer);
            generating = (double) DecoderHandler.decode(customPacketBuffer);
            genDay = (double) DecoderHandler.decode(customPacketBuffer);
            genNight = (double) DecoderHandler.decode(customPacketBuffer);
            storage = (double) DecoderHandler.decode(customPacketBuffer);
            maxStorage = (double) DecoderHandler.decode(customPacketBuffer);
            output = (double) DecoderHandler.decode(customPacketBuffer);
            rain = (boolean) DecoderHandler.decode(customPacketBuffer);
            solarType = (int) DecoderHandler.decode(customPacketBuffer);
            type = EnumType.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            defaultOutoput = (double) DecoderHandler.decode(customPacketBuffer);
            defaultMaxStorage = (double) DecoderHandler.decode(customPacketBuffer);
            defaultDay = (double) DecoderHandler.decode(customPacketBuffer);
            defaultNight = (double) DecoderHandler.decode(customPacketBuffer);
            tier = (int) DecoderHandler.decode(customPacketBuffer);
            boolean isNull = (boolean) DecoderHandler.decode(customPacketBuffer);
            if (!isNull) {
                solarpanels = EnumSolarPanels.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            }
            activeState = GenerationState.values()[(int) DecoderHandler.decode(customPacketBuffer)];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            packet.writeDouble(deptPercent);
            packet.writeDouble(debt);
            EncoderHandler.encode(packet, sunIsUp);
            EncoderHandler.encode(packet, skyIsVisible);
            EncoderHandler.encode(packet, generating);
            EncoderHandler.encode(packet, genDay);
            EncoderHandler.encode(packet, genNight);
            EncoderHandler.encode(packet, storage);
            EncoderHandler.encode(packet, maxStorage);
            EncoderHandler.encode(packet, output);
            EncoderHandler.encode(packet, rain);
            EncoderHandler.encode(packet, solarType);
            EncoderHandler.encode(packet, type);
            EncoderHandler.encode(packet, defaultOutoput);
            EncoderHandler.encode(packet, defaultMaxStorage);
            EncoderHandler.encode(packet, defaultDay);
            EncoderHandler.encode(packet, defaultNight);
            EncoderHandler.encode(packet, tier);
            EncoderHandler.encode(packet, solarpanels == null);
            if (solarpanels != null) {
                EncoderHandler.encode(packet, solarpanels);
            }
            EncoderHandler.encode(packet, activeState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void loadBeforeFirstUpdate() {
        super.loadBeforeFirstUpdate();
        this.inputslot.wirelessmodule();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    public List<ItemStack> getDrop() {
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (!inputslot.get(i).isEmpty()) {
                list.add(inputslot.get(i));
            }
        }
        return list;
    }

    @Override
    public void addInformation(final ItemStack itemStack, final List<String> info) {


        if (this.getWorld() == null) {
            info.add(Localization.translate("supsolpans.iu.GenerationDay.tooltip") + " "
                    + ModUtils.getString(this.genDay) + " EF/t ");
            info.add(Localization.translate("supsolpans.iu.GenerationNight.tooltip") + " "
                    + ModUtils.getString(this.genNight) + " EF/t ");
        } else {
            if (this.getWorld().isDay()) {
                info.add(Localization.translate("supsolpans.iu.GenerationDay.tooltip") + " "
                        + ModUtils.getString(this.generating) + " EF/t ");
                info.add(Localization.translate("supsolpans.iu.GenerationNight.tooltip") + " "
                        + ModUtils.getString(this.genNight) + " EF/t ");
            } else {
                info.add(Localization.translate("supsolpans.iu.GenerationDay.tooltip") + " "
                        + ModUtils.getString(this.genDay) + " EF/t ");
                info.add(Localization.translate("supsolpans.iu.GenerationNight.tooltip") + " "
                        + ModUtils.getString(this.generating) + " EF/t ");
            }
        }
        info.add(Localization.translate("iu.item.tooltip.Output") + " "
                + ModUtils.getString(this.output) + " EF/t ");
        info.add(Localization.translate("iu.item.tooltip.Capacity") + " "
                + ModUtils.getString(this.maxStorage) + " EF ");
        info.add(Localization.translate("iu.tier") + ModUtils.getString(this.tier));


    }

    public EnumSolarPanels getPanels() {
        return this.solarpanels;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public void gainFuel() {
        double coefpollution = 1;
        switch (this.timer.getIndexWork()) {
            case -1:
                coefpollution = 0.25;
                break;
            case 1:
                coefpollution = 0.75;
                break;
            case 2:
                coefpollution = 0.5;
                break;
        }
        switch (this.activeState) {
            case DAY:
                this.generating = type.coefficient_day * this.genDay;
                break;
            case NIGHT:
                this.generating = type.coefficient_night * this.genNight;
                break;
            case RAINDAY:
                this.generating = type.coefficient_rain * type.coefficient_day * this.genDay;
                break;
            case RAINNIGHT:
                this.generating = type.coefficient_rain * type.coefficient_night * this.genNight;
                break;
            case NETHER:
                this.generating = type.coefficient_nether * this.genDay;
                break;
            case END:
                this.generating = type.coefficient_end * this.genDay;
                break;
            case NONE:
                this.generating = 0;
                break;

        }
        double coefficient_phase;
        coefficient_phase = experimental_generating();
        double coef = moonPhase;
        if (this.sunIsUp) {
            coef = 1;
        }
        this.generating *= coefpollution * coefficient_phase * coef;
    }

    public List<ItemStack> getSelfDrops(int fortune, boolean wrench) {
        List<ItemStack> drop = super.getSelfDrops(fortune, wrench);
        drop = Collections.singletonList(this.adjustDrop(drop.get(0), wrench, fortune));
        return drop;
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench, int fortune) {
        drop = super.adjustDrop(drop, wrench, fortune);
        if (wrench || this.teBlock.getDefaultDrop() == DefaultDrop.Self) {
            CompoundTag nbt = ModUtils.nbt(drop);
            if (fortune == 100) {
                nbt.remove(this.timer.toString());
            }
        }
        return drop;
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (!this.getWorld().isClientSide() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));
            this.addedToEnergyNet = false;
        }

    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide()) {
            this.biome = this.getWorld().getBiome(this.pos);
            this.canRain = (biome.value().getPrecipitationAt(pos) == Biome.Precipitation.RAIN || biome.value().getModifiedClimateSettings().downfall() > 0.0F);
            this.hasSky = !(this.getLevel().dimension() == Level.NETHER);

            updateVisibility();
            this.inputslot.checkmodule();
            this.solarType = this.inputslot.solartype();
            IAdvEnergyNet advEnergyNet = EnergyNetGlobal.instance;
            this.sunCoef = advEnergyNet.getSunCoefficient(this.getWorld());
            if (!addedToEnergyNet) {
                this.energyConductorMap.clear();
                this.addedToEnergyNet = true;
                validReceivers.clear();
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this));
            }


        }

    }

    private double experimental_generating() {
        double k = this.sunCoef.getCoef();

        double coef = this.coef;

        return Math.max(coef, k);

    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);

        this.storage = nbttagcompound.getDouble("storage");
        this.debt = nbttagcompound.getDouble("debt");
        this.deptPercent = nbttagcompound.getDouble("deptPercent");

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putDouble("storage", this.storage);
        nbttagcompound.putDouble("debt", this.debt);
        nbttagcompound.putDouble("deptPercent", this.deptPercent);
        return nbttagcompound;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.debt > 0) {
            if (!this.slotDept.isEmpty()) {
                final double amount = ElectricItem.manager.discharge(this.slotDept.get(0), debt, this.tier, false, false, false);
                this.debt -= amount;
            }
        }
        if (this.getWorld().getGameTime() % 40 == 0) {
            updateVisibility();
            this.solarType = this.inputslot.solartypeFast();
            if (this.solarType == 0) {
                this.setActive("");
            } else {
                this.setActive(EnumType.getFromID(this.solarType).getNameType());
            }
        }


        if (this.charge) {
            this.inputslot.charge();
        }
        if (this.activeState == GenerationState.NONE) {
            this.generating = 0;
            return;
        }
        gainFuel();


        if (this.generating > 0) {
            if (this.storage + this.generating <= this.maxStorage) {
                double tempGenerate = this.generating + this.generating * deptPercent / 100D;
                if (deptPercent < 0) {
                    this.debt += this.generating * deptPercent / 100D;
                    if (this.debt < 0) {
                        this.debt = 0;
                    }
                } else {
                    this.debt += this.generating * deptPercent / 200D;
                    if (debt >= debtMax) {
                        debt = debtMax;
                    }
                }
                this.generating = tempGenerate;
                if (debt < debtMax) {
                    this.storage += generating;
                } else {
                    if (solarpanels != null && solarpanels.solarold != null) {
                        new PacketChangeSolarPanel(solarpanels, this);
                    }
                }
            } else {
                this.storage = this.maxStorage;
            }
        }

    }

    public void updateVisibility() {
        if (biome == null) {
            this.biome = this.getLevel().getBiome(this.pos);
        }
        this.wetBiome = this.biome.value().getModifiedClimateSettings().downfall() > 0.0F;
        this.noSunWorld = this.getLevel().dimension() == Level.NETHER;

        this.rain = this.wetBiome && (this.getLevel().isRaining() || this.getLevel().isThundering());
        this.sunIsUp = this.getLevel().isDay();
        this.skyIsVisible = this.getLevel().canSeeSky(this.worldPosition) &&
                (this.getLevel().getBlockState(this.worldPosition.above()).getMapColor(getLevel(), this.worldPosition.above()) == MapColor.NONE) &&
                !this.noSunWorld;

        if (!this.skyIsVisible) {
            this.activeState = GenerationState.NONE;
        }
        if (this.sunIsUp && this.skyIsVisible) {
            if (!(this.rain)) {
                this.activeState = GenerationState.DAY;
            } else {
                this.activeState = GenerationState.RAINDAY;
            }

        }
        if (!this.sunIsUp && this.skyIsVisible) {
            if (!(rain)) {
                this.activeState = GenerationState.NIGHT;
            } else {
                this.activeState = GenerationState.RAINNIGHT;
            }
        }
        if (this.level.dimension() == Level.END) {
            this.activeState = GenerationState.END;
        }
        if (this.level.dimension() == Level.NETHER) {
            this.activeState = GenerationState.NETHER;
        }

    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, Direction side) {
        return true;
    }

    public void extractEnergy(double amount) {
        this.storage = (this.storage - amount);
    }

    public int getSourceTier() {
        return this.tier;
    }

    @Override
    public List<ItemStack> getWrenchDrops(
            Level world,
            BlockPos blockPos,
            BlockState iBlockState,
            BlockEntity tileEntity,
            Player entityPlayer,
            int i
    ) {
        return new ArrayList<>(inputslot);
    }

    @Override
    public void wrenchBreak(final Level world, final BlockPos pos) {
        this.wrenchBreak();
    }

    @Override
    public Direction getFacing(Level world, BlockPos blockPos) {
        return this.getFacing();
    }

    @Override
    public boolean setFacing(Level world, BlockPos blockPos, Direction enumFacing, Player entityPlayer) {
        return false;
    }

    @Override
    public boolean canEntityDestroy(final Entity entity) {
        return super.canEntityDestroy(entity) && debt == 0;
    }

    @Override
    public boolean wrenchCanRemove(Level world, BlockPos blockPos, Player entityPlayer) {
        return getComponentPrivate().wrenchCanRemove(entityPlayer) && debt == 0;
    }

    public ContainerBase<TileSolarPanel> getGuiContainer(Player player) {
        if (this.twoContainer) {
            return new ContainerSolarPanels1(player, this);
        }
        return new ContainerSolarPanels(player, this);
    }

    public double canExtractEnergy() {

        return Math.min(this.output, this.storage);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player player, ContainerBase<? extends IAdvInventory> isAdmin) {
        if (this.twoContainer) {
            this.twoContainer = false;
            ((ContainerSolarPanels1) isAdmin).tileentity.twoContainer = false;
            return new GuiSolarPanels1((ContainerSolarPanels1) isAdmin);
        }
        return new GuiSolarPanels((ContainerSolarPanels) isAdmin);
    }

    public double gaugeEnergyScaled(final float i) {

        if ((this.storage * i / this.maxStorage) > 84) {
            return 84;
        }

        return (float) (this.storage * i / (this.maxStorage));


    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, solarType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            solarType = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTileServer(Player player, double event) {
        if (event == 1000) {
            this.twoContainer = true;
            this.onActivated(player, player.getUsedItemHand(), Direction.NORTH, new Vec3(0, 0, 0));
        } else {
            deptPercent = event;
        }
    }

    public EnumType getTypePanel() {
        return this.type;
    }

    public void setType(EnumType type) {
        this.type = type;
    }

    public int setSolarType(EnumType type) {
        if (type == null) {
            setType(EnumType.DEFAULT);
            return 0;
        }
        setType(type);
        switch (type) {
            case AIR:
                if (this.pos.getY() >= 130) {
                    return 1;
                }
                break;
            case EARTH:
                if (this.pos.getY() <= 40) {
                    return 2;
                }
                break;
            case NETHER:
                if (this.getLevel().dimension() == Level.NETHER) {
                    return 3;
                }
                break;
            case END:
                if (this.getLevel().dimension() == Level.END) {
                    return 4;
                }
                break;
            case NIGHT:
                if (!this.sunIsUp) {
                    return 5;
                }
                break;
            case DAY:
                if (this.sunIsUp) {
                    return 6;
                }
                break;
            case RAIN:
                if ((this.getLevel().isRaining() || this.getLevel().isThundering())) {
                    return 7;
                }
                break;

        }
        setType(EnumType.DEFAULT);
        return 0;
    }

    @Override
    public double getPerEnergy() {
        return this.perenergy;
    }

    @Override
    public double getPastEnergy() {
        return this.pastEnergy;
    }

    @Override
    public void setPastEnergy(final double pastEnergy) {
        this.pastEnergy = pastEnergy;
    }

    @Override
    public void addPerEnergy(final double setEnergy) {
        this.perenergy += setEnergy;
    }

    @Override
    public boolean isSource() {
        return true;
    }


    public boolean canRender() {
        return true;
    }


    public enum GenerationState {
        DAY,
        NIGHT,
        RAINDAY,
        RAINNIGHT,
        NETHER,
        END,
        NONE
    }


}
