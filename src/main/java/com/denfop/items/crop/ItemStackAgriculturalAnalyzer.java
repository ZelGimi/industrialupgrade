package com.denfop.items.crop;

import com.denfop.api.crop.Crop;
import com.denfop.api.crop.CropItem;
import com.denfop.api.crop.genetics.Genome;
import com.denfop.containermenu.ContainerMenuAgriculturalAnalyzer;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.screen.ScreenAgriculturalAnalyzer;
import com.denfop.screen.ScreenIndustrialUpgrade;
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
    public Crop crop;


    public ItemStackAgriculturalAnalyzer(Player player, ItemStack stack, int inventorySize) {
        super(player, stack, inventorySize);
        this.inventorySize = inventorySize;
        this.itemStack1 = stack;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {

        return itemstack.getItem() instanceof CropItem;
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

    public ContainerMenuAgriculturalAnalyzer getGuiContainer(Player player) {
        return new ContainerMenuAgriculturalAnalyzer(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(Player player, ContainerMenuBase<?> isAdmin) {
        return new ScreenAgriculturalAnalyzer((ContainerMenuAgriculturalAnalyzer) isAdmin, itemStack1);
    }


    @Override
    public void addInventorySlot(final Inventory var1) {

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
