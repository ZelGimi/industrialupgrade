package com.denfop.recipe;


import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.denfop.datagen.itemtag.ItemTagProvider.mapItems;

public class InputOreDict implements IInputItemStack {

    public final Integer meta;
    private final TagKey<Item> tag;
    private final List<ItemStack> ores;
    public int amount;

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
        Iterable<Holder<Item>> holder = BuiltInRegistries.ITEM.getTagOrEmpty(this.tag);
        holder.forEach(itemHolder -> ores.add(new ItemStack(itemHolder)));
        if (ores.isEmpty()) {
            if (mapItems.containsKey(tag.location())) {
                mapItems.get(tag.location()).forEach(items -> ores.add(items.copy()));
            }
        }
        for (ItemStack stack : ores) {
            stack.setCount(this.getAmount());
        }

    }

    public InputOreDict(CompoundTag tagCompound) {
        this.amount = tagCompound.getInt("Amount");

        this.meta = tagCompound.contains("Meta") ? tagCompound.getInt("Meta") : 0;


        this.tag = TagKey.create(Registries.ITEM, new ResourceLocation(tagCompound.getString("ItemTag")));


        this.ores = new ArrayList<>();
        ListTag list = tagCompound.getList("Ores", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            ores.add(ItemStack.of(list.getCompound(i)));
        }
        if (ores.isEmpty()) {
            BuiltInRegistries.ITEM.getTagOrEmpty(this.tag).forEach(itemHolder -> ores.add(new ItemStack(itemHolder)));
            if (ores.isEmpty()) {
                if (mapItems.containsKey(tag.location())) {
                    mapItems.get(tag.location()).forEach(items -> ores.add(items.copy()));
                }
            }
            for (ItemStack stack : ores) {
                stack.setCount(this.getAmount());
            }
        }
    }

    public InputOreDict(TagKey<Item> tag, int amount) {
        this.amount = amount;
        this.meta = 0;
        this.tag = tag;
        ores = new ArrayList<>();
        BuiltInRegistries.ITEM.getTagOrEmpty(this.tag).forEach(itemHolder -> ores.add(new ItemStack(itemHolder)));
        if (ores.isEmpty()) {
            if (mapItems.containsKey(tag.location())) {
                mapItems.get(tag.location()).forEach(items -> ores.add(items.copy()));
            }
        }
        for (ItemStack stack : ores) {
            stack.setCount(this.getAmount());
        }
    }

    public InputOreDict(int amount, TagKey<Item> tag) {
        this.amount = amount;
        this.meta = 0;
        this.tag = tag;
        ores = new ArrayList<>();
        BuiltInRegistries.ITEM.getTagOrEmpty(this.tag).forEach(itemHolder -> ores.add(new ItemStack(itemHolder)));
        if (ores.isEmpty()) {
            if (mapItems.containsKey(tag.location())) {
                mapItems.get(tag.location()).forEach(items -> ores.add(items.copy()));
            }
        }
        for (ItemStack stack : ores) {
            stack.setCount(this.getAmount());
        }

    }

    public InputOreDict(FriendlyByteBuf buffer) {
        this(buffer.readInt(), new TagKey<>(Registries.ITEM, buffer.readResourceLocation()));

    }

    public static ItemStack setSize(ItemStack stack, int col) {
        stack = stack.copy();
        stack.setCount(col);
        return stack;
    }

    @Override
    public void growAmount(final int col) {
        amount += col;
        for (ItemStack stack : getOres()) {
            stack.setCount(this.getAmount());
        }
    }

    public boolean matches(ItemStack subject) {
        List<ItemStack> inputs = this.getOres();
        boolean useOreStackMeta = this.meta == null;
        Item subjectItem = subject.getItem();
        int subjectMeta = 0;

        return subject.is(tag);
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

    @Override
    public CompoundTag writeNBT() {
        CompoundTag tagCompound = new CompoundTag();
        tagCompound.putByte("id", (byte) 1);
        tagCompound.putInt("Amount", amount);

        if (meta != null) {
            tagCompound.putInt("Meta", meta);
        }

        if (tag != null) {
            tagCompound.putString("ItemTag", tag.location().toString());
        }

        ListTag list = new ListTag();
        for (ItemStack stack : ores) {
            list.add(stack.save(new CompoundTag()));
        }
        tagCompound.put("Ores", list);

        return tagCompound;
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
