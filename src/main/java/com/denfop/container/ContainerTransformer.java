package com.denfop.container;

import com.denfop.tiles.base.TileTransformer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerTransformer extends ContainerFullInv<TileTransformer> {

    public ContainerTransformer(EntityPlayer player, TileTransformer tileEntity1, int height) {
        super(player, tileEntity1, height);
    }


}
