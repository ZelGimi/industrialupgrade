package com.denfop.container;

import com.denfop.tiles.base.TileEntityAutoDigger;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerDigger extends ContainerFullInv<TileEntityAutoDigger> {

    public ContainerDigger(TileEntityAutoDigger tileEntity1, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntity1, 178 + 36, 255);
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 8; k++) {
                addSlotToContainer(new SlotInvSlot((tileEntity1).inputslot, i * 8 + k, 17 + 18 * i
                        , 18 + 18 * k));
            }
        }
        for (int i = 0; i < 3; i++) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).slot_upgrade, i, 58, (int) (18 + 18 * 1.5 + 18 * i)));
        }
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 6; k++) {
                addSlotToContainer(new SlotInvSlot((tileEntity1).outputSlot, k + i * 6, 88 + 18 * k, 18 + 18 * i));
            }
        }

    }


}
