package com.denfop.api.space;

import java.util.ArrayList;
import java.util.List;

public class System implements ISystem {

    private final String name;
    List<IStar> stars = new ArrayList<>();

    public System(String name) {
        this.name = name;
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

}
