package com.denfop.api.gui;

import com.denfop.gui.GuiIC2;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import ic2.core.util.Util;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.LogicOp;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;

public class TextBox extends GuiElement<TextBox> {

    protected static final int enabledColour = 14737632;
    protected static final int disabledColour = 7368816;
    protected static final int invalidColour = -3092272;
    protected final boolean drawBackground;
    protected String text;
    protected boolean focused;
    protected int cursor;
    protected int cursorTick;
    protected int scrollOffset;
    protected int selectionEnd;
    protected int maxTextLength;
    protected IEnableHandler enableHandler;
    protected Predicate<String> validator;
    protected TextBox.ITextBoxWatcher watcher;

    public TextBox(GuiIC2<?> gui, int x, int y, int width, int height) {
        this(gui, x, y, width, height, "");
    }

    public TextBox(GuiIC2<?> gui, int x, int y, int width, int height, String text) {
        this(gui, x, y, width, height, text, true);
    }

    public TextBox(GuiIC2<?> gui, int x, int y, int width, int height, boolean drawBackground) {
        this(gui, x, y, width, height, "", drawBackground);
    }

    public TextBox(GuiIC2<?> gui, int x, int y, int width, int height, String text, boolean drawBackground) {
        super(gui, x, y, width, height);
        this.maxTextLength = 32;
        this.validator = Predicates.alwaysTrue();
        this.text = text;
        this.drawBackground = drawBackground;
        this.selectionEnd = this.cursor = text.length();
    }

    public TextBox withTextEnableHandler(IEnableHandler enableHandler) {
        this.enableHandler = enableHandler;
        return this;
    }

    public TextBox withTextValidator(Predicate<String> validator) {
        this.validator = validator;
        return this;
    }

    public TextBox withTextWatcher(TextBox.ITextBoxWatcher watcher) {
        this.watcher = watcher;
        return this;
    }

    public boolean willDraw() {
        return this.enableHandler == null || this.enableHandler.isEnabled();
    }

    public boolean isFocused() {
        return this.focused;
    }

    public void setFocused(boolean focused) {
        if (focused && !this.focused) {
            this.cursorTick = 0;
        }

        this.focused = focused;
    }

    public void setMaxTextLength(int length) {
        if (length >= 0) {
            this.maxTextLength = length;
        }

    }

    public String getText() {
        return this.text;
    }

    public boolean setText(String text) {
        if (this.setText(text, true)) {
            this.setCursorPositionEnd();
            return true;
        } else {
            return false;
        }
    }

    public boolean setText(String text, boolean forceLength) {
        assert text != null;

        if (!this.validator.apply(text) || text.length() > this.maxTextLength && !forceLength) {
            return false;
        } else {
            String old = this.text;
            this.text = text.length() <= this.maxTextLength ? text : text.substring(0, this.maxTextLength);
            if (this.watcher != null) {
                this.watcher.onChanged(old, text);
            }

            return true;
        }
    }

    public void tick() {
        super.tick();
        ++this.cursorTick;
    }

    public void drawBackground(int mouseX, int mouseY) {
        super.drawBackground(mouseX, mouseY);
        if (this.drawBackground) {
            this.gui.drawColoredRect(this.x - 1, this.y - 1, this.width + 2, this.height + 2, -6250336);
            this.gui.drawColoredRect(this.x, this.y, this.width, this.height, -16777216);
        }

    }

    public void drawForeground(int mouseX, int mouseY) {
        super.drawForeground(mouseX, mouseY);
        int colour = this.willDraw() ? 14737632 : 7368816;
        int textOffset = this.cursor - this.scrollOffset;
        int selectionOffset = this.selectionEnd - this.scrollOffset;
        String text = this.gui.trimStringToWidth(
                this.text.substring(this.scrollOffset),
                this.drawBackground ? this.width - 8 : this.width
        );
        boolean validOffset = textOffset >= 0 && textOffset <= text.length();
        int xStartPos = (this.drawBackground ? this.x + 4 : this.x) - this.gui.getGuiLeft();
        int yPos = (this.drawBackground ? this.y + (this.height - 8) / 2 : this.y) - this.gui.getGuiTop();
        int xPos = xStartPos;
        if (selectionOffset > text.length()) {
            selectionOffset = text.length();
        }

        if (!text.isEmpty()) {
            xPos = this.gui.drawString(xStartPos, yPos, validOffset ? text.substring(0, textOffset) : text, colour, true);
        }

        boolean inStringOrFull = this.cursor < this.text.length() || this.text.length() >= this.maxTextLength;
        int xCursorPos = xPos;
        if (!validOffset) {
            xCursorPos = textOffset > 0 ? xStartPos + this.width : xStartPos;
        } else if (inStringOrFull) {
            xCursorPos = xPos - 1;
            --xPos;
        }

        if (!text.isEmpty() && validOffset && textOffset < text.length()) {
            this.gui.drawString(xPos, yPos, text.substring(textOffset), colour, true);
        }

        if (this.focused && this.cursorTick / 6 % 2 == 0 && validOffset) {
            if (inStringOrFull) {
                this.gui.drawColoredRect(xCursorPos, yPos - 1, 1, 10, -3092272);
            } else {
                this.gui.drawString(xCursorPos, yPos, "_", colour, true);
            }
        }

        if (selectionOffset != textOffset) {
            int selectionEnd = xStartPos + this.gui.getStringWidth(text.substring(0, selectionOffset));
            this.drawHighlightedArea(xCursorPos, yPos - 1, selectionEnd - 1, yPos + 1 + 8);
        }

    }

