package com.denfop.container;

import com.denfop.tiles.mechanism.TilePlasticCreator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPlasticCreator extends ContainerFullInv<TilePlasticCreator> {

    public ContainerPlasticCreator(EntityPlayer entityPlayer, TilePlasticCreator tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 56, 53, 56, 17, 116, 35, 152, 8);
    }

    public ContainerPlasticCreator(
            EntityPlayer entityPlayer, TilePlasticCreator tileEntity1, int height, int dischargeX,
            int dischargeY, int inputX, int inputY, int outputX, int outputY, int upgradeX, int upgradeY
    ) {
        super(entityPlayer, tileEntity1, height);
        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA,
                    1, inputX - 18, inputY
            ));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity1.input_slot,
                0, -20, 84
        ));
        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA,
                    0, inputX + 18, inputY
            ));
        }
        if (tileEntity1.outputSlot != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                    0, outputX, outputY
            ));
        }

        addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot,
                0, 8, 62
        ));
        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot1,
                0, 27, 62
        ));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, upgradeX, upgradeY + i * 18
            ));
        }
    }


}
