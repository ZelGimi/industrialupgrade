package com.denfop.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITemperatureMechanism {

    boolean process(ITemperature tile);

    short transfer(ITemperature receiver, ITemperature extract);

    void work(ITemperature tile);

    boolean hasHeaters(ITemperature tile);

    boolean hasHeaters(World world, BlockPos pos);
}
