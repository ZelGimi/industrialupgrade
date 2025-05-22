package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityPlantFertilizer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPlantFertilizer extends ContainerFullInv<TileEntityPlantFertilizer> {

    public ContainerPlantFertilizer(TileEntityPlantFertilizer tileEntityChickenFarm, EntityPlayer var1) {
        super(tileEntityChickenFarm, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.slot, 0, 80, 18));
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityChickenFarm.upgradeSlot, i, 10 + i * 18, 65));
        }
    }

}
