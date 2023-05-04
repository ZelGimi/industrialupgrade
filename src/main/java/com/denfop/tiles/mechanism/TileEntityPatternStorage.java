package com.denfop.tiles.mechanism;


import com.denfop.api.Recipes;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.recipe.IPatternStorage;
import com.denfop.api.recipe.RecipeInfo;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerPatternStorage;
import com.denfop.gui.GuiPatternStorage;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotConsumableId;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.item.ItemCrystalMemory;
import ic2.core.profile.NotClassic;
import ic2.core.ref.ItemName;
import ic2.core.ref.TeBlock.DefaultDrop;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@NotClassic
public class TileEntityPatternStorage extends TileEntityInventory implements IHasGui, INetworkClientTileEntityEventListener,
        IPatternStorage {

    public final InvSlotConsumableId diskSlot;
    private final List<RecipeInfo> patterns = new ArrayList<>();
    public int index = 0;
    public int maxIndex;
    public RecipeInfo pattern;
    public double patternUu;
    public double patternEu;

    public TileEntityPatternStorage() {
        this.diskSlot = new InvSlotConsumableId(
                this,
                "SaveSlot",
                InvSlot.Access.IO,
                1,
                InvSlot.InvSide.ANY,
                ItemName.crystal_memory.getInstance()
        );
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.readContents(nbttagcompound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.writeContentsAsNbtList(nbt);
        return nbt;
    }

    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (!this.getWorld().isRemote) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            this.readContents(nbt);
        }

    }

    protected ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (wrench || this.teBlock.getDefaultDrop() == DefaultDrop.Self) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(drop);
            this.writeContentsAsNbtList(nbt);
        }

        return drop;
    }

    public void readContents(NBTTagCompound nbt) {
        NBTTagList patternList = nbt.getTagList("patterns", 10);

        for (int i = 0; i < patternList.tagCount(); ++i) {
            NBTTagCompound contentTag = patternList.getCompoundTagAt(i);
            ItemStack Item = new ItemStack(contentTag);
            this.addPattern(new RecipeInfo(Item, Recipes.recipes
                    .getRecipeOutput("replicator", false, Item)
                    .getOutput().metadata.getDouble(
                            "matter")));
        }

        this.refreshInfo();
    }

    private void writeContentsAsNbtList(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();

        for (final RecipeInfo stack : this.patterns) {
            NBTTagCompound contentTag = new NBTTagCompound();
            stack.getStack().writeToNBT(contentTag);
            list.appendTag(contentTag);
        }

        nbt.setTag("patterns", list);
    }

    public ContainerBase<TileEntityPatternStorage> getGuiContainer(EntityPlayer player) {
        return new ContainerPatternStorage(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiPatternStorage(new ContainerPatternStorage(player, this));
    }

    public void onNetworkEvent(EntityPlayer player, int event) {
        ItemStack crystalMemory;
        switch (event) {
            case 0:
                if (!this.patterns.isEmpty()) {
                    if (this.index <= 0) {
                        this.index = this.patterns.size() - 1;
                    } else {
                        --this.index;
                    }

                    this.refreshInfo();
                }
                break;
            case 1:
                if (!this.patterns.isEmpty()) {
                    if (this.index >= this.patterns.size() - 1) {
                        this.index = 0;
                    } else {
                        ++this.index;
                    }

                    this.refreshInfo();
                }
                break;
            case 2:
                if (this.index >= 0 && this.index < this.patterns.size() && !this.diskSlot.isEmpty()) {
                    crystalMemory = this.diskSlot.get();
                    if (crystalMemory.getItem() instanceof ItemCrystalMemory) {
                        ((ItemCrystalMemory) crystalMemory.getItem()).writecontentsTag(
                                crystalMemory,
                                this.patterns.get(this.index).getStack()
                        );
                    }
                }
                break;
            case 3:
                if (!this.diskSlot.isEmpty()) {
                    crystalMemory = this.diskSlot.get();
                    if (crystalMemory.getItem() instanceof ItemCrystalMemory) {
                        ItemStack record = ((ItemCrystalMemory) crystalMemory.getItem()).readItemStack(crystalMemory);
                        if (record != null) {
                            this.addPattern(new RecipeInfo(record, Recipes.recipes
                                    .getRecipeOutput("replicator", false, record)
                                    .getOutput().metadata.getDouble(
                                            "matter")));
                        }
                    }
                }
        }

    }

    public void refreshInfo() {
        if (this.index < 0 || this.index >= this.patterns.size()) {
            this.index = 0;
        }

        this.maxIndex = this.patterns.size();
        if (this.patterns.isEmpty()) {
            this.pattern = null;
        } else {
            this.pattern = this.patterns.get(this.index);
            this.patternUu = this.pattern.getCol();
        }

    }

    public void onGuiClosed(EntityPlayer player) {
    }

    public boolean addPattern(RecipeInfo stack) {
        if (StackUtil.isEmpty(stack.getStack())) {
            throw new IllegalArgumentException("empty stack: " + StackUtil.toStringSafe(stack.getStack()));
        } else {
            Iterator<RecipeInfo> var2 = this.patterns.iterator();

            RecipeInfo pattern;
            do {
                if (!var2.hasNext()) {
                    this.patterns.add(stack);
                    this.refreshInfo();
                    return true;
                }

                pattern = var2.next();
            } while (!StackUtil.checkItemEquality(pattern.getStack(), stack.getStack()));

            return false;
        }
    }

    public List<RecipeInfo> getPatterns() {
        return this.patterns;
    }

}
