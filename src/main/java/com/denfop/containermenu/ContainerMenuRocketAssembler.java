package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityRocketAssembler;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuRocketAssembler extends ContainerMenuFullInv<BlockEntityRocketAssembler> {

    public ContainerMenuRocketAssembler(BlockEntityRocketAssembler tileEntityBatteryFactory, Player var1) {
        super(var1, tileEntityBatteryFactory, 178, 216 + 18 + 22);
        this.addSlotToContainer(new SlotInvSlot(
                tileEntityBatteryFactory.inputSlotA,
                0,
                48 - 4 + 0 * 18,
                17 - 10 + 3
        ));
        for (int i = 1; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48 - 18 - 4 + (i - 1) * 18,
                    17 + 3 + 18 - 10
            ));
        }
        for (int j = 1; j < 7; j++) {
            for (int i = 4 + 5 * (j - 1); i < 9 + 5 * (j - 1); i++) {
                this.addSlotToContainer(new SlotInvSlot(
                        tileEntityBatteryFactory.inputSlotA,
                        i,
                        48 - 4 - 18 - 18 + (i - (4 + 5 * (j - 1))) * 18,
                        17 + 3 + 18 - 10 + 18 * j
                ));
            }
        }
        for (int j = 7; j < 8; j++) {
            for (int i = 4 + 5 * (j - 1); i < 7 + 5 * (j - 1); i++) {
                this.addSlotToContainer(new SlotInvSlot(
                        tileEntityBatteryFactory.inputSlotA,
                        i,
                        48 - 18 - 4 + (i - (4 + 5 * (j - 1))) * 18,
                        17 + 3 + 18 - 10 + 18 * j
                ));
            }
        }

        this.addSlotToContainer(new SlotInvSlot(
                tileEntityBatteryFactory.outputSlot,
                0,
                142, 91
        ));

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityBatteryFactory.upgradeSlot, i, 98 + i * 18, 155));
        }
    }

}
