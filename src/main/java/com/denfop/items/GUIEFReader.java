package com.denfop.items;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.gui.GuiCore;
import com.denfop.gui.IGuiUpdate;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GUIEFReader extends GuiCore<ContainerEFReader> implements IGuiUpdate {

    private static final ResourceLocation background = new ResourceLocation(
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

    public GUIEFReader(ContainerEFReader container, final ItemStack itemStack1) {
        super(container);

        this.name = itemStack1.getDisplayName();
        this.itemStack = itemStack1;
        this.ySize = 218;

    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 6, 0);
        this.fontRenderer.drawString(Localization.translate("itemToolMEter.mode") + " Sink",
                (160 + this.fontRenderer.getStringWidth(Localization.translate("itemToolMEter.mode") + " Sink")) / 2, 42,
                ModUtils.convertRGBcolorToInt(13, 229, 34)
        );
        this.fontRenderer.drawString(Localization.translate("itemToolMEter.mode") + " Source",
                (160 + this.fontRenderer.getStringWidth(Localization.translate("itemToolMEter.mode") + " Sink")) / 2, 99,
                ModUtils.convertRGBcolorToInt(13, 229, 34)
        );

        if (this.mode == 0) {
            this.fontRenderer.drawString(Localization.translate("itemToolMEter.mode.EnergyIn") + ":",
                    16, 42,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            this.fontRenderer.drawString(ModUtils.getString(this.energySink) + " EF/t",
                    16, 52,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            this.fontRenderer.drawString(Localization.translate("itemToolMEter.avg") + " " + ModUtils.getString(this.average / tick) +
                            " " +
                            "EF/t",
                    12, 62,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            this.fontRenderer.drawString(Localization.translate("itemToolMEter.max/min") + ": ",
                    16, 72,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            this.fontRenderer.drawString(ModUtils.getString(this.min) + "/" + ModUtils.getString(this.max),
                    16, 82,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
        }
        if (this.mode == 1) {

            this.fontRenderer.drawString(Localization.translate("itemToolMEter.mode.EnergyOut") + ":",
                    16, 42,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            this.fontRenderer.drawString(ModUtils.getString(this.energySource) + " EF/t",
                    16, 52,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            this.fontRenderer.drawString(Localization.translate("itemToolMEter.avg") + " " + ModUtils.getString(this.average / tick) +
                            " " +
                            "EF/t",
                    12, 62,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            this.fontRenderer.drawString(Localization.translate("itemToolMEter.max/min") + ": ",
                    16, 72,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            this.fontRenderer.drawString(ModUtils.getString(this.min) + "/" + ModUtils.getString(this.max),
                    16, 82,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
        }
        this.fontRenderer.drawString(Localization.translate("itemToolMEter.cycle", ModUtils.getString(this.tick / 20D)),
                17, 99,
                ModUtils.convertRGBcolorToInt(13, 229, 34)
        );
        this.fontRenderer.drawString(Localization.translate("itemToolMEter.mode.reset"),
                36, 113,
                ModUtils.convertRGBcolorToInt(13, 229, 34)
        );

    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        if (this.mode == 0) {
            this.drawTexturedModalRect(this.guiLeft + 127, this.guiTop + 61, 191, 6, 10, 19);

        } else {
            this.drawTexturedModalRect(this.guiLeft + 127, this.guiTop + 70, 191, 55, 10, 19);

        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = mouseX - xMin;
        int y = mouseY - yMin;
        if (x >= 112 && y >= 55 && x <= 151 && y <= 74) {
            this.mode = 0;
            this.average = 0;
            this.min = Double.MAX_VALUE;
            this.max = 0;
            this.tick = 0;
            this.col = 0;
        }
        if (x >= 112 && y >= 75 && x <= 151 && y <= 94) {
            this.mode = 1;
            this.average = 0;
            this.min = Double.MAX_VALUE;
            this.max = 0;
            this.tick = 0;
            this.col = 0;
        }
        if (x >= 25 && y >= 110 && x <= 84 && y <= 124) {
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

    @Override
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
