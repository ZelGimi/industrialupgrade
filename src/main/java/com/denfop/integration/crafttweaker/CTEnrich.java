package com.denfop.integration.crafttweaker;

import com.blamejared.mtlib.utils.BaseAction;
import com.denfop.api.IDoubleMachineRecipeManager;
import com.denfop.api.Recipes;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Map;
import java.util.Objects;

@ZenClass("mods.industrialupgrade.Enrich")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTEnrich {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container, IIngredient fill) {
        CraftTweakerAPI.apply(new AddEnrichIngredientAction(container, fill, output));
    }


    private static class AddEnrichIngredientAction extends BaseAction {

        private final IIngredient container;

        private final IIngredient fill;

        private final IItemStack output;

        public AddEnrichIngredientAction(IIngredient container, IIngredient fill, IItemStack output) {
            super("enrichment");
            this.container = container;
            this.fill = fill;
            this.output = output;
        }

        public static ItemStack getItemStack(IItemStack item) {
            if (item == null) {
                return null;
            } else {
                Object internal = item.getInternal();
                if (!(internal instanceof ItemStack)) {
                    CraftTweakerAPI.logError("Not a valid item stack: " + item);
                }

                return new ItemStack(((ItemStack) internal).getItem(), item.getAmount(), item.getDamage());
            }
        }

        public void apply() {
            Recipes.enrichment.addRecipe(
                    new IC2RecipeInput(this.container),
                    new IC2RecipeInput(this.fill), null,

                    getItemStack(this.output)
            );

        }

        public String describe() {
            return "Adding enrich bottle recipe " + this.container + " + " + this.fill + " => " + this.output;
        }

        public Object getOverrideKey() {
            return null;
        }

        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + ((this.container != null) ? this.container.hashCode() : 0);
            hash = 67 * hash + ((this.fill != null) ? this.fill.hashCode() : 0);
            hash = 67 * hash + ((this.output != null) ? this.output.hashCode() : 0);
            return hash;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            AddEnrichIngredientAction other = (AddEnrichIngredientAction) obj;
            if (!Objects.equals(this.container, other.container)) {
                return false;
            }
            if (!Objects.equals(this.fill, other.fill)) {
                return false;
            }
            return Objects.equals(this.output, other.output);
        }

    }

    private static class Remove extends BaseAction {

        private final Map<IDoubleMachineRecipeManager.Input, RecipeOutput> recipes;

        protected Remove(Map<IDoubleMachineRecipeManager.Input, RecipeOutput> recipes) {
            super("enrichment");
            this.recipes = recipes;
        }

        protected String getRecipeInfo(Map.Entry<IDoubleMachineRecipeManager.Input, RecipeOutput> recipe) {
            return recipe.toString();
        }

        @Override
        public void apply() {

            for (Map.Entry<IDoubleMachineRecipeManager.Input, RecipeOutput> iRecipeInputRecipeOutputEntry : recipes.entrySet()) {
                Recipes.enrichment.getRecipes().remove(
                        iRecipeInputRecipeOutputEntry.getKey(),
                        iRecipeInputRecipeOutputEntry.getValue()
                );
            }

        }

    }

}
