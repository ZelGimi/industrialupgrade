package com.denfop.integration.crafttweaker;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.recipe.InputOreDict;
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

@ZenClass("mods.industrialupgrade.AlloySmelter")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTAlloySmelter {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container, IIngredient fill, Short temperature) {
        CraftTweakerAPI.apply(new AddAlloSmelterIngredientAction(container, fill, output, temperature));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new RemoveAlloSmelterIngredientAction(output));
    }

    private static class AddAlloSmelterIngredientAction extends BaseAction {

        private final IIngredient container;

        private final IIngredient fill;

        private final IItemStack output;
        private final short temperature;

        public AddAlloSmelterIngredientAction(
                IIngredient container,
                IIngredient fill,
                IItemStack output,
                final Short temperature
        ) {
            super("alloysmelter");
            this.container = container;
            this.fill = fill;
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
            final NBTTagCompound nbt = ModUtils.nbt();
            nbt.setShort("temperature", this.temperature);
            ItemStack stack = new IC2InputItemStack(this.container).getInputs().get(0);
            int amount = new IC2InputItemStack(this.container).getAmount();
            if (OreDictionary.getOreIDs(stack).length > 0) {
                ore = OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]);
            }
            stack = new IC2InputItemStack(this.fill).getInputs().get(0);
            int amount1 = new IC2InputItemStack(this.fill).getAmount();
            if (OreDictionary.getOreIDs(stack).length > 0) {
                ore1 = OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]);
            }
            Recipes.recipes.addAdderRecipe("alloysmelter", new BaseMachineRecipe(
                    new Input(
                            OreDictionary.getOres(ore).isEmpty()
                                    ? new IC2InputItemStack(this.container)
                                    : new InputOreDict(ore, amount),
                            OreDictionary.getOres(ore1).isEmpty()
                                    ? new IC2InputItemStack(this.fill)
                                    : new InputOreDict(ore1, amount1)
                    ),
                    new RecipeOutput(nbt, getItemStack(this.output))
            ));
        }

        public String describe() {
            return "Adding alloy smelter recipe " + this.container + " + " + this.fill + " => " + this.output;
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
            AddAlloSmelterIngredientAction other = (AddAlloSmelterIngredientAction) obj;
            if (!Objects.equals(this.container, other.container)) {
                return false;
            }
            if (!Objects.equals(this.fill, other.fill)) {
                return false;
            }
            return Objects.equals(this.output, other.output);
        }

    }


    //

    private static class RemoveAlloSmelterIngredientAction extends BaseAction {


        private final IItemStack output;

        public RemoveAlloSmelterIngredientAction(
                IItemStack output
        ) {
            super("alloysmelter");
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
            Recipes.recipes.addRemoveRecipe("alloysmelter", getItemStack(this.output));
        }

        public String describe() {
            return "removing alloy smelter recipe " + this.output;
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
