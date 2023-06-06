package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerObsidianGenerator;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiObsidianGenerator extends GuiIU<ContainerObsidianGenerator> {

    public final ContainerObsidianGenerator container;

    public GuiObsidianGenerator(ContainerObsidianGenerator container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 5, 60, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

        TankGauge.createNormal(this, 40, 8, container.base.fluidTank1).drawForeground(par1, par2);
        TankGauge.createNormal(this, 64, 8, container.base.fluidTank2).drawForeground(par1, par2);
        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EU";
        new AdvArea(this, 26, 56, 37, 71)
                .withTooltip(tooltip2)
                .drawForeground(par1, par2);

    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (16 * this.container.base.getProgress());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 25, yoffset + 57 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 101, yoffset + 34, 176, 32, progress, 16);
        }
        TankGauge.createNormal(this, 40, 8, container.base.fluidTank1).drawBackground(xoffset, yoffset);
        TankGauge.createNormal(this, 64, 8, container.base.fluidTank2).drawBackground(xoffset, yoffset);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2 + 15, 1, name, 4210752, false);

    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiObsidianGenerator.png");
    }

}
