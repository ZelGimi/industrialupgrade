package com.denfop.api.gui;

import com.denfop.Localization;
import com.denfop.blocks.FluidName;
import com.denfop.componets.Fluids;
import com.denfop.gui.GuiCore;
import com.denfop.utils.KeyboardIU;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import java.util.List;

public class TankGauge extends GuiElement<TankGauge> {


    public static final int v = 100;
    protected final IFluidTank tank;
    private final TankGauge.TankGuiStyle style;

    public TankGauge(GuiCore<?> gui, int x, int y, int width, int height, IFluidTank tank, TankGauge.TankGuiStyle style) {
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

    public TankGuiStyle getStyle() {
        return style;
    }

    public void drawBackground(PoseStack poseStack, int mouseX, int mouseY) {
        bindCommonTexture();
        FluidStack fs = this.tank.getFluid();
        if (!fs.isEmpty() && fs.getAmount() > 0) {
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
                fluidHeight = 47;
            }

            Fluid fluid = fs.getFluid();
            if (fluid == net.minecraft.world.level.material.Fluids.WATER)
                fluid = FluidName.fluidwater.getInstance().get();
            IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
            TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
            int color = extensions.getTintColor();
            double renderHeight = (double) fluidHeight * ModUtils.limit(
                    (double) fs.getAmount() / (double) this.tank.getCapacity(),
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
                    -1,
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
        FluidStack fs = this.tank.getFluid();
        if (KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {
            if (this.tank instanceof Fluids.InternalFluidTank) {
                Fluids.InternalFluidTank tank1 = (Fluids.InternalFluidTank) this.tank;
                ret.add(Localization.translate("iu.tank.fluids"));
                ret.addAll(tank1.getFluidList());
            }
        } else if (!fs.isEmpty() && fs.getAmount() > 0) {
            Fluid fluid = fs.getFluid();
            if (fluid != null) {
                ret.add(Localization.translate(fs.getFluid().getFluidType().getDescriptionId()) + ": " + fs.getAmount() + " " + Localization.translate("iu.generic.text.mb"));
            } else {
                ret.add("invalid fluid stack");
            }
        } else {
            ret.add(Localization.translate("iu.generic.text.empty"));
        }

        return ret;
    }

    public enum TankGuiStyle {
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
