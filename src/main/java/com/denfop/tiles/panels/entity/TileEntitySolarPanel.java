package com.denfop.tiles.panels.entity;


import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.Config;
import com.denfop.IUCore;
import com.denfop.api.IAdvEnergyNet;
import com.denfop.api.energy.IAdvEnergySource;
import com.denfop.api.energy.SunCoef;
import com.denfop.audio.AudioSource;
import com.denfop.container.ContainerSolarPanels;
import com.denfop.gui.GuiSolarPanels;
import com.denfop.invslot.InvSlotPanel;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.proxy.CommonProxy;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.tile.IWrenchable;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.init.Localization;
import ic2.core.ref.TeBlock;
import ic2.core.util.StackUtil;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileEntitySolarPanel extends TileEntityInventory implements IAdvEnergySource, IHasGui,
        IWrenchable, IEnergyProvider, INetworkDataProvider, INetworkClientTileEntityEventListener,
        INetworkUpdateListener, INetworkTileEntityEventListener {


    private final List<String> list_player = new ArrayList<>();
    public double coef;
    public List<IEnergyTile> list;
    public EnumSolarPanels solarpanels;
    public int tier;
    public List<WirelessTransfer> wirelessTransferList = new ArrayList<>();
    public InvSlotPanel inputslot;
    public boolean getmodulerf = false;
    public Biome biome;
    public boolean personality = false;
    public int solarType;
    public String player = null;
    public EnumType type;
    public boolean work = true;
    public boolean work1 = true;
    public boolean work2 = true;
    public boolean charge;
    public boolean wireless = false;
    public GenerationState active = GenerationState.NONE;
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
    public int time;
    public int time1;
    public int time2;
    public double o;
    public double maxStorage2;
    public double storage2;
    public boolean rf = true;
    public double moonPhase = 1;
    public double tick;
    public SunCoef sunCoef;
    protected double tierPower;
    protected boolean canRain;
    protected boolean hasSky;
    protected boolean addedToEnet;
    protected double pastEnergy;
    protected double perenergy;
    List<TransferRFEnergy> transferRFEnergyList = new ArrayList<>();
    private AudioSource audioSource;

    public TileEntitySolarPanel(
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
        this.time = 28800;
        this.time1 = 14400;
        this.time2 = 14400;
        this.maxStorage2 = this.maxStorage * 4;
        this.production = gOutput;
        this.u = gOutput;
        this.tier = tier;
        this.o = tier;
        this.inputslot = new InvSlotPanel(this, tier, 9, InvSlot.Access.IO);
        this.tierPower = EnergyNet.instance.getPowerFromTier(tier);
        this.type = EnumType.DEFAULT;
        this.solarpanels = type;
        this.list = new ArrayList<>();
        this.coef = 0;
        this.pastEnergy = 0;
        this.perenergy = 0;
        this.tick = 0;
    }

    public TileEntitySolarPanel(EnumSolarPanels solarpanels) {
        this(solarpanels.tier, solarpanels.genday, solarpanels.producing, solarpanels.maxstorage, solarpanels);

    }
    public void initiate(int soundEvent) {

        IC2.network.get(true).initiateTileEntityEvent(this, soundEvent, true);

    }  public void onNetworkEvent(int event) {
        if (this.audioSource == null && this.getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, this.getStartSoundFile());
        }

        if (event == 0) {
            if (this.audioSource != null) {
                this.audioSource.stop();
                this.audioSource.play();
            }
        }
    }

    public String getStartSoundFile() {
        return "Machines/pen.ogg";
    }
    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    protected boolean canEntityDestroy(Entity entity) {
        return !this.personality || (entity instanceof EntityPlayer && this.list_player.contains(entity.getName()));
    }

    @Override
    protected boolean wrenchCanRemove(final EntityPlayer player) {
        return !this.personality || (this.list_player.contains(player.getName()));

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
                    + ModUtils.getString(this.genDay) + " EU/t ");
            info.add(Localization.translate("supsolpans.iu.GenerationNight.tooltip") + " "
                    + ModUtils.getString(this.genNight) + " EU/t ");

            info.add(Localization.translate("ic2.item.tooltip.Output") + " "
                    + ModUtils.getString(this.production) + " EU/t ");
            info.add(Localization.translate("ic2.item.tooltip.Capacity") + " "
                    + ModUtils.getString(this.maxStorage) + " EU ");
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
        if (!this.work) {
            coefpollution = 0.75;
        }
        if (!this.work1) {
            coefpollution = 0.5;
        }
        if (!this.work2) {
            coefpollution = 0.25;
        }


        switch (this.active) {
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

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        this.player = placer.getName();
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        if (nbt.getBoolean("hasPollution")) {
            this.time = nbt.getInteger("time");
            this.time1 = nbt.getInteger("time1");
            this.time2 = nbt.getInteger("time2");
            this.work = nbt.getBoolean("work");
            this.work1 = nbt.getBoolean("work1");
            this.work2 = nbt.getBoolean("work2");
        }
    }

    protected List<ItemStack> getSelfDrops(int fortune, boolean wrench) {
        ItemStack drop = this.getPickBlock(null, null);
        drop = this.adjustDrop(drop, wrench, fortune);
        return drop == null ? Collections.emptyList() : Collections.singletonList(drop);
    }

    protected ItemStack adjustDrop(ItemStack drop, boolean wrench, int fortune) {
        drop = super.adjustDrop(drop, wrench);
        if (wrench || this.teBlock.getDefaultDrop() == TeBlock.DefaultDrop.Self) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(drop);
            if (fortune != 100) {
                nbt.setInteger("time", this.time);
                nbt.setInteger("time1", this.time1);
                nbt.setInteger("time2", this.time2);
                nbt.setBoolean("work", this.work);
                nbt.setBoolean("work1", this.work1);
                nbt.setBoolean("work2", this.work2);
                nbt.setBoolean("hasPollution", true);
            }
        }
        return drop;
    }

    protected void onLoaded() {
        super.onLoaded();
        if (!this.world.isRemote) {
            this.addedToEnet = !MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.canRain = (this.world.getBiome(this.pos).canRain() || this.world.getBiome(this.pos).getRainfall() > 0.0F);
            this.hasSky = !this.world.provider.isNether();
            this.biome = this.world.getBiome(this.pos);
            updateVisibility();
            this.inputslot.checkmodule();
            this.inputslot.getrfmodule();
            this.inputslot.personality();
            this.solarType = this.inputslot.solartype();
            this.wirelessTransferList.clear();
            this.inputslot.wirelessmodule();
            IAdvEnergyNet advEnergyNet = (IAdvEnergyNet) EnergyNet.instance;
            this.sunCoef = advEnergyNet.getSunCoefficient(this.world);
            if (this.personality) {
                for (int h = 0; h < this.inputslot.size(); h++) {
                    if (!this.inputslot.get(h).isEmpty() && this.inputslot.get(h).getItem() instanceof ItemAdditionModule
                            && this.inputslot.get(h).getItemDamage() == 0) {
                        NBTTagCompound nbt = ModUtils.nbt(this.inputslot.get(h));
                        int size = nbt.getInteger("size");
                        for (int m = 0; m < size; m++) {
                            this.list_player.add(nbt.getString("player_" + m));
                        }
                        break;
                    }

                }
                this.list_player.add(this.player);
            }
        }
    }

    private double experimental_generating() {
        double k = this.sunCoef.getCoef();

        double coef = this.coef;

        return Math.max(coef, k);

    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

        this.storage = nbttagcompound.getDouble("storage");
        this.time = nbttagcompound.getInteger("time");
        this.time1 = nbttagcompound.getInteger("time1");
        this.time2 = nbttagcompound.getInteger("time2");
        this.player = nbttagcompound.getString("player");
        this.storage2 = nbttagcompound.getDouble("storage2");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("time", this.time);
        nbttagcompound.setInteger("time1", this.time1);
        nbttagcompound.setInteger("time2", this.time2);
        nbttagcompound.setString("player", player);
        nbttagcompound.setDouble("storage", this.storage);
        nbttagcompound.setDouble("storage2", this.storage2);
        return nbttagcompound;
    }

    public double extractEnergy1(double maxExtract, boolean simulate) {
        double temp;

        temp = this.storage2;

        if (temp > 0) {
            double energyExtracted = Math.min(temp, maxExtract);
            if (!simulate &&
                    this.storage2 - temp >= 0.0D) {
                this.storage2 -= temp;
                if (energyExtracted > 0) {
                    temp -= energyExtracted;
                    this.storage2 += temp;
                }
                return energyExtracted;
            }
        }
        return 0;
    }

    public boolean canConnectEnergy(EnumFacing arg0) {
        return true;
    }

    public int getEnergyStored(EnumFacing from) {
        return (int) this.storage2;
    }

    public int getMaxEnergyStored(EnumFacing from) {
        return (int) this.maxStorage2;
    }

    public int extractEnergy(EnumFacing facing, int maxExtract, boolean simulate) {
        return extractEnergy((int) Math.min(this.production * 4, maxExtract), simulate);
    }

    public int extractEnergy(int paramInt, boolean paramBoolean) {
        int i = (int) Math.min(this.storage2, Math.min(this.production * 4, paramInt));
        if (!paramBoolean) {
            this.storage2 -= i;
        }
        return i;
    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (this.addedToEnet) {
            this.addedToEnet = MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    protected void updateEntityServer() {
        super.updateEntityServer();


        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            updateVisibility();

        }


        if (this.charge) {
            this.inputslot.charge();
        }
        if (this.charge && this.getmodulerf) {
            this.inputslot.rfcharge();
        }
        if (this.getmodulerf) {

            if (this.getWorld().getWorldTime() % 60 == 0) {
                transferRFEnergyList.clear();
                for (EnumFacing facing : EnumFacing.VALUES) {
                    BlockPos pos = new BlockPos(
                            this.pos.getX() + facing.getFrontOffsetX(),
                            this.pos.getY() + facing.getFrontOffsetY(),
                            this.pos.getZ() + facing.getFrontOffsetZ()
                    );
                    TileEntity tile = this.getWorld().getTileEntity(pos);
                    if (tile == null) {
                        continue;
                    }
                    if (tile instanceof IEnergyReceiver) {
                        transferRFEnergyList.add(new TransferRFEnergy(tile, ((IEnergyReceiver) tile), facing));
                    }
                }

            }
            boolean refresh = false;
            for (TransferRFEnergy rfEnergy : this.transferRFEnergyList) {
                if (rfEnergy.getTile().isInvalid()) {
                    refresh = true;
                    continue;
                }
                extractEnergy(rfEnergy.getFacing(), rfEnergy.getSink().receiveEnergy(rfEnergy.getFacing().getOpposite(),
                        extractEnergy(rfEnergy.getFacing(), (int) this.storage2, true), false
                ), false);
            }
            if (refresh) {
                transferRFEnergyList.clear();
                for (EnumFacing facing : EnumFacing.VALUES) {
                    BlockPos pos = new BlockPos(
                            this.pos.getX() + facing.getFrontOffsetX(),
                            this.pos.getY() + facing.getFrontOffsetY(),
                            this.pos.getZ() + facing.getFrontOffsetZ()
                    );
                    TileEntity tile = this.getWorld().getTileEntity(pos);
                    if (tile == null) {
                        continue;
                    }
                    if (tile instanceof IEnergyReceiver) {
                        transferRFEnergyList.add(new TransferRFEnergy(tile, ((IEnergyReceiver) tile), facing));
                    }
                }
            }
        }

        if (this.active == GenerationState.NONE) {
            this.generating = 0;
            return;
        }
        if (this.getWorld().provider.getWorldTime() % 20 == 0) {
            this.inputslot.time();
        }
        if (this.wireless) {
            boolean refresh = false;
            try {
                for (WirelessTransfer transfer : this.wirelessTransferList) {
                    if (transfer.getTile().isInvalid()) {
                        refresh = true;
                        continue;
                    }
                    double energy = Math.min(this.getOfferedEnergy(), transfer.getSink().getDemandedEnergy());
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

        if (this.storage2 >= this.maxStorage2) {
            this.storage2 = this.maxStorage2;
        } else if (this.storage2 < 0) {
            this.storage2 = 0;
        }


        if (this.generating > 0) {
            if (getmodulerf) {
                if (!rf) {
                    if (this.storage + this.generating <= this.maxStorage) {
                        this.storage += this.generating;
                    } else {
                        this.storage = this.maxStorage;
                    }
                } else {

                    if ((this.storage2 + (this.generating * 4)) <= this.maxStorage2) {
                        this.storage2 += (this.generating * 4);
                    } else {
                        this.storage2 = this.maxStorage2;

                    }
                }

            } else {
                if (this.storage + this.generating <= this.maxStorage) {
                    this.storage += this.generating;
                } else {
                    this.storage = this.maxStorage;
                }
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
            this.active = GenerationState.NONE;
        }
        if (this.sunIsUp && this.skyIsVisible) {
            if (!(this.rain)) {
                this.active = GenerationState.DAY;
            } else {
                this.active = GenerationState.RAINDAY;
            }

        }
        if (!this.sunIsUp && this.skyIsVisible) {
            if (!(rain)) {
                this.active = GenerationState.NIGHT;
            } else {
                this.active = GenerationState.RAINNIGHT;
            }
        }
        if (this.world.provider.getDimension() == 1) {
            this.active = GenerationState.END;
        }
        if (this.world.provider.getDimension() == -1) {
            this.active = GenerationState.NETHER;
        }

    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
        return true;
    }

    public void drawEnergy(double amount) {
        this.storage = (this.storage - amount);
    }

    public int getSourceTier() {
        return this.tier;
    }


    @Override
    protected boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (personality) {
            if (!(this.list_player.contains(player.getName()) || player.capabilities.isCreativeMode)) {
                CommonProxy.sendPlayerMessage(player, Localization.translate("iu.error"));
                return false;
            }
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
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
        List<ItemStack> list = new ArrayList<>();
        inputslot.forEach(list::add);
        return list;
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

    public void onGuiClosed(EntityPlayer player) {
    }

    public ContainerBase<TileEntitySolarPanel> getGuiContainer(EntityPlayer player) {
        return new ContainerSolarPanels(player, this);
    }

    public double getOfferedEnergy() {

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

    public float gaugeEnergyScaled2(final float i) {
        if ((this.storage2 * i / this.maxStorage2) > 24) {
            return 24;
        }

        return (float) (this.storage2 * i / (this.maxStorage2));

    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("generating");
        ret.add("storage");
        ret.add("maxStorage");
        ret.add("production");
        ret.add("solarType");
        ret.add("tier");
        ret.add("type");
        return ret;
    }

    @Override
    public void onNetworkEvent(EntityPlayer player, int event) {
        if(getmodulerf) {
            this.rf = !this.rf;
            initiate(0);
        }
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
