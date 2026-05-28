package com.denfop.containermenu;

import com.denfop.IUItem;
import com.denfop.blockentity.transport.tiles.BlockEntityItemPipes;
import com.denfop.blockentity.transport.tiles.BlockEntityMultiCable;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public class ContainerMenuCable extends ContainerMenuFullInv<BlockEntityMultiCable> {

    public static boolean inventorySlots = true;
    public Direction facing;
    private boolean connect_item;

    public ContainerMenuCable(Player entityPlayer, BlockEntityMultiCable tileEntity1) {
        super(entityPlayer, tileEntity1, 166);


        facing = null;
    }

    public ContainerMenuCable(Player var1, BlockEntityItemPipes tileEntity1, boolean b) {
        super(var1, tileEntity1, 166);


    }

    public ContainerMenuCable(Player entityPlayer, BlockEntityMultiCable tileEntity1, Direction facing) {
        super(entityPlayer, tileEntity1, 166);

        if (facing.getAxis() == Direction.Axis.Y) {
            facing = facing.getOpposite();
        }
        this.facing = facing;

        if (tileEntity1 instanceof BlockEntityItemPipes && entityPlayer
                .getItemInHand(InteractionHand.MAIN_HAND)
                .getItem() != IUItem.connect_item.getItem()) {
            if (((BlockEntityItemPipes) tileEntity1).isInput()) {
                for (int i = 0; i < 9; i++) {
                    addSlotToContainer(new SlotVirtual(
                            tileEntity1,
                            i,
                            116 + (i % 3) * 18,
                            19 + (i / 3) * 18,
                            ((BlockEntityItemPipes) tileEntity1).getInfoSlotFromFacing(facing)
                    ));
                }
                for (int i = 0; i < 9; i++) {
                    addSlotToContainer(new SlotVirtual(tileEntity1,
                            i + 9,
                            8 + (i % 3) * 18,
                            19 + (i / 3) * 18,
                            ((BlockEntityItemPipes) tileEntity1).getInfoSlotFromFacing(facing)
                    ));
                }
            } else {
                for (int i = 0; i < 9; i++) {
                    addSlotToContainer(new SlotVirtual(
                            tileEntity1,
                            i,
                            116 + (i % 3) * 18,
                            19 + (i / 3) * 18,
                            ((BlockEntityItemPipes) tileEntity1).getInfoSlotFromFacing(facing)
                    ));
                }
                for (int i = 0; i < 9; i++) {
                    addSlotToContainer(new SlotVirtual(tileEntity1,
                            i + 9,
                            8 + (i % 3) * 18,
                            19 + (i / 3) * 18,
                            ((BlockEntityItemPipes) tileEntity1).getInfoSlotFromFacing(facing)
                    ));
                }
            }

        }
    }

    @Override
    protected void addPlayerInventorySlots(Inventory inventory, int width, int height) {
        this.connect_item = inventory.player.getItemInHand(InteractionHand.MAIN_HAND)
                .getItem() == IUItem.connect_item.getItem();
        if (!this.connect_item && inventorySlots) {
            int n4 = (width - 162) / 2;

            int n3;
            for (n3 = 0; n3 < 3; ++n3) {
                for (int i = 0; i < 9; ++i) {
                    this.addSlot(new Slot(inventory, i + n3 * 9 + 9, n4 + i * 18, height + -81 + n3 * 18));
                }
            }

            for (n3 = 0; n3 < 9; ++n3) {
                this.addSlot(new Slot(inventory, n3, n4 + n3 * 18, height + -24));
            }
        }
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        if (clickType == ClickType.PICKUP_ALL)
            return;
        super.clicked(slotId, dragType, clickType, player);
    }


}
