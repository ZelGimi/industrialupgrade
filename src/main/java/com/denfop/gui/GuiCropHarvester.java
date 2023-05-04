package com.denfop.gui;


import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerCropHarvester;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCropHarvester extends GuiIU<ContainerCropHarvester> {

    public GuiCropHarvester(ContainerCropHarvester container) {
        super(container);
        this.addComponent(new GuiComponent(this, 19, 37, EnumTypeComponent.ENERGY_CLASSIC,
                new Component<>(this.container.base.energy)
        ));
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation("ic2", "textures/gui/GUICropHarvester.png");
    }

}
