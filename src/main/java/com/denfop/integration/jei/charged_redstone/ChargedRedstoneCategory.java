package com.denfop.integration.jei.charged_redstone;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.multimechanism.simple.BlockEntityCentrifuge;
import com.denfop.blocks.mechanism.BlockMoreMachine3Entity;
import com.denfop.containermenu.ContainerMenuMultiMachine;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class ChargedRedstoneCategory extends ScreenMain implements IRecipeCategory<ChargedRedstoneHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;

    public ChargedRedstoneCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(new ContainerMenuMultiMachine(Minecraft.getInstance().player,
                ((BlockEntityCentrifuge) BlockMoreMachine3Entity.centrifuge_iu.getDummyTe()), 1, true
        ));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                140
        );
    }


    @Nonnull
    @Override
    public String getTitles() {
        return ItemStackHelper.fromData(IUItem.charged_redstone).getDisplayName().getString();
    }


    @Override
    public RecipeType<ChargedRedstoneHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @SuppressWarnings("removal")
    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(ChargedRedstoneHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        drawSplitString(stack, Localization.translate(recipe.output), 5, 3,
                140 - 5, 4210752
        );
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChargedRedstoneHandler recipe, IFocusGroup focuses) {
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(recipe.input);
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
