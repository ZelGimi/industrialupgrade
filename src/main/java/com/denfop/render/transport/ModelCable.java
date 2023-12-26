
package com.denfop.render.transport;

import com.denfop.blocks.state.TileEntityBlockStateContainer;
import com.denfop.render.base.AbstractModel;
import com.denfop.render.base.BasicBakedBlockModel;
import com.denfop.render.base.ISpecialParticleModel;
import com.denfop.render.base.ModelCuboidUtil;
import com.denfop.tiles.transport.tiles.RenderState;
import com.denfop.tiles.transport.tiles.TileEntityCable;
import com.denfop.tiles.transport.types.CableType;
import com.denfop.tiles.transport.types.ICableItem;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCable extends AbstractModel implements ISpecialParticleModel {
    private final Map<ResourceLocation, TextureAtlasSprite> textures;
    private final LoadingCache<RenderState, IBakedModel> modelCache;
    private final ICableItem[] values;

    public ModelCable(ICableItem[] values) {
        this.values = values;
        textures =  generateTextureLocations();
        this.modelCache =
                CacheBuilder.newBuilder().maximumSize(256L).expireAfterAccess(5L, TimeUnit.MINUTES).build(new CacheLoader<RenderState, IBakedModel>() {
            public IBakedModel load(@Nonnull RenderState key) {
                return ModelCable.this.generateModel(key);
            }
        });
    }

    private Map<ResourceLocation, TextureAtlasSprite> generateTextureLocations() {
        Map<ResourceLocation, TextureAtlasSprite> ret = new HashMap();
        for (ICableItem item : values)
        ret.put(item.getRecourse(), null);

        return ret;
    }

    public Collection<ResourceLocation> getTextures() {
        return this.textures.keySet();
    }

    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {

        for (Map.Entry<ResourceLocation, TextureAtlasSprite> locationTextureAtlasSpriteEntry : this.textures.entrySet()) {
            locationTextureAtlasSpriteEntry.setValue(bakedTextureGetter.apply(locationTextureAtlasSpriteEntry.getKey()));
        }

        return this;
    }

    @Nonnull
    public List<BakedQuad> getQuads(IBlockState rawState, EnumFacing side, long rand) {
        if (!(rawState instanceof TileEntityBlockStateContainer.PropertiesStateInstance)) {
            return  Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getMissingModel().getQuads(rawState, side, rand);
        } else {
            TileEntityBlockStateContainer.PropertiesStateInstance state = (TileEntityBlockStateContainer.PropertiesStateInstance)rawState;
            if (!state.hasValue(TileEntityCable.renderStateProperty)) {
                return  Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getMissingModel().getQuads(state, side, rand);
            } else {
                RenderState prop = state.getValue(TileEntityCable.renderStateProperty);

                try {
                    return this.modelCache.get(prop).getQuads(state, side, rand);
                } catch (ExecutionException var8) {
                    throw new RuntimeException(var8);
                }
            }
        }
    }

    private IBakedModel generateModel(RenderState prop) {
        float th = 0.25f;
        float sp = (1.0F - th) / 2.0F;
        List<BakedQuad>[] faceQuads = new List[EnumFacing.VALUES.length];

        for(int i = 0; i < faceQuads.length; ++i) {
            faceQuads[i] = new ArrayList<>();
        }

        List<BakedQuad> generalQuads = new ArrayList<>();
        TextureAtlasSprite sprite = this.textures.get(new ResourceLocation(prop.resourceLocation.toString().replace(".png","")));
        EnumFacing[] var7 = EnumFacing.VALUES;
        int i = var7.length;

        int used;
        for(used = 0; used < i; ++used) {
            EnumFacing facing = var7[used];
            boolean hasConnection = (prop.connectivity & 1 << ((var7.length - 1) - facing.ordinal())) != 0;
            float zS = sp;
            float yS = sp;
            float xS = sp;
            float yE;
            float zE;
            float xE = yE = zE = sp + th;
            if (hasConnection) {
                switch (facing) {
                    case DOWN:
                        yS = 0.0F;
                        yE = sp;
                        break;
                    case UP:
                        yS = sp + th;
                        yE = 1.0F;
                        break;
                    case NORTH:
                        zS = 0.0F;
                        zE = sp;
                        break;
                    case SOUTH:
                        zS = sp + th;
                        zE = 1.0F;
                        break;
                    case WEST:
                        xS = 0.0F;
                        xE = sp;
                        break;
                    case EAST:
                        xS = sp + th;
                        xE = 1.0F;
                        break;
                    default:
                        throw new RuntimeException();
                }

                ModelCuboidUtil.addCuboid(xS, yS, zS, xE, yE, zE, EnumSet.complementOf(EnumSet.of(facing.getOpposite())), sprite, faceQuads,
                        generalQuads);
            } else {
                ModelCuboidUtil.addCuboid(sp, sp, sp, xE, yE, zE, EnumSet.of(facing), sprite, faceQuads,generalQuads);
            }
        }

        used = 0;

        for(i = 0; i < faceQuads.length; ++i) {
            if (faceQuads[i].isEmpty()) {
                faceQuads[i] = Collections.emptyList();
            } else {
                ++used;
            }
        }

        if (used == 0) {
            faceQuads = null;
        }

        if ((generalQuads).isEmpty()) {
            generalQuads = Collections.emptyList();
        }

        return new BasicBakedBlockModel(faceQuads,generalQuads, sprite);
    }

    public void onReload() {
        this.modelCache.invalidateAll();
    }

    public boolean needsEnhancing(IBlockState state) {
        return true;
    }

    public TextureAtlasSprite getParticleTexture( TileEntityBlockStateContainer.PropertiesStateInstance state) {
        if (!state.hasValue(TileEntityCable.renderStateProperty)) {
            return  Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getMissingModel().getParticleTexture();
        } else {
           RenderState prop = state.getValue(TileEntityCable.renderStateProperty);
            return this.textures.get((prop.resourceLocation));
        }
    }
}
