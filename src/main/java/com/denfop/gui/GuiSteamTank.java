package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerSteamTank;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSteamTank extends GuiIU<ContainerSteamTank> {

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

                FluidStack fs = container.base.getFluidTank().getFluid();
                if (fs != null && fs.amount > 0) {
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
                    GlStateManager.color(1, 1, 1, 1);
                    this.gui.bindTexture();
                    this.gui.drawTexturedModalRect(this.gui.guiLeft + 99, this.gui.guiTop + 23, 177, 1, 12, 46);
                }


            }
        });
    }

    protected void drawForegroundLayer(int par1, int par2) {

        super.drawForegroundLayer(par1, par2);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;


    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guisteamtank.png");
    }

}
