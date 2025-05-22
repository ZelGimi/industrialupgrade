package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.*;
import com.denfop.container.ContainerPump;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPump extends GuiIU<ContainerPump> {

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

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);


    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);


    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
