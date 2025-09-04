package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityTunerWireless;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuTunerWireless extends ContainerMenuFullInv<BlockEntityTunerWireless> {

    public ContainerMenuTunerWireless(Player entityPlayer, BlockEntityTunerWireless tileEntity1) {
        this(entityPlayer, tileEntity1, 166);


        addSlotToContainer(new SlotInvSlot((tileEntity1).inputslot, 0, 81,
                22
        ));

    }

    public ContainerMenuTunerWireless(Player entityPlayer, BlockEntityTunerWireless tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

}
