package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerBaseNuclearReactor;
import ic2.core.GuiIC2;
import ic2.core.gui.Gauge;
import ic2.core.gui.LinkedGauge;
import ic2.core.gui.Text;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.gui.dynamic.TextProvider;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GUINuclearReactor extends GuiIC2<ContainerBaseNuclearReactor> {

    public final ContainerBaseNuclearReactor container;
    public final String name;
    private final ResourceLocation background;

    public GUINuclearReactor(ContainerBaseNuclearReactor container1) {
        super(container1);

        this.background = new ResourceLocation(Constants.TEXTURES, container1.base.background);

        this.container = container1;
        this.name = Localization.translate("iu.blockAdvRea.name");
        this.ySize = 243;
        this.xSize = 212;
    }

    public void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

        new AdvArea(this, 5, 160, 22, 177)
                .withTooltip(Localization.translate("ic2.NuclearReactor.gui.mode.electric"))
                .drawForeground(par1, par2);
        (new LinkedGauge(this, 7, 136, container.base, "heat", Gauge.GaugeStyle.HeatNuclearReactor)).withTooltip(
                () -> Localization.translate(
                        "ic2.NuclearReactor.gui.info.temp",
                        ((IGuiValueProvider) (GUINuclearReactor.this.container).base).getGuiValue("heat") * 100.0D
                )).drawForeground(par1, par2);
        Text.create(this, 107, 136, 200, 13, TextProvider.of(() -> Localization.translate(
                "ic2.NuclearReactor.gui.info.EUoutput",
                Math.round(((GUINuclearReactor.this.container).base).getOfferedEnergy())
        )), 5752026, false, 4, 0, false, true).drawForeground(par1, par2)
        ;

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
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);
        int size = this.container.base.getReactorSize();
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
                        ((IGuiValueProvider) (GUINuclearReactor.this.container).base).getGuiValue("heat") * 100.0D
                )).drawBackground(xOffset, yOffset);

        Text.create(this, 107, 136, 200, 13, TextProvider.of(() -> Localization.translate(
                "ic2.NuclearReactor.gui.info.EUoutput",
                Math.round(((GUINuclearReactor.this.container).base).getOfferedEnergy())
        )), 5752026, false, 4, 0, false, true).drawBackground(xOffset, yOffset)
        ;
    }

}
