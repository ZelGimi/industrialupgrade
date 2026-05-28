package com.denfop.integration.jei.environment;

import com.denfop.blockentity.mechanism.BlockEntityElectricDryer;
import com.denfop.blocks.BlockNitrateMud;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IInteractiveJeiCategory;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentalTransformationCategory extends ScreenMain implements IRecipeCategory<EnvironmentalTransformationHandler>{

    private static final int WIDTH = 176;
    private static final int HEIGHT = 138;

    private static final int TEXT_COLOR = 0x404040;
    private static final int MUTED_TEXT_COLOR = 0x606060;
    private static final int PANEL_COLOR = 0xFFEAF4F0;
    private static final int PANEL_BORDER_DARK = 0xFF4A4A4A;
    private static final int PANEL_BORDER_LIGHT = 0xFFFFFFFF;
    private static final int PANEL_INNER_BORDER = 0xFFB7C9C4;

    private static final int TEXT_X = 7;
    private static final int TEXT_WIDTH = WIDTH - 14;
    private static final int TITLE_Y = 7;
    private static final int DESCRIPTION_Y = 21;
    private static final int SLOT_Y = 64;
    private static final int BOTTOM_Y = 93;
    private static final int LINE_HEIGHT = 10;

    private final IDrawable bg;
    private final JeiInform jeiInform;

    public EnvironmentalTransformationCategory(
            final IGuiHelper guiHelper,
            final JeiInform jeiInform
    ) {
        super(((BlockEntityElectricDryer) BlockBaseMachine3Entity.electric_dryer.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = Component.literal(getTitles());
        this.bg = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate("environmental_transformations.jei");
    }

    @SuppressWarnings("removal")
    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public RecipeType<EnvironmentalTransformationHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder,
            EnvironmentalTransformationHandler recipe,
            IFocusGroup focuses
    ) {
        if (recipe.getType() == EnvironmentalTransformationHandler.Type.PEAT_FROM_COMPOSTER) {
            builder.addSlot(RecipeIngredientRole.INPUT, 14, SLOT_Y).addItemStack(recipe.getFirstInput());
            builder.addSlot(RecipeIngredientRole.INPUT, 40, SLOT_Y).addItemStack(recipe.getSecondInput());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 136, SLOT_Y).addItemStack(recipe.getFinalOutput());
            return;
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 10, SLOT_Y).addItemStack(recipe.getFirstInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 34, SLOT_Y).addItemStack(recipe.getSecondInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 88, SLOT_Y).addItemStack(recipe.getIntermediateOutput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 142, SLOT_Y).addItemStack(recipe.getFinalOutput());
    }

    @Override
    public void draw(
            EnvironmentalTransformationHandler recipe,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphics guiGraphics,
            double mouseX,
            double mouseY
    ) {
        drawPanel(guiGraphics);

        drawWrappedText(guiGraphics, Localization.translate(recipe.getTitleKey()), TEXT_X, TITLE_Y, TEXT_WIDTH, TEXT_COLOR, 1);
        drawWrappedText(guiGraphics, Localization.translate(recipe.getDescriptionKey()), TEXT_X, DESCRIPTION_Y, TEXT_WIDTH, MUTED_TEXT_COLOR, 3);

        if (recipe.getType() == EnvironmentalTransformationHandler.Type.PEAT_FROM_COMPOSTER) {
            drawArrow(guiGraphics, 67, SLOT_Y + 4, 58);
            drawWrappedCenteredText(guiGraphics, Localization.translate("environmental_transformations.condition"), WIDTH / 2, BOTTOM_Y, TEXT_WIDTH, MUTED_TEXT_COLOR, 2);
            drawWrappedCenteredText(guiGraphics, Localization.translate("environmental_transformations.peat.hint"), WIDTH / 2, BOTTOM_Y + 24, TEXT_WIDTH, MUTED_TEXT_COLOR, 2);
            return;
        }

        drawArrow(guiGraphics, 58, SLOT_Y + 4, 28);
        drawArrow(guiGraphics, 112, SLOT_Y + 4, 24);
        drawWrappedCenteredText(guiGraphics, Localization.translate("environmental_transformations.stage_line"), WIDTH / 2, BOTTOM_Y, TEXT_WIDTH, MUTED_TEXT_COLOR, 2);
        drawWrappedCenteredText(guiGraphics, Localization.translate("environmental_transformations.time") + " " + recipe.getTotalTimeSeconds() + "s", WIDTH / 2, BOTTOM_Y + 24, TEXT_WIDTH, MUTED_TEXT_COLOR, 1);
    }

    @Override
    public List<Component> getTooltipStrings(
            EnvironmentalTransformationHandler recipe,
            IRecipeSlotsView recipeSlotsView,
            double mouseX,
            double mouseY
    ) {
        List<Component> tooltips = new ArrayList<>();

        if (recipe.getType() == EnvironmentalTransformationHandler.Type.PEAT_FROM_COMPOSTER) {
            if (isInside(mouseX, mouseY, 7, BOTTOM_Y - 2, WIDTH - 14, 42)) {
                tooltips.add(Component.literal(Localization.translate("environmental_transformations.peat.tooltip")));
            }
            return tooltips;
        }

        if (isInside(mouseX, mouseY, 7, BOTTOM_Y - 2, WIDTH - 14, 42)) {
            tooltips.add(Component.literal(Localization.translate("environmental_transformations.saltpeter.tooltip1")));
            tooltips.add(Component.literal(Localization.translate("environmental_transformations.saltpeter.tooltip2") + " " + (BlockNitrateMud.STAGE_TIME / 20) + "s"));
        }

        return tooltips;
    }

    private void drawPanel(GuiGraphics guiGraphics) {
        guiGraphics.fill(0, 0, WIDTH, HEIGHT, PANEL_BORDER_DARK);
        guiGraphics.fill(1, 1, WIDTH - 1, HEIGHT - 1, PANEL_BORDER_LIGHT);
        guiGraphics.fill(3, 3, WIDTH - 3, HEIGHT - 3, PANEL_INNER_BORDER);
        guiGraphics.fill(5, 5, WIDTH - 5, HEIGHT - 5, PANEL_COLOR);
    }

    private void drawArrow(GuiGraphics guiGraphics, int x, int y, int width) {
        Font font = Minecraft.getInstance().font;
        int centerY = y + 5;
        guiGraphics.hLine(x, x + width - 8, centerY, 0xFF606060);
        guiGraphics.drawString(font, ">", x + width - 7, y, MUTED_TEXT_COLOR, false);
    }

    private void drawWrappedText(
            GuiGraphics guiGraphics,
            String text,
            int x,
            int y,
            int width,
            int color,
            int maxLines
    ) {
        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> lines = font.split(Component.literal(text), width);
        int linesToDraw = Math.min(maxLines, lines.size());

        for (int i = 0; i < linesToDraw; i++) {
            guiGraphics.drawString(font, lines.get(i), x, y + i * LINE_HEIGHT, color, false);
        }
    }

    private void drawWrappedCenteredText(
            GuiGraphics guiGraphics,
            String text,
            int centerX,
            int y,
            int width,
            int color,
            int maxLines
    ) {
        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> lines = font.split(Component.literal(text), width);
        int linesToDraw = Math.min(maxLines, lines.size());

        for (int i = 0; i < linesToDraw; i++) {
            FormattedCharSequence line = lines.get(i);
            guiGraphics.drawString(font, line, centerX - font.width(line) / 2, y + i * LINE_HEIGHT, color, false);
        }
    }

    private boolean isInside(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    protected ResourceLocation getTexture() {
        return null;
    }
}