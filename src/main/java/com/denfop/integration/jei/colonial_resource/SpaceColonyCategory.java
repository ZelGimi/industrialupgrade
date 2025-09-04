package com.denfop.integration.jei.colonial_resource;

import com.denfop.Constants;
import com.denfop.blockentity.mechanism.BlockEntityLaserPolisher;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
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

public class SpaceColonyCategory extends ScreenMain implements IRecipeCategory<SpaceColonyHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;

    public SpaceColonyCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityLaserPolisher) BlockBaseMachine3Entity.laser_polisher.getDummyTe()).getGuiContainer1(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                170
        );
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate("spacecolony_jei");
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void draw(SpaceColonyHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        drawSplitString(stack,
                Localization.translate("iu.space_recipe.jei") + Localization.translate("iu.body." + recipe.body.getName().toLowerCase()),
                5,
                3,
                140 - 5,
                4210752
        );
        drawSplitString(stack,
                Localization.translate("iu.space_recipe.jei2"), 5, 20,
                140 - 5, 4210752
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SpaceColonyHandler recipes, IFocusGroup focuses) {
        int amount = recipes.getInput().size() + recipes.getOutput().size();
        for (int i = 0; i < amount; i++) {
            int x = 5 + (i % 6) * 20;
            int y = 65 + (i / 6) * 19;
            if (i < recipes.getInput().size()) {
                builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(recipes.getInput().get(i));
            } else {
                builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).setFluidRenderer(1, true, 16, 16).addFluidStack(recipes.getOutput().get(i - recipes.getInput().size()).getFluid(), recipes.getOutput().get(i - recipes.getInput().size()).getAmount());
            }

        }
    }

    @Override
    public RecipeType<SpaceColonyHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
