package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IPatternStorage;
import com.denfop.api.recipe.RecipeInfo;
import com.denfop.api.widget.IType;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuScanner;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryScannable;
import com.denfop.items.ItemCrystalMemory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenScanner;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.*;

public abstract class BlockEntityScanner extends BlockEntityElectricMachine implements IType,
        IUpdatableTileEvent {

    public final int maxprogress;
    public final InventoryScannable inputSlot;
    public final Inventory diskSlot;
    public int progress;
    public double patternUu;
    public double patternEu;
    public ItemStack pattern;
    public BaseMachineRecipe recipe;
    public BlockEntityScanner.State state;
    Map<BlockPos, IPatternStorage> iPatternStorageMap = new HashMap<>();
    List<IPatternStorage> iPatternStorageList = new ArrayList<>();
    private ItemStack currentStack;

    public BlockEntityScanner(int maxprogress, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(512000, 14, 0, block, pos, state);
        this.currentStack = ModUtils.emptyStack;
        this.pattern = ModUtils.emptyStack;
        this.progress = 0;
        this.maxprogress = maxprogress;
        this.state = BlockEntityScanner.State.IDLE;
        this.inputSlot = new InventoryScannable(this, 1);
        this.diskSlot = new Inventory(
                this,
                Inventory.TypeItemSlot.INPUT_OUTPUT,
                1
        ) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.crystalMemory.getItem();
            }
        };
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    @Override
    public void onNeighborChange(BlockState neighbor, BlockPos neighborPos) {
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
    }


    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            state = State.values()[(int) DecoderHandler.decode(customPacketBuffer)];
            progress = (int) DecoderHandler.decode(customPacketBuffer);
            patternEu = (double) DecoderHandler.decode(customPacketBuffer);
            patternUu = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, state);
            EncoderHandler.encode(packet, progress);
            EncoderHandler.encode(packet, patternEu);
            EncoderHandler.encode(packet, patternUu);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.state != BlockEntityScanner.State.COMPLETED) {
            if (this.progress < maxprogress) {
                if (!this.inputSlot.isEmpty() && (ModUtils.isEmpty(this.currentStack) || ModUtils.checkItemEquality(
                        this.currentStack,
                        this.inputSlot.get(0)
                ))) {
                    if (this.getPatternStorage() == null && this.diskSlot.isEmpty()) {
                        this.state = BlockEntityScanner.State.NO_STORAGE;
                        this.reset();
                    } else if (this.energy.getEnergy() >= 256.0D) {
                        if (ModUtils.isEmpty(this.currentStack)) {
                            this.currentStack = ModUtils.setSize(this.inputSlot.get(0), 1);
                        }


                        if (this.recipe == null) {
                            this.state = BlockEntityScanner.State.FAILED;
                        } else if (this.isPatternRecorded(this.pattern)) {
                            this.state = BlockEntityScanner.State.ALREADY_RECORDED;
                            this.reset();
                        } else {
                            this.state = BlockEntityScanner.State.SCANNING;
                            this.energy.useEnergy(256.0D);
                            ++this.progress;
                            if (this.progress >= maxprogress) {
                                this.refreshInfo();
                                this.pattern = this.currentStack.copy();
                                this.state = BlockEntityScanner.State.COMPLETED;
                                this.inputSlot.get(0).shrink(1);
                            }
                        }
                    } else {
                        this.state = BlockEntityScanner.State.NO_ENERGY;
                    }
                } else {
                    this.state = BlockEntityScanner.State.IDLE;
                    this.reset();

                }
            } else if (ModUtils.isEmpty(this.pattern)) {
                this.state = BlockEntityScanner.State.IDLE;
                this.progress = 0;

            }
        }

    }

    public void reset() {
        this.progress = 0;
        this.currentStack = ModUtils.emptyStack;
    }

    private boolean isPatternRecorded(ItemStack stack) {
        if (!this.diskSlot.isEmpty() && this.diskSlot.get(0).getItem() instanceof ItemCrystalMemory) {
            ItemStack crystalMemory = this.diskSlot.get(0);
            if (ModUtils.checkItemEquality(((ItemCrystalMemory) crystalMemory.getItem()).readItemStack(level.registryAccess(), crystalMemory), stack)) {
                return true;
            }
        }

        IPatternStorage storage = this.getPatternStorage();
        if (storage == null) {
            return false;
        } else {
            Iterator<RecipeInfo> var3 = storage.getPatterns().iterator();

            RecipeInfo stored;
            do {
                if (!var3.hasNext()) {
                    return false;
                }

                stored = var3.next();
            } while (!ModUtils.checkItemEquality(stored.getStack(), stack));

            return true;
        }
    }

    private void record() {
        if (!ModUtils.isEmpty(this.pattern) && this.patternUu != 1.0D / 0.0) {
            if (!this.savetoDisk(this.pattern)) {
                IPatternStorage storage = this.getPatternStorage();
                if (storage == null) {
                    this.state = BlockEntityScanner.State.TRANSFER_ERROR;
                    return;
                }
                if (!storage.addPattern(new RecipeInfo(this.pattern, this.patternUu))) {
                    this.state = BlockEntityScanner.State.TRANSFER_ERROR;
                    return;
                }
            }

        }
        this.reset();
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getInt("progress");
        CompoundTag contentTag = nbttagcompound.getCompound("currentStack");
        this.currentStack = ItemStack.parseOptional(provider, contentTag);
        contentTag = nbttagcompound.getCompound("pattern");
        this.pattern = ItemStack.parseOptional(provider, contentTag);
        int stateIdx = nbttagcompound.getInt("state");
        this.state = stateIdx < BlockEntityScanner.State.values().length
                ? BlockEntityScanner.State.values()[stateIdx]
                : BlockEntityScanner.State.IDLE;
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putInt("progress", this.progress);
        CompoundTag contentTag;
        if (!ModUtils.isEmpty(this.currentStack)) {
            contentTag = new CompoundTag();
            this.currentStack.save(this.provider, contentTag);
            nbt.put("currentStack", contentTag);
        }

        if (!ModUtils.isEmpty(this.pattern)) {
            contentTag = new CompoundTag();
            this.pattern.save(this.provider, contentTag);
            nbt.put("pattern", contentTag);
        }

        nbt.putInt("state", this.state.ordinal());
        return nbt;
    }

    public ContainerMenuScanner getGuiContainer(Player player) {
        return new ContainerMenuScanner(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player player, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenScanner((ContainerMenuScanner) isAdmin);
    }


    public IPatternStorage getPatternStorage() {
        if (this.iPatternStorageList.isEmpty()) {
            return null;
        } else {
            return this.iPatternStorageList.get(0);
        }
    }

    public boolean savetoDisk(ItemStack stack) {
        if (!this.diskSlot.isEmpty() && stack != null) {
            if (this.diskSlot.get(0).getItem() instanceof ItemCrystalMemory) {
                ItemStack crystalMemory = this.diskSlot.get(0);
                ((ItemCrystalMemory) crystalMemory.getItem()).writecontentsTag(this.registryAccess(), crystalMemory, stack);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void updateTileServer(Player player, double event) {
        if (!this.iPatternStorageList.isEmpty() || !this.diskSlot.isEmpty())
            switch ((int) event) {
                case 0:
                    this.reset();
                    break;
                case 1:
                    if (this.state == State.COMPLETED) {
                        this.record();
                        this.state = BlockEntityScanner.State.IDLE;
                    }
            }

    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.inputSlot.isEmpty()) {
            if (pattern.isEmpty()) {
                this.recipe = null;
            } else {
                this.recipe = Recipes.recipes.getRecipeOutput("replicator", false, this.pattern);
            }
        } else {
            this.recipe = Recipes.recipes.getRecipeOutput("replicator", false, this.inputSlot.get(0));
            this.pattern = this.inputSlot.get(0);
        }
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

    private void refreshInfo() {
        if (!ModUtils.isEmpty(this.pattern)) {
            this.patternUu = this.recipe.getOutput().metadata.getDouble("matter");
            this.patternEu = this.patternUu * 1000000000;
        }

    }

    public int getPercentageDone() {
        return 100 * this.progress / this.maxprogress;
    }

    public int getSubPercentageDoneScaled(int width) {
        return width * (100 * this.progress % this.maxprogress) / this.maxprogress;
    }

    public boolean isDone() {
        return this.progress >= 3300;
    }

    public BlockEntityScanner.State getState() {
        return this.state;
    }

    public enum State {
        IDLE,
        SCANNING,
        COMPLETED,
        FAILED,
        NO_STORAGE,
        NO_ENERGY,
        TRANSFER_ERROR,
        ALREADY_RECORDED;

        State() {
        }
    }

}
