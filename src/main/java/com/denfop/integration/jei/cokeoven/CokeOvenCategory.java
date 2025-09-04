package com.denfop.integration.jei.cokeoven;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityLaserPolisher;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
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

public class CokeOvenCategory extends ScreenMain implements IRecipeCategory<CokeOvenHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;
    private int progress = 0;

    public CokeOvenCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityLaserPolisher) BlockBaseMachine3Entity.laser_polisher.getDummyTe()).getGuiContainer1(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guicokeoven" +
                        ".png"), 5, 5, 168,
                92
        );
    }

    @Override
    public RecipeType<CokeOvenHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(new ItemStack(IUItem.cokeoven.getItem(), 1).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(CokeOvenHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;
        int xScale = (int) (38.0 * progress / 100D);

        if (xScale >= 38.0) {
            progress = 0;
        }

        bindTexture(getTexture());
        drawTexturedModalRect(stack, +83, +41, 177, 19, xScale, 11);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CokeOvenHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 57, 38).addItemStack(recipe.getStack());
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 17).setFluidRenderer(10000, true, 12, 47).addFluidStack(FluidName.fluidsteam.getInstance().get(), 1000);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 17).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getOutput().getFluid(), recipe.getOutput().getAmount());
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicokeoven.png");
    }


}
