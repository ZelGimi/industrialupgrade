package com.denfop.screen;


import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.blockentity.base.BlockEntityNeutronGenerator;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuNeutronGenerator;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenNeutronGenerator<T extends ContainerMenuNeutronGenerator> extends ScreenMain<ContainerMenuNeutronGenerator> {

    public final ContainerMenuNeutronGenerator container;
    public final String progressLabel;
    public final String amplifierLabel;

    public ScreenNeutronGenerator(ContainerMenuNeutronGenerator container1) {
        super(container1);
        this.container = container1;
        this.progressLabel = Localization.translate("Matter.gui.info.progress");
        this.amplifierLabel = Localization.translate("Matter.gui.info.amplifier");
        this.addComponent(new ScreenWidget(this, 4, 15, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        addWidget(TankWidget.createNormal(this, 96, 22, container.base.fluidTank));
        this.componentList.add(new ScreenWidget(this, 117, 41, EnumTypeComponent.FLUID_PART,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.componentList.add(new ScreenWidget(this, 45, 60, EnumTypeComponent.WORK_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((BlockEntityNeutronGenerator) this.getEntityBlock()).work ? Localization.translate("turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((BlockEntityNeutronGenerator) this.getEntityBlock()).work;
                    }
                })
        ));
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;

    }

    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        this.font.draw(poseStack, this.progressLabel, 8, 28, 4210752);
        this.font.draw(poseStack, this.container.base.getProgressAsString(), 18, 39, 4210752);


    }


    public String getName() {
        return this.container.base.getName();
    }

    public ResourceLocation getTexture() {

        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");


    }

}
