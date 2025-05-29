package com.denfop.mixin.access;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(DeferredRegister.class)
public interface DeferredRegisterAccessor<T> {

    @Accessor
    Map<RegistryObject<T>, Supplier<? extends T>> getEntries();
}
