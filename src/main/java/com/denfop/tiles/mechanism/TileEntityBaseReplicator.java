package com.denfop.tiles.mechanism;

import com.denfop.api.gui.IType;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerReplicator;
import com.denfop.gui.GuiReplicator;
import com.denfop.invslot.InvSlotConsumableLiquid;
import com.denfop.invslot.InvSlotConsumableLiquidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityElectricMachine;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.recipe.IPatternStorage;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot;
import ic2.core.network.GuiSynced;
import ic2.core.profile.NotClassic;
import ic2.core.ref.FluidName;
import ic2.core.util.StackUtil;
import ic2.core.uu.UuIndex;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@NotClassic
public class TileEntityBaseReplicator extends TileEntityElectricMachine implements IHasGui, IUpgradableBlock, IType,
        INetworkClientTileEntityEventListener {

    private static final double uuPerTickBase = 1.0E-4D;
    private static final double euPerTickBase = 512.0D;
    private static final int defaultTier = 4;
    private static final int defaultEnergyStorage = 2000000;
    public final InvSlotConsumableLiquid fluidSlot;
    public final InvSlotOutput cellSlot;
    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;
    @GuiSynced
    public final FluidTank fluidTank;
    protected final Fluids fluids;
    private final double coef;
    public double uuProcessed = 0.0D;
    public ItemStack pattern;
    public int index;
    public int maxIndex;
    public double patternUu;
    public double patternEu;
    private double uuPerTick = 1.0E-4D;
    private double euPerTick = 512.0D;
    private double extraUuStored = 0.0D;
    private TileEntityBaseReplicator.Mode mode;

    public TileEntityBaseReplicator(double coef) {
        super(2000000, 4, 0);
        this.mode = TileEntityBaseReplicator.Mode.STOPPED;
        this.fluidSlot = new InvSlotConsumableLiquidByList(this, "fluid", InvSlot.Access.I, 1,
                InvSlot.InvSide.ANY, InvSlotConsumableLiquid.OpType.Drain, FluidName.uu_matter.getInstance()
        );
        this.cellSlot = new InvSlotOutput(this, "cell", 1);
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 16000, Fluids.fluidPredicate(FluidName.uu_matter.getInstance()));
        this.coef = coef;
    }

    private static int applyModifier(int base, int extra, double multiplier) {
        double ret = (double) Math.round(((double) base + (double) extra) * multiplier);
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.fluidTank.getFluidAmount() < this.fluidTank.getCapacity()) {
            this.gainFluid();
        }

        boolean newActive = false;
        if (this.mode != Mode.STOPPED && this.energy.getEnergy() >= this.euPerTick && this.pattern != null && this.outputSlot.canAdd(
                this.pattern)) {
            double uuRemaining = this.patternUu - this.uuProcessed;
            boolean finish;
            if (uuRemaining <= this.uuPerTick) {
                finish = true;
            } else {
                uuRemaining = this.uuPerTick;
                finish = false;
            }

            if (this.consumeUu(uuRemaining)) {
                newActive = true;
                this.energy.useEnergy(this.euPerTick);
                this.uuProcessed += uuRemaining;
                if (finish) {
                    this.uuProcessed = 0.0D;
                    if (this.mode == Mode.SINGLE) {
                        this.mode = Mode.STOPPED;
                    } else {
                        this.refreshInfo();
                    }

                    if (this.pattern != null) {
                        this.outputSlot.add(this.pattern);
                    }
                }
            }
        }

        this.setActive(newActive);
        this.upgradeSlot.tickNoMark();


    }

    private boolean consumeUu(double amount) {
        if (amount <= this.extraUuStored) {
            this.extraUuStored -= amount;
            return true;
        } else {
            amount -= this.extraUuStored;
            int toDrain = (int) Math.ceil(amount * 1000.0D);
            FluidStack drained = this.fluidTank.drainInternal(toDrain, false);
            if (drained != null && drained.getFluid() == FluidName.uu_matter.getInstance() && drained.amount == toDrain) {
                this.fluidTank.drainInternal(toDrain, true);
                amount -= (double) drained.amount / 1000.0D;
                if (amount < 0.0D) {
                    this.extraUuStored = -amount;
                } else {
                    this.extraUuStored = 0.0D;
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public void refreshInfo() {
        IPatternStorage storage = this.getPatternStorage();
        ItemStack oldPattern = this.pattern;
        if (storage == null) {
            this.pattern = null;
        } else {
            List<ItemStack> patterns = storage.getPatterns();
            if (this.index < 0 || this.index >= patterns.size()) {
                this.index = 0;
            }

            this.maxIndex = patterns.size();
            if (patterns.isEmpty()) {
                this.pattern = null;
            } else {
                this.pattern = patterns.get(this.index);
                this.patternUu = UuIndex.instance.getInBuckets(this.pattern) * this.coef;
                if (!StackUtil.checkItemEqualityStrict(this.pattern, oldPattern)) {
                    this.uuProcessed = 0.0D;
                    this.mode = Mode.STOPPED;
                }
            }
        }

        if (this.pattern == null) {
            this.uuProcessed = 0.0D;
            this.mode = Mode.STOPPED;
        }

    }

    public IPatternStorage getPatternStorage() {
        World world = this.getWorld();
        EnumFacing[] var2 = EnumFacing.VALUES;
        for (EnumFacing dir : var2) {
            TileEntity target = world.getTileEntity(this.pos.offset(dir));
            if (target instanceof IPatternStorage) {
                return (IPatternStorage) target;
            }
        }

        return null;
    }

    public void setOverclockRates() {
        this.upgradeSlot.onChanged();
        this.uuPerTick = 1.0E-4D / this.upgradeSlot.processTimeMultiplier;
        this.euPerTick = (512.0D + (double) this.upgradeSlot.extraEnergyDemand) * this.upgradeSlot.energyDemandMultiplier;
        this.energy.setSinkTier(applyModifier(4, this.upgradeSlot.extraTier, 1.0D));
        this.energy.setCapacity(applyModifier(
                2000000,
                this.upgradeSlot.extraEnergyStorage,
                this.upgradeSlot.energyStorageMultiplier
        ));
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiReplicator(new ContainerReplicator(player, this));

    }

    public ContainerBase<TileEntityBaseReplicator> getGuiContainer(EntityPlayer player) {
        return new ContainerReplicator(player, this);

    }

    protected void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
            this.refreshInfo();
        }

    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }

    }

    public boolean gainFluid() {
        return this.fluidSlot.processIntoTank(this.fluidTank, this.cellSlot);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.extraUuStored = nbt.getDouble("extraUuStored");
        this.uuProcessed = nbt.getDouble("uuProcessed");
        this.index = nbt.getInteger("index");
        int modeIdx = nbt.getInteger("mode");
        this.mode = modeIdx < Mode.values().length ? Mode.values()[modeIdx] : Mode.STOPPED;
        NBTTagCompound contentTag = nbt.getCompoundTag("pattern");
        this.pattern = new ItemStack(contentTag);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("extraUuStored", this.extraUuStored);
        nbt.setDouble("uuProcessed", this.uuProcessed);
        nbt.setInteger("index", this.index);
        nbt.setInteger("mode", this.mode.ordinal());
        if (this.pattern != null) {
            NBTTagCompound contentTag = new NBTTagCompound();
            this.pattern.writeToNBT(contentTag);
            nbt.setTag("pattern", contentTag);
        }

        return nbt;
    }

    public void onNetworkEvent(EntityPlayer player, int event) {
        switch (event) {
            case 0:
            case 1:
                if (this.mode == Mode.STOPPED) {
                    IPatternStorage storage = this.getPatternStorage();
                    if (storage != null) {
                        List<ItemStack> patterns = storage.getPatterns();
                        if (!patterns.isEmpty()) {
                            if (event == 0) {
                                if (this.index <= 0) {
                                    this.index = patterns.size() - 1;
                                } else {
                                    --this.index;
                                }
                            } else if (this.index >= patterns.size() - 1) {
                                this.index = 0;
                            } else {
                                ++this.index;
                            }

                            this.refreshInfo();
                        }
                    }
                }
            case 2:
            default:
                break;
            case 3:
                if (this.mode != Mode.STOPPED) {
                    this.uuProcessed = 0.0D;
                    this.mode = Mode.STOPPED;
                }
                break;
            case 4:
                if (this.pattern != null) {
                    this.mode = Mode.SINGLE;
                    if (player != null) {
                        IC2.achievements.issueAchievement(player, "replicateObject");
                    }
                }
                break;
            case 5:
                if (this.pattern != null) {
                    this.mode = Mode.CONTINUOUS;
                    if (player != null) {
                        IC2.achievements.issueAchievement(player, "replicateObject");
                    }
                }
        }

    }

    public void onGuiClosed(EntityPlayer player) {
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        return this.energy.useEnergy(amount);
    }

    public Mode getMode() {
        return this.mode;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.RedstoneSensitive,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing,
                UpgradableProperty.FluidConsuming
        );
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    public enum Mode {
        STOPPED,
        SINGLE,
        CONTINUOUS;

        Mode() {
        }
    }

}
