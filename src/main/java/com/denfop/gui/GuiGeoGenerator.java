package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerGeoGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGeoGenerator extends GuiIU<ContainerGeoGenerator> {

    public ContainerGeoGenerator container;
    public String name;

    public GuiGeoGenerator(ContainerGeoGenerator container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.name = Localization.translate((container.base).getName());
        this.addElement(TankGauge.createNormal(this, 62, 20, (container.base).fluidTank));
        this.addComponent(new GuiComponent(this, 34, 40, EnumTypeComponent.FLUID_PART2,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 84, 40, EnumTypeComponent.FLUID_PART1,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 104, 38, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.getEnergy())
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
