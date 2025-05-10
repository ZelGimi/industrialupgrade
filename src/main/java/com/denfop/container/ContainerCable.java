package com.denfop.container;

import com.denfop.IUItem;
import com.denfop.tiles.transport.tiles.TileEntityItemPipes;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;

public class ContainerCable extends ContainerFullInv<TileEntityMultiCable> {

    public final Direction facing;

    public ContainerCable(Player entityPlayer, TileEntityMultiCable tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        if (tileEntity1 instanceof TileEntityItemPipes && entityPlayer.getItemInHand(InteractionHand.MAIN_HAND)
                .getItem() != IUItem.connect_item.getItem()) {
            for (int i = 0; i < 9; i++) {
                addSlotToContainer(new SlotVirtual(tileEntity1, i, 116 + (i % 3) * 18, 19 + (i / 3) * 18,
                        ((TileEntityItemPipes) tileEntity1).list
                ));
            }
            for (int i = 0; i < 9; i++) {
                addSlotToContainer(new SlotVirtual(tileEntity1, i + 9, 8 + (i % 3) * 18, 19 + (i / 3) * 18,
                        ((TileEntityItemPipes) tileEntity1).list
                ));
            }
        }
        facing = null;
    }

    public ContainerCable(Player entityPlayer, TileEntityMultiCable tileEntity1, Direction facing) {
        super(entityPlayer, tileEntity1, 166);
        if (facing.getAxis() == Direction.Axis.Y) {
            facing = facing.getOpposite();
        }
        this.facing = facing;
        if (tileEntity1 instanceof TileEntityItemPipes && entityPlayer
                .getItemInHand(InteractionHand.MAIN_HAND)
                .getItem() != IUItem.connect_item.getItem()) {
            if (((TileEntityItemPipes) tileEntity1).isInput()) {
                for (int i = 0; i < 9; i++) {
                    addSlotToContainer(new SlotVirtual(
                            tileEntity1,
                            i,
                            116 + (i % 3) * 18,
                            19 + (i / 3) * 18,
                            ((TileEntityItemPipes) tileEntity1).getInfoSlotFromFacing(facing)
                    ));
                }
                for (int i = 0; i < 9; i++) {
                    addSlotToContainer(new SlotVirtual(tileEntity1,
                            i + 9,
                            8 + (i % 3) * 18,
                            19 + (i / 3) * 18,
                            ((TileEntityItemPipes) tileEntity1).getInfoSlotFromFacing(facing)
                    ));
                }
            } else {
                for (int i = 0; i < 9; i++) {
                    addSlotToContainer(new SlotVirtual(
                            tileEntity1,
                            i,
                            116 + (i % 3) * 18,
                            19 + (i / 3) * 18,
                            ((TileEntityItemPipes) tileEntity1).getInfoSlotFromFacing(facing)
                    ));
                }
                for (int i = 0; i < 9; i++) {
                    addSlotToContainer(new SlotVirtual(tileEntity1,
                            i + 9,
                            8 + (i % 3) * 18,
                            19 + (i / 3) * 18,
                            ((TileEntityItemPipes) tileEntity1).getInfoSlotFromFacing(facing)
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
