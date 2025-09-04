package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityDoubleElectricMachine;
import com.denfop.blockentity.base.BlockEntityPainting;
import com.denfop.blockentity.base.EnumDoubleElectricMachine;
import com.denfop.blockentity.mechanism.BlockEntitySunnariumPanelMaker;
import com.denfop.blockentity.mechanism.BlockEntityUpgradeRover;
import com.denfop.blockentity.mechanism.dual.BlockEntityEnrichment;
import com.denfop.blockentity.mechanism.dual.BlockEntitySynthesis;
import com.denfop.blockentity.mechanism.dual.BlockEntityUpgradeBlock;
import com.denfop.blockentity.mechanism.dual.heat.BlockEntityAlloySmelter;
import com.denfop.blockentity.mechanism.dual.heat.BlockEntityWeldingMachine;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuDoubleElectricMachine extends ContainerMenuBaseDoubleElectricMachine {

    public ContainerMenuDoubleElectricMachine(
            Player entityPlayer,
            BlockEntityDoubleElectricMachine tileEntity1,
            EnumDoubleElectricMachine type
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
                147,
                8,
                type.register,
                type.outputx,
                type.outputy
        );
    }


    public ContainerMenuDoubleElectricMachine(
            Player entityPlayer,
            BlockEntityDoubleElectricMachine tileEntity1,
            int height,
            int dischargeX,
            int dischargeY,
            int inputX1,
            int inputY1,
            int inputX2,
            int inputY2,
            int upgradeX,
            int upgradeY,
            boolean register,
            int outputx,
            int outputy
    ) {
        super(entityPlayer, tileEntity1, height, dischargeX, dischargeY, register);
        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA,
                    1, inputX2, inputY2
            ));
        }

        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA,
                    0, inputX1, inputY1
            ));
        }
        if (tileEntity1.outputSlot != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                    0, outputx, outputy
            ));
        }
        for (int i = 0; i < 4; i++) {
            if (tileEntity1 instanceof BlockEntityEnrichment) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                        i, 144, 9 + i * 18
                ));
            } else if (tileEntity1 instanceof BlockEntitySunnariumPanelMaker) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                        i, upgradeX + 5, upgradeY + 1 + i * 18
                ));
            } else if (tileEntity1 instanceof BlockEntitySynthesis) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                        i, upgradeX + 5, upgradeY + 1 + i * 18
                ));
            } else if (tileEntity1 instanceof BlockEntityWeldingMachine) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                        i, upgradeX + 5, upgradeY + i * 18
                ));
            } else if (tileEntity1 instanceof BlockEntityUpgradeBlock
                    || tileEntity1 instanceof BlockEntityUpgradeRover
            ) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                        i, upgradeX + 5, upgradeY + 1 + i * 18
                ));
            } else if (tileEntity1 instanceof BlockEntityPainting) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                        i, upgradeX + 5, upgradeY + 1 + i * 18
                ));
            } else {
                addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                        i, upgradeX, upgradeY + i * 18
                ));
            }

        }
        if (tileEntity1 instanceof BlockEntityAlloySmelter) {
            addSlotToContainer(new SlotInvSlot(((BlockEntityAlloySmelter) tileEntity1).input_slot,
                    0, -20, 84
            ));
        }
        if (tileEntity1 instanceof BlockEntitySynthesis) {
            addSlotToContainer(new SlotInvSlot(((BlockEntitySynthesis) tileEntity1).input_slot,
                    0, -20, 84
            ));
        }
        if (tileEntity1 instanceof BlockEntityEnrichment) {
            addSlotToContainer(new SlotInvSlot(((BlockEntityEnrichment) tileEntity1).input_slot,
                    0, -20, 84
            ));
        }
        if (tileEntity1 instanceof BlockEntitySunnariumPanelMaker) {
            addSlotToContainer(new SlotInvSlot(((BlockEntitySunnariumPanelMaker) tileEntity1).input_slot,
                    0, -20, 84
            ));
        }
        if (tileEntity1 instanceof BlockEntityWeldingMachine) {
            addSlotToContainer(new SlotInvSlot(((BlockEntityWeldingMachine) tileEntity1).input_slot,
                    0, -20, 84
            ));
        }
    }


}
