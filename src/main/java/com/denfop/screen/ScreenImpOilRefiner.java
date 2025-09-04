package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerMenuImpOilRefiner;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenImpOilRefiner<T extends ContainerMenuImpOilRefiner> extends ScreenMain<ContainerMenuImpOilRefiner> {

    private static final ResourceLocation background;

    static {
        background = new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

    public ContainerMenuImpOilRefiner container;

    public ScreenImpOilRefiner(ContainerMenuImpOilRefiner container1) {
        super(container1, EnumTypeStyle.IMPROVED);
        this.container = container1;
        this.imageHeight = 200;
        componentList.clear();
        inventory = new ScreenWidget(this, 7, 119, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );
        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new ScreenWidget(this, 153, 21, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addWidget(TankWidget.createNormal(this, 12, 20, container.base.getFluidTank(0)));
        this.addWidget(TankWidget.createNormal(this, 60, 20, container.base.getFluidTank(1)));
        this.addWidget(TankWidget.createNormal(this, 108, 20, container.base.getFluidTank(2)));
        this.addComponent(new ScreenWidget(this, 35, 38, EnumTypeComponent.FLUID_PART5,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 83, 38, EnumTypeComponent.FLUID_PART5,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 131, 38, EnumTypeComponent.FLUID_PART5,
                new WidgetDefault<>(new EmptyWidget())
        ));

        this.addComponent(new ScreenWidget(this, 63, 76, EnumTypeComponent.FLUID_PART6,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 111, 76, EnumTypeComponent.FLUID_PART6,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 70, 78, EnumTypeComponent.FLUID_PART7,
                new WidgetDefault<>(new WidgetSize(47, 3))
        ));
        this.addComponent(new ScreenWidget(this, 10, 95, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.container.base != null) {
            bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }

    }

}
