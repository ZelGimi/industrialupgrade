package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuCombinerSolidMatter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;


public class ScreenCombinerSolidMatter<T extends ContainerMenuCombinerSolidMatter> extends ScreenMain<ContainerMenuCombinerSolidMatter> {

    public final T container;

    public ScreenCombinerSolidMatter(T container1) {
        super(container1);
        this.container = container1;
        this.addComponent(new ScreenWidget(this, 70, 25, EnumTypeComponent.ENERGY_HEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }


    protected void renderBg(GuiGraphics poseStack, float f, int x, int y) {
        super.renderBg(poseStack, f, x, y);
        if (this.container.base != null) {
            bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
        bindTexture();


    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
