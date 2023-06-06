package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerCropmatron;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCropmatron extends GuiIU<ContainerCropmatron> {

    public GuiCropmatron(ContainerCropmatron container) {
        super(container);
        this.ySize = 192;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 138, 82, EnumTypeComponent.ENERGY_CLASSIC,
                new Component<>(this.container.base.energy)
        ));
        this.addElement(TankGauge.createPlain(this, 11, 26, 24, 47, container.base.getWaterTank()));
        this.addElement(TankGauge.createPlain(this, 105, 26, 24, 47, container.base.getExTank()));
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUICropmatron.png");
    }

}
