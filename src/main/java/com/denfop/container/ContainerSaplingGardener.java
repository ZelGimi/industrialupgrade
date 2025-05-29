package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntitySaplingGardener;
import net.minecraft.world.entity.player.Player;

public class ContainerSaplingGardener extends ContainerFullInv<TileEntitySaplingGardener> {

    public ContainerSaplingGardener(TileEntitySaplingGardener tileEntityChickenFarm, Player var1) {
        super(tileEntityChickenFarm, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.slot, 0, 80, 18));
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 10 + i * 18, 65));
        }
    }

}
