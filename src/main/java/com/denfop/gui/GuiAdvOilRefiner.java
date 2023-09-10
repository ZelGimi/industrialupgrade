package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerAdvOilRefiner;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAdvOilRefiner extends GuiIU<ContainerAdvOilRefiner> {

    private static final ResourceLocation background;

    static {
        background = new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIOilRefiner1.png");
    }

    public ContainerAdvOilRefiner container;

    public GuiAdvOilRefiner(ContainerAdvOilRefiner container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 131, 38, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        TankGauge.createNormal(this, 12, 6, container.base.fluidTank[0]).drawForeground(par1, par2);
        TankGauge.createNormal(this, 74, 6, container.base.fluidTank[1]).drawForeground(par1, par2);
        TankGauge.createNormal(this, 106, 6, container.base.fluidTank[2]).drawForeground(par1, par2);
        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EF";
        new AdvArea(this, 38, 68, 68, 78)
                .withTooltip(tooltip2)
                .drawForeground(par1, par2);

    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        this.mc.getTextureManager().bindTexture(background);
        int energy = (int) ((this.container.base.energy.getEnergy() / this.container.base.energy.getCapacity()) * 29);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        energy = Math.min(energy, 29);
        this.drawTexturedModalRect(xOffset + 39, yOffset + 69, 177, 104, energy, 9);
        TankGauge.createNormal(this, 12, 6, container.base.fluidTank[0]).drawBackground(xOffset, yOffset);
        TankGauge.createNormal(this, 74, 6, container.base.fluidTank[1]).drawBackground(xOffset, yOffset);
        TankGauge.createNormal(this, 106, 6, container.base.fluidTank[2]).drawBackground(xOffset, yOffset);
        if (this.container.base != null) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
        this.drawBackground();
    }

}
