package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerImpAlloySmelter;
import com.denfop.tiles.mechanism.TileEntityImpAlloySmelter;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;

public class GuiImpAlloySmelter<T extends ContainerImpAlloySmelter> extends GuiIU<ContainerImpAlloySmelter> {

    public final ContainerImpAlloySmelter container;

    public GuiImpAlloySmelter(ContainerImpAlloySmelter container1) {
        super(container1, ((TileEntityImpAlloySmelter) container1.base).getStyle());
        this.container = container1;

        componentList.clear();
        this.invSlotList.add(container.base.outputSlot);
        inventory = new GuiComponent(this, 7, 83, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.invSlotList))
        );

        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new GuiComponent(this, 30, 59, EnumTypeComponent.HEAT,
                new Component<>(((TileEntityImpAlloySmelter) this.container.base).heat)
        ));
        this.addComponent(new GuiComponent(this, 10, 60, EnumTypeComponent.ENERGY, new Component<>(this.container.base.energy)));
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 85, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
        componentList.add(new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(((TileEntityImpAlloySmelter) this.container.base).input_slot)
                ))
        ));
    }



    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
