package com.denfop.container;

import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.agriculture.ICropItem;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.items.crop.ItemStackAgriculturalAnalyzer;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketHeldItemChange;

import java.util.Objects;

public class ContainerAgriculturalAnalyzer extends ContainerHandHeldInventory<ItemStackAgriculturalAnalyzer> {


    public final int inventorySize;
    private final int current;

    public ContainerAgriculturalAnalyzer(EntityPlayer player, ItemStackAgriculturalAnalyzer Toolbox1) {
        super(Toolbox1);

        inventorySize = Toolbox1.inventorySize;
        int slots = Toolbox1.inventorySize;
        this.addSlotToContainer(new Slot(Toolbox1, 0, 12, 24) {
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return stack.getItem() instanceof ICropItem;
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }

            @Override
            public void putStack(final ItemStack stack) {
                super.putStack(stack);
                if (stack.isEmpty()) {
                    Toolbox1.crop = null;
                    Toolbox1.genome = null;
                } else {
                    ModUtils.nbt(stack).setBoolean("analyzed", true);
                    Toolbox1.genome = new Genome(stack);
                    Toolbox1.crop = CropNetwork.instance.getCropFromStack(stack).copy();
                    Toolbox1.genome.loadCrop(Toolbox1.crop);
                }
            }
        });
        this.current = player.inventory.currentItem;
        addPlayerInventorySlots(player, 233);

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
                if (slot >= 0 && slot < this.inventorySlots.size() && this.base.isThisContainer(this.inventorySlots.get(
                                slot)
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

    public ItemStack slotClick1(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        return super.slotClick(
                slotId,
                dragType,
                clickType,
                player
        );
    }

}
