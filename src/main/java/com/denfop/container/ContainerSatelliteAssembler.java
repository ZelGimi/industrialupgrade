package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityProbeAssembler;
import com.denfop.tiles.mechanism.TileEntitySatelliteAssembler;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSatelliteAssembler extends ContainerFullInv<TileEntitySatelliteAssembler> {

    public ContainerSatelliteAssembler(TileEntitySatelliteAssembler tileEntityBatteryFactory, EntityPlayer var1) {
        super(var1, tileEntityBatteryFactory,178,225);
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48+18+3 + i * 18,
                    17-4
            ));
        }
        for (int i = 4; i < 9; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48+3 + (i-4) * 18,
                    17+18-4
            ));
        }
        for (int i = 9; i < 15; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48-18+3 + (i-9) * 18,
                    17+36-4
            ));
        }
        for (int i = 15; i < 22; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48-18-18+3 + (i-15) * 18,
                    17+36+18-4
            ));
        }
        for (int i = 22; i < 28; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48-18-18+3 + (i-22) * 18,
                    17+36+36-4
            ));
        }
        for (int i = 28; i < 33; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48-18-18+3 + (i-28) * 18,
                    17+36+36+18-4
            ));
        }
        for (int i = 33; i < 36; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48-18 + (i-33)+3 * 18,
                    17+36+36+36-4
            ));
        }



        this.addSlotToContainer(new SlotInvSlot(
                tileEntityBatteryFactory.outputSlot,
                0,
                179, 38
        ));

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityBatteryFactory.upgradeSlot, i, 179, 77 + i * 18));
        }
    }

}
