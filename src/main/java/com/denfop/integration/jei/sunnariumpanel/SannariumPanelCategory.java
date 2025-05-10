package com.denfop.integration.jei.sunnariumpanel;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockSunnariumPanelMaker;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TileEntityStampMechanism;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class SannariumPanelCategory extends GuiIU implements IRecipeCategory<SannariumPanelHandler> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;
    JeiInform jeiInform;
    public SannariumPanelCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityStampMechanism) BlockBaseMachine3.stamp_mechanism.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guisunnariumpanelmaker" +
                        ".png"), 3, 3, 140,
                75
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }

    @Override
    public RecipeType<SannariumPanelHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.sunnariummaker, 1).getDescriptionId());
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(SannariumPanelHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;
        energy++;
        int energylevel = (int) Math.min(14.0F * energy / 100, 14);
        int xScale = 14 * progress / 100;

        if (xScale > 14) {
            progress = 0;
        }

        bindTexture(getTexture());


        drawTexturedModalRect(stack,+22, 54 + 14 - energylevel, 176, 14 - energylevel,
                14, energylevel
        );


        drawTexturedModalRect(stack,+74, +33, 177, 15, xScale + 1, 15);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SannariumPanelHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,26,33).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT,48,33).addItemStack(recipe.getInput1());
        builder.addSlot(RecipeIngredientRole.OUTPUT,103,33).addItemStack(recipe.getOutput());
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(recipe.getContainer().input.getAllStackInputs());

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisunnariumpanelmaker.png");
    }


}
