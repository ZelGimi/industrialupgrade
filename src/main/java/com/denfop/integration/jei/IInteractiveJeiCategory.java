package com.denfop.integration.jei;

import com.mojang.blaze3d.platform.InputConstants;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;

public interface IInteractiveJeiCategory<T> {

    default List<Component> getTooltipStrings(
            final T recipe,
            final IRecipeSlotsView recipeSlotsView,
            final double mouseX,
            final double mouseY
    ) {
        return Collections.emptyList();
    }

    default boolean handleInput(
            final T recipe,
            final double mouseX,
            final double mouseY,
            final InputConstants.Key input
    ) {
        return false;
    }
}