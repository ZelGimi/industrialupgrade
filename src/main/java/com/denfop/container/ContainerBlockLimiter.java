package com.denfop.container;

import com.denfop.tiles.base.TileLimiter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBlockLimiter extends ContainerFullInv<TileLimiter> {

    public ContainerBlockLimiter(TileLimiter tileEntityLimiter, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntityLimiter, 166);
        addSlotToContainer(new SlotInvSlot(tileEntityLimiter.slot, 0, 105, 27));


    }


}
