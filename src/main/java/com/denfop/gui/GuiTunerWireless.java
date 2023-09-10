package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerTunerWireless;
import com.denfop.network.packet.PacketUpdateServerTile;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiTunerWireless extends GuiCore<ContainerTunerWireless> {

    public final ContainerTunerWireless container;

    public GuiTunerWireless(ContainerTunerWireless container1) {
        super(container1);
        this.container = container1;
    }

    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0, (this.width - this.xSize) / 2 + 103, (this.height - this.ySize) / 2 + 21,
                68, 17, Localization.translate("button.rf")
        ));
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());

    }


    protected void actionPerformed(@Nonnull GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == 0) {
            new PacketUpdateServerTile(this.container.base, 0);

        }
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiTunerWireless.png");
    }

}
