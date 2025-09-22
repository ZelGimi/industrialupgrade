package com.denfop.integration.crafttweaker;

import com.denfop.recipe.IInputItemStack;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InputItemStack implements IInputItemStack {

    List<ItemStack> stacks;

    public InputItemStack(IIngredient ingredient) {
        stacks = new ArrayList<>();
        ingredient.getItems().forEach(iItemStack -> stacks.add(CraftTweakerMC.getItemStack(iItemStack)));
    }

    public boolean matches(ItemStack subject) {
        List<ItemStack> inputs = stacks;
        Item subjectItem = subject.getItem();
        int subjectMeta = subject.getItemDamage();

        return inputs.stream()
                .anyMatch(oreStack -> {
                    Item oreItem = oreStack.getItem();
                    int metaRequired = oreStack.getItemDamage();
                    return subjectItem == oreItem && (subjectMeta == metaRequired || metaRequired == 32767);
                });
    }

    public int getAmount() {
        return stacks.get(0).getCount();
    }

    @Override
    public void growAmount(final int col) {
        stacks.forEach(stack -> stack.grow(col));
    }

    public List<ItemStack> getInputs() {

        return stacks;
    }

    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.stacks.hashCode();
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            InputItemStack other = (InputItemStack) obj;
            return Objects.equals(this.stacks, other.stacks);
        }
    }

}
