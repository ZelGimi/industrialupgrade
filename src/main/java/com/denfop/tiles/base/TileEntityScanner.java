package com.denfop.tiles.base;

import com.denfop.api.Recipes;
import com.denfop.api.gui.IType;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IPatternStorage;
import com.denfop.api.recipe.RecipeInfo;
import com.denfop.container.ContainerScanner;
import com.denfop.gui.GuiScanner;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotConsumable;
import com.denfop.invslot.InvSlotConsumableId;
import com.denfop.invslot.InvSlotScannable;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.item.ItemCrystalMemory;
import ic2.core.profile.NotClassic;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@NotClassic
public abstract class TileEntityScanner extends TileEntityElectricMachine implements IHasGui, IType,
        INetworkClientTileEntityEventListener {

    public final int maxprogress;
    public final InvSlotConsumable inputSlot;
    public final InvSlot diskSlot;
    public int progress;
    public double patternUu;
    public double patternEu;
    public ItemStack pattern;
    public BaseMachineRecipe recipe;
    Map<BlockPos, IPatternStorage> iPatternStorageMap = new HashMap<>();
    List<IPatternStorage> iPatternStorageList = new ArrayList<>();
    private ItemStack currentStack;
    private TileEntityScanner.State state;

    public TileEntityScanner(int maxprogress) {
        super(512000, 4, 0);
        this.currentStack = StackUtil.emptyStack;
        this.pattern = StackUtil.emptyStack;
        this.progress = 0;
        this.maxprogress = maxprogress;
        this.state = TileEntityScanner.State.IDLE;
        this.inputSlot = new InvSlotScannable(this, "input", 1);
        this.diskSlot = new InvSlotConsumableId(
                this,
                "disk",
                InvSlot.Access.IO,
                1,
                InvSlot.InvSide.ANY,
                ItemName.crystal_memory.getInstance()
        );
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
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.state != TileEntityScanner.State.COMPLETED) {
            if (this.progress < maxprogress) {
                if (!this.inputSlot.isEmpty() && (StackUtil.isEmpty(this.currentStack) || StackUtil.checkItemEquality(
                        this.currentStack,
                        this.inputSlot.get()
                ))) {
                    if (this.getPatternStorage() == null && this.diskSlot.isEmpty()) {
                        this.state = TileEntityScanner.State.NO_STORAGE;
                        this.reset();
                    } else if (this.energy.getEnergy() >= 256.0D) {
                        if (StackUtil.isEmpty(this.currentStack)) {
                            this.currentStack = StackUtil.copyWithSize(this.inputSlot.get(), 1);
                        }


                        if (this.recipe == null) {
                            this.state = TileEntityScanner.State.FAILED;
                        } else if (this.isPatternRecorded(this.pattern)) {
                            this.state = TileEntityScanner.State.ALREADY_RECORDED;
                            this.reset();
                        } else {
                            this.state = TileEntityScanner.State.SCANNING;
                            this.energy.useEnergy(256.0D);
                            ++this.progress;
                            if (this.progress >= maxprogress) {
                                this.refreshInfo();
                                this.pattern = this.currentStack.copy();
                                this.state = TileEntityScanner.State.COMPLETED;
                                this.inputSlot.consume(1, false, true);
                            }
                        }
                    } else {
                        this.state = TileEntityScanner.State.NO_ENERGY;
                    }
                } else {
                    this.state = TileEntityScanner.State.IDLE;
                    this.reset();
                }
            } else if (StackUtil.isEmpty(this.pattern)) {
                this.state = TileEntityScanner.State.IDLE;
                this.progress = 0;
            }
        }

    }

    public void reset() {
        this.progress = 0;
        this.currentStack = StackUtil.emptyStack;
    }

    private boolean isPatternRecorded(ItemStack stack) {
        if (!this.diskSlot.isEmpty() && this.diskSlot.get().getItem() instanceof ItemCrystalMemory) {
            ItemStack crystalMemory = this.diskSlot.get();
            if (StackUtil.checkItemEquality(((ItemCrystalMemory) crystalMemory.getItem()).readItemStack(crystalMemory), stack)) {
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
            } while (!StackUtil.checkItemEquality(stored.getStack(), stack));

            return true;
        }
    }

    private void record() {
        if (!StackUtil.isEmpty(this.pattern) && this.patternUu != 1.0D / 0.0) {
            if (!this.savetoDisk(this.pattern)) {
                IPatternStorage storage = this.getPatternStorage();
                if (storage == null) {
                    this.state = TileEntityScanner.State.TRANSFER_ERROR;
                    return;
                }
                if (!storage.addPattern(new RecipeInfo(this.pattern, this.patternUu))) {
                    this.state = TileEntityScanner.State.TRANSFER_ERROR;
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
        this.state = stateIdx < TileEntityScanner.State.values().length
                ? TileEntityScanner.State.values()[stateIdx]
                : TileEntityScanner.State.IDLE;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("progress", this.progress);
        NBTTagCompound contentTag;
        if (!StackUtil.isEmpty(this.currentStack)) {
            contentTag = new NBTTagCompound();
            this.currentStack.writeToNBT(contentTag);
            nbt.setTag("currentStack", contentTag);
        }

        if (!StackUtil.isEmpty(this.pattern)) {
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

    public void onNetworkEvent(EntityPlayer player, int event) {
        switch (event) {
            case 0:
                this.reset();
                break;
            case 1:
                if (this.state == State.COMPLETED) {
                    this.record();
                    this.state = TileEntityScanner.State.IDLE;
                }
        }

    }

    @Override
    protected void onLoaded() {
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
        if (!StackUtil.isEmpty(this.pattern)) {
            this.patternUu = this.recipe.getOutput().metadata.getDouble("matter");
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

    public TileEntityScanner.State getState() {
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
