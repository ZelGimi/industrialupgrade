package com.denfop.container;

import com.denfop.tiles.mechanism.cooling.TileCooling;
import net.minecraft.world.entity.player.Player;

public class ContainerCoolMachine extends ContainerFullInv<TileCooling> {

    public ContainerCoolMachine(Player entityPlayer, TileCooling tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);
    }


}
