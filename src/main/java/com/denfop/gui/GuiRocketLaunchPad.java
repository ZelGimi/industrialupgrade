package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerRocketLaunchPad;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import static com.denfop.api.gui.GuiElement.bindBlockTexture;
import static com.denfop.api.gui.GuiElement.getBlockTextureMap;

public class GuiRocketLaunchPad<T extends ContainerRocketLaunchPad> extends GuiIU<ContainerRocketLaunchPad> {

    public GuiRocketLaunchPad(ContainerRocketLaunchPad guiContainer) {
        super(guiContainer);
        imageHeight = 220;
        this.componentList.clear();
        this.addElement(new TankGauge(this, 106, 15, 12, 35, guiContainer.base.tank, TankGauge.TankGuiStyle.Plain));

        this.componentList.add(new GuiComponent(this, 56, 12, EnumTypeComponent.ENERGY_HEIGHT_1,
                new Component<>(guiContainer.base.energy)
        ));
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer( poseStack,par1, par2);
        for (int i = 0; i < 9; i++) {
            if (container.base.tanks[i].getFluidAmount() <= 0) {
                continue;
            }
            FluidStack fs = container.base.tanks[i].getFluid();
            new Area(this, 8 + i * 18, 60, 18, 18).withTooltip(fs
                    .getFluid()
                    .getFluidType().getDescription().getString() + ": " + fs.getAmount() + " " + Localization.translate("iu.generic.text.mb")).drawForeground(
                    poseStack, par1,
                    par2
            );
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics  poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer( poseStack,partialTicks, mouseX, mouseY);
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
            if (fluid == net.minecraft.world.level.material.Fluids.WATER)
                fluid = FluidName.fluidwater.getInstance().get();
            IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
            TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
            int color = extensions.getTintColor();
            bindBlockTexture();
            this.drawSprite(poseStack,
                  guiLeft+  fluidX,
                    guiTop+ fluidY,
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
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_rocket_pad.png");
    }

}
