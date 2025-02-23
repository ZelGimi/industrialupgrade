package com.denfop.container;

import com.denfop.tiles.geothermalpump.TileEntityGeothermalGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGeothermalgenerator extends ContainerFullInv<TileEntityGeothermalGenerator> {

    public ContainerGeothermalgenerator(TileEntityGeothermalGenerator tileEntityGeothermalGenerator, EntityPlayer var1) {
        super(tileEntityGeothermalGenerator, var1);
    }

}
