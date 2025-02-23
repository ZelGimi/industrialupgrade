package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityAutoFuse;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAutoFuse extends ContainerFullInv<TileEntityAutoFuse> {

    public ContainerAutoFuse(TileEntityAutoFuse tileEntityAutoFuse, EntityPlayer var1) {
        super(tileEntityAutoFuse, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityAutoFuse.slotBomb, 0, 82, 32));
    }

}
