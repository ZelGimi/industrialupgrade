package com.denfop.mixin.access;

import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Bee.class)
public interface BeeAccessor {

    @Invoker("setFlag")
    void invokeSetFlag(int flagId, boolean value);
}
