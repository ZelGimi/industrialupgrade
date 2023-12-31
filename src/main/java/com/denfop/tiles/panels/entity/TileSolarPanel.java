package com.denfop.tiles.panels.entity;


import com.denfop.Config;
import com.denfop.Localization;
import com.denfop.api.IAdvEnergyNet;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.IEnergySource;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.SunCoef;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.tile.IWrenchable;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.componets.ComponentPollution;
import com.denfop.componets.ComponentTimer;
import com.denfop.container.ContainerSolarPanels;
import com.denfop.gui.GuiSolarPanels;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotPanel;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TileSolarPanel extends TileEntityInventory implements IEnergySource,
        IWrenchable, IUpdatableTileEvent {


    public final ComponentTimer timer;
    public final ComponentPollution pollution;
    public double coef;
    public List<IEnergyTile> list;
    public EnumSolarPanels solarpanels;
    public int tier;
    public List<WirelessTransfer> wirelessTransferList = new ArrayList<>();
    public InvSlotPanel inputslot;
    public Biome biome;
    public int solarType;
    public EnumType type;

    public boolean charge;
    public boolean wireless = false;
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
    public double production;
    public double maxStorage;
    public double p;
    public double k;
    public double m;
    public double u;
    public double o;
    public double moonPhase = 1;
    public double tick;
    public SunCoef sunCoef;
    public int level = 0;
    public boolean canRain;
    public boolean hasSky;
    protected double tierPower;
    protected boolean addedToEnet;
    protected double pastEnergy;
    protected double perenergy;

    public TileSolarPanel(
            final int tier, final double gDay,
            final double gOutput, final double gmaxStorage, EnumSolarPanels type
    ) {
        this.solarType = 0;
        this.genDay = gDay;
        this.genNight = gDay / 2;
        this.storage = 0;
        this.generating = 0;
        this.maxStorage = gmaxStorage;
        this.p = gmaxStorage;
        this.k = gDay;
        this.m = gDay / 2;
        this.production = gOutput;
        this.u = gOutput;
        this.tier = tier;
        this.o = tier;
        this.inputslot = new InvSlotPanel(this, tier, 9, InvSlot.TypeItemSlot.INPUT_OUTPUT);
        this.tierPower = EnergyNetGlobal.instance.getPowerFromTier(tier);
        this.type = EnumType.DEFAULT;
        this.solarpanels = type;
        this.list = new ArrayList<>();
        this.coef = 0;
        this.pastEnergy = 0;
        this.perenergy = 0;
        this.tick = 0;
        this.pollution = this.addComponent(new ComponentPollution(this));
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(8, 0, 0), new Timer(4, 0, 0), new Timer(4, 0, 0)) {
            @Override
            public boolean needWriteNBTToDrops() {
                return true;
            }
        });
        this.pollution.setTimer(timer);
    }

    public TileSolarPanel(EnumSolarPanels solarpanels) {
        this(solarpanels.tier, solarpanels.genday, solarpanels.producing, solarpanels.maxstorage, solarpanels);

    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        final CustomPacketBuffer packet = super.writeUpdatePacket();
        try {
            EncoderHandler.encode(packet, this.pollution,false);
            EncoderHandler.encode(packet, this.timer,false);
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
            sunIsUp = (boolean) DecoderHandler.decode(customPacketBuffer);
            skyIsVisible = (boolean) DecoderHandler.decode(customPacketBuffer);
            generating = (double) DecoderHandler.decode(customPacketBuffer);
            genDay = (double) DecoderHandler.decode(customPacketBuffer);
            genNight = (double) DecoderHandler.decode(customPacketBuffer);
            storage = (double) DecoderHandler.decode(customPacketBuffer);
            maxStorage = (double) DecoderHandler.decode(customPacketBuffer);
            production = (double) DecoderHandler.decode(customPacketBuffer);
            rain = (boolean) DecoderHandler.decode(customPacketBuffer);
            solarType = (int) DecoderHandler.decode(customPacketBuffer);
            type = EnumType.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            u = (double) DecoderHandler.decode(customPacketBuffer);
            p = (double) DecoderHandler.decode(customPacketBuffer);
            k = (double) DecoderHandler.decode(customPacketBuffer);
            m = (double) DecoderHandler.decode(customPacketBuffer);
            tier = (int) DecoderHandler.decode(customPacketBuffer);
            boolean isNull = (boolean) DecoderHandler.decode(customPacketBuffer);
            if(!isNull)
            solarpanels = EnumSolarPanels.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            activeState = GenerationState.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            wireless = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, sunIsUp);
            EncoderHandler.encode(packet, skyIsVisible);
            EncoderHandler.encode(packet, generating);
            EncoderHandler.encode(packet, genDay);
            EncoderHandler.encode(packet, genNight);
            EncoderHandler.encode(packet, storage);
            EncoderHandler.encode(packet, maxStorage);
            EncoderHandler.encode(packet, production);
            EncoderHandler.encode(packet, rain);
            EncoderHandler.encode(packet, solarType);
            EncoderHandler.encode(packet, type);
            EncoderHandler.encode(packet, u);
            EncoderHandler.encode(packet, p);
            EncoderHandler.encode(packet, k);
            EncoderHandler.encode(packet, m);
            EncoderHandler.encode(packet, tier);
            EncoderHandler.encode(packet, solarpanels == null);
            if(solarpanels != null)
            EncoderHandler.encode(packet, solarpanels);
            EncoderHandler.encode(packet, activeState);
            EncoderHandler.encode(packet, wireless);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }


    public void loadBeforeFirstUpdate() {
        super.loadBeforeFirstUpdate();
        this.wirelessTransferList.clear();
        this.inputslot.wirelessmodule();
        this.wireless = !this.wirelessTransferList.isEmpty();
    }

    public String getStartSoundFile() {
        return "Machines/pen.ogg";
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
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final List<String> info, final ITooltipFlag advanced) {


        if (Config.promt) {
            info.add(Localization.translate("supsolpans.iu.GenerationDay.tooltip") + " "
                    + ModUtils.getString(this.genDay) + " EF/t ");
            info.add(Localization.translate("supsolpans.iu.GenerationNight.tooltip") + " "
                    + ModUtils.getString(this.genNight) + " EF/t ");

            info.add(Localization.translate("iu.item.tooltip.Output") + " "
                    + ModUtils.getString(this.production) + " EF/t ");
            info.add(Localization.translate("iu.item.tooltip.Capacity") + " "
                    + ModUtils.getString(this.maxStorage) + " EF ");
            info.add(Localization.translate("iu.tier") + ModUtils.getString(this.tier));

        }


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
        drop = super.adjustDrop(drop, wrench);
        if (wrench || this.teBlock.getDefaultDrop() == MultiTileBlock.DefaultDrop.Self) {
            NBTTagCompound nbt = ModUtils.nbt(drop);
            if (fortune == 100) {
                nbt.removeTag(this.timer.toString());
            }
        }
        return drop;
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();

        MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));

    }


    public void onLoaded() {
        super.onLoaded();
        if (!this.world.isRemote) {
            this.canRain = (this.world.getBiome(this.pos).canRain() || this.world.getBiome(this.pos).getRainfall() > 0.0F);
            this.hasSky = !this.world.provider.isNether();
            this.biome = this.world.getBiome(this.pos);
            updateVisibility();
            this.inputslot.checkmodule();
            this.solarType = this.inputslot.solartype();
            IAdvEnergyNet advEnergyNet = EnergyNetGlobal.instance;
            this.sunCoef = advEnergyNet.getSunCoefficient(this.world);
        }
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this));
    }

    private double experimental_generating() {
        double k = this.sunCoef.getCoef();

        double coef = this.coef;

        return Math.max(coef, k);

    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

        this.storage = nbttagcompound.getDouble("storage");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("storage", this.storage);
        return nbttagcompound;
    }


    public void updateEntityServer() {
        super.updateEntityServer();

        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
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
        if (this.wireless) {
            boolean refresh = false;
            try {
                for (WirelessTransfer transfer : this.wirelessTransferList) {
                    if (transfer.getTile().isInvalid()) {
                        refresh = true;
                        continue;
                    }
                    double energy = Math.min(this.canExtractEnergy(), transfer.getSink().getDemandedEnergy());
                    transfer.work(energy);
                    this.storage -= energy;
                }
            } catch (Exception ignored) {
            }
            if (refresh) {
                this.wirelessTransferList.clear();
                this.inputslot.wirelessmodule();
            }
        }
        gainFuel();


        if (this.generating > 0) {
            if (this.storage + this.generating <= this.maxStorage) {
                this.storage += this.generating;
            } else {
                this.storage = this.maxStorage;
            }
        }

    }

    public void updateVisibility() {
        this.wetBiome = this.biome.getRainfall() > 0.0F;
        this.noSunWorld = this.world.provider.isNether();

        this.rain = this.wetBiome && (this.world.isRaining() || this.world.isThundering());
        this.sunIsUp = this.world.isDaytime();
        this.skyIsVisible = this.world.canBlockSeeSky(this.pos.up()) &&
                (this.world.getBlockState(this.pos.up()).getMaterial().getMaterialMapColor() ==
                        MapColor.AIR) && !this.noSunWorld;
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
        if (this.world.provider.getDimension() == 1) {
            this.activeState = GenerationState.END;
        }
        if (this.world.provider.getDimension() == -1) {
            this.activeState = GenerationState.NETHER;
        }

    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
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
            World world,
            BlockPos blockPos,
            IBlockState iBlockState,
            TileEntity tileEntity,
            EntityPlayer entityPlayer,
            int i
    ) {
        return new ArrayList<>(Arrays.asList(inputslot.gets()));
    }

    @Override
    public void wrenchBreak(final World world, final BlockPos pos) {
        this.wrenchBreak();
    }

    @Override
    public EnumFacing getFacing(World world, BlockPos blockPos) {
        return this.getFacing();
    }

    @Override
    public boolean setFacing(World world, BlockPos blockPos, EnumFacing enumFacing, EntityPlayer entityPlayer) {
        return false;
    }

    @Override
    public boolean wrenchCanRemove(World world, BlockPos blockPos, EntityPlayer entityPlayer) {
        return true;
    }


    public ContainerSolarPanels getGuiContainer(EntityPlayer player) {
        return new ContainerSolarPanels(player, this);
    }

    public double canExtractEnergy() {

        return Math.min(this.production, this.storage);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiSolarPanels(new ContainerSolarPanels(player, this));
    }

    public double gaugeEnergyScaled(final float i) {

        if ((this.storage * i / this.maxStorage) > 24) {
            return 24;
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
    public void updateTileServer(EntityPlayer player, double event) {

    }

    public EnumType getType() {
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
                if (this.world.provider.getDimension() == -1) {
                    return 3;
                }
                break;
            case END:
                if (this.world.provider.getDimension() == 1) {
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
                if ((this.world.isRaining() || this.world.isThundering())) {
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

    @Override
    public TileEntity getTileEntity() {
        return this;
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
