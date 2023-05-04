package com.denfop.api.gui;

import com.denfop.gui.GuiIC2;
import com.google.common.base.Supplier;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ItemImage extends GuiElement<ItemImage> {

    private final Supplier<ItemStack> itemSupplier;

    public ItemImage(GuiIC2<?> gui, int x, int y, Supplier<ItemStack> itemSupplier) {
        super(gui, x, y, 16, 16);
        this.itemSupplier = itemSupplier;
    }

    public void drawBackground(int mouseX, int mouseY) {
        super.drawBackground(mouseX, mouseY);
        ItemStack stack = this.itemSupplier.get();
        if (!StackUtil.isEmpty(stack)) {
            RenderHelper.enableGUIStandardItemLighting();
            this.gui.drawItem(this.x, this.y, stack);
            RenderHelper.disableStandardItemLighting();
        }

    }

}
