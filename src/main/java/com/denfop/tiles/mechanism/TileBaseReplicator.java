package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.api.Recipes;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.IPatternStorage;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.RecipeInfo;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.FluidName;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.Fluids;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerReplicator;
import com.denfop.gui.GuiReplicator;
import com.denfop.invslot.*;
import com.denfop.invslot.InventoryFluidByList;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TileBaseReplicator extends TileElectricMachine implements IUpgradableBlock, IType,
        IUpdatableTileEvent {

    public final InventoryFluid fluidSlot;
    public final InventoryOutput cellSlot;
    public final InventoryOutput outputSlot;
    public final InventoryUpgrade upgradeSlot;
    public final FluidTank fluidTank;
    protected final Fluids fluids;
    private final double coef;
    private final ComponentUpgrade componentUpgrades;
    private final ComponentUpgradeSlots componentUpgrade;
    public double uuProcessed = 0.0D;
    public RecipeInfo pattern;
    public int index;
    public int maxIndex;
    public double patternUu;
    public double patternEu;
    public TileBaseReplicator.Mode mode;
    Map<BlockPos, IPatternStorage> iPatternStorageMap = new HashMap<>();
    List<IPatternStorage> iPatternStorageList = new ArrayList<>();
    private double uuPerTick = 1.0E-4D;
    private double euPerTick = 512.0D;
    private double extraUuStored = 0.0D;
    private boolean instant = false;
    private boolean stack = false;

    public TileBaseReplicator(double coef) {
        super(2000000, 4, 0);
        this.mode = TileBaseReplicator.Mode.STOPPED;
        this.fluidSlot = new InventoryFluidByList(this, Inventory.TypeItemSlot.INPUT, 1,
                InventoryFluid.TypeFluidSlot.INPUT, FluidName.fluiduu_matter.getInstance()
        );
        this.cellSlot = new InventoryOutput(this, 1);
        this.outputSlot = new InventoryOutput(this, 1);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 16000, Fluids.fluidPredicate(FluidName.fluiduu_matter.getInstance()));
        this.coef = coef;
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, this.upgradeSlot) {
            @Override
            public void setOverclockRates(final InventoryUpgrade invSlotUpgrade) {
                super.setOverclockRates(invSlotUpgrade);
                ((TileBaseReplicator) this.getParent()).setOverclockRates();
            }
        });

    }

    private static int applyModifier(int base, int extra, double multiplier) {
        double ret = (double) Math.round(((double) base + (double) extra) * multiplier);
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    private static double applyModifier(int base, double extra, double multiplier) {
        return (double) Math.round(((double) base + extra) * multiplier);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            uuProcessed = (double) DecoderHandler.decode(customPacketBuffer);
            pattern = (RecipeInfo) DecoderHandler.decode(customPacketBuffer);
            mode = Mode.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            index = (int) DecoderHandler.decode(customPacketBuffer);
            maxIndex = (int) DecoderHandler.decode(customPacketBuffer);
            patternUu = (double) DecoderHandler.decode(customPacketBuffer);
            patternEu = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, uuProcessed);
            EncoderHandler.encode(packet, pattern);
            EncoderHandler.encode(packet, mode);
            EncoderHandler.encode(packet, index);
            EncoderHandler.encode(packet, maxIndex);
            EncoderHandler.encode(packet, patternUu);
            EncoderHandler.encode(packet, patternEu);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void onNeighborChange(final Block neighbor, final BlockPos neighborPos) {
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

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.fluidTank.getFluidAmount() < this.fluidTank.getCapacity()) {
            this.gainFluid();
        }
        if (this.componentUpgrades.isChange()) {
            this.instant = this.componentUpgrades.hasUpgrade(TypeUpgrade.INSTANT);
            this.stack = this.componentUpgrades.hasUpgrade(TypeUpgrade.STACK);
            this.componentUpgrades.setChange(false);
        }
        boolean newActive = false;
        double energyConsume = this.euPerTick;
        if (this.instant) {
            energyConsume *= 10;
        }
        if (this.mode != Mode.STOPPED && this.energy.getEnergy() >= energyConsume && this.pattern != null && this.outputSlot.canAdd(
                this.pattern.getStack())) {


            double uuRemaining = this.patternUu - this.uuProcessed;
            boolean finish;
            if (this.instant) {
                if (uuRemaining <= (this.fluidTank.getFluidAmount() * 1D)) {
                    uuRemaining = this.patternUu;
                    finish = true;
                } else {
                    if (uuRemaining <= this.uuPerTick) {
                        finish = true;
                    } else {
                        uuRemaining = this.uuPerTick;
                        finish = false;
                    }
                }
            } else {
                if (uuRemaining <= this.uuPerTick) {
                    finish = true;
                } else {
                    uuRemaining = this.uuPerTick;
                    finish = false;
                }
            }
            double size = 1;
            if (this.stack) {
                size = (this.fluidTank.getFluidAmount() / (this.patternUu * 1000));
                size = Math.min(size, this.pattern.getStack().getMaxStackSize());
                final int amount = this.outputSlot.get(0).isEmpty() ? 64 : this.outputSlot.get(0).getMaxStackSize() - this.outputSlot.get(0).getCount();
                size = Math.min(amount, size);
                if (size >= 1) {
                    uuRemaining = this.patternUu;
                    finish = true;
                }

            }
            if (this.consumeUu(uuRemaining * size)) {
                newActive = true;
                this.energy.useEnergy(energyConsume);
                this.uuProcessed += uuRemaining * size;
                if (finish) {
                    this.uuProcessed = 0.0D;
                    if (this.mode == Mode.SINGLE) {
                        this.mode = Mode.STOPPED;
                    } else {
                        this.refreshInfo();
                    }

                    if (this.pattern != null) {
                        for (int i = 0; i < size; i++) {
                            this.outputSlot.add(this.pattern.getStack());
                        }
                    }
                }
            }
        }

        this.setActive(newActive);
    }

    private boolean consumeUu(double amount) {
        if (amount <= this.extraUuStored) {
            this.extraUuStored -= amount;
            return true;
        } else {
            amount -= this.extraUuStored;
            int toDrain = (int) Math.ceil(amount * 1000.0D);
            FluidStack drained = this.fluidTank.drainInternal(toDrain, false);
            if (drained != null && drained.getFluid() == FluidName.fluiduu_matter.getInstance() && drained.amount == toDrain) {
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
                this.patternEu = this.patternUu * 1000000000;
                if (oldPattern == null || !ModUtils.checkItemEqualityStrict(this.pattern.getStack(), oldPattern.getStack())) {
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

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
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
        if (isPattern) {
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
        } else {
            nbt.setBoolean("isPattern", false);
        }

        return nbt;
    }

    public void updateTileServer(EntityPlayer player, double event) {
        switch ((int) event) {
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

                }
                break;
            case 5:
                if (this.pattern != null) {
                    this.mode = Mode.CONTINUOUS;

                }
        }

    }

    public void onGuiClosed(EntityPlayer player) {
    }

    public Mode getMode() {
        return this.mode;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.FluidInput
        );
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    public enum Mode {
        STOPPED,
        SINGLE,
        CONTINUOUS;

        Mode() {
        }
    }

}
