package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerDefaultMultiElement;
import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantGenerator;
import net.minecraft.resources.ResourceLocation;

public class GuiChemicalGenerator<T extends ContainerDefaultMultiElement> extends GuiIU<ContainerDefaultMultiElement> {

    public GuiChemicalGenerator(ContainerDefaultMultiElement guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, this.imageWidth / 2 - 20, (this.imageHeight - 80) / 2,
                EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(((TileEntityChemicalPlantGenerator) this.container.base).getEnergy())
        ));

    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guichemicalplant.png");
    }

}
