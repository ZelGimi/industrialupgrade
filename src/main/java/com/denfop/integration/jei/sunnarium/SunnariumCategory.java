package com.denfop.integration.jei.sunnarium;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityStampMechanism;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
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

import javax.annotation.Nonnull;

public class SunnariumCategory extends ScreenMain implements IRecipeCategory<SunnariumHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public SunnariumCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityStampMechanism) BlockBaseMachine3Entity.stamp_mechanism.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiSunnariumMaker".toLowerCase() +
                        ".png"), 3, 3, 140,
                77
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }

    @Override
    public RecipeType<SunnariumHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.sunnariumpanelmaker).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(SunnariumHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;
        energy++;
        int energylevel = (int) Math.min(14.0F * energy / 100, 14);
        int xScale = (int) (17.0F * progress / 100);
        int xScale1 = (int) (13.0F * progress / 100);
        if (xScale > 17.0F) {
            progress = 0;
        }

        bindTexture(getTexture());


        drawTexturedModalRect(stack, +9, 59 + 14 - energylevel, 176, 14 - energylevel,
                14, energylevel
        );


        drawTexturedModalRect(stack, +46, +21, 177, 20, xScale + 1, 33);
        drawTexturedModalRect(stack, +82, +21, 177, 56, xScale1 + 1, 33);

    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SunnariumHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 29, 18).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 65, 18).addItemStack(recipe.getInput1());
        builder.addSlot(RecipeIngredientRole.INPUT, 29, 40).addItemStack(recipe.getInput2());
        builder.addSlot(RecipeIngredientRole.INPUT, 65, 40).addItemStack(recipe.getInput3());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 29).addItemStack(recipe.getOutput());
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(recipe.getContainer().input.getAllStackInputs());

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiSunnariumMaker.png".toLowerCase());
    }


}
