package com.denfop.api.gui;

import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.gui.GuiCore;
import com.denfop.items.book.GUIBook;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class GuiElementMultiBlock extends GuiElement<GuiElementFluidToFluids> {

    private final MultiBlockStructure multiblock;
    private final EnumFacing facing;
    public int scroll;
    public int state = 0;
    public int height = 0;
    public int rotate = -45;
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
        bindCommonTexture1();
        final Map<BlockPos, ItemStack> map =multiblock.ItemStackMap;
        int structureLength =multiblock.getLength();
        int structureWidth = multiblock.getWeight();
        int structureHeight = multiblock.getHeight();
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();

        float scale = 100.0F;
        float sx = 1F / Math.min(structureWidth, structureLength);
        float sy = (0.93F) / structureHeight;
        scale *= Math.min(sx, sy);
        BlockRendererDispatcher blockRender = Minecraft.getMinecraft().getBlockRendererDispatcher();
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        GlStateManager.translate(
                this.gui.guiLeft + this.x + scale,
                this.gui.guiTop + this.y + scale * structureHeight,
                zTranslate
        );

        GlStateManager.scale(scale, -scale, 1.0F);
        GlStateManager.rotate(25, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0, 1, 0);
        // scale 20 25 33
        double tempScale = 25 / scale;
        double devRotate = rotate % 360;
        if(structureLength > 3 || structureWidth > 3) {
            final double devRotate1 = Math.abs(devRotate);
            GlStateManager.rotate((float) devRotate1, 0.0F, 0.2F, 0.0F);
        }else{
            GlStateManager.rotate((float) devRotate, 0.0F, 0.2F, 0.0F);
        }
        GlStateManager.scale(0.9, 0.9, 1);

        if (devRotate == 315 || devRotate == -45) {
            structureWidth = Math.max(structureLength, structureWidth);
            GlStateManager.translate(
                    2.5 - Math.max(2 * (structureHeight - 3),0),
                    (structureHeight - 2) - 1.25 * (structureHeight - 3) * tempScale,
                    -(structureHeight - 3)
            );
        }
        if (devRotate == -315 || devRotate == 45) {
            GlStateManager.translate(-0.5, -0.1, (structureHeight - 3));
        }
        if (devRotate == -135 || devRotate == 225) {
            structureLength = Math.max(structureLength, structureWidth);
            GlStateManager.translate(
                    -0.5 + ((structureLength - 3) * tempScale),
                    -0.1 + 0.725 * (structureLength - 3) * tempScale,
                    -structureLength * tempScale
            );
        }
        if (devRotate == 135 || devRotate == -225) {
            structureWidth = Math.max(structureLength, structureWidth);
            GlStateManager.translate(-structureWidth * tempScale, (0.625 + 0.725 * (structureWidth - 3) * tempScale),
                    (-2.5 - ((structureWidth - 3)) * tempScale)
            );
        }
        GlStateManager.disableLighting();
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(7425);
        } else {
            GlStateManager.shadeModel(7424);
        }

        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        for (Map.Entry<BlockPos, ItemStack> itemStackBlockPosEntry : map.entrySet()) {
            IBlockState state;
            EnumFacing facing = multiblock.RotationMap.get(itemStackBlockPosEntry.getKey());
            if (facing == EnumFacing.WEST || facing == EnumFacing.EAST) {
                facing = facing.getOpposite();
            }
            state =
                    ((BlockTileEntity) Block.getBlockFromItem(itemStackBlockPosEntry.getValue().getItem())).getStateFromMeta1(
                            itemStackBlockPosEntry.getValue(),
                            facing
                    );
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(7, DefaultVertexFormats.BLOCK);
            IBakedModel model = blockRender.getModelForState(state);
            blockRender.getBlockModelRenderer().renderModel(((GUIBook) this.gui).player.world, model, state,
                    itemStackBlockPosEntry.getKey(),
                    buffer, true
            );
            tessellator.draw();

        }
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableBlend();
        RenderHelper.disableStandardItemLighting();
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
