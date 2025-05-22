package com.denfop.api.space;

import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface IStar extends IBody {


    ResourceLocation getResource();

    List<IPlanet> getPlanetList();

    List<IAsteroid> getAsteroidList();

}
