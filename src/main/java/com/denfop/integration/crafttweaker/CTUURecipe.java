package com.denfop.integration.crafttweaker;

import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import ic2.core.uu.UuIndex;
import net.minecraft.item.ItemStack;
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


            UuIndex.instance.add(getItemStack(this.output), this.number);


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
