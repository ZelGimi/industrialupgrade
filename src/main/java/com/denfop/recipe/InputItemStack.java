package com.denfop.recipe;

import com.denfop.api.item.IEnergyItem;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class InputItemStack implements IInputItemStack {

    public final ItemStack input;
    public int amount;

    public InputItemStack(ItemStack input) {
        this(input, ModUtils.getSize(input));
    }

    InputItemStack(ItemStack input, int amount) {
        this.input = input.copy();
        this.amount = amount;
    }

    public boolean matches(ItemStack subject) {
        boolean energy = (this.input.getItem() instanceof IEnergyItem && subject.getItem() instanceof IEnergyItem);
        return subject.getItem() == this.input.getItem() && (subject.getMetadata() == this.input.getMetadata() || this.input.getMetadata() == 32767 || energy) && (this.input.getMetadata() == 32767 || (energy || ModUtils.matchesNBT(
                subject.getTagCompound(),
                this.input.getTagCompound()
        )));
    }

    public int getAmount() {
        return this.amount;
    }

    @Override
    public void growAmount(final int col) {
        this.amount++;
        this.input.setCount(amount);
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
