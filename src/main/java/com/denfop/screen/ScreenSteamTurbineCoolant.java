package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.TankWidget;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuSteamTurbineCoolant;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ScreenSteamTurbineCoolant<T extends ContainerMenuSteamTurbineCoolant> extends ScreenMain<ContainerMenuSteamTurbineCoolant> {
    boolean hoverPlus = false;
    boolean hoverMinus = false;

    public ScreenSteamTurbineCoolant(ContainerMenuSteamTurbineCoolant guiContainer) {
        super(guiContainer);
        this.addWidget(new TankWidget(
                this,
                69,
                8,
                117 - 80,
                73 - 15,
                container.base.getCoolant()
        ) {

            protected List<String> getToolTip() {
                List<String> ret = new ArrayList<>();
                FluidStack fs = container.base.getCoolant().getFluid();
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
            public void drawBackground(PoseStack poseStack, final int mouseX, final int mouseY) {

                FluidStack fs = container.base.getCoolant().getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
                    int fluidX = this.x;
                    int fluidY = this.y;
                    int fluidWidth;
                    int fluidHeight;
                    fluidX += 6;
                    fluidY += 6;
                    fluidWidth = 28;
                    fluidHeight = 44;

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
        this.componentList.clear();

    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        if (hoverPlus) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if (hoverMinus) {
            new PacketUpdateServerTile(this.container.base, 1);
        }
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        hoverMinus = false;
        hoverPlus = false;
        if (par1 >= 62 && par2 >= 67 && par1 <= 73 && par2 <= 78) {
            hoverPlus = true;
            new TooltipWidget(this, 62, 67, 12, 12).withTooltip("+1").drawForeground(poseStack, par1, par2);
        }
        if (par1 >= 103 && par2 >= 67 && par1 <= 103 + 11 && par2 <= 78) {
            hoverMinus = true;
            new TooltipWidget(this, 103, 67, 12, 12).withTooltip("-1").drawForeground(poseStack, par1, par2);
        }
        drawString(poseStack, String.valueOf(this.container.base.getPressure()), 92 - getStringWidth(String.valueOf(this.container.base.getPressure())), 69, ModUtils.convertRGBcolorToInt(15,
                125, 205
        ));
    }


    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        if (hoverPlus) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 62, this.guiTop + 67, 244, 13, 12, 12);

        }
        if (hoverMinus) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 103, this.guiTop + 67, 244, 0, 12, 12);

        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisteamturbine_coolant.png");
    }

}
