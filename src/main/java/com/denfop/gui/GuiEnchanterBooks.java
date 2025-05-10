package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerEnchanterBooks;
import net.minecraft.resources.ResourceLocation;

public class GuiEnchanterBooks<T extends ContainerEnchanterBooks> extends GuiIU<ContainerEnchanterBooks> {

    public GuiEnchanterBooks(ContainerEnchanterBooks guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 70, 35, EnumTypeComponent.ENCHANT_PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));

        this.addComponent(new GuiComponent(this, 10, 35, EnumTypeComponent.EXP,
                new Component<>(this.container.base.enchant)
        ));
    }



    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
