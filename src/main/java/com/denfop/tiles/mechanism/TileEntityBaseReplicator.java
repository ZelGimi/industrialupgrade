package com.denfop.tiles.mechanism;

import com.denfop.api.Recipes;
import com.denfop.api.gui.IType;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.recipe.IPatternStorage;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.RecipeInfo;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerReplicator;
import com.denfop.gui.GuiReplicator;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotConsumableLiquid;
import com.denfop.invslot.InvSlotConsumableLiquidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityElectricMachine;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.profile.NotClassic;
import ic2.core.ref.FluidName;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NotClassic
public class TileEntityBaseReplicator extends TileEntityElectricMachine implements IHasGui, IUpgradableBlock, IType,
        INetworkClientTileEntityEventListener {

    public final InvSlotConsumableLiquid fluidSlot;
    public final InvSlotOutput cellSlot;
    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final FluidTank fluidTank;
    protected final Fluids fluids;
    private final double coef;
    public double uuProcessed = 0.0D;
    public RecipeInfo pattern;
    public int index;
    public int maxIndex;
    public double patternUu;
    public double patternEu;
    public TileEntityBaseReplicator.Mode mode;
    Map<BlockPos, IPatternStorage> iPatternStorageMap = new HashMap<>();
    List<IPatternStorage> iPatternStorageList = new ArrayList<>();
    private double uuPerTick = 1.0E-4D;
    private double euPerTick = 512.0D;
    private double extraUuStored = 0.0D;

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

    private static double applyModifier(int base, double extra, double multiplier) {
        return (double) Math.round(((double) base + extra) * multiplier);
    }

    @Override
    protected void onNeighborChange(final Block neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        final TileEntity tile = this.getWorld().getTileEntity(neighborPos);

        if (tile instanceof IPatternStorage) {
            if (!this.iPatternStorageList.contains((IPatternStorage) tile)) {
                this.iPatternStorageList.add((IPatternStorage) tile);
                this.iPatternStorageMap.put(neighborPos, (IPatternStorage) tile);
            }
        } else if (tile == null) {
            IPatternStorage storage = iPatternStorageMap.get(neighborPos);
            if (storage != null) {
                this.iPatternStorageList.remove(storage);
                this.iPatternStorageMap.remove(neighborPos, storage);
            }
        }
        refreshInfo();
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.fluidTank.getFluidAmount() < this.fluidTank.getCapacity()) {
            this.gainFluid();
        }

        boolean newActive = false;
        if (this.mode != Mode.STOPPED && this.energy.getEnergy() >= this.euPerTick && this.pattern != null && this.outputSlot.canAdd(
                this.pattern.getStack())) {
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
                        this.outputSlot.add(this.pattern.getStack());
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
        RecipeInfo oldPattern = this.pattern;
        if (storage == null) {
            this.pattern = null;
        } else {
            List<RecipeInfo> patterns = storage.getPatterns();
            if (this.index < 0 || this.index >= patterns.size()) {
                this.index = 0;
            }

            this.maxIndex = patterns.size();
            if (patterns.isEmpty()) {
                this.pattern = null;
            } else {
                this.pattern = patterns.get(this.index);
                this.patternUu = pattern.getCol() * this.coef;
                if (oldPattern == null || !StackUtil.checkItemEqualityStrict(this.pattern.getStack(), oldPattern.getStack())) {
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
        if (this.iPatternStorageList.isEmpty()) {
            return null;
        } else {
            return this.iPatternStorageList.get(0);
        }
    }

    public void setOverclockRates() {
        this.upgradeSlot.onChanged();
        this.uuPerTick = 1.0E-4D / this.upgradeSlot.processTimeMultiplier;
        this.euPerTick = (512.0D + this.upgradeSlot.extraEnergyDemand) * this.upgradeSlot.energyDemandMultiplier;
        this.energy.setSinkTier(applyModifier(4, this.upgradeSlot.extraTier, 1.0D));
        this.energy.setCapacity(applyModifier(
                2000000,
                this.upgradeSlot.extraEnergyStorage,
                this.upgradeSlot.energyStorageMultiplier
        ));
    }

    @SideOnly(Side.CLIENT)
    public GuiReplicator getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiReplicator(new ContainerReplicator(player, this));

    }

    public ContainerReplicator getGuiContainer(EntityPlayer player) {
        return new ContainerReplicator(player, this);

    }

    protected void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
            for (EnumFacing facing : EnumFacing.VALUES) {
                final BlockPos neighborPos = pos.offset(facing);
                final TileEntity tile = this.getWorld().getTileEntity(neighborPos);
                if (tile instanceof IPatternStorage) {
                    if (!this.iPatternStorageList.contains((IPatternStorage) tile)) {
                        this.iPatternStorageList.add((IPatternStorage) tile);
                        this.iPatternStorageMap.put(neighborPos, (IPatternStorage) tile);
                    }
                } else if (tile == null) {
                    IPatternStorage storage = iPatternStorageMap.get(neighborPos);
                    if (storage != null) {
                        this.iPatternStorageList.remove(storage);
                        this.iPatternStorageMap.remove(neighborPos, storage);
                    }
                }
            }
            this.refreshInfo();
        }

    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }

    }

    public void gainFluid() {
        this.fluidSlot.processIntoTank(this.fluidTank, this.cellSlot);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.extraUuStored = nbt.getDouble("extraUuStored");
        this.uuProcessed = nbt.getDouble("uuProcessed");
        this.index = nbt.getInteger("index");
        int modeIdx = nbt.getInteger("mode");
        this.mode = modeIdx < Mode.values().length ? Mode.values()[modeIdx] : Mode.STOPPED;
        boolean isPattern = nbt.getBoolean("isPattern");
        if(isPattern) {
            NBTTagCompound contentTag = nbt.getCompoundTag("pattern");
            final ItemStack stack = new ItemStack(contentTag);
            this.pattern = new RecipeInfo(stack, Recipes.recipes
                    .getRecipeOutput("replicator", false, stack)
                    .getOutput().metadata.getDouble(
                            "matter"));
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("extraUuStored", this.extraUuStored);
        nbt.setDouble("uuProcessed", this.uuProcessed);
        nbt.setInteger("index", this.index);
        nbt.setInteger("mode", this.mode.ordinal());
        if (this.pattern != null) {
            nbt.setBoolean("isPattern", true);
            NBTTagCompound contentTag = new NBTTagCompound();
            this.pattern.getStack().writeToNBT(contentTag);
            nbt.setTag("pattern", contentTag);
        }else
            nbt.setBoolean("isPattern", false);

        return nbt;
    }

    public void onNetworkEvent(EntityPlayer player, int event) {
        switch (event) {
            case 0:
            case 1:
                if (this.mode == Mode.STOPPED) {
                    IPatternStorage storage = this.getPatternStorage();
                    if (storage != null) {
                        List<RecipeInfo> patterns = storage.getPatterns();
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
