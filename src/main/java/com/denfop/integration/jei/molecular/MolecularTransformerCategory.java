package com.denfop.integration.jei.molecular;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TileModuleMachine;
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

public class MolecularTransformerCategory extends GuiIU implements IRecipeCategory<MolecularTransformerHandler> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private final JeiInform jeiInform;
    public MolecularTransformerCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileModuleMachine) BlockBaseMachine.modulator.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimoleculartransformernew" +
                        ".png"), 10, 49, 203,
                73
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.blockmolecular).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(MolecularTransformerHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;
        double xScale = 18.0D * progress / 100;
        if (xScale > 18) {
            progress = 0;
        }
        drawTexturedModalRect(stack,23 - 10, 75 - 49, 242, 32, 14, (int) xScale);
        int y = 5;
        int x = 49;
        drawSplitString( stack, recipe.inputText, x, y, 200 - x, 16777215);
        y += 18;
        drawSplitString( stack, recipe.outputText, x, y, 200 - x, 16777215);
        y += 18;
        this.font.draw( stack,recipe.totalEU, x, y, 16777215);

    }

    @Override
    public RecipeType<MolecularTransformerHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MolecularTransformerHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,12,8).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT,12,48).addItemStack(recipe.getOutput());
    }



    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimoleculartransformernew.png");
    }


}
