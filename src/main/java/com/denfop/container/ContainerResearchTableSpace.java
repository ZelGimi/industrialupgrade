package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityResearchTableSpace;
import net.minecraft.world.entity.player.Player;

public class ContainerResearchTableSpace extends ContainerFullInv<TileEntityResearchTableSpace> {

    public final Player player;

    public ContainerResearchTableSpace(TileEntityResearchTableSpace tileEntityResearchTableSpace, Player var1) {
        super(var1, tileEntityResearchTableSpace, 250);
        this.addSlotToContainer(new SlotInvSlot(tileEntityResearchTableSpace.slotLens, 0, 197, 230));
        this.player = var1;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        this.base.timer = 5;
    }



}
