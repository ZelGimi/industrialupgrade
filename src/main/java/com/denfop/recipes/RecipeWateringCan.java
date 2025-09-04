package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.powerutils.PowerUtils;
import com.wateringcan.WateringCan;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RecipeWateringCan {
    public static void register() {
        Recipes.recipe.addRecipe(new ItemStack(WateringCan.simple_watering_can.getItem()), "  A", "AB ", "AAA",

                Character.valueOf('A'), "c:plates/bismuth",

                Character.valueOf('B'), Items.WATER_BUCKET);
        Recipes.recipe.addRecipe(new ItemStack(WateringCan.adv_watering_can.getItem()), "  A", "AB ", "AAA",

                Character.valueOf('A'), "c:plates/muntsa",

                Character.valueOf('B'), WateringCan.simple_watering_can.getItem());
        Recipes.recipe.addRecipe(new ItemStack(WateringCan.imp_watering_can.getItem()), "  A", "AB ", "AAA",

                Character.valueOf('A'), "c:plates/stellite",

                Character.valueOf('B'), WateringCan.adv_watering_can.getItem());
    }
}
