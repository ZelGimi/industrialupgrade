package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.TankGauge;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerSolarDestiller;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

@OnlyIn(Dist.CLIENT)
public class GuiSolarDestiller<T extends ContainerSolarDestiller> extends GuiIU<ContainerSolarDestiller> {

    public GuiSolarDestiller(ContainerSolarDestiller container) {
        super(container, container.base.getStyle());
        this.imageHeight = 170;
        this.inventory.setY(101);
        this.componentList.clear();
        this.addElement(new TankGauge(this, 38, 20, 22, 40, container.base.inputTank, TankGauge.TankGuiStyle.Normal) {


            @Override
            public void drawBackground(PoseStack poseStack, final int mouseX, final int mouseY) {

                FluidStack fs = this.tank.getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
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
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
                    int color = extensions.getTintColor();
                    double renderHeight = (double) fluidHeight * ModUtils.limit(
                            (double) fs.getAmount() / (double) this.tank.getCapacity(),
                            0.0D,
                            1.0D
                    );
                    bindBlockTexture();
                    this.gui.drawSprite(poseStack, guiLeft +
                                    fluidX,
                            guiTop + (double) (fluidY + fluidHeight) - renderHeight,
                            fluidWidth,
                            renderHeight,
                            sprite,
                            color,
                            1.0D,
                            false,
                            true
                    );
                    this.gui.bindTexture();
                    this.gui.drawTexturedModalRect(poseStack, this.gui.guiLeft + this.x + 2, this.gui.guiTop + this.y + 2, 179, 53, 19, 46);

                }
            }
        });
        this.addElement(new TankGauge(this, 115, 20, 22, 40, container.base.outputTank, TankGauge.TankGuiStyle.Normal) {


            @Override
            public void drawBackground(PoseStack poseStack, final int mouseX, final int mouseY) {

                FluidStack fs = this.tank.getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
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
                            guiLeft + fluidX,
                            guiTop + (double) (fluidY + fluidHeight) - renderHeight,
                            fluidWidth,
                            renderHeight,
                            sprite,
                            color,
                            1.0D,
                            false,
                            true
                    );
                    this.gui.bindTexture();
                    this.gui.drawTexturedModalRect(poseStack, this.gui.guiLeft + this.x + 2, this.gui.guiTop + this.y + 2, 179, 53, 19, 46);

                }
            }
        });

    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        String name = Localization.translate(this.container.base.getName());
        if (!this.isBlack) {
            this.drawXCenteredString(poseStack, this.imageWidth / 2, 6, Component.nullToEmpty(name), ModUtils.convertRGBcolorToInt(216, 216, 216), false);
        } else {
            this.drawXCenteredString(poseStack, this.imageHeight / 2, 6, Component.nullToEmpty(name), ModUtils.convertRGBcolorToInt(216, 216, 216), false);
        }
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        this.bindTexture();
        if (this.container.base.canWork()) {
            final int tick = this.container.base.getTickRate();
            double progress = (double) ((this.container.base.getWorld().getGameTime() % tick) / (tick * 1D));
            progress = Math.min(1, progress);
            this.drawTexturedModalRect(poseStack, this.guiLeft + 62, this.guiTop + 40, 201, 1,
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
