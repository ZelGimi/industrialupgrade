package com.denfop.api.gui;

import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.gui.GuiCore;
import com.denfop.items.book.GUIBook;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class GuiElementMultiBlock extends GuiElement<GuiElementFluidToFluids> {

    private final EnumFacing facing;
    public int scroll;
    public int state = 0;
    public int height = 0;
    public int rotate = -45;
    private MultiBlockStructure multiblock;
    private double xTranslate;
    private float yTranslate;
    private float zTranslate;
    private float scale;

    public GuiElementMultiBlock(final GuiCore<?> gui, final int x, final int y, MultiBlockStructure multiBlockStructure) {
        super(gui, x, y, 100, 190);
        this.multiblock = multiBlockStructure;
        this.scroll = 0;
        this.facing = EnumFacing.NORTH;
    }

    @Override
    public void drawForeground(final int mouseX, final int mouseY) {


    }

    public boolean onMouseClick(int mouseX, int mouseY, MouseButton button, boolean onThis) {
        return onThis && this.onMouseClick(mouseX, mouseY, button);
    }

    protected boolean onMouseClick(int mouseX, int mouseY, MouseButton button) {
        return false;
    }

    public boolean onMouseDrag(int mouseX, int mouseY, MouseButton button, long timeFromLastClick, boolean onThis) {
        return onThis && this.onMouseDrag(mouseX, mouseY, button, timeFromLastClick);
    }

    protected boolean onMouseDrag(int mouseX, int mouseY, MouseButton button, long timeFromLastClick) {
        return false;
    }

    public boolean onMouseRelease(int mouseX, int mouseY, MouseButton button, boolean onThis) {
        return onThis && this.onMouseRelease(mouseX, mouseY, button);
    }

    protected boolean onMouseRelease(int mouseX, int mouseY, MouseButton button) {
        return false;
    }


    public void drawBackground(int mouseX, int mouseY) {
        this.multiblock = InitMultiBlockSystem.perHeatReactorMultiBlock;
        bindBlockTexture();
        int length = multiblock.getLength();
        int width = multiblock.getWeight();
        int height = multiblock.getHeight();

        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableRescaleNormal();

        // Расчёт максимального размера модели и масштаба
        float maxDimension = Math.max(Math.max(width, height), length);
        float guiMaxDimension = Math.min(gui.xSize, gui.ySize); // Использование меньшего измерения для масштабирования
        float scale = (guiMaxDimension / maxDimension) / 2f; // Разделение на 2 для уменьшения до более удобного размера

        // Центрирование модели на экране
        GlStateManager.translate(gui.guiLeft + gui.xSize / 2, gui.guiTop + gui.ySize / 2, 100);
        GlStateManager.scale(scale, -scale, scale); // Применение масштаба
        GlStateManager.rotate(rotate, 0f, 1.0F, 0.0F); // Поворот модели

        BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        Map<BlockPos, ItemStack> blocks = multiblock.ItemStackMap;
        for (Map.Entry<BlockPos, ItemStack> entry : blocks.entrySet()) {
            BlockPos pos = entry.getKey();
            ItemStack stack = entry.getValue();
            EnumFacing facing = multiblock.RotationMap.get(pos);
            if (stack.isEmpty()) {
                continue;
            }

            IBlockState state = ((BlockTileEntity) Block.getBlockFromItem(stack.getItem())).getStateFromMeta1(stack, facing);

            GlStateManager.pushMatrix();
            GlStateManager.translate((pos.getX() - width / 2), (pos.getY() - height / 2), (pos.getZ() - length / 2));
            IBakedModel model = blockRenderer.getModelForState(state);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(7, DefaultVertexFormats.BLOCK);

            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableRescaleNormal();


    }

    public void onMouseScroll(int mouseX, int mouseY, ScrollDirection direction) {
        if (direction == ScrollDirection.up) {
            rotate += 90;
        }
        if (direction == ScrollDirection.down) {
            rotate -= 90;
        }
    }

    protected int getMaxScroll() {
        return this.multiblock.height;
    }

}
