package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuElectrolyzer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenElectrolyzer<T extends ContainerMenuElectrolyzer> extends ScreenMain<ContainerMenuElectrolyzer> {

    private static final ResourceLocation background;

    static {
        background = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/GUIElectolyzer.png".toLowerCase());
    }

    public ContainerMenuElectrolyzer container;

    public ScreenElectrolyzer(ContainerMenuElectrolyzer container1) {
        super(container1);
        this.container = container1;
        componentList.clear();
        inventory = new ScreenWidget(this, 7, 119, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );
        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new ScreenWidget(this, 15, 102, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 122, 102, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addWidget(TankWidget.createNormal(this, 12, 44, container.base.getFluidTank(0)));
        this.addWidget(TankWidget.createNormal(this, 74, 6, container.base.getFluidTank(1)));
        this.addWidget(TankWidget.createNormal(this, 74, 63, container.base.getFluidTank(2)));
        this.addComponent(new ScreenWidget(this, 16, 26, EnumTypeComponent.FLUID_PART6,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 35, 85, EnumTypeComponent.FLUID_PART8,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 35, 45, EnumTypeComponent.FLUID_PART3,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 95, 25, EnumTypeComponent.FLUID_PART3,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 95, 85, EnumTypeComponent.FLUID_PART8,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 95, 25, EnumTypeComponent.FLUID_PART8,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 95, 85, EnumTypeComponent.FLUID_PART3,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 106, 32, EnumTypeComponent.FLUID_PART9,
                new WidgetDefault<>(new ComponentCustomizeSize(3, 54))
        ));
        this.addComponent(new ScreenWidget(this, 106, 26, EnumTypeComponent.FLUID_PART9,
                new WidgetDefault<>(new ComponentCustomizeSize(3, 5))
        ));
        this.addComponent(new ScreenWidget(this, 106, 92, EnumTypeComponent.FLUID_PART9,
                new WidgetDefault<>(new ComponentCustomizeSize(3, 6))
        ));
        this.imageHeight = 200;
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft(), this.guiTop(), 0, 0, this.imageWidth, this.imageHeight);


    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.container.base != null) {
            bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }


    }

}
