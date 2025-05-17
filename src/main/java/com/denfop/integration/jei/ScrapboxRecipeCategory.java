package com.denfop.integration.jei;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.tiles.mechanism.TileEntityUpgradeMachineFactory;
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

public class ScrapboxRecipeCategory extends GuiIU implements IRecipeCategory<ScrapboxRecipeHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;

    public ScrapboxRecipeCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityUpgradeMachineFactory) BlockBaseMachine3.upgrade_machine.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(
                new ResourceLocation(Constants.MOD_ID + ":textures/gui/ScrapboxRecipes.png".toLowerCase()),
                55,
                30,
                82,
                26
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }

    @Override
    public RecipeType<ScrapboxRecipeHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(Constants.ABBREVIATION + ".crafting.scrap_box");
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void draw(ScrapboxRecipeHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        float value = (float) recipe.getNeed();
        String text;
        if ((double) value < 0.001) {
            text = "< 0.01";
        } else {
            text = "  " + String.format("%.2f", value * 100.0F);
        }

        Minecraft.getInstance().font.draw(stack,text + "%", 86, 9, 4210752);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ScrapboxRecipeHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,1,5).addItemStack(IUItem.scrapBox);
        builder.addSlot(RecipeIngredientRole.OUTPUT,61,5).addItemStack(recipe.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
