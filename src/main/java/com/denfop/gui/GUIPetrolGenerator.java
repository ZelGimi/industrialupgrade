package com.denfop.gui;

import com.denfop.container.ContainerPetrolGenerator;
import com.denfop.utils.ModUtils;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.gui.GuiElement;
import ic2.core.gui.TankGauge;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiPetrolGenerator extends GuiIC2<ContainerPetrolGenerator> {

    public ContainerPetrolGenerator container;
    public String name;

    public GuiPetrolGenerator(ContainerPetrolGenerator container1) {
        super(container1);
        this.container = container1;
        this.name = Localization.translate((container.base).getName());
        this.addElement(TankGauge.createNormal(this, 70, 20, (container.base).fluidTank));

    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 6, 4210752);
        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EU";
        new AdvArea(this, 111, 28, 136, 38)
                .withTooltip(tooltip2)
                .drawForeground(par1, par2);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(IC2.RESOURCE_DOMAIN, "textures/gui/GUIFluidGenerator.png");
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);
        if (this.container.base.energy.getEnergy() > 0.0D) {
            int i2 = (int) (this.container.base.energy.getFillRatio() * 25);
            this.drawTexturedModalRect(xOffset + 111, yOffset + 25, 176, 0, i2, 17);
        }
        if (this.container.base instanceof IUpgradableBlock) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("ic2", "textures/gui/infobutton.png"));
            this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }

        x -= this.guiLeft;
        y -= this.guiTop;
        for (final GuiElement<?> guiElement : this.elements) {
            if (guiElement.isEnabled()) {
                guiElement.drawBackground(x, y);
            }
        }


    }


}
