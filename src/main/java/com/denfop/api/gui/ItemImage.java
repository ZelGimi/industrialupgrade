package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import com.denfop.utils.ModUtils;
import com.google.common.base.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;

public class ItemImage extends GuiElement<ItemImage> {

    private final Supplier<ItemStack> itemSupplier;

    public ItemImage(GuiCore<?> gui, int x, int y, Supplier<ItemStack> itemSupplier) {
        super(gui, x, y, 16, 16);
        this.itemSupplier = itemSupplier;
    }

    @Override
    public void drawBackground(PoseStack poseStack, int mouseX, int mouseY) {
        super.drawBackground(poseStack, mouseX, mouseY);
        ItemStack stack = this.itemSupplier.get();
        if (!ModUtils.isEmpty(stack)) {
            RenderSystem.enableBlend();
            gui.drawItem(mouseX + this.x, mouseY + this.y, stack);
            RenderSystem.disableBlend();
        }
    }
}
