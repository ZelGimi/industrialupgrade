package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityBatteryFactory;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuBattery extends ContainerMenuFullInv<BlockEntityBatteryFactory> {

    public ContainerMenuBattery(BlockEntityBatteryFactory tileEntityBatteryFactory, Player var1) {
        super(tileEntityBatteryFactory, var1);
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    10 + (i % 3) * 18,
                    17 + (i / 3) * 18
            ));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntityBatteryFactory.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityBatteryFactory.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
