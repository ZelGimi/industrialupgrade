
package com.denfop.render.base;

import java.util.Collections;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class BasicBakedBlockModel extends AbstractBakedModel {
    private final List<BakedQuad>[] faceQuads;
    private final List<BakedQuad> generalQuads;
    private final TextureAtlasSprite particleTexture;

    public BasicBakedBlockModel(List<BakedQuad>[] faceQuads, List<BakedQuad> generalQuads, TextureAtlasSprite particleTexture) {
        this.faceQuads = faceQuads;
        this.generalQuads = generalQuads;
        this.particleTexture = particleTexture;
    }

    public static BakedQuad createQuad(int[] vertexData, EnumFacing side, TextureAtlasSprite sprite) {
        return new BakedQuad(vertexData, -1, side, sprite, true, ModelCuboidUtil.vertexFormat);
    }

    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        if (side == null) {
            return this.generalQuads;
        } else {
            return this.faceQuads == null ? Collections.emptyList() : this.faceQuads[side.ordinal()];
        }
    }

    public TextureAtlasSprite getParticleTexture() {
        return this.particleTexture;
    }
}
