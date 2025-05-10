package com.denfop.mixin.access;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {
    @Accessor
    Slot getHoveredSlot();

    @Accessor
    int getQuickCraftingType();

    @Accessor
    Slot getClickedSlot();

    @Accessor
    Slot getSnapbackEnd();

    @Accessor
    Slot getQuickdropSlot();
}
