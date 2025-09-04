package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentProgress;
import com.denfop.containermenu.ContainerMenuIndustrialOrePurifier;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenIndustrialOrePurifier<T extends ContainerMenuIndustrialOrePurifier> extends ScreenMain<ContainerMenuIndustrialOrePurifier> {

    public final ContainerMenuIndustrialOrePurifier container;

    public ScreenIndustrialOrePurifier(ContainerMenuIndustrialOrePurifier container1) {
        super(container1);
        this.container = container1;

        this.addComponent(new ScreenWidget(this, 7, 64, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.componentList.add(new ScreenWidget(this, 10, 40, EnumTypeComponent.SOLARIUM_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.se)
        ));
        this.addComponent(new ScreenWidget(this, 81, 45, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(new ComponentProgress(this.container.base, 1,
                        (short) 0
                ) {
                    @Override
                    public double getBar() {
                        return container.base.getProgress();
                    }
                })
        ));
    }


    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        bindTexture(getTexture());


        int xoffset = guiLeft;
        int yoffset = guiTop;

        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack, this.imageWidth / 2 + 15, 5, name, 4210752, false);

    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
