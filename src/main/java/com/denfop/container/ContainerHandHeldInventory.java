package com.denfop.container;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketHeldItemChange;

public class ContainerHandHeldInventory<T extends ItemStackInventory & IAdvInventory> extends ContainerBase<T> {

    public ContainerHandHeldInventory(T inventory) {
        super(inventory);
    }

    protected void addPlayerInventorySlots(EntityPlayer player, int width, int height) {
        int xStart = (width - 162) / 2;

        int col;
        for (col = 0; col < 3; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(player.inventory, col1 + col * 9 + 9, xStart + col1 * 18,
                        height + -82 + col * 18
                ));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(player.inventory, col, xStart + col * 18, height + -24));
        }

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
                    return ModUtils.emptyStack;
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
        super.onContainerClosed(player);
    }

}
