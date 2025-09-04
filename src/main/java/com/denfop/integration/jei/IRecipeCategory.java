package com.denfop.integration.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public interface IRecipeCategory<T> extends mezz.jei.api.recipe.category.IRecipeCategory<T> {
    @Override
    default @NotNull Component getTitle() {
        return Component.literal(getTitles());
    }

    ;

    String getTitles();

    @Override
    default IDrawable getIcon() {
        return null;
    }
}
