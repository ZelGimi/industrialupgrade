package com.denfop.containermenu;

import com.denfop.blockentity.reactors.gas.cell.BlockEntityMainTank;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGasTank extends ContainerMenuFullInv<BlockEntityMainTank> {

    public ContainerMenuGasTank(BlockEntityMainTank tileEntityMainTank, Player var1) {
        super(var1, tileEntityMainTank, 188, 209);
    }

}
