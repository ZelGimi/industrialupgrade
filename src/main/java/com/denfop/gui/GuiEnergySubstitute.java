package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.container.ContainerSubstitute;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class GuiEnergySubstitute extends GuiIU<ContainerSubstitute> {

    public final ContainerSubstitute container;

    public GuiEnergySubstitute(ContainerSubstitute container1) {
        super(container1);
        this.container = container1;
        this.ySize = 200;
        this.inventory.setX(7);
        this.inventory.setY(119);
        this.addElement(new CustomButton(
                this,
                83,
                21,
                88,
                15,
                container1.base,
                0,
                Localization.translate("button.find_energypaths")
        ));
        this.addElement(new CustomButton(this, 83, 40, 88, 15, container1.base, 1, Localization.translate("button" +
                ".set_value_energypaths")));

    }


    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2, 4, name, 4210752, false);
    }

    public void initGui() {
        super.initGui();

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);


    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);


    }


    protected void actionPerformed(GuiButton guibutton) {


    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

}
