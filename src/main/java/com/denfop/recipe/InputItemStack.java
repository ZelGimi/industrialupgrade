package com.denfop.recipe;

import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class InputItemStack implements IInputItemStack {

    public final ItemStack input;
    public final int amount;

    InputItemStack(ItemStack input) {
        this(input, ModUtils.getSize(input));
    }

    InputItemStack(ItemStack input, int amount) {
        if (ModUtils.isEmpty(input)) {
            throw new IllegalArgumentException("invalid input stack");
        } else {
            this.input = input.copy();
            this.amount = amount;
        }
    }

    public boolean matches(ItemStack subject) {
        return subject.getItem() == this.input.getItem() && (subject.getMetadata() == this.input.getMetadata() || this.input.getMetadata() == 32767) && (this.input.getMetadata() == 32767 || ModUtils.matchesNBT(
                subject.getTagCompound(),
                this.input.getTagCompound()
        ));
    }

    public int getAmount() {
        return this.amount;
    }

    public List<ItemStack> getInputs() {
        return Collections.singletonList(ModUtils.setSize(this.input, this.getAmount()));
    }

    public String toString() {
        return "RInputItemStack<" + ModUtils.setSize(this.input, this.amount) + ">";
    }

    public boolean equals(Object obj) {
        InputItemStack other;
        return obj != null && this.getClass() == obj.getClass() && ModUtils.checkItemEqualityStrict(
                (other = (InputItemStack) obj).input,
                this.input
        ) && other.amount == this.amount;
    }

}
