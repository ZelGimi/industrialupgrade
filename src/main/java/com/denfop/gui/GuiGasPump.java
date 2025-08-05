package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.api.vein.Type;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerGasPump;
import net.minecraft.resources.ResourceLocation;

public class GuiGasPump<T extends ContainerGasPump> extends GuiIU<ContainerGasPump> {

    public final ContainerGasPump container;


    public GuiGasPump(ContainerGasPump container1) {
        super(container1);
        this.container = container1;
        addElement(TankGauge.createNormal(this, 96, 22, container.base.fluidTank));
        this.componentList.add(new GuiComponent(this, 117, 41, EnumTypeComponent.FLUID_PART,
                new Component<>(new ComponentEmpty())
        ));
        this.componentList.add(new GuiComponent(this, 43, 39, EnumTypeComponent.GAS,
                new Component(new ComponentProgress(this.container.base, 0, 0) {
                    @Override
                    public double getBar() {
                        if (container.base.find && container.base.count > 0 && container.base.maxcount > 0 && container.base.type == Type.GAS.ordinal()) {
                            return container.base
                                    .count / (container.base.maxcount * 1D);
                        } else {
                            return 0;
                        }

                    }
                }) {
                    @Override
                    public String getText(final GuiComponent guiComponent) {
                        if (container.base.find && container.base.count > 0 && container.base.maxcount > 0 && container.base.type == Type.GAS.ordinal()) {


                            return
                                    Localization.translate("iu.fluidgas") + ": " + container.base
                                            .count + "/" + container.base.maxcount
                                            + Localization.translate(Constants.ABBREVIATION + ".generic.text.mb");

                        } else {
                            return Localization.translate("iu.notfindgas");

                        }
                    }
                }
        ));
    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
