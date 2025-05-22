package com.denfop.recipe;

import com.denfop.utils.ModUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InputItemStacks implements IInputItemStack {

    public final List<ItemStack> input;
    public int amount;

    public InputItemStacks(ItemStack[] input) {
        this(input, ModUtils.getSize(input[0]));
    }

    InputItemStacks(ItemStack[] input, int amount) {
        this.input = new ArrayList<>();
        for (ItemStack stack : input) {
            this.input.add(stack.copy());
        }
        this.amount = amount;
    }

    public boolean matches(ItemStack subject) {
        List<ItemStack> inputs = input;
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
        return this.amount;
    }

    @Override
    public void growAmount(final int col) {
        amount += col;
        for (ItemStack stack : input) {
            stack.setCount(this.getAmount());
        }
    }

    public List<ItemStack> getInputs() {
        return input;
    }


    public boolean equals(Object obj) {
        InputItemStacks other;
        return obj != null && this.getClass() == obj.getClass() && this.input.equals((other = (InputItemStacks) obj).input) && other.amount == this.amount;
    }

}
