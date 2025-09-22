package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerCombinerSolidMatter;
import net.minecraft.util.ResourceLocation;


public class GuiCombinerSolidMatter extends GuiIU<ContainerCombinerSolidMatter> {

    public final ContainerCombinerSolidMatter container;

    public GuiCombinerSolidMatter(ContainerCombinerSolidMatter container1) {
        super(container1);
        this.container = container1;
        this.addComponent(new GuiComponent(this, 70, 25, EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy)
        ));
    }


    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        if (this.container.base != null) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
        this.mc.getTextureManager().bindTexture(getTexture());


    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
