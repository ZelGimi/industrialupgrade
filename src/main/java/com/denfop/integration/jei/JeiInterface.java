package com.denfop.integration.jei;

import com.denfop.screen.ScreenMain;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class JeiInterface<C extends ScreenMain & com.denfop.integration.jei.IRecipeCategory> implements IRecipeCategory {

    private final C instance;

    public JeiInterface(C instance) {
        this.instance = instance;
    }

    @Override
    public RecipeType getRecipeType() {
        return instance.getRecipeType();
    }

    @Override
    public Component getTitle() {
        return Component.literal(instance.getTitles());
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return instance.getIcon();
    }

    @SuppressWarnings("removal")
    @Override
    public @Nullable IDrawable getBackground() {
        return instance.getBackground();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Object recipe, IFocusGroup focuses) {
        instance.setRecipe(builder, recipe, focuses);
    }

    @Override
    public void draw(Object recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        instance.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }
}
