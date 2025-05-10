package com.denfop.integration.jei.waterrotorsupgrade;

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
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class WaterRotorUpgradeCategory extends GuiIU implements IRecipeCategory<WaterRotorUpgradeHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;
    public WaterRotorUpgradeCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityUpgradeMachineFactory) BlockBaseMachine3.upgrade_machine.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorsupgrade_jei" +
                        ".png"), 5, 5, 140,
                80
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }

    @Override
    public RecipeType<WaterRotorUpgradeHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine2, 1, 44).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WaterRotorUpgradeHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,52+22,37).addItemStack(recipe.getInputs()[1]);
        builder.addSlot(RecipeIngredientRole.INPUT,30+22,37).addItemStack(recipe.getInputs()[0]);
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorsupgrade_jei.png");
    }


}
