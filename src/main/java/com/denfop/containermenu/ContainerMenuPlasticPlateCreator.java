package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityPlasticPlateCreator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuPlasticPlateCreator extends ContainerMenuFullInv<BlockEntityPlasticPlateCreator> {

    public ContainerMenuPlasticPlateCreator(Player entityPlayer, BlockEntityPlasticPlateCreator tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 56, 53, 116, 35, 152, 8);
    }

    public ContainerMenuPlasticPlateCreator(
            Player entityPlayer, BlockEntityPlasticPlateCreator tileEntity1, int height, int dischargeX,
            int dischargeY, int outputX, int outputY, int upgradeX, int upgradeY
    ) {
        super(entityPlayer, tileEntity1, height);


        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA,
                    0, 56, 17
            ));
        }
        addSlotToContainer(new SlotInvSlot(base.input_slot,
                0, -20, 84
        ));
        if (tileEntity1.outputSlot != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                    0, outputX, outputY
            ));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot1,
                0, 27, 62
        ));
        addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot,
                0, 8, 62
        ));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, upgradeX, upgradeY + i * 18
            ));
        }
    }


}
