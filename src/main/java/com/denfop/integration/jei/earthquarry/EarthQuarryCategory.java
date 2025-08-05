package com.denfop.integration.jei.earthquarry;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.TileEntityElectricDryer;
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

public class EarthQuarryCategory extends GuiIU implements IRecipeCategory<EarthQuarryHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;

    public EarthQuarryCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityElectricDryer) BlockBaseMachine3.electric_dryer.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guivein" +
                        ".png"), 3, 3, 140,
                140
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate("earth_quarry.jei");
    }


    @SuppressWarnings("removal")
    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void draw(EarthQuarryHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        int y = 20;
        int x = 25;
        drawSplitString(stack, Localization.translate("earth_quarry.jei1"), 5, 3,
                140 - 5, 4210752
        );
        drawSplitString(stack, " + " + recipe.chance + "%" + " ->", 20, 30,
                140 - 5, 4210752
        );

    }

    @Override
    public RecipeType<EarthQuarryHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EarthQuarryHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 25).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 60, 25).addItemStack(recipe.getOutput());

    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
