package com.denfop.items.energy;

import com.denfop.containermenu.ContainerMenuHandHeldInventory;
import com.denfop.containermenu.SlotVirtual;
import com.denfop.containermenu.VirtualSlotItem;
import com.denfop.datacomponent.ContainerItem;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.network.packet.PacketItemStackUpdate;
import com.denfop.utils.ModUtils;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ContainerMenuMagnet extends ContainerMenuHandHeldInventory<ItemStackMagnet> {


    public final int inventorySize;
    private final ItemStackMagnet Toolbox1;
    private final int current;

    public ContainerMenuMagnet(Player player, ItemStackMagnet Toolbox1) {
        super(Toolbox1, player);
        this.Toolbox1 = Toolbox1;
        inventorySize = Toolbox1.inventorySize;
        int slots = Toolbox1.inventorySize;
        this.current = player.getInventory().selected;
        slots = slots / 9;


        for (int i = 0; i < Toolbox1.list.size(); i++) {
            addSlotToContainer(new SlotVirtual(Toolbox1, slots * 9 + i, 10 + (i) * 18, 50,
                    new VirtualSlotItem(Toolbox1, inventorySize)
            ));
        }
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
        if (closeGUI && !player.level().isClientSide()) {
            this.base.saveAsThrown(stack);
            player.closeContainer();
        } else if (type == ClickType.CLONE) {
            ItemStack held = player.getInventory().getSelected();
            if (this.base.isThisContainer(held)) {
                held.getOrDefault(DataComponentsInit.CONTAINER, ContainerItem.EMPTY).updateUUID(held, 0);
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

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (getPlayer() instanceof ServerPlayer) {
            new PacketItemStackUpdate("type", this.base.itemStack1.getOrDefault(DataComponentsInit.BLACK_LIST, false), (ServerPlayer) getPlayer());
        }
    }


}
