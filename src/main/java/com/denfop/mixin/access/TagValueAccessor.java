package com.denfop.mixin.access;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.item.crafting.Ingredient$TagValue")
public interface TagValueAccessor {
    @Accessor("tag")
    TagKey<Item> getTag();
}
