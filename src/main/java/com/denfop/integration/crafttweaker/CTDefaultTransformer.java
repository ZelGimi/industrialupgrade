package com.denfop.integration.crafttweaker;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.utils.ModUtils;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;

@ZenClass("mods.industrialupgrade.DefaultAssembler")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTDefaultTransformer {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient fill, Double matter) {
        CraftTweakerAPI.apply(new AddAlloSmelterIngredientAction(fill, output, matter));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new RemoveAlloSmelterIngredientAction(output));
    }

    private static class AddAlloSmelterIngredientAction extends BaseAction {

        private final IIngredient container;


        private final IItemStack output;
        private final double matter;

        public AddAlloSmelterIngredientAction(
                IIngredient container,
                IItemStack output,
                final Double matter
        ) {
            super("defaultassembler");
            this.container = container;
            this.output = output;
            this.matter = matter;
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

            final NBTTagCompound nbt = ModUtils.nbt();
            nbt.setDouble("need", this.matter);
            final IC2RecipeInput stack = new IC2RecipeInput(this.container);

            Recipes.recipes.addRecipe("defaultcollector", new BaseMachineRecipe(
                    new Input(
                            stack
                    ),
                    new RecipeOutput(nbt, getItemStack(this.output))
            ));
        }


        public Object getOverrideKey() {
            return null;
        }

        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + ((this.container != null) ? this.container.hashCode() : 0);
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
            AddAlloSmelterIngredientAction other = (AddAlloSmelterIngredientAction) obj;
            if (!Objects.equals(this.container, other.container)) {
                return false;
            }

            return Objects.equals(this.output, other.output);
        }

    }


    private static class RemoveAlloSmelterIngredientAction extends BaseAction {


        private final IItemStack output;

        public RemoveAlloSmelterIngredientAction(
                IItemStack output
        ) {
            super("defaultassembler");
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
            Recipes.recipes.removeRecipe("defaultcollector", new RecipeOutput(null, getItemStack(this.output)));
        }

        public String describe() {
            return "removing crystallizer recipe " + this.output;
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
            RemoveAlloSmelterIngredientAction other = (RemoveAlloSmelterIngredientAction) obj;

            return Objects.equals(this.output, other.output);
        }

    }


}
