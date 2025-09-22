package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerRocketLaunchPad;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import static com.denfop.api.gui.GuiElement.bindBlockTexture;
import static com.denfop.api.gui.GuiElement.getBlockTextureMap;

public class GuiRocketLaunchPad extends GuiIU<ContainerRocketLaunchPad> {

    public GuiRocketLaunchPad(ContainerRocketLaunchPad guiContainer) {
        super(guiContainer);
        ySize = 220;
        this.componentList.clear();
        this.addElement(new TankGauge(this, 106, 15, 12, 35, guiContainer.base.tank){
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
                    fluidHeight = 35;

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
        });

        this.componentList.add(new GuiComponent(this, 56, 12, EnumTypeComponent.ENERGY_HEIGHT_1,
                new Component<>(guiContainer.base.energy)
        ));
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        for (int i = 0; i < 9; i++) {
            if (container.base.tanks[i].getFluidAmount() <= 0) {
                continue;
            }
            FluidStack fs = container.base.tanks[i].getFluid();
            new Area(this, 8 + i * 18, 60, 18, 18).withTooltip(fs
                    .getFluid()
                    .getLocalizedName(fs) + ": " + fs.amount + " " + Localization.translate("iu.generic.text.mb")).drawForeground(
                    par1,
                    par2
            );
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        for (int i = 0; i < 9; i++) {
            if (container.base.tanks[i].getFluidAmount() <= 0) {
                continue;
            }
            FluidStack fs = container.base.tanks[i].getFluid();
            int fluidX = 7 + i * 18 + 1;
            int fluidY = 79 - 20 + 1;
            int fluidWidth = 16;
            int fluidHeight = 16;
            Fluid fluid = fs.getFluid();
            TextureAtlasSprite sprite = fluid != null
                    ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString())
                    : null;
            int color = fluid != null ? fluid.getColor(fs) : -1;
            bindBlockTexture();
            this.drawSprite(
                    fluidX,
                    fluidY,
                    fluidWidth,
                    fluidHeight,
                    sprite,
                    color,
                    1.0,
                    false,
                    false
            );

        }
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_rocket_pad.png");
    }

}
