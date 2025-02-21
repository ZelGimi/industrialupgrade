package com.denfop.tiles.mechanism;


import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.IPatternStorage;
import com.denfop.api.recipe.RecipeInfo;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerPatternStorage;
import com.denfop.gui.GuiPatternStorage;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemCrystalMemory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TilePatternStorage extends TileEntityInventory implements IUpdatableTileEvent,
        IPatternStorage {

    public final InvSlot diskSlot;
    private final List<RecipeInfo> patterns = new ArrayList<>();
    public int index = 0;
    public int maxIndex;
    public RecipeInfo pattern;
    public double patternUu;
    public double patternEu;

    public TilePatternStorage() {
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

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.pattern_storage_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
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

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            index = (int) DecoderHandler.decode(customPacketBuffer);
            maxIndex = (int) DecoderHandler.decode(customPacketBuffer);
            pattern = (RecipeInfo) DecoderHandler.decode(customPacketBuffer);
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
            EncoderHandler.encode(packet, index);
            EncoderHandler.encode(packet, maxIndex);
            EncoderHandler.encode(packet, pattern);
            EncoderHandler.encode(packet, patternUu);
            EncoderHandler.encode(packet, patternEu);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (!this.getWorld().isRemote) {
            NBTTagCompound nbt = ModUtils.nbt(stack);
            this.readContents(nbt);
        }

    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (drop.isItemEqual(this.getPickBlock(
                null,
                null
        )) && (wrench || this.teBlock.getDefaultDrop() == MultiTileBlock.DefaultDrop.Self)) {
            NBTTagCompound nbt = ModUtils.nbt(drop);
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

    public ContainerBase<TilePatternStorage> getGuiContainer(EntityPlayer player) {
        return new ContainerPatternStorage(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiPatternStorage(new ContainerPatternStorage(player, this));
    }

    public void updateTileServer(EntityPlayer player, double event) {
        ItemStack crystalMemory;
        switch ((int) event) {
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
            this.patternEu = this.patternUu * 1000000000;
        }

    }

    public void onGuiClosed(EntityPlayer player) {
    }

    public boolean addPattern(RecipeInfo stack) {
        if (ModUtils.isEmpty(stack.getStack())) {
            throw new IllegalArgumentException("empty stack: " + stack.getStack());
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
            } while (!ModUtils.checkItemEquality(pattern.getStack(), stack.getStack()));

            return false;
        }
    }

    public List<RecipeInfo> getPatterns() {
        return this.patterns;
    }

}
