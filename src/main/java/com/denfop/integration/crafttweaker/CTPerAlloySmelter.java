package com.denfop.integration.crafttweaker;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;

@ZenClass("mods.industrialupgrade.peralloysmelter")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTPerAlloySmelter {

    @ZenMethod
    public static void addRecipe(
            IItemStack output, IIngredient container, IIngredient fill, IIngredient fill1, IIngredient fill2, IIngredient fill3,
            Short temperature
    ) {
        CraftTweakerAPI.apply(new AddAlloSmelterIngredientAction(container, fill, fill1, fill2, fill3, output, temperature));
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
            super("peralloysmelter");
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
            Recipes.recipes.addRemoveRecipe("peralloysmelter", getItemStack(this.output));
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

    private static class AddAlloSmelterIngredientAction extends BaseAction {

        private final IIngredient container;

        private final IIngredient fill;
        private final IIngredient fill1;
        private final IIngredient fill2;
        private final IIngredient fill3;
        private final IItemStack output;
        private final short temperature;

        public AddAlloSmelterIngredientAction(
                IIngredient container,
                IIngredient fill,
                IIngredient fill1,
                IIngredient fill2,
                IIngredient fill3,
                IItemStack output,
                final Short temperature
        ) {
            super("impalloysmelter");
            this.container = container;
            this.fill = fill;
            this.fill1 = fill1;
            this.fill2 = fill2;
            this.fill3 = fill3;
            this.output = output;
            this.temperature = temperature;
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
            String ore = "";
            String ore1 = "";
            String ore2 = "";
            String ore3 = "";
            String ore4 = "";
            final NBTTagCompound nbt = ModUtils.nbt();
            nbt.setShort("temperature", this.temperature);
            ItemStack stack = new InputItemStack(this.container).getInputs().get(0);
            int amount = new InputItemStack(this.container).getAmount();
            if (OreDictionary.getOreIDs(stack).length > 0) {
                ore = OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]);
            }
            stack = new InputItemStack(this.fill).getInputs().get(0);
            int amount1 = new InputItemStack(this.fill).getAmount();
            if (OreDictionary.getOreIDs(stack).length > 0) {
                ore1 = OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]);
            }
            stack = new InputItemStack(this.fill1).getInputs().get(0);
            int amount2 = new InputItemStack(this.fill1).getAmount();
            if (OreDictionary.getOreIDs(stack).length > 0) {
                ore2 = OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]);
            }
            stack = new InputItemStack(this.fill2).getInputs().get(0);
            int amount3 = new InputItemStack(this.fill2).getAmount();
            if (OreDictionary.getOreIDs(stack).length > 0) {
                ore3 = OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]);
            }
            stack = new InputItemStack(this.fill3).getInputs().get(0);
            int amount4 = new InputItemStack(this.fill3).getAmount();
            if (OreDictionary.getOreIDs(stack).length > 0) {
                ore4 = OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]);
            }
            final IInputHandler input = com.denfop.api.Recipes.inputFactory;
            Recipes.recipes.addAdderRecipe("peralloysmelter", new BaseMachineRecipe(
                    new Input(
                            OreDictionary.getOres(ore).isEmpty()
                                    ? new InputItemStack(this.container)
                                    : input.getInput(ore, amount),
                            OreDictionary.getOres(ore1).isEmpty()
                                    ? new InputItemStack(this.fill)
                                    : input.getInput(ore1, amount1),
                            OreDictionary.getOres(ore2).isEmpty()
                                    ? new InputItemStack(this.fill1)
                                    : input.getInput(ore2, amount2),
                            OreDictionary.getOres(ore3).isEmpty()
                                    ? new InputItemStack(this.fill2)
                                    : input.getInput(ore3, amount3),
                            OreDictionary.getOres(ore4).isEmpty()
                                    ? new InputItemStack(this.fill3)
                                    : input.getInput(ore4, amount4)
                    ),
                    new RecipeOutput(nbt, getItemStack(this.output))
            ));
        }

        public String describe() {
            return "Adding advanced alloy smelter recipe " + this.container + " + " + this.fill + " + " + this.fill1 + " => " + this.output;
        }

        public Object getOverrideKey() {
            return null;
        }

        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + ((this.container != null) ? this.container.hashCode() : 0);
            hash = 67 * hash + ((this.fill != null) ? this.fill.hashCode() : 0);
            hash = 67 * hash + ((this.fill1 != null) ? this.fill1.hashCode() : 0);
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
            if (!Objects.equals(this.fill, other.fill)) {
                return false;
            }
            if (!Objects.equals(this.fill1, other.fill1)) {
                return false;
            }
            return Objects.equals(this.output, other.output);
        }

    }


}
