package com.denfop.api.gui;

import com.denfop.Localization;
import com.denfop.blocks.FluidName;
import com.denfop.componets.Fluids;
import com.denfop.gui.GuiCore;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class TanksGauge extends GuiElement<TankGauge> {


    public static final int v = 100;
    private final List<Fluids.InternalFluidTank> tank;
    private final TanksGauge.TankGuiStyle style;

    private TanksGauge(
            GuiCore<?> gui,
            int x,
            int y,
            int width,
            int height,
            List<Fluids.InternalFluidTank> tank,
            TanksGauge.TankGuiStyle style
    ) {
        super(gui, x, y, width, height);
        if (tank == null) {
            throw new NullPointerException("null tank");
        } else {
            this.tank = tank;
            this.style = style;
        }
    }

    public static TanksGauge createNormal(GuiCore<?> gui, int x, int y, List<Fluids.InternalFluidTank> tank) {
        return new TanksGauge(gui, x, y, 20, 55, tank, TanksGauge.TankGuiStyle.Normal);
    }

    public static TanksGauge createPlain(
            GuiCore<?> gui,
            int x,
            int y,
            int width,
            int height,
            List<Fluids.InternalFluidTank> tank
    ) {
        return new TanksGauge(gui, x, y, width, height, tank, TanksGauge.TankGuiStyle.Plain);
    }

    public static TanksGauge createBorderless(
            GuiCore<?> gui,
            int x,
            int y,
            List<Fluids.InternalFluidTank> tank,
            boolean mirrored
    ) {
        return new TanksGauge(
                gui,
                x,
                y,
                12,
                47,
                tank,
                mirrored ? TanksGauge.TankGuiStyle.BorderlessMirrored : TanksGauge.TankGuiStyle.Borderless
        );
    }

    public void drawBackground(int mouseX, int mouseY) {
        bindCommonTexture();
        FluidStack fs = null;
        int amount = 0;
        int capacity = 0;
        for (Fluids.InternalFluidTank tank1 : this.tank) {
            if (fs == null && tank1.getFluid() != null) {
                fs = tank1.getFluid();
            }
            amount += tank1.getFluidAmount();
            capacity += tank1.getCapacity();
        }
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
            TextureAtlasSprite sprite = fluid != null ? getBlockTextureMap().getAtlasSprite(FluidName
                    .isFluid(fluid)
                    .getStill(fs)
                    .toString()) :
                    null;
            int color = fluid != null ? fluid.getColor(fs) : -1;
            double renderHeight = (double) fluidHeight * ModUtils.limit(
                    (double) amount / (double) capacity,
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
        FluidStack fs = null;
        int amount = 0;
        for (Fluids.InternalFluidTank tank1 : this.tank) {
            if (fs == null && tank1.getFluid() != null) {
                fs = tank1.getFluid();
            }
            amount += tank1.getFluidAmount();
        }
        if (fs != null && amount > 0) {
            Fluid fluid = fs.getFluid();
            if (fluid != null) {
                ret.add(fluid.getLocalizedName(fs) + ": " + amount + " " + Localization.translate("iu.generic.text.mb"));
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
