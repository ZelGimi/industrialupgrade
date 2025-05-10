package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.api.vein.Type;
import com.denfop.container.ContainerOilPump;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiOilPump<T extends ContainerOilPump> extends GuiIU<ContainerOilPump> {

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


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
