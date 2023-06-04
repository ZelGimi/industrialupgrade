package com.denfop.integration.crafttweaker;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;

@ZenClass("mods.industrialupgrade.Synthesis")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTSynthesis {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container, IIngredient fill, int percent) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("percent", percent);
        CraftTweakerAPI.apply(new AddSynthesisIngredientAction(container, fill, output, tag));
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
            super("synthesis");
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
            Recipes.recipes.addRemoveRecipe("synthesis", CraftTweakerMC.getItemStack(output));

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

    private static class AddSynthesisIngredientAction extends BaseAction {

        private final IIngredient container;

        private final IIngredient fill;

        private final IItemStack output;
        private final NBTTagCompound nbt;

        public AddSynthesisIngredientAction(IIngredient container, IIngredient fill, IItemStack output, NBTTagCompound nbt) {
            super("synthesis");
            this.container = container;
            this.fill = fill;
            this.output = output;
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
                assert internal instanceof ItemStack;
                return new ItemStack(((ItemStack) internal).getItem(), item.getAmount(), item.getDamage());
            }
        }

        public void apply() {
            Recipes.recipes.addAdderRecipe("synthesis", new BaseMachineRecipe(
                    new Input(
                            new IC2RecipeInput(this.container),
                            new IC2RecipeInput(this.fill)
                    ),
                    new RecipeOutput(nbt, getItemStack(this.output))
            ));


        }

        public String describe() {
            return "Adding synthesis recipe " + this.container + " + " + this.fill + " => " + this.output;
        }

        public Object getOverrideKey() {
            return null;
        }

        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + ((this.container != null) ? this.container.hashCode() : 0);
            hash = 67 * hash + ((this.fill != null) ? this.fill.hashCode() : 0);
            hash = 67 * hash + ((this.output != null) ? this.output.hashCode() : 0);
            hash = 67 * hash + ((this.nbt != null) ? this.nbt.hashCode() : 0);
            return hash;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            AddSynthesisIngredientAction other = (AddSynthesisIngredientAction) obj;
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
