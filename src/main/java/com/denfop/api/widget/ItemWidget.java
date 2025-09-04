package com.denfop.api.widget;

import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.ModUtils;
import com.google.common.base.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;

public class ItemWidget extends ScreenWidget {

    private final Supplier<ItemStack> itemSupplier;

    public ItemWidget(ScreenIndustrialUpgrade<?> gui, int x, int y, Supplier<ItemStack> itemSupplier) {
        super(gui, x, y, 16, 16);
        this.itemSupplier = itemSupplier;
    }

    @Override
    public void drawBackground(PoseStack poseStack, int mouseX, int mouseY) {
        super.drawBackground(poseStack, mouseX, mouseY);
        ItemStack stack = this.itemSupplier.get();
        if (!ModUtils.isEmpty(stack)) {
            RenderSystem.enableBlend();
            this.gui.drawItem(mouseX+this.x,mouseY+ this.y, stack);
            RenderSystem.disableBlend();
        }
    }
}
