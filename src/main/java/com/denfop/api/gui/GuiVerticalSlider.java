package com.denfop.api.gui;

import com.denfop.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiVerticalSlider extends GuiButton {

    protected static final ResourceLocation TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/gui/slider.png");
    private final String name;
    private final float min;
    private final float max;
    public boolean isMouseDown;
    public GuiPageButtonList.GuiResponder responder;
    public FormatHelper formatHelper;
    private String displayString1;
    private float sliderPosition = 1.0F;


    public GuiVerticalSlider(
            GuiPageButtonList.GuiResponder p_i45541_1_, int p_i45541_2_, int p_i45541_3_, int p_i45541_4_,
            String p_i45541_5_, float p_i45541_6_, float p_i45541_7_, float p_i45541_8_, FormatHelper p_i45541_9_,
            int height
    ) {
        super(p_i45541_2_, p_i45541_3_, p_i45541_4_, 1, height, "");
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
                ? I18n.format(this.name, new Object[0]) + ": " + this.getSliderValue()
                : this.formatHelper.getText(this.id, I18n.format(this.name, new Object[0]), this.getSliderValue());
    }

    protected int getHoverState(boolean p_146114_1_) {
        return 0;
    }

    protected void mouseDragged(Minecraft p_146119_1_, int p_146119_2_, int p_146119_3_) {
        if (this.visible) {
            if (this.isMouseDown) {
                this.sliderPosition = (float) (p_146119_3_ - (this.y + 4)) / (float) (this.height - 8);
                if (this.sliderPosition < 0.0F) {
                    this.sliderPosition = 0.0F;
                }

                if (this.sliderPosition > 1.0F) {
                    this.sliderPosition = 1.0F;
                }

                this.displayString1 = this.getDisplayString();
                this.responder.setEntryValue(this.id, this.getSliderValue());
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURES);
            this.drawTexturedModalRect(this.x,
                    this.y - 1, 255, 0
                    , 1, height - 2
            );

            this.drawTexturedModalRect(this.x - 1,
                    this.y - 2, 3, 0
                    , 4, 1
            );
            this.drawTexturedModalRect(this.x - 1,
                    this.y + this.height - 4, 3, 0
                    , 4, 1
            );
            this.drawTexturedModalRect(this.x - 1,
                    this.y + this.height / 2 - 3, 3, 0
                    , 4, 1
            );

            for (int y = this.y + this.height / 4 - 2; y < this.y + this.height - 1 - 2; y += this.height / 4) {
                this.drawTexturedModalRect(x - 1,
                        y, 4, 3
                        , 3, 1
                );
            }


            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);


            this.drawTexturedModalRect(this.x - 2,
                    this.y + (int) (this.sliderPosition * (float) (this.height - 3)) - 2, 3, 7
                    , 5, 3
            );
            int j = 14737632;

            if (packedFGColour != 0) {
                j = packedFGColour;
            } else if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = 16777120;
            }
            this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, this.displayString1,
                    this.x + (this.width - 8) / 2 + 4 + Minecraft.getMinecraft().fontRenderer.getStringWidth(this.displayString1) - 5,
                    this.y + this.height / 2 - 7
                    , j
            );

        }
    }

    public boolean mousePressed(Minecraft p_146116_1_, int mouseX, int mouseY) {
        if (this.enabled && this.visible && mouseX >= this.x - 4 && mouseY >= this.y && mouseX < this.x + 4 && mouseY < this.y + this.height) {
            this.sliderPosition = (float) (mouseY - (this.y + 4)) / (float) (this.height - 8);
            if (this.sliderPosition < 0.0F) {
                this.sliderPosition = 0.0F;
            }

            if (this.sliderPosition > 1.0F) {
                this.sliderPosition = 1.0F;
            }

            this.displayString1 = this.getDisplayString();
            this.responder.setEntryValue(this.id, this.getSliderValue());
            this.isMouseDown = true;
            return true;
        } else {
            return false;
        }
    }

    public void mouseReleased(int p_146118_1_, int p_146118_2_) {
        this.isMouseDown = false;
    }

    @SideOnly(Side.CLIENT)
    public interface FormatHelper {

        String getText(int var1, String var2, float var3);

    }

}
