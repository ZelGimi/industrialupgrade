package com.denfop.render.multiblock;

import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class TileEntityMultiBlockRender<T extends TileMultiBlockBase> extends TileEntitySpecialRenderer<T> {


    public void render(
            @Nonnull TileMultiBlockBase te,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {





    }
    public static Function createFunction( TileMultiBlockBase te){
        Function function= o -> {

                GlStateManager.pushMatrix();
                GlStateManager.translate(te.getBlockPos().getX() + 0.5f, te.getBlockPos().getY(), te.getBlockPos().getZ() + 0.5f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                GL11.glDisable(GL11.GL_BLEND);
                GlStateManager.disableLighting();
                te.render(te);
                GL11.glEnable(GL11.GL_BLEND);
                GlStateManager.enableLighting();
                GlStateManager.popMatrix();

            return 0;
        };
        return function;
    }
}
