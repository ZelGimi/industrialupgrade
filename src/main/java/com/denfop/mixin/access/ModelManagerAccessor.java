package com.denfop.mixin.access;

import net.minecraft.client.resources.model.ModelManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ModelManager.class)
public interface ModelManagerAccessor {
    @Accessor
    int getMaxMipmapLevels();
}
