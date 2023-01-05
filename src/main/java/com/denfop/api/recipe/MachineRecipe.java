package com.denfop.api.recipe;

import java.util.List;

public class MachineRecipe {

    private final BaseMachineRecipe recipe;
    private final List<Integer> list;

    public MachineRecipe(BaseMachineRecipe recipe, List<Integer> list) {
        this.recipe = recipe;
        this.list = list;
    }

    public BaseMachineRecipe getRecipe() {
        return recipe;
    }

    public List<Integer> getList() {
        return list;
    }

}
