package com.denfop.render.sintezator;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.tiles.base.TileSintezator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntitySintezatorRender extends TileEntitySpecialRenderer<TileSintezator> {

    public static Map<IBlockState, IBakedModel> modelMap = new HashMap<>();

    public void render(
            @Nonnull TileSintezator tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        GL11.glPushMatrix();
        GL11.glRotatef(0F, 0.0F, 0F, 0F);
        for (int i = 0; i < 9; i++) {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.25 + 0.189 * (i % 3), y + 0.3125, z + 0.25 + 0.189 * (i / 3));
            GlStateManager.scale(0.125, 0.125, 0.125);
            if (!tile.inputslot.get(i).isEmpty()) {
                ItemStack stack = tile.inputslot.get(i);
                Item item = stack.getItem();
                Block block = Block.getBlockFromItem(item);
                if (block != Blocks.AIR) {
                    if (block instanceof BlockTileEntity) {
                        BlockTileEntity blockTileEntity = (BlockTileEntity) block;

                        IMultiTileBlock teBlock = blockTileEntity.teInfo.getIdMap().isEmpty() ? null :
                                blockTileEntity.teInfo.getIdMap().get(stack.getItemDamage());
                        IBlockState state = blockTileEntity.getDefaultState().withProperty(
                                blockTileEntity.typeProperty,
                                blockTileEntity.typeProperty.getState(teBlock, tile.active)
                        );
                        IBakedModel model = modelMap.get(state);
                        if (model == null) {
                            model = Minecraft
                                    .getMinecraft()
                                    .getBlockRendererDispatcher()
                                    .getModelForState(state);
                            modelMap.put(state, model);
                        }
                        for (EnumFacing enumfacing : EnumFacing.values()) {
                            render(model, state, enumfacing);
                        }

                        render(model, state, null);
                    } else {
                        IBlockState state = block.getStateFromMeta(stack.getItemDamage());
                        IBakedModel model = modelMap.get(state);
                        if (model == null) {
                            model = Minecraft
                                    .getMinecraft()
                                    .getBlockRendererDispatcher()
                                    .getModelForState(state);
                            modelMap.put(state, model);
                        }
                        for (EnumFacing enumfacing : EnumFacing.values()) {
                            render(model, state, enumfacing);
                        }

                        render(model, state, null);
                    }
                }
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();

    }

    public void render(IBakedModel model, IBlockState state, EnumFacing enumfacing) {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        int i = 0;
        final List<BakedQuad> listQuads = model.getQuads(state, enumfacing, 0L);
        for (int j = listQuads.size(); i < j; ++i) {
            BakedQuad bakedquad = listQuads.get(i);


            bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
            bufferbuilder.addVertexData(bakedquad.getVertexData());


            Vec3i vec3i = bakedquad.getFace().getDirectionVec();
            bufferbuilder.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
            tessellator.draw();
        }

    }

}
