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
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;

@ZenClass("mods.industrialupgrade.GenRods")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTGenRods {

    @ZenMethod
    public static void addRecipe(
            IItemStack output,
            IIngredient container,
            IIngredient fill,
            IIngredient fill1,
            IIngredient fill2,
            IIngredient fill3,
            IIngredient fill4
    ) {
        CraftTweakerAPI.apply(new AddGenRodsIngredientAction(
                container,
                fill,
                fill1,
                fill2,
                fill3,
                fill4,
                output
        ));
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
            super("rod_assembler");
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
            Recipes.recipes.addRemoveRecipe("rod_assembler", CraftTweakerMC.getItemStack(output));

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

    private static class AddGenRodsIngredientAction extends BaseAction {

        private final IIngredient container;

        private final IIngredient fill;

        private final IItemStack output;
        private final IIngredient fill1;
        private final IIngredient fill2;
        private final IIngredient fill3;
        private final IIngredient fill4;

        public AddGenRodsIngredientAction(
                IIngredient container,
                IIngredient fill,
                IIngredient fill1,
                IIngredient fill2,
                IIngredient fill3,
                IIngredient fill4,
                IItemStack output
        ) {
            super("rod_assembler");
            this.container = container;
            this.fill = fill;
            this.fill1 = fill1;
            this.fill2 = fill2;
            this.fill3 = fill3;
            this.fill4 = fill4;
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
            Recipes.recipes.addAdderRecipe("rod_assembler", new BaseMachineRecipe(
                    new Input(
                            new InputItemStack(this.container),
                            new InputItemStack(this.fill),
                            new InputItemStack(this.fill1),
                            new InputItemStack(this.fill2),
                            new InputItemStack(this.fill3),
                            new InputItemStack(this.fill4)
                    ),
                    new RecipeOutput(null, getItemStack(this.output))
            ));
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
            hash = 67 * hash + ((this.fill4 != null) ? this.fill4.hashCode() : 0);
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
            AddGenRodsIngredientAction other = (AddGenRodsIngredientAction) obj;
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


}
