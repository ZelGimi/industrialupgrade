package com.denfop.container;

import com.denfop.tiles.base.EnumTripleElectricMachine;
import com.denfop.tiles.base.TileEntityTripleElectricMachine;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerTripleElectricMachine extends ContainerFullInv<TileEntityTripleElectricMachine> {

    public ContainerTripleElectricMachine(
            EntityPlayer entityPlayer,
            TileEntityTripleElectricMachine tileEntity1,
            EnumTripleElectricMachine type
    ) {
        this(
                entityPlayer,
                tileEntity1,
                166,
                type.dischangeX,
                type.dischangeY,
                type.inputx,
                type.inputy,
                type.inputx1,
                type.inputy1,
                type.inputx2,
                type.inputy2,
                152,
                8,
                type.register,
                type.outputx,
                type.outputy
        );
    }


    public ContainerTripleElectricMachine(
            EntityPlayer entityPlayer,
            TileEntityTripleElectricMachine tileEntity1,
            int height,
            int dischargeX,
            int dischargeY,
            int inputX1,
            int inputY1,
            int inputX2,
            int inputY2,
            int inputX3,
            int inputY3,
            int upgradeX,
            int upgradeY,
            boolean register,
            int outputx,
            int outputy
    ) {
        super(entityPlayer, tileEntity1, height);
        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA,
                    0, inputX1, inputY1
            ));
        }
        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA,
                    1, inputX2, inputY2
            ));
        }
        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA,
                    2, inputX3, inputY3
            ));
        }
        if (tileEntity1.outputSlot != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                    0, outputx, outputy
            ));
        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, upgradeX, upgradeY + i * 18
            ));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.dischargeSlot, 0, dischargeX, dischargeY));

    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        ret.add("temperature");
        return ret;
    }

}
