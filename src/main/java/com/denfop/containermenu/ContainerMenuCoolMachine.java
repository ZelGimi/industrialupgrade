package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.cooling.BlockEntityCooling;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuCoolMachine extends ContainerMenuFullInv<BlockEntityCooling> {

    public ContainerMenuCoolMachine(Player entityPlayer, BlockEntityCooling tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);
    }


}
