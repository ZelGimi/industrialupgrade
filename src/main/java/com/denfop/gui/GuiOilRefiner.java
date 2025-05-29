package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerOilRefiner;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiOilRefiner<T extends ContainerOilRefiner> extends GuiIU<ContainerOilRefiner> {

    private static final ResourceLocation background;

    static {
        background = new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

    public ContainerOilRefiner container;

    public GuiOilRefiner(ContainerOilRefiner container1) {
        super(container1, EnumTypeStyle.DEFAULT);
        this.container = container1;
        this.imageHeight = 200;
        componentList.clear();
        inventory = new GuiComponent(this, 7, 119, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );
        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new GuiComponent(this, 153, 21, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addElement(TankGauge.createNormal(this, 12, 20, container.base.getFluidTank(0)));
        this.addElement(TankGauge.createNormal(this, 60, 20, container.base.getFluidTank(1)));
        this.addElement(TankGauge.createNormal(this, 108, 20, container.base.getFluidTank(2)));
        this.addComponent(new GuiComponent(this, 35, 38, EnumTypeComponent.FLUID_PART5,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 83, 38, EnumTypeComponent.FLUID_PART5,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 131, 38, EnumTypeComponent.FLUID_PART5,
                new Component<>(new ComponentEmpty())
        ));

        this.addComponent(new GuiComponent(this, 63, 76, EnumTypeComponent.FLUID_PART6,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 111, 76, EnumTypeComponent.FLUID_PART6,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 70, 78, EnumTypeComponent.FLUID_PART7,
                new Component<>(new ComponentCustomizeSize(47, 3))
        ));
        this.addComponent(new GuiComponent(this, 10, 95, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

    protected void renderBg(GuiGraphics poseStack, float f, int x, int y) {
        super.renderBg(poseStack, f, x, y);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.container.base != null) {
            bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }

    }

}
