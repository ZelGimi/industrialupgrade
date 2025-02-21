package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerCombPump;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCompPump extends GuiIU<ContainerCombPump> {

    public final ContainerCombPump container;

    public GuiCompPump(ContainerCombPump container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 7, 65, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(this.container.base.energyQe)
        ));
        this.addElement(TankGauge.createNormal(this, 70, 16, container.base.fluidTank));
        this.addComponent(new GuiComponent(this, 10, 27, EnumTypeComponent.ENERGY, new Component<>(this.container.base.energy)));
        this.addComponent(new GuiComponent(this, 36, 35, EnumTypeComponent.PROCESS2,
                new Component<>(new ComponentProgress(
                        this.container.base,
                        1,
                        (short) this.container.base.defaultOperationLength
                ) {
                    @Override
                    public double getBar() {
                        return container.base.guiProgress;
                    }
                })
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
