package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerQuantumStorage;
import net.minecraft.util.ResourceLocation;

public class GuiQuantumStorage extends GuiIU<ContainerQuantumStorage> {

    private final ContainerQuantumStorage container;

    public GuiQuantumStorage(ContainerQuantumStorage container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;

        this.addComponent(new GuiComponent(this, 60, 50, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(this.container.base.qe)
        ));


    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        this.drawForeground(mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {

        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.drawBackground();
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main.png");
    }

}
