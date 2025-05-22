package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerSmelteryFuelTank;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class GuiSmelteryFuelTank extends GuiIU<ContainerSmelteryFuelTank> {

    public GuiSmelteryFuelTank(ContainerSmelteryFuelTank guiContainer) {
        super(guiContainer);
        this.componentList.clear();

        this.addElement(new TankGauge(this, 67, 15, 37, 49, container.base.getFuelTank(), TankGauge.TankGuiStyle.Normal) {

            protected List<String> getToolTip() {
                List<String> ret = new ArrayList<>();
                FluidStack fs = container.base.getFuelTank().getFluid();
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

            @Override
            public void drawBackground(final int mouseX, final int mouseY) {

                FluidStack fs = this.tank.getFluid();
                if (fs != null && fs.amount > 0) {
                    int fluidX = this.x;
                    int fluidY = this.y;
                    int fluidWidth = this.width;
                    int fluidHeight = this.height;
                    if (this.getStyle().withBorder) {
                        fluidX += 3;
                        fluidY += 3;
                        fluidWidth = 37;
                        fluidHeight = 49;
                    }

                    Fluid fluid = fs.getFluid();
                    TextureAtlasSprite sprite = fluid != null
                            ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString())
                            : null;
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
                    this.gui.bindTexture();
                    this.gui.drawTexturedModalRect(this.gui.guiLeft + 95, this.gui.guiTop + 21, 177, 1, 19, 46);

                }
            }
        });
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guismeltery_fuel.png");
    }

}
