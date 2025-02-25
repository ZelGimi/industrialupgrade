package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityBatteryFactory;
import com.denfop.tiles.mechanism.TileEntityRoverAssembler;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerRoverAssembler extends ContainerFullInv<TileEntityRoverAssembler> {

    public ContainerRoverAssembler(TileEntityRoverAssembler tileEntityBatteryFactory, EntityPlayer var1) {
        super(var1, tileEntityBatteryFactory,178,213);
        for (int i = 0; i < 5; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    44 + i * 18,
                    16
            ));
        }
     for (int i = 5; i < 12; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    26 + (i-5) * 18,
                    34
            ));
        }
        for (int i = 12; i < 21; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    8 + (i-12) * 18,
                    52
            ));
        }
        for (int i = 21; i < 30; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    8 + (i-21) * 18,
                    52+18
            ));
        }
        for (int i = 30; i < 37; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    26 + (i-30) * 18,
                    52+18+18
            ));
        }
        for (int i = 37; i < 40; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    44 + (i-37) * 36,
                    106
            ));
        }
        this.addSlotToContainer(new SlotInvSlot(
                tileEntityBatteryFactory.outputSlot,
                0,
                176+28, 61
        ));

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityBatteryFactory.upgradeSlot, i, 204, 100 + i * 18));
        }
    }

}
