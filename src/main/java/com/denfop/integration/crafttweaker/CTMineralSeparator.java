package com.denfop.integration.crafttweaker;

import com.blamejared.mtlib.utils.BaseAction;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.items.resource.ItemIngots;
import com.denfop.utils.ModUtils;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Objects;

@ZenClass("mods.industrialupgrade.MineralSeparator")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTMineralSeparator {

    @ZenMethod
    public static void addRecipe(IItemStack[] output, IIngredient input, short temperature, int... col) {
        CraftTweakerAPI.apply(new AddMineralSeparatorIngredientAction(output, input, temperature, col));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new RemoveMineralSeparatorIngredientAction(output));
    }

    private static class AddMineralSeparatorIngredientAction extends BaseAction {


        private final IItemStack[] output;
        private final IIngredient input;
        private final short temperature;
        private final int[] col;

        public AddMineralSeparatorIngredientAction(IItemStack[] output, IIngredient input, short temperature, int[] col) {
            super("handlerho");
            this.output = output;
            this.input = input;
            this.temperature = temperature;
            this.col = col;
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

        public static ItemStack[] getItemStack(IItemStack[] item) {
            if (item == null) {
                return null;
            } else {
                Object internal = item[0].getInternal();
                if (!(internal instanceof ItemStack)) {
                    CraftTweakerAPI.logError("Not a valid item stack: " + item[0]);
                }
                ItemStack[] itemStacks = new ItemStack[item.length];
                assert internal instanceof ItemStack;
                for (int i = 0; i < itemStacks.length; i++) {
                    itemStacks[i] = new ItemStack(
                            ((ItemStack) item[i].getInternal()).getItem(),
                            item[i].getAmount(),
                            item[i].getDamage()
                    );
                }
                return itemStacks;
            }
        }

        public void apply() {
            NBTTagCompound nbt = ModUtils.nbt();
            nbt.setShort("temperature", temperature);
            for (int i = 0; i < col.length; i++) {
                nbt.setInteger("input" + i, col[i]);
            }
            final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;

            ItemStack stack = new IC2RecipeInput(this.input).getInputs().get(0);
            IRecipeInput second1;
            if (OreDictionary.getOreIDs(stack).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(stack)[0])
                    .isEmpty() && stack.getItem() instanceof ItemIngots) {
                second1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]));
            } else {
                second1 = input.forStack(stack);
            }
            Recipes.recipes.addRecipe(
                    "handlerho",
                    new BaseMachineRecipe(
                            new Input(
                                    second1
                            ),
                            new RecipeOutput(nbt, getItemStack(output))
                    )
            );
        }

        public String describe() {
            return "";
        }

        public Object getOverrideKey() {
            return null;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }
            AddMineralSeparatorIngredientAction that = (AddMineralSeparatorIngredientAction) o;
            return temperature == that.temperature && Arrays.equals(output, that.output) && Objects.equals(
                    input,
                    that.input
            ) && Arrays.equals(col, that.col);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(super.hashCode(), input, temperature);
            result = 31 * result + Arrays.hashCode(output);
            result = 31 * result + Arrays.hashCode(col);
            return result;
        }

    }


    //

    private static class RemoveMineralSeparatorIngredientAction extends BaseAction {


        private final IItemStack input;

        public RemoveMineralSeparatorIngredientAction(
                IItemStack input
        ) {
            super("handlerho");
            this.input = input;
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
            Recipes.recipes.removeRecipe("handlerho", Recipes.recipes.getRecipeOutput("handlerho", false,
                    getItemStack(this.input)
            ).getOutput());
        }

        public String describe() {
            return "removing alloy smelter recipe " + this.input;
        }

        public Object getOverrideKey() {
            return null;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }
            RemoveMineralSeparatorIngredientAction that = (RemoveMineralSeparatorIngredientAction) o;
            return Objects.equals(input, that.input);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), input);
        }

    }


}
