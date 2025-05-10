package com.denfop.container;

import com.denfop.tiles.mechanism.TileTunerWireless;
import net.minecraft.world.entity.player.Player;

public class ContainerTunerWireless extends ContainerFullInv<TileTunerWireless> {

    public ContainerTunerWireless(Player entityPlayer, TileTunerWireless tileEntity1) {
        this(entityPlayer, tileEntity1, 166);


        addSlotToContainer(new SlotInvSlot((tileEntity1).inputslot, 0, 81,
                22
        ));

    }

    public ContainerTunerWireless(Player entityPlayer, TileTunerWireless tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

}
