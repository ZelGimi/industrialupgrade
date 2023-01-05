package com.denfop.tiles.base;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.container.ContainerSinSolarPanel;
import com.denfop.gui.GuiSintezator;
import com.denfop.invslot.InvSlotSintezator;
import com.denfop.tiles.panels.entity.EnumType;
import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import com.denfop.tiles.panels.entity.TransferRFEnergy;
import com.denfop.tiles.panels.entity.WirelessTransfer;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySintezator extends TileEntityInventory implements IEnergySource, IHasGui,
        IEnergyProvider, INetworkDataProvider, INetworkClientTileEntityEventListener,
        INetworkUpdateListener {

    public final InvSlotSintezator inputslot;
    public final InvSlotSintezator inputslotA;
    public int solartype;
    public double generating;
    public double genDay;
    public double genNight;
    public boolean sunIsUp;
    public boolean skyIsVisible;
    public short facing;
    public boolean noSunWorld;
    public int machineTire;
    public boolean addedToEnergyNet;
    public double storage;
    public double production;
    public double maxStorage;

    public boolean rain = false;
    public boolean getmodulerf = false;
    public double progress;
    public double storage2;
    public double maxStorage2;
    public double progress2;
    public boolean wetBiome;
    public EnumType type;
    public TileEntitySolarPanel.GenerationState active;
    public List<WirelessTransfer> wirelessTransferList = new ArrayList<>();
    List<TransferRFEnergy> transferRFEnergyList = new ArrayList<>();

    public TileEntitySintezator() {
        this.facing = 2;
        this.storage = 0;
        this.storage2 = 0;
        this.sunIsUp = false;
        this.skyIsVisible = false;
        this.genNight = 0;
        this.genDay = 0;
        this.maxStorage = 0;
        this.maxStorage2 = 0;
        this.machineTire = 0;
        this.inputslot = new InvSlotSintezator(this, "input", 0, 9);
        this.inputslotA = new InvSlotSintezator(this, "input1", 1, 4);
        this.solartype = 0;
        this.type = EnumType.DEFAULT;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.blocksintezator);
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    protected boolean isNormalCube() {
        return false;
    }

    public void intialize() {
        this.noSunWorld = this.getWorld().provider.isNether();
        this.updateVisibility();
        this.wirelessTransferList.clear();
        this.inputslot.wirelessmodule();

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
            this.active = TileEntitySolarPanel.GenerationState.NONE;
        }
        if (this.sunIsUp && this.skyIsVisible) {
            if (!(this.rain)) {
                this.active = TileEntitySolarPanel.GenerationState.DAY;
            } else {
                this.active = TileEntitySolarPanel.GenerationState.RAINDAY;
            }

        }
        if (!this.sunIsUp && this.skyIsVisible) {
            if (!(rain)) {
                this.active = TileEntitySolarPanel.GenerationState.NIGHT;
            } else {
                this.active = TileEntitySolarPanel.GenerationState.RAINNIGHT;
            }
        }
        if (this.world.provider.getDimension() == 1) {
            this.active = TileEntitySolarPanel.GenerationState.END;
        }
        if (this.world.provider.getDimension() == -1) {
            this.active = TileEntitySolarPanel.GenerationState.NETHER;
        }

    }

    @Override
    public int extractEnergy(final EnumFacing enumFacing, final int i, final boolean b) {
        return extractEnergy((int) Math.min(this.production * Config.coefficientrf, i), b);
    }

    public int extractEnergy(int paramInt, boolean paramBoolean) {
        int i = (int) Math.min(this.storage2, Math.min(this.production * Config.coefficientrf, paramInt));
        if (!paramBoolean) {
            this.storage2 -= i;
        }
        return i;
    }

    @Override
    public int getEnergyStored(final EnumFacing enumFacing) {
        return (int) this.storage2;
    }

    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if (nbttagcompound.getDouble("storage2") > 0) {
            this.storage2 = nbttagcompound.getDouble("storage2");
        }
        if (nbttagcompound.getInteger("solarType") != 0) {
            this.solartype = nbttagcompound.getInteger("solarType");
        }

        if (nbttagcompound.getDouble("storage") > 0) {
            this.storage = nbttagcompound.getDouble("storage");
        }
        if (nbttagcompound.getDouble("maxStorage") > 0) {
            this.genDay = nbttagcompound.getDouble("genDay");
            this.genNight = nbttagcompound.getDouble("genNight");
            this.maxStorage = nbttagcompound.getDouble("maxStorage");
            this.production = nbttagcompound.getDouble("production");
            this.maxStorage2 = nbttagcompound.getDouble("maxStorage2");

        }
    }

    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (storage2 > 0) {
            nbttagcompound.setDouble("storage2", this.storage2);
        }
        nbttagcompound.setInteger("solarType", this.solartype);

        if (storage > 0) {
            nbttagcompound.setDouble("storage", this.storage);
        }
        if (maxStorage > 0) {
            nbttagcompound.setDouble("genDay", this.genDay);
            nbttagcompound.setDouble("genNight", this.genNight);
            nbttagcompound.setDouble("production", this.production);

            nbttagcompound.setDouble("maxStorage", this.maxStorage);
            if (maxStorage2 > 0) {
                nbttagcompound.setDouble("maxStorage2", this.maxStorage2);

            }
        }
        return nbttagcompound;

    }

    public double gaugeEnergyScaled(final float i) {

        return progress * i;

    }

    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
        intialize();
        this.markDirty();
    }

    public void onUnloaded() {
        if (IC2.platform.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }

        super.onUnloaded();
    }

    public double getOfferedEnergy() {
        return Math.min(this.storage, this.production);
    }

    public void drawEnergy(final double amount) {
        this.storage -= amount;
    }

    public int getSourceTier() {
        return this.machineTire;
    }

    public double gaugeEnergyScaled1(int i) {
        return progress2 * i;
    }

    @Override
    public int getMaxEnergyStored(final EnumFacing enumFacing) {
        return (int) this.maxStorage2;
    }

    public void updateTileEntityField() {
        if (this.world != null) {
            IC2.network.get(true).updateTileEntityField(this, "solartype");
        }

    }

    @Override
    public boolean canConnectEnergy(final EnumFacing enumFacing) {
        return true;
    }

    @Override
    public boolean emitsEnergyTo(final IEnergyAcceptor iEnergyAcceptor, final EnumFacing enumFacing) {
        return true;
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {

    }

    public ContainerBase<? extends TileEntitySintezator> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSinSolarPanel(entityPlayer, this);
    }

    public void updateEntityServer() {

        super.updateEntityServer();

        if (this.getmodulerf && this.storage2 > 0) {
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


        this.gainFuel();

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
        if (this.generating > 0D) {
            if (!this.getmodulerf) {
                if (((this.storage + this.generating)) <= (this.maxStorage)) {
                    this.storage += this.generating;
                } else {
                    this.storage = (this.maxStorage);
                }
            } else {
                if (((this.storage2 + this.generating * Config.coefficientrf)) <= (this.maxStorage2)) {
                    this.storage2 += this.generating * Config.coefficientrf;
                } else {
                    this.storage2 = (this.maxStorage2);
                }
            }
        }
        this.progress2 = Math.min(1, this.storage2 / this.maxStorage2);

        this.progress = Math.min(1, this.storage / this.maxStorage);
        if (this.storage < 0D) {
            this.storage = 0D;
        }
        if (this.maxStorage <= 0D) {
            this.storage = 0D;
        }
        if (this.storage2 < 0D) {
            this.storage2 = 0D;
        }
        if (this.maxStorage2 <= 0D) {
            this.storage2 = 0D;
        }
    }

    public void gainFuel() {


        if (this.getWorld().provider.getWorldTime() % 80 == 0) {
            this.updateVisibility();
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

    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("generating");
        ret.add("genDay");
        ret.add("genNight");
        ret.add("storage");
        ret.add("maxStorage");
        ret.add("production");
        ret.add("machineTire");
        ret.add("solartype");
        return ret;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiSintezator(new ContainerSinSolarPanel(entityPlayer, this));

    }

    @Override
    public void onGuiClosed(final EntityPlayer entityPlayer) {

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
                if (this.getWorld().provider.getDimension() == -1) {
                    return 3;
                }
                break;
            case END:
                if (this.getWorld().provider.getDimension() == 1) {
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
                if ((this.getWorld().isRaining() || this.getWorld().isThundering())) {
                    return 7;
                }
                break;

        }
        setType(EnumType.DEFAULT);
        return 0;
    }

}
