package com.denfop.container;

import com.denfop.tiles.mechanism.TileModuleMachine;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerModuleMachine extends ContainerFullInv<TileModuleMachine> {

    public ContainerModuleMachine(EntityPlayer entityPlayer, TileModuleMachine tileEntity1) {
        this(entityPlayer, tileEntity1, 179);


        for (int j = 0; j < 9; ++j) {

            addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot,
                    9 + j, 9 + 18 * j, 36
            ));
        }
        for (int j = 0; j < 9; ++j) {

            addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot,
                    j, 9 + 18 * j, 54
            ));
        }
        for (int j = 0; j < 9; ++j) {

            addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot,
                    18 + j, 9 + 18 * j, 72
            ));
        }
        addSlotToContainer(new SlotInvSlot((tileEntity1).inputslotA, 0, 81,
                14
        ));

    }

    public ContainerModuleMachine(EntityPlayer entityPlayer, TileModuleMachine tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

}
