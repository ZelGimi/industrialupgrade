package com.denfop.items.crop;

import com.denfop.api.agriculture.ICrop;
import com.denfop.api.agriculture.ICropItem;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.container.ContainerAgriculturalAnalyzer;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiAgriculturalAnalyzer;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemStackInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ItemStackAgriculturalAnalyzer extends ItemStackInventory {

    public final int inventorySize;
    public final ItemStack itemStack1;
    public Genome genome;
    public ICrop crop;



    public ItemStackAgriculturalAnalyzer(Player player, ItemStack stack, int inventorySize) {
        super(player, stack, inventorySize);
        this.inventorySize = inventorySize;
        this.itemStack1 = stack;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {

        return itemstack.getItem() instanceof ICropItem;
    }

    public void save() {
        super.save();
    }

    public void saveAndThrow(ItemStack stack) {
        ListTag contentList = new ListTag();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (!ModUtils.isEmpty(this.inventory[i])) {
                CompoundTag nbt = new CompoundTag();
                nbt.putByte("Slot", (byte) i);
                this.inventory[i].save(nbt);
                contentList.add(nbt);
            }
        }

        ModUtils.nbt(stack).put("Items", contentList);
        this.clear();
    }

    public ContainerAgriculturalAnalyzer getGuiContainer(Player player) {
        return new ContainerAgriculturalAnalyzer(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(Player player, ContainerBase<?> isAdmin) {
        return new GuiAgriculturalAnalyzer((ContainerAgriculturalAnalyzer) isAdmin, itemStack1);
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
    }


    @Override
    public void addInventorySlot(final InvSlot var1) {

    }

    @Override
    public int getBaseIndex(final InvSlot var1) {
        return 0;
    }


    public ItemStack get(int index) {
        return this.inventory[index];
    }

    protected void restore(ItemStack[] backup) {
        if (backup.length != this.inventory.length) {
            throw new IllegalArgumentException("invalid array size");
        } else {
            System.arraycopy(backup, 0, this.inventory, 0, this.inventory.length);

        }
    }

    @Nonnull
    public String getName() {
        return "toolbox";
    }

    public boolean hasCustomName() {
        return false;
    }




    protected ItemStack[] backup() {
        ItemStack[] ret = new ItemStack[this.inventory.length];

        for (int i = 0; i < this.inventory.length; ++i) {
            ItemStack content = this.inventory[i];
            ret[i] = ModUtils.isEmpty(content) ? ModUtils.emptyStack : content.copy();
        }

        return ret;
    }

    public void put(ItemStack content) {
        this.put(0, content);
    }

    public void put(int index, ItemStack content) {
        if (ModUtils.isEmpty(content)) {
            content = ModUtils.emptyStack;
        }

        this.inventory[index] = content;
        this.save();
    }

    public int getStackSizeLimit() {
        return 64;
    }


}
