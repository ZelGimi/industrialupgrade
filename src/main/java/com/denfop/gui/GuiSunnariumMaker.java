package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerSunnariumMaker;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSunnariumMaker extends GuiIU<ContainerSunnariumMaker> {

    public final ContainerSunnariumMaker container;

    public GuiSunnariumMaker(ContainerSunnariumMaker container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        String tooltip =
                "SE: " + ModUtils.getString(this.container.base.sunenergy.getEnergy()) + "/" + ModUtils.getString(this.container.base.sunenergy.getCapacity());
        new AdvArea(this, 51, 63, 76, 73).withTooltip(tooltip).drawForeground(mouseX, mouseY);
        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EU";
        new AdvArea(this, 13, 61, 24, 76)
                .withTooltip(tooltip2)
                .drawForeground(mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (17 * this.container.base.getProgress());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 12, yoffset + 61 + 1 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 55, yoffset + 20, 176, 31, progress + 1, 31);
        }
        if (this.container.base.sunenergy.getEnergy() > 0.0D) {
            int i1 = (int) (24.0D * this.container.base.sunenergy.getFillRatio());
            drawTexturedModalRect(xoffset + 52, yoffset + 60, 176, 63, i1 + 1, 16);
        }
    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiSunnariumMaker.png");
    }

}
