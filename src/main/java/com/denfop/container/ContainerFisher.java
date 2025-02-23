package com.denfop.container;

import com.denfop.tiles.base.TileFisher;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerFisher extends ContainerFullInv<TileFisher> {

    public ContainerFisher(EntityPlayer entityPlayer, TileFisher tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

        addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot, 0, 17,
                45
        ));

        for (int i = 0; i < 9; i++) {
            int count = i / 3;
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, i, 65 + (i - (3 * count)) * 18, 27 + count * 18));

        }
    }

    public ContainerFisher(EntityPlayer entityPlayer, TileFisher tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }


}
