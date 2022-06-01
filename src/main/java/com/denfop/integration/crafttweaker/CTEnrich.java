package com.denfop.integration.crafttweaker;

import com.blamejared.mtlib.utils.BaseAction;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;

@ZenClass("mods.industrialupgrade.Enrich")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTEnrich {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container, IIngredient fill) {
        CraftTweakerAPI.apply(new AddEnrichIngredientAction(container, fill, output));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new Remove(output));
    }

    private static class Remove extends BaseAction {


        private final IItemStack output;

        public Remove(
                IItemStack output
        ) {
            super("enrichment");
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
                assert internal instanceof ItemStack;
                return new ItemStack(((ItemStack) internal).getItem(), item.getAmount(), item.getDamage());
            }
        }

        public void apply() {
            Recipes.recipes.removeRecipe("enrichment", new RecipeOutput(null, getItemStack(this.output)));
        }

        public String describe() {
            return "removing recipe " + this.output;
        }

        public Object getOverrideKey() {
            return null;
        }

        public int hashCode() {
            int hash = 7;
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
            Remove other = (Remove) obj;

            return Objects.equals(this.output, other.output);
        }

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
                assert internal instanceof ItemStack;
                return new ItemStack(((ItemStack) internal).getItem(), item.getAmount(), item.getDamage());
            }
        }

        public void apply() {
            Recipes.recipes.addRecipe(
                    "enrichment",
                    new BaseMachineRecipe(
                            new Input(
                                    new IC2RecipeInput(this.container),
                                    new IC2RecipeInput(this.fill)
                            ),
                            new RecipeOutput(null, getItemStack(this.output))
                    )
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


}
