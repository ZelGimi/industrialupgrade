package com.denfop.container;

import com.denfop.tiles.mechanism.quarry.TileBaseQuantumQuarry;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerQuantumQuarry extends ContainerFullInv<TileBaseQuantumQuarry> {

    public ContainerQuantumQuarry(EntityPlayer entityPlayer, TileBaseQuantumQuarry tileEntity1) {
        this(entityPlayer, tileEntity1, 166 + 60);
        if (tileEntity1.outputSlot != null) {

            for (int j = 0; j < 7; ++j) {
                for (int i = 0; i < 7; ++i) {
                    addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                            j * 7 + i, 30 + 18 * j, 6 + 18 * i
                    ));
                }
            }

            addSlotToContainer(new SlotInvSlot(tileEntity1.inputslotB, 0, 8,
                    6 + 30
            ));
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot, 0, 8,
                    6 + 18 + 30
            ));
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputslotA, 0, 8,
                    6 + 18 + 18 + 30
            ));
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputslotC, 0, 8,
                    6 + 18 + 18 + 18 + 30
            ));
        }
    }

    public ContainerQuantumQuarry(EntityPlayer entityPlayer, TileBaseQuantumQuarry tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }


}
