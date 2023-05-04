
package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.CustomButton;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerCanner;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCanner extends GuiIU<ContainerCanner> {

    public static final ResourceLocation texture = new ResourceLocation(Constants.MOD_ID, "textures/gui/GUICanner.png");

    public GuiCanner(ContainerCanner container) {
        super(container);
        this.ySize = 184;
        this.componentList.clear();
        this.addElement((new CustomButton(this, 77, 64, 22, 13, this.createEventSender(3))).withTooltip("ic2.Canner.gui" +
                ".switchTanks"));

        this.addElement(new AdvArea(this,
                61,
                79,
                114,
                96).withTooltip(Localization.translate("ic2.Canner.gui.switch.EnrichLiquid")));
        this.addElement(TankGauge.createNormal(this, 39, 42, container.base.fluidTank));
        this.addElement(TankGauge.createNormal(this, 117, 42, container.base.outputTank));
        this.addComponent(new GuiComponent(this, 12, 62, EnumTypeComponent.ENERGY_CLASSIC,
                new Component<>(this.container.base.energy)
        ));

    }


    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.bindTexture();


        int progressSize = (int) Math.round(this.container.base.getProgress() * 23.0F);
        if (progressSize > 0) {
            this.drawTexturedRect(74.0, 22.0, progressSize, 14.0, 233.0, 0.0);
        }

    }

    protected ResourceLocation getTexture() {
        return texture;
    }

}
