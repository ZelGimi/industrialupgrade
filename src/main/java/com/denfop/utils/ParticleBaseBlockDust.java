package com.denfop.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleBlockDust;
import net.minecraft.world.World;

public class ParticleBaseBlockDust extends ParticleBlockDust {

    public ParticleBaseBlockDust(
            final World worldIn,
            final double xCoordIn,
            final double yCoordIn,
            final double zCoordIn,
            final double xSpeedIn,
            final double ySpeedIn,
            final double zSpeedIn,
            final IBlockState state
    ) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
    }

}
