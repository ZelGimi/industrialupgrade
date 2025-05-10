package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerAdvSteamQuarry;
import net.minecraft.resources.ResourceLocation;

public class GuiAdvSteamQuarry<T extends ContainerAdvSteamQuarry> extends GuiIU<ContainerAdvSteamQuarry> {

    public final ContainerAdvSteamQuarry container;

    public GuiAdvSteamQuarry(ContainerAdvSteamQuarry container1) {
        super(container1, EnumTypeStyle.STEAM);
        this.container = container1;


        this.addComponent(new GuiComponent(this, 7, 64, EnumTypeComponent.STEAM_FLUID,
                new Component<>(this.container.base.steam)
        ));
    }




    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisteam_machine.png");
    }

}
