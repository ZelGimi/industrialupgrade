package com.denfop.screen;


import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.containermenu.ContainerMenuHeliumGenerator;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenHeliumGenerator<T extends ContainerMenuHeliumGenerator> extends ScreenMain<ContainerMenuHeliumGenerator> {

    public final ContainerMenuHeliumGenerator container;
    public final String progressLabel;
    public final String amplifierLabel;

    public ScreenHeliumGenerator(ContainerMenuHeliumGenerator container1) {
        super(container1);
        this.container = container1;
        this.progressLabel = Localization.translate("Matter.gui.info.progress");
        this.amplifierLabel = Localization.translate("Matter.gui.info.amplifier");
        addWidget(TankWidget.createNormal(this, 96, 22, container.base.fluidTank));
        this.componentList.add(new ScreenWidget(this, 117, 41, EnumTypeComponent.FLUID_PART,
                new WidgetDefault<>(new EmptyWidget())
        ));
    }


    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        draw(poseStack, this.progressLabel, 8, 22, 4210752);
        draw(poseStack, this.container.base.getProgressAsString(), 18, 31, 4210752);


    }


    public String getName() {
        return this.container.base.getName();
    }

    public ResourceLocation getTexture() {

        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");


    }

}
