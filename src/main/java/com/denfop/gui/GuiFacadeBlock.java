package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerFacadeBlock;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiFacadeBlock extends GuiIU<ContainerFacadeBlock> {

    public GuiFacadeBlock(final ContainerFacadeBlock container) {
        super(container);
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        handleUpgradeTooltip(mouseX,mouseY);
    }
    private void handleUpgradeTooltip(int mouseX, int mouseY) {
       if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            List<String> compatibleUpgrades = ListInformationUtils.facade;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(h + 3, k + 3, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(getTexture());

    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0, (this.width - this.xSize) / 2 + 48, (this.height - this.ySize) / 2 + 60,
                40, 15, Localization.translate("button.write")
        ));
        this.buttonList.add(new GuiButton(1, (this.width - this.xSize) / 2 + 115, (this.height - this.ySize) / 2 + 60,
                40, 15, Localization.translate("facademechanism_button")
        ));
    }
    protected void actionPerformed(@Nonnull GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == 0) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if (guibutton.id == 1) {
            new PacketUpdateServerTile(this.container.base, 1);
        }
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guifacademachine.png");
    }

}
