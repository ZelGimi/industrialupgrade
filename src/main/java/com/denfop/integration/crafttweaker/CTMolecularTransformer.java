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
import ic2.api.recipe.IRecipeInputFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Objects;

@ZenClass("mods.industrialupgrade.MolecularTransformer")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTMolecularTransformer {

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new Remove(output));
    }

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient ingredient, double energy) {
        if (ingredient.getAmount() < 0) {
            CraftTweakerAPI.logWarning("invalid ingredient: " + ingredient + " - stack size not known");
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setDouble("energy", energy);
            CraftTweakerAPI.apply(new AddMolecularAction(ingredient,

                    new ItemStack[]{getItemStack(output)}, tag, false
            ));
        }
    }

    @ZenMethod
    public static void addOreRecipe(IItemStack output, IIngredient ingredient, double energy) {
        if (ingredient.getAmount() < 0) {
            CraftTweakerAPI.logWarning("invalid ingredient: " + ingredient + " - stack size not known");
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setDouble("energy", energy);
            CraftTweakerAPI.apply(new AddMolecularAction(ingredient,

                    new ItemStack[]{getItemStack(output)}, tag, true
            ));
        }
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

    private static class Remove extends BaseAction {


        private final IItemStack output;

        public Remove(
                IItemStack output
        ) {
            super("molecular");
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
            Recipes.recipes.removeRecipe("molecular", new RecipeOutput(null, getItemStack(this.output)));
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

    private static class AddMolecularAction extends BaseAction {

        private final IIngredient ingredient;
        private final NBTTagCompound tag;
        private final ItemStack[] output;
        private final boolean oreDictionary;

        public AddMolecularAction(IIngredient ingredient, ItemStack[] output, NBTTagCompound tag, boolean oreDictionary) {
            super("MolecularTransformer");
            this.ingredient = ingredient;
            this.tag = tag;
            this.output = output;
            this.oreDictionary = oreDictionary;
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
            final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
            if (oreDictionary) {
                ItemStack stack = new IC2RecipeInput(this.ingredient).getInputs().get(0);
                String ore = OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]);
                Recipes.recipes.addRecipe("molecular", new BaseMachineRecipe(
                        new Input(
                                OreDictionary.getOres(ore).isEmpty()
                                        ? new IC2RecipeInput(this.ingredient)
                                        : input.forOreDict(ore)),
                        new RecipeOutput(tag, output)
                ));


            } else {
                Recipes.recipes.addRecipe("molecular", new BaseMachineRecipe(
                        new Input(
                                new IC2RecipeInput(this.ingredient)),
                        new RecipeOutput(tag, output)
                ));

            }

        }

        public String describe() {
            return "Adding molecular recipe " + this.ingredient + " + " + this.tag + " => " + Arrays.toString(this.output);
        }

        public Object getOverrideKey() {
            return null;
        }

        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + ((this.ingredient != null) ? this.ingredient.hashCode() : 0);
            hash = 67 * hash + ((this.tag != null) ? this.tag.hashCode() : 0);
            hash = 67 * hash + ((this.output != null) ? Arrays.hashCode(this.output) : 0);
            return hash;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            CTMolecularTransformer.AddMolecularAction other = (CTMolecularTransformer.AddMolecularAction) obj;
            if (!Objects.equals(this.ingredient, other.ingredient)) {
                return false;
            }
            if (!Objects.equals(this.tag, other.tag)) {
                return false;
            }

            return Arrays.equals(this.output, other.output);
        }

    }


}
