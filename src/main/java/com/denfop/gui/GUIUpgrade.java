package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerUpgrade;
import com.denfop.network.packet.PacketItemStackEvent;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GUIUpgrade extends GuiCore<ContainerUpgrade> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.TEXTURES,
            "textures/gui/guichangefacing.png"
    );
    private final String name;
    private final ItemStack itemStack;

    public GUIUpgrade(ContainerUpgrade container, final ItemStack itemStack1) {
        super(container);

        this.name = itemStack1.getDisplayName();
        this.itemStack = itemStack1;
        this.xSize = 121;
        this.ySize = 99;
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2 - 10, 6, 0);
        new AdvArea(this, 26, 44, 37, 51).withTooltip(Localization.translate("iu.dir.west")).drawForeground(par1, par2);
        new AdvArea(this, 50, 44, 68, 51).withTooltip(Localization.translate("iu.dir.east")).drawForeground(
                par1,
                par2
        );
        new AdvArea(this, 40, 44, 47, 51)
                .withTooltip(Localization.translate("iu.tooltip.upgrade.ejector.anyside"))
                .drawForeground(par1
                        , par2);
        new AdvArea(this, 40, 30, 47, 41).withTooltip(Localization.translate("iu.dir.north")).drawForeground(
                par1,
                par2
        );
        new AdvArea(this, 40, 54, 47, 65).withTooltip(Localization.translate("iu.dir.south")).drawForeground(par1, par2);
        new AdvArea(this, 40, 18, 47, 25).withTooltip(Localization.translate("iu.dir.top")).drawForeground(
                par1,
                par2
        );
        new AdvArea(this, 40, 70, 47, 77).withTooltip(Localization.translate("iu.dir.bottom")).drawForeground(par1, par2);

    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        final byte dir = ModUtils.nbt(this.itemStack).getByte("dir");
        if (dir == 0) {
            this.drawTexturedModalRect(this.guiLeft + 39, this.guiTop + 43, 125, 11, 10, 10);
        }
        if (dir == 1) {
            this.drawTexturedModalRect(this.guiLeft + 39, this.guiTop + 69, 125, 11, 10, 10);
        }
        if (dir == 2) {
            this.drawTexturedModalRect(this.guiLeft + 39, this.guiTop + 17, 125, 11, 10, 10);
        }
        if (dir == 3) {
            this.drawTexturedModalRect(this.guiLeft + 39, this.guiTop + 29, 125, 22, 13, 26);
        }
        if (dir == 4) {
            this.drawTexturedModalRect(this.guiLeft + 39, this.guiTop + 53, 125, 22, 13, 26);
        }

        if (dir == 5) {
            this.drawTexturedModalRect(this.guiLeft + 25, this.guiTop + 43, 125, 0, 26, 11);
        }
        if (dir == 6) {
            this.drawTexturedModalRect(this.guiLeft + 49, this.guiTop + 43, 125, 0, 26, 11);
        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = mouseX - xMin;
        int y = mouseY - yMin;
        if (x >= 26 && y >= 44 && x <= 37 && y <= 51) {
            new PacketItemStackEvent(5, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 5);
        }
        if (x >= 50 && y >= 44 && x <= 68 && y <= 51) {
            new PacketItemStackEvent(6, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 6);
        }
        if (x >= 40 && y >= 44 && x <= 47 && y <= 51) {
            new PacketItemStackEvent(0, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 0);
        }
        if (x >= 40 && y >= 30 && x <= 47 && y <= 41) {
            new PacketItemStackEvent(3, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 3);
        }
        if (x >= 40 && y >= 54 && x <= 47 && y <= 65) {
            new PacketItemStackEvent(4, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 4);
        }
        if (x >= 40 && y >= 18 && x <= 47 && y <= 25) {
            new PacketItemStackEvent(2, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 2);
        }
        if (x >= 40 && y >= 70 && x <= 47 && y <= 77) {
            new PacketItemStackEvent(1, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 1);
        }
    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
