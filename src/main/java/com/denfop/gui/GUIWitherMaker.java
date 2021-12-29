package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerBaseWitherMaker;
import ic2.core.GuiIC2;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIWitherMaker extends GuiIC2<ContainerBaseWitherMaker> {

    public final ContainerBaseWitherMaker container;

    public GUIWitherMaker(
            ContainerBaseWitherMaker container1
    ) {
        super(container1);
        this.container = container1;
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (40 * this.container.base.getProgress());
        this.mc.getTextureManager().bindTexture(getTexture());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;

        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 79, yoffset + 51 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 81, yoffset + 16, 177, 19, progress + 1, 18);
        }

    }

    public String getName() {
        return "";
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIWitherMaker.png");
    }

}
