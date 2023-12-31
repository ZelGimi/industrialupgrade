package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.TankGauge;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerHydrogenGenerator;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiHydrogenGenerator extends GuiIU<ContainerHydrogenGenerator> {

    public ContainerHydrogenGenerator container;
    public String name;

    public GuiHydrogenGenerator(ContainerHydrogenGenerator container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.name = Localization.translate((container.base).getName());
        this.addElement(TankGauge.createNormal(this, 70, 20, (container.base).fluidTank));
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 6, 4210752);
        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EF";
        new AdvArea(this, 111, 28, 136, 38)
                .withTooltip(tooltip2)
                .drawForeground(par1, par2);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIFluidGenerator.png");
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
            this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }

        x -= this.guiLeft;
        y -= this.guiTop;
        for (final GuiElement<?> guiElement : this.elements) {
            guiElement.drawBackground(x, y);

        }
        this.drawBackground();

    }


}
