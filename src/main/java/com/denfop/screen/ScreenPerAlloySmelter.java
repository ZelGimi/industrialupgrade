package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.BlockEntityPerAlloySmelter;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuPerAlloySmelter;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;

public class ScreenPerAlloySmelter<T extends ContainerMenuPerAlloySmelter> extends ScreenMain<ContainerMenuPerAlloySmelter> {

    public final ContainerMenuPerAlloySmelter container;

    public ScreenPerAlloySmelter(ContainerMenuPerAlloySmelter container1) {
        super(container1, ((BlockEntityPerAlloySmelter) container1.base).getStyle());
        this.container = container1;

        componentList.clear();
        this.inventoryList.add(container.base.outputSlot);
        inventory = new ScreenWidget(this, 7, 83, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.inventoryList))
        );

        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new ScreenWidget(this, 30, 59, EnumTypeComponent.HEAT,
                new WidgetDefault<>(((BlockEntityPerAlloySmelter) this.container.base).heat)
        ));
        this.addComponent(new ScreenWidget(this, 10, 60, EnumTypeComponent.ENERGY, new WidgetDefault<>(this.container.base.energy)));
        this.addComponent(new ScreenWidget(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 97, 35, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
        componentList.add(new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(((BlockEntityPerAlloySmelter) this.container.base).input_slot)
                ))
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
