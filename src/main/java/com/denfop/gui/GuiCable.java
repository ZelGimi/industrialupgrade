package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerBlockLimiter;
import com.denfop.container.ContainerCable;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiCable extends GuiIU<ContainerCable> {


    public GuiCable(ContainerCable guiContainer) {
        super(guiContainer);


    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.limiter.info"));
            List<String> compatibleUpgrades = ListInformationUtils.limiter_info;
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
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if(x >= 62 && y >= 41 && x <= 73 && y <= 48){
            new PacketUpdateServerTile(this.container.base, EnumFacing.WEST.ordinal());
        }
        if(x >= 86 && y >= 41 && x <= 97 && y <= 48){
            new PacketUpdateServerTile(this.container.base, EnumFacing.EAST.ordinal());
        }
        if(x >= 76 && y >= 27 && x <= 83 && y <= 38){
            new PacketUpdateServerTile(this.container.base, EnumFacing.NORTH.ordinal());
        }
        if(x >= 76 && y >= 51 && x <= 83 && y <= 62){
            new PacketUpdateServerTile(this.container.base, EnumFacing.SOUTH.ordinal());
        }
        if(x >= 76 && y >= 15 && x <= 83 && y <= 22){
            new PacketUpdateServerTile(this.container.base, EnumFacing.UP.ordinal());
        }
        if(x >= 76 && y >= 67 && x <= 83 && y <= 74){
            new PacketUpdateServerTile(this.container.base, EnumFacing.DOWN.ordinal());
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        new AdvArea(this, 62, 41, 73, 48).withTooltip(Localization.translate("iu.dir.west")).drawForeground(par1, par2);
        new AdvArea(this, 86, 41, 97, 48).withTooltip(Localization.translate("iu.dir.east")).drawForeground(
                par1,
                par2
        );

        new AdvArea(this, 76, 27, 83, 38).withTooltip(Localization.translate("iu.dir.north")).drawForeground(
                par1,
                par2
        );
        new AdvArea(this, 76, 51, 83, 62).withTooltip(Localization.translate("iu.dir.south")).drawForeground(par1, par2);
        new AdvArea(this, 76, 15, 83, 22).withTooltip(Localization.translate("iu.dir.top")).drawForeground(
                par1,
                par2
        );
        new AdvArea(this, 76, 67, 83, 74).withTooltip(Localization.translate("iu.dir.bottom")).drawForeground(par1, par2);



    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;

        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(xoffset + 3, yoffset + 3, 0, 0, 10, 10);

        this.mc.getTextureManager().bindTexture(getTexture());
        for(EnumFacing facing : this.container.base.getBlackList()){
            if(facing.ordinal() == 0){
                drawTexturedModalRect(xoffset + 75, yoffset + 66, 214, 119, 10, 10);
            }
            if(facing.ordinal() == 1){
                drawTexturedModalRect(xoffset + 75, yoffset + 14, 214, 119, 10, 10);
            }
            if(facing.ordinal() == 2){
                drawTexturedModalRect(xoffset + 75, yoffset + 26, 214, 130, 10, 14);
            }
            if(facing.ordinal() == 3){
                drawTexturedModalRect(xoffset + 75, yoffset + 50, 214, 130, 10, 14);
            }
            if(facing.ordinal() == 4){
                drawTexturedModalRect(xoffset + 61, yoffset + 40, 214, 108, 14, 10);
            }
            if(facing.ordinal() == 5){
                drawTexturedModalRect(xoffset + 85, yoffset + 40, 214, 108, 14, 10);
            }
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicable.png");
    }

}
