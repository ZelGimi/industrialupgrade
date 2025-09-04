package com.denfop.containermenu;

import com.denfop.api.crop.CropItem;
import com.denfop.api.crop.CropNetwork;
import com.denfop.api.crop.genetics.Genome;
import com.denfop.items.crop.ItemStackAgriculturalAnalyzer;
import com.denfop.utils.ModUtils;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ContainerMenuAgriculturalAnalyzer extends ContainerMenuHandHeldInventory<ItemStackAgriculturalAnalyzer> {


    public final int inventorySize;
    private final int current;

    public ContainerMenuAgriculturalAnalyzer(Player player, ItemStackAgriculturalAnalyzer Toolbox1) {
        super(Toolbox1, player);
        this.player = player;
        this.inventory = player.getInventory();
        inventorySize = Toolbox1.inventorySize;
        int slots = Toolbox1.inventorySize;
        this.addSlotToContainer(new Slot(Toolbox1, 0, 180, 65) {
            @Override
            public boolean mayPlace(final ItemStack stack) {
                return stack.getItem() instanceof CropItem;
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public void set(final ItemStack stack) {
                super.set(stack);
                if (stack.isEmpty()) {
                    Toolbox1.crop = null;
                    Toolbox1.genome = null;
                } else {
                    ModUtils.nbt(stack).putBoolean("analyzed", true);
                    Toolbox1.genome = new Genome(stack);
                    Toolbox1.crop = CropNetwork.instance.getCropFromStack(stack).copy();
                    Toolbox1.genome.loadCrop(Toolbox1.crop);
                }
            }
        });
        this.current = player.getInventory().selected;
        addPlayerInventorySlots(player.getInventory(), 166);

    }

    protected void addPlayerInventorySlots(Inventory inventory, int width, int height) {
        int xStart = (width - 162) / 2;

        int col;


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
