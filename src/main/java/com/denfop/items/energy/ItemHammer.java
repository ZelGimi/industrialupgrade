package com.denfop.items.energy;

import java.util.HashSet;

public class ItemHammer extends ItemToolIU {

    public ItemHammer() {
        super("molot", 2, 1, new HashSet<>());
        this.setHarvestLevel("pickaxe", 1);
        setNoRepair();
    }

    @Override
    public void registerModels() {
        this.registerModels(this.name);
    }

}
