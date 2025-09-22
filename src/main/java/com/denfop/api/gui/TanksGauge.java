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

public class TanksGauge extends GuiElement {



    private final List<Fluids.InternalFluidTank> tank;

    private TanksGauge(
            GuiCore<?> gui,
            int x,
            int y,
            int width,
            int height,
            List<Fluids.InternalFluidTank> tank
    ) {
        super(gui, x, y, width, height);
        if (tank == null) {
            throw new NullPointerException("null tank");
        } else {
            this.tank = tank;
        }
    }

    public static TanksGauge createNormal(GuiCore<?> gui, int x, int y, List<Fluids.InternalFluidTank> tank) {
        return new TanksGauge(gui, x, y, 20, 55, tank);
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
            this.gui.drawTexturedRect(
                    this.x,
                    this.y,
                    this.width,
                    this.height,
                    6.0D,
                    100.0D
            );

            int fluidX = this.x;
            int fluidY = this.y;
            int fluidWidth;
            int fluidHeight;
            fluidX += 4;
            fluidY += 4;
            fluidWidth = 12;
            fluidHeight = 47;

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
            bindCommonTexture();
            int gaugeX = this.x;
            int gaugeY = this.y;
            this.gui.drawTexturedRect(gaugeX, gaugeY, 20.0D, 55.0D, 38.0D, 100.0D);

        } else {
            this.gui.drawTexturedRect(
                    this.x,
                    this.y,
                    this.width,
                    this.height,
                    70.0D,
                    100.0D
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



}
