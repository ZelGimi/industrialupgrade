package com.denfop.container;

import com.denfop.tiles.base.TileLimiter;
import net.minecraft.world.entity.player.Player;

public class ContainerBlockLimiter extends ContainerFullInv<TileLimiter> {

    public ContainerBlockLimiter(TileLimiter tileEntityLimiter, Player entityPlayer) {
        super(entityPlayer, tileEntityLimiter, 166);
        addSlotToContainer(new SlotInvSlot(tileEntityLimiter.slot, 0, 105, 27));


    }


}
