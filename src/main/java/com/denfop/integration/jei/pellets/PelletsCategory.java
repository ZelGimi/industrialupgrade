package com.denfop.integration.jei.pellets;

import com.denfop.Constants;
import com.denfop.blockentity.mechanism.multimechanism.simple.BlockEntityOreWashing;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.blocks.mechanism.BlockMoreMachine3Entity;
import com.denfop.containermenu.ContainerMenuMultiMachine;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
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

public class PelletsCategory extends ScreenMain implements IRecipeCategory<PelletsHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;

    public PelletsCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(new ContainerMenuMultiMachine(Minecraft.getInstance().player,
                ((BlockEntityOreWashing) BlockMoreMachine3Entity.orewashing.getDummyTe()), 1, true
        ));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guipallet" +
                        ".png"), 5, 3, 165,
                77
        );

    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3Entity.pallet_generator).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(PelletsHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        bindTexture(getTexture());
        for (int x = 30; x <= 86 - 18; x += 18) {
            for (int y = 18; y <= 36; y += 18) {
                this.drawTexturedModalRect(stack, 0 + x, 0 + y, 179, 30, 18, 18);
            }
        }
        final double num = 20000 / Math.ceil(recipe.getInput());

        drawSplitString(stack,
                String.format("%.2f☢ -> 50 EF", num),
                90,
                30,
                200 - 10,
                ModUtils.convertRGBcolorToInt(255, 255, 255)
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PelletsHandler recipe, IFocusGroup focuses) {
        for (int i = 0; i < recipe.getCol(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 31 + (i % 3) * 18, 19 + (i / 3) * 18).addItemStack(recipe.getInput2());

        }
    }

    @Override
    public RecipeType<PelletsHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guipallet.png");
    }


}
