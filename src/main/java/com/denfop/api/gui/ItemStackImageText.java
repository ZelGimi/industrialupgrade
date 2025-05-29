package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ItemStackImageText extends GuiElement<ItemStackImageText> {

    private final Supplier<ItemStack> itemSupplier;
    private final String text;

    public ItemStackImageText(GuiCore<?> gui, int x, int y, Supplier<ItemStack> itemSupplier, String text) {
        super(gui, x, y, 16, 16);
        this.itemSupplier = itemSupplier;
        this.text = text;
    }

    public void drawBackground(GuiGraphics poseStack, int mouseX, int mouseY) {
        super.drawBackground(poseStack, mouseX, mouseY);
        ItemStack stack = this.itemSupplier.get();
        if (!ModUtils.isEmpty(stack)) {
            this.gui.drawItemStack(poseStack, this.x, this.y, stack);
        }

    }

    public void drawForeground(GuiGraphics poseStack, int mouseX, int mouseY) {
        if (this.contains(mouseX, mouseY)) {
            ItemStack stack = this.itemSupplier.get();
            if (!ModUtils.isEmpty(stack)) {
                String[] lines = text.split("\n");
                List<String> stringList = new ArrayList<>(Arrays.asList(lines));
                this.gui.drawTooltipOnlyName(poseStack, mouseX, mouseY, stack, stringList);
            }
        }

    }

}
