package com.denfop.integration.jei.spacebody;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.TileEntitySolidMixer;
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

public class SpaceBodyCategory extends GuiIU implements IRecipeCategory<SpaceBodyHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;
    public SpaceBodyCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntitySolidMixer) BlockBaseMachine3.solid_mixer.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                170
        );
    }

    @Override
    public RecipeType<SpaceBodyHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate("spacebody_jei");
    }



    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void draw(SpaceBodyHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        drawSplitString( stack,
                Localization.translate("iu.space_recipe.jei") + Localization.translate("iu.body." + recipe.body.getName().toLowerCase()),
                5,
                3,
                150 - 5,
                4210752
        );
       drawSplitString( stack,
                Localization.translate("iu.space_recipe.jei1"), 5, 20,
               150 - 5, 4210752
        );

        drawSplitString( stack,
                Localization.translate("iu.start_space.info"), 5, 110,
                150 - 5, 4210752
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SpaceBodyHandler recipe, IFocusGroup focuses) {
        int amount = recipe.getInput().size() + recipe.getOutput().size();
        for (int i = 0; i < amount; i++) {
            int x = 5 + (i % 6) * 20;
            int y = 65 + (i / 6) * 19;
            if (i < recipe.getInput().size()) {
                builder.addSlot(RecipeIngredientRole.OUTPUT,x,y).addItemStack(recipe.getInput().get(i));

            } else {
                builder.addSlot(RecipeIngredientRole.OUTPUT,x,y).setFluidRenderer(recipe.getOutput().get(i-recipe.getInput().size()).getAmount(),true,16,16).addFluidStack(recipe.getOutput().get(i-recipe.getInput().size()).getFluid(),recipe.getOutput().get(i-recipe.getInput().size()).getAmount());
            }

        }
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
