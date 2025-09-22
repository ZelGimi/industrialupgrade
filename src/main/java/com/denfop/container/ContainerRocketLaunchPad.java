package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityRocketLaunchPad;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerRocketLaunchPad extends ContainerFullInv<TileEntityRocketLaunchPad> {

    public ContainerRocketLaunchPad(TileEntityRocketLaunchPad tileEntityRocketLaunchPad, EntityPlayer var1) {
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
