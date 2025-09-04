package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.dual.heat.BlockEntityWeldingMachine;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuDoubleElectricMachine;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;

public class ScreenWelding<T extends ContainerMenuDoubleElectricMachine> extends ScreenMain<ContainerMenuDoubleElectricMachine> {

    public final ContainerMenuDoubleElectricMachine container;

    public ScreenWelding(ContainerMenuDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
        this.addComponent(new ScreenWidget(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 104, 58, EnumTypeComponent.HEAT,
                new WidgetDefault<>(this.container.base.heat)
        ));
        this.addComponent(new ScreenWidget(this, 58, 35, EnumTypeComponent.ENERGY, new WidgetDefault<>(this.container.base.energy)));
        componentList.add(new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(((BlockEntityWeldingMachine) this.container.base).input_slot)
                ))
        ));
        this.addComponent(new ScreenWidget(this, 80, 35, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
