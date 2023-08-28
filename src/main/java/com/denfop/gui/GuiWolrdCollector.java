package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerWorldCollector;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiWolrdCollector extends GuiCore<ContainerWorldCollector> {

    public final ContainerWorldCollector container;

    public GuiWolrdCollector(ContainerWorldCollector container1) {
        super(container1);
        this.container = container1;
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        String tooltip2 =
                ModUtils.getString(this.container.base.matter_energy) + "/" + ModUtils.getString(this.container.base.max_matter_energy) + " " +
                        "ME";
        new AdvArea(this, 58, 35, 69, 50)
                .withTooltip(tooltip2)
                .drawForeground(par1, par2);
        new AdvArea(this, 80, 35, 101, 49)
                .withTooltip(Localization.translate("gui.MolecularTransformer.progress") + ": " + (int) (Math.min(
                        this.container.base.guiProgress,
                        1D
                ) * 100) + "%")
                .drawForeground(par1, par2);
    }


    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (14.0F * this.container.base.matter_energy / this.container.base.max_matter_energy);
        int progress = (int) (24.0F * this.container.base.guiProgress);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 56 + 1, yoffset + 36 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 79, yoffset + 34, 176, 14, progress + 1, 16);
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }


    public ResourceLocation getTexture() {
        switch (this.container.base.enumTypeCollector) {
            case AQUA:
                return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiwaterassembler.png");
            case NETHER:
                return new ResourceLocation(Constants.TEXTURES, "textures/gui/guinetherassembler.png");
            case EARTH:
                return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiearthassembler.png");
            case END:
                return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiendassembler.png");
            case AER:
                return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiaerassembler.png");
            default:
                return new ResourceLocation(Constants.TEXTURES, "textures/gui/guicrystallize.png");

        }

    }

}
