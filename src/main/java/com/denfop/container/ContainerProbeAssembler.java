package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityProbeAssembler;
import net.minecraft.world.entity.player.Player;

public class ContainerProbeAssembler extends ContainerFullInv<TileEntityProbeAssembler> {

    public ContainerProbeAssembler(TileEntityProbeAssembler tileEntityBatteryFactory, Player var1) {
        super(var1, tileEntityBatteryFactory, 178, 225);
        for (int i = 0; i < 2; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    62 + i * 18,
                    14
            ));
        }
        for (int i = 2; i < 6; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48 - 4 + (i - 2) * 18,
                    14 + 18
            ));
        }
        for (int i = 6; i < 14; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48 - 36 - 4 + (i - 6) * 18,
                    14 + 36
            ));
        }
        for (int i = 14; i < 22; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48 - 36 - 4 + (i - 14) * 18,
                    14 + 36 + 18
            ));
        }
        for (int i = 22; i < 30; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48 - 36 - 4 + (i - 22) * 18,
                    14 + 36 + 18 + 18
            ));
        }
        for (int i = 30; i < 34; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48 - 4 + (i - 30) * 18,
                    14 + 18 + 36 + 36
            ));
        }
        for (int i = 34; i < 36; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48 + 18 - 4 + (i - 34) * 18,
                    14 + 36 + 36 + 36
            ));
        }


        this.addSlotToContainer(new SlotInvSlot(
                tileEntityBatteryFactory.outputSlot,
                0,
                186, 67
        ));

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityBatteryFactory.upgradeSlot, i, 186, 106 + i * 18));
        }
    }

}
