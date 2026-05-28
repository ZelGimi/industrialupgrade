package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.widget.*;
import com.denfop.blockentity.transport.tiles.BlockEntityItemPipes;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuCable;
import com.denfop.containermenu.SlotInfo;
import com.denfop.containermenu.SlotVirtual;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class ScreenCable1<T extends ContainerMenuCable> extends ScreenMain<ContainerMenuCable> {


    private final boolean input;

    public ScreenCable1(ContainerMenuCable guiContainer) {
        super(guiContainer);
        this.slots = new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.inventoryList))
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
        boolean tooltipWhite = true;
        boolean tooltipBlack = true;

        for (int i = 36; i < this.container.slots.size(); i++) {
            int index = i - 36;
            Slot slot = this.container.slots.get(i);

            boolean hovered = par1 >= slot.x && par1 <= slot.x + 18
                    && par2 >= slot.y && par2 <= slot.y + 18;
            if (!slot.hasItem())
                continue;
            if (index < 9 && hovered) {
                tooltipBlack = false;
            }
            if (index >= 9 && hovered) {
                tooltipWhite = false;
            }
        }
        if (tooltipBlack)
            new AdvancedTooltipWidget(this, 111, 15, 171, 74).withTooltip(Localization.translate("iu.blacklist_tube")).drawForeground(poseStack, par1, par2);
        if (tooltipWhite)
            new AdvancedTooltipWidget(this, 4, 15, 63, 74).withTooltip(Localization.translate("iu.whitelist_tube")).drawForeground(poseStack, par1, par2);
        new TooltipWidget(this, 79, 63, 20, 20).withTooltip(((BlockEntityItemPipes) container.base).redstoneSignal ? Localization.translate(
                "Transformer.gui" +
                        ".switch.mode1") : Localization.translate("EUStorage.gui.mod.redstone0")).drawForeground(poseStack, par1, par2);


    }

    @Override
    public boolean canRenderSlot(Slot slot) {
        return !(slot instanceof SlotVirtual && !((BlockEntityItemPipes) this.container.base).cableType.isItem());
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        new ItemWidget(this, 81, 65, () -> new ItemStack(Items.REDSTONE)).drawBackground(poseStack, this.guiLeft, this.guiTop);
        SlotInfo slotInfo = ((BlockEntityItemPipes) this.container.base).getInfoSlotFromFacing(this.container.facing);

        if (slotInfo != null && !((BlockEntityItemPipes) this.container.base).cableType.isItem())
            for (int i = 0; i < slotInfo.size(); i++) {
                ItemStack stack = slotInfo.get(i);
                if (!stack.isEmpty()) {
                    if (stack.getItem() != IUItem.reinforcedFluidCell.getItem()) {
                        FluidStack fluidStack = FluidHandlerFix.getFluidHandler(stack).drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                        stack = IUItem.reinforcedFluidCell.getItemStack();
                        fluidStack.setAmount(1);
                        FluidHandlerFix.getFluidHandler(stack).fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        stack.set(DataComponentsInit.TYPE_RECIPE, true);
                        slotInfo.setItem(i, stack);

                    }
                    FluidStack fluidStack = FluidHandlerFix.getFluidHandler(stack).drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                    if (!fluidStack.isEmpty()) {
                        if (i < 9)
                            new FluidDefaultWidget(this, 115 + (i % 3) * 18,
                                    18 + (i / 3) * 18, fluidStack).drawBackground(poseStack, guiLeft, guiTop);
                        else
                            new FluidDefaultWidget(this, 7 + ((i - 9) % 3) * 18,
                                    18 + ((i - 9) / 3) * 18, fluidStack).drawBackground(poseStack, guiLeft, guiTop);

                    }
                }
            }
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
