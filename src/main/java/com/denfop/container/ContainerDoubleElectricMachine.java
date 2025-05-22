package com.denfop.container;

import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import com.denfop.tiles.base.TilePainting;
import com.denfop.tiles.mechanism.TileEntityUpgradeRover;
import com.denfop.tiles.mechanism.TileSunnariumPanelMaker;
import com.denfop.tiles.mechanism.dual.TileEnrichment;
import com.denfop.tiles.mechanism.dual.TileSynthesis;
import com.denfop.tiles.mechanism.dual.TileUpgradeBlock;
import com.denfop.tiles.mechanism.dual.heat.TileAlloySmelter;
import com.denfop.tiles.mechanism.dual.heat.TileWeldingMachine;
import net.minecraft.world.entity.player.Player;

public class ContainerDoubleElectricMachine extends ContainerBaseDoubleElectricMachine {

    public ContainerDoubleElectricMachine(
            Player entityPlayer,
            TileDoubleElectricMachine tileEntity1,
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


    public ContainerDoubleElectricMachine(
            Player entityPlayer,
            TileDoubleElectricMachine tileEntity1,
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
            if (tileEntity1 instanceof TileEnrichment) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                        i, 144, 9 + i * 18
                ));
            } else if (tileEntity1 instanceof TileSunnariumPanelMaker) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                        i, upgradeX + 5, upgradeY + 1 + i * 18
                ));
            } else if (tileEntity1 instanceof TileSynthesis) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                        i, upgradeX + 5, upgradeY + 1 + i * 18
                ));
            }
            else if (tileEntity1 instanceof TileWeldingMachine) {
                  addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                           i, upgradeX + 5, upgradeY + i * 18
                 ));
                }
            else if (tileEntity1 instanceof TileUpgradeBlock
                || tileEntity1 instanceof TileEntityUpgradeRover
            ) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                        i, upgradeX + 5, upgradeY + 1 + i * 18
                ));
            } else if (tileEntity1 instanceof TilePainting) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                        i, upgradeX + 5, upgradeY + 1 + i * 18
                ));
            } else {
                addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                        i, upgradeX, upgradeY + i * 18
                ));
            }

        }
        if (tileEntity1 instanceof TileAlloySmelter) {
            addSlotToContainer(new SlotInvSlot(((TileAlloySmelter) tileEntity1).input_slot,
                    0, -20, 84
            ));
        }
        if (tileEntity1 instanceof TileSynthesis) {
            addSlotToContainer(new SlotInvSlot(((TileSynthesis) tileEntity1).input_slot,
                    0, -20, 84
            ));
        }
        if (tileEntity1 instanceof TileEnrichment) {
            addSlotToContainer(new SlotInvSlot(((TileEnrichment) tileEntity1).input_slot,
                    0, -20, 84
            ));
        }
        if (tileEntity1 instanceof TileSunnariumPanelMaker) {
            addSlotToContainer(new SlotInvSlot(((TileSunnariumPanelMaker) tileEntity1).input_slot,
                    0, -20, 84
            ));
        }
        if (tileEntity1 instanceof TileWeldingMachine) {
            addSlotToContainer(new SlotInvSlot(((TileWeldingMachine) tileEntity1).input_slot,
                    0, -20, 84
            ));
        }
    }


}
