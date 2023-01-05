package com.denfop.tiles.mechanism.quarry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Objects;

public class QuarryItem {

    private final ItemStack stack;
    private final String oreDict;

    public QuarryItem(ItemStack stack) {
        this.stack = stack;
        if (OreDictionary.getOreIDs(stack).length > 0) {
            oreDict = OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]);
        } else {
            oreDict = "";
        }
    }

    public QuarryItem(ItemStack stack, String oreDict) {
        this.stack = stack;
        this.oreDict = oreDict;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QuarryItem that = (QuarryItem) o;
        return this.stack.isItemEqual(that.getStack());
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack, oreDict);
    }

    public ItemStack getStack() {
        return stack;
    }

    public String getOreDict() {
        return oreDict;
    }

    public boolean isGem() {
        return this.getOreDict().startsWith("gem");
    }

    public boolean isShard() {
        return this.getOreDict().startsWith("shard");
    }

}
