package com.denfop.api.widget;

import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ItemStackWidget extends ScreenWidget {

    private final Supplier<ItemStack> itemSupplier;

    public ItemStackWidget(ScreenIndustrialUpgrade<?> gui, int x, int y, Supplier<ItemStack> itemSupplier) {
        super(gui, x, y, 16, 16);
        this.itemSupplier = itemSupplier;
    }

    public void drawBackground(GuiGraphics poseStack, int mouseX, int mouseY) {
        if (!visible()) {
            return;
        }
        super.drawBackground(poseStack, mouseX, mouseY);
        ItemStack stack = this.itemSupplier.get();
        if (!ModUtils.isEmpty(stack)) {
            RenderSystem.enableBlend();
            this.gui.drawItemStack(poseStack, this.x, this.y, stack);
            RenderSystem.disableBlend();
        }

    }

    public void drawForeground(GuiGraphics poseStack, int mouseX, int mouseY) {
        if (!visible()) {
            return;
        }
        if (this.contains(mouseX, mouseY)) {
            ItemStack stack = this.itemSupplier.get();
            if (!ModUtils.isEmpty(stack)) {
                this.gui.drawTooltip(poseStack, mouseX, mouseY, stack);
            }
        }

    }

}
