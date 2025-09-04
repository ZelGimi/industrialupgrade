package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityLimiter;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuBlockLimiter extends ContainerMenuFullInv<BlockEntityLimiter> {

    public ContainerMenuBlockLimiter(BlockEntityLimiter tileEntityLimiter, Player entityPlayer) {
        super(entityPlayer, tileEntityLimiter, 166);
        addSlotToContainer(new SlotInvSlot(tileEntityLimiter.slot, 0, 105, 27));


    }


}
