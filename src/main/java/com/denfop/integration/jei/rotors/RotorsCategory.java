package com.denfop.integration.jei.rotors;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityRodManufacturer;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
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

public class RotorsCategory extends ScreenMain implements IRecipeCategory<RotorsHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;

    public RotorsCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityRodManufacturer) BlockBaseMachine3Entity.rods_manufacturer.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorsr_jei" +
                        ".png"), 5, 5, 140,
                80
        );
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine2, 1, 17).getDescriptionId());
    }

    @Override
    public RecipeType<RotorsHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RotorsHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 15).addItemStack(recipe.getInputs()[0]);
        builder.addSlot(RecipeIngredientRole.INPUT, 30, 37).addItemStack(recipe.getInputs()[0]);
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 37).addItemStack(recipe.getInputs()[4]);
        builder.addSlot(RecipeIngredientRole.INPUT, 74, 37).addItemStack(recipe.getInputs()[0]);
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 59).addItemStack(recipe.getInputs()[0]);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 113, 37).addItemStack(recipe.getOutput());

    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorsr_jei.png");
    }


}
