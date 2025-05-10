package com.denfop.container;

import com.denfop.tiles.base.TileTransformer;
import net.minecraft.world.entity.player.Player;

public class ContainerTransformer extends ContainerFullInv<TileTransformer> {

    public ContainerTransformer(Player player, TileTransformer tileEntity1, int height) {
        super(player, tileEntity1, height);
    }


}
