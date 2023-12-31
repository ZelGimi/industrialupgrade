package com.denfop.container;

import com.denfop.tiles.base.TileBaseGenerationMicrochip;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBaseGenerationChipMachine
        extends ContainerFullInv<TileBaseGenerationMicrochip> {

    public ContainerBaseGenerationChipMachine(EntityPlayer entityPlayer, TileBaseGenerationMicrochip tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 152, 8);
    }

    public ContainerBaseGenerationChipMachine(
            EntityPlayer entityPlayer,
            TileBaseGenerationMicrochip tileEntity1,
            int height,
            int upgradeX,
            int upgradeY
    ) {
        super(entityPlayer, tileEntity1, height);
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 0, 10, 9));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 1, 10, 30));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 2, 43, 9));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 3, 43, 29));
        }
        if ((tileEntity1).outputSlot != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).outputSlot, 0, 112, 19));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 4, 71, 19));
        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(
                    new SlotInvSlot((tileEntity1).upgradeSlot, i, upgradeX, upgradeY + i * 18));
        }
    }


}
