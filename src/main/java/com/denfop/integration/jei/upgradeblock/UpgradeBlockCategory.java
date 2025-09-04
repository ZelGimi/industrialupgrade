package com.denfop.integration.jei.upgradeblock;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityTunerWireless;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
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

public class UpgradeBlockCategory extends ScreenMain implements IRecipeCategory<UpgradeBlockHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public UpgradeBlockCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityTunerWireless) BlockBaseMachine3Entity.tuner.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiUpgradeBlock".toLowerCase() +
                        ".png"), 3, 3, 148,
                80
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }

    @Override
    public RecipeType<UpgradeBlockHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.upgradeblock).getDescriptionId());
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(UpgradeBlockHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        progress++;

        int xScale = 27 * progress / 100;
        int xScale1 = 27 * progress / 100;
        if (xScale > 27) {
            progress = 0;
        }
        draw(stack,
                recipe.metadata.getString("type").isEmpty() ?
                        Localization.translate("upgradeblock_upgrade") : Localization.translate("upgradeblock_modification"),
                64,
                58,
                4210752
        );
        bindTexture(getTexture());

        drawTexturedModalRect(stack, +33, +35, 180, 7, xScale + 1, 16);


        drawTexturedModalRect(stack, +78, +35, 225, 7, xScale1 + 1, 16);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, UpgradeBlockHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 35).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 61, 35).addItemStack(recipe.getInput1());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 35).addItemStack(recipe.getOutput());
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiUpgradeBlock.png".toLowerCase());
    }


}
