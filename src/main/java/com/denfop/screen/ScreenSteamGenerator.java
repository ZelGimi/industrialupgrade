package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TankWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuSteamGenerator;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenSteamGenerator<T extends ContainerMenuSteamGenerator> extends ScreenMain<ContainerMenuSteamGenerator> {

    public ContainerMenuSteamGenerator container;
    public String name;

    public ScreenSteamGenerator(ContainerMenuSteamGenerator container1) {
        super(container1);
        this.container = container1;
        this.name = Localization.translate((container.base).getName());
        this.addWidget(TankWidget.createNormal(this, 70, 20, (container.base).fluidTank1));
        this.addComponent(new ScreenWidget(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 103, 36, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.energy)
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
