package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerChickenFarm;
import com.denfop.container.ContainerCowFarm;
import com.denfop.container.ContainerPigFarm;
import com.denfop.container.ContainerPlantFertilizer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiPlantFertilizer extends GuiIU<ContainerPlantFertilizer> {

    public GuiPlantFertilizer(ContainerPlantFertilizer guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 98, 45, EnumTypeComponent.ENERGY_WEIGHT,
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
