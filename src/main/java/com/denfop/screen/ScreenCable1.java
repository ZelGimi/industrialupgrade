package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.blockentity.transport.tiles.BlockEntityItemPipes;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuCable;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ScreenCable1<T extends ContainerMenuCable> extends ScreenMain<ContainerMenuCable> {


    private final boolean input;

    public ScreenCable1(ContainerMenuCable guiContainer) {
        super(guiContainer);
        this.slots = new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.invSlotList))
        );
        this.componentList.clear();
        this.input = ((BlockEntityItemPipes) guiContainer.base).isInput();
        this.addWidget(new ButtonWidget(this, 79, 63, 20, 20, container.base, 10, ""));
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
        new AdvancedTooltipWidget(this, 111, 15, 171, 74).withTooltip(Localization.translate("iu.blacklist_tube")).drawForeground(poseStack, par1, par2);
        new AdvancedTooltipWidget(this, 4, 15, 63, 74).withTooltip(Localization.translate("iu.whitelist_tube")).drawForeground(poseStack, par1, par2);
        new TooltipWidget(this, 79, 63, 20, 20).withTooltip(((BlockEntityItemPipes) container.base).redstoneSignal ? Localization.translate(
                "Transformer.gui" +
                        ".switch.mode1") : Localization.translate("EUStorage.gui.mod.redstone0")).drawForeground(poseStack, par1, par2);


    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        new ItemWidget(this, 81, 65, () -> new ItemStack(Items.REDSTONE)).drawBackground(poseStack, this.guiLeft, this.guiTop);

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
