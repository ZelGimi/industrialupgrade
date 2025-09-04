package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuProgrammingTable;
import net.minecraft.resources.ResourceLocation;

public class ScreenProgrammingTable<T extends ContainerMenuProgrammingTable> extends ScreenMain<ContainerMenuProgrammingTable> {

    public ScreenProgrammingTable(ContainerMenuProgrammingTable guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 130, 55, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 55, 45, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container.base.timer)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
