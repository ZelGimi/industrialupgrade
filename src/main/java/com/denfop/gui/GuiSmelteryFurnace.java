package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerSmelteryFurnace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiSmelteryFurnace extends GuiIU<ContainerSmelteryFurnace> {

    public GuiSmelteryFurnace(ContainerSmelteryFurnace guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.ySize = 181;
        this.addComponent(new GuiComponent(this, 100, 46, EnumTypeComponent.FIRE,
                new Component<>(this.getContainer().base.progress)
        ));
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        GlStateManager.color(1, 1, 1, 1);
        drawTexturedModalRect(this.guiLeft + 80, guiTop + 44, 178, 62, 18, 18);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guismeltery.png");
    }

}
