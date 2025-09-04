package com.denfop.integration.jei.convertermatter;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.multimechanism.simple.BlockEntityCompressor;
import com.denfop.blocks.mechanism.BlockSimpleMachineEntity;
import com.denfop.containermenu.ContainerMenuMultiMachine;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
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

import javax.annotation.Nonnull;

public class ConverterCategory extends ScreenMain implements IRecipeCategory<ConverterHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public ConverterCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(new ContainerMenuMultiMachine(Minecraft.getInstance().player,
                ((BlockEntityCompressor) BlockSimpleMachineEntity.compressor_iu.getDummyTe()), 1, true
        ));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiconvertersolidmatter.png"), 3, 5,
                168,
                135
        );
    }

    @Override
    public RecipeType<ConverterHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.convertersolidmatter, 1, 0).getDescriptionId());
    }


    @SuppressWarnings("removal")
    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(ConverterHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        progress++;
        energy++;
        int energylevel = (int) Math.min(38.0F * energy / 100, 38);

        int xScale = 31 * progress / 100;
        if (xScale > 31) {
            progress = 0;
        }

        bindTexture(getTexture());
        drawTexturedModalRect(stack, 116, 110, 176,
                81, energylevel, 11
        );


        drawTexturedModalRect(stack, 78, 46, 176, 24, xScale, 16);
        drawTexturedModalRect(stack, (166) - xScale, 46, 208 - xScale, 24, xScale, 16);

        drawTexturedModalRect(stack, 114, 9 + 1, 177, 42, 16, xScale);

        drawTexturedModalRect(stack, 114, 98 - xScale, 177, 74 - xScale, 16, xScale);

    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ConverterHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 113, 45).addItemStack(recipe.getOutput());
        builder.addSlot(RecipeIngredientRole.INPUT, 47, 45).addItemStack(recipe.getOutput());
    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiconvertersolidmatter.png");
    }


}
