package com.denfop.container;

import com.denfop.tiles.mechanism.TilePlasticPlateCreator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPlasticPlateCreator extends ContainerFullInv<TilePlasticPlateCreator> {

    public ContainerPlasticPlateCreator(EntityPlayer entityPlayer, TilePlasticPlateCreator tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 56, 53, 116, 35, 152, 8);
    }

    public ContainerPlasticPlateCreator(
            EntityPlayer entityPlayer, TilePlasticPlateCreator tileEntity1, int height, int dischargeX,
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
