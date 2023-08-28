package com.denfop.container;

import com.denfop.tiles.mechanism.cooling.TileCooling;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCoolMachine extends ContainerFullInv<TileCooling> {

    public ContainerCoolMachine(EntityPlayer entityPlayer, TileCooling tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);
    }


}
