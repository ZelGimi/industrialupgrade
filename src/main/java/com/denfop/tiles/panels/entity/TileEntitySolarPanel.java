package com.denfop.tiles.panels.entity;


import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.Config;
import com.denfop.container.ContainerSolarPanels;
import com.denfop.gui.GuiSolarPanels;
import com.denfop.invslot.InvSlotPanel;
import com.denfop.items.modules.EnumBaseType;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.utils.ModUtils;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.tile.IWrenchable;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.init.Localization;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySolarPanel extends TileEntityInventory implements IEnergySource, IHasGui,
        IWrenchable, IEnergyProvider, INetworkDataProvider, INetworkClientTileEntityEventListener,
        INetworkUpdateListener {


    public EnumSolarPanels solarpanels;
    public int tier;

    public InvSlotPanel inputslot;
    public boolean getmodulerf = false;

    public boolean personality = false;
    public int solarType;
    public String player = null;
    protected double tierPower;
    public EnumType type;
    public boolean work = true;
    public boolean work1 = true;
    public boolean work2 = true;
    public boolean charge;
    public int wireless = 0;
    public GenerationState active = GenerationState.NONE;
    protected boolean canRain;
    protected boolean hasSky;
    protected boolean addedToEnet;

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

    public TileEntitySolarPanel(
            final int tier, final double gDay,
            final double gOutput, final double gmaxStorage, EnumSolarPanels type
    ) {
        this.solarType = 0;
        this.genDay = gDay;
        this.genNight = gDay / 2;
        this.storage = 0;
        generating = 0;
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
    }

    public TileEntitySolarPanel(EnumSolarPanels solarpanels) {
        this(solarpanels.tier, solarpanels.genday, solarpanels.producing, solarpanels.maxstorage, solarpanels);

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
        double moonPhase = 1;
        coefficient_phase = experimental_generating();
        if (this.active == GenerationState.NIGHT || this.active == GenerationState.RAINNIGHT) {
            for (int i = 0; i < this.inputslot.size(); i++) {
                if (this.inputslot.get(i) != null && EnumModule.getFromID(this.inputslot
                        .get(i).getItemDamage()) != null && this.inputslot.get(i).getItem() instanceof ItemBaseModules) {
                    EnumModule module = EnumModule.getFromID(this.inputslot.get(i).getItemDamage());
                    EnumBaseType type = module.type;
                    if (type == EnumBaseType.MOON_LINSE) {
                        moonPhase = module.percent;
                        break;
                    }

                }
            }
        }


        this.generating *= coefpollution * coefficient_phase * moonPhase;
    }

    protected void onLoaded() {
        super.onLoaded();
        if (!this.world.isRemote) {
            this.addedToEnet = !MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.canRain = (this.world.getBiome(this.pos).canRain() || this.world.getBiome(this.pos).getRainfall() > 0.0F);
            this.hasSky = !this.world.provider.isNether();
            updateVisibility();
        }
    }

    private double experimental_generating() {
        double k = 0;
        float angle = this.getWorld().getCelestialAngle(1.0F) - 0.784690560F < 0 ? 1.0F - 0.784690560F : -0.784690560F;
        float celestialAngle = (this.getWorld().getCelestialAngle(1.0F) + angle) * 360.0F;

        celestialAngle %= 360;
        celestialAngle += 12;
        //TODO: end code GC
        if (celestialAngle <= 90) {
            k = celestialAngle / 90;
        } else if (celestialAngle > 90 && celestialAngle < 180) {
            celestialAngle -= 90;
            k = 1 - celestialAngle / 90;
        } else if (celestialAngle > 180 && celestialAngle < 270) {
            celestialAngle -= 180;
            k = celestialAngle / 90;
        } else if (celestialAngle > 270 && celestialAngle < 360) {
            celestialAngle -= 270;
            k = 1 - celestialAngle / 90;
        }

        double coef = 0;
        for (int i = 0; i < this.inputslot.size(); i++) {
            if (this.inputslot.get(i) != null && EnumModule.getFromID(this.inputslot
                    .get(i).getItemDamage()) != null && this.inputslot.get(i).getItem() instanceof ItemBaseModules) {
                EnumModule module = EnumModule.getFromID(this.inputslot.get(i).getItemDamage());
                EnumBaseType type = module.type;
                if (type == EnumBaseType.PHASE) {
                    coef = module.percent;
                    break;
                }

            }
        }
        return Math.max(coef, k);

    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

        this.storage = nbttagcompound.getDouble("storage");
        this.time = nbttagcompound.getInteger("time");
        this.time1 = nbttagcompound.getInteger("time1");
        this.time2 = nbttagcompound.getInteger("time2");
        this.production = nbttagcompound.getDouble("production");
        this.generating = nbttagcompound.getDouble("generating");
        this.tier = nbttagcompound.getInteger("tier");


        if (nbttagcompound.getInteger("solarType") != 0) {
            this.solarType = nbttagcompound.getInteger("solarType");
        }
        if (nbttagcompound.getBoolean("getmodulerf")) {
            this.getmodulerf = nbttagcompound.getBoolean("getmodulerf");
        }
        if (getmodulerf) {
            this.rf = nbttagcompound.getBoolean("rf");
        }
        if (!nbttagcompound.getString("player").isEmpty()) {
            this.player = nbttagcompound.getString("player");
        }
        this.wireless = nbttagcompound.getInteger("wireless");
        if (nbttagcompound.getBoolean("getmodulerf")) {
            this.storage2 = nbttagcompound.getDouble("storage2");
        }


    }


    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (getmodulerf) {
            nbttagcompound.setBoolean("getmodulerf", this.getmodulerf);
        }

        nbttagcompound.setInteger("time", this.time);
        nbttagcompound.setInteger("time1", this.time1);
        nbttagcompound.setInteger("time2", this.time2);
        nbttagcompound.setInteger("tier", this.tier);
        nbttagcompound.setDouble("production", this.production);
        if (player != null) {
            nbttagcompound.setString("player", player);
        }
        nbttagcompound.setInteger("wireless", this.wireless);
        nbttagcompound.setInteger("solarType", this.solarType);
        nbttagcompound.setDouble("storage", this.storage);
        if (this.getmodulerf) {
            nbttagcompound.setDouble("storage2", this.storage2);
            nbttagcompound.setBoolean("rf", this.rf);
        }
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
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            updateVisibility();

        }
        if (this.getWorld().provider.getWorldTime() % 20 == 0) {
            this.inputslot.getrfmodule();
            this.inputslot.time();
            this.inputslot.checkmodule();
        }
        this.inputslot.wirelessmodule();
        gainFuel();
        if (this.getmodulerf) {
            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos pos = new BlockPos(
                        this.pos.getX() + facing.getFrontOffsetX(),
                        this.pos.getY() + facing.getFrontOffsetY(),
                        this.pos.getZ() + facing.getFrontOffsetZ()
                );

                if (this.getWorld().getTileEntity(pos) == null) {
                    continue;
                }
                TileEntity tile = this.getWorld().getTileEntity(pos);

                if (!(tile instanceof TileEntitySolarPanel)) {

                    if (tile instanceof IEnergyReceiver) {
                        extractEnergy(facing, ((IEnergyReceiver) tile).receiveEnergy(facing.getOpposite(),
                                extractEnergy(facing, (int) this.storage2, true), false
                        ), false);
                    }
                }
            }
        }
        if (this.charge) {
            this.inputslot.charge();
        }
        if (this.charge && this.getmodulerf) {
            this.inputslot.rfcharge();
        }
        this.inputslot.personality();

        if (this.storage2 >= this.maxStorage2) {
            this.storage2 = this.maxStorage2;
        } else if (this.storage2 < 0) {
            this.storage2 = 0;
        }
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            this.solarType = this.inputslot.solartype();

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
        this.wetBiome = this.world.getBiome(this.pos).getRainfall() > 0.0F;
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


    public boolean getGuiState(String name) {

        return super.getGuiState(name);
    }


    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
        return true;
    }


    public void drawEnergy(double amount) {
        this.storage = (int) (this.storage - amount);
    }

    public int getSourceTier() {
        return Math.max(EnergyNet.instance.getTierFromPower(this.production), 2);
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
    public boolean canSetFacing(World world, BlockPos pos, EnumFacing enumFacing, EntityPlayer player) {
        if (!this.teBlock.allowWrenchRotating()) {
            return false;
        } else if (enumFacing == this.getFacing()) {
            return false;
        } else {
            return this.getSupportedFacings().contains(enumFacing);
        }
    }

    @Override
    public EnumFacing getFacing(World world, BlockPos blockPos) {
        return this.getFacing();
    }

    @Override
    public boolean setFacing(World world, BlockPos blockPos, EnumFacing enumFacing, EntityPlayer entityPlayer) {
        if (!this.canSetFacingWrench(enumFacing, entityPlayer)) {
            return false;
        } else {
            this.setFacing(enumFacing);
            return true;
        }
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

    public boolean rf = true;

    @Override
    public void onNetworkEvent(EntityPlayer player, int event) {
        this.rf = !this.rf;

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

    public void onUpgraded() {
        this.rerender();
    }

    public void markDirty1() {

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
