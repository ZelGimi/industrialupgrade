package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiEnriched extends GuiIC2<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GuiEnriched(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
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
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());

        int progress = (int) (15 * this.container.base.getProgress());
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 25, yoffset + 57 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 67 + 1, yoffset + 35, 177, 32, progress + 1, 15);
        }

    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUEnrichment.png");
    }

}
