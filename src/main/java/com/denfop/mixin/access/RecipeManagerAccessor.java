package com.denfop.mixin.access;

import com.google.common.collect.Multimap;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RecipeManager.class)
public interface RecipeManagerAccessor {

    @Accessor
    Multimap<RecipeType<?>, RecipeHolder<?>> getByType();
}
