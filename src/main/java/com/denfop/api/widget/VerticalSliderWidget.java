package com.denfop.api.widget;

import com.denfop.Constants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class VerticalSliderWidget extends AbstractWidget {

    private static final ResourceLocation TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/gui/slider.png");
    private final String name;
    private final int def;
    private final FormatHelper formatHelper;
    private final ButtonListSliderWidget.WidgetResponder responder;
    private final int id;
    private String displayString1;
    private int min;
    private int max;
    private int sizeRender;
    private float sliderPosition = 1.0F;
    private boolean isMouseDown;

    public VerticalSliderWidget(ButtonListSliderWidget.WidgetResponder p_i45541_1_, int p_i45541_2_, int x, int y, String name, int min, int max, int def, FormatHelper p_i45541_9_, int height) {
        super(x, y, 1, height, Component.literal(""));
        this.name = name;
        this.min = min;
        this.id = p_i45541_2_;
        this.max = max;
        this.def = def;
        this.sliderPosition = (float) (def - min) / (max - min);
        updateRenderSize();
        this.formatHelper = p_i45541_9_;
        this.responder = p_i45541_1_;
        this.displayString1 = this.getDisplayString();
    }

    private String getDisplayString() {
        return this.formatHelper == null
                ? I18n.get(this.name) + ": " + this.getSliderValue()
                : this.formatHelper.getText(this.id, I18n.get(this.name, new Object[0]), this.getSliderValue());
    }

    private void updateRenderSize() {
        this.sizeRender = Math.max(1, (int) Math.floor(this.height * ((this.max - this.min) / ((double) this.max + 1))));
    }

    public float getSliderValue() {
        return this.min + (this.max - this.min) * this.sliderPosition;
    }

    public void setSliderValue(float value) {
        this.sliderPosition = (value - this.min) / (this.max - this.min);
        if (this.sliderPosition < 0.0F) this.sliderPosition = 0.0F;
        if (this.sliderPosition > 1.0F) this.sliderPosition = 1.0F;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        if (!visible)
            return;
        RenderSystem.setShaderTexture(0, TEXTURES);

        blit(stack, this.x,
                this.y - 1, 255, 0
                , 1, height - 2
        );

        blit(stack, this.x - 1,
                this.y - 2, 3, 0
                , 4, 1
        );
        blit(stack, this.x - 1,
                this.y + this.height - 4, 3, 0
                , 4, 1
        );
        blit(stack, this.x - 1,
                this.y + this.height / 2 - 3, 3, 0
                , 4, 1
        );

        for (int y = this.y + this.height / 4 - 2; y < this.y + this.height - 1 - 2; y += this.height / 4) {
            blit(stack, x - 1,
                    y, 4, 3
                    , 3, 1
            );
        }


        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);


        blit(stack, this.x - 2,
                this.y + (int) (this.sliderPosition * (float) (this.height - 3)) - 2, 3, 7
                , 5, 3
        );
        int j = 14737632;

        if (packedFGColor != 0) {
            j = packedFGColor;
        } else if (!this.active) {
            j = 10526880;
        } else if (this.isHovered) {
            j = 16777120;
        }
        drawCenteredString(stack, Minecraft.getInstance().font, this.getDisplayString(), this.x + (this.width - 8) / 2 + 4 + Minecraft.getInstance().font.width(this.displayString1) - 5, this.y + this.height / 2 - 7, j);
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            this.isMouseDown = true;
            updateSliderPosition(mouseY);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isMouseDown = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.isMouseDown) {
            updateSliderPosition(mouseY);
            return true;
        }
        return false;
    }

    private void updateSliderPosition(double mouseY) {
        this.sliderPosition = (float) (mouseY - (this.y + 4)) / (float) (this.height - 8);
        if (this.sliderPosition < 0.0F) this.sliderPosition = 0.0F;
        if (this.sliderPosition > 1.0F) this.sliderPosition = 1.0F;
        this.responder.setEntryValue(this.id, (float) Math.round(this.getSliderValue()));
    }


    @Override
    public void updateNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, this.getDisplayString());
    }

    public void setMin(final int min) {
        this.min = min;
        this.setMax(max);
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.x - 4 && mouseX < this.x + 4 &&
                mouseY >= this.y && mouseY < this.y + this.height;
    }

    public boolean handleMouseWheel(ScrollDirection direction, int mouseX, int mouseY) {
        if (this.visible && isMouseOver(mouseX, mouseY)) {

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

    public void setMax(int p_i45541_7_) {
        if (this.max != p_i45541_7_) {
            this.max = p_i45541_7_;
            if (max == 0) {
                this.sizeRender = this.height;
            } else {
                this.sizeRender = (int) ((this.height - 8) / ((this.max - this.min) * 1D));


            }
            if (this.sizeRender < 1) {
                this.sizeRender = 1;
            }
            this.sliderPosition = (float) (def * ((this.height - 8) / ((this.max - this.min) * 1D)));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public interface FormatHelper {

        String getText(int var1, String var2, float var3);

    }
}
