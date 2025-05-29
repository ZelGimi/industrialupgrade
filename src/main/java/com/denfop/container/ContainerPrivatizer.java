package com.denfop.container;

import com.denfop.tiles.mechanism.TilePrivatizer;
import net.minecraft.world.entity.player.Player;

public class ContainerPrivatizer extends ContainerFullInv<TilePrivatizer> {

    public ContainerPrivatizer(Player entityPlayer, TilePrivatizer tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

        addSlotToContainer(new SlotInvSlot((tileEntity1).inputslotA, 0, 81,
                22
        ));
        for (int j = 0; j < 9; ++j) {

            addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot,
                    j, 9 + 18 * j, 54
            ));
        }


    }

    public ContainerPrivatizer(Player entityPlayer, TilePrivatizer tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

}
