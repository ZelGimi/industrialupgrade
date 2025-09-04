package com.denfop.integration.jei.primalrolling;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityFluidIntegrator;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
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
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class PrimalRollingCategory extends ScreenMain implements IRecipeCategory<PrimalRollingHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;

    public PrimalRollingCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityFluidIntegrator) BlockBaseMachine3Entity.fluid_integrator.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                140
        );
    }

    @Override
    public RecipeType<PrimalRollingHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3Entity.rolling_machine).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void draw(PrimalRollingHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        drawSplitString(stack, Localization.translate("iu.rolling.jei"), 5, 3,
                150 - 5, 4210752
        );
        drawSplitString(stack, "+", 26, 31,
                150 - 5, 4210752
        );
        drawSplitString(stack, "->", 47, 31,
                150 - 5, 4210752
        );
        drawSplitString(stack, Localization.translate("iu.rolling.jei1") + "." + Localization.translate(
                        "iu.rolling.jei2"), 5, 45,
                150 - 5, 4210752
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PrimalRollingHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 25).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 30, 25).addItemStack(new ItemStack(IUItem.cutter.getItem()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 60, 25).addItemStack(recipe.getOutput());

    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
