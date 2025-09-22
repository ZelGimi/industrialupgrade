package com.denfop.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InputOreDict implements IInputItemStack {

    public final String input;
    public final Integer meta;
    public int amount;
    private List<ItemStack> ores;

    InputOreDict(String input) {
        this(input, 1);
    }

    public InputOreDict(String input, int amount) {
        this(input, amount, null);
    }

    InputOreDict(String input, int amount, Integer meta) {
        this.input = input;
        this.amount = amount;
        this.meta = meta;
    }

    public boolean matches(ItemStack subject) {
        List<ItemStack> inputs = this.getOres();
        boolean useOreStackMeta = this.meta == null;
        Item subjectItem = subject.getItem();
        int subjectMeta = subject.getItemDamage();

        return inputs.stream()
                .anyMatch(oreStack -> {
                    Item oreItem = oreStack.getItem();
                    int metaRequired = useOreStackMeta ? oreStack.getItemDamage() : this.meta;
                    return subjectItem == oreItem && (subjectMeta == metaRequired || metaRequired == 32767);
                });
    }

    public int getAmount() {
        return this.amount;
    }

    @Override
    public void growAmount(final int col) {
        amount += col;
        for (ItemStack stack : getOres()) {
            stack.setCount(this.getAmount());
        }
    }

    public List<ItemStack> getInputs() {
        return this.getOres();


    }


    public boolean equals(Object obj) {
        InputOreDict other;
        if (obj != null && this.getClass() == obj.getClass() && this.input.equals((other = (InputOreDict) obj).input) && other.amount == this.amount) {
            return Objects.equals(this.meta, other.meta);
        } else {
            return false;
        }
    }

    private List<ItemStack> getOres() {
        if (this.ores != null) {
            return this.ores;
        } else {
            ores = new ArrayList<>();
            List<ItemStack> ret = OreDictionary.getOres(this.input);
            if (ret != OreDictionary.EMPTY_LIST) {
                for (ItemStack stack : ret) {
                    stack = stack.copy();
                    stack.setCount(this.getAmount());
                    this.ores.add(stack);
                }

            }

            return ores;
        }
    }

}
