package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.vein.common.Type;
import com.denfop.api.widget.*;
import com.denfop.containermenu.ContainerMenuOilPump;
import com.denfop.utils.Localization;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenOilPump<T extends ContainerMenuOilPump> extends ScreenMain<ContainerMenuOilPump> {

    public final ContainerMenuOilPump container;


    public ScreenOilPump(ContainerMenuOilPump container1) {
        super(container1);
        this.container = container1;
        addWidget(TankWidget.createNormal(this, 96, 22, container.base.fluidTank));
        this.componentList.add(new ScreenWidget(this, 117, 41, EnumTypeComponent.FLUID_PART,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.componentList.add(new ScreenWidget(this, 43, 39, EnumTypeComponent.OIL,
                new WidgetDefault(new EmptyWidget()) {
                    @Override
                    public String getText(final ScreenWidget screenWidget) {
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


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
