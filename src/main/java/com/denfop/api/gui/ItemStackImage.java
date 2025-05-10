package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ItemStackImage extends GuiElement<ItemStackImage> {

    private final Supplier<ItemStack> itemSupplier;

    public ItemStackImage(GuiCore<?> gui, int x, int y, Supplier<ItemStack> itemSupplier) {
        super(gui, x, y, 16, 16);
        this.itemSupplier = itemSupplier;
    }

    public void drawBackground(PoseStack poseStack, int mouseX, int mouseY) {
        if (!visible()) {
            return;
        }
        super.drawBackground(poseStack, mouseX, mouseY);
        ItemStack stack = this.itemSupplier.get();
        if (!ModUtils.isEmpty(stack)) {
            RenderSystem.enableBlend();
            this.gui.drawItemStack(this.x, this.y, stack);
            RenderSystem.disableBlend();
        }

    }

    public void drawForeground(PoseStack poseStack, int mouseX, int mouseY) {
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
