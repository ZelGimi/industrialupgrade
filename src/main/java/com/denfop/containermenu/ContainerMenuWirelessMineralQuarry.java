package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityWirelessMineralQuarry;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuWirelessMineralQuarry extends ContainerMenuFullInv<BlockEntityWirelessMineralQuarry> {

    public ContainerMenuWirelessMineralQuarry(BlockEntityWirelessMineralQuarry tileEntityWirelessMineralQuarry, Player var1) {
        super(tileEntityWirelessMineralQuarry, var1);
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityWirelessMineralQuarry.invslot,
                    i, 152, 8 + i * 18
            ));

        }
        for (int i = 0; i < 18; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityWirelessMineralQuarry.output, i, 30 + (i % 6) * 18, 28 + (i / 6) * 18
            ));

        }
    }

}
