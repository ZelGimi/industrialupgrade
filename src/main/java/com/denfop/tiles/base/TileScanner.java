package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IPatternStorage;
import com.denfop.api.recipe.RecipeInfo;
import com.denfop.container.ContainerScanner;
import com.denfop.gui.GuiScanner;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotScannable;
import com.denfop.items.ItemCrystalMemory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class TileScanner extends TileElectricMachine implements IType,
        IUpdatableTileEvent {

    public final int maxprogress;
    public final InvSlotScannable inputSlot;
    public final InvSlot diskSlot;
    public int progress;
    public double patternUu;
    public double patternEu;
    public ItemStack pattern;
    public BaseMachineRecipe recipe;
    Map<BlockPos, IPatternStorage> iPatternStorageMap = new HashMap<>();
    List<IPatternStorage> iPatternStorageList = new ArrayList<>();
    private ItemStack currentStack;
    public TileScanner.State state;

    public TileScanner(int maxprogress) {
        super(512000, 14, 0);
        this.currentStack = ModUtils.emptyStack;
        this.pattern = ModUtils.emptyStack;
        this.progress = 0;
        this.maxprogress = maxprogress;
        this.state = TileScanner.State.IDLE;
        this.inputSlot = new InvSlotScannable(this, 1);
        this.diskSlot = new InvSlot(
                this,
                InvSlot.TypeItemSlot.INPUT_OUTPUT,
                1
        ) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.crystalMemory;
            }
        };
    }

    @Override
    public SoundEvent getSound() {
        return null;
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
        if (this.state != TileScanner.State.COMPLETED) {
            if (this.progress < maxprogress) {
                if (!this.inputSlot.isEmpty() && (ModUtils.isEmpty(this.currentStack) || ModUtils.checkItemEquality(
                        this.currentStack,
                        this.inputSlot.get()
                ))) {
                    if (this.getPatternStorage() == null && this.diskSlot.isEmpty()) {
                        this.state = TileScanner.State.NO_STORAGE;
                        this.reset();
                    } else if (this.energy.getEnergy() >= 256.0D) {
                        if (ModUtils.isEmpty(this.currentStack)) {
                            this.currentStack = ModUtils.setSize(this.inputSlot.get(), 1);
                        }


                        if (this.recipe == null) {
                            this.state = TileScanner.State.FAILED;
                        } else if (this.isPatternRecorded(this.pattern)) {
                            this.state = TileScanner.State.ALREADY_RECORDED;
                            this.reset();
                        } else {
                            this.state = TileScanner.State.SCANNING;
                            this.energy.useEnergy(256.0D);
                            ++this.progress;
                            if (this.progress >= maxprogress) {
                                this.refreshInfo();
                                this.pattern = this.currentStack.copy();
                                this.state = TileScanner.State.COMPLETED;
                                this.inputSlot.get().shrink(1);
                            }
                        }
                    } else {
                        this.state = TileScanner.State.NO_ENERGY;
                    }
                } else {
                    this.state = TileScanner.State.IDLE;
                    this.reset();
                }
            } else if (ModUtils.isEmpty(this.pattern)) {
                this.state = TileScanner.State.IDLE;
                this.progress = 0;
            }
        }

    }

    public void reset() {
        this.progress = 0;
        this.currentStack = ModUtils.emptyStack;
    }

    private boolean isPatternRecorded(ItemStack stack) {
        if (!this.diskSlot.isEmpty() && this.diskSlot.get().getItem() instanceof ItemCrystalMemory) {
            ItemStack crystalMemory = this.diskSlot.get();
            if (ModUtils.checkItemEquality(((ItemCrystalMemory) crystalMemory.getItem()).readItemStack(crystalMemory), stack)) {
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
                    this.state = TileScanner.State.TRANSFER_ERROR;
                    return;
                }
                if (!storage.addPattern(new RecipeInfo(this.pattern, this.patternUu))) {
                    this.state = TileScanner.State.TRANSFER_ERROR;
                    return;
                }
            }

        }
        this.reset();
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getInteger("progress");
        NBTTagCompound contentTag = nbttagcompound.getCompoundTag("currentStack");
        this.currentStack = new ItemStack(contentTag);
        contentTag = nbttagcompound.getCompoundTag("pattern");
        this.pattern = new ItemStack(contentTag);
        int stateIdx = nbttagcompound.getInteger("state");
        this.state = stateIdx < TileScanner.State.values().length
                ? TileScanner.State.values()[stateIdx]
                : TileScanner.State.IDLE;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("progress", this.progress);
        NBTTagCompound contentTag;
        if (!ModUtils.isEmpty(this.currentStack)) {
            contentTag = new NBTTagCompound();
            this.currentStack.writeToNBT(contentTag);
            nbt.setTag("currentStack", contentTag);
        }

        if (!ModUtils.isEmpty(this.pattern)) {
            contentTag = new NBTTagCompound();
            this.pattern.writeToNBT(contentTag);
            nbt.setTag("pattern", contentTag);
        }

        nbt.setInteger("state", this.state.ordinal());
        return nbt;
    }

    public ContainerScanner getGuiContainer(EntityPlayer player) {
        return new ContainerScanner(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScanner getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiScanner(new ContainerScanner(player, this));
    }

    public void onGuiClosed(EntityPlayer player) {
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
            if (this.diskSlot.get().getItem() instanceof ItemCrystalMemory) {
                ItemStack crystalMemory = this.diskSlot.get();
                ((ItemCrystalMemory) crystalMemory.getItem()).writecontentsTag(crystalMemory, stack);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void updateTileServer(EntityPlayer player, double event) {
        switch ((int) event) {
            case 0:
                this.reset();
                break;
            case 1:
                if (this.state == State.COMPLETED) {
                    this.record();
                    this.state = TileScanner.State.IDLE;
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
            this.recipe = Recipes.recipes.getRecipeOutput("replicator", false, this.inputSlot.get());
            this.pattern = this.inputSlot.get(0);
        }
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

    public TileScanner.State getState() {
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
