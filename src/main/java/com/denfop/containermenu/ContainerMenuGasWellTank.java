package com.denfop.containermenu;

import com.denfop.blockentity.gaswell.BlockEntityGasWellTank;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGasWellTank extends ContainerMenuFullInv<BlockEntityGasWellTank> {

    public ContainerMenuGasWellTank(BlockEntityGasWellTank tileEntityGeothermalExchanger, Player var1) {
        super(tileEntityGeothermalExchanger, var1);
    }

}
