package com.denfop.containermenu;

import com.denfop.blockentity.reactors.heat.fueltank.BlockEntityMainTank;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuHeatTank extends ContainerMenuFullInv<BlockEntityMainTank> {

    public ContainerMenuHeatTank(BlockEntityMainTank tileEntityMainTank, Player var1) {
        super(var1, tileEntityMainTank, 188, 210);
    }

}
