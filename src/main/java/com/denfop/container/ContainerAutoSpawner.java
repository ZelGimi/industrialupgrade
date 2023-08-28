package com.denfop.container;

import com.denfop.tiles.base.TileAutoSpawner;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAutoSpawner extends ContainerFullInv<TileAutoSpawner> {

    public ContainerAutoSpawner(EntityPlayer entityPlayer, TileAutoSpawner tileEntity1) {
        super(entityPlayer, tileEntity1, 177);
        if (tileEntity1.outputSlot != null) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, j + i * 9, 8 + 18 * j, 18 + i * 18));
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.module_slot, i, 191, 18 + i * 18));
        }


        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.module_upgrade, i, 191, 99 + i * 18));
        }


    }


}
