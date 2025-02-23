package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerApothecaryBee;
import com.denfop.container.ContainerChickenFarm;
import com.denfop.container.ContainerCollectorProductBee;
import com.denfop.container.ContainerCowFarm;
import com.denfop.container.ContainerPigFarm;
import com.denfop.container.ContainerPlantFertilizer;
import com.denfop.container.ContainerWeeder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiCollectorProductBee extends GuiIU<ContainerCollectorProductBee> {

    public GuiCollectorProductBee(ContainerCollectorProductBee guiContainer) {
        super(guiContainer);
        this.ySize += 40;
        this.inventory.setY(this.inventory.getY() + 40);
        this.addElement(new ImageInterface(this, 0, 0, this.xSize, this.ySize));
        this.addElement(TankGauge.createNormal(this, 23, 21, container.base.tank));
        this.addElement(TankGauge.createNormal(this, 120, 21, container.base.tank1));

        this.addComponent(new GuiComponent(this, 7, 64, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
    }
    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft + 3, guiTop + 3, 0, 0, 10, 10);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
