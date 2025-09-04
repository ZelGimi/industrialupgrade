package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityGenerationMicrochip;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuBaseGenerationChipMachine
        extends ContainerMenuFullInv<BlockEntityGenerationMicrochip> {

    public ContainerMenuBaseGenerationChipMachine(Player entityPlayer, BlockEntityGenerationMicrochip tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 152, 8);
    }

    public ContainerMenuBaseGenerationChipMachine(
            Player entityPlayer,
            BlockEntityGenerationMicrochip tileEntity1,
            int height,
            int upgradeX,
            int upgradeY
    ) {
        super(entityPlayer, tileEntity1, height);
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 0, 10, 9));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity1.input_slot,
                0, -20, 84
        ));
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
