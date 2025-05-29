package com.denfop.container;


import com.denfop.tiles.mechanism.TileEntityItemManipulator;
import net.minecraft.world.entity.player.Player;

public class ContainerItemManipulator extends ContainerFullInv<TileEntityItemManipulator> {

    public ContainerItemManipulator(Player entityPlayer, TileEntityItemManipulator tileEntity1) {
        this(entityPlayer, tileEntity1, 221);
        for (int j = 0; j < 27; ++j) {

            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot1,
                    j, 8 + 18 * (j % 9), 20 + 18 * (j / 9)
            ));
        }
        for (int j = 0; j < 27; ++j) {

            addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot2,
                    j, 8 + 18 * (j % 9), 80 + 18 * (j / 9)
            ));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot,
                0, 185, 38
        ));
        addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot1,
                0, 185, 98
        ));
    }

    public ContainerItemManipulator(Player entityPlayer, TileEntityItemManipulator tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

}
