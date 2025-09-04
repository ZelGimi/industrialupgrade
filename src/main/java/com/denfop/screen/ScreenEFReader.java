package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.containermenu.ContainerMenuEFReader;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenEFReader<T extends ContainerMenuEFReader> extends ScreenMain<ContainerMenuEFReader> {

    private static final ResourceLocation background = ResourceLocation.tryBuild(
            Constants.TEXTURES,
            "textures/gui/guitooleumeter.png"
    );
    private final String name;
    private final ItemStack itemStack;
    private double min = Double.MAX_VALUE;
    private double max = 0;
    private double average = 0;
    private double col = 0;
    private double energySink = 0;
    private double energySource = 0;

    private long tick = 0;

    private byte mode = 0;

    public ScreenEFReader(ContainerMenuEFReader container, final ItemStack itemStack1) {
        super(container);
        this.componentList.clear();
        this.name = itemStack1.getDisplayName().getString();
        this.itemStack = itemStack1;
        this.imageHeight = 121;
        this.imageWidth = 188;

    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        draw(poseStack, this.name, (this.imageWidth - this.getStringWidth(this.name)) / 2, 6, 0);
        draw(poseStack, Localization.translate("itemToolMEter.mode") + " Sink",
                (169 + this.getStringWidth(Localization.translate("itemToolMEter.mode") + " Sink")) / 2, 18,
                ModUtils.convertRGBcolorToInt(13, 229, 34)
        );
        draw(poseStack, Localization.translate("itemToolMEter.mode") + " Source",
                (169 + this.getStringWidth(Localization.translate("itemToolMEter.mode") + " Sink")) / 2, 78,
                ModUtils.convertRGBcolorToInt(13, 229, 34)
        );

        if (this.mode == 0) {
            draw(poseStack, Localization.translate("itemToolMEter.mode.EnergyIn") + ":",
                    12, 18,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            draw(poseStack, ModUtils.getString(this.energySink) + " EF/t",
                    12, 28,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            draw(poseStack, Localization.translate("itemToolMEter.avg") + " " + ModUtils.getString(this.average / tick) +
                            " " +
                            "EF/t",
                    8, 38,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            draw(poseStack, Localization.translate("itemToolMEter.max/min") + ": ",
                    12, 48,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            draw(poseStack, ModUtils.getString(this.max) + "/" + ModUtils.getString(this.min),
                    12, 58,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
        }
        if (this.mode == 1) {

            draw(poseStack, Localization.translate("itemToolMEter.mode.EnergyOut") + ":",
                    12, 18,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            draw(poseStack, ModUtils.getString(this.energySource) + " EF/t",
                    12, 28,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            draw(poseStack, Localization.translate("itemToolMEter.avg") + " " + ModUtils.getString(this.average / tick) +
                            " " +
                            "EF/t",
                    8, 38,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            draw(poseStack, Localization.translate("itemToolMEter.max/min") + ": ",
                    12, 48,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            draw(poseStack, ModUtils.getString(this.max) + "/" + ModUtils.getString(this.min),
                    12, 58,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
        }
        draw(poseStack, Localization.translate("itemToolMEter.cycle", ModUtils.getString(this.tick / 40D)),
                12, 78,
                ModUtils.convertRGBcolorToInt(13, 229, 34)
        );
        draw(poseStack, Localization.translate("itemToolMEter.mode.reset"),
                38, 101,
                ModUtils.convertRGBcolorToInt(0, 0, 0)
        );

    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        if (this.mode == 0) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 130, this.guiTop + 34, 198, 35, 19, 17);

        } else {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 130, this.guiTop + 54, 198, 55, 19, 17);

        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = mouseX - xMin;
        int y = mouseY - yMin;
        if (x >= 130 && y >= 34 && x <= 130 + 19 && y <= 34 + 17) {
            this.mode = 0;
            this.average = 0;
            this.min = Double.MAX_VALUE;
            this.max = 0;
            this.tick = 0;
            this.col = 0;
        }
        if (x >= 130 && y >= 54 && x <= 130 + 19 && y <= 54 + 17) {
            this.mode = 1;
            this.average = 0;
            this.min = Double.MAX_VALUE;
            this.max = 0;
            this.tick = 0;
            this.col = 0;
        }
        if (x >= 18 && y >= 96 && x <= 82 && y <= 113) {
            this.average = 0;
            this.min = Double.MAX_VALUE;
            this.max = 0;
            this.tick = 0;
            this.col = 0;
        }
    }

    protected ResourceLocation getTexture() {
        return background;
    }


    public void readField(final String string, final CustomPacketBuffer packetBuffer) {
        packetBuffer.readByte();
        if (string.equals("energySink")) {
            energySink = packetBuffer.readDouble();
        }
        if (string.equals("energySource")) {
            energySource = packetBuffer.readDouble();
        }
        if (this.mode == 0) {
            if (this.min > this.energySink) {
                this.min = this.energySink;
            }
            if (this.max < this.energySink) {
                this.max = this.energySink;
            }
            this.average += this.energySink;
        }
        if (this.mode == 1) {
            if (this.min > this.energySource) {
                this.min = this.energySource;
            }
            if (this.max < this.energySource) {
                this.max = this.energySource;
            }
            this.average += this.energySource;
        }
        this.tick++;
    }

}
