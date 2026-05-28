package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.storage.TypeComponent;
import com.denfop.api.storage.TypeSlot;
import com.denfop.api.widget.FluidDefaultWidget;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerBus;
import com.denfop.containermenu.SlotVirtual;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScreenBus<T extends ContainerBus> extends ScreenMain<ContainerBus> {

    private static final int REDSTONE_X = 117;
    private static final int REDSTONE_Y = 25;

    private static final int MODE_X = 117;
    private static final int MODE_Y = 51;

    private static final int BUTTON_W = 20;
    private static final int BUTTON_H = 22;

    private boolean hoverRedstone = false;
    private boolean hoverComponent = false;

    public ScreenBus(ContainerBus guiContainer) {
        super(guiContainer, EnumTypeStyle.STORAGE);
        this.componentList.clear();
        this.imageHeight = 173;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (hoverRedstone) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if (hoverComponent) {
            new PacketUpdateServerTile(this.container.base, 1);
        }
    }

    @Override
    public boolean canRenderSlot(Slot slot) {
        return !(slot instanceof SlotVirtual && container.base.listSlot.isFluid());
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(guiGraphics, mouseX, mouseY);

        updateHoverState(mouseX, mouseY);

        if (hoverRedstone) {
            this.drawTooltip(mouseX, mouseY, getRedstoneTooltip());
        } else if (hoverComponent) {
            this.drawTooltip(mouseX, mouseY, getModeTooltip());
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(guiGraphics, partialTicks, mouseX, mouseY);


        updateHoverState(mouseX - this.guiLeft, mouseY - this.guiTop);

        if (this.container.base.isFluid()) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = this.container.base.listSlot.get(i);
                if (!stack.isEmpty()) {
                    if (stack.getItem() != IUItem.reinforcedFluidCell.getItem()) {
                        FluidStack fluidStack = FluidHandlerFix.getFluidHandler(stack)
                                .drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);

                        stack = IUItem.reinforcedFluidCell.getItemStack();
                        fluidStack.setAmount(1);
                        FluidHandlerFix.getFluidHandler(stack)
                                .fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        stack.set(DataComponentsInit.TYPE_RECIPE, true);
                        this.container.base.setItem(i, stack);
                    }

                    FluidStack fluidStack = FluidHandlerFix.getFluidHandler(stack)
                            .drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);

                    if (!fluidStack.isEmpty()) {
                        new FluidDefaultWidget(
                                this,
                                61 + (i % 3) * 18,
                                22 + (i / 3) * 18,
                                fluidStack
                        ).drawBackground(guiGraphics, guiLeft, guiTop);
                    }
                }
            }
        }

        if (this.container.base.slotMode == TypeSlot.RANDOM) {
            drawTexturedModalRect(guiGraphics, this.guiLeft + MODE_X, this.guiTop + MODE_Y, 235, 24, BUTTON_W, BUTTON_H);
        }

        if (this.container.base.componentMode == TypeComponent.IGNORE) {
            drawTexturedModalRect(guiGraphics, this.guiLeft + MODE_X, this.guiTop + MODE_Y, 235, 24, BUTTON_W, BUTTON_H);
        }

        switch (this.container.base.redstoneMode) {
            case FULL ->
                    drawTexturedModalRect(guiGraphics, this.guiLeft + REDSTONE_X, this.guiTop + REDSTONE_Y, 214, 47, BUTTON_W, BUTTON_H);
            case HALF ->
                    drawTexturedModalRect(guiGraphics, this.guiLeft + REDSTONE_X, this.guiTop + REDSTONE_Y, 214, 24, BUTTON_W, BUTTON_H);
        }

        if (hoverRedstone) {
            drawTexturedModalRect(guiGraphics, this.guiLeft + REDSTONE_X, this.guiTop + REDSTONE_Y, 235, 47, BUTTON_W, BUTTON_H);
        }

        if (hoverComponent) {
            drawTexturedModalRect(guiGraphics, this.guiLeft + MODE_X, this.guiTop + MODE_Y, 235, 47, BUTTON_W, BUTTON_H);
        }
    }

    private void updateHoverState(final int mouseX, final int mouseY) {
        this.hoverRedstone = isPointInArea(mouseX, mouseY, REDSTONE_X, REDSTONE_Y, BUTTON_W, BUTTON_H);
        this.hoverComponent = isPointInArea(mouseX, mouseY, MODE_X, MODE_Y, BUTTON_W, BUTTON_H);
    }

    private boolean isPointInArea(final int mouseX, final int mouseY, final int x, final int y, final int width, final int height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    private List<String> getRedstoneTooltip() {
        List<String> lines = new ArrayList<>();
        lines.add(tr("iu.bus.tooltip.redstone_mode") + ": " + enumText("iu.bus.redstone_mode", this.container.base.redstoneMode));

        return lines;
    }

    private List<String> getModeTooltip() {
        List<String> lines = new ArrayList<>();


        if (isExport()) {
            lines.add(tr("iu.bus.tooltip.slot_mode") + ": " + enumText("iu.bus.slot_mode", this.container.base.slotMode));
        } else {
            lines.add(tr("iu.bus.tooltip.component_mode") + ": " + enumText("iu.bus.component_mode", this.container.base.componentMode));
        }


        return lines;
    }

    private boolean isExport() {
        return !this.container.base.isImport();
    }


    private String enumText(final String baseKey, final Enum<?> value) {
        if (value == null) {
            return tr(baseKey + ".none");
        }

        String key = baseKey + "." + value.name().toLowerCase(Locale.ROOT);
        String translated = tr(key);

        return translated.equals(key) ? value.name() : translated;
    }

    private String tr(final String key) {
        return Localization.translate(key);
    }

    @Override
    protected ResourceLocation getTexture() {
        if (container.base.isImport()) {
            return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiimport.png");
        } else {
            return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiexport.png");
        }
    }
}