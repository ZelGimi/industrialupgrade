package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuEnchanterBooks;
import net.minecraft.resources.ResourceLocation;

public class ScreenEnchanterBooks<T extends ContainerMenuEnchanterBooks> extends ScreenMain<ContainerMenuEnchanterBooks> {

    public ScreenEnchanterBooks(ContainerMenuEnchanterBooks guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 130, 55, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 70, 35, EnumTypeComponent.ENCHANT_PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));

        this.addComponent(new ScreenWidget(this, 10, 35, EnumTypeComponent.EXP,
                new WidgetDefault<>(this.container.base.enchant)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
