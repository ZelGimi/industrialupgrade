package com.denfop.recipe;


import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InputOreDict implements IInputItemStack {

    public int amount;
    public final Integer meta;
    private final TagKey<Item> tag;
    private final List<ItemStack> ores;

    public InputOreDict(String input) {
        this(input.toLowerCase(), 1);
    }

    public InputOreDict(String input, int amount) {
        this(input.toLowerCase(), amount, 0);
    }

    public InputOreDict(String input, int amount, Integer meta) {
        ResourceLocation input1 = new ResourceLocation(input.toLowerCase());
        this.amount = amount;
        this.meta = meta;
        this.tag = ItemTags.create(input1);
        ores = new ArrayList<>();
        Iterable<Holder<Item>> holder = Registry.ITEM.getTagOrEmpty(this.tag);
        holder.forEach(itemHolder -> ores.add(new ItemStack(itemHolder)));
        for (ItemStack stack : ores) {
            stack.setCount(this.getAmount());
        }
    }

    public InputOreDict(TagKey<Item> tag, int amount) {
        this.amount = amount;
        this.meta = 0;
        this.tag = tag;
        ores = new ArrayList<>();
        Registry.ITEM.getTagOrEmpty(this.tag).forEach(itemHolder -> ores.add(new ItemStack(itemHolder)));
        for (ItemStack stack : ores) {
            stack.setCount(this.getAmount());
        }
    }

    @Override
    public void growAmount(final int col) {
        amount += col;
        for (ItemStack stack : getOres()) {
            stack.setCount(this.getAmount());
        }
    }

    public InputOreDict(int amount, TagKey<Item> tag) {
        this.amount = amount;
        this.meta = 0;
        this.tag = tag;
        ores = new ArrayList<>();
        Registry.ITEM.getTagOrEmpty(this.tag).forEach(itemHolder -> ores.add(new ItemStack(itemHolder)));
        for (ItemStack stack : ores) {
            stack.setCount(this.getAmount());
        }
    }

    public InputOreDict(FriendlyByteBuf buffer) {
        this(buffer.readInt(), new TagKey<>(Registry.ITEM_REGISTRY, buffer.readResourceLocation()));

    }

    public static ItemStack setSize(ItemStack stack, int col) {
        stack = stack.copy();
        stack.setCount(col);
        return stack;
    }

    public boolean matches(ItemStack subject) {
        List<ItemStack> inputs = this.getOres();
        boolean useOreStackMeta = this.meta == null;
        Item subjectItem = subject.getItem();
        int subjectMeta = 0;

        return inputs.stream()
                .anyMatch(oreStack -> {
                    Item oreItem = oreStack.getItem();
                    int metaRequired = useOreStackMeta ? 0 : this.meta;
                    return subjectItem == oreItem && (subjectMeta == metaRequired || metaRequired == 32767);
                });
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(1);
        buffer.writeInt(amount);
        buffer.writeResourceLocation(this.tag.location());
    }

    public int getAmount() {
        return this.amount;
    }

    public List<ItemStack> getInputs() {
        List<ItemStack> ores = this.getOres();
        boolean allSuitableEntries = ores.stream().allMatch(stack -> stack.getCount() == this.getAmount());

        if (allSuitableEntries) {
            return ores;
        } else {

            return ores.stream()
                    .filter(stack -> stack.getItem() != ItemStack.EMPTY.getItem())
                    .map(stack -> stack.getCount() == this.getAmount() ? stack : setSize(
                            stack,
                            this.getAmount()
                    )).toList();
        }


    }

    @Override
    public boolean hasTag() {
        return true;
    }

    @Override
    public TagKey<Item> getTag() {
        return this.tag;
    }


    public boolean equals(Object obj) {
        InputOreDict other;
        if (obj != null && this.getClass() == obj.getClass() && this.tag.equals((other = (InputOreDict) obj).tag) && other.amount == this.amount) {
            return Objects.equals(this.meta, other.meta);
        } else {
            return false;
        }
    }

    private List<ItemStack> getOres() {
        return this.ores;
    }

}
