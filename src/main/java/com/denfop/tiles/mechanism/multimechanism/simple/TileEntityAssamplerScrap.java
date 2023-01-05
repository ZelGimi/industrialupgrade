package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.init.Localization;
import ic2.core.item.type.CraftingItemType;
import ic2.core.ref.ItemName;
import net.minecraft.item.ItemStack;

public class TileEntityAssamplerScrap extends TileEntityMultiMachine {

    public TileEntityAssamplerScrap() {
        super(
                EnumMultiMachine.AssamplerScrap.usagePerTick,
                EnumMultiMachine.AssamplerScrap.lenghtOperation,
                3
        );
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addrecipe(ItemStack input, ItemStack output) {
        final IRecipeInputFactory input1 = ic2.api.recipe.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "scrap",
                new BaseMachineRecipe(
                        new Input(
                                input1.forStack(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public void init() {
        addrecipe(
                new ItemStack(ItemName.crafting.getItemStack(CraftingItemType.rubber).getItem(), 9, 23),
                new ItemStack(ItemName.crafting.getItemStack(CraftingItemType.rubber).getItem(), 1, 24)
        );
        addrecipe(
                new ItemStack(ItemName.crafting.getItemStack(CraftingItemType.rubber).getItem(), 9, 24),
                new ItemStack(IUItem.doublescrapBox, 1)
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.AssamplerScrap;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockAssamplerScrap.name");
    }

    public String getStartSoundFile() {
        return "Machines/AssamplerScrap.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
