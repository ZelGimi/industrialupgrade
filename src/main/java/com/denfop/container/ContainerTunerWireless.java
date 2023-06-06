package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityTunerWireless;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerTunerWireless extends ContainerFullInv<TileEntityTunerWireless> {

    public ContainerTunerWireless(EntityPlayer entityPlayer, TileEntityTunerWireless tileEntity1) {
        this(entityPlayer, tileEntity1, 166);


        addSlotToContainer(new SlotInvSlot((tileEntity1).inputslot, 0, 81,
                22
        ));

    }

    public ContainerTunerWireless(EntityPlayer entityPlayer, TileEntityTunerWireless tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

}
