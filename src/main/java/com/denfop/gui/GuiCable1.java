package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerCable;
import com.denfop.tiles.transport.tiles.TileEntityItemPipes;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GuiCable1<T extends ContainerCable> extends GuiIU<ContainerCable> {


    private final boolean input;

    public GuiCable1(ContainerCable guiContainer) {
        super(guiContainer);
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.invSlotList))
        );
        this.componentList.clear();
        this.input = ((TileEntityItemPipes) guiContainer.base).isInput();
        this.addElement(new CustomButton(this, 79, 63, 20, 20, container.base, 10, ""));
    }


    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        final String name = Localization.translate("iu.dir." + container.facing.name().toLowerCase());
        if (!this.isBlack) {
            this.drawXCenteredString(poseStack, this.imageWidth / 2, 4, net.minecraft.network.chat.Component.nullToEmpty(name), 4210752, false);
        } else {
            this.drawXCenteredString(poseStack, this.imageWidth / 2, 4, net.minecraft.network.chat.Component.nullToEmpty(name), ModUtils.convertRGBcolorToInt(216, 216, 216), false);
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        new AdvArea(this, 111, 15, 171, 74).withTooltip(Localization.translate("iu.blacklist_tube")).drawForeground(poseStack, par1, par2);
        new AdvArea(this, 4, 15, 63, 74).withTooltip(Localization.translate("iu.whitelist_tube")).drawForeground(poseStack, par1, par2);
        new Area(this, 79, 63, 20, 20).withTooltip(((TileEntityItemPipes) container.base).redstoneSignal ? Localization.translate(
                "Transformer.gui" +
                        ".switch.mode1") : Localization.translate("EUStorage.gui.mod.redstone0")).drawForeground(poseStack, par1, par2);


    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        new ItemImage(this, 81, 65, () -> new ItemStack(Items.REDSTONE)).drawBackground(poseStack, this.guiLeft, this.guiTop);

    }

    @Override
    protected ResourceLocation getTexture() {
        if (!input) {
            return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiinputtube.png");
        } else {
            return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guioutputtube.png");
        }

    }

}
