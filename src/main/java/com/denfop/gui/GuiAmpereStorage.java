package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerAmpereGenerator;
import com.denfop.container.ContainerAmpereStorage;
import com.denfop.container.ContainerSteamAmpereGenerator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiAmpereStorage extends GuiIU<ContainerAmpereStorage> {

    public ContainerAmpereStorage container;
    public String name;

    public GuiAmpereStorage(ContainerAmpereStorage guiContainer, boolean b) {
        super(guiContainer);
        this.container = guiContainer;
        this.name = Localization.translate(guiContainer.base.getName());

        this.addComponent(new GuiComponent(this, 7, 35, EnumTypeComponent.AMPERE,
                new Component<>(this.container.base.pressure)
        ));
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;

    }


    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);


    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());



    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_amperestorage.png");

    }


}
