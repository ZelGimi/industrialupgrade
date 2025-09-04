package com.denfop.integration.jei.worldcollector.end;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityEnchanterBooks;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
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

public class EndCategory extends ScreenMain implements IRecipeCategory<EndHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public EndCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityEnchanterBooks) BlockBaseMachine3Entity.enchanter_books.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiendassembler" +
                        ".png"), 5, 5, 140,
                75
        );
    }

    @Override
    public RecipeType<EndHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine2, 1, 23).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(EndHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        progress++;
        energy++;
        int energylevel = (int) Math.min(51.0F * energy / 100, 51.0F);
        int xScale = (int) (34 * progress / 100);

        if (xScale > 34) {
            progress = 0;
        }

        bindTexture(getTexture());


        drawTexturedModalRect(stack,
                25 + 1, 12 + 51 - energylevel, 179, 2 + 51 - energylevel,
                5, energylevel
        );


        drawTexturedModalRect(stack, +66 - 5, +34 - 5, 177, 60, xScale, 18);
        drawSplitString(stack,
                Localization.translate("iu.need_info") + recipe.getNeed() + Localization.translate("iu.need_info_matter"),
                79,
                54,
                150 - 69,
                ModUtils.convertRGBcolorToInt(0, 0, 0)
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EndHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 39, 19).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 105, 30).addItemStack(recipe.getOutput());
    }

    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiendassembler.png");
    }


}
