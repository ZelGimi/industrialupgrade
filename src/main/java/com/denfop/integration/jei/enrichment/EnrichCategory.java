package com.denfop.integration.jei.enrichment;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TileEntityEnchanterBooks;
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
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class EnrichCategory extends GuiIU implements IRecipeCategory<EnrichHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public EnrichCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityEnchanterBooks) BlockBaseMachine3.enchanter_books.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform=jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guenrichment" +
                        ".png"), 3, 3, 140,
                75
        );
    }

    @Override
    public RecipeType<EnrichHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine, 1, 10).getDescriptionId());
    }

    public String getModName() {
        return Constants.MOD_NAME;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(EnrichHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;
        energy++;
        double energylevel = Math.min(14.0F * energy / 100, 14);
        double xScale = 15D * progress / 100;
        if (xScale > 15) {
            progress = 0;
        }

        bindTexture(getTexture());
        drawTexturedModalRect( stack, 64 + 2, +33, 177, 32, (int) xScale + 1, 15);

        drawSplitString( stack,
              recipe.getRadAmount() + "☢", 53, 60,
                140 - 5, 4210752
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EnrichHandler recipes, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,13,33).addItemStack(recipes.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT,35,33).addItemStack(recipes.getInput1());
        builder.addSlot(RecipeIngredientRole.OUTPUT,99,33).addItemStack(recipes.getOutput());
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(recipes.getContainer().input.getAllStackInputs());

    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guenrichment.png");
    }


}
