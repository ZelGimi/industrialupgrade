package com.denfop.api.heat;

import com.denfop.api.ITemperature;
import net.minecraft.world.World;

public interface IHeatTile {

    World getWorldTile();

    ITemperature getITemperature();

}
