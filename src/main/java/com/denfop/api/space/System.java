package com.denfop.api.space;

public class System implements ISystem {

    private final String name;

    public System(String name) {
        this.name = name;
        SpaceNet.instance.addSystem(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

}
