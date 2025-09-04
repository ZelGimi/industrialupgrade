package com.denfop.integration.jei.charged_redstone;


import com.denfop.IUItem;
import com.denfop.utils.Localization;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChargedRedstoneHandler {

    private static final List<ChargedRedstoneHandler> recipes = new ArrayList<>();
    public final ItemStack input;
    public final String output;


    public ChargedRedstoneHandler(ItemStack input, String output) {
        this.input = input;
        this.output = output;
    }

    public static List<ChargedRedstoneHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ChargedRedstoneHandler addRecipe(
            ItemStack input, String output
    ) {
        ChargedRedstoneHandler recipe = new ChargedRedstoneHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        addRecipe(
                new ItemStack(IUItem.charged_redstone.getItem()),
                Localization.translate("charged_redstone.info")
        );
    }


}
