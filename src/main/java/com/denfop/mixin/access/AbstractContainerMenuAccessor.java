package com.denfop.mixin.access;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(AbstractContainerMenu.class)
public interface AbstractContainerMenuAccessor {


    @Invoker("synchronizeCarriedToRemote")
    void invokeSynchronizeCarriedToRemote();

    @Invoker("triggerSlotListeners")
    void invokeTriggerSlotListeners(int index, ItemStack stack, Supplier<ItemStack> supplier);

    @Invoker("synchronizeSlotToRemote")
    void invokeSynchronizeSlotToRemote(int index, ItemStack stack, Supplier<ItemStack> supplier);

}
