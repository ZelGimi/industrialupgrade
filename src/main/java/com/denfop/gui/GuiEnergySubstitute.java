package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.container.ContainerSubstitute;
import ic2.core.init.Localization;
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
        this.ySize = 178;
        this.inventory.setX(7);
        this.inventory.setY(96);
    }


    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2, 4, name, 4210752, false);
    }

    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0, (this.width - this.xSize) / 2 + 103, (this.height - this.ySize) / 2 + 21,
                68, 9, Localization.translate("button.find_energypaths")
        ));
        this.buttonList.add(new GuiButton(1, (this.width - this.xSize) / 2 + 103, (this.height - this.ySize) / 2 + 39,
                68, 9, Localization.translate("button.set_value_energypaths")
        ));
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);


    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);


    }


    protected void actionPerformed(GuiButton guibutton) {

        if (guibutton.id == 0) {
            IUCore.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);

        }
        if (guibutton.id == 1) {
            IUCore.network.get(false).initiateClientTileEntityEvent(this.container.base, 1);

        }
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicontroller.png");
    }

}
