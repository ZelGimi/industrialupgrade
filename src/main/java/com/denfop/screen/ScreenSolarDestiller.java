package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.TankWidget;
import com.denfop.containermenu.ContainerMenuSolarDestiller;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

@OnlyIn(Dist.CLIENT)
public class ScreenSolarDestiller<T extends ContainerMenuSolarDestiller> extends ScreenMain<ContainerMenuSolarDestiller> {

    public ScreenSolarDestiller(ContainerMenuSolarDestiller container) {
        super(container, container.base.getStyle());
        this.imageHeight = 170;
        this.inventory.setY(101);
        this.componentList.clear();
        this.addWidget(new TankWidget(this, 38, 20, 22, 40, container.base.inputTank) {


            @Override
            public void drawBackground(GuiGraphics poseStack, final int mouseX, final int mouseY) {

                FluidStack fs = this.tank.getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
                    int fluidX = this.x;
                    int fluidY = this.y;
                    int fluidWidth;
                    int fluidHeight;
                    fluidX += 3;
                    fluidY += 3;
                    fluidWidth = 17;
                    fluidHeight = 45;
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
        this.addWidget(new TankWidget(this, 115, 20, 22, 40, container.base.outputTank) {


            @Override
            public void drawBackground(GuiGraphics poseStack, final int mouseX, final int mouseY) {

                FluidStack fs = this.tank.getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
                    int fluidX = this.x;
                    int fluidY = this.y;
                    int fluidWidth;
                    int fluidHeight;
                    fluidX += 3;
                    fluidY += 3;
                    fluidWidth = 17;
                    fluidHeight = 45;

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
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
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

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
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
                return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guisolardestiller.png");
            case ADVANCED:
                return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiadvsolardestiller.png");
            case IMPROVED:
                return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiimpsolardestiller.png");
            case PERFECT:
                return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guipersolardestiller.png");
            case PHOTONIC:
                return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiphosolardestiller.png");
        }
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guisolardestiller.png");

    }

}
