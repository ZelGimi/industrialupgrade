package com.denfop.integration.jei.multiblock;

import com.denfop.Constants;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.blockentity.mechanism.BlockEntityMoonSpotter;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IRecipeCategory;
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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class MultiBlockCategory extends ScreenMain implements IRecipeCategory<MultiBlockHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;

    public MultiBlockCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityMoonSpotter) BlockBaseMachine3Entity.moon_spotter.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                170
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate("multiblock.jei");
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void draw(MultiBlockHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        String name1 = Localization.translate("multiblock.jei1");
        Minecraft minecraft = Minecraft.getInstance();
        int xCenter = 140 / 2;
        int maxWidth = 140 - 20;
        int color = 4210752;

        renderTextCentered(stack, minecraft, name1, xCenter, 25, maxWidth, color);
        int y = 2;

        renderTextCentered(stack, minecraft, Localization.translate("multiblock.jei2"), xCenter, (57 + y * 25), maxWidth,
                color
        );

        renderTextCentered(stack, minecraft, Localization.translate("multiblock.jei3"), xCenter, (10 + y * 25) + 90, maxWidth,
                color
        );
    }

    public void renderTextCentered(GuiGraphics stack, @Nonnull Minecraft minecraft, String text, int xCenter, int y, int maxWidth, int color) {
        Font fontRenderer = minecraft.font;


        List<FormattedText> lines = fontRenderer.getSplitter().splitLines(text, maxWidth, Style.EMPTY);


        int lineHeight = 9;


        int totalHeight = lines.size() * lineHeight;


        int startY = y - totalHeight / 2;


        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).getString();
            int lineWidth = fontRenderer.width(line);
            int x = xCenter - lineWidth / 2;
            draw(stack, line, x, startY + i * lineHeight, color);
        }
    }

    @Override
    public RecipeType<MultiBlockHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MultiBlockHandler recipes, IFocusGroup focuses) {
        final MultiBlockStructure structure = recipes.getStructure();
        for (int i = 0; i < structure.itemStackList.size(); i++) {
            int x = 5 + (i % 6) * 20;
            int y = 45 + (i / 6) * 19;
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addItemStack(structure.itemStackList.get(i));

        }
        double y = 2;

        builder.addSlot(RecipeIngredientRole.INPUT, 61, (int) (5 + 27 + y * 23)).addItemStack(structure.itemStackList.get(0));

    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
