package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuCombPump;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenCompPump<T extends ContainerMenuCombPump> extends ScreenMain<ContainerMenuCombPump> {

    public final ContainerMenuCombPump container;

    public ScreenCompPump(ContainerMenuCombPump container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.addComponent(new ScreenWidget(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 7, 65, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.energyQe)
        ));
        this.addWidget(TankWidget.createNormal(this, 70, 16, container.base.fluidTank));
        this.addComponent(new ScreenWidget(this, 10, 27, EnumTypeComponent.ENERGY, new WidgetDefault<>(this.container.base.energy)));
        this.addComponent(new ScreenWidget(this, 36, 35, EnumTypeComponent.PROCESS2,
                new WidgetDefault<>(new ComponentProgress(
                        this.container.base,
                        1,
                        (short) this.container.base.defaultOperationLength
                ) {
                    @Override
                    public double getBar() {
                        return container.base.guiProgress;
                    }
                })
        ));
        this.addComponent(new ScreenWidget(this, 93, 36, EnumTypeComponent.FLUID_PART3,
                new WidgetDefault<>(new EmptyWidget())
        ));
    }

    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);


    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);


    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
