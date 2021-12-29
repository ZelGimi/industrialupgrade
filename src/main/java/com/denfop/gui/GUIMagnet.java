package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerMagnet;
import ic2.core.GuiIC2;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIMagnet extends GuiIC2<ContainerMagnet> {

    public final ContainerMagnet container;

    public GUIMagnet(ContainerMagnet container1) {
        super(container1);
        this.container = container1;
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(this.getTexture());
        drawTexturedModalRect(xoffset, yoffset, 0, 0, this.xSize, this.ySize);
        int chargeLevel = (int) (48.0F * this.container.base.energy.getFillRatio());

        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 140 + 1 + 5, yoffset + 28 + 48 - chargeLevel, 176,
                    48 - chargeLevel, 48, chargeLevel
            );
        }

    }


    public String getName() {
        return null;
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIMagnet.png");
    }

}
