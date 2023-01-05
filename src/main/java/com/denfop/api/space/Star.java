package com.denfop.api.space;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class Star implements IStar {

    private final String name;
    private final ISystem system;
    private final ResourceLocation location;
    List<IPlanet> planetList;

    public Star(String name, ISystem system, ResourceLocation location) {
        this.name = name;
        this.system = system;
        this.location = location;
        this.planetList = new ArrayList<>();
        SpaceNet.instance.addStar(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ISystem getSystem() {
        return this.system;
    }


    @Override
    public ResourceLocation getResource() {
        return this.location;
    }

    @Override
    public List<IPlanet> getPlanetList() {
        return this.planetList;
    }

}
