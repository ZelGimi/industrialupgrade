package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerCanner;
import com.denfop.network.packet.PacketUpdateServerTile;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiCanner extends GuiIU<ContainerCanner> {

    public static final ResourceLocation texture = new ResourceLocation(Constants.MOD_ID, "textures/gui/GUICanner.png");

    public GuiCanner(ContainerCanner container) {
        super(container);
        this.ySize = 184;
        this.componentList.clear();
        this.addElement(new Area(
                this,
                77, 64, 22, 13
        ).withTooltip(Localization.translate("Canner.gui.switchTanks")));

        this.addElement(TankGauge.createNormal(this, 40, 39, container.base.fluidTank));
        this.addElement(TankGauge.createNormal(this, 116, 39, container.base.outputTank));
        this.addComponent(new GuiComponent(this, 10, 57, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 74, 23, EnumTypeComponent.PROCESS2,
                new Component<>(container.base.componentProgress)
        ));

    }

    @Override
    protected void mouseClicked(int i, int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        i -= this.guiLeft;
        j -= this.guiTop;
        if (i >= 77 && i <= 77 + 22 && j >= 64 && j <= 64 + 13) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            new PacketUpdateServerTile(this.container.base, 3);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.bindTexture();


        int progressSize = (int) Math.round(this.container.base.componentProgress.getBar() * 23.0F);
        if (progressSize > 0) {
            this.drawTexturedRect(74.0, 22.0, progressSize, 14.0, 233.0, 0.0);
        }

    }

    protected ResourceLocation getTexture() {
        return texture;
    }

}
