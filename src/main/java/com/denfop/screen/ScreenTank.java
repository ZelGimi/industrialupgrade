package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.containermenu.ContainerMenuTank;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenTank<T extends ContainerMenuTank> extends ScreenMain<ContainerMenuTank> {

    public final ContainerMenuTank container;

    public ScreenTank(ContainerMenuTank container1) {
        super(container1);
        this.container = container1;

        this.addComponent(new ScreenWidget(this, 78, 36, EnumTypeComponent.FLUID_PART1,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 117, 41, EnumTypeComponent.FLUID_PART,
                new WidgetDefault<>(new EmptyWidget())
        ));
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {

        super.drawForegroundLayer(poseStack, par1, par2);
        TankWidget.createNormal(this, 96, 22, container.base.getFluidTank()).drawForeground(poseStack, par1, par2);
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        int xOffset = (this.width - this.imageWidth) / 2;
        int yOffset = (this.height - this.imageHeight) / 2;
        TankWidget.createNormal(this, 96, 22, container.base.getFluidTank()).drawBackground(poseStack, xOffset, yOffset);


    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
