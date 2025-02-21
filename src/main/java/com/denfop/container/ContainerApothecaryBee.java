package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityApothecaryBee;
import com.denfop.tiles.mechanism.TileEntityChickenFarm;
import com.denfop.tiles.mechanism.TileEntityCowFarm;
import com.denfop.tiles.mechanism.TileEntityPigFarm;
import com.denfop.tiles.mechanism.TileEntityPlantFertilizer;
import com.denfop.tiles.mechanism.TileEntityWeeder;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerApothecaryBee extends ContainerFullInv<TileEntityApothecaryBee> {

    public ContainerApothecaryBee(TileEntityApothecaryBee tileEntityChickenFarm, EntityPlayer var1) {
        super(tileEntityChickenFarm, var1);
    }

}
