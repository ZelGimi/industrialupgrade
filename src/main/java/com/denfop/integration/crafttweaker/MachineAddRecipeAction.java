package com.denfop.integration.crafttweaker;

import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import ic2.api.recipe.IBasicMachineRecipeManager;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.Objects;

public class MachineAddRecipeAction extends BaseAction {

    private final String name;
    private final IMachineRecipeManager machine;
    private final ItemStack[] output;
    private final IRecipeInput input;
    private final NBTTagCompound tag;

    public MachineAddRecipeAction(
            String name,
            IMachineRecipeManager machine,
            ItemStack[] output,
            NBTTagCompound tag,
            IRecipeInput input
    ) {
        super(name);
        this.name = name;
        this.machine = machine;
        this.output = output;
        this.input = input;
        this.tag = tag;
    }

    public void apply() {
        try {
            if (this.machine instanceof IBasicMachineRecipeManager) {
                ((IBasicMachineRecipeManager) this.machine).addRecipe(this.input, this.tag, true, this.output);
            }
        } catch (RuntimeException var2) {
            CraftTweakerAPI.logError(var2.getMessage());
        }

    }

    public String describe() {
        if (this.output.length == 1) {
            return "Adding " + this.name + " recipe for " + this.output[0].getDisplayName();
        } else {
            StringBuilder result = new StringBuilder();
            result.append("Adding ").append(this.name).append(" recipe for ");
            result.append("[");

            for (int i = 0; i < this.output.length; ++i) {
                if (i == 0) {
                    result.append(", ");
                } else {
                    result.append(this.output[i].getDisplayName());
                }
            }

            result.append("]");
            return result.toString();
        }
    }

    public Object getOverrideKey() {
        return null;
    }

    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 47 * hash + (this.machine != null ? this.machine.hashCode() : 0);
        hash = 47 * hash + Arrays.deepHashCode(this.output);
        hash = 47 * hash + (this.input != null ? this.input.hashCode() : 0);
        hash = 47 * hash + (this.tag != null ? this.tag.hashCode() : 0);
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            MachineAddRecipeAction other;
            label60:
            {
                other = (MachineAddRecipeAction) obj;
                if (this.name == null) {
                    if (other.name == null) {
                        break label60;
                    }
                } else if (this.name.equals(other.name)) {
                    break label60;
                }

                return false;
            }

            if (Objects.equals(this.machine, other.machine)) {
                if (!Arrays.deepEquals(this.output, other.output)) {
                    return false;
                } else if (Objects.equals(this.input, other.input)) {
                    return Objects.equals(this.tag, other.tag);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

}
