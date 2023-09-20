package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerBlockLimiter;
import com.denfop.container.ContainerCable;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.transport.tiles.TileEntityItemPipes;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiCable1 extends GuiIU<ContainerCable> {


    public GuiCable1(ContainerCable guiContainer) {
        super(guiContainer);
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.invSlotList))
        );
        this.componentList.add(slots);

    }


    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(new ItemStack(IUItem.item_pipes,1,
                ((TileEntityItemPipes)this.container.base).cableType.ordinal()).getDisplayName());
        if (!this.isBlack) {
            this.drawXCenteredString(this.xSize / 2, 6, name, 4210752, false);
        } else {
            this.drawXCenteredString(this.xSize / 2, 6, name, ModUtils.convertRGBcolorToInt(216, 216, 216), false);
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        new AdvArea(this,10,18,10+18*9,36).withTooltip(Localization.translate("iu.blacklist_tube")).drawForeground(par1, par2);
        new AdvArea(this,10,40,10+18*9,56).withTooltip(Localization.translate("iu.whitelist_tube")).drawForeground(par1, par2);


    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicable1.png");
    }

}
