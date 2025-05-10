package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerHydrogenGenerator;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiHydrogenGenerator<T extends ContainerHydrogenGenerator> extends GuiIU<ContainerHydrogenGenerator> {

    public ContainerHydrogenGenerator container;
    public String name;

    public GuiHydrogenGenerator(ContainerHydrogenGenerator container1) {
        super(container1);
        this.container = container1;
        this.name = Localization.translate((container.base).getName());
        this.addElement(TankGauge.createNormal(this, 70, 20, (container.base).fluidTank));
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 103, 36, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 32, 40, EnumTypeComponent.FLUID_PART2,
                new Component<>(new ComponentEmpty())
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.container.base instanceof IUpgradableBlock) {
            bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }


    }


}
