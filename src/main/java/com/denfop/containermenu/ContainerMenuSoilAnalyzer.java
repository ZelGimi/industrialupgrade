package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntitySoilAnalyzer;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSoilAnalyzer extends ContainerMenuBase<BlockEntitySoilAnalyzer> {

    public ContainerMenuSoilAnalyzer(BlockEntitySoilAnalyzer tileEntitySoilAnalyzer, Player var1) {
        super(tileEntitySoilAnalyzer, null);
        this.inventory = var1.getInventory();
        this.player = var1;
    }

}
