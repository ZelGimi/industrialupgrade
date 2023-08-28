package com.denfop.recipe;

import com.denfop.utils.ModUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InputOreDict implements IInputItemStack {

    public final String input;
    public final int amount;
    public final Integer meta;
    private List<ItemStack> ores;

    InputOreDict(String input) {
        this(input, 1);
    }

    InputOreDict(String input, int amount) {
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

    public List<ItemStack> getInputs() {
        List<ItemStack> ores = this.getOres();
        boolean allSuitableEntries = ores.stream().allMatch(stack -> ModUtils.getSize(stack) == this.getAmount());

        if (allSuitableEntries) {
            return ores;
        } else {
            List<ItemStack> ret = ores.stream()
                    .filter(stack -> stack.getItem() != ItemStack.EMPTY.getItem())
                    .map(stack -> ModUtils.getSize(stack) == this.getAmount() ? stack : ModUtils.setSize(
                            stack,
                            this.getAmount()
                    )).collect(Collectors.toList());

            return Collections.unmodifiableList(ret);
        }


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
            List<ItemStack> ret = OreDictionary.getOres(this.input);
            if (ret != OreDictionary.EMPTY_LIST) {
                this.ores = ret;
            }

            return ret;
        }
    }

}
