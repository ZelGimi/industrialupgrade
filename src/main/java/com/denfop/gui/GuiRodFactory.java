package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerRodFactory;
import com.denfop.network.IUpdatableTileEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiRodFactory extends GuiIU<ContainerRodFactory> {

    public GuiRodFactory(ContainerRodFactory guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 70, 36, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
        this.addComponent(new GuiComponent(this, 80, 60, EnumTypeComponent.LEFT,
                new Component<>(new ComponentButton(this.container.base, 0, "-1") {
                    @Override
                    public void ClickEvent() {
                        ((IUpdatableTileEvent) this.getEntityBlock()).updateTileServer(Minecraft.getMinecraft().player, 0);

                        super.ClickEvent();
                        this.getEntityBlock().onActivated(Minecraft.getMinecraft().player,
                                Minecraft.getMinecraft().player.getActiveHand(), EnumFacing.SOUTH, 0, 0, 0
                        );
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 81 + EnumTypeComponent.RIGHT.getWeight() + 10, 60, EnumTypeComponent.RIGHT,
                new Component<>(new ComponentButton(this.container.base, 1, "+1") {
                    @Override
                    public void ClickEvent() {
                        ((IUpdatableTileEvent) this.getEntityBlock()).updateTileServer(Minecraft.getMinecraft().player, 1);

                        super.ClickEvent();
                        this.getEntityBlock().onActivated(Minecraft.getMinecraft().player,
                                Minecraft.getMinecraft().player.getActiveHand(), EnumFacing.SOUTH, 0, 0, 0
                        );
                    }
                })
        ));
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(String.valueOf(this.container.base.type), 92, 65, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
