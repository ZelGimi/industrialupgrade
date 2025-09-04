package com.denfop.containermenu;

import com.denfop.blockentity.geothermalpump.BlockEntityGeothermalExchanger;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGeothermalExchanger extends ContainerMenuFullInv<BlockEntityGeothermalExchanger> {

    public ContainerMenuGeothermalExchanger(BlockEntityGeothermalExchanger tileEntityGeothermalExchanger, Player var1) {
        super(tileEntityGeothermalExchanger, var1);
    }

}
