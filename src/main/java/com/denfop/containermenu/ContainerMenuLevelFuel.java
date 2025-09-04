package com.denfop.containermenu;

import com.denfop.blockentity.reactors.water.controller.BlockEntityMainController;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuLevelFuel extends ContainerMenuFullInv<BlockEntityMainController> {

    public ContainerMenuLevelFuel(BlockEntityMainController main, Player var1) {
        super(var1, main);
    }

}
