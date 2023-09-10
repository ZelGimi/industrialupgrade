package com.simplequarries;


import com.denfop.container.ContainerFullInv;
import com.denfop.container.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBaseQuarry extends ContainerFullInv<TileBaseQuarry> {

    public ContainerBaseQuarry(EntityPlayer entityPlayer, TileBaseQuarry tileEntity1) {
        this(entityPlayer, tileEntity1, 166);
        for (int j = 0; j < tileEntity1.input.size(); ++j) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.input,
                    j, 6, 6 + j * 18
            ));
        }
        if (tileEntity1.outputSlot != null) {

            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j, 30 + 18 * j, 6
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 6, 30 + 18 * j, 6 + 18
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 12, 30 + 18 * j, 6 + 18 + 18
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 18, 30 + 18 * j, 6 + 18 + 18 + 18
                ));
            }

        }
    }

    public ContainerBaseQuarry(EntityPlayer entityPlayer, TileBaseQuarry tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }


}
