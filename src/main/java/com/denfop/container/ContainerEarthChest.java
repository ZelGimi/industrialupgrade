package com.denfop.container;

import com.denfop.tiles.quarry_earth.TileEntityChest;
import net.minecraft.world.entity.player.Player;

public class ContainerEarthChest extends ContainerFullInv<TileEntityChest> {

    public ContainerEarthChest(TileEntityChest tileEntityChest, Player var1) {
        super(tileEntityChest, var1);
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChest.getSlot(), i, 10 + i * 18, 30));
        }
    }

}
