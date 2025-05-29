package com.denfop.api.space;


import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IStar extends IBody {


    ResourceLocation getResource();

    List<IPlanet> getPlanetList();

    List<IAsteroid> getAsteroidList();
}
