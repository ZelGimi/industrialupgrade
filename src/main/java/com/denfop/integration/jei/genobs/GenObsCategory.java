package com.denfop.integration.jei.genobs;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TileEntitySingleFluidAdapter;
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

public class GenObsCategory extends GuiIU implements IRecipeCategory<GenObsHandler> {

    private final JeiInform jeiInform;

    private final IDrawableStatic bg;
    private int energy = 0;
    private int progress = 0;

    public GenObsCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntitySingleFluidAdapter) BlockBaseMachine3.single_fluid_adapter.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guiobsidiangenerator" +
                        ".png"), 5, 5, 145,
                78
        );
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine1, 1, 10).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(GenObsHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        progress++;
        energy++;

        int xScale = 32 * progress / 100;
        if (xScale > 32) {
            progress = 0;
        }

        bindTexture(getTexture());


        drawTexturedModalRect( stack,83, 35, 177, 41, xScale, 19);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GenObsHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 42, 20).setFluidRenderer(10000,true,12,47).addFluidStack(recipe.getInput().getFluid(),recipe.getInput().getAmount());
        builder.addSlot(RecipeIngredientRole.INPUT, 65, 20).setFluidRenderer(10000,true,12,47).addFluidStack(recipe.getInput1().getFluid(),recipe.getInput1().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT,  121, 35).addItemStack(recipe.getOutput());
    }

    @Override
    public RecipeType<GenObsHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiobsidiangenerator.png");
    }


}
