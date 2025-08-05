package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.*;
import com.denfop.container.ContainerTank;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiTank<T extends ContainerTank> extends GuiIU<ContainerTank> {

    public final ContainerTank container;

    public GuiTank(ContainerTank container1) {
        super(container1);
        this.container = container1;

        this.addComponent(new GuiComponent(this, 78, 36, EnumTypeComponent.FLUID_PART1,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 117, 41, EnumTypeComponent.FLUID_PART,
                new Component<>(new ComponentEmpty())
        ));
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {

        super.drawForegroundLayer(poseStack, par1, par2);
        TankGauge.createNormal(this, 96, 22, container.base.getFluidTank()).drawForeground(poseStack, par1, par2);
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        int xOffset = (this.width - this.imageWidth) / 2;
        int yOffset = (this.height - this.imageHeight) / 2;
        TankGauge.createNormal(this, 96, 22, container.base.getFluidTank()).drawBackground(poseStack, xOffset, yOffset);


    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
