package com.denfop.container;

import com.denfop.items.bags.ItemStackLeadBox;
import com.denfop.items.reactors.IRadioactiveItemType;
import com.denfop.utils.ModUtils;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ContainerLeadBox extends ContainerHandHeldInventory<ItemStackLeadBox> {


    public final int inventorySize;
    private final int current;

    public ContainerLeadBox(Player player, ItemStackLeadBox Toolbox1) {
        super(Toolbox1, player);

        inventorySize = Toolbox1.inventorySize;
        int slots = Toolbox1.inventorySize;
        slots = slots / 9;

        int col;
        for (col = 0; col < slots; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(Toolbox1, col1 + col * 9, 8 + col1 * 18, 18 + col * 18) {
                    @Override
                    public boolean mayPlace(final ItemStack stack) {
                        return stack.getItem() instanceof IRadioactiveItemType;
                    }
                });
            }
        }
        this.current = player.getInventory().selected;
        addPlayerInventorySlots(player.getInventory(), 166);

    }

    protected void addPlayerInventorySlots(Player player, int width, int height) {
        int xStart = (width - 162) / 2;

        int col;
        for (col = 0; col < 3; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(player.getInventory(), col1 + col * 9 + 9, xStart + col1 * 18,
                        height + -82 + col * 18
                ));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(player.getInventory(), col, xStart + col * 18, height + -24));
        }

    }

    @Override
    public void clicked(int slot, int button, ClickType type, Player player) {
        if (slot >= 0 && slot < this.slots.size())
            if (this.base.isThisContainer(this.slots.get(slot).getItem()))
                return;
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
                if (slot >= 0 && slot < this.slots.size()) {
                    closeGUI = this.base.isThisContainer(this.slots.get(slot).getItem());
                }
                break;
            case QUICK_MOVE:
                if (slot >= 0 && slot < this.slots.size() && this.base.isThisContainer(this.slots.get(
                                slot)
                        .getItem())) {
                    this.setCarried(ModUtils.emptyStack);
                }
                break;
            case SWAP:

                if (button == current) {
                    this.setCarried(ItemStack.EMPTY);
                    return;
                }
                assert this.getSlotFromInventory(player.getInventory(), button) != null;

                boolean swapOut = this.base.isThisContainer(this.getSlotFromInventory(player.getInventory(), button).getItem());
                boolean swapTo = this.base.isThisContainer(this.slots.get(slot).getItem());
                if (swapOut || swapTo) {
                    for (int i = 0; i < 9; ++i) {
                        if (swapOut && slot == Objects.requireNonNull(this.getSlotFromInventory(
                                player.getInventory(),
                                i
                        )).index || swapTo && button == i) {
                            if (player instanceof ServerPlayer serverPlayer) {
                                serverPlayer.connection.send(new ClientboundSetCarriedItemPacket(i));
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
        if (closeGUI && !player.getLevel().isClientSide()) {
            this.base.saveAsThrown(stack);
            player.closeContainer();
        } else if (type == ClickType.CLONE) {
            ItemStack held = player.getInventory().getSelected();
            if (this.base.isThisContainer(held)) {
                held.getTag().remove("uid");
            }
        }

        this.setCarried(stack);
    }


    public ItemStack slotClick1(int slotId, int dragType, ClickType clickType, Player player) {
        super.clicked(
                slotId,
                dragType,
                clickType,
                player
        );
        return getCarried();
    }

}
