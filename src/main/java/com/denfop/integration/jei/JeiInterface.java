package com.denfop.integration.jei;

import com.denfop.screen.ScreenMain;
import com.mojang.blaze3d.platform.InputConstants;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class JeiInterface<C extends ScreenMain & com.denfop.integration.jei.IRecipeCategory> implements IRecipeCategory {

    private final C instance;

    public JeiInterface(final C instance) {
        this.instance = instance;
    }

    @Override
    public RecipeType getRecipeType() {
        return this.instance.getRecipeType();
    }

    @Override
    public Component getTitle() {
        return Component.literal(this.instance.getTitles());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.instance.getIcon();
    }

    @SuppressWarnings("removal")
    @Override
    public @Nullable IDrawable getBackground() {
        return this.instance.getBackground();
    }

    @Override
    public void setRecipe(
            final IRecipeLayoutBuilder builder,
            final Object recipe,
            final IFocusGroup focuses
    ) {
        this.instance.setRecipe(builder, recipe, focuses);
    }

    @Override
    public void draw(
            final Object recipe,
            final IRecipeSlotsView recipeSlotsView,
            final GuiGraphics guiGraphics,
            final double mouseX,
            final double mouseY
    ) {
        this.instance.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public List<Component> getTooltipStrings(
            final Object recipe,
            final IRecipeSlotsView recipeSlotsView,
            final double mouseX,
            final double mouseY
    ) {
        if (this.instance instanceof IInteractiveJeiCategory<?> interactiveCategory) {
            @SuppressWarnings("unchecked") final IInteractiveJeiCategory<Object> typedCategory =
                    (IInteractiveJeiCategory<Object>) interactiveCategory;

            return typedCategory.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
        }

        return Collections.emptyList();
    }

    @Override
    public boolean handleInput(
            final Object recipe,
            final double mouseX,
            final double mouseY,
            final InputConstants.Key input
    ) {
        if (this.instance instanceof IInteractiveJeiCategory<?> interactiveCategory) {
            @SuppressWarnings("unchecked") final IInteractiveJeiCategory<Object> typedCategory =
                    (IInteractiveJeiCategory<Object>) interactiveCategory;

            return typedCategory.handleInput(recipe, mouseX, mouseY, input);
        }

        return false;
    }
}