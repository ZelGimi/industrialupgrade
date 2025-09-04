package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.chemicalplant.BlockEntityChemicalPlantGenerator;
import com.denfop.containermenu.ContainerMenuDefaultMultiElement;
import net.minecraft.resources.ResourceLocation;

public class ScreenChemicalGenerator<T extends ContainerMenuDefaultMultiElement> extends ScreenMain<ContainerMenuDefaultMultiElement> {

    public ScreenChemicalGenerator(ContainerMenuDefaultMultiElement guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, this.imageWidth / 2 - 20, (this.imageHeight - 80) / 2,
                EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new WidgetDefault<>(((BlockEntityChemicalPlantGenerator) this.container.base).getEnergy())
        ));

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guichemicalplant.png");
    }

}
