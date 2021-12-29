package com.denfop.integration.crafttweaker;

import com.blamejared.mtlib.utils.BaseAction;
import com.denfop.api.IMicrochipFarbricatorRecipeManager;
import com.denfop.api.Recipes;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Map;
import java.util.Objects;

@ZenClass("mods.industrialupgrade.GenMicrochip")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTGenMicrochip {

    @ZenMethod
    public static void addRecipe(
            IItemStack output,
            IIngredient container,
            IIngredient fill,
            IIngredient fill1,
            IIngredient fill2,
            IIngredient fill3,
            int temperature
    ) {
        CraftTweakerAPI.apply(new AddGenMicrochipIngredientAction(
                container,
                fill,
                fill1,
                fill2,
                fill3,
                output,
                (short) temperature
        ));
    }


    private static class AddGenMicrochipIngredientAction extends BaseAction {

        private final IIngredient container;

        private final IIngredient fill;

        private final IItemStack output;
        private final IIngredient fill1;
        private final IIngredient fill2;
        private final IIngredient fill3;
        private final NBTTagCompound nbt;

        public AddGenMicrochipIngredientAction(
                IIngredient container,
                IIngredient fill,
                IIngredient fill1,
                IIngredient fill2,
                IIngredient fill3,
                IItemStack output,
                short temperature
        ) {
            super("genmicrochip");
            this.container = container;
            this.fill = fill;
            this.fill1 = fill1;
            this.fill2 = fill2;
            this.fill3 = fill3;
            this.output = output;
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setShort("temperature", temperature);
            this.nbt = nbt;
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
            Recipes.GenerationMicrochip.addRecipe(

                    new IC2RecipeInput(this.container),
                    new IC2RecipeInput(this.fill),
                    new IC2RecipeInput(this.fill1),
                    new IC2RecipeInput(this.fill2),
                    new IC2RecipeInput(this.fill3),


                    getItemStack(this.output), this.nbt
            );

        }

        public String describe() {
            return "Adding generation microchip bottle recipe " + this.container + " + " + this.fill + " => " + this.output;
        }

        public Object getOverrideKey() {
            return null;
        }

        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + ((this.container != null) ? this.container.hashCode() : 0);
            hash = 67 * hash + ((this.fill != null) ? this.fill.hashCode() : 0);
            hash = 67 * hash + ((this.fill1 != null) ? this.fill1.hashCode() : 0);
            hash = 67 * hash + ((this.fill2 != null) ? this.fill2.hashCode() : 0);
            hash = 67 * hash + ((this.fill3 != null) ? this.fill3.hashCode() : 0);
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
            AddGenMicrochipIngredientAction other = (AddGenMicrochipIngredientAction) obj;
            if (!Objects.equals(this.container, other.container)) {
                return false;
            }
            if (!Objects.equals(this.fill, other.fill)) {
                return false;
            }
            if (!Objects.equals(this.fill1, other.fill1)) {
                return false;
            }
            if (!Objects.equals(this.fill2, other.fill2)) {
                return false;
            }
            if (!Objects.equals(this.fill3, other.fill3)) {
                return false;
            }
            return Objects.equals(this.output, other.output);
        }

    }

    private static class Remove extends BaseAction {

        private final Map<IMicrochipFarbricatorRecipeManager.Input, RecipeOutput> recipes;

        protected Remove(Map<IMicrochipFarbricatorRecipeManager.Input, RecipeOutput> recipes) {
            super("genmicrochip");
            this.recipes = recipes;
        }

        protected String getRecipeInfo() {
            return recipes.toString();
        }


        @Override
        public void apply() {
            for (Map.Entry<IMicrochipFarbricatorRecipeManager.Input, RecipeOutput> iRecipeInputRecipeOutputEntry : recipes.entrySet()) {
                Recipes.GenerationMicrochip.getRecipes().remove(
                        iRecipeInputRecipeOutputEntry.getKey(),
                        iRecipeInputRecipeOutputEntry.getValue()
                );
            }
        }


    }

}
