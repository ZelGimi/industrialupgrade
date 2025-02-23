package com.denfop.utils;

import com.denfop.api.item.IEnergyItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Objects;

public class MatterRecipe {

    private final ItemStack stack;
    int COMPARE_DAMAGE = 1;
    int COMPARE_NBT = 2;
    int COMPARE_QUANTITY = 4;
    private boolean can;
    private double aer;
    private double aqua;
    private double earth;
    private double night;
    private double sun;
    private double end;
    private double nether;
    private double matter;

    public MatterRecipe(ItemStack stack) {
        this.stack = stack.copy();
        this.stack.setCount(1);
        this.aer = 0;
        this.aqua = 0;
        this.earth = 0;
        this.night = 0;
        this.sun = 0;
        this.end = 0;
        this.nether = 0;
        this.matter = 0;
        this.can = false;
    }

    public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB) {
        if (stackA.isEmpty() && stackB.isEmpty()) {
            return true;
        } else if (!stackA.isEmpty() && !stackB.isEmpty()) {
            if (stackA.getTagCompound() == null && stackB.getTagCompound() != null) {
                return false;
            } else {
                return (stackA.getTagCompound() == null || stackA.getTagCompound().equals(stackB.getTagCompound()));
            }
        } else {
            return false;
        }
    }

    public boolean isEqualNbt(@Nullable ItemStack left, @Nullable ItemStack right) {


        if (!areItemStackTagsEqual(left, right)) {
            if (left.hasTagCompound() && !right.hasTagCompound() && left.getTagCompound().hasNoTags()) {
                return true;
            } else if (!left.hasTagCompound() && right.hasTagCompound() && right.getTagCompound().hasNoTags()) {
                return true;
            } else {
                return !left.hasTagCompound() && !right.hasTagCompound();
            }
        }

        return true;
    }

    public boolean isEqual(@Nullable ItemStack left, @Nullable ItemStack right, int flags) {


        if (left.getItem() != right.getItem()) {
            return false;
        }
        int damage = left.getItemDamage();
        int damage1 = right.getItemDamage();
        if (left.getItem() instanceof IEnergyItem && right.getItem() instanceof IEnergyItem) {
            return true;
        }
        if ((flags & COMPARE_DAMAGE) == COMPARE_DAMAGE && damage != OreDictionary.WILDCARD_VALUE && damage1 != OreDictionary.WILDCARD_VALUE) {
            if (damage != damage1) {
                return false;
            }
        }

        if ((flags & COMPARE_NBT) == COMPARE_NBT) {
            if (!isEqualNbt(left, right)) {
                return false;
            }
        }

        if ((flags & COMPARE_QUANTITY) == COMPARE_QUANTITY) {
            return left.getCount() == right.getCount();
        }

        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MatterRecipe that = (MatterRecipe) o;
        return isEqual(that.stack, this.stack, 1 | 2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack, aer, aqua, earth, night, sun, end, nether, matter);
    }

    public void setCan(final boolean can) {
        this.can = can;
    }

    public double getAer() {
        return aer;
    }

    public double getAqua() {
        return aqua;
    }

    public double getEarth() {
        return earth;
    }

    public double getEnd() {
        return end;
    }

    public double getMatter() {
        return matter;
    }

    public double getNether() {
        return nether;
    }

    public double getNight() {
        return night;
    }

    public double getSun() {
        return sun;
    }

    public boolean can() {
        return this.can && (this.aer != 0 || this.aqua != 0 || this.earth != 0 || this.nether != 0 || this.sun != 0 || this.night != 0 || this.matter != 0 || this.end != 0);
    }

    public ItemStack getStack() {
        return stack;
    }

    public void addAer(double aer) {
        this.aer += aer;
    }

    public void addNether(double nether) {
        this.nether += nether;
    }

    public void addAqua(double aqua) {
        this.aqua += aqua;
    }

    public void addSun(double sun) {
        this.sun += sun;
    }

    public void addMatter(double matter) {
        this.matter += matter;
    }

    public void addEnd(double end) {
        this.end += end;
    }

    public void addEarth(double earth) {
        this.earth += earth;
    }

    public void addNight(double night) {
        this.night += night;
    }

}
