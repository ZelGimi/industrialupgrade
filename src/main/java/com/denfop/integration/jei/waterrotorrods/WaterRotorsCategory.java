package com.denfop.integration.jei.waterrotorrods;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TileEntityUpgradeMachineFactory;
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

public class WaterRotorsCategory extends GuiIU implements IRecipeCategory<WaterRotorsHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;
    public WaterRotorsCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityUpgradeMachineFactory) BlockBaseMachine3.upgrade_machine.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorsr_jei" +
                        ".png"), 5, 5, 140,
                80
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }

    @Override
    public RecipeType<WaterRotorsHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate( ItemStackHelper.fromData(IUItem.basemachine2, 1, 44).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void draw(WaterRotorsHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {

     }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WaterRotorsHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,52,15).addItemStack(recipe.getInputs()[0]);
        builder.addSlot(RecipeIngredientRole.INPUT,30,37).addItemStack(recipe.getInputs()[0]);
        builder.addSlot(RecipeIngredientRole.INPUT,52,37).addItemStack(recipe.getInputs()[4]);
        builder.addSlot(RecipeIngredientRole.INPUT,74,37).addItemStack(recipe.getInputs()[0]);
        builder.addSlot(RecipeIngredientRole.INPUT,52,59).addItemStack(recipe.getInputs()[0]);
        builder.addSlot(RecipeIngredientRole.OUTPUT,113,37).addItemStack(recipe.getOutput());
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorsr_jei.png");
    }


}
