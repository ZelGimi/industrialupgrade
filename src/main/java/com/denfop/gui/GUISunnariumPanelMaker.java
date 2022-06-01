package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.tiles.mechanism.TileEntitySunnariumPanelMaker;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSunnariumPanelMaker extends GuiIC2<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GuiSunnariumPanelMaker(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        String tooltip =
                "SE: " + ModUtils.getString(((TileEntitySunnariumPanelMaker) this.container.base).sunenergy.getEnergy()) + "/" + ModUtils.getString(
                        ((TileEntitySunnariumPanelMaker) this.container.base).sunenergy.getCapacity());
        new AdvArea(this, 100, 60, 125, 70).withTooltip(tooltip).drawForeground(mouseX, mouseY);
        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EU";
        new AdvArea(this, 26, 56, 37, 71)
                .withTooltip(tooltip2)
                .drawForeground(mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (14 * this.container.base.getProgress());

        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 25, yoffset + 57 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        if (((TileEntitySunnariumPanelMaker) this.container.base).sunenergy.getEnergy() > 0.0D) {
            int i1 = (int) (24.0D * ((TileEntitySunnariumPanelMaker) this.container.base).sunenergy.getFillRatio());
            drawTexturedModalRect(xoffset + 101, yoffset + 57, 176, 49, i1 + 1, 16);
        }
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 74, yoffset + 34, 177, 32, progress + 1, 15);
        }

    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiSunnariumPanelMaker.png");
    }

}