    protected void drawHighlightedArea(int startX, int startY, int endX, int endY) {
        int temp;
        if (startX < endX) {
            temp = startX;
            startX = endX;
            endX = temp;
        }

        if (startY < endY) {
            temp = startY;
            startY = endY;
            endY = temp;
        }

        startX += this.gui.getGuiLeft();
        endX += this.gui.getGuiLeft();
        startY += this.gui.getGuiTop();
        endY += this.gui.getGuiTop();
        if (endX > this.x + this.width) {
            endX = this.x + this.width;
        }

        if (startX > this.x + this.width) {
            startX = this.x + this.width;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(LogicOp.OR_REVERSE);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(startX, endY, 0.0D).endVertex();
        vertexbuffer.pos(endX, endY, 0.0D).endVertex();
        vertexbuffer.pos(endX, startY, 0.0D).endVertex();
        vertexbuffer.pos(startX, startY, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    public boolean onMouseClick(int mouseX, int mouseY, MouseButton button, boolean onThis) {
        this.setFocused(onThis);
        if (this.focused && onThis && MouseButton.left == button) {
            int end = mouseX - this.x;
            if (this.drawBackground) {
                end -= 4;
            }

            String text = this.gui.trimStringToWidth(
                    this.text.substring(this.scrollOffset),
                    this.drawBackground ? this.width - 8 : this.width
            );
            this.setCursorPosition(this.gui.trimStringToWidth(text, end).length() + this.scrollOffset);
        }

        return onThis;
    }

    public boolean onKeyTyped(char typedChar, int keyCode) {
        if (!this.focused) {
            return super.onKeyTyped(typedChar, keyCode);
        } else {
            if (GuiScreen.isKeyComboCtrlA(keyCode)) {
                this.setCursorPositionEnd();
                this.setSelectionPos(0);
            } else if (GuiScreen.isKeyComboCtrlC(keyCode)) {
                GuiScreen.setClipboardString(this.getSelectedText());
            } else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
                if (this.willDraw()) {
                    this.writeText(GuiScreen.getClipboardString());
                }
            } else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
                GuiScreen.setClipboardString(this.getSelectedText());
                if (this.willDraw()) {
                    this.writeText("");
                }
            } else {
                switch (keyCode) {
                    case 14:
                        if (GuiScreen.isCtrlKeyDown()) {
                            if (this.willDraw()) {
                                this.deleteWords(-1);
                            }
                        } else if (this.willDraw()) {
                            this.deleteFromCursor(-1);
                        }
                        break;
                    case 199:
                        if (GuiScreen.isShiftKeyDown()) {
                            this.setSelectionPos(0);
                        } else {
                            this.setCursorPositionStart();
                        }
                        break;
                    case 203:
                        if (GuiScreen.isShiftKeyDown()) {
                            if (GuiScreen.isCtrlKeyDown()) {
                                this.setSelectionPos(this.getNthWordFromPos(-1, this.selectionEnd));
                            } else {
                                this.setSelectionPos(this.selectionEnd - 1);
                            }
                        } else if (GuiScreen.isCtrlKeyDown()) {
                            this.setCursorPosition(this.getNthWordFromCursor(-1));
                        } else {
                            this.moveCursorBy(-1);
                        }
                        break;
                    case 205:
                        if (GuiScreen.isShiftKeyDown()) {
                            if (GuiScreen.isCtrlKeyDown()) {
                                this.setSelectionPos(this.getNthWordFromPos(1, this.selectionEnd));
                            } else {
                                this.setSelectionPos(this.selectionEnd + 1);
                            }
                        } else if (GuiScreen.isCtrlKeyDown()) {
                            this.setCursorPosition(this.getNthWordFromCursor(1));
                        } else {
                            this.moveCursorBy(1);
                        }
                        break;
                    case 207:
                        if (GuiScreen.isShiftKeyDown()) {
                            this.setSelectionPos(this.text.length());
                        } else {
                            this.setCursorPositionEnd();
                        }
                        break;
                    case 211:
                        if (GuiScreen.isCtrlKeyDown()) {
                            if (this.willDraw()) {
                                this.deleteWords(1);
                            }
                        } else if (this.willDraw()) {
                            this.deleteFromCursor(1);
                        }
                        break;
                    default:
                        if (!ChatAllowedCharacters.isAllowedCharacter(typedChar) || !this.willDraw()) {
                            return super.onKeyTyped(typedChar, keyCode);
                        }

                        this.writeText(String.valueOf(typedChar));
                }
            }

            return true;
        }
    }

    public void writeText(String textToWrite) {
        StringBuilder newText = new StringBuilder();
        String cleanString = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
        int start = Math.min(this.cursor, this.selectionEnd);
        int end = Math.max(this.cursor, this.selectionEnd);
        int insertionPoint = this.maxTextLength - this.text.length() - (start - end);
        if (!this.text.isEmpty()) {
            newText.append(this.text, 0, start);
        }

        int extraLength;
        if (insertionPoint < cleanString.length()) {
            newText.append(cleanString, 0, insertionPoint);
            extraLength = insertionPoint;
        } else {
            newText.append(cleanString);
            extraLength = cleanString.length();
        }

        if (!this.text.isEmpty() && end < this.text.length()) {
            newText.append(this.text.substring(end));
        }

        if (this.setText(newText.toString(), true)) {
            this.moveCursorBy(start - this.selectionEnd + extraLength);
        }

    }

    public void deleteWords(int num) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursor) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursor);
            }
        }

    }

    public void deleteFromCursor(int num) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursor) {
                this.writeText("");
            } else {
                int start;
                int end;
                if (num < 0) {
                    start = this.cursor;
                    end = this.cursor + num;
                } else {
                    start = this.cursor + num;
                    end = this.cursor;
                }

                StringBuilder newText = new StringBuilder();
                if (end >= 0) {
                    newText.append(this.text, 0, end);
                }

                if (start < this.text.length()) {
                    newText.append(this.text.substring(start));
                }

                if (this.validator.apply(newText.toString())) {
                    String old = this.text;
                    this.text = newText.toString();
                    if (this.watcher != null) {
                        this.watcher.onChanged(old, this.text);
                    }

                    if (num < 0) {
                        this.moveCursorBy(num);
                    }
                }
            }
        }

    }

    protected int getNthWordFromCursor(int numWords) {
        return this.getNthWordFromPos(numWords, this.cursor);
    }

    protected int getNthWordFromPos(int numWords, int position) {
        return this.getNthWordFromPosWS(numWords, position, true);
    }

    protected int getNthWordFromPosWS(int numWords, int position, boolean skipWs) {
        boolean positive = numWords >= 0;
        int k = 0;

        for (int absN = Math.abs(numWords); k < absN; ++k) {
            if (positive) {
                int end = this.text.length();
                position = this.text.indexOf(32, position);
                if (position == -1) {
                    position = end;
                } else {
                    while (skipWs && position < end && this.text.charAt(position) == ' ') {
                        ++position;
                    }
                }
            } else {
                while (skipWs && position > 0 && this.text.charAt(position - 1) == ' ') {
                    --position;
                }

                while (position > 0 && this.text.charAt(position - 1) != ' ') {
                    --position;
                }
            }
        }

        return position;
    }

    public String getSelectedText() {
        return this.text.substring(Math.min(this.cursor, this.selectionEnd), Math.max(this.cursor, this.selectionEnd));
    }

    protected void setCursorPositionStart() {
        this.setCursorPosition(0);
    }

    protected void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    protected void moveCursorBy(int num) {
        this.setCursorPosition(this.selectionEnd + num);
    }

    protected void setCursorPosition(int position) {
        this.cursor = Util.limit(position, 0, this.text.length());
        this.setSelectionPos(this.cursor);
    }

    protected void setSelectionPos(int position) {
        int textLength = this.text.length();
        position = Util.limit(position, 0, textLength);
        this.selectionEnd = position;
        if (this.scrollOffset > textLength) {
            this.scrollOffset = textLength;
        }

        int width = this.drawBackground ? this.width - 8 : this.width;
        int maxPosition = this.gui.trimStringToWidth(this.text.substring(this.scrollOffset), width).length() + this.scrollOffset;
        if (position == this.scrollOffset) {
            this.scrollOffset -= this.gui.trimStringToWidthReverse(this.text, width).length();
        }

        if (position > maxPosition) {
            this.scrollOffset += position - maxPosition;
        } else if (position <= this.scrollOffset) {
            this.scrollOffset -= this.scrollOffset - position;
        }

        this.scrollOffset = Util.limit(this.scrollOffset, 0, textLength);
    }

    public interface ITextBoxWatcher {

        void onChanged(String var1, String var2);

    }

}
