package com.denfop.items.armour.special;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.widget.ButtonListSliderWidget;
import com.denfop.api.widget.SliderWidget;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.network.packet.PacketColorPicker;
import com.denfop.render.streak.PlayerStreakInfo;
import com.denfop.render.streak.RGB;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_TEX_COLOR;
import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;

@OnlyIn(Dist.CLIENT)
public class WidgetColorPicker<T extends ContainerMenuBase<?>> extends ScreenMain<ContainerMenuStreak> implements ButtonListSliderWidget.WidgetResponder, SliderWidget.FormatHelper {

    private final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/Color.png".toLowerCase());
    boolean isRgb = false;
    private PlayerStreakInfo colorPicker;
    private Checkbox rgb;

    public WidgetColorPicker(ContainerMenuStreak container, final ItemStack itemStack1) {
        super(container);
        this.componentList.clear();
        this.title = Component.literal("");
    }

    @Override
    protected void init() {
        super.init();
        this.colorPicker = IUCore.mapStreakInfo.get(this.container.player.getName().getString());
        if (this.colorPicker == null) {
            this.colorPicker = new PlayerStreakInfo(new RGB((short) 0, (short) 0, (short) 0), false);
            IUCore.mapStreakInfo.put(this.container.player.getName().getString(), colorPicker);
            new PacketColorPicker(colorPicker, this.container.player.getName().getString());

        }
        this.renderables.add(new SliderWidget(this, 0, (this.width - this.imageWidth) / 2 + 10, (this.height - this.imageHeight) / 2 + 80,
                Localization.translate("iu.red"),
                0, 255, colorPicker.getRgb().getRed(), this
        ));

        this.renderables.add(new SliderWidget(this, 1, (this.width - this.imageWidth) / 2 + 10, (this.height - this.imageHeight) / 2 + 110,
                Localization.translate("iu.green"),
                0, 255, colorPicker.getRgb().getGreen(), this
        ));
        this.renderables.add(new SliderWidget(this, 2, (this.width - this.imageWidth) / 2 + 10, (this.height - this.imageHeight) / 2 + 140,
                Localization.translate("iu.blue"),
                0, 255, colorPicker.getRgb().getBlue(), this
        ));
        rgb = new Checkbox(
                (this.width - this.imageWidth) / 2 + 10,
                (this.height - this.imageHeight) / 2 + 155, 20, 20,
                Component.translatable("iu.rgb"),
                colorPicker.isRainbow()
        );
        this.isRgb = colorPicker.isRainbow();
        this.renderables.add(rgb);
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
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
        new PacketColorPicker(colorPicker, this.container.player.getName().getString());
    }

    @Override
    public void setEntryValue(final int id, @Nonnull final String value) {

    }

    public void drawTexturedModalRect1(PoseStack poseStack, int x, int y, int textureX, int textureY, int width, int height) {
        double[] name = new double[3];
        for (int i = 0; i < 3; i++) {

            if (this.renderables.get(i) instanceof SliderWidget) {
                SliderWidget slider = (SliderWidget) this.renderables.get(i);
                name[i] = slider.getSliderValue();

            }
        }
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        poseStack.pushPose();
        Matrix4f matrix = poseStack.last().pose();
        bufferbuilder.begin(QUADS, POSITION_TEX_COLOR);

        bufferbuilder.vertex(matrix, x, y + height, 0).uv(
                (float) (textureX) * 0.00390625F,
                (float) (textureY + height) * 0.00390625F
        ).color((float) name[0] / 255, (float) name[1] / 255, (float) name[2] / 255, 1).endVertex();
        bufferbuilder.vertex(matrix, x + width, y + height, 0).uv(
                (float) (textureX + width) * 0.00390625F,
                (float) (textureY + height) * 0.00390625F
        ).color((float) name[0] / 255, (float) name[1] / 255, (float) name[2] / 255, 1).endVertex();
        bufferbuilder.vertex(matrix, x + width, y, 0).uv(
                (float) (textureX + width) * 0.00390625F,
                (float) (textureY) * 0.00390625F
        ).color((float) name[0] / 255, (float) name[1] / 255, (float) name[2] / 255, 1).endVertex();
        bufferbuilder.vertex(matrix, x, y, 0).uv(
                (float) (textureX) * 0.00390625F,
                (float) (textureY) * 0.00390625F
        ).color((float) name[0] / 255, (float) name[1] / 255, (float) name[2] / 255, 1).endVertex();
        tessellator.end();
        poseStack.popPose();

    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();

    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int xOffset = guiLeft;
        int yOffset = guiTop;
        bindTexture(this.background);

        this.drawTexturedModalRect1(poseStack.pose(), xOffset, yOffset, 15, 1, 180, 60);
        if (isRgb != rgb.selected()) {
            isRgb = rgb.selected();
            colorPicker.setRainbow(isRgb);
            new PacketColorPicker(colorPicker, this.container.player.getName().getString());
        }

    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;

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

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();

    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
