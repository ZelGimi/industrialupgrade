package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityRocketLaunchPad;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuRocketLaunchPad extends ContainerMenuFullInv<BlockEntityRocketLaunchPad> {

    public ContainerMenuRocketLaunchPad(BlockEntityRocketLaunchPad tileEntityRocketLaunchPad, Player var1) {
        super(var1, tileEntityRocketLaunchPad, 222);
        for (int i = 0; i < 27; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityRocketLaunchPad.outputSlot,
                    i,
                    8 + (i % 9) * 18,
                    82 + (i / 9) * 18));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntityRocketLaunchPad.roverSlot, 0, 80, 24));
    }

}
