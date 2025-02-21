package com.denfop.container;

import com.denfop.IUItem;
import com.denfop.items.bags.ItemStackBags;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketHeldItemChange;

import java.util.Objects;

public class ContainerBags extends ContainerHandHeldInventory<ItemStackBags> {


    public final int inventorySize;
    private final ItemStackBags Toolbox1;
    private final int current;

    public ContainerBags(EntityPlayer player, ItemStackBags Toolbox1) {
        super(Toolbox1);
        this.Toolbox1 = Toolbox1;
        inventorySize = Toolbox1.inventorySize;
        int slots = Toolbox1.inventorySize;
        this.current = player.inventory.currentItem;
        slots = slots / 9;

        int col;
        Item item = Toolbox1.itemStack1.getItem();
        if (item == IUItem.bags) {
            for (col = 0; col < slots; ++col) {
                for (int col1 = 0; col1 < 9; ++col1) {
                    this.addSlotToContainer(new Slot(Toolbox1, col1 + col * 9, 8 + col1 * 18, 30 + col * 18));
                }
            }
        }else if (item == IUItem.adv_bags){
            for (col = 0; col < slots; ++col) {
                for (int col1 = 0; col1 < 9; ++col1) {
                    this.addSlotToContainer(new Slot(Toolbox1, col1 + col * 9, 8 + col1 * 18, 19 + col * 18));
                }
            }
        }else{
            for (col = 0; col < slots; ++col) {
                for (int col1 = 0; col1 < 9; ++col1) {
                    this.addSlotToContainer(new Slot(Toolbox1, col1 + col * 9, 8 + col1 * 18, 19 + col * 18));
                }
            }
        }

        if (item == IUItem.bags) {
            for (int i = 0; i < Toolbox1.list.length; i++) {
                addSlotToContainer(new SlotVirtual(Toolbox1, slots * 9 + i, 180, 9 + (i) * 18,
                        new VirtualSlotItem(Toolbox1.list, inventorySize)
                ));
            }
        } else {
            for (int i = 0; i < Toolbox1.list.length; i++) {
                addSlotToContainer(new SlotVirtual(Toolbox1, slots * 9 + i, 180, 25 + (i) * 18,
                        new VirtualSlotItem(Toolbox1.list, inventorySize)
                ));
            }
        }
        int y;
        if (item == IUItem.bags) {
            y = 177;
        }else if (item == IUItem.adv_bags){
            y = 202;
        }else{
            y = 238;
        }
        addPlayerInventorySlots(player, y);

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
            case THROW:
                if (slot >= 0 && slot < this.inventorySlots.size()) {
                    closeGUI = this.base.isThisContainer(this.inventorySlots.get(slot).getStack());
                }
                break;
            case QUICK_MOVE:
                if (slot >= 0 && slot < this.inventorySlots.size() && this.base.isThisContainer(this.inventorySlots
                        .get(slot)
                        .getStack())) {
                    return ModUtils.emptyStack;
                }
                break;
            case SWAP:

                if (button == current) {
                    return ItemStack.EMPTY;
                }
                assert this.getSlotFromInventory(player.inventory, button) != null;

                boolean swapOut = this.base.isThisContainer(this.getSlotFromInventory(player.inventory, button).getStack());
                boolean swapTo = this.base.isThisContainer(this.inventorySlots.get(slot).getStack());
                if (swapOut || swapTo) {
                    for (int i = 0; i < 9; ++i) {
                        if (swapOut && slot == Objects.requireNonNull(this.getSlotFromInventory(
                                player.inventory,
                                i
                        )).slotNumber || swapTo && button == i) {
                            if (player instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) player).connection.sendPacket(new SPacketHeldItemChange(i));
                            }
                            break label82;
                        }
                    }
                }
                break;
            default:
                throw new RuntimeException("Unexpected ClickType: " + type);
        }

        ItemStack stack = this.slotClick1(slot, button, type, player);
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

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

    }

    public ItemStack slotClick1(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        return super.slotClick(
                slotId,
                dragType,
                clickType,
                player
        );
    }

}
