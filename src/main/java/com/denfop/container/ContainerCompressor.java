package com.denfop.container;

import com.denfop.tiles.reactors.gas.compressor.TileEntityBaseCompressor;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCompressor extends ContainerFullInv<TileEntityBaseCompressor> {

    public ContainerCompressor(TileEntityBaseCompressor tileEntityBaseCompressor, EntityPlayer var1) {
        super(var1, tileEntityBaseCompressor, 188, 209);
    }

}
