package com.denfop.items.storage;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class ClientItemGridTooltipComponent implements ClientTooltipComponent {

    private static final int CELL_SIZE = 18;
    private static final int COLUMNS = 9;

    private final List<ItemGridTooltipComponent.Entry> entries;

    public ClientItemGridTooltipComponent(ItemGridTooltipComponent component) {
        this.entries = component.entries();
    }

    private static String formatCount(long value) {
        if (value >= 1_000_000_000L) return String.format("%.1fB", value / 1_000_000_000.0);
        if (value >= 1_000_000L) return String.format("%.1fM", value / 1_000_000.0);
        if (value >= 1_000L) return String.format("%.1fK", value / 1_000.0);
        return String.valueOf(value);
    }

    @Override
    public int getHeight() {
        int rows = Math.max(1, (int) Math.ceil(entries.size() / (double) COLUMNS));
        return rows * CELL_SIZE;
    }

    @Override
    public int getWidth(Font font) {
        int columns = Math.min(COLUMNS, Math.max(1, entries.size()));
        return columns * CELL_SIZE;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        for (int i = 0; i < entries.size(); i++) {
            ItemGridTooltipComponent.Entry entry = entries.get(i);

            int col = i % COLUMNS;
            int row = i / COLUMNS;

            int drawX = x + col * CELL_SIZE;
            int drawY = y + row * CELL_SIZE;

            if (entry instanceof ItemGridTooltipComponent.EntryItem itemEntry) {
                renderItemEntry(font, guiGraphics, drawX, drawY, itemEntry);
            } else if (entry instanceof ItemGridTooltipComponent.EntryFluid fluidEntry) {
                renderFluidEntry(font, guiGraphics, drawX, drawY, fluidEntry);
            }
        }
    }

    private void renderItemEntry(Font font, GuiGraphics guiGraphics, int x, int y,
                                 ItemGridTooltipComponent.EntryItem entry) {
        ItemStack stack = entry.stack();
        guiGraphics.renderItem(stack, x, y);
        guiGraphics.renderItemDecorations(font, stack, x, y, formatCount(entry.count()));
    }

    private void renderFluidEntry(Font font, GuiGraphics guiGraphics, int x, int y,
                                  ItemGridTooltipComponent.EntryFluid entry) {
        FluidStack fluidStack = entry.stack();
        if (fluidStack.isEmpty()) {
            return;
        }

        IClientFluidTypeExtensions fluidExtensions =
                IClientFluidTypeExtensions.of(fluidStack.getFluid());

        ResourceLocation stillTexture = fluidExtensions.getStillTexture(fluidStack);
        if (stillTexture == null) {
            return;
        }

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(stillTexture);

        int tint = fluidExtensions.getTintColor(fluidStack);

        int a = (tint >> 24) & 255;
        int r = (tint >> 16) & 255;
        int g = (tint >> 8) & 255;
        int b = tint & 255;

        if (a == 0) {
            a = 255;
        }

        RenderSystem.enableBlend();

        guiGraphics.setColor(r / 255f, g / 255f, b / 255f, a / 255f);
        guiGraphics.blit(x + 1, y + 1, 0, 16, 16, sprite);
        guiGraphics.setColor(1f, 1f, 1f, 1f);

        RenderSystem.disableBlend();

        String countText = formatCount(entry.count());
        guiGraphics.drawString(font, countText, x + 1, y + 10, 0xFFFFFF, true);
    }
}