package com.denfop.integration.crafttweaker;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.denfop.api.Recipes;
import com.denfop.utils.ModUtils;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Collections;
import java.util.Objects;

@ZenClass("mods.industrialupgrade.MatterRecipe")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTMatterRecipe {

    @ZenMethod
    public static void addRecipe(
            IItemStack output,
            double matter,
            double sunmatter,
            double aquamatter,
            double nethermatter,
            double nightmatter,
            double earthmatter,
            double endmatter,
            double aermatter
    ) {

        NBTTagCompound tag = new NBTTagCompound();
        double[] quantitysolid = {matter, sunmatter, aquamatter, nethermatter, nightmatter, earthmatter, endmatter, aermatter};
        for (int i = 0; i < quantitysolid.length; i++) {
            ModUtils.SetDoubleWithoutItem(tag, ("quantitysolid_" + i), quantitysolid[i]);
        }

        CraftTweakerAPI.apply(new AddMolecularAction(output, tag, false));

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

    @ZenMethod
    public static void remove(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new CTMatterRecipe.Remove(input));
    }


    private static class AddMolecularAction extends BaseAction {

        private final IItemStack output;
        private final NBTTagCompound nbt;
        private final boolean delete;

        public AddMolecularAction(IItemStack output, NBTTagCompound nbt, boolean delete) {
            super("matter");
            this.output = output;
            this.nbt = nbt;
            this.delete = delete;
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
            final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
            if (!this.delete) {
                Recipes.matterrecipe.addRecipe(
                        input.forStack((ItemStack) output.getInternal()),
                        this.nbt, false,
                        getItemStack(this.output)
                );
            }

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
            CTMatterRecipe.AddMolecularAction other = (CTMatterRecipe.AddMolecularAction) obj;

            return Objects.equals(this.output, other.output);
        }

    }

    private static class Remove extends BaseAction {

        private final IItemStack input;

        public Remove(IItemStack input) {
            super("matter");
            this.input = input;
        }

        public void apply() {
            RecipeOutput output = Recipes.matterrecipe.getOutputFor(CraftTweakerMC.getItemStack(input), false);
            if (output == null || output.items.isEmpty()) {
                return;
            }
            Recipes.matterrecipe.removeRecipe(CraftTweakerMC.getItemStack(input), Collections.singletonList(output.items.get(0)));

        }

        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(this.input);
        }

    }


}
