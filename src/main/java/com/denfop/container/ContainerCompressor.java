package com.denfop.container;

import com.denfop.tiles.reactors.gas.compressor.TileEntityBaseCompressor;
import net.minecraft.world.entity.player.Player;

public class ContainerCompressor extends ContainerFullInv<TileEntityBaseCompressor> {

    public ContainerCompressor(TileEntityBaseCompressor tileEntityBaseCompressor, Player var1) {
        super(var1, tileEntityBaseCompressor, 188, 209);
    }

}
