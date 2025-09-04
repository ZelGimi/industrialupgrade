package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityAutoFuse;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuAutoFuse extends ContainerMenuFullInv<BlockEntityAutoFuse> {

    public ContainerMenuAutoFuse(BlockEntityAutoFuse tileEntityAutoFuse, Player var1) {
        super(tileEntityAutoFuse, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityAutoFuse.slotBomb, 0, 82, 32));
    }

}
