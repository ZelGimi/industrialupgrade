package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerSolarGeneratorEnergy;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.gui.Area;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiSolarGeneratorEnergy extends GuiIC2<ContainerSolarGeneratorEnergy> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/SunnariumGenerator.png"
    );
    private final ContainerSolarGeneratorEnergy container;
    private final String name;

    public GuiSolarGeneratorEnergy(ContainerSolarGeneratorEnergy container1) {
        super(container1);
        this.ySize = 196;
        this.container = container1;
        this.name = Localization.translate(container1.base.getName());


    }

    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0, (this.width - this.xSize) / 2 + 13, (this.height - this.ySize) / 2 + 73,
                50, 16, Localization.translate(
                "button.sunnarium")
        ));
    }

    protected void actionPerformed(GuiButton guibutton) {

        if (guibutton.id == 0) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);

        }
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        String tooltip = "SE: " + ModUtils.getString(this.container.base.sunenergy.getEnergy());
        new Area(this, 123, 38, 146 - 123, 46 - 38).withTooltip(tooltip).drawForeground(mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        if (this.container.base.sunenergy.getEnergy() > 0.0D) {
            int i1 = (int) (24.0D * this.container.base.sunenergy.getFillRatio());
            drawTexturedModalRect(j + 123, k + 34, 176, 14, i1 + 1, 16);
        }

        this.fontRenderer.drawString(this.name, j + (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, k + 6,
                4210752
        );

    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

}
