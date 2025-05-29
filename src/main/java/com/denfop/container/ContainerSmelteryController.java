package com.denfop.container;

import com.denfop.tiles.smeltery.TileEntitySmelteryController;
import net.minecraft.world.entity.player.Player;

public class ContainerSmelteryController extends ContainerFullInv<TileEntitySmelteryController> {

    public ContainerSmelteryController(
            TileEntitySmelteryController tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 166);

    }

}
