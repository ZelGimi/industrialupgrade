package com.denfop.items.energy;

import com.denfop.container.ContainerHandHeldInventory;
import com.denfop.container.SlotVirtual;
import com.denfop.container.VirtualSlotItem;
import com.denfop.network.packet.PacketItemStackUpdate;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketHeldItemChange;

import java.util.Objects;

public class ContainerMagnet extends ContainerHandHeldInventory<ItemStackMagnet> {


    public final int inventorySize;
    private final ItemStackMagnet Toolbox1;
    private final int current;

    public ContainerMagnet(EntityPlayer player, ItemStackMagnet Toolbox1) {
        super(Toolbox1);
        this.Toolbox1 = Toolbox1;
        inventorySize = Toolbox1.inventorySize;
        int slots = Toolbox1.inventorySize;
        this.current = player.inventory.currentItem;
        slots = slots / 9;


        for (int i = 0; i < Toolbox1.list.length; i++) {
            addSlotToContainer(new SlotVirtual(Toolbox1, slots * 9 + i, 10 + (i) * 18, 50,
                    new VirtualSlotItem(Toolbox1.list, inventorySize)
            ));
        }
        addPlayerInventorySlots(player, 166);

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
        for (IContainerListener crafter : this.listeners) {
            if (crafter instanceof EntityPlayerMP) {
                NBTTagCompound tagCompound = this.base.getNBT();
                new PacketItemStackUpdate("type", tagCompound.getBoolean("type"), (EntityPlayerMP) crafter);
            }
        }
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
