package com.denfop.integration.crafttweaker;

import com.blamejared.mtlib.helpers.LogHelper;
import com.denfop.IUCore;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;

@ZenClass("mods.industrialupgrade.QuarryFurnaceRecipe")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTFQuarry {

    @ZenMethod
    public static void addRecipe(IItemStack output) {


        CraftTweakerAPI.apply(new AddMolecularAction(output));

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

    @ZenMethod
    public static void remove(IItemStack input) {
        CraftTweakerAPI.apply(new CTFQuarry.Remove(input));
    }


    private static class AddMolecularAction extends BaseAction {

        private final IItemStack output;


        public AddMolecularAction(IItemStack output) {
            super("fquarry");
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


            if (OreDictionary.getOreIDs((ItemStack) output.getInternal()).length == 0) {
                if (!IUCore.list_furnace_adding.contains((ItemStack) output.getInternal())) {
                    IUCore.list_furnace_adding.add((ItemStack) output.getInternal());
                } else {
                    int i = OreDictionary.getOreIDs((ItemStack) output.getInternal())[0];
                    if (!IUCore.list_furnace_adding.contains(OreDictionary.getOres(OreDictionary.getOreName(i)).get(0))) {
                        IUCore.list_furnace_adding.add(OreDictionary.getOres(OreDictionary.getOreName(i)).get(0));
                    }
                }
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

    private static class Remove extends BaseAction {

        private final IItemStack input;

        public Remove(IItemStack input) {
            super("fquarry");
            this.input = input;
        }

        public void apply() {
            if (OreDictionary.getOreIDs((ItemStack) input.getInternal()).length == 0) {
                IUCore.list_furnace_removing.add((ItemStack) input.getInternal());
            } else {
                int i = OreDictionary.getOreIDs((ItemStack) input.getInternal())[0];
                IUCore.list_furnace_removing.add(OreDictionary.getOres(OreDictionary.getOreName(i)).get(0));
            }
        }

        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(this.input);
        }

    }


}
