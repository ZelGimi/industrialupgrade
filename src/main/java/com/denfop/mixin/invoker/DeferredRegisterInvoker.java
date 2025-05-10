package com.denfop.mixin.invoker;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DeferredRegister.class)
public interface DeferredRegisterInvoker<T> {

    @Invoker("addEntries")
    void invokeAddEntries(RegisterEvent event);
}
