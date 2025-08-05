package com.denfop.integration.jei.synthesis;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TileEntityStampMechanism;
import com.denfop.utils.ModUtils;
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

public class SynthesisCategory extends GuiIU implements IRecipeCategory<SynthesisHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public SynthesisCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityStampMechanism) BlockBaseMachine3.stamp_mechanism.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guisynthesis" +
                        ".png"), 3, 3, 148,
                80
        );
    }

    @Override
    public RecipeType<SynthesisHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine, 1, 11).getDescriptionId());
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(SynthesisHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        progress++;
        energy++;
        int energylevel = (int) Math.min(14.0F * energy / 100, 14);
        int xScale = 15 * progress / 100;
        int xScale1 = 20 * progress / 100;
        if (xScale > 15) {
            progress = 0;
        }

        bindTexture(getTexture());

        drawTexturedModalRect(stack, +129, 32 + 14 - energylevel, 176, 14 - energylevel,
                14, energylevel
        );


        drawTexturedModalRect(stack, +39, +32, 177, 33, xScale + 1, 14);

        drawTexturedModalRect(stack, +75, +30, 177, 52, xScale1 + 1, 23);
        draw(
                stack, Localization.translate("chance") + recipe.getPercent() + "%",
                64,
                64,
                ModUtils.convertRGBcolorToInt(255, 255, 255)
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SynthesisHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 31).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 56, 31).addItemStack(recipe.getInput1());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 31).addItemStack(recipe.getOutput());
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(recipe.getContainer().input.getAllStackInputs());

    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guisynthesis.png");
    }


}
