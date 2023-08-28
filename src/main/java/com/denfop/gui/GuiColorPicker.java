package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.network.packet.PacketColorPicker;
import com.denfop.render.streak.EventSpectralSuitEffect;
import com.denfop.render.streak.PlayerStreakInfo;
import com.denfop.render.streak.RGB;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.io.IOException;

public class GuiColorPicker extends GuiScreen implements GuiPageButtonList.GuiResponder, GuiSlider.FormatHelper {

    protected final EntityPlayer player;
    protected final int xSize = 176;

    protected final int ySize = 166;
    private final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/Color.png");
    private PlayerStreakInfo colorPicker;

    public GuiColorPicker(EntityPlayer player) {
        this.player = player;
        this.colorPicker = EventSpectralSuitEffect.mapStreakInfo.get(this.player.getName());
    }

    @Override
    public void initGui() {
        this.colorPicker = EventSpectralSuitEffect.mapStreakInfo.get(this.player.getName());
        if (this.colorPicker == null) {
            this.colorPicker = new PlayerStreakInfo(new RGB((short) 0, (short) 0, (short) 0), false);
            EventSpectralSuitEffect.mapStreakInfo.put(this.player.getName(), colorPicker);
            new PacketColorPicker(colorPicker, this.player.getName());

        }
        this.buttonList.add(new GuiSlider(this, 0, (this.width - this.xSize) / 2 + 10, (this.height - this.ySize) / 2 + 65,
                Localization.translate("iu.red"),
                0, 255, colorPicker.getRgb().getRed(), this
        ));

        this.buttonList.add(new GuiSlider(this, 1, (this.width - this.xSize) / 2 + 10, (this.height - this.ySize) / 2 + 95,
                Localization.translate("iu.green"),
                0, 255, colorPicker.getRgb().getGreen(), this
        ));
        this.buttonList.add(new GuiSlider(this, 2, (this.width - this.xSize) / 2 + 10, (this.height - this.ySize) / 2 + 125,
                Localization.translate("iu.blue"),
                0, 255, colorPicker.getRgb().getBlue(), this
        ));


        this.buttonList.add(new GuiCheckBox(3, (this.width - this.xSize) / 2 + 10, (this.height - this.ySize) / 2 + 155,
                Localization.translate("iu.rgb"), colorPicker.isRainbow()
        ));

    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }


    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {

        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        this.drawGuiContainerBackgroundLayer();
    }

    public void drawTexturedModalRect1(int x, int y, int textureX, int textureY, int width, int height) {
        double[] name = new double[3];
        for (int i = 0; i < 3; i++) {

            if (this.buttonList.get(i) instanceof GuiSlider) {
                GuiSlider slider = (GuiSlider) this.buttonList.get(i);
                name[i] = slider.getSliderValue();
            }
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        GL11.glColor4f((float) name[0] / 255, (float) name[1] / 255, (float) name[2] / 255, 1);

        bufferbuilder.pos(x, y + height, this.zLevel).tex(
                (float) (textureX) * 0.00390625F,
                (float) (textureY + height) * 0.00390625F
        ).endVertex();
        bufferbuilder.pos(x + width, y + height, this.zLevel).tex(
                (float) (textureX + width) * 0.00390625F,
                (float) (textureY + height) * 0.00390625F
        ).endVertex();
        bufferbuilder.pos(x + width, y, this.zLevel).tex(
                (float) (textureX + width) * 0.00390625F,
                (float) (textureY) * 0.00390625F
        ).endVertex();
        bufferbuilder.pos(x, y, this.zLevel).tex(
                (float) (textureX) * 0.00390625F,
                (float) (textureY) * 0.00390625F
        ).endVertex();
        tessellator.draw();


    }

    protected void drawGuiContainerBackgroundLayer() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(this.background);

        this.drawTexturedModalRect1(xOffset, yOffset, 15, 1, 180, 60);

    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (player != null) {
            if (guibutton instanceof GuiSlider) {
                GuiSlider slider = (GuiSlider) guibutton;
                switch (guibutton.id) {
                    case 0:
                        this.colorPicker.getRgb().setRed((short) slider.getSliderValue());
                        break;
                    case 1:
                        this.colorPicker.getRgb().setGreen((short) slider.getSliderValue());

                        break;
                    case 2:
                        this.colorPicker.getRgb().setBlue((short) slider.getSliderValue());
                        break;

                }
            }
            if (guibutton instanceof GuiCheckBox) {
                GuiCheckBox checkbox = (GuiCheckBox) guibutton;
                this.colorPicker.setRainbow(checkbox.isChecked());

            }
            new PacketColorPicker(colorPicker, this.player.getName());

        }

    }

    @Override
    public void setEntryValue(final int id, final boolean value) {


    }

    @Override
    public void setEntryValue(final int id, final float value) {
        switch (id) {
            case 0:
                this.colorPicker.getRgb().setRed((short) value);
                break;
            case 1:
                this.colorPicker.getRgb().setGreen((short) value);
                break;
            case 2:
                this.colorPicker.getRgb().setBlue((short) value);
                break;

        }
    }

    @Override
    public void setEntryValue(final int id, @Nonnull final String value) {

    }

    @Nonnull
    @Override
    public String getText(final int id, @Nonnull final String name, final float value) {
        switch (id) {
            case 0:
                return Localization.translate("iu.red") + (int) value;
            case 1:
                return Localization.translate("iu.green") + (int) value;
            case 2:
                return Localization.translate("iu.blue") + (int) value;

        }
        return "";
    }

}
