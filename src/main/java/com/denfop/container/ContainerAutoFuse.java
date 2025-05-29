package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityAutoFuse;
import net.minecraft.world.entity.player.Player;

public class ContainerAutoFuse extends ContainerFullInv<TileEntityAutoFuse> {

    public ContainerAutoFuse(TileEntityAutoFuse tileEntityAutoFuse, Player var1) {
        super(tileEntityAutoFuse, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityAutoFuse.slotBomb, 0, 82, 32));
    }

}
