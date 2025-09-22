package com.denfop.render.base;

import com.denfop.tiles.bee.TileEntityApiary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class TileEntitySolarSystemRender extends TileEntitySpecialRenderer<TileEntityApiary> {


    public void render(
            TileEntityApiary tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {

    }

    public void render(FluidStack fluidStack, IBakedModel model, IBlockState state, EnumFacing enumfacing) {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        int i = 0;
        final List<BakedQuad> listQuads = model.getQuads(state, enumfacing, 0L);
        for (int j = listQuads.size(); i < j; ++i) {
            BakedQuad bakedquad = listQuads.get(i);


            bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
            bufferbuilder.addVertexData(bakedquad.getVertexData());


            bufferbuilder.putColorRGB_F4(
                    (fluidStack.getFluid().getColor(fluidStack) >> 16 & 255) / 255.0F,
                    (fluidStack.getFluid().getColor(fluidStack) >> 8 & 255) / 255.0F,
                    (fluidStack.getFluid().getColor(fluidStack) & 255) / 255.0F
            );


            Vec3i vec3i = bakedquad.getFace().getDirectionVec();
            bufferbuilder.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
            tessellator.draw();
        }

    }

}
