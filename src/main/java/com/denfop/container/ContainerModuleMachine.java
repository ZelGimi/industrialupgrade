package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityModuleMachine;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerModuleMachine extends ContainerFullInv<TileEntityModuleMachine> {

    public ContainerModuleMachine(EntityPlayer entityPlayer, TileEntityModuleMachine tileEntity1) {
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

    public ContainerModuleMachine(EntityPlayer entityPlayer, TileEntityModuleMachine tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

}
