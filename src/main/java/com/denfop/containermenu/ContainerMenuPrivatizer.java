package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityPrivatizer;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuPrivatizer extends ContainerMenuFullInv<BlockEntityPrivatizer> {

    public ContainerMenuPrivatizer(Player entityPlayer, BlockEntityPrivatizer tileEntity1) {
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

    public ContainerMenuPrivatizer(Player entityPlayer, BlockEntityPrivatizer tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

}
