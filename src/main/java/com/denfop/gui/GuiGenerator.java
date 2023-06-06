package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerGenerator;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiGenerator extends GuiIU<ContainerGenerator> {

    private static final ResourceLocation background;

    static {
        background = new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiGenerator.png");
    }

    public ContainerGenerator container;
    public String name;

    public GuiGenerator(ContainerGenerator container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.name = Localization.translate(this.container.base.getName());
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 6, 4210752);
        new AdvArea(this, 90, 35, 121, 51).withTooltip(Localization.translate(
                "ic2.generic.text.bufferEU",
                ModUtils.getString(this.container.base.getEnergy())
        )).drawForeground(par1, par2);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiGenerator.png");
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        int i1;
        if (this.container.base.fuel > 0) {
            i1 = this.container.base.gaugeFuelScaled(12);
            this.drawTexturedModalRect(j + 66, k + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = this.container.base.gaugeStorageScaled(24);
        this.drawTexturedModalRect(j + 94, k + 35, 176, 14, i1, 17);
        this.drawBackground();
    }

}
