package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
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
public class GUIUpgrade extends GuiIU<ContainerUpgrade> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.TEXTURES,
            "textures/gui/guiupgrade1.png"
    );
    private static final ResourceLocation background1 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/gui/guiupgrade2.png"
    );
    private final String name;
    private final ItemStack itemStack;
    private final boolean fluid;

    public GUIUpgrade(ContainerUpgrade container, final ItemStack itemStack1) {
        super(container);
        this.componentList.clear();
        this.name = itemStack1.getDisplayName();
        this.itemStack = itemStack1;
        this.xSize = 172;
        this.ySize = 232;
        this.fluid = !(itemStack1.isItemEqual(IUItem.ejectorUpgrade) || itemStack1.isItemEqual(IUItem.pullingUpgrade));
        ;
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 12, 0);
        new AdvArea(this, 76 + 3, 26 + 3, 87 + 3, 37 + 3).withTooltip(Localization.translate("iu.dir.top")).drawForeground(
                par1,
                par2
        );
        new AdvArea(this, 56 + 3, 62 + 3, 71 + 3, 73 + 3).withTooltip(Localization.translate("iu.dir.west")).drawForeground(
                par1,
                par2
        );
        new AdvArea(this, 92 + 3, 62 + 3, 107 + 3, 73 + 3).withTooltip(Localization.translate("iu.dir.east")).drawForeground(
                par1,
                par2
        );
        new AdvArea(this, 76 + 3, 62 + 3, 87 + 3, 73 + 3)
                .withTooltip(Localization.translate("iu.tooltip.upgrade.ejector.anyside"))
                .drawForeground(par1
                        , par2);
        new AdvArea(this, 76 + 3, 42 + 3, 87 + 3, 57 + 3).withTooltip(Localization.translate("iu.dir.north")).drawForeground(
                par1,
                par2
        );
        new AdvArea(this, 76 + 3, 78 + 3, 87 + 3, 93 + 3).withTooltip(Localization.translate("iu.dir.south")).drawForeground(
                par1,
                par2
        );
        new AdvArea(this, 76 + 3, 98 + 3, 87 + 3, 109 + 3).withTooltip(Localization.translate("iu.dir.bottom")).drawForeground(
                par1,
                par2
        );


    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        final byte dir = ModUtils.nbt(this.itemStack).getByte("dir");
        if (dir == 0) {
            this.drawTexturedModalRect(this.guiLeft + 75 + 4, this.guiTop + 61 + 4, 209, 49, 14, 14);
        }


        if (dir == 1) {
            this.drawTexturedModalRect(this.guiLeft + 75 + 4, this.guiTop + 97 + 4, 209, 85, 14, 14);
        }
        if (dir == 2) {
            this.drawTexturedModalRect(this.guiLeft + 75 + 4, this.guiTop + 25 + 4, 209, 13, 14, 14);
        }

        if (dir == 3) {
            this.drawTexturedModalRect(this.guiLeft + 75 + 4, this.guiTop + 41 + 4, 209, 29, 14, 18);
        }
        if (dir == 4) {
            this.drawTexturedModalRect(this.guiLeft + 75 + 4, this.guiTop + 77 + 4, 209, 65, 14, 18);
        }

        if (dir == 5) {
            this.drawTexturedModalRect(this.guiLeft + 55 + 4, this.guiTop + 61 + 4, 189, 49, 18, 14);
        }
        if (dir == 6) {
            if (!fluid) {
                this.drawTexturedModalRect(this.guiLeft + 90 + 4, this.guiTop + 61 + 4, 225, 49, 18, 14);
            } else {
                this.drawTexturedModalRect(this.guiLeft + 91 + 4, this.guiTop + 61 + 4, 225, 49, 18, 14);
            }

        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = mouseX - xMin;
        int y = mouseY - yMin;

        if (x >= 76 + 4 && y >= 62 + 4 && x <= 87 + 3 && y <= 73 + 4) {
            new PacketItemStackEvent(0, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 0); //any
        }

        if (x >= 76 + 4 && y >= 42 + 4 && x <= 87 + 4 && y <= 57 + 4) {
            new PacketItemStackEvent(3, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 3); //north
        }
        if (x >= 76 + 4 && y >= 78 + 4 && x <= 87 + 4 && y <= 93 + 4) {
            new PacketItemStackEvent(4, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 4); // south
        }

        if (x >= 76 + 4 && y >= 26 + 4 && x <= 87 + 4 && y <= 37 + 4) {
            new PacketItemStackEvent(2, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 2); //top
        }

        if (x >= 76 + 4 && y >= 98 + 4 && x <= 87 + 4 && y <= 109 + 4) {
            new PacketItemStackEvent(1, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 1); // bottom
        }

        if (x >= 56 + 4 && y >= 62 + 4 && x <= 71 + 4 && y <= 73 + 4) {
            new PacketItemStackEvent(5, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 5); //west
        }

        if (x >= 92 + 4 && y >= 62 + 4 && x <= 107 + 4 && y <= 73 + 4) {
            new PacketItemStackEvent(6, container.base.player);
            ModUtils.nbt(this.itemStack).setByte("dir", (byte) 6); // east
        }


    }

    protected ResourceLocation getTexture() {
        if (fluid) {
            return background1;
        }
        return background;
    }

}
