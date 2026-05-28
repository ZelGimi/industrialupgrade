package com.denfop.integration.jei.multiblock;

import com.denfop.Constants;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.blockentity.mechanism.BlockEntityMoonSpotter;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IInteractiveJeiCategory;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.platform.InputConstants;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MultiBlockCategory extends ScreenMain implements IRecipeCategory<MultiBlockHandler>, IInteractiveJeiCategory<MultiBlockHandler> {

    private static final int WIDTH = 176;
    private static final int HEIGHT = 206;

    private static final int PREVIEW_X = 8;
    private static final int PREVIEW_Y = 22;
    private static final int PREVIEW_W = 160;
    private static final int PREVIEW_H = 96;

    private static final int CONTROLS_Y = 121;

    private static final int SLOT_START_X = 8;
    private static final int SLOT_START_Y = 138;
    private static final int SLOT_COLUMNS = 8;
    private static final int SLOT_SIZE = 18;
    private static final int MAX_VISIBLE_STACKS = 16;

    private static final int MORE_Y = 176;
    private static final int STATS_Y = 182;

    private static final int MODE_X = 8;
    private static final int MODE_Y = CONTROLS_Y;
    private static final int MODE_W = 34;
    private static final int MODE_H = 11;

    private static final int PREV_X = 46;
    private static final int PREV_Y = CONTROLS_Y;
    private static final int PREV_W = 12;
    private static final int PREV_H = 11;

    private static final int NEXT_X = 62;
    private static final int NEXT_Y = CONTROLS_Y;
    private static final int NEXT_W = 12;
    private static final int NEXT_H = 11;

    private static final int STATUS_X = 78;
    private static final int STATUS_Y = CONTROLS_Y;
    private static final int STATUS_W = 44;
    private static final int STATUS_H = 11;

    private static final int RESET_X = 126;
    private static final int RESET_Y = CONTROLS_Y;
    private static final int RESET_W = 42;
    private static final int RESET_H = 11;

    private static final float DEFAULT_YAW = -35.0F;
    private static final float DEFAULT_PITCH = 25.0F;

    private final IDrawableStatic background;
    private final JeiInform jeiInform;

    private MultiBlockJeiRenderer.LayerMode layerMode = MultiBlockJeiRenderer.LayerMode.AUTO;
    private int manualLayerIndex = 0;

    private float yaw = DEFAULT_YAW;
    private float pitch = DEFAULT_PITCH;

    private boolean draggingPreview = false;
    private double lastMouseX = 0.0D;
    private double lastMouseY = 0.0D;

    public MultiBlockCategory(final IGuiHelper guiHelper, final JeiInform jeiInform) {
        super(((BlockEntityMoonSpotter) BlockBaseMachine3Entity.moon_spotter.getDummyTe())
                .getGuiContainer(Minecraft.getInstance().player));

        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT + 50);
        this.jeiInform = jeiInform;
        this.title = Component.literal(getTitles());
    }

    @Nonnull
    @Override
    public String getTitles() {
        return tr("multiblock.jei");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public RecipeType<MultiBlockHandler> getRecipeType() {
        return this.jeiInform.recipeType;
    }

    @Override
    public void setRecipe(
            final IRecipeLayoutBuilder builder,
            final MultiBlockHandler recipe,
            final IFocusGroup focuses
    ) {
        final List<ItemStack> ingredients = recipe.getIngredientStacks();
        final int visibleCount = Math.min(MAX_VISIBLE_STACKS, ingredients.size());

        for (int i = 0; i < visibleCount; i++) {
            final ItemStack stack = ingredients.get(i).copy();
            if (stack.isEmpty()) {
                continue;
            }

            stack.setCount(Mth.clamp(stack.getCount(), 1, 64));

            final int slotX = SLOT_START_X + (i % SLOT_COLUMNS) * SLOT_SIZE;
            final int slotY = SLOT_START_Y + (i / SLOT_COLUMNS) * SLOT_SIZE;

            builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY)
                    .addItemStack(stack);
        }

        if (ingredients.size() > visibleCount) {
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                    .addItemStacks(ingredients);
        }
    }

    @Override
    public void draw(
            final MultiBlockHandler recipe,
            final IRecipeSlotsView recipeSlotsView,
            final GuiGraphics guiGraphics,
            final double mouseX,
            final double mouseY
    ) {
        final Minecraft minecraft = Minecraft.getInstance();

        normalizeLayer(recipe);
        updateMouseDrag(mouseX, mouseY);

        drawBase(guiGraphics);
        drawTitle(guiGraphics, minecraft, recipe);
        drawButtons(guiGraphics, minecraft, recipe, mouseX, mouseY);
        drawPreview(guiGraphics, recipe, mouseX, mouseY);
        drawInfo(guiGraphics, minecraft, recipe);
    }

    @Override
    public boolean handleInput(
            final MultiBlockHandler recipe,
            final double mouseX,
            final double mouseY,
            final InputConstants.Key input
    ) {
        if (input.getType() != InputConstants.Type.MOUSE || input.getValue() != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return false;
        }

        if (isMouseInside(mouseX, mouseY, MODE_X, MODE_Y, MODE_W, MODE_H)) {
            cycleMode();
            normalizeLayer(recipe);
            return true;
        }

        if (isMouseInside(mouseX, mouseY, PREV_X, PREV_Y, PREV_W, PREV_H)) {
            this.layerMode = MultiBlockJeiRenderer.LayerMode.SINGLE;
            this.manualLayerIndex--;
            normalizeLayer(recipe);
            return true;
        }

        if (isMouseInside(mouseX, mouseY, NEXT_X, NEXT_Y, NEXT_W, NEXT_H)) {
            this.layerMode = MultiBlockJeiRenderer.LayerMode.SINGLE;
            this.manualLayerIndex++;
            normalizeLayer(recipe);
            return true;
        }

        if (isMouseInside(mouseX, mouseY, RESET_X, RESET_Y, RESET_W, RESET_H)) {
            this.yaw = DEFAULT_YAW;
            this.pitch = DEFAULT_PITCH;
            return true;
        }

        return false;
    }

    @Override
    public List<Component> getTooltipStrings(
            final MultiBlockHandler recipe,
            final IRecipeSlotsView recipeSlotsView,
            final double mouseX,
            final double mouseY
    ) {
        if (recipe == null || recipe.getStructure() == null) {
            return Collections.emptyList();
        }

        if (isMouseInside(mouseX, mouseY, MODE_X, MODE_Y, MODE_W, MODE_H)) {
            return List.of(
                    Component.literal(tr("multiblock.jei.tooltip.switch_mode")).withStyle(ChatFormatting.AQUA),
                    Component.literal(tr("multiblock.jei.tooltip.mode_order")).withStyle(ChatFormatting.GRAY)
            );
        }

        if (isMouseInside(mouseX, mouseY, PREV_X, PREV_Y, PREV_W, PREV_H)) {
            return List.of(Component.literal(tr("multiblock.jei.tooltip.previous_layer")).withStyle(ChatFormatting.AQUA));
        }

        if (isMouseInside(mouseX, mouseY, NEXT_X, NEXT_Y, NEXT_W, NEXT_H)) {
            return List.of(Component.literal(tr("multiblock.jei.tooltip.next_layer")).withStyle(ChatFormatting.AQUA));
        }

        if (isMouseInside(mouseX, mouseY, STATUS_X, STATUS_Y, STATUS_W, STATUS_H)) {
            return List.of(Component.literal(tr("multiblock.jei.tooltip.current_mode_layer")).withStyle(ChatFormatting.AQUA));
        }

        if (isMouseInside(mouseX, mouseY, RESET_X, RESET_Y, RESET_W, RESET_H)) {
            return List.of(Component.literal(tr("multiblock.jei.tooltip.reset_rotation")).withStyle(ChatFormatting.AQUA));
        }

        final MultiBlockJeiRenderer.HoveredBlock hovered = MultiBlockJeiRenderer.findHoveredBlock(
                recipe.getStructure(),
                PREVIEW_X,
                PREVIEW_Y,
                PREVIEW_W,
                PREVIEW_H,
                mouseX,
                mouseY,
                this.layerMode,
                this.manualLayerIndex,
                this.yaw,
                this.pitch
        );

        if (hovered == null) {
            return Collections.emptyList();
        }

        final List<Component> tooltip = new ArrayList<>();
        tooltip.add(hovered.stack().getHoverName().copy().withStyle(ChatFormatting.GREEN));

        tooltip.add(Component.literal(trf(
                "multiblock.jei.tooltip.local_pos",
                hovered.relativePos().getX(),
                hovered.relativePos().getY(),
                hovered.relativePos().getZ()
        )).withStyle(ChatFormatting.GRAY));

        if (hovered.direction() != null) {
            tooltip.add(Component.literal(trf(
                    "multiblock.jei.tooltip.facing",
                    getLocalizedDirectionName(hovered.direction().getName())
            )).withStyle(ChatFormatting.AQUA));
        }

        return tooltip;
    }

    private void drawBase(final GuiGraphics guiGraphics) {
        guiGraphics.fill(0, 0, WIDTH, HEIGHT, 0xE0101018);
        guiGraphics.fill(1, 1, WIDTH - 1, HEIGHT - 1, 0xE021212B);

        guiGraphics.fill(
                PREVIEW_X - 1,
                PREVIEW_Y - 1,
                PREVIEW_X + PREVIEW_W + 1,
                PREVIEW_Y + PREVIEW_H + 1,
                0xFF050509
        );

        guiGraphics.fill(
                PREVIEW_X,
                PREVIEW_Y,
                PREVIEW_X + PREVIEW_W,
                PREVIEW_Y + PREVIEW_H,
                0xFF111118
        );

        guiGraphics.fill(
                PREVIEW_X + 1,
                PREVIEW_Y + 1,
                PREVIEW_X + PREVIEW_W - 1,
                PREVIEW_Y + PREVIEW_H - 1,
                0xFF171723
        );

        guiGraphics.fill(6, 135, WIDTH - 6, HEIGHT - 7, 0x70303040);
    }

    private void drawTitle(
            final GuiGraphics guiGraphics,
            final Minecraft minecraft,
            final MultiBlockHandler recipe
    ) {
        final Font font = minecraft.font;

        final String titleText = fitText(font, com.denfop.utils.ModUtils.cleanComponentString(recipe.getDisplayName().getString()), WIDTH - 16);
        final int titleX = Math.max(6, (WIDTH - font.width(titleText)) / 2);

    }

    private void drawButtons(
            final GuiGraphics guiGraphics,
            final Minecraft minecraft,
            final MultiBlockHandler recipe,
            final double mouseX,
            final double mouseY
    ) {
        final Font font = minecraft.font;

        drawButton(guiGraphics, font, tr("multiblock.jei.button.mode"),
                MODE_X, MODE_Y, MODE_W, MODE_H,
                isMouseInside(mouseX, mouseY, MODE_X, MODE_Y, MODE_W, MODE_H));

        drawButton(guiGraphics, font, tr("multiblock.jei.button.previous"),
                PREV_X, PREV_Y, PREV_W, PREV_H,
                isMouseInside(mouseX, mouseY, PREV_X, PREV_Y, PREV_W, PREV_H));

        drawButton(guiGraphics, font, tr("multiblock.jei.button.next"),
                NEXT_X, NEXT_Y, NEXT_W, NEXT_H,
                isMouseInside(mouseX, mouseY, NEXT_X, NEXT_Y, NEXT_W, NEXT_H));

        drawStatusBox(guiGraphics, font, recipe);

        drawButton(guiGraphics, font, tr("multiblock.jei.button.reset"),
                RESET_X, RESET_Y, RESET_W, RESET_H,
                isMouseInside(mouseX, mouseY, RESET_X, RESET_Y, RESET_W, RESET_H));
    }

    private void drawStatusBox(
            final GuiGraphics guiGraphics,
            final Font font,
            final MultiBlockHandler recipe
    ) {
        guiGraphics.fill(STATUS_X, STATUS_Y, STATUS_X + STATUS_W, STATUS_Y + STATUS_H, 0xFF3A3A4A);
        guiGraphics.fill(STATUS_X + 1, STATUS_Y + 1, STATUS_X + STATUS_W - 1, STATUS_Y + STATUS_H - 1, 0xFF1A1A24);

        final int layerCount = Math.max(1, recipe.getLayerCount());
        final int currentLayer = MultiBlockJeiRenderer.getVisibleLayerIndex(
                recipe.getStructure(),
                this.layerMode,
                this.manualLayerIndex
        ) + 1;

        final String statusText = switch (this.layerMode) {
            case AUTO -> trf("multiblock.jei.status.auto", currentLayer, layerCount);
            case ALL -> tr("multiblock.jei.status.all");
            case SINGLE -> trf("multiblock.jei.status.single", currentLayer, layerCount);
        };

        final String fitted = fitText(font, statusText, STATUS_W - 4);

        guiGraphics.drawString(
                font,
                fitted,
                STATUS_X + STATUS_W / 2 - font.width(fitted) / 2,
                STATUS_Y + 2,
                0xAEEBFF,
                false
        );
    }

    private void drawButton(
            final GuiGraphics guiGraphics,
            final Font font,
            final String text,
            final int x,
            final int y,
            final int width,
            final int height,
            final boolean hovered
    ) {
        final int border = hovered ? 0xFF78DFFF : 0xFF3A3A4A;
        final int fill = hovered ? 0xFF2A3C46 : 0xFF1A1A24;
        final int textColor = hovered ? 0xFFFFFFFF : 0xFFBFC7D5;

        guiGraphics.fill(x, y, x + width, y + height, border);
        guiGraphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, fill);

        final String fitted = fitText(font, text, width - 4);

        guiGraphics.drawString(
                font,
                fitted,
                x + width / 2 - font.width(fitted) / 2,
                y + 2,
                textColor,
                false
        );
    }

    private void drawPreview(
            final GuiGraphics guiGraphics,
            final MultiBlockHandler recipe,
            final double mouseX,
            final double mouseY
    ) {
        final MultiBlockStructure structure = recipe.getStructure();

        MultiBlockJeiRenderer.render(
                guiGraphics,
                structure,
                PREVIEW_X,
                PREVIEW_Y,
                PREVIEW_W,
                PREVIEW_H,
                mouseX,
                mouseY,
                this.layerMode,
                this.manualLayerIndex,
                this.yaw,
                this.pitch
        );
    }

    private void drawInfo(
            final GuiGraphics guiGraphics,
            final Minecraft minecraft,
            final MultiBlockHandler recipe
    ) {
        final Font font = minecraft.font;

        final String blocksText = fitText(font, trf("multiblock.jei.stats.blocks", recipe.getBlockCount()), 78);
        final String typesText = fitText(font, trf("multiblock.jei.stats.types", recipe.getTypeCount()), 78);

        guiGraphics.drawString(font, blocksText, 10, STATS_Y, 0xC8C8C8, false);
        guiGraphics.drawString(font, typesText, WIDTH - 10 - font.width(typesText), STATS_Y, 0xC8C8C8, false);

        if (recipe.getTypeCount() > MAX_VISIBLE_STACKS) {
            final String more = trf("multiblock.jei.stats.more", recipe.getTypeCount() - MAX_VISIBLE_STACKS);
            guiGraphics.drawString(
                    font,
                    more,
                    WIDTH / 2 - font.width(more) / 2,
                    MORE_Y,
                    0x8F8F9A,
                    false
            );
        }
    }

    private void updateMouseDrag(final double mouseX, final double mouseY) {
        final boolean leftPressed = isLeftMousePressed();

        if (!leftPressed) {
            this.draggingPreview = false;
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
            return;
        }

        if (!this.draggingPreview) {
            if (isMouseInside(mouseX, mouseY, PREVIEW_X, PREVIEW_Y, PREVIEW_W, PREVIEW_H)) {
                this.draggingPreview = true;
                this.lastMouseX = mouseX;
                this.lastMouseY = mouseY;
            }
            return;
        }

        final double dx = mouseX - this.lastMouseX;
        final double dy = mouseY - this.lastMouseY;

        this.yaw += (float) dx * 0.75F;
        this.pitch += (float) dy * 0.55F;
        this.pitch = Mth.clamp(this.pitch, -75.0F, 75.0F);

        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;
    }

    private boolean isLeftMousePressed() {
        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getWindow() == null) {
            return false;
        }

        return GLFW.glfwGetMouseButton(
                minecraft.getWindow().getWindow(),
                GLFW.GLFW_MOUSE_BUTTON_LEFT
        ) == GLFW.GLFW_PRESS;
    }

    private void cycleMode() {
        this.layerMode = switch (this.layerMode) {
            case AUTO -> MultiBlockJeiRenderer.LayerMode.ALL;
            case ALL -> MultiBlockJeiRenderer.LayerMode.SINGLE;
            case SINGLE -> MultiBlockJeiRenderer.LayerMode.AUTO;
        };
    }

    private void normalizeLayer(final MultiBlockHandler recipe) {
        final int layerCount = Math.max(1, recipe.getLayerCount());

        if (this.manualLayerIndex < 0) {
            this.manualLayerIndex = layerCount - 1;
        }

        if (this.manualLayerIndex >= layerCount) {
            this.manualLayerIndex = 0;
        }
    }

    private String fitText(final Font font, final String text, final int maxWidth) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        if (font.width(text) <= maxWidth) {
            return text;
        }

        String result = text;
        while (!result.isEmpty() && font.width(result + ".") > maxWidth) {
            result = result.substring(0, result.length() - 1);
        }

        return result.isEmpty() ? "" : result + ".";
    }

    private boolean isMouseInside(
            final double mouseX,
            final double mouseY,
            final int x,
            final int y,
            final int width,
            final int height
    ) {
        return mouseX >= x
                && mouseY >= y
                && mouseX < x + width
                && mouseY < y + height;
    }

    private String getLocalizedDirectionName(final String directionName) {
        return tr("multiblock.jei.direction." + directionName);
    }

    private String tr(final String key) {
        return Localization.translate(key);
    }

    private String trf(final String key, final Object... args) {
        return String.format(Locale.ROOT, tr(key), args);
    }

    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guivein.png");
    }
}