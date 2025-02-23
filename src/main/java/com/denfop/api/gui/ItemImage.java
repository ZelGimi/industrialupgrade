package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import com.denfop.utils.ModUtils;
import com.google.common.base.Supplier;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ItemImage extends GuiElement<ItemImage> {

    private final Supplier<ItemStack> itemSupplier;

    public ItemImage(GuiCore<?> gui, int x, int y, Supplier<ItemStack> itemSupplier) {
        super(gui, x, y, 16, 16);
        this.itemSupplier = itemSupplier;
    }

    public void drawBackground(int mouseX, int mouseY) {
        super.drawBackground(mouseX, mouseY);
        ItemStack stack = this.itemSupplier.get();
        if (!ModUtils.isEmpty(stack)) {
            RenderHelper.enableGUIStandardItemLighting();
            this.gui.drawItem(this.x, this.y, stack);
            RenderHelper.disableStandardItemLighting();
        }

    }

}
