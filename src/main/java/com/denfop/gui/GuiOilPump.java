package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.api.vein.Type;
import com.denfop.container.ContainerOilPump;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiOilPump extends GuiIU<ContainerOilPump> {

    public final ContainerOilPump container;


    public GuiOilPump(ContainerOilPump container1) {
        super(container1);
        this.container = container1;
        addElement(TankGauge.createNormal(this, 96, 22, container.base.fluidTank));
        this.componentList.add(new GuiComponent(this, 117, 41, EnumTypeComponent.FLUID_PART,
                new Component<>(new ComponentEmpty())
        ));
        this.componentList.add(new GuiComponent(this, 43, 39, EnumTypeComponent.OIL,
                new Component(new ComponentEmpty()) {
                    @Override
                    public String getText(final GuiComponent guiComponent) {
                        if (container.base.find && container.base.count > 0 && container.base.maxcount > 0 && container.base.type == Type.OIL.ordinal()) {


                            return
                                    Localization.translate("iu.fluidneft") + ": " + container.base
                                            .count + "/" + container.base.maxcount
                                            + Localization.translate(Constants.ABBREVIATION + ".generic.text.mb");

                        } else {
                            return Localization.translate("iu.notfindoil");

                        }
                    }
                }
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
