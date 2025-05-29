package com.denfop.render.base;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;

public abstract class RenderType extends net.minecraft.client.renderer.RenderType {
    public static final net.minecraft.client.renderer.RenderType LEASH = create("leash_circle", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_STRIP, 256, false, false, net.minecraft.client.renderer.RenderType.CompositeState.builder().setShaderState(POSITION_COLOR_SHADER).setTextureState(NO_TEXTURE).setCullState(NO_CULL).setDepthTestState(NO_DEPTH_TEST).createCompositeState(false));
    public static final net.minecraft.client.renderer.RenderType LEASH1 = create("leash_circle", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_STRIP, 256, false, false, net.minecraft.client.renderer.RenderType.CompositeState.builder().setShaderState(POSITION_COLOR_SHADER).setTextureState(NO_TEXTURE).setCullState(NO_CULL).createCompositeState(false));
    public static final net.minecraft.client.renderer.RenderType LEASH_TRANSPARENT = create(
            "leash_circle_transparent",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.TRIANGLE_STRIP,
            256,
            false,
            false,
            CompositeState.builder()

                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)

                    .setTextureState(RenderStateShard.NO_TEXTURE)

                    .setCullState(RenderStateShard.NO_CULL)

                    .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)

                    .setWriteMaskState(WriteMaskStateShard.COLOR_WRITE)

                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .createCompositeState(false)
    );

    public RenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

}
