package com.denfop.render.base;

import com.denfop.blocks.state.TileEntityBlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface ISpecialParticleModel extends IBakedModel {

    default boolean needsEnhancing(IBlockState state) {
        return Minecraft
                .getMinecraft()
                .getRenderItem()
                .getItemModelMesher()
                .getModelManager()
                .getMissingModel()
                .getParticleTexture()
                .getIconName()
                .equals(this.getParticleTexture().getIconName());
    }

    default void enhanceParticle(Particle particle, TileEntityBlockStateContainer.PropertiesStateInstance state) {
        particle.setParticleTexture(this.getParticleTexture(state));
    }

    default TextureAtlasSprite getParticleTexture(TileEntityBlockStateContainer.PropertiesStateInstance state) {
        return this.getParticleTexture();
    }

}
