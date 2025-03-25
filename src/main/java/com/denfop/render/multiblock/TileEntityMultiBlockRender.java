package com.denfop.render.multiblock;

import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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

    public static Function createFunction(TileMultiBlockBase te) {
        Function function = o -> {

            GlStateManager.pushMatrix();
            GlStateManager.translate(te.getBlockPos().getX() + 0.5f, te.getBlockPos().getY(), te.getBlockPos().getZ() + 0.5f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);



            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            te.render(te);

            GlStateManager.popMatrix();

            return 0;
        };
        return function;
    }

}
