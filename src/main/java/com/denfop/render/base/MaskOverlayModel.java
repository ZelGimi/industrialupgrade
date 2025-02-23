package com.denfop.render.base;

import com.denfop.render.fluidcell.ImageSize;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public abstract class MaskOverlayModel extends AbstractModel {

    private final ResourceLocation baseModelLocation;
    private final ResourceLocation maskTextureLocation;
    private final boolean scaleOverlay;
    private final float offset;
    private IBakedModel bakedModel;
    private MergedItemModel mergedModel;
    private float uS;
    private float vS;
    private float uE;
    private float vE;
    private ThreadLocal<MergedItemModel> currentMergedModel = ThreadLocal.withInitial(() -> MaskOverlayModel.this.mergedModel.copy());

    protected MaskOverlayModel(
            ResourceLocation baseModelLocation,
            ResourceLocation maskTextureLocation,
            boolean scaleOverlay,
            float offset
    ) {
        this.baseModelLocation = baseModelLocation;
        this.maskTextureLocation = maskTextureLocation;
        this.scaleOverlay = scaleOverlay;
        this.offset = offset;
    }

    private static BitSet readMask(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BitSet ret = new BitSet(width * height);

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int alpha = img.getRGB(x, y) >>> 24;
                if (alpha > 128) {
                    ret.set(y * width + x);
                }
            }
        }

        return ret;
    }

    private static List<ImageSize> searchAreas(BitSet pixels, int width) {
        List<ImageSize> ret = new ArrayList();

        int areaWidth;
        for (int idx = 0; (idx = pixels.nextSetBit(idx)) != -1; idx += areaWidth) {
            int y = idx / width;
            int x = idx - y * width;
            areaWidth = Math.min(width - x, pixels.nextClearBit(idx + 1) - idx);
            int areaHeight = 1;

            for (int nextLineIdx = idx + width; pixels.get(nextLineIdx) && pixels.nextClearBit(nextLineIdx + 1) >= nextLineIdx + areaWidth; nextLineIdx += width) {
                pixels.clear(nextLineIdx, nextLineIdx + areaWidth);
                ++areaHeight;
            }

            ret.add(new ImageSize(x, y, areaWidth, areaHeight));
        }

        return ret;
    }

    private static void generateQuads(List<ImageSize> areas, int width, int height, float offset, int tint, List<BakedQuad> out) {
        assert tint == -1;

        float zF = (7.5F - offset) / 16.0F;
        float zB = (8.5F + offset) / 16.0F;
        IntBuffer buffer = ModelCuboidUtil.getQuadBuffer();

        for (ImageSize area : areas) {
            float xS = (float) area.x / (float) width;
            float yS = 1.0F - (float) area.y / (float) height;
            float xE = (float) (area.x + area.width) / (float) width;
            float yE = 1.0F - (float) (area.y + area.height) / (float) height;
            ModelCuboidUtil.generateVertex(xS, yS, zF, -1, 0.0F, 0.0F, EnumFacing.SOUTH, buffer);
            ModelCuboidUtil.generateVertex(xE, yS, zF, -1, 1.0F, 0.0F, EnumFacing.SOUTH, buffer);
            ModelCuboidUtil.generateVertex(xE, yE, zF, -1, 1.0F, 1.0F, EnumFacing.SOUTH, buffer);
            ModelCuboidUtil.generateVertex(xS, yE, zF, -1, 0.0F, 1.0F, EnumFacing.SOUTH, buffer);
            out.add(new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.SOUTH, null, true,
                    ModelCuboidUtil.vertexFormat
            ));
            buffer.rewind();


            ModelCuboidUtil.generateVertex(xS, yS, zB, -1, 0.0F, 0.0F, EnumFacing.NORTH, buffer);
            ModelCuboidUtil.generateVertex(xS, yE, zB, -1, 0.0F, 1.0F, EnumFacing.NORTH, buffer);
            ModelCuboidUtil.generateVertex(xE, yE, zB, -1, 1.0F, 1.0F, EnumFacing.NORTH, buffer);
            ModelCuboidUtil.generateVertex(xE, yS, zB, -1, 1.0F, 0.0F, EnumFacing.NORTH, buffer);
            out.add(new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.NORTH, null, true,
                    ModelCuboidUtil.vertexFormat
            ));
            buffer.rewind();
        }

    }

    public Collection<ResourceLocation> getDependencies() {
        return Arrays.asList(this.baseModelLocation);
    }

    public IBakedModel bake(
            IModelState state,
            VertexFormat format,
            Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter
    ) {
        IModel baseModel;
        BufferedImage img;
        try {
            baseModel = ModelLoaderRegistry.getModel(this.baseModelLocation);
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(this.maskTextureLocation);
            img = TextureUtil.readBufferedImage(resource.getInputStream());
        } catch (Exception var12) {
            throw new RuntimeException(var12);
        }

        int width = img.getWidth();
        int height = img.getHeight();
        List<ImageSize> areas = searchAreas(readMask(img), width);
        this.bakedModel = baseModel.bake(baseModel.getDefaultState(), format, bakedTextureGetter);
        List<BakedQuad> origQuads = this.bakedModel.getQuads((IBlockState) null, (EnumFacing) null, 0L);
        int retextureStart = origQuads.size();
        List<BakedQuad> mergedQuads = new ArrayList(retextureStart + areas.size() * 2);
        mergedQuads.addAll(origQuads);
        generateQuads(areas, width, height, this.offset, -1, mergedQuads);
        this.calculateUV(areas, width, height);
        this.mergedModel = new MergedItemModel(this.bakedModel, mergedQuads, retextureStart, areas.size() * 2);
        return this;
    }

    protected IBakedModel get() {
        return this.bakedModel;
    }

    protected IBakedModel get(TextureAtlasSprite overlay, int colorMultiplier) {
        if (overlay == null) {
            throw new NullPointerException();
        } else {
            MergedItemModel ret = (MergedItemModel) this.currentMergedModel.get();
            if (this.scaleOverlay) {
                ret.setSprite(overlay, colorMultiplier, this.uS, this.vS, this.uE, this.vE);
            } else {
                ret.setSprite(overlay, colorMultiplier, 0.0F, 0.0F, 1.0F, 1.0F);
            }

            return ret;
        }
    }

    protected IBakedModel get(float[] uvs, int[] colorMultipliers) {
        if (uvs == null) {
            throw new NullPointerException();
        } else if (uvs.length == 0) {
            return this.get();
        } else if (uvs.length % 4 != 0) {
            throw new IllegalArgumentException("invalid uv array");
        } else {
            MergedItemModel ret = this.currentMergedModel.get();
            if (this.scaleOverlay) {
                ret.setSprite(uvs, colorMultipliers, this.uS, this.vS, this.uE, this.vE);
            } else {
                ret.setSprite(uvs, colorMultipliers, 0.0F, 0.0F, 1.0F, 1.0F);
            }

            return ret;
        }
    }

    private void calculateUV(List<ImageSize> areas, int width, int height) {
        if (this.scaleOverlay) {
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;

            for (ImageSize area : areas) {
                if (area.x < minX) {
                    minX = area.x;
                }

                if (area.y < minY) {
                    minY = area.y;
                }

                if (area.x + area.width > maxX) {
                    maxX = area.x + area.width;
                }

                if (area.y + area.height > maxY) {
                    maxY = area.y + area.height;
                }
            }

            this.uS = (float) minX / (float) width;
            this.vS = (float) minY / (float) height;
            this.uE = (float) maxX / (float) width;
            this.vE = (float) maxY / (float) height;
        }
    }

    public void onReload() {
        this.currentMergedModel = ThreadLocal.withInitial(() -> MaskOverlayModel.this.mergedModel.copy());
    }


}
