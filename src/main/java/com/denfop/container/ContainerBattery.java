package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityBatteryFactory;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBattery extends ContainerFullInv<TileEntityBatteryFactory> {

    public ContainerBattery(TileEntityBatteryFactory tileEntityBatteryFactory, EntityPlayer var1) {
        super(tileEntityBatteryFactory,var1);
        for(int i =0;i < 9;i++){
            this.addSlotToContainer(new SlotInvSlot(tileEntityBatteryFactory.inputSlotA, i, 10 + (i % 3) * 18, 17 + (i/3)*18));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntityBatteryFactory.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityBatteryFactory.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
