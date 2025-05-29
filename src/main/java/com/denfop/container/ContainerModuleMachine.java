package com.denfop.container;

import com.denfop.tiles.mechanism.TileModuleMachine;
import net.minecraft.world.entity.player.Player;

public class ContainerModuleMachine extends ContainerFullInv<TileModuleMachine> {

    public ContainerModuleMachine(Player entityPlayer, TileModuleMachine tileEntity1) {
        this(entityPlayer, tileEntity1, 179);
        addSlotToContainer(new SlotInvSlot((tileEntity1).inputslotA, 0, 81,
                14
        ));

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


    }

    public ContainerModuleMachine(Player entityPlayer, TileModuleMachine tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

}
