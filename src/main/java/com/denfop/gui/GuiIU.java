package com.denfop.gui;

import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerBase;
import com.denfop.invslot.InvSlot;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class GuiIU<T extends ContainerBase<? extends IInventory>> extends GuiCore<T> {

    private final EnumTypeStyle style;
    public boolean isBlack = false;
    protected GuiComponent inventory;
    protected GuiComponent slots;
    protected List<InvSlot> invSlotList = new ArrayList<>();
    protected List<GuiComponent> componentList = new ArrayList<>();

    public GuiIU(final T container) {
        super(container);
        this.style = EnumTypeStyle.DEFAULT;
        inventory = new GuiComponent(this, 7, 83, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );

        componentList.add(inventory);
        componentList.add(slots);
    }

    public GuiIU(final T container, EnumTypeStyle style) {
        super(container);
        this.style = style;
        inventory = new GuiComponent(this, 7, 83, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );

        componentList.add(inventory);
        componentList.add(slots);
    }

    public GuiIU(final T container, EnumTypeComponent style) {
        super(container);
        this.style = getStyle(style);
        inventory = new GuiComponent(this, 7, 83, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );

        componentList.add(inventory);
        componentList.add(slots);
    }

    public float adjustTextScale(String text, int canvasWidth, int canvasHeight, float scale, float scaleStep) {
        FontRenderer fontRenderer = mc.fontRenderer;
        float newScale = scale;
        float min = 70;
        float max = 0;
        boolean prevScaleDecrease = false;
        boolean prevScaleIncrease = false;
        while (true) {


            if (newScale < min) {
                min = newScale;
            }
            if (newScale > max) {
                max = newScale;
            }
            List<String> lines = splitTextToLines(text, canvasWidth, newScale, fontRenderer);

            int totalTextHeight = (int) (lines.size() * fontRenderer.FONT_HEIGHT * newScale);

            if (isTextTooLarge(lines, canvasWidth, canvasHeight, newScale, fontRenderer)) {

                newScale *= 1 - scaleStep;
                prevScaleDecrease = true;
                if (prevScaleIncrease) {
                    newScale = (min + max) * 0.95f / 2;
                    break;
                }
            } else if (totalTextHeight < canvasHeight * 0.8F) {
                prevScaleIncrease = true;
                newScale *= (1 + scaleStep);
                if (prevScaleDecrease) {
                    newScale = (min + max) * 1.2f / 2;
                    break;
                }
            } else {

                break;
            }
        }
        return newScale;
    }

    public boolean isTextTooLarge(
            List<String> lines,
            int canvasWidth,
            int canvasHeight,
            float scale,
            FontRenderer fontRenderer
    ) {
        int totalHeight = (int) (lines.size() * fontRenderer.FONT_HEIGHT * scale);

        for (String line : lines) {
            int lineWidth = (int) (fontRenderer.getStringWidth(line) * scale);
            if (lineWidth > canvasWidth) {
                return true;
            }
        }
        return totalHeight > canvasHeight;
    }

    public List<String> wrapTextWithNewlines(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] paragraphs = text.split("\n");

        for (String paragraph : paragraphs) {
            List<String> wrappedLines = wrapText(paragraph, maxWidth);
            lines.addAll(wrappedLines);
        }

        return lines;
    }

    public List<String> wrapText(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        String[] words = text.split(" ");
        for (String word : words) {
            if (fontRenderer.getStringWidth(currentLine + word) <= maxWidth) {
                currentLine.append(word).append(" ");
            } else {
                lines.add(currentLine.toString().trim());
                while (fontRenderer.getStringWidth(word) > maxWidth) {
                    int partLength = maxWidth / fontRenderer.getCharWidth(' ');
                    String part = word.substring(0, partLength);
                    lines.add(part);
                    word = word.substring(part.length());
                }

                currentLine.setLength(0);
                currentLine.append(word).append(" ");
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString().trim());
        }

        return lines;
    }
    private void enableScissor(int x, int y, int width, int height) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaleFactor = scaledResolution.getScaleFactor();
        int scaledHeight = scaledResolution.getScaledHeight();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);


        GL11.glScissor(
                x * scaleFactor,
                (scaledHeight - (y + height)) * scaleFactor,
                width * scaleFactor,
                height * scaleFactor
        );
    }

    private void disableScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
    public void drawTextInCanvasWithScissor(String text, int canvasX, int canvasY, int canvasWidth, int canvasHeight, float scale) {
        int maxWidth = (int) (canvasWidth / scale);
        int x = guiLeft+ canvasX;
        int y = guiTop + canvasY;


        List<String> lines = wrapTextWithNewlines(text, maxWidth);


        for (String line : lines) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            fontRenderer.drawString(line, (int) (x / scale), (int) (y / scale), 0xFFFFFF);
            GlStateManager.popMatrix();

            y += 10;
        }

    }
    public void drawTextInCanvas(String text, int canvasX, int canvasY, int canvasWidth, int canvasHeight, float scale) {
        int maxWidth = (int) (canvasWidth / scale);
        int lineHeight = (int) (10 * scale);
        int x = canvasX;
        int y = canvasY;


        List<String> lines = wrapTextWithNewlines(text, maxWidth);


        for (String line : lines) {
            if (y + lineHeight > canvasY + canvasHeight) {
                break;
            }
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            fontRenderer.drawString(line, (int) (x / scale), (int) (y / scale), 0xFFFFFF);
            GlStateManager.popMatrix();

            y += lineHeight;
        }
    }

    public void drawTextInCanvas(
            String text, int canvasX, int canvasY, int canvasWidth, int canvasHeight, float scale,
            int color
    ) {
        int maxWidth = (int) (canvasWidth / scale);
        int lineHeight = (int) (10 * scale);
        int x = canvasX;
        int y = canvasY;


        List<String> lines = wrapTextWithNewlines(text, maxWidth);


        for (String line : lines) {
            if (y + lineHeight > canvasY + canvasHeight) {
                break;
            }
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            fontRenderer.drawString(line, (int) (x / scale), (int) (y / scale), color);
            GlStateManager.popMatrix();

            y += lineHeight;
        }
    }

    public List<String> splitTextToLines(String text, int canvasWidth, float scale, FontRenderer fontRenderer) {
        List<String> lines = new LinkedList<>();
        String[] manualLines = text.split("\n");

        for (String manualLine : manualLines) {
            StringBuilder currentLine = new StringBuilder();
            String[] words = manualLine.split(" ");

            for (String word : words) {
                String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
                int lineWidth = (int) (fontRenderer.getStringWidth(testLine) * scale);

                if (lineWidth > canvasWidth) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    currentLine.append(currentLine.length() == 0 ? word : " " + word);
                }
            }

            if (currentLine.length() > 0) {
                lines.add(currentLine.toString());
            }
        }
        return lines;
    }

    public EnumTypeComponent getComponent() {
        switch (this.style) {
            case ADVANCED:
                return EnumTypeComponent.ADVANCED;
            case IMPROVED:
                return EnumTypeComponent.IMPROVED;
            case PERFECT:
                return EnumTypeComponent.PERFECT;
            case PHOTONIC:
                return EnumTypeComponent.PHOTONIC;
            case STEAM:
                return EnumTypeComponent.STEAM_DEFAULT;
            case BIO:
                return EnumTypeComponent.BIO_DEFAULT;
            case SPACE:
                return EnumTypeComponent.SPACE_DEFAULT;
            default:
                return EnumTypeComponent.DEFAULT;
        }
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public EnumTypeStyle getStyle(EnumTypeComponent style) {
        switch (style) {
            case ADVANCED:
                return EnumTypeStyle.ADVANCED;
            case IMPROVED:
                return EnumTypeStyle.IMPROVED;
            case PERFECT:
                return EnumTypeStyle.PERFECT;
            case PHOTONIC:
                return EnumTypeStyle.PHOTONIC;
            case STEAM_DEFAULT:
                return EnumTypeStyle.STEAM;
            case BIO_DEFAULT:
                return EnumTypeStyle.BIO;
            case SPACE_DEFAULT:
                return EnumTypeStyle.SPACE;
            default:
                return EnumTypeStyle.DEFAULT;
        }
    }

    public EnumTypeStyle getStyle() {
        return this.style;
    }

    public void addComponent(GuiComponent component) {
        componentList.add(component);
    }

    public void removeComponent(int index) {
        componentList.remove(index);
    }

    public void removeComponent(GuiComponent component) {
        componentList.remove(component);
    }

    public void drawForeground(int mouseX, int mouseY) {
        componentList.forEach(guiComponent -> guiComponent.drawForeground(mouseX, mouseY));
    }

    public void drawBackground() {
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        componentList.forEach(guiComponent -> guiComponent.drawBackground(xoffset, yoffset));

    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        componentList.forEach(guiComponent -> guiComponent.buttonClicked(x, y));

    }

    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.drawBackground();
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        int textWidth = this.fontRenderer.getStringWidth(name);
        float scale = 1.0f;


        if (textWidth > 120) {
            scale = 120f / textWidth;
        }


        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0f);


        int centerX = this.guiLeft + this.xSize / 2;
        int textX = (int) ((centerX / scale) - (textWidth / 2.0f));
        int textY = (int) ((this.guiTop + 6) / scale);


        this.fontRenderer.drawString(name, textX, textY, 4210752);


        GlStateManager.popMatrix();

    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.drawForeground(par1, par2);
    }

}
