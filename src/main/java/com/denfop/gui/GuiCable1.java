package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.CustomButton;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ItemImage;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerCable;
import com.denfop.tiles.transport.tiles.TileEntityItemPipes;
import com.denfop.utils.ModUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiCable1 extends GuiIU<ContainerCable> {


    private final boolean input;

    public GuiCable1(ContainerCable guiContainer) {
        super(guiContainer);
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.inventoryList))
        );
        this.componentList.clear();
        this.input = ((TileEntityItemPipes) guiContainer.base).isInput();
        this.addElement(new CustomButton(this, 79, 63, 20, 20, container.base, 10, ""));
    }


    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        final String name = Localization.translate("iu.dir." + container.facing.name().toLowerCase());
        if (!this.isBlack) {
            this.drawXCenteredString(this.xSize / 2, 4, name, 4210752, false);
        } else {
            this.drawXCenteredString(this.xSize / 2, 4, name, ModUtils.convertRGBcolorToInt(216, 216, 216), false);
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        new AdvArea(this, 111, 15, 171, 74).withTooltip(Localization.translate("iu.blacklist_tube")).drawForeground(par1, par2);
        new AdvArea(this, 4, 15, 63, 74).withTooltip(Localization.translate("iu.whitelist_tube")).drawForeground(par1, par2);
        new Area(this, 79, 63, 20, 20).withTooltip(((TileEntityItemPipes) container.base).redstoneSignal ? Localization.translate(
                "Transformer.gui" +
                        ".switch.mode1") : Localization.translate("EUStorage.gui.mod.redstone0")).drawForeground(par1, par2);


    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        new ItemImage(this, 81, 65, () -> new ItemStack(Items.REDSTONE)).drawBackground(this.guiLeft, this.guiTop);

    }

    @Override
    protected ResourceLocation getTexture() {
        if (!input) {
            return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiinputtube.png");
        } else {
            return new ResourceLocation(Constants.MOD_ID, "textures/gui/guioutputtube.png");
        }

    }

}
