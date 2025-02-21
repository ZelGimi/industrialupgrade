package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerSolarDestiller;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSolarDestiller extends GuiIU<ContainerSolarDestiller> {

    public GuiSolarDestiller(ContainerSolarDestiller container) {
        super(container, container.base.getStyle());
        this.ySize = 170;
        this.inventory.setY(101);
        this.componentList.clear();
        this.addElement(new TankGauge(this, 38, 20, 22, 40, container.base.inputTank, TankGauge.TankGuiStyle.Normal) {


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
                        fluidWidth = 17;
                        fluidHeight = 44;
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
                    this.gui.drawTexturedModalRect(this.gui.guiLeft + this.x + 2, this.gui.guiTop + this.y + 2, 179, 53, 19, 46);

                }
            }
        });
        this.addElement(new TankGauge(this, 115, 20, 22, 40, container.base.outputTank, TankGauge.TankGuiStyle.Normal) {


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
                        fluidWidth = 17;
                        fluidHeight = 44;
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
                    this.gui.drawTexturedModalRect(this.gui.guiLeft + this.x + 2, this.gui.guiTop + this.y + 2, 179, 53, 19, 46);

                }
            }
        });

    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        if (!this.isBlack) {
            this.drawXCenteredString(this.xSize / 2, 6, name, ModUtils.convertRGBcolorToInt(216, 216, 216), false);
        } else {
            this.drawXCenteredString(this.xSize / 2, 6, name, ModUtils.convertRGBcolorToInt(216, 216, 216), false);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.bindTexture();
        if (this.container.base.canWork()) {
            final int tick = this.container.base.getTickRate();
            double progress = (double) ((this.container.base.getWorld().getWorldTime() % tick) / (tick * 1D));
            progress = Math.min(1, progress);
            this.drawTexturedModalRect(this.guiLeft + 62, this.guiTop + 40, 201, 1,
                    (int) (52 * progress), 10
            );
        }

    }

    protected ResourceLocation getTexture() {
        switch (container.base.getStyle()) {
            case DEFAULT:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisolardestiller.png");
            case ADVANCED:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiadvsolardestiller.png");
            case IMPROVED:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiimpsolardestiller.png");
            case PERFECT:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guipersolardestiller.png");
            case PHOTONIC:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiphosolardestiller.png");
        }
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisolardestiller.png");

    }

}
