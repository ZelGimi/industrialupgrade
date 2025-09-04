package com.denfop.blockentity.mechanism;

import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.IPatternStorage;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.RecipeInfo;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.api.widget.IType;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.FluidName;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuReplicator;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenReplicator;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.*;

public class BlockEntityBaseReplicator extends BlockEntityElectricMachine implements BlockEntityUpgrade, IType,
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
    public BlockEntityBaseReplicator.Mode mode;
    Map<BlockPos, IPatternStorage> iPatternStorageMap = new HashMap<>();
    List<IPatternStorage> iPatternStorageList = new ArrayList<>();
    private double uuPerTick = 1.0E-4D;
    private double euPerTick = 512.0D;
    private double extraUuStored = 0.0D;
    private boolean instant = false;
    private boolean stack = false;

    public BlockEntityBaseReplicator(double coef, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(2000000, 4, 0, block, pos, state);
        this.mode = BlockEntityBaseReplicator.Mode.STOPPED;
        this.fluidSlot = new InventoryFluidByList(this, Inventory.TypeItemSlot.INPUT, 1,
                InventoryFluid.TypeFluidSlot.INPUT, FluidName.fluiduu_matter.getInstance().get()
        );
        this.cellSlot = new InventoryOutput(this, 1);
        this.outputSlot = new InventoryOutput(this, 1);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 16000, Fluids.fluidPredicate(FluidName.fluiduu_matter.getInstance().get()));
        this.coef = coef;
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, this.upgradeSlot) {
            @Override
            public void setOverclockRates(final InventoryUpgrade invSlotUpgrade) {
                super.setOverclockRates(invSlotUpgrade);
                ((BlockEntityBaseReplicator) this.getParent()).setOverclockRates();
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
    public void onNeighborChange(final BlockState neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        final BlockEntity tile = this.getWorld().getBlockEntity(neighborPos);

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
                        if (size > 1)
                            for (int i = 0; i < size - 1; i++) {
                                this.outputSlot.add(this.pattern.getStack());
                            }
                        else {
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
            FluidStack drained = this.fluidTank.drain(toDrain, IFluidHandler.FluidAction.SIMULATE);
            if (!drained.isEmpty() && drained.getFluid() == FluidName.fluiduu_matter.getInstance().get() && drained.getAmount() == toDrain) {
                this.fluidTank.drain(toDrain, IFluidHandler.FluidAction.EXECUTE);
                amount -= (double) drained.getAmount() / 1000.0D;
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

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player player, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenReplicator((ContainerMenuReplicator) isAdmin);

    }

    public ContainerMenuReplicator getGuiContainer(Player player) {
        return new ContainerMenuReplicator(player, this);

    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getLevel().isClientSide) {
            this.setOverclockRates();
            for (Direction facing : Direction.values()) {
                final BlockPos neighborPos = pos.offset(facing.getNormal());
                final BlockEntity tile = this.getWorld().getBlockEntity(neighborPos);
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

    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.extraUuStored = nbt.getDouble("extraUuStored");
        this.uuProcessed = nbt.getDouble("uuProcessed");
        this.index = nbt.getInt("index");
        int modeIdx = nbt.getInt("mode");
        this.mode = modeIdx < Mode.values().length ? Mode.values()[modeIdx] : Mode.STOPPED;
        boolean isPattern = nbt.getBoolean("isPattern");
        if (isPattern) {
            CompoundTag contentTag = nbt.getCompound("pattern");
            final ItemStack stack = ItemStack.of(contentTag);
            try {
                this.pattern = new RecipeInfo(stack, Recipes.recipes
                        .getRecipeOutput("replicator", false, stack)
                        .getOutput().metadata.getDouble(
                                "matter"));
            }catch (Exception e){
                if (nbt.contains("amount")){
                    this.pattern = new RecipeInfo(stack, nbt.getDouble("amount"));
                }else{
                    pattern = null;
                }
            }
        }
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putDouble("extraUuStored", this.extraUuStored);
        nbt.putDouble("uuProcessed", this.uuProcessed);
        nbt.putInt("index", this.index);
        nbt.putInt("mode", this.mode.ordinal());
        if (this.pattern != null) {
            nbt.putBoolean("isPattern", true);
            CompoundTag contentTag = new CompoundTag();
            this.pattern.getStack().save(contentTag);
            nbt.put("pattern", contentTag);
            nbt.putDouble("amount",pattern.getCol());
        } else {
            nbt.putBoolean("isPattern", false);
        }

        return nbt;
    }

    public void updateTileServer(Player player, double event) {
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


    public Mode getMode() {
        return this.mode;
    }

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(
                EnumBlockEntityUpgrade.Processing,
                EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage,
                EnumBlockEntityUpgrade.ItemExtract,
                EnumBlockEntityUpgrade.FluidInput
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
