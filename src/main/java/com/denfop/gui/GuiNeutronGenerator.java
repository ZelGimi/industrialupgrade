package com.denfop.gui;


import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerNeutronGenerator;
import com.denfop.network.packet.PacketUpdateServerTile;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiNeutronGenerator extends GuiIU<ContainerNeutronGenerator> {

    public final ContainerNeutronGenerator container;
    public final String progressLabel;
    public final String amplifierLabel;

    public GuiNeutronGenerator(ContainerNeutronGenerator container1) {
        super(container1);
        this.container = container1;
        this.progressLabel = Localization.translate("Matter.gui.info.progress");
        this.amplifierLabel = Localization.translate("Matter.gui.info.amplifier");
        addElement(TankGauge.createNormal(this, 96, 22, container.base.fluidTank));
        this.xSize = 200;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 182 && x <= 190 && y >= 6 && y <= 14) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.progressLabel, 8, 28, 4210752);
        this.fontRenderer.drawString(this.container.base.getProgressAsString(), 18, 39, 4210752);
        new AdvArea(this, 182, 6, 190, 14).withTooltip(this.container.base.work ? Localization.translate("turn_off") :
                Localization.translate("turn_on")).drawForeground(par1, par2);
        this.mc.getTextureManager().bindTexture(this.getTexture());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.container.base.work) {
            this.drawTexturedModalRect(+181, +5, 203, 5, 11, 11);

        }

    }


    public String getName() {
        return this.container.base.getName();
    }

    public ResourceLocation getTexture() {

        return new ResourceLocation(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI.png");


    }

}
