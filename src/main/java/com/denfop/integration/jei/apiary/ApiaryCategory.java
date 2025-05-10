package com.denfop.integration.jei.apiary;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockApiary;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.triple.heat.TileAdvAlloySmelter;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import com.denfop.integration.jei.IRecipeCategory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class ApiaryCategory extends GuiIU implements IRecipeCategory<ApiaryHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;
    public ApiaryCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileAdvAlloySmelter) BlockBaseMachine1.adv_alloy_smelter.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform=jeiInform;
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                140
        );
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockApiary.apiary).getDescriptionId());
    }


    @Override
    public RecipeType<ApiaryHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ApiaryHandler recipe, IFocusGroup focuses) {
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addFluidStack(recipe.input, 1);
    }

    @Override
    public void draw(ApiaryHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        this.drawSplitString(stack,recipe.output, 5, 3,
                140 - 5, 4210752
        );
    }



    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
