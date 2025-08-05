package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.TankGauge;
import com.denfop.blocks.FluidName;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerSteamTank;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GuiSteamTank<T extends ContainerSteamTank> extends GuiIU<ContainerSteamTank> {

    public final ContainerSteamTank container;

    public GuiSteamTank(ContainerSteamTank container1) {
        super(container1, EnumTypeStyle.STEAM);
        this.container = container1;
        this.addElement(new TankGauge(
                this,
                59,
                15,
                117 - 59,
                73 - 15,
                container.base.getFluidTank(),
                TankGauge.TankGuiStyle.Normal
        ) {

            protected List<String> getToolTip() {
                List<String> ret = new ArrayList<>();
                FluidStack fs = container.base.getFluidTank().getFluid();
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

                FluidStack fs = container.base.getFluidTank().getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
                    int fluidX = this.x;
                    int fluidY = this.y;
                    int fluidWidth = this.width;
                    int fluidHeight = this.height;
                    if (this.getStyle().withBorder) {
                        fluidX += 6;
                        fluidY += 6;
                        fluidWidth = 46;
                        fluidHeight = 46;
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
                    this.gui.drawSprite(poseStack,mouseX+
                            fluidX,
                             mouseY+(double) (fluidY + fluidHeight) - renderHeight,
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
                    this.gui.drawTexturedModalRect(poseStack, this.gui.guiLeft + 99, this.gui.guiTop + 23, 177, 1, 12, 46);
                }


            }
        });
    }




    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guisteamtank.png");
    }

}
