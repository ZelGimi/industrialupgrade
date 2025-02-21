package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityPlantCollector;
import com.denfop.tiles.mechanism.TileEntityTreeBreaker;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPlantCollector extends ContainerFullInv<TileEntityPlantCollector> {

    public ContainerPlantCollector(TileEntityPlantCollector tileEntityChickenFarm, EntityPlayer var1) {
        super(tileEntityChickenFarm, var1);
        for (int i = 0; i < 18; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.output, i, 10 + (i % 9) * 18, 18 + 18 * (i / 9)));
        }
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 10 + i * 18, 65));
        }
    }

}
