package com.denfop.containermenu;

import com.denfop.blockentity.geothermalpump.BlockEntityGeothermalGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGeothermalgenerator extends ContainerMenuFullInv<BlockEntityGeothermalGenerator> {

    public ContainerMenuGeothermalgenerator(BlockEntityGeothermalGenerator tileEntityGeothermalGenerator, Player var1) {
        super(tileEntityGeothermalGenerator, var1);
    }

}
