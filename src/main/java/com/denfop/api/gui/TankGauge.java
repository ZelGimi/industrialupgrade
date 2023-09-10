package com.denfop.api.gui;

import com.denfop.Localization;
import com.denfop.gui.GuiCore;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import java.util.List;

public class TankGauge extends GuiElement<TankGauge> {


    public static final int v = 100;
    private final IFluidTank tank;
    private final TankGauge.TankGuiStyle style;

    private TankGauge(GuiCore<?> gui, int x, int y, int width, int height, IFluidTank tank, TankGauge.TankGuiStyle style) {
        super(gui, x, y, width, height);
        if (tank == null) {
            throw new NullPointerException("null tank");
        } else {
            this.tank = tank;
            this.style = style;
        }
    }

    public static TankGauge createNormal(GuiCore<?> gui, int x, int y, IFluidTank tank) {
        return new TankGauge(gui, x, y, 20, 55, tank, TankGauge.TankGuiStyle.Normal);
    }

    public static TankGauge createPlain(GuiCore<?> gui, int x, int y, int width, int height, IFluidTank tank) {
        return new TankGauge(gui, x, y, width, height, tank, TankGauge.TankGuiStyle.Plain);
    }

    public static TankGauge createBorderless(GuiCore<?> gui, int x, int y, IFluidTank tank, boolean mirrored) {
        return new TankGauge(
                gui,
                x,
                y,
                12,
                47,
                tank,
                mirrored ? TankGauge.TankGuiStyle.BorderlessMirrored : TankGauge.TankGuiStyle.Borderless
        );
    }

    public void drawBackground(int mouseX, int mouseY) {
        bindCommonTexture();
        FluidStack fs = this.tank.getFluid();
        if (fs != null && fs.amount > 0) {
            if (this.style.withBorder) {
                this.gui.drawTexturedRect(
                        this.x,
                        this.y,
                        this.width,
                        this.height,
                        6.0D,
                        100.0D
                );
            }

            int fluidX = this.x;
            int fluidY = this.y;
            int fluidWidth = this.width;
            int fluidHeight = this.height;
            if (this.style.withBorder) {
                fluidX += 4;
                fluidY += 4;
                fluidWidth = 12;
                fluidHeight = 47;
            }

            Fluid fluid = fs.getFluid();
            TextureAtlasSprite sprite = fluid != null ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString()) : null;
            int color = fluid != null ? fluid.getColor(fs) : -1;
            double renderHeight = (double) fluidHeight * ModUtils.limit(
                    (double) fs.amount / (double) this.tank.getCapacity(),
                    0.0D,
                    1.0D
            );
            bindBlockTexture();
            this.gui.drawSprite(
                    fluidX,
                    (double) (fluidY + fluidHeight) - renderHeight,
                    fluidWidth,
                    renderHeight,
                    sprite,
                    color,
                    1.0D,
                    false,
                    true
            );
            if (this.style.withGauge) {
                bindCommonTexture();
                int gaugeX = this.x;
                int gaugeY = this.y;
                if (!this.style.withBorder) {
                    gaugeX -= 4;
                    gaugeY -= 4;
                }

                this.gui.drawTexturedRect(gaugeX, gaugeY, 20.0D, 55.0D, 38.0D, 100.0D, this.style.mirrorGauge);
            }
        } else if (this.style.withBorder) {
            this.gui.drawTexturedRect(
                    this.x,
                    this.y,
                    this.width,
                    this.height,
                    70.0D,
                    100.0D,
                    this.style.mirrorGauge
            );
        } else if (this.style.withGauge) {
            this.gui.drawTexturedRect(
                    this.x,
                    this.y,
                    this.width,
                    this.height,
                    74.0D,
                    104.0D,
                    this.style.mirrorGauge
            );
        }

    }

    protected List<String> getToolTip() {
        List<String> ret = super.getToolTip();
        FluidStack fs = this.tank.getFluid();
        if (fs != null && fs.amount > 0) {
            Fluid fluid = fs.getFluid();
            if (fluid != null) {
                ret.add(fluid.getLocalizedName(fs) + ": " + fs.amount + " " + Localization.translate("iu.generic.text.mb"));
            } else {
                ret.add("invalid fluid stack");
            }
        } else {
            ret.add(Localization.translate("iu.generic.text.empty"));
        }

        return ret;
    }

    private enum TankGuiStyle {
        Normal(true, true, false),
        Borderless(false, true, false),
        BorderlessMirrored(false, true, true),
        Plain(false, false, false);

        public final boolean withBorder;
        public final boolean withGauge;
        public final boolean mirrorGauge;

        TankGuiStyle(boolean withBorder, boolean withGauge, boolean mirrorGauge) {
            this.withBorder = withBorder;
            this.withGauge = withGauge;
            this.mirrorGauge = mirrorGauge;
        }
    }

}
