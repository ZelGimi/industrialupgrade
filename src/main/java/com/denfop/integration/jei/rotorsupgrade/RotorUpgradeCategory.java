package com.denfop.integration.jei.rotorsupgrade;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityRodManufacturer;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
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

public class RotorUpgradeCategory extends ScreenMain implements IRecipeCategory<RotorUpgradeHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;

    public RotorUpgradeCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityRodManufacturer) BlockBaseMachine3Entity.rods_manufacturer.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorsupgrade_jei" +
                        ".png"), 5, 5, 140,
                80
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }

    @Override
    public RecipeType<RotorUpgradeHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine2, 1, 18).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void draw(RotorUpgradeHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {

    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RotorUpgradeHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 51 + 23, 37).addItemStack(recipe.getInputs()[1]);
        builder.addSlot(RecipeIngredientRole.INPUT, 29 + 23, 37).addItemStack(recipe.getInputs()[0]);
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorsupgrade_jei.png");
    }


}
