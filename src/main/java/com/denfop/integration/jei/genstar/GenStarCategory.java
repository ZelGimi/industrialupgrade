package com.denfop.integration.jei.genstar;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TileEntityNuclearWasteRecycler;
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

public class GenStarCategory extends GuiIU implements IRecipeCategory<GenStarHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public GenStarCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityNuclearWasteRecycler) BlockBaseMachine3.nuclear_waste_recycler.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/GuiWitherMaker".toLowerCase() +
                        ".png"), 3, 3, 147,
                78
        );
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine, 1, 13).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public RecipeType<GenStarHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void draw(GenStarHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        progress++;


        int xScale = (int) (22D * progress / 100D);
        if (xScale >= 22) {
            progress = 0;
        }


        bindTexture(getTexture());


        drawTexturedModalRect(stack, +74, +32, 177, 0, xScale, 18);

    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GenStarHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 24).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 23, 24).addItemStack(recipe.getInput1());
        builder.addSlot(RecipeIngredientRole.INPUT, 41, 24).addItemStack(recipe.getInput2());
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 42).addItemStack(recipe.getInput3());
        builder.addSlot(RecipeIngredientRole.INPUT, 23, 42).addItemStack(recipe.getInput4());
        builder.addSlot(RecipeIngredientRole.INPUT, 41, 42).addItemStack(recipe.getInput5());
        builder.addSlot(RecipeIngredientRole.INPUT, 23, 60).addItemStack(recipe.getInput6());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 117, 33).addItemStack(recipe.getOutput());
    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/GuiWitherMaker.png".toLowerCase());
    }


}
