package com.denfop.items.storage;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ClientItemGridTooltipComponent implements ClientTooltipComponent {

    private static final int CELL_SIZE = 18;
    private static final int COLUMNS = 9;
    private static final int ICON_SIZE = 16;

    private final List<ItemGridTooltipComponent.Entry> entries;

    public ClientItemGridTooltipComponent(ItemGridTooltipComponent component) {
        this.entries = component.entries();
    }

    private static void drawTintedSprite(PoseStack poseStack,
                                         TextureAtlasSprite sprite,
                                         int x,
                                         int y,
                                         int z,
                                         int width,
                                         int height,
                                         float red,
                                         float green,
                                         float blue,
                                         float alpha) {
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        bufferBuilder.vertex(poseStack.last().pose(), x, y + height, z)
                .uv(u0, v1)
                .color(red, green, blue, alpha)
                .endVertex();

        bufferBuilder.vertex(poseStack.last().pose(), x + width, y + height, z)
                .uv(u1, v1)
                .color(red, green, blue, alpha)
                .endVertex();

        bufferBuilder.vertex(poseStack.last().pose(), x + width, y, z)
                .uv(u1, v0)
                .color(red, green, blue, alpha)
                .endVertex();

        bufferBuilder.vertex(poseStack.last().pose(), x, y, z)
                .uv(u0, v0)
                .color(red, green, blue, alpha)
                .endVertex();

        BufferUploader.drawWithShader(bufferBuilder.end());
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
    public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        for (int i = 0; i < entries.size(); i++) {
            ItemGridTooltipComponent.Entry entry = entries.get(i);

            int col = i % COLUMNS;
            int row = i / COLUMNS;

            int drawX = x + col * CELL_SIZE;
            int drawY = y + row * CELL_SIZE;

            if (entry instanceof ItemGridTooltipComponent.EntryItem itemEntry) {
                renderItemEntry(font, poseStack, itemRenderer, drawX, drawY, itemEntry);
            } else if (entry instanceof ItemGridTooltipComponent.EntryFluid fluidEntry) {
                renderFluidEntry(font, poseStack, drawX, drawY, fluidEntry, blitOffset);
            }
        }
    }

    private void renderItemEntry(Font font, PoseStack poseStack, ItemRenderer itemRenderer, int x, int y,
                                 ItemGridTooltipComponent.EntryItem entry) {
        ItemStack stack = entry.stack();

        itemRenderer.renderAndDecorateItem(stack, x + 1, y + 1);
        itemRenderer.renderGuiItemDecorations(font, stack, x + 1, y + 1, formatCount(entry.count()));
    }

    private void renderFluidEntry(Font font, PoseStack poseStack, int x, int y,
                                  ItemGridTooltipComponent.EntryFluid entry, int blitOffset) {
        FluidStack fluidStack = entry.stack();
        if (fluidStack.isEmpty()) {
            return;
        }

        IClientFluidTypeExtensions fluidExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
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

        GuiComponent.fill(poseStack, x, y, x + CELL_SIZE - 1, y + CELL_SIZE - 1, 0x33000000);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        drawTintedSprite(
                poseStack,
                sprite,
                x + 1,
                y + 1,
                blitOffset,
                ICON_SIZE,
                ICON_SIZE,
                r / 255.0F,
                g / 255.0F,
                b / 255.0F,
                a / 255.0F
        );

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        String countText = formatCount(entry.count());
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, blitOffset + 200.0D);
        font.drawShadow(poseStack, countText, x + 1, y + 10, 0xFFFFFF);
        poseStack.popPose();
    }
}