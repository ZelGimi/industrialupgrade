package com.denfop.render.base;

import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ModelCuboidUtil {

    public static final VertexFormat vertexFormat;
    public static final int dataStride;
    private static final int[] faceShades;

    static {
        vertexFormat = DefaultVertexFormats.ITEM;
        dataStride = vertexFormat.getNextOffset() / 4;
        faceShades = getFaceShades();
    }

    public ModelCuboidUtil() {
    }

    public static IntBuffer getQuadBuffer() {
        return IntBuffer.allocate(4 * dataStride);
    }

    public static void addCuboid(
            float xS,
            float yS,
            float zS,
            float xE,
            float yE,
            float zE,
            Set<EnumFacing> faces,
            TextureAtlasSprite sprite,
            List<BakedQuad>[] faceQuads,
            List<BakedQuad> generalQuads
    ) {
        float spriteU = sprite.getMinU();
        float spriteV = sprite.getMinV();
        addCuboid(
                xS,
                yS,
                zS,
                xE,
                yE,
                zE,
                spriteU,
                spriteV,
                sprite.getMaxU() - spriteU,
                sprite.getMaxV() - spriteV,
                faces,
                sprite,
                faceQuads,
                generalQuads
        );
    }

    public static void addCuboid(
            float xS,
            float yS,
            float zS,
            float xE,
            float yE,
            float zE,
            int color,
            Set<EnumFacing> faces,
            TextureAtlasSprite sprite,
            List<BakedQuad>[] faceQuads,
            List<BakedQuad> generalQuads
    ) {
        float spriteU = sprite.getMinU();
        float spriteV = sprite.getMinV();
        addCuboid(
                xS,
                yS,
                zS,
                xE,
                yE,
                zE,
                color,
                spriteU,
                spriteV,
                sprite.getMaxU() - spriteU,
                sprite.getMaxV() - spriteV,
                faces,
                sprite,
                faceQuads,
                generalQuads
        );
    }

    public static void addFlippedCuboid(
            float xS,
            float yS,
            float zS,
            float xE,
            float yE,
            float zE,
            Set<EnumFacing> faces,
            TextureAtlasSprite sprite,
            List<BakedQuad>[] faceQuads,
            List<BakedQuad> generalQuads
    ) {
        float spriteU = sprite.getMaxU();
        float spriteV = sprite.getMaxV();
        addCuboid(
                xS,
                yS,
                zS,
                xE,
                yE,
                zE,
                spriteU,
                spriteV,
                sprite.getMinU() - spriteU,
                sprite.getMinV() - spriteV,
                faces,
                sprite,
                faceQuads,
                generalQuads
        );
    }

    public static void addFlippedCuboid(
            float xS,
            float yS,
            float zS,
            float xE,
            float yE,
            float zE,
            int colour,
            Set<EnumFacing> faces,
            TextureAtlasSprite sprite,
            List<BakedQuad>[] faceQuads,
            List<BakedQuad> generalQuads
    ) {
        float spriteU = sprite.getMaxU();
        float spriteV = sprite.getMaxV();
        addCuboid(
                xS,
                yS,
                zS,
                xE,
                yE,
                zE,
                colour,
                spriteU,
                spriteV,
                sprite.getMinU() - spriteU,
                sprite.getMinV() - spriteV,
                faces,
                sprite,
                faceQuads,
                generalQuads
        );
    }

    public static void addFlippedCuboidWithYOffset(
            float xS,
            float yS,
            float zS,
            float xE,
            float yE,
            float zE,
            int colour,
            Set<EnumFacing> faces,
            TextureAtlasSprite sprite,
            List<BakedQuad>[] faceQuads,
            List<BakedQuad> generalQuads,
            float offset
    ) {
        float spriteU = sprite.getMaxU();
        float spriteV = sprite.getMaxV();
        addCuboidWithYOffset(
                xS,
                yS,
                zS,
                xE,
                yE,
                zE,
                colour,
                spriteU,
                spriteV,
                sprite.getMinU() - spriteU,
                sprite.getMinV() - spriteV,
                faces,
                sprite,
                faceQuads,
                generalQuads,
                offset
        );
    }

    private static void addCuboid(
            float xS,
            float yS,
            float zS,
            float xE,
            float yE,
            float zE,
            float spriteU,
            float spriteV,
            float spriteWidth,
            float spriteHeight,
            Set<EnumFacing> faces,
            TextureAtlasSprite sprite,
            List<BakedQuad>[] faceQuads,
            List<BakedQuad> generalQuads
    ) {
        IntBuffer quadBuffer = getQuadBuffer();

        for (EnumFacing facing : faces) {
            boolean isFace;
            switch (facing) {
                case DOWN:
                    if (xS == xE || zS == zE) {
                        continue;
                    }

                    generateBlockVertex(xS, yS, zS, spriteU + spriteWidth * xS, spriteV + spriteHeight * zS, facing, quadBuffer);
                    generateBlockVertex(xE, yS, zS, spriteU + spriteWidth * xE, spriteV + spriteHeight * zS, facing, quadBuffer);
                    generateBlockVertex(xE, yS, zE, spriteU + spriteWidth * xE, spriteV + spriteHeight * zE, facing, quadBuffer);
                    generateBlockVertex(xS, yS, zE, spriteU + spriteWidth * xS, spriteV + spriteHeight * zE, facing, quadBuffer);
                    isFace = yS == 0.0F;
                    break;
                case UP:
                    if (xS == xE || zS == zE) {
                        continue;
                    }

                    generateBlockVertex(xS, yE, zS, spriteU + spriteWidth * xS, spriteV + spriteHeight * zS, facing, quadBuffer);
                    generateBlockVertex(xS, yE, zE, spriteU + spriteWidth * xS, spriteV + spriteHeight * zE, facing, quadBuffer);
                    generateBlockVertex(xE, yE, zE, spriteU + spriteWidth * xE, spriteV + spriteHeight * zE, facing, quadBuffer);
                    generateBlockVertex(xE, yE, zS, spriteU + spriteWidth * xE, spriteV + spriteHeight * zS, facing, quadBuffer);
                    isFace = yE == 1.0F;
                    break;
                case NORTH:
                    if (xS == xE || yS == yE) {
                        continue;
                    }

                    generateBlockVertex(xS, yS, zS, spriteU + spriteWidth * xS, spriteV + spriteHeight * yS, facing, quadBuffer);
                    generateBlockVertex(xS, yE, zS, spriteU + spriteWidth * xS, spriteV + spriteHeight * yE, facing, quadBuffer);
                    generateBlockVertex(xE, yE, zS, spriteU + spriteWidth * xE, spriteV + spriteHeight * yE, facing, quadBuffer);
                    generateBlockVertex(xE, yS, zS, spriteU + spriteWidth * xE, spriteV + spriteHeight * yS, facing, quadBuffer);
                    isFace = zS == 0.0F;
                    break;
                case SOUTH:
                    if (xS == xE || yS == yE) {
                        continue;
                    }

                    generateBlockVertex(xS, yS, zE, spriteU + spriteWidth * xS, spriteV + spriteHeight * yS, facing, quadBuffer);
                    generateBlockVertex(xE, yS, zE, spriteU + spriteWidth * xE, spriteV + spriteHeight * yS, facing, quadBuffer);
                    generateBlockVertex(xE, yE, zE, spriteU + spriteWidth * xE, spriteV + spriteHeight * yE, facing, quadBuffer);
                    generateBlockVertex(xS, yE, zE, spriteU + spriteWidth * xS, spriteV + spriteHeight * yE, facing, quadBuffer);
                    isFace = zE == 1.0F;
                    break;
                case WEST:
                    if (yS == yE || zS == zE) {
                        continue;
                    }

                    generateBlockVertex(xS, yS, zS, spriteU + spriteWidth * zS, spriteV + spriteHeight * yS, facing, quadBuffer);
                    generateBlockVertex(xS, yS, zE, spriteU + spriteWidth * zE, spriteV + spriteHeight * yS, facing, quadBuffer);
                    generateBlockVertex(xS, yE, zE, spriteU + spriteWidth * zE, spriteV + spriteHeight * yE, facing, quadBuffer);
                    generateBlockVertex(xS, yE, zS, spriteU + spriteWidth * zS, spriteV + spriteHeight * yE, facing, quadBuffer);
                    isFace = xS == 0.0F;
                    break;
                case EAST:
                    if (yS == yE || zS == zE) {
                        continue;
                    }

                    generateBlockVertex(xE, yS, zS, spriteU + spriteWidth * zS, spriteV + spriteHeight * yS, facing, quadBuffer);
                    generateBlockVertex(xE, yE, zS, spriteU + spriteWidth * zS, spriteV + spriteHeight * yE, facing, quadBuffer);
                    generateBlockVertex(xE, yE, zE, spriteU + spriteWidth * zE, spriteV + spriteHeight * yE, facing, quadBuffer);
                    generateBlockVertex(xE, yS, zE, spriteU + spriteWidth * zE, spriteV + spriteHeight * yS, facing, quadBuffer);
                    isFace = xE == 1.0F;
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected facing: " + facing);
            }

            if (quadBuffer.position() > 0) {
                BakedQuad quad = BakedBlockModel.createQuad(
                        Arrays.copyOf(quadBuffer.array(), quadBuffer.position()),
                        facing,
                        sprite
                );
                if (isFace) {
                    faceQuads[facing.ordinal()].add(quad);
                } else {
                    generalQuads.add(quad);
                }

                quadBuffer.rewind();
            }
        }

    }

    private static void addCuboid(
            float xS,
            float yS,
            float zS,
            float xE,
            float yE,
            float zE,
            int color,
            float spriteU,
            float spriteV,
            float spriteWidth,
            float spriteHeight,
            Set<EnumFacing> faces,
            TextureAtlasSprite sprite,
            List<BakedQuad>[] faceQuads,
            List<BakedQuad> generalQuads
    ) {
        IntBuffer quadBuffer = getQuadBuffer();

        for (EnumFacing facing : faces) {
            boolean isFace;
            switch (facing) {
                case DOWN:
                    if (xS == xE || zS == zE) {
                        continue;
                    }

                    generateBlockVertex(
                            xS,
                            yS,
                            zS,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * zS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yS,
                            zS,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * zS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yS,
                            zE,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * zE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yS,
                            zE,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * zE,
                            facing,
                            quadBuffer
                    );
                    isFace = yS == 0.0F;
                    break;
                case UP:
                    if (xS == xE || zS == zE) {
                        continue;
                    }

                    generateBlockVertex(
                            xS,
                            yE,
                            zS,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * zS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yE,
                            zE,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * zE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yE,
                            zE,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * zE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yE,
                            zS,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * zS,
                            facing,
                            quadBuffer
                    );
                    isFace = yE == 1.0F;
                    break;
                case NORTH:
                    if (xS == xE || yS == yE) {
                        continue;
                    }

                    generateBlockVertex(
                            xS,
                            yS,
                            zS,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yE,
                            zS,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yE,
                            zS,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yS,
                            zS,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    isFace = zS == 0.0F;
                    break;
                case SOUTH:
                    if (xS == xE || yS == yE) {
                        continue;
                    }

                    generateBlockVertex(
                            xS,
                            yS,
                            zE,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yS,
                            zE,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yE,
                            zE,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yE,
                            zE,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    isFace = zE == 1.0F;
                    break;
                case WEST:
                    if (yS == yE || zS == zE) {
                        continue;
                    }

                    generateBlockVertex(
                            xS,
                            yS,
                            zS,
                            color,
                            spriteU + spriteWidth * zS,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yS,
                            zE,
                            color,
                            spriteU + spriteWidth * zE,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yE,
                            zE,
                            color,
                            spriteU + spriteWidth * zE,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yE,
                            zS,
                            color,
                            spriteU + spriteWidth * zS,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    isFace = xS == 0.0F;
                    break;
                case EAST:
                    if (yS == yE || zS == zE) {
                        continue;
                    }

                    generateBlockVertex(
                            xE,
                            yS,
                            zS,
                            color,
                            spriteU + spriteWidth * zS,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yE,
                            zS,
                            color,
                            spriteU + spriteWidth * zS,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yE,
                            zE,
                            color,
                            spriteU + spriteWidth * zE,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yS,
                            zE,
                            color,
                            spriteU + spriteWidth * zE,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    isFace = xE == 1.0F;
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected facing: " + facing);
            }

            if (quadBuffer.position() > 0) {
                BakedQuad quad = BakedBlockModel.createQuad(
                        Arrays.copyOf(quadBuffer.array(), quadBuffer.position()),
                        facing,
                        sprite
                );
                if (isFace) {
                    faceQuads[facing.ordinal()].add(quad);
                } else {
                    generalQuads.add(quad);
                }

                quadBuffer.rewind();
            }
        }

    }

    public static void addCuboidWithYOffset(
            float xS,
            float yS,
            float zS,
            float xE,
            float yE,
            float zE,
            int color,
            float spriteU,
            float spriteV,
            float spriteWidth,
            float spriteHeight,
            Set<EnumFacing> faces,
            TextureAtlasSprite sprite,
            List<BakedQuad>[] faceQuads,
            List<BakedQuad> generalQuads,
            float offset
    ) {
        IntBuffer quadBuffer = getQuadBuffer();

        for (EnumFacing facing : faces) {
            boolean isFace;
            switch (facing) {
                case DOWN:
                    if (xS == xE || zS == zE) {
                        continue;
                    }

                    generateBlockVertex(
                            xS,
                            yS + offset,
                            zS,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * zS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yS + offset,
                            zS,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * zS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yS + offset,
                            zE,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * zE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yS + offset,
                            zE,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * zE,
                            facing,
                            quadBuffer
                    );
                    isFace = yS == 0.0F;
                    break;
                case UP:
                    if (xS == xE || zS == zE) {
                        continue;
                    }

                    generateBlockVertex(
                            xS,
                            yE + offset,
                            zS,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * zS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yE + offset,
                            zE,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * zE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yE + offset,
                            zE,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * zE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yE + offset,
                            zS,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * zS,
                            facing,
                            quadBuffer
                    );
                    isFace = yE == 1.0F;
                    break;
                case NORTH:
                    if (xS == xE || yS == yE) {
                        continue;
                    }

                    generateBlockVertex(
                            xS,
                            yS + offset,
                            zS,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yE + offset,
                            zS,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yE + offset,
                            zS,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yS + offset,
                            zS,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    isFace = zS == 0.0F;
                    break;
                case SOUTH:
                    if (xS == xE || yS == yE) {
                        continue;
                    }

                    generateBlockVertex(
                            xS,
                            yS + offset,
                            zE,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yS + offset,
                            zE,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yE + offset,
                            zE,
                            color,
                            spriteU + spriteWidth * xE,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yE + offset,
                            zE,
                            color,
                            spriteU + spriteWidth * xS,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    isFace = zE == 1.0F;
                    break;
                case WEST:
                    if (yS == yE || zS == zE) {
                        continue;
                    }

                    generateBlockVertex(
                            xS,
                            yS + offset,
                            zS,
                            color,
                            spriteU + spriteWidth * zS,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yS + offset,
                            zE,
                            color,
                            spriteU + spriteWidth * zE,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yE + offset,
                            zE,
                            color,
                            spriteU + spriteWidth * zE,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xS,
                            yE + offset,
                            zS,
                            color,
                            spriteU + spriteWidth * zS,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    isFace = xS == 0.0F;
                    break;
                case EAST:
                    if (yS == yE || zS == zE) {
                        continue;
                    }

                    generateBlockVertex(
                            xE,
                            yS + offset,
                            zS,
                            color,
                            spriteU + spriteWidth * zS,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yE + offset,
                            zS,
                            color,
                            spriteU + spriteWidth * zS,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yE + offset,
                            zE,
                            color,
                            spriteU + spriteWidth * zE,
                            spriteV + spriteHeight * yE,
                            facing,
                            quadBuffer
                    );
                    generateBlockVertex(
                            xE,
                            yS + offset,
                            zE,
                            color,
                            spriteU + spriteWidth * zE,
                            spriteV + spriteHeight * yS,
                            facing,
                            quadBuffer
                    );
                    isFace = xE == 1.0F;
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected facing: " + facing);
            }

            if (quadBuffer.position() > 0) {
                BakedQuad quad = BakedBlockModel.createQuad(
                        Arrays.copyOf(quadBuffer.array(), quadBuffer.position()),
                        facing,
                        sprite
                );
                if (isFace) {
                    faceQuads[facing.ordinal()].add(quad);
                } else {
                    generalQuads.add(quad);
                }

                quadBuffer.rewind();
            }
        }

    }

    public static void generateVertex(float x, float y, float z, int color, float u, float v, EnumFacing facing, IntBuffer out) {
        generateVertex(
                x,
                y,
                z,
                color,
                u,
                v,
                (float) facing.getFrontOffsetX(),
                (float) facing.getFrontOffsetY(),
                (float) facing.getFrontOffsetZ(),
                out
        );
    }

    public static void generateVertex(
            float x,
            float y,
            float z,
            int color,
            float u,
            float v,
            float nx,
            float ny,
            float nz,
            IntBuffer out
    ) {
        out.put(Float.floatToRawIntBits(x));
        out.put(Float.floatToRawIntBits(y));
        out.put(Float.floatToRawIntBits(z));
        out.put(color);
        out.put(Float.floatToRawIntBits(u));
        out.put(Float.floatToRawIntBits(v));
        out.put(packNormals(nx, ny, nz));
    }

    public static void generateBlockVertex(
            float x,
            float y,
            float z,
            int color,
            float u,
            float v,
            EnumFacing facing,
            IntBuffer out
    ) {
        generateVertex(
                x,
                y,
                z,
                color,
                u,
                v,
                (float) facing.getFrontOffsetX(),
                (float) facing.getFrontOffsetY(),
                (float) facing.getFrontOffsetZ(),
                out
        );
    }

    public static void generateBlockVertex(float x, float y, float z, float u, float v, EnumFacing facing, IntBuffer out) {
        generateVertex(
                x,
                y,
                z,
                faceShades[facing.ordinal()],
                u,
                v,
                (float) facing.getFrontOffsetX(),
                (float) facing.getFrontOffsetY(),
                (float) facing.getFrontOffsetZ(),
                out
        );
    }

    private static int packNormals(float nx, float ny, float nz) {
        return mapFloatToByte(nx) | mapFloatToByte(ny) << 8 | mapFloatToByte(nz) << 16;
    }

    private static int mapFloatToByte(float f) {
        assert f >= -1.0F && f <= 1.0F;

        return Math.round(f * 127.0F) & 255;
    }

    private static int[] getFaceShades() {
        int[] ret = new int[EnumFacing.VALUES.length];
        double[] faceBrightness = new double[]{0.5, 1.0, 0.8, 0.8, 0.6, 0.6};
        EnumFacing[] var2 = EnumFacing.VALUES;

        for (EnumFacing facing : var2) {
            int brightness = ModUtils.limit((int) (faceBrightness[facing.ordinal()] * 255.0), 0, 255);
            ret[facing.ordinal()] = -16777216 | brightness << 16 | brightness << 8 | brightness;
        }

        return ret;
    }

    public static IBakedModel getMissingModel() {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getMissingModel();
    }


}
