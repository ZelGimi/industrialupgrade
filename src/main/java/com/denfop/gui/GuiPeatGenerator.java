package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerPeatGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPeatGenerator extends GuiIU<ContainerPeatGenerator> {


    public ContainerPeatGenerator container;
    public String name;

    public GuiPeatGenerator(ContainerPeatGenerator container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.name = Localization.translate(this.container.base.getName());
        this.addComponent(new GuiComponent(this, 66, 36, EnumTypeComponent.FIRE,
                new Component<>(new ComponentProgress(this.container.base, 1, (short) 1) {
                    @Override
                    public double getBar() {
                        return container.base.gaugeFuelScaled(12) / 12D;
                    }

                })
        ));
        this.addComponent(new GuiComponent(this, 86, 36, EnumTypeComponent.ENERGY_WEIGHT_1,
                new Component<>(this.container.base.energy)
        ));
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);


    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
    }

}
