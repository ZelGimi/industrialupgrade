package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import com.denfop.utils.ModUtils;
import com.google.common.base.Supplier;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemStackImageText extends GuiElement<ItemStackImageText> {

    private final Supplier<ItemStack> itemSupplier;
    private final String text;

    public ItemStackImageText(GuiCore<?> gui, int x, int y, Supplier<ItemStack> itemSupplier, String text) {
        super(gui, x, y, 16, 16);
        this.itemSupplier = itemSupplier;
        this.text = text;
    }

    public void drawBackground(int mouseX, int mouseY) {
        super.drawBackground(mouseX, mouseY);
        ItemStack stack = this.itemSupplier.get();
        if (!ModUtils.isEmpty(stack)) {
            RenderHelper.enableGUIStandardItemLighting();
            this.gui.drawItemStack(this.x, this.y, stack);
            RenderHelper.disableStandardItemLighting();
        }

    }

    public void drawForeground(int mouseX, int mouseY) {
        if (this.contains(mouseX, mouseY)) {
            ItemStack stack = this.itemSupplier.get();
            if (!ModUtils.isEmpty(stack)) {
                String[] lines = text.split("\n");
                List<String> stringList = new ArrayList<>(Arrays.asList(lines));
                this.gui.drawTooltipOnlyName(mouseX, mouseY, stack, stringList);
            }
        }

    }

}
