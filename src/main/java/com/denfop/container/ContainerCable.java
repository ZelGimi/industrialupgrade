package com.denfop.container;

import com.denfop.IUItem;
import com.denfop.tiles.transport.tiles.TileEntityItemPipes;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ContainerCable extends ContainerFullInv<TileEntityMultiCable> {

    public ContainerCable(EntityPlayer entityPlayer, TileEntityMultiCable tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        if (tileEntity1 instanceof TileEntityItemPipes && entityPlayer.getHeldItem(EnumHand.MAIN_HAND).getItem() != IUItem.connect_item) {
            for (int i = 0; i < 9; i++) {
                addSlotToContainer(new SlotVirtual(tileEntity1, i, 10 + i * 18, 18,
                        ((TileEntityItemPipes) tileEntity1).list
                ));
            }
            for (int i = 0; i < 9; i++) {
                addSlotToContainer(new SlotVirtual(tileEntity1, i + 9, 10 + i * 18, 40,
                        ((TileEntityItemPipes) tileEntity1).list
                ));
            }
        }
    }
    public ItemStack slotClick(int slot, int button, ClickType type, EntityPlayer player) {
        if( type == ClickType.PICKUP_ALL)
            return ItemStack.EMPTY;
        return super.slotClick(slot, button, type, player);
    }

}
