package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.*;
import com.denfop.container.ContainerPump;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiPump<T extends ContainerPump> extends GuiIU<ContainerPump> {

    public final ContainerPump container;

    public GuiPump(ContainerPump container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.addElement(TankGauge.createNormal(this, 70, 16, container.base.fluidTank));
        this.addComponent(new GuiComponent(this, 10, 27, EnumTypeComponent.ENERGY, new Component<>(this.container.base.energy)));
        this.addComponent(new GuiComponent(this, 36, 35, EnumTypeComponent.PROCESS2,
                new Component<>(this.container.base.componentProgress)
        ));
        this.addComponent(new GuiComponent(this, 93, 36, EnumTypeComponent.FLUID_PART3,
                new Component<>(new ComponentEmpty())
        ));
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
