package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.FluidItem;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerDefaultMultiElement;
import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class GuiChemicalGenerator extends GuiIU<ContainerDefaultMultiElement> {

    public GuiChemicalGenerator(ContainerDefaultMultiElement guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, this.xSize / 2 - 20, (this.ySize - 80) / 2,
                EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(((TileEntityChemicalPlantGenerator) this.container.base).getEnergy())
        ));

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guichemicalplant.png");
    }

}
