package com.denfop.api.widget;

import com.denfop.componets.Fluids;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class TanksWidget extends ScreenWidget {


    public static final int v = 100;
    private final List<Fluids.InternalFluidTank> tank;

    private TanksWidget(
            ScreenIndustrialUpgrade<?> gui,
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

    public static TanksWidget createNormal(ScreenIndustrialUpgrade<?> gui, int x, int y, List<Fluids.InternalFluidTank> tank) {
        return new TanksWidget(gui, x, y, 20, 55, tank);
    }



    public void drawBackground(PoseStack poseStack, int mouseX, int mouseY) {
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
            this.gui.drawTexturedModalRect(poseStack,
                    mouseX + this.x,
                    mouseY + this.y,
                    70,
                    100,
                    this.width,
                    this.height
            );

            int fluidX = this.x;
            int fluidY = this.y;
            int fluidWidth;
            int fluidHeight;
            fluidX += 4;
            fluidY += 4;
            fluidWidth = 12;
            fluidHeight = 48;

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
            bindCommonTexture();
            int gaugeX = this.x;
            int gaugeY = this.y;

            this.gui.drawTexturedModalRect(poseStack, mouseX + gaugeX, mouseY + gaugeY, 38, 100, 20, 55);

        } else {
            this.gui.drawTexturedModalRect(poseStack,
                    mouseX + this.x,
                    mouseY + this.y,
                    70,
                    100,
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

}
