package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.blockentity.mechanism.BlockEntityGenerationMicrochip;
import com.powerutils.PowerUtils;
import net.minecraft.world.item.ItemStack;

public class RecipesPowerUtils {
    public static void register() {
       Recipes.recipe.addRecipe(new ItemStack(PowerUtils.module_ic.getItem()), new Object[]{
                "ABA", "CDC", "ABA",

                Character.valueOf('A'), IUItem.copperCableItem,

                Character.valueOf('B'), new ItemStack(IUItem.tranformer.getItem(8), 1),

                Character.valueOf('C'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 3),

                Character.valueOf('D'),
                new ItemStack(IUItem.electricblock.getItem(5), 1)});
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(PowerUtils.module_fe), new Object[]{
                "ABA", "CDC", "ABA",

                Character.valueOf('A'), ItemStackHelper.fromData(IUItem.basecircuit),

                Character.valueOf('B'), ItemStackHelper.fromData(IUItem.tranformer, 1, 8),

                Character.valueOf('C'), ItemStackHelper.fromData(IUItem.basecircuit, 1, 4),

                Character.valueOf('D'),
                new ItemStack(IUItem.core.getStack(1), 1)});

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(PowerUtils.itemPowerConverter, 1, 0), new Object[]{
                "ABA", "CDE", "ABA",

                Character.valueOf('A'), IUItem.advancedAlloy,

                Character.valueOf('B'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 2),

                Character.valueOf('C'), ItemStackHelper.fromData(PowerUtils.module_ic),

                Character.valueOf('D'),
                IUItem.machine,

                Character.valueOf('E'), ItemStackHelper.fromData(PowerUtils.module_fe)});

        Recipes.recipe.addRecipe(ItemStackHelper.fromData(PowerUtils.itemPowerConverter, 1, 1), new Object[]{
                "ABA", "CDE", "ABA",

                Character.valueOf('A'), IUItem.advancedAlloy,

                Character.valueOf('B'), BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3),

                Character.valueOf('C'), ItemStackHelper.fromData(PowerUtils.module_ic),

                Character.valueOf('D'),
                IUItem.machine,

                Character.valueOf('E'), ItemStackHelper.fromData(PowerUtils.module_qe)});
        Recipes.recipe.addRecipe(ItemStackHelper.fromData(PowerUtils.module_qe), new Object[]{
                "ABA", "CDC", "ABA",

                Character.valueOf('A'), ItemStackHelper.fromData(IUItem.sunnarium, 1, 3),

                Character.valueOf('B'), ItemStackHelper.fromData(IUItem.quantumtool),

                Character.valueOf('C'), ItemStackHelper.fromData(IUItem.radiationresources, 1, 2),

                Character.valueOf('D'),
                ItemStackHelper.fromData(IUItem.core, 1, 3)});

    }

}
