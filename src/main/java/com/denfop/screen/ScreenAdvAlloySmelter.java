package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.triple.heat.BlockEntityAdvAlloySmelter;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuTripleElectricMachine;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;

@OnlyIn(Dist.CLIENT)
public class ScreenAdvAlloySmelter<T extends ContainerMenuTripleElectricMachine> extends ScreenMain<ContainerMenuTripleElectricMachine> {

    public final ContainerMenuTripleElectricMachine container;

    public ScreenAdvAlloySmelter(ContainerMenuTripleElectricMachine container1) {
        super(container1, ((BlockEntityAdvAlloySmelter) container1.base).getStyle());
        this.container = container1;

        componentList.clear();
        this.invSlotList.add(container.base.outputSlot);
        inventory = new ScreenWidget(this, 7, 83, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.invSlotList))
        );

        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new ScreenWidget(this, 105, 59, EnumTypeComponent.HEAT,
                new WidgetDefault<>(((BlockEntityAdvAlloySmelter) this.container.base).heat)
        ));
        this.addComponent(new ScreenWidget(this, 58, 35, EnumTypeComponent.ENERGY, new WidgetDefault<>(this.container.base.energy)));
        this.addComponent(new ScreenWidget(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 80, 35, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
        componentList.add(new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(((BlockEntityAdvAlloySmelter) this.container.base).input_slot)
                ))
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
