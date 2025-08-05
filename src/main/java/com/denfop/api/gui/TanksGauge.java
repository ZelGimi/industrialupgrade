package com.denfop.api.gui;

import com.denfop.Localization;
import com.denfop.componets.Fluids;
import com.denfop.gui.GuiCore;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

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

    public void drawBackground(GuiGraphics poseStack, int mouseX, int mouseY) {
        bindCommonTexture();
        FluidStack fs = null;
        int amount = 0;
        int capacity = 0;
        for (Fluids.InternalFluidTank tank1 : this.tank) {
            if (fs == null && !tank1.getFluid().isEmpty()) {
                fs = tank1.getFluid();
            }
            amount += tank1.getFluidAmount();
            capacity += tank1.getCapacity();
        }
        if (fs != null && !fs.isEmpty() && fs.getAmount() > 0) {
            if (this.style.withBorder) {
                this.gui.drawTexturedModalRect(poseStack,
                        mouseX + this.x,
                        mouseY + this.y,
                        70,
                        100,
                        this.width,
                        this.height
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
                fluidHeight = 48;
            }

            Fluid fluid = fs.getFluid();

            IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
            TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
            int color = extensions.getTintColor();
            double renderHeight = (double) fluidHeight * ModUtils.limit(
                    (double) amount / (double) capacity,
                    0.0D,
                    1.0D
            );
            bindBlockTexture();
            this.gui.drawSprite(poseStack,
                    mouseX + fluidX,
                    mouseY + (double) (fluidY + fluidHeight) - renderHeight,
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

                this.gui.drawTexturedModalRect(poseStack, mouseX + gaugeX, mouseY + gaugeY, 38, 100, 20, 55);

            }
        } else if (this.style.withBorder) {
            this.gui.drawTexturedModalRect(poseStack,
                    mouseX + this.x,
                    mouseY + this.y,
                    70,
                    100,
                    this.width,
                    this.height
            );
        } else if (this.style.withGauge) {
            this.gui.drawTexturedModalRect(poseStack,
                    mouseX + this.x,
                    mouseY + this.y,
                    74,
                    104,
                    this.width,
                    this.height
            );
        }

    }

    protected List<String> getToolTip() {
        List<String> ret = super.getToolTip();
        FluidStack fs = null;
        int amount = 0;
        for (Fluids.InternalFluidTank tank1 : this.tank) {
            if (fs == null && !tank1.getFluid().isEmpty()) {
                fs = tank1.getFluid();
            }
            amount += tank1.getFluidAmount();
        }
        if (fs != null && amount > 0) {
            Fluid fluid = fs.getFluid();
            if (fluid != null) {
                ret.add(Localization.translate(fs.getFluid().getFluidType().getDescriptionId()) + ": " + amount + " " + Localization.translate("iu.generic.text.mb"));
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
