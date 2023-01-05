package com.denfop.render.transport;

import com.denfop.Constants;
import com.denfop.tiles.transport.tiles.TileEntityQCable;
import com.denfop.tiles.transport.tiles.TileEntityQCable.CableRenderState;
import com.denfop.tiles.transport.types.QEType;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import ic2.core.model.AbstractModel;
import ic2.core.model.BasicBakedBlockModel;
import ic2.core.model.ISpecialParticleModel;
import ic2.core.model.ModelUtil;
import ic2.core.model.VdUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ModelQCable extends AbstractModel implements ISpecialParticleModel {

    private final Map<ResourceLocation, TextureAtlasSprite> textures;
    private final LoadingCache<CableRenderState, IBakedModel> modelCache;

    public ModelQCable() {
        textures = generateTextureLocations();
        this.modelCache = CacheBuilder
                .newBuilder()
                .maximumSize(256L)
                .expireAfterAccess(5L, TimeUnit.MINUTES)
                .build(new CacheLoader<CableRenderState, IBakedModel>() {
                    public IBakedModel load(@Nonnull CableRenderState key) {
                        return ModelQCable.this.generateModel(key);
                    }
                });
    }

    private static Map<ResourceLocation, TextureAtlasSprite> generateTextureLocations() {
        Map<ResourceLocation, TextureAtlasSprite> ret = new HashMap<>();
        StringBuilder name = new StringBuilder();
        name.append("blocks/wiring/qcable/");
        int reset0 = name.length();
        QEType[] var3 = QEType.values;


        for (QEType type : var3) {
            name.append(type.name());
            name.append("_qcable");
            int reset1 = name.length();

            for (int insulation = 0; insulation <= type.maxInsulation; ++insulation) {


                ret.put(new ResourceLocation(Constants.MOD_ID, name.toString()), null);


                name.setLength(reset1);
            }

            name.setLength(reset0);
        }

        return ret;
    }

    private static ResourceLocation getTextureLocation(QEType type) {
        String loc = "blocks/wiring/qcable/" + type.getName();


        return new ResourceLocation(Constants.MOD_ID, loc);
    }

    public Collection<ResourceLocation> getTextures() {
        return this.textures.keySet();
    }

    public IBakedModel bake(
            IModelState state,
            VertexFormat format,
            Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter
    ) {

        for (final Entry<ResourceLocation, TextureAtlasSprite> resourceLocationTextureAtlasSpriteEntry : this.textures.entrySet()) {
            resourceLocationTextureAtlasSpriteEntry.setValue(bakedTextureGetter.apply(resourceLocationTextureAtlasSpriteEntry.getKey()));
        }

        return this;
    }

    @Nonnull
    public List<BakedQuad> getQuads(IBlockState rawState, EnumFacing side, long rand) {
        if (!(rawState instanceof Ic2BlockStateInstance)) {
            return ModelUtil.getMissingModel().getQuads(rawState, side, rand);
        } else {
            Ic2BlockStateInstance state = (Ic2BlockStateInstance) rawState;
            if (!state.hasValue(TileEntityQCable.renderStateProperty)) {
                return ModelUtil.getMissingModel().getQuads(state, side, rand);
            } else {
                CableRenderState prop = state.getValue(TileEntityQCable.renderStateProperty);

                try {
                    return this.modelCache.get(prop).getQuads(state, side, rand);
                } catch (ExecutionException var9) {
                    throw new RuntimeException(var9);
                }

            }
        }
    }

    private IBakedModel generateModel(CableRenderState prop) {
        float th = prop.type.thickness + (float) (prop.insulation * 2) * 0.0625F;
        float sp = (1.0F - th) / 2.0F;
        List<BakedQuad>[] faceQuads = new List[EnumFacing.VALUES.length];

        for (int i = 0; i < faceQuads.length; ++i) {
            faceQuads[i] = new ArrayList<>();
        }

        List<BakedQuad> generalQuads = new ArrayList<>();
        TextureAtlasSprite sprite = this.textures.get(getTextureLocation(prop.type
        ));
        EnumFacing[] var7 = EnumFacing.VALUES;
        int i = var7.length;

        for (int var9 = 0; var9 < i; ++var9) {
            EnumFacing facing = var7[var9];
            boolean hasConnection = (prop.connectivity & 1 << facing.ordinal()) != 0;
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

                VdUtil.addCuboid(
                        xS,
                        yS,
                        zS,
                        xE,
                        yE,
                        zE,
                        EnumSet.complementOf(EnumSet.of(facing.getOpposite())),
                        sprite,
                        faceQuads,
                        generalQuads
                );
            } else {
                VdUtil.addCuboid(sp, sp, sp, xE, yE, zE, EnumSet.of(facing), sprite, faceQuads, generalQuads);
            }
        }

        int used = 0;

        for (i = 0; i < faceQuads.length; ++i) {
            if (faceQuads[i].isEmpty()) {
                faceQuads[i] = Collections.emptyList();
            } else {
                ++used;
            }
        }

        if (used == 0) {
            faceQuads = null;
        }

        if (generalQuads.isEmpty()) {
            generalQuads = Collections.emptyList();
        }

        return new BasicBakedBlockModel(faceQuads, generalQuads, sprite);
    }

    public void onReload() {
        this.modelCache.invalidateAll();
    }

    public boolean needsEnhancing(IBlockState state) {
        return true;
    }

    public TextureAtlasSprite getParticleTexture(Ic2BlockStateInstance state) {
        if (!state.hasValue(TileEntityQCable.renderStateProperty)) {
            return ModelUtil.getMissingModel().getParticleTexture();
        } else {
            CableRenderState prop = state.getValue(TileEntityQCable.renderStateProperty);

            return this.textures.get(getTextureLocation(prop.type));

        }
    }

}
