package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityBatteryFactory;
import com.denfop.tiles.mechanism.TileEntityProbeAssembler;
import com.denfop.tiles.mechanism.TileEntityRoverAssembler;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerProbeAssembler extends ContainerFullInv<TileEntityProbeAssembler> {

    public ContainerProbeAssembler(TileEntityProbeAssembler tileEntityBatteryFactory, EntityPlayer var1) {
        super(var1, tileEntityBatteryFactory,210,216+18);
        for (int i = 0; i < 2; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48+18 + i * 18,
                    17
            ));
        }
        for (int i = 2; i < 6; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48 + (i-2) * 18,
                    17+18
            ));
        }
        for (int i = 6; i < 14; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48-36 + (i-6) * 18,
                    17+36
            ));
        }
        for (int i = 14; i < 22; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48-36 + (i-14) * 18,
                    17+36+18
            ));
        }
        for (int i = 22; i < 30; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48-36 + (i-22) * 18,
                    17+36+18+18
            ));
        }
        for (int i = 30; i < 34; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48 + (i-30) * 18,
                    17+18+36+36
            ));
        }
        for (int i = 34; i < 36; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48+18 + (i-34) * 18,
                    17+36+36+36
            ));
        }


        this.addSlotToContainer(new SlotInvSlot(
                tileEntityBatteryFactory.outputSlot,
                0,
                176+30, 64
        ));

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityBatteryFactory.upgradeSlot, i, 192, 118 + i * 18));
        }
    }

}
