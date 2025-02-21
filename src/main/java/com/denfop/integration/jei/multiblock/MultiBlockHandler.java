package com.denfop.integration.jei.multiblock;


import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.multiblock.MultiBlockSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultiBlockHandler {

    private static final List<MultiBlockHandler> recipes = new ArrayList<>();
    private final String name;
    private final MultiBlockStructure structure;


    public MultiBlockHandler(String name, MultiBlockStructure structure) {
        this.name = name;
        this.structure = structure;
    }

    public static List<MultiBlockHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static MultiBlockHandler addRecipe(
            String name, MultiBlockStructure structure
    ) {
        MultiBlockHandler recipe = new MultiBlockHandler(name, structure);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        for (Map.Entry<String, MultiBlockStructure> entry : MultiBlockSystem.getInstance().mapMultiBlocks.entrySet()) {
            addRecipe(
                    entry.getKey(),
                    entry.getValue()
            );


        }
    }

    public MultiBlockStructure getStructure() {
        return structure;
    }

    public String getName() {
        return name;
    }


}
