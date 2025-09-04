package com.denfop.integration.jei.bf;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityLaserPolisher;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.Fluids;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
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
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class BlastFCategory extends ScreenMain implements IRecipeCategory<BlastFHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;
    private int progress = 0;

    public BlastFCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityLaserPolisher) BlockBaseMachine3Entity.laser_polisher.getDummyTe()).getGuiContainer1(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guiblastfurnace_jei" +
                        ".png"), 5, 5, 168,
                92
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(new ItemStack(IUItem.blastfurnace.getItem(0), 1).getDescriptionId());
    }


    @Override
    public RecipeType<BlastFHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(BlastFHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;
        int xScale = (int) (38.0 * progress / 100D);

        if (xScale >= 38.0) {
            progress = 0;
        }

        bindTexture(getTexture());
        drawTexturedModalRect(stack, +83, +41, 177, 19, xScale, 11);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BlastFHandler recipes, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 58, 39).addItemStack(recipes.getStack());
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 17).setFluidRenderer(10000, true, 12, 47).addFluidStack(Fluids.WATER, 10000);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 133, 39).addItemStack(recipes.getOutput());

    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiblastfurnace_jei.png");
    }


}
