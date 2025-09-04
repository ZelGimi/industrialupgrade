package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerMenuAdvSteamQuarry;
import net.minecraft.resources.ResourceLocation;

public class ScreenAdvSteamQuarry<T extends ContainerMenuAdvSteamQuarry> extends ScreenMain<ContainerMenuAdvSteamQuarry> {

    public final ContainerMenuAdvSteamQuarry container;

    public ScreenAdvSteamQuarry(ContainerMenuAdvSteamQuarry container1) {
        super(container1, EnumTypeStyle.STEAM);
        this.container = container1;


        this.addComponent(new ScreenWidget(this, 7, 64, EnumTypeComponent.STEAM_FLUID,
                new WidgetDefault<>(this.container.base.steam)
        ));
    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guisteam_machine.png");
    }

}
