package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import com.denfop.utils.ModUtils;
import com.google.common.base.Supplier;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ItemStackImage extends GuiElement<ItemStackImage> {

    private final Supplier<ItemStack> itemSupplier;

    public ItemStackImage(GuiCore<?> gui, int x, int y, Supplier<ItemStack> itemSupplier) {
        super(gui, x, y, 16, 16);
        this.itemSupplier = itemSupplier;
    }

    public void drawBackground(int mouseX, int mouseY) {
        if (!visible()) {
            return;
        }
        super.drawBackground(mouseX, mouseY);
        ItemStack stack = this.itemSupplier.get();
        if (!ModUtils.isEmpty(stack)) {
            RenderHelper.enableGUIStandardItemLighting();
            this.gui.drawItemStack(this.x, this.y, stack);
            RenderHelper.disableStandardItemLighting();
        }

    }

    public void drawForeground(int mouseX, int mouseY) {
        if (!visible()) {
            return;
        }
        if (this.contains(mouseX, mouseY)) {
            ItemStack stack = this.itemSupplier.get();
            if (!ModUtils.isEmpty(stack)) {
                this.gui.drawTooltip(mouseX, mouseY, stack);
            }
        }

    }

}
