package com.denfop.gui;


import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.container.ContainerWaterGenerator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiWaterGenerator<T extends ContainerWaterGenerator> extends GuiIU<ContainerWaterGenerator> {

    public final ContainerWaterGenerator container;
    public final String progressLabel;
    public final String amplifierLabel;

    public GuiWaterGenerator(ContainerWaterGenerator container1) {
        super(container1);
        this.container = container1;
        this.progressLabel = Localization.translate("Matter.gui.info.progress");
        this.amplifierLabel = Localization.translate("Matter.gui.info.amplifier");
        addElement(TankGauge.createNormal(this, 96, 22, container.base.fluidTank));
        this.componentList.add(new GuiComponent(this, 117, 41, EnumTypeComponent.FLUID_PART,
                new Component<>(new ComponentEmpty())
        ));
    }


    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer( poseStack,par1, par2);
        this.font.draw( poseStack,this.progressLabel, 8, 22, 4210752);
        this.font.draw( poseStack,this.container.base.getProgressAsString(), 18, 31, 4210752);


    }


    public String getName() {
        return this.container.base.getName();
    }

    public ResourceLocation getTexture() {

        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");


    }

}
