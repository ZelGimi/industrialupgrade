package com.denfop.containermenu;

import com.denfop.IUItem;
import com.denfop.blockentity.transport.tiles.BlockEntityItemPipes;
import com.denfop.blockentity.transport.tiles.BlockEntityMultiCable;
import com.denfop.containermenu.slot.SlotVirtual;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;

public class ContainerMenuCable extends ContainerMenuFullInv<BlockEntityMultiCable> {

    public final Direction facing;

    public ContainerMenuCable(Player entityPlayer, BlockEntityMultiCable tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        if (tileEntity1 instanceof BlockEntityItemPipes && entityPlayer.getItemInHand(InteractionHand.MAIN_HAND)
                .getItem() != IUItem.connect_item.getItem()) {
            for (int i = 0; i < 9; i++) {
                addSlotToContainer(new SlotVirtual(tileEntity1, i, 116 + (i % 3) * 18, 19 + (i / 3) * 18,
                        ((BlockEntityItemPipes) tileEntity1).list
                ));
            }
            for (int i = 0; i < 9; i++) {
                addSlotToContainer(new SlotVirtual(tileEntity1, i + 9, 8 + (i % 3) * 18, 19 + (i / 3) * 18,
                        ((BlockEntityItemPipes) tileEntity1).list
                ));
            }
        }
        facing = null;
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
    public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        if (clickType == ClickType.PICKUP_ALL)
            return;
        super.clicked(slotId, dragType, clickType, player);
    }


}
