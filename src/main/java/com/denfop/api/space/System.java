package com.denfop.api.space;

import java.util.ArrayList;
import java.util.List;

public class System implements ISystem {

    private final String name;
    private final int distance;
    List<IStar> stars = new ArrayList<>();

    public System(String name) {
        this.name = name;
        this.distance = 0;
        SpaceNet.instance.addSystem(this);
    }

    public System(String name, int distance) {
        this.name = name;
        this.distance = distance;
        SpaceNet.instance.addSystem(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<IStar> getStarList() {
        return stars;
    }

    @Override
    public int getDistanceFromSolar() {
        return distance;
    }

}
