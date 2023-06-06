package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.gui.Gauge;
import com.denfop.api.gui.IGuiValueProvider;
import com.denfop.api.gui.LinkedGauge;
import com.denfop.container.ContainerBaseNuclearReactor;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiNuclearReactor extends GuiIU<ContainerBaseNuclearReactor> {

    public final ContainerBaseNuclearReactor container;
    private final ResourceLocation background;

    public GuiNuclearReactor(ContainerBaseNuclearReactor container1) {
        super(container1, container1.base.getStyle());

        this.background = new ResourceLocation(Constants.TEXTURES, container1.base.background);

        this.container = container1;
        this.ySize = 243;
        this.xSize = 256;
        this.inventory.setX(25);
        this.inventory.setY(160);
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 216 && x <= 229 && y >= 39 && y <= 51) {
            IUCore.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);
        }
    }

    public void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

        new AdvArea(this, 5, 160, 22, 177)
                .withTooltip(Localization.translate("ic2.NuclearReactor.gui.mode.electric"))
                .drawForeground(par1, par2);

        (new LinkedGauge(this, 7, 136, container.base, "heat", Gauge.GaugeStyle.HeatNuclearReactor)).withTooltip(
                () -> Localization.translate(
                        "ic2.NuclearReactor.gui.info.temp",
                        ((IGuiValueProvider) (GuiNuclearReactor.this.container).base).getGuiValue("heat") * 100.0D
                )).drawForeground(par1, par2);
        this.fontRenderer.drawString(Localization.translate(
                        "ic2.NuclearReactor.gui.info.EUoutput",
                        Math.round(this.container.base.output * 5 * this.container.base.getCoef())
                ),
                111, 139, 5752026
        );
        new AdvArea(this, 216, 39, 229, 51).withTooltip(this.container.base.work ? Localization.translate("turn_off") :
                Localization.translate("turn_on")).drawForeground(par1, par2);
    }

    @Override
    protected ResourceLocation getTexture() {
        return this.background;
    }

    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.background);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, 212, this.ySize);
        int size = this.container.base.getReactorSize();
        this.drawBackground();
        this.mc.getTextureManager().bindTexture(this.background);
        int startX = xOffset + 26 - 18;
        int startY = yOffset + 25;

        if (this.container.base.sizeY == 7) {
            startY -= 18;
        }
        int i2;
        for (i2 = 0; i2 < this.container.base.sizeY; ++i2) {
            for (int x = size; x < this.container.base.sizeX; ++x) {
                this.drawTexturedModalRect(startX + x * 18, startY + i2 * 18, 213, 1, 16, 16);
            }
        }
        (new LinkedGauge(this, 7, 136, container.base, "heat", Gauge.GaugeStyle.HeatNuclearReactor)).withTooltip(
                () -> Localization.translate(
                        "ic2.NuclearReactor.gui.info.temp",
                        ((IGuiValueProvider) (GuiNuclearReactor.this.container).base).getGuiValue("heat") * 100.0D
                )).drawBackground(xOffset, yOffset);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.background);
        this.drawTexturedModalRect(xOffset + 209, yOffset + 27, 209, 27, 40, 61 - 27);
        if (this.container.base.work) {
            this.drawTexturedModalRect(xOffset + 215, yOffset + 38, 224, 70, 239 - 224, 84 - 70);

        }

    }

}
