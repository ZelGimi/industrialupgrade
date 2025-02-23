package com.denfop.integration.crafttweaker;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.utils.ModUtils;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;

@ZenClass("mods.industrialupgrade.UURecipe")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTUURecipe {

    @ZenMethod
    public static void addRecipe(IItemStack output, double col) {
        CraftTweakerAPI.apply(new AddMolecularAction(output, col));
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


    private static class AddMolecularAction extends BaseAction {

        private final IItemStack output;
        private final double number;


        public AddMolecularAction(IItemStack output, final double col) {
            super("uu");
            this.output = output;
            this.number = col;
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
            nbt.setDouble("matter", number);

            Recipes.recipes.addAdderRecipe(
                    "replicator",
                    new BaseMachineRecipe(
                            new Input(
                                    new InputItemStack(output)
                            ),
                            new RecipeOutput(nbt, CraftTweakerMC.getItemStacks(output))
                    )
            );
        }

        public String describe() {
            return "";
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
            AddMolecularAction other = (AddMolecularAction) obj;

            return Objects.equals(this.output, other.output);
        }

    }


}
