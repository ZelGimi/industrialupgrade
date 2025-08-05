package com.denfop.mixin.access;


import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(DeferredRegister.class)
public interface DeferredRegisterAccessor<T> {

    @Accessor
    Map<DeferredHolder<T, ? extends T>, Supplier<? extends T>> getEntries();
}
