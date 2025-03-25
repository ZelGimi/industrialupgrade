package com.denfop.render.oilquarry;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.vein.Type;
import com.denfop.blocks.BlockHeavyOre;
import com.denfop.blocks.BlockMineral;
import com.denfop.blocks.FluidName;
import com.denfop.tiles.base.TileQuarryVein;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class TileEntityQuarryOilRender extends TileEntitySpecialRenderer<TileQuarryVein> {


    float rotation;
    float prevRotation;


    public void render(
            TileQuarryVein tile,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {

        if (tile.vein != null && tile.vein.get()) {
            if (tile.vein.getType() != Type.EMPTY) {
                GL11.glPushMatrix();
                GL11.glTranslated(x, y, z);
                GL11.glTranslatef(0.4F, 1.2f, 0.4F);
                GlStateManager.scale(0.25, 0.25, 0.25);
                GL11.glRotatef(rotation, 1F, 1F, 1F);
                IBlockState state;
                IBakedModel model;
                this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                if (tile.dataBlock == null) {
                    if (tile.vein.getType() == Type.VEIN) {
                        if (tile.vein.isOldMineral()) {
                            state = IUItem.heavyore.getDefaultState().withProperty(
                                    BlockHeavyOre.VARIANT,
                                    BlockHeavyOre.Type.getFromID(tile.vein.getMeta())
                            );
                        } else {
                            state = IUItem.mineral.getDefaultState().withProperty(
                                    BlockMineral.VARIANT,
                                    BlockMineral.Type.getFromID(tile.vein.getMeta())
                            );
                        }
                    } else {
                        if (tile.vein.getType() == Type.OIL) {
                            state = IUItem.oilblock.getDefaultState();
                        } else {
                            state = IUItem.gasBlock.getDefaultState();
                        }
                    }
                    tile.dataBlock = new DataBlock(state);
                    model = Minecraft
                            .getMinecraft()
                            .getBlockRendererDispatcher()
                            .getModelForState(state);
                    tile.dataBlock.setState(model);
                } else {
                    state = tile.blockState;
                }
                model = tile.dataBlock.getState();
                for (EnumFacing enumfacing : EnumFacing.values()) {
                    render(model, state, enumfacing);
                }

                render(model, state, null);
                GL11.glPopMatrix();
                rotation = prevRotation + (rotation - prevRotation) * partialTicks;
                prevRotation = rotation;
                rotation += 0.25;
            }
        }
        if (tile.vein != null && tile.vein.get()) {
            ItemStack stack = null;
            if (tile.vein.getType() == Type.VEIN) {
                if (tile.vein.isOldMineral()) {
                    stack = new ItemStack(IUItem.heavyore, 1, tile.vein.getMeta());
                }else{
                    stack = new ItemStack(IUItem.mineral, 1, tile.vein.getMeta());
                }

            } else if (tile.vein.getType() == Type.OIL) {
                stack = new ItemStack(IUItem.oilblock);
            } else if (tile.vein.getType() == Type.GAS) {
                stack = new ItemStack(FluidName.fluidgas.getInstance().getBlock());

            }

            if (stack != null) {
                final int col = tile.vein.getCol();
                final boolean isOil = tile.vein.getType() == Type.OIL || tile.vein.getType() == Type.GAS;
                int colmax = tile.vein.getMaxCol();

                int variety = tile.vein.getMeta() / 3;
                int type = tile.vein.getMeta() % 3;
                String varietyString = variety == 0 ? "iu.sweet_oil" : "iu.sour_oil";
                String typeString = type == 0 ? "iu.light_oil" : type == 1 ? "iu.medium_oil" : "iu.heavy_oil";
                ITextComponent itextcomponent;
                if (isOil) {
                    if (tile.vein.getType() != Type.GAS) {
                        itextcomponent =
                                new TextComponentString(Localization.translate(varietyString)+" " + Localization.translate(typeString) + stack.getDisplayName());
                    } else {
                        itextcomponent =
                                new TextComponentString(stack.getDisplayName());
                    }
                } else {
                    itextcomponent =
                            new TextComponentString(stack.getDisplayName());


                }

                ITextComponent itextcomponent1 = new TextComponentString(col + (isOil ? "mb" :
                        "") + "/" + colmax + (
                        isOil
                                ?
                                "mb"
                                : ""));
                if (this.rendererDispatcher.cameraHitResult != null && tile
                        .getPos()
                        .equals(this.rendererDispatcher.cameraHitResult.getBlockPos())) {
                    this.setLightmapDisabled(true);
                    this.drawNameplate(tile, itextcomponent.getFormattedText(), x, y + 0.5, z, 12);
                    this.drawNameplate(tile, itextcomponent1.getFormattedText(), x, y + 0.25, z, 12);
                    this.setLightmapDisabled(false);
                }
            }
        }
    }

    public static void render(IBakedModel model, IBlockState state, EnumFacing enumfacing) {

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
