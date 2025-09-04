package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.AdvancedTooltipWidget;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.containermenu.ContainerMenuWorldCollector;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;


public class ScreenWolrdCollector<T extends ContainerMenuWorldCollector> extends ScreenIndustrialUpgrade<ContainerMenuWorldCollector> {

    public final ContainerMenuWorldCollector container;

    public ScreenWolrdCollector(ContainerMenuWorldCollector container1) {
        super(container1);
        this.container = container1;
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        String tooltip2 =
                ModUtils.getString(this.container.base.matter_energy) + "/" + ModUtils.getString(this.container.base.max_matter_energy) + " " +
                        "ME";
        new AdvancedTooltipWidget(this, 29, 15, 38, 70)
                .withTooltip(tooltip2)
                .drawForeground(poseStack, par1, par2);
        new TooltipWidget(this, 66, 34, 34, 18)
                .withTooltip(Localization.translate("gui.MolecularTransformer.progress") + ": " + (int) (Math.min(
                        this.container.base.guiProgress,
                        1D
                ) * 100) + "%")
                .drawForeground(poseStack, par1, par2);
    }


    protected void renderBg(GuiGraphics poseStack, float f, int x, int y) {
        super.renderBg(poseStack, f, x, y);
        bindTexture(getTexture());
        int chargeLevel = (int) (51.0F * this.container.base.matter_energy / this.container.base.max_matter_energy);
        int progress = (int) (34.0F * this.container.base.guiProgress);
        int xoffset = guiLeft;
        int yoffset = guiTop;
        if (chargeLevel > 0) {
            drawTexturedModalRect(poseStack, xoffset + 31, yoffset + 17 + 51 - chargeLevel, 179, 2 + 51 - chargeLevel,
                    5, chargeLevel
            );
        }
        if (progress > 0) {
            drawTexturedModalRect(poseStack, xoffset + 66, yoffset + 34, 177, 60, progress, 18);
        }
        bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }


    public ResourceLocation getTexture() {
        switch (this.container.base.enumTypeCollector) {
            case AQUA:
                return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guiwaterassembler.png");
            case NETHER:
                return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guinetherassembler.png");
            case EARTH:
                return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guiearthassembler.png");
            case END:
                return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guiendassembler.png");
            case AER:
                return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guiaerassembler.png");
            default:
                return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guicrystallize.png");

        }

    }

}
