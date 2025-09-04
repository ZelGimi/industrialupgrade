package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuAirCollector;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenAirCollector<T extends ContainerMenuAirCollector> extends ScreenMain<ContainerMenuAirCollector> {

    private static final ResourceLocation background;

    static {
        background = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiaircollector.png");
    }

    public ContainerMenuAirCollector container;

    public ScreenAirCollector(ContainerMenuAirCollector container1) {
        super(container1);
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

        this.addComponent(new ScreenWidget(this, 16, 56, EnumTypeComponent.FLUID_PART4,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 28, 67, EnumTypeComponent.FLUID_PART7,
                new WidgetDefault<>(new ComponentCustomizeSize(7, 3))
        ));

        this.addComponent(new ScreenWidget(this, 60, 56, EnumTypeComponent.FLUID_PART4,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 72, 67, EnumTypeComponent.FLUID_PART7,
                new WidgetDefault<>(new ComponentCustomizeSize(7, 3))
        ));

        this.addComponent(new ScreenWidget(this, 108, 56, EnumTypeComponent.FLUID_PART4,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 120, 67, EnumTypeComponent.FLUID_PART7,
                new WidgetDefault<>(new ComponentCustomizeSize(7, 3))
        ));

        this.addComponent(new ScreenWidget(this, 23, 79, EnumTypeComponent.FLUID_PART7,
                new WidgetDefault<>(new ComponentCustomizeSize(43, 3))
        ));
        this.addComponent(new ScreenWidget(this, 67, 79, EnumTypeComponent.FLUID_PART7,
                new WidgetDefault<>(new ComponentCustomizeSize(47, 3))
        ));
        this.addComponent(new ScreenWidget(this, 131, 76, EnumTypeComponent.ENERGY_HEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        TankWidget.createNormal(this, 36, 20, container.base.fluidTank[0]).drawForeground(poseStack, par1, par2);
        TankWidget.createNormal(this, 80, 20, container.base.fluidTank[1]).drawForeground(poseStack, par1, par2);
        TankWidget.createNormal(this, 128, 20, container.base.fluidTank[2]).drawForeground(poseStack, par1, par2);
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.container.base != null) {
            bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
        TankWidget.createNormal(this, 36, 20, container.base.fluidTank[0]).drawBackground(poseStack, this.guiLeft, guiTop);
        TankWidget.createNormal(this, 80, 20, container.base.fluidTank[1]).drawBackground(poseStack, this.guiLeft, guiTop);
        TankWidget.createNormal(this, 128, 20, container.base.fluidTank[2]).drawBackground(poseStack, this.guiLeft, guiTop);

    }

}
