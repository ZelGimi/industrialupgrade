package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityPrivatizer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPrivatizer extends ContainerFullInv<TileEntityPrivatizer> {

    public ContainerPrivatizer(EntityPlayer entityPlayer, TileEntityPrivatizer tileEntity1) {
        this(entityPlayer, tileEntity1, 166);


        for (int j = 0; j < 9; ++j) {

            addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot,
                    j, 9 + 18 * j, 54
            ));
        }

        addSlotToContainer(new SlotInvSlot((tileEntity1).inputslotA, 0, 81,
                22
        ));

    }

    public ContainerPrivatizer(EntityPlayer entityPlayer, TileEntityPrivatizer tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

}
