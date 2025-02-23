package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityRocketAssembler;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerRocketAssembler extends ContainerFullInv<TileEntityRocketAssembler> {

    public ContainerRocketAssembler(TileEntityRocketAssembler tileEntityBatteryFactory, EntityPlayer var1) {
        super(var1, tileEntityBatteryFactory, 210, 216 + 18 + 22);
        this.addSlotToContainer(new SlotInvSlot(
                tileEntityBatteryFactory.inputSlotA,
                0,
                48 + 0 * 18,
                17 - 10
        ));
        for (int i = 1; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(
                    tileEntityBatteryFactory.inputSlotA,
                    i,
                    48 - 18 + (i - 1) * 18,
                    17 + 18 - 10
            ));
        }
        for (int j = 1; j < 7; j++) {
            for (int i = 4+5*(j-1); i < 9+5*(j-1); i++) {
                this.addSlotToContainer(new SlotInvSlot(
                        tileEntityBatteryFactory.inputSlotA,
                        i,
                        48 - 18 - 18 + (i - (4 + 5 * (j - 1))) * 18,
                        17 + 18 - 10 + 18 * j
                ));
            }
        }
        for (int j = 7; j < 8; j++) {
            for (int i = 4+5*(j-1); i < 7+5*(j-1); i++) {
                this.addSlotToContainer(new SlotInvSlot(
                        tileEntityBatteryFactory.inputSlotA,
                        i,
                        48 - 18 + (i - (4 + 5 * (j - 1))) * 18,
                        17 + 18 - 10 + 18 * j
                ));
            }
        }

        this.addSlotToContainer(new SlotInvSlot(
                tileEntityBatteryFactory.outputSlot,
                0,
                156 , 64
        ));

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityBatteryFactory.upgradeSlot, i, 192, 118 + i * 18));
        }
    }

}
