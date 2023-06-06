package com.denfop.container;

import com.denfop.items.HandHeldInventory;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketHeldItemChange;

public class ContainerHandHeldInventory<T extends HandHeldInventory> extends ContainerBase<T> {

    public ContainerHandHeldInventory(T inventory) {
        super(inventory);
    }

    public ItemStack slotClick(int slot, int button, ClickType type, EntityPlayer player) {
        boolean closeGUI;
        closeGUI = false;
        label82:
        switch (type) {
            case CLONE:
            case PICKUP_ALL:
            case QUICK_CRAFT:
                break;
            case PICKUP:
                if (slot >= 0 && slot < this.inventorySlots.size()) {
                    closeGUI = this.base.isThisContainer(this.inventorySlots.get(slot).getStack());
                }
                break;
            case QUICK_MOVE:
                if (slot >= 0 && slot < this.inventorySlots.size() && this.base.isThisContainer(this.inventorySlots.get(
                                slot)
                        .getStack())) {
                    return StackUtil.emptyStack;
                }
                break;
            case SWAP:
                assert this.getSlotFromInventory(player.inventory, button) != null;

                assert this.getSlotFromInventory(player.inventory, button) != null;

                boolean swapOut = this.base.isThisContainer(this
                        .getSlotFromInventory(player.inventory, button)
                        .getStack());
                boolean swapTo = this.base.isThisContainer(this.inventorySlots.get(slot).getStack());
                if (swapOut || swapTo) {
                    for (int i = 0; i < 9; ++i) {
                        if (swapOut && slot == this.getSlotFromInventory(
                                player.inventory,
                                i
                        ).slotNumber || swapTo && button == i) {
                            if (player instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) player).connection.sendPacket(new SPacketHeldItemChange(i));
                            }
                            break label82;
                        }
                    }
                }
                break;
            case THROW:
                if (slot >= 0 && slot < this.inventorySlots.size()) {
                    closeGUI = this.base.isThisContainer(this.inventorySlots.get(slot).getStack());
                }
                break;
            default:
                throw new RuntimeException("Unexpected ClickType: " + type);
        }

        ItemStack stack = super.slotClick(slot, button, type, player);
        if (closeGUI && !player.getEntityWorld().isRemote) {
            this.base.saveAsThrown(stack);
            player.closeScreen();
        } else if (type == ClickType.CLONE) {
            ItemStack held = player.inventory.getItemStack();
            if (this.base.isThisContainer(held)) {
                held.getTagCompound().removeTag("uid");
            }
        }

        return stack;
    }

    public void onContainerClosed(EntityPlayer player) {
        this.base.onGuiClosed(player);
        super.onContainerClosed(player);
    }

}
