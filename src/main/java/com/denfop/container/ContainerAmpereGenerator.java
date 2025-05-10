package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityAmpereGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerAmpereGenerator extends ContainerFullInv<TileEntityAmpereGenerator> {

    public ContainerAmpereGenerator(Player entityPlayer, TileEntityAmpereGenerator tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
