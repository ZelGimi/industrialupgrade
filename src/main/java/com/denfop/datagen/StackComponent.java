package com.denfop.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.Collections;
import java.util.stream.Stream;

public record StackComponent(ItemStack stack) implements ICustomIngredient {
    public static final Codec<StackComponent> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    ItemStack.CODEC.fieldOf("stack").forGetter(StackComponent::stack)
            ).apply(inst, StackComponent::new)
    );


    public StackComponent(ItemStack stack) {
        this.stack = stack;
    }

    public boolean equals(Object p_301162_) {
        boolean var10000;
        if (p_301162_ instanceof StackComponent ingredient$tagvalue) {
            var10000 = ItemStack.isSameItem(ingredient$tagvalue.stack, this.stack);
        } else {
            var10000 = false;
        }

        return var10000;
    }

    @Override
    public boolean test(ItemStack itemStack) {
        return ItemStack.isSameItem(itemStack, this.stack);
    }

    @Override
    public Stream<ItemStack> getItems() {
        return Collections.singleton(stack).stream();
    }


    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return null;
    }


}
