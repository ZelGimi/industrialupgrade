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
public class GuiVerticalSliderList extends GuiButton {

    protected static final ResourceLocation TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/gui/slider.png");
    private final int def;
    private final String name;
    public boolean isMouseDown;
    public GuiPageButtonList.GuiResponder responder;
    public FormatHelper formatHelper;
    private int min;
    private int sizeRender;
    private String displayString1;
    private float sliderPosition = 1.0F;
    private int max;


    public GuiVerticalSliderList(
            GuiPageButtonList.GuiResponder p_i45541_1_, int p_i45541_2_, int p_i45541_3_, int p_i45541_4_,
            String p_i45541_5_, int p_i45541_6_, int p_i45541_7_, int p_i45541_8_, FormatHelper p_i45541_9_,
            int height
    ) {
        super(p_i45541_2_, p_i45541_3_, p_i45541_4_, 1, height, "");
        this.name = p_i45541_5_;
        this.min = p_i45541_6_;
        this.max = p_i45541_7_;
        this.sizeRender = (int) Math.floor(this.height * ((this.max - this.min) / ((this.max + 1) * 1D)));
        if (this.sizeRender < 1) {
            this.sizeRender = 1;
        }
        this.def = p_i45541_8_;
        this.sliderPosition = (p_i45541_8_ - p_i45541_6_) / ((p_i45541_7_ - p_i45541_6_) * 1F);
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
            this.responder.setEntryValue(this.id, (float) Math.round(this.getSliderValue()));
        }

    }

    public void setMin(final int min) {
        this.min = min;
        this.setMax(max);
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
                this.responder.setEntryValue(this.id, (float) Math.round(this.getSliderValue()));
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURES);
            this.drawTexturedModalRect(this.x,
                    this.y - 1, 255, 0
                    , 1, height - 2
            );


            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);


            this.drawTexturedModalRect(this.x - 1,
                    this.y - 1 + ((this.height) - this.sizeRender) * this.sliderPosition, 12, 13
                    , 3, this.sizeRender
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
            this.responder.setEntryValue(this.id, (float) Math.round(this.getSliderValue()));
            this.isMouseDown = true;
            return true;
        } else {
            return false;
        }
    }

    public void mouseReleased(int p_146118_1_, int p_146118_2_) {
        this.isMouseDown = false;
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.x - 4 && mouseX < this.x + 4 &&
                mouseY >= this.y && mouseY < this.y + this.height;
    }

    public boolean handleMouseWheel(ScrollDirection direction, int mouseX, int mouseY) {
        if (this.visible && this.enabled && isMouseOver(mouseX, mouseY)) {

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


    @SideOnly(Side.CLIENT)
    public interface FormatHelper {

        String getText(int var1, String var2, float var3);

    }

}
