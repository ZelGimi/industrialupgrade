package com.denfop.api.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.gui.GuiCore;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiSlider extends AbstractWidget {

    protected static final ResourceLocation TEXTURES = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/slider.png");
    private final String name;
    private final float min;
    private final float max;
    private final int id;
    public boolean isMouseDown;
    public GuiPageButtonList.GuiResponder responder;
    public FormatHelper formatHelper;
    private String displayString1;
    private float sliderPosition = 1.0F;

    public GuiSlider(
            GuiPageButtonList.GuiResponder p_i45541_1_,
            int p_i45541_2_,
            int p_i45541_3_,
            int p_i45541_4_,
            String p_i45541_5_,
            float p_i45541_6_,
            float p_i45541_7_,
            float p_i45541_8_,
            FormatHelper p_i45541_9_
    ) {
        super(p_i45541_3_, p_i45541_4_, 150, 1, Component.literal(""));
        this.id = p_i45541_2_;
        this.name = p_i45541_5_;
        this.min = p_i45541_6_;
        this.max = p_i45541_7_;
        this.sliderPosition = (p_i45541_8_ - p_i45541_6_) / (p_i45541_7_ - p_i45541_6_);
        this.formatHelper = p_i45541_9_;
        this.responder = p_i45541_1_;
        this.displayString1 = this.getDisplayString();
    }

    public GuiSlider(
            GuiPageButtonList.GuiResponder p_i45541_1_, int p_i45541_2_, int p_i45541_3_, int p_i45541_4_,
            String p_i45541_5_, float p_i45541_6_, float p_i45541_7_, float p_i45541_8_, FormatHelper p_i45541_9_,
            int width
    ) {
        super(p_i45541_3_, p_i45541_4_, width, 1, Component.literal(""));
        this.id = p_i45541_2_;
        this.name = p_i45541_5_;
        this.min = p_i45541_6_;
        this.max = p_i45541_7_;
        this.sliderPosition = (p_i45541_8_ - p_i45541_6_) / (p_i45541_7_ - p_i45541_6_);
        this.formatHelper = p_i45541_9_;
        this.responder = p_i45541_1_;
        this.displayString1 = this.getDisplayString();
    }

    public float getSliderValue() {
        return this.min + (this.max - this.min) * this.sliderPosition;
    }

    public void setSliderValue(float p_175218_1_, boolean p_175218_2_) {
        this.sliderPosition = (p_175218_1_ - this.min) / (this.max - this.min);
        this.displayString1 = this.getDisplayString();
        if (p_175218_2_) {
            this.responder.setEntryValue(this.id, this.getSliderValue());
        }

    }


    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, this.getDisplayString());
    }

    public float getSliderPosition() {
        return this.sliderPosition;
    }

    public void setSliderPosition(float p_175219_1_) {
        this.sliderPosition = p_175219_1_;
        this.displayString1 = this.getDisplayString();
        this.responder.setEntryValue(this.id, this.getSliderValue());
    }

    private String getDisplayString() {
        return this.formatHelper == null
                ? Localization.translate(this.name, new Object[0]) + ": " + this.getSliderValue()
                : this.formatHelper.getText(this.id, Localization.translate(this.name, new Object[0]), this.getSliderValue());
    }


    protected int getHoverState(boolean p_146114_1_) {
        return 0;
    }

    public boolean mouseDragged(double p_146119_2_, double p_146119_3_, int button, double dragX, double dragY) {
        if (this.visible) {
            if (this.isMouseDown && this.isMouseOver(this.getX(), p_146119_3_)) {
                updateSliderPosition(p_146119_2_);
                this.displayString1 = this.getDisplayString();
                this.responder.setEntryValue(this.id, this.getSliderValue());
                return true;
            }
            return false;
        }
        return false;

    }

    private void updateSliderPosition(double mouseX) {
        this.sliderPosition = (float) (mouseX - (this.getX() + 4)) / (float) (this.width - 8);
        if (this.sliderPosition < 0.0F) {
            this.sliderPosition = 0.0F;
        }

        if (this.sliderPosition > 1.0F) {
            this.sliderPosition = 1.0F;
        }
        this.responder.setEntryValue(this.id, (float) Math.round(this.getSliderValue()));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        GuiCore.bindTexture(TEXTURES);
        guiGraphics.blit(GuiCore.currentTexture, this.getX(),
                this.getY(), 0, 255
                , this.width, 1
        );
        guiGraphics.blit(GuiCore.currentTexture, this.getX(),
                this.getY() - 2, 6, 13
                , 1, 4
        );
        guiGraphics.blit(GuiCore.currentTexture, this.getX() + this.width - 1,
                this.getY() - 2, 6, 13
                , 1, 4
        );
        guiGraphics.blit(GuiCore.currentTexture, this.getX() + (this.width) / 2,
                this.getY() - 2, 6, 13
                , 1, 4
        );
        for (int x = this.getX() + this.width / 4; x < this.getX() + this.width - 1; x += this.width / 4) {
            guiGraphics.blit(GuiCore.currentTexture, x + 1,
                    this.getY() - 1, 9, 14
                    , 1, 3
            );
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);


        guiGraphics.blit(GuiCore.currentTexture, (this.getX() + (int) (this.sliderPosition * (float) (this.width - 3))),
                (int) ((this.getY() - 2)), 12, 13
                , 3, 5
        );
        int j = 14737632;

        if (packedFGColor != 0) {
            j = packedFGColor;
        } else if (!this.active) {
            j = 10526880;
        } else if (this.isHovered) {
            j = 16777120;
        }
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.getDisplayString(), this.getX() + this.width / 2, this.getY() + (1 - 8) / 2 - 8, j);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            this.isMouseDown = true;
            updateSliderPosition(mouseX);
            return true;
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isMouseDown = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.getX() - 4 && mouseX < this.getX() + 4 + width &&
                mouseY >= this.getY() - 4 && mouseY < this.getY() + 8 + height;
    }

    public boolean handleMouseWheel(ScrollDirection direction, int mouseX, int mouseY) {
        if (this.visible && this.active && isMouseOver(mouseX, mouseY)) {

            float step = 1.0F / (this.max - this.min);


            this.sliderPosition += direction == ScrollDirection.down ? step : -step;


            if (this.sliderPosition < 0.0F) {
                this.sliderPosition = 0.0F;
            }
            if (this.sliderPosition > 1.0F) {
                this.sliderPosition = 1.0F;
            }


            this.displayString1 = this.getDisplayString();
            this.responder.setEntryValue(this.id, (float) Math.round(this.getSliderValue()));

            return true;
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public interface FormatHelper {

        String getText(int var1, String var2, float var3);

    }

}
