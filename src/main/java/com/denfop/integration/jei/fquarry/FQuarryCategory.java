package com.denfop.integration.jei.fquarry;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TileEntityFluidIntegrator;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class FQuarryCategory extends GuiIU implements IRecipeCategory<FQuarryHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;
    private int energy = 0;

    public FQuarryCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityFluidIntegrator) BlockBaseMachine3.fluid_integrator.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform=jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquantumquerry.png"), 3, 3, 160,
                80
        );
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.machines, 1, 8).getDescriptionId());
    }



    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public RecipeType<FQuarryHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FQuarryHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,8, 21).addItemStack(new ItemStack(IUItem.analyzermodule.getItem()));
        builder.addSlot(RecipeIngredientRole.OUTPUT,32, 12).addItemStack(recipe.getOutput());
        builder.addSlot(RecipeIngredientRole.INPUT,8, 39).addItemStack( ItemStackHelper.fromData(IUItem.module9, 1));
    }



    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquantumquerry.png");
    }


}
