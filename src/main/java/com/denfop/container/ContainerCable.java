package com.denfop.container;

import com.denfop.IUItem;
import com.denfop.tiles.transport.tiles.TileEntityItemPipes;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

public class ContainerCable extends ContainerFullInv<TileEntityMultiCable> {

    public final EnumFacing facing;

    public ContainerCable(EntityPlayer entityPlayer, TileEntityMultiCable tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        if (tileEntity1 instanceof TileEntityItemPipes && entityPlayer
                .getHeldItem(EnumHand.MAIN_HAND)
                .getItem() != IUItem.connect_item) {
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

    public ContainerCable(EntityPlayer entityPlayer, TileEntityMultiCable tileEntity1, EnumFacing facing) {
        super(entityPlayer, tileEntity1, 166);
        if (facing.getAxis() == EnumFacing.Axis.Y)
            facing = facing.getOpposite();
        this.facing = facing;
        if (tileEntity1 instanceof TileEntityItemPipes && entityPlayer
                .getHeldItem(EnumHand.MAIN_HAND)
                .getItem() != IUItem.connect_item) {
            if (((TileEntityItemPipes) tileEntity1).isInput()) {
                for (int i = 0; i < 9; i++) {
                    addSlotToContainer(new SlotVirtual(
                            tileEntity1,
                            i ,
                            116 + (i % 3) * 18,
                            19 + (i / 3) * 18,
                            ((TileEntityItemPipes) tileEntity1).getInfoSlotFromFacing(facing)
                    ));
                }
                for (int i = 0; i < 9; i++) {
                    addSlotToContainer(new SlotVirtual(tileEntity1,
                            i + 9 ,
                            8 + (i % 3) * 18,
                            19 + (i / 3) * 18,
                            ((TileEntityItemPipes) tileEntity1).getInfoSlotFromFacing(facing)
                    ));
                }
            }else{
                for (int i = 0; i < 9; i++) {
                    addSlotToContainer(new SlotVirtual(
                            tileEntity1,
                            i ,
                            116 + (i % 3) * 18,
                            19 + (i / 3) * 18,
                            ((TileEntityItemPipes) tileEntity1).getInfoSlotFromFacing(facing)
                    ));
                }
                for (int i = 0; i < 9; i++) {
                    addSlotToContainer(new SlotVirtual(tileEntity1,
                            i + 9 ,
                            8 + (i % 3) * 18,
                            19 + (i / 3) * 18,
                            ((TileEntityItemPipes) tileEntity1).getInfoSlotFromFacing(facing)
                    ));
                }
            }

        }
    }

    public ItemStack slotClick(int slot, int button, ClickType type, EntityPlayer player) {
        if (type == ClickType.PICKUP_ALL) {
            return ItemStack.EMPTY;
        }
        return super.slotClick(slot, button, type, player);
    }

}
