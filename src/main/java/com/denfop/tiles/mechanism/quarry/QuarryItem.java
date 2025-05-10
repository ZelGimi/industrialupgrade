package com.denfop.tiles.mechanism.quarry;


import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuarryItem {

    private final ItemStack stack;
    private TagKey<Item> oreDict;

    public QuarryItem(ItemStack stack) {
        this.stack = stack;
        List<TagKey<Item>> list = stack.getTags().toList();
        try {
            oreDict = list.get(0);
        }catch (Exception e){
            oreDict = new TagKey<>(Registry.ITEM_REGISTRY,new ResourceLocation("","unknown"));
        }
    }

    public QuarryItem(ItemStack stack, TagKey<Item> oreDict) {
        this.stack = stack;
        this.oreDict = oreDict;
    }

    public QuarryItem(String oreDict) {
        this.oreDict = new TagKey<>(Registry.ITEM_REGISTRY, new ResourceLocation(oreDict));
        this.stack = new Ingredient.TagValue(this.oreDict).getItems().stream().toList().get(0);

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
        return this.stack.is(that.getStack().getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack, oreDict);
    }

    public ItemStack getStack() {
        return stack;
    }

    public TagKey<Item> getOreDict() {
        return oreDict;
    }

    public boolean isGem() {
        return this.getOreDict().location().getPath().startsWith("gem");
    }

    public boolean isShard() {
        return this.getOreDict().location().getPath().startsWith("shard");
    }

}
