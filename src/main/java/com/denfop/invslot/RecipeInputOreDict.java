//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.invslot;

import ic2.api.recipe.IRecipeInput;
import ic2.core.recipe.RecipeInputBase;
import ic2.core.util.StackUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RecipeInputOreDict extends RecipeInputBase implements IRecipeInput {
    public final String input;
    public final int amount;
    public final Integer meta;
    private List<ItemStack> ores;

    RecipeInputOreDict(String input) {
        this(input, 1);
    }

  public   RecipeInputOreDict(String input, int amount) {
        this(input, amount, (Integer)null);
    }

    public  RecipeInputOreDict(String input, int amount, Integer meta) {
        this.input = input;
        this.amount = amount;
        this.meta = meta;
    }

    public boolean matches(ItemStack subject) {
        List<ItemStack> inputs = this.getOres();
        boolean useOreStackMeta = this.meta == null;
        Item subjectItem = subject.getItem();
        int subjectMeta = subject.getItemDamage();
        Iterator var6 = inputs.iterator();

        Item oreItem;
        int metaRequired;
        do {
            do {
                ItemStack oreStack;
                do {
                    if (!var6.hasNext()) {
                        return false;
                    }

                    oreStack = (ItemStack)var6.next();
                    oreItem = oreStack.getItem();
                } while(oreItem == null);

                metaRequired = useOreStackMeta ? oreStack.getItemDamage() : this.meta;
            } while(subjectItem != oreItem);
        } while(subjectMeta != metaRequired && metaRequired != 32767);

        return true;
    }

    public int getAmount() {
        return this.amount;
    }

    public List<ItemStack> getInputs() {
        List<ItemStack> ores = this.getOres();
        boolean hasUnsuitableEntries = false;
        Iterator var3 = ores.iterator();

        while(var3.hasNext()) {
            ItemStack stack = (ItemStack)var3.next();
            if (StackUtil.getSize(stack) != this.getAmount()) {
                hasUnsuitableEntries = true;
                break;
            }
        }

        if (!hasUnsuitableEntries) {
            return ores;
        } else {
            List<ItemStack> ret = new ArrayList(ores.size());
            Iterator var7 = ores.iterator();

            while(var7.hasNext()) {
                ItemStack stack = (ItemStack)var7.next();
                if (stack.getItem() != null) {
                    if (StackUtil.getSize(stack) != this.getAmount()) {
                        stack = StackUtil.copyWithSize(stack, this.getAmount());
                    }

                    ret.add(stack);
                }
            }

            return Collections.unmodifiableList(ret);
        }
    }

    public String toString() {
        return this.meta == null ? "RInputOreDict<" + this.amount + "x" + this.input + ">" : "RInputOreDict<" + this.amount + "x" + this.input + "@" + this.meta + ">";
    }

    public boolean equals(Object obj) {
        RecipeInputOreDict other;
        if (obj != null && this.getClass() == obj.getClass() && this.input.equals((other = (RecipeInputOreDict)obj).input) && other.amount == this.amount) {
            return this.meta == null ? other.meta == null : this.meta == other.meta;
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
