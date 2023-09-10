package com.denfop.integration.crafttweaker;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.recipe.InputOreDict;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;

@ZenClass("mods.industrialupgrade.DoubleMolecularTransformer")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTDoubleMolecularTransformer {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container, IIngredient fill, double energy) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("energy", energy);
        CraftTweakerAPI.apply(new AddMolecularIngredientAction(container, fill, output, tag));
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
            super("doublemolecular");
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
            Recipes.recipes.addRemoveRecipe("doublemolecular", CraftTweakerMC.getItemStack(output));
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

    private static class AddMolecularIngredientAction extends BaseAction {

        private final IIngredient container;

        private final IIngredient fill;

        private final IItemStack output;
        private final NBTTagCompound nbt;

        public AddMolecularIngredientAction(IIngredient container, IIngredient fill, IItemStack output, NBTTagCompound nbt) {
            super("doublemolecular");
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
            String ore = "";
            String ore1 = "";
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
            Recipes.recipes.addAdderRecipe("doublemolecular", new BaseMachineRecipe(
                    new Input(
                            OreDictionary.getOres(ore).isEmpty()
                                    ? new IC2InputItemStack(this.container)
                                    : new InputOreDict(ore, amount),
                            OreDictionary.getOres(ore1).isEmpty()
                                    ? new IC2InputItemStack(this.fill)
                                    : new InputOreDict(ore1, amount1)
                    ),
                    new RecipeOutput(this.nbt, getItemStack(this.output))
            ));


        }

        public String describe() {
            return "Adding double molecular recipe " + this.container + " + " + this.fill + " => " + this.output;
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
            AddMolecularIngredientAction other = (AddMolecularIngredientAction) obj;
            if (!Objects.equals(this.container, other.container)) {
                return false;
            }
            if (!Objects.equals(this.fill, other.fill)) {
                return false;
            }
            if (!Objects.equals(this.nbt, other.nbt)) {
                return false;
            }

            return Objects.equals(this.output, other.output);
        }

    }


}
