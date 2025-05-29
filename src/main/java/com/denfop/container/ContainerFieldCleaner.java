package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityFieldCleaner;
import net.minecraft.world.entity.player.Player;

public class ContainerFieldCleaner extends ContainerFullInv<TileEntityFieldCleaner> {

    public ContainerFieldCleaner(TileEntityFieldCleaner tileEntityChickenFarm, Player var1) {
        super(tileEntityChickenFarm, var1);
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 150, 10 + i * 18));
        }
    }

}
