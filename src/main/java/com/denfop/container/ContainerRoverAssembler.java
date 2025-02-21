package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityBatteryFactory;
import com.denfop.tiles.mechanism.TileEntityRoverAssembler;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerRoverAssembler extends ContainerFullInv<TileEntityRoverAssembler> {

    public ContainerRoverAssembler(TileEntityRoverAssembler tileEntityBatteryFactory, EntityPlayer var1) {
        super(var1, tileEntityBatteryFactory,210,216);
        for (int i = 0; i < 5; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48 + i * 18,
                    17
            ));
        }
     for (int i = 5; i < 12; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48-18 + (i-5) * 18,
                    17+18
            ));
        }
        for (int i = 12; i < 21; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48-36 + (i-12) * 18,
                    17+36
            ));
        }
        for (int i = 21; i < 30; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48-36 + (i-21) * 18,
                    17+36+18
            ));
        }
        for (int i = 30; i < 37; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48-18 + (i-30) * 18,
                    17+36+18+18
            ));
        }
        for (int i = 37; i < 40; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48 + (i-37) * 36,
                    17+36+18+18+18
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
