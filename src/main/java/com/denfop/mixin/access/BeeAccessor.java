package com.denfop.mixin.access;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.world.entity.animal.Bee;

@Mixin(Bee.class)
public interface BeeAccessor {

    @Invoker("setFlag")
    void invokeSetFlag(int flagId, boolean value);
}
