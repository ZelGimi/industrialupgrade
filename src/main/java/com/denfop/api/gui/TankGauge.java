package com.denfop.api.gui;

import com.denfop.Localization;
import com.denfop.blocks.FluidName;
import com.denfop.componets.Fluids;
import com.denfop.gui.GuiCore;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class TankGauge extends GuiElement {


    protected final IFluidTank tank;

    public TankGauge(GuiCore<?> gui, int x, int y, int width, int height, IFluidTank tank) {
        super(gui, x, y, width, height);
        if (tank == null) {
            throw new NullPointerException("null tank");
        } else {
            this.tank = tank;
        }
    }

    public static TankGauge createNormal(GuiCore<?> gui, int x, int y, IFluidTank tank) {
        return new TankGauge(gui, x, y, 20, 55, tank);
    }


    public void drawBackground(int mouseX, int mouseY) {
        bindCommonTexture();
        FluidStack fs = this.tank.getFluid();
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
                    .toString()) : null;
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
            bindCommonTexture();
            int gaugeX = this.x;
            int gaugeY = this.y;
            this.gui.drawTexturedRect(gaugeX, gaugeY, 20.0D, 55.0D, 38.0D, 100.0D);

        } else  {
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
        FluidStack fs = this.tank.getFluid();
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            if (this.tank instanceof Fluids.InternalFluidTank) {
                Fluids.InternalFluidTank tank1 = (Fluids.InternalFluidTank) this.tank;
                ret.add(Localization.translate("iu.tank.fluids"));
                ret.addAll(tank1.getFluidList());
            }
        } else if (fs != null && fs.amount > 0) {
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


}
