package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityQuantumMiner;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerQuantumMiner extends ContainerFullInv<TileEntityQuantumMiner> {

    public ContainerQuantumMiner(EntityPlayer entityPlayer, TileEntityQuantumMiner tileEntity1) {
        this(entityPlayer, tileEntity1, 166);
        if (tileEntity1.outputSlot != null) {

            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j, 30 + 18 * j, 16
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 6, 30 + 18 * j, 16 + 18
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 12, 30 + 18 * j, 16 + 18 + 18
                ));
            }

        }
    }

    public ContainerQuantumMiner(EntityPlayer entityPlayer, TileEntityQuantumMiner tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }


}
