package com.denfop.container;

import com.denfop.tiles.geothermalpump.TileEntityGeothermalGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerGeothermalgenerator extends ContainerFullInv<TileEntityGeothermalGenerator> {

    public ContainerGeothermalgenerator(TileEntityGeothermalGenerator tileEntityGeothermalGenerator, Player var1) {
        super(tileEntityGeothermalGenerator, var1);
    }

}
