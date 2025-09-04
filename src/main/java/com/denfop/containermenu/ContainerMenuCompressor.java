package com.denfop.containermenu;

import com.denfop.blockentity.reactors.gas.compressor.BlockEntityBaseCompressor;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuCompressor extends ContainerMenuFullInv<BlockEntityBaseCompressor> {

    public ContainerMenuCompressor(BlockEntityBaseCompressor tileEntityBaseCompressor, Player var1) {
        super(var1, tileEntityBaseCompressor, 188, 209);
    }

}
