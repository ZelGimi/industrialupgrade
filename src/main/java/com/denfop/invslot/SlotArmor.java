package com.denfop.invslot;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.world.inventory.InventoryMenu.*;

public class SlotArmor extends Slot {

    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
    private final EquipmentSlot armorType;

    public SlotArmor(Inventory inventory, EquipmentSlot armorType, int x, int y) {
        super(inventory, 36 + armorType.getIndex(), x, y);
        this.armorType = armorType;
    }

    public EquipmentSlot getEquipmentSlotForItem(ItemStack p_147234_) {
        EquipmentSlot slot = p_147234_.getEquipmentSlot();
        if (slot != null) {
            return slot;
        } else {
            Equipable equipable = Equipable.get(p_147234_);
            if (equipable != null) {
                EquipmentSlot equipmentslot = equipable.getEquipmentSlot();
                if (equipmentslot != EquipmentSlot.BODY) {
                    return equipmentslot;
                }
            }

            return EquipmentSlot.MAINHAND;
        }
    }

    public boolean mayPlace(ItemStack stack) {
        Item item = stack.getItem();
        if (item == null) {
            return false;
        } else {
            return getEquipmentSlotForItem(stack) == this.armorType;
        }
    }

    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[this.armorType.getIndex()]);
    }
}
