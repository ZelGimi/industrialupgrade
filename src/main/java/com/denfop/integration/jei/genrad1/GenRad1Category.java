package com.denfop.integration.jei.genrad1;

import com.denfop.Constants;
import com.denfop.blockentity.mechanism.BlockEntitySingleFluidAdapter;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class GenRad1Category extends ScreenMain implements IRecipeCategory<GenRad1Handler> {

    private final JeiInform jeiInform;

    private final IDrawableStatic bg;

    public GenRad1Category(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntitySingleFluidAdapter) BlockBaseMachine3Entity.single_fluid_adapter.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guipurifier" +
                        ".png"), 5, 4, 165,
                77
        );

    }

    @Override
    public RecipeType<GenRad1Handler> getRecipeType() {
        return jeiInform.recipeType;
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3Entity.radiation_purifier).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GenRad1Handler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 78 - 4, 35 - 2).addItemStack(recipe.getOutput());
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guipurifier.png");
    }


}
