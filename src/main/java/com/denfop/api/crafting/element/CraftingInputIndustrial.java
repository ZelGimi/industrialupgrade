package com.denfop.api.crafting.element;

import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CraftingInputIndustrial implements RecipeInput {
    public static final CraftingInputIndustrial EMPTY = new CraftingInputIndustrial(0, 0, List.of());
    private final int width;
    private final int height;
    private final List<ItemStack> items;
    private final StackedContents stackedContents = new StackedContents();
    private final int ingredientCount;

    public CraftingInputIndustrial(int p_346099_, int p_344783_, List<ItemStack> p_345241_) {
        this.width = p_346099_;
        this.height = p_344783_;
        this.items = p_345241_;
        int i = 0;
        Iterator var5 = p_345241_.iterator();

        while (var5.hasNext()) {
            ItemStack itemstack = (ItemStack) var5.next();
            if (!itemstack.isEmpty()) {
                ++i;
                this.stackedContents.accountStack(itemstack, 1);
            }
        }

        this.ingredientCount = i;
    }

    public static CraftingInputIndustrial of(int p_346122_, int p_344877_, List<ItemStack> p_345183_) {
        return ofPositioned(p_346122_, p_344877_, p_345183_).input();
    }

    public static Positioned ofPositioned(int p_347479_, int p_347466_, List<ItemStack> p_347585_) {
        if (p_347479_ != 0 && p_347466_ != 0) {
            int i = p_347479_ - 1;
            int j = 0;
            int k = p_347466_ - 1;
            int l = 0;

            int i2;
            for (i2 = 0; i2 < p_347466_; ++i2) {
                boolean flag = true;

                for (int j1 = 0; j1 < p_347479_; ++j1) {
                    ItemStack itemstack = (ItemStack) p_347585_.get(j1 + i2 * p_347479_);
                    if (!itemstack.isEmpty()) {
                        i = Math.min(i, j1);
                        j = Math.max(j, j1);
                        flag = false;
                    }
                }

                if (!flag) {
                    k = Math.min(k, i2);
                    l = Math.max(l, i2);
                }
            }

            i2 = j - i + 1;
            int j2 = l - k + 1;
            if (i2 > 0 && j2 > 0) {
                if (i2 == p_347479_ && j2 == p_347466_) {
                    return new Positioned(new CraftingInputIndustrial(p_347479_, p_347466_, p_347585_), i, k);
                } else {
                    List<ItemStack> list = new ArrayList(i2 * j2);

                    for (int k2 = 0; k2 < j2; ++k2) {
                        for (int k1 = 0; k1 < i2; ++k1) {
                            int l1 = k1 + i + (k2 + k) * p_347479_;
                            list.add((ItemStack) p_347585_.get(l1));
                        }
                    }

                    return new Positioned(new CraftingInputIndustrial(i2, j2, list), i, k);
                }
            } else {
                return CraftingInputIndustrial.Positioned.EMPTY;
            }
        } else {
            return CraftingInputIndustrial.Positioned.EMPTY;
        }
    }

    public ItemStack getItem(int p_345667_) {
        return (ItemStack) this.items.get(p_345667_);
    }

    public ItemStack getItem(int p_346237_, int p_345556_) {
        return (ItemStack) this.items.get(p_346237_ + p_345556_ * this.width);
    }

    public int size() {
        return this.items.size();
    }

    public boolean isEmpty() {
        return this.ingredientCount == 0;
    }

    public StackedContents stackedContents() {
        return this.stackedContents;
    }

    public List<ItemStack> items() {
        return this.items;
    }

    public int ingredientCount() {
        return this.ingredientCount;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public boolean equals(Object p_345299_) {
        if (p_345299_ == this) {
            return true;
        } else {
            boolean var10000;
            if (p_345299_ instanceof CraftingInputIndustrial) {
                CraftingInputIndustrial craftinginput = (CraftingInputIndustrial) p_345299_;
                var10000 = this.width == craftinginput.width && this.height == craftinginput.height && this.ingredientCount == craftinginput.ingredientCount && ItemStack.listMatches(this.items, craftinginput.items);
            } else {
                var10000 = false;
            }

            return var10000;
        }
    }

    public int hashCode() {
        int i = ItemStack.hashStackList(this.items);
        i = 31 * i + this.width;
        return 31 * i + this.height;
    }

    public static record Positioned(CraftingInputIndustrial input, int left, int top) {
        public static final Positioned EMPTY;

        static {
            EMPTY = new Positioned(CraftingInputIndustrial.EMPTY, 0, 0);
        }

        public Positioned(CraftingInputIndustrial input, int left, int top) {
            this.input = input;
            this.left = left;
            this.top = top;
        }

        public CraftingInputIndustrial input() {
            return this.input;
        }

        public int left() {
            return this.left;
        }

        public int top() {
            return this.top;
        }
    }
}
