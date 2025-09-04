package com.denfop.mixin.access;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {
    @Accessor
    int getSnapbackStartX();

    @Accessor
    int getSnapbackStartY();

    @Accessor
    int getQuickCraftingRemainder();

    @Accessor("quickCraftingRemainder")
    void setQuickCraftingRemainder(int value);

    @Accessor
    Slot getHoveredSlot();

    @Accessor
    boolean getIsSplittingStack();

    @Accessor
    ItemStack getSnapbackItem();

    @Accessor("snapbackItem")
    void setSnapbackItem(ItemStack stack);

    @Accessor
    long getSnapbackTime();

    @Accessor
    ItemStack getDraggingItem();

    @Accessor
    int getQuickCraftingType();

    @Accessor
    Slot getClickedSlot();

    @Accessor
    Slot getSnapbackEnd();

    @Accessor
    Slot getQuickdropSlot();
}
