package com.denfop.integration.jei.quantummolecular;

import com.denfop.Constants;
import com.denfop.blockentity.mechanism.BlockEntityLaserPolisher;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
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

public class QuantumMolecularCategory extends ScreenMain implements IRecipeCategory<QuantumMolecularHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;
    private int progress = 0;

    public QuantumMolecularCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityLaserPolisher) BlockBaseMachine3Entity.laser_polisher.getDummyTe()).getGuiContainer1(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guidoublemoleculartransformernew" +
                        ".png"), 10, 49, 216,
                73
        );
    }


    @Nonnull
    @Override
    public String getTitles() {
        return JEICompat.getBlockStack(BlockBaseMachine3Entity.quantum_transformer).getDisplayName().getString();
    }

    @Override
    public RecipeType<QuantumMolecularHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(QuantumMolecularHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        progress++;
        double xScale = 18.0D * progress / 100;
        if (xScale > 18) {
            progress = 0;
        }
        bindTexture(getTexture());
        drawTexturedModalRect(stack, 31 - 11, 75 - 49, 43, 237, 14, (int) xScale);
        int y = 5;
        int x = 65;
        drawSplitString(stack, recipe.inputText, x, y, 246 - x, 16777215);
        y += 18 + 1;
        drawSplitString(stack, recipe.inputText1, x, y, 246 - x, 16777215);
        y += 18 + 1;
        drawSplitString(stack, recipe.outputText, x, y, 246 - x, 16777215);
        y += 18 + 1;
        drawString(stack, recipe.totalEU, x, y, 16777215);
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, QuantumMolecularHandler recipes, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 7).addItemStack(recipes.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 7).addItemStack(recipes.getInput1());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 18, 47).addItemStack(recipes.getOutput());
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guidoublemoleculartransformernew.png");
    }


}
