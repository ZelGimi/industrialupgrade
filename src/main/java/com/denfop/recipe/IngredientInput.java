package com.denfop.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class IngredientInput extends Ingredient {

    private final IInputItemStack input;
    private ItemStack[] items;
    private IntList list;

    public IngredientInput(final IInputItemStack input) {
        super(Stream.of());
        this.input = input;

    }

    public ItemStack[] getItem() {
        if (this.items == null) {
            this.items = this.input.getInputs().toArray(new ItemStack[0]);
        }
        return this.items;
    }

    public JsonElement toJson() {

        JsonArray jsonarray = new JsonArray();
        if (this.input.hasTag()) {
            jsonarray.add(new TagValue(this.input.getTag()).serialize());
        } else {
            jsonarray.add(new ItemValue(this.input.getInputs().get(0)).serialize());
        }
        return jsonarray;

    }

    public boolean test(@Nullable final ItemStack item) {
        return this.input.matches(item);
    }


    public IntList getStackingIds() {
        if (this.list == null) {
            final ItemStack[] items = this.getItem();
            this.list = new IntArrayList(items.length);
            for (final ItemStack itemstack : items) {
                this.list.add(StackedContents.getStackingIndex(itemstack));
            }
            this.list.sort(IntComparators.NATURAL_COMPARATOR);
        }
        return this.list;
    }
    @Override
    public boolean isSimple() {
        return false;
    }
}
