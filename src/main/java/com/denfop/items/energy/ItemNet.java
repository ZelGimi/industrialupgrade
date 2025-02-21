package com.denfop.items.energy;

import java.util.HashSet;

public class ItemNet extends ItemToolIU{

    public ItemNet() {
        super("net", 0, 0, new HashSet<>());
        this.efficiency = 1f;
    }

    @Override
    public void registerModels() {
        registerModels(name);
    }

}
