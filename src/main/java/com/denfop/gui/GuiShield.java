package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerShield;
import com.denfop.network.packet.PacketItemStackEvent;
import com.denfop.utils.ModUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class GuiShield extends GuiIU<ContainerShield> {

    public GuiShield(ContainerShield guiContainer) {
        super(guiContainer);
        this.componentList.add(new GuiComponent(this, 30, 50, EnumTypeComponent.ACCEPT,
                new Component<>(new ComponentButton(guiContainer.base, 1, ""))));

        this.componentList.add(new GuiComponent(this, 60, 50, EnumTypeComponent.CANCEL,
                new Component<>(new ComponentButton(guiContainer.base, -2, ""))));
        this.componentList.add(new GuiComponent(this, 100, 50, EnumTypeComponent.ACCEPT,
                new Component<>(new ComponentButton(guiContainer.base, 0, ""))));

        this.componentList.add(new GuiComponent(this, 130, 50, EnumTypeComponent.CANCEL,
                new Component<>(new ComponentButton(guiContainer.base, -1, ""))));

        this.componentList.add(new GuiComponent(this, 10, 66, EnumTypeComponent.WHITE,
                new Component<>(new ComponentButton(guiContainer.base, 3, ""))));
        this.componentList.add(new GuiComponent(this, 10, 50, EnumTypeComponent.BLACK,
                new Component<>(new ComponentButton(guiContainer.base, 2, ""))));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(Localization.translate("iu.shield" + (container.base.visibleShield ? ".visible" :
                        ".invisible")),
                10, 42, 0);
        this.fontRenderer.drawString(Localization.translate("iu.laser" + (container.base.visibleLaser ? ".visible" :
                        ".invisible")),
                93, 42, 0);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
