package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntitySoilAnalyzer;
import net.minecraft.world.entity.player.Player;

public class ContainerSoilAnalyzer extends ContainerBase<TileEntitySoilAnalyzer> {

    public ContainerSoilAnalyzer(TileEntitySoilAnalyzer tileEntitySoilAnalyzer, Player var1) {
        super(tileEntitySoilAnalyzer, null);
        this.inventory = var1.getInventory();
        this.player = var1;
    }

}
