package com.denfop.mixin.invoker;


import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemStack.class)
public interface ItemStackInvoker {

    @Invoker("onItemUse")
    InteractionResult getOnItemUse(UseOnContext p_41662_, java.util.function.Function<UseOnContext, InteractionResult> callback);
}
