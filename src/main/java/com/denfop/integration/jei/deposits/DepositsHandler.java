package com.denfop.integration.jei.deposits;


import com.denfop.world.WorldBaseGen;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.VeinType;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DepositsHandler {

    private static final List<DepositsHandler> recipes = new ArrayList<>();
    private final VeinType veinType;

    public DepositsHandler(VeinType veinType) {
        this.veinType = veinType;
    }

    public static List<DepositsHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static DepositsHandler addRecipe(
            VeinType veinType
    ) {
        DepositsHandler recipe = new DepositsHandler(veinType);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        for (VeinType veinType : WorldBaseGen.veinTypes) {
            addRecipe(veinType);


        }
    }

    public List<ItemStack> getInputs() {

        List<ItemStack> stack = new ArrayList<>();
        if (veinType.getHeavyOre() != null) {
            stack.add(new ItemStack(veinType.getHeavyOre().getBlock(), 1));
        }
        for (ChanceOre ore : veinType.getOres()) {
            stack.add(new ItemStack(ore.getBlock().getBlock(), 1));
        }
        return stack;
    }

    public VeinType getVeinType() {
        return veinType;
    }


}
