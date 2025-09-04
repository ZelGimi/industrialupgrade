package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.widget.*;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuBiomassGenerator;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenBiomassGenerator<T extends ContainerMenuBiomassGenerator> extends ScreenMain<ContainerMenuBiomassGenerator> {

    public ContainerMenuBiomassGenerator container;
    public String name;

    public ScreenBiomassGenerator(ContainerMenuBiomassGenerator container1) {
        super(container1);
        this.container = container1;
        this.name = Localization.translate((container.base).getName());
        this.addWidget(TankWidget.createNormal(this, 70, 20, (container.base).fluidTank));
        this.addComponent(new ScreenWidget(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 103, 36, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 32, 40, EnumTypeComponent.FLUID_PART2,
                new WidgetDefault<>(new EmptyWidget())
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.container.base instanceof BlockEntityUpgrade) {
            bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }


    }


}
