package com.denfop.container;

import com.denfop.tiles.mechanism.exp.TileStorageExp;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerStorageExp extends ContainerFullInv<TileStorageExp> {

    public ContainerStorageExp(EntityPlayer entityPlayer, TileStorageExp tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

    }

    public ContainerStorageExp(EntityPlayer entityPlayer, TileStorageExp tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);

        addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot,
                0, 80, 41
        ));

    }


}
