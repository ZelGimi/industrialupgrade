package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityPlantGardener;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPlantGardener extends ContainerFullInv<TileEntityPlantGardener> {

    public ContainerPlantGardener(TileEntityPlantGardener tileEntityChickenFarm, EntityPlayer var1) {
        super(tileEntityChickenFarm, var1);
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.output, i, 10 + i * 18, 18));
        }
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 10 + i * 18, 65));
        }
    }

}
