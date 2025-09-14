package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.TankWidget;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuGasWellTank;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ScreenGasWellTank<T extends ContainerMenuGasWellTank> extends ScreenMain<ContainerMenuGasWellTank> {

    public ScreenGasWellTank(ContainerMenuGasWellTank guiContainer) {
        super(guiContainer);
        componentList.clear();
        this.addWidget(new TankWidget(
                this,
                75,
                13,
                26,
                61,
                guiContainer.base.getTank()
        ) {

            protected List<String> getToolTip() {
                List<String> ret = new ArrayList<>();
                FluidStack fs = guiContainer.base.getTank().getFluid();
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (this.tank instanceof Fluids.InternalFluidTank) {
                        Fluids.InternalFluidTank tank1 = (Fluids.InternalFluidTank) this.tank;
                        ret.add(Localization.translate("iu.tank.fluids"));
                        ret.addAll(tank1.getFluidList());
                    }
                } else if (!fs.isEmpty() && fs.getAmount() > 0) {
                    Fluid fluid = fs.getFluid();
                    if (fluid != null) {
                        ret.add(fluid.getFluidType().getDescription().getString() + ": " + fs.getAmount() + " " + Localization.translate("iu.generic.text.mb"));
                    } else {
                        ret.add("invalid fluid stack");
                    }
                } else {
                    ret.add(Localization.translate("iu.generic.text.empty"));
                }

                return ret;
            }

            @Override
            public void drawBackground(GuiGraphics poseStack, final int mouseX, final int mouseY) {

                FluidStack fs = guiContainer.base.getTank().getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
                    int fluidX = this.x;
                    int fluidY = this.y;
                    int fluidWidth;
                    int fluidHeight;
                    fluidX += 3;
                    fluidY += 3;
                    fluidWidth = 20;
                    fluidHeight = 55;

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
                    this.gui.drawSprite(poseStack, mouseX +
                                    fluidX,
                            mouseY + (double) (fluidY + fluidHeight) - renderHeight,
                            fluidWidth,
                            renderHeight,
                            sprite,
                            color,
                            1.0D,
                            false,
                            true
                    );
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    this.gui.bindTexture();
                    this.gui.drawTexturedModalRect(poseStack, this.gui.guiLeft + 97, this.gui.guiTop + 14, 191, 5, 7, 46);

                }


            }
        });
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guigaswell_tank.png");
    }

}
