package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.vein.common.Type;
import com.denfop.api.widget.*;
import com.denfop.componets.ComponentProgress;
import com.denfop.containermenu.ContainerMenuGasPump;
import com.denfop.utils.Localization;
import net.minecraft.resources.ResourceLocation;

public class ScreenGasPump<T extends ContainerMenuGasPump> extends ScreenMain<ContainerMenuGasPump> {

    public final ContainerMenuGasPump container;


    public ScreenGasPump(ContainerMenuGasPump container1) {
        super(container1);
        this.container = container1;
        addWidget(TankWidget.createNormal(this, 96, 22, container.base.fluidTank));
        this.componentList.add(new ScreenWidget(this, 117, 41, EnumTypeComponent.FLUID_PART,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.componentList.add(new ScreenWidget(this, 43, 39, EnumTypeComponent.GAS,
                new WidgetDefault(new ComponentProgress(this.container.base, 0, 0) {
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
                    public String getText(final ScreenWidget screenWidget) {
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
