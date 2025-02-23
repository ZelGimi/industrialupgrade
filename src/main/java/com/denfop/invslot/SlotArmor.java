package com.denfop.invslot;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SlotArmor extends Slot {

    private final EntityEquipmentSlot armorType;

    public SlotArmor(InventoryPlayer inventory, EntityEquipmentSlot armorType, int x, int y) {
        super(inventory, 36 + armorType.getIndex(), x, y);
        this.armorType = armorType;
    }

    public boolean isItemValid(ItemStack stack) {
        Item item = stack.getItem();
        return item != ItemStack.EMPTY.getItem() && item.isValidArmor(
                stack,
                this.armorType,
                ((InventoryPlayer) this.inventory).player
        );
    }

    @SideOnly(Side.CLIENT)
    public String getSlotTexture() {
        return ItemArmor.EMPTY_SLOT_NAMES[this.armorType.getIndex()];
    }

}
