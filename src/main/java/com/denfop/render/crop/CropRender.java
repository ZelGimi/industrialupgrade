package com.denfop.render.crop;

import com.denfop.Constants;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.agriculture.ICrop;
import com.denfop.blocks.state.TileEntityBlockStateContainer;
import com.denfop.blocks.state.UnlistedProperty;
import com.denfop.render.base.AbstractModel;
import com.denfop.render.base.BakedBlockModel;
import com.denfop.render.base.ModelCuboidUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class CropRender extends AbstractModel {

    public static final IUnlistedProperty<CropRenderState> renderStateProperty = new UnlistedProperty<>(
            "renderstate",
            CropRenderState.class
    );
    private static final ResourceLocation STICK = new ResourceLocation(Constants.MOD_ID, "blocks/stick");
    private static final ResourceLocation UPGRADED_STICK = new ResourceLocation(Constants.MOD_ID, "blocks/stick_upgraded");
    private static final CropList<CropTextures> textures = new CropList<>();
    private static final List<ResourceLocation> texturesList = new ArrayList<>();
    private final LoadingCache<CropRenderState, IBakedModel> modelCache;

    public CropRender() {
        preloadTextures();
        modelCache = CacheBuilder.newBuilder()
                .maximumSize(256L)
                .expireAfterAccess(5L, TimeUnit.MINUTES)
                .build(CacheLoader.from(this::createModel));
    }

    private void preloadTextures() {
        if (textures.isEmpty()) {

            textures.add(new CropTextures(STICK));
            textures.add(new CropTextures(UPGRADED_STICK));
            CropNetwork.instance.getCropMap().values().forEach(crop -> {
                crop.getTextures().forEach(location -> textures.add(new CropTextures(location)));
                crop.getTopTexture().forEach(location -> textures.add(new CropTextures(location)));
            });


            textures.forEach(cropTextures -> texturesList.add(cropTextures.getLocation()));

        }
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return texturesList;
    }

    @Override
    public IBakedModel bake(
            IModelState state,
            VertexFormat format,
            Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter
    ) {
        textures.forEach(location -> location.setSprite(bakedTextureGetter.apply(location.getLocation())));
        return this;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState rawState, EnumFacing side, long rand) {
        CropRenderState renderState = extractRenderState(rawState);

        try {
            return modelCache.get(renderState).getQuads(rawState, side, rand);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CropRenderState extractRenderState(IBlockState rawState) {
        if (rawState instanceof TileEntityBlockStateContainer.PropertiesStateInstance) {
            TileEntityBlockStateContainer.PropertiesStateInstance stateInstance = (TileEntityBlockStateContainer.PropertiesStateInstance) rawState;
            return stateInstance.hasValue(renderStateProperty)
                    ? stateInstance.getValue(renderStateProperty)
                    : new CropRenderState(null, false, false);
        }
        return new CropRenderState(null, false, false);
    }

    public IBakedModel createModel(CropRenderState state) {
        if (state.crop != null && state.crop.getStage() >= 0) {
            return createCropModel(state);
        }
        return createStickModel(state.isDoubleStick());
    }

    private IBakedModel createCropModel(CropRenderState state) {
        List<BakedQuad>[] faceQuads = new List[EnumFacing.HORIZONTALS.length];
        for (int i = 0; i < faceQuads.length; i++) {
            faceQuads[i] = new ArrayList<>();
        }

        List<BakedQuad> generalQuads = new ArrayList<>();
        TextureAtlasSprite stickSprite = textures.get(0).getSprite();
        if (!state.isNeedTwoTexture()) {
            addCroppedQuads(faceQuads, generalQuads, stickSprite, 0);
        }

        TextureAtlasSprite cropSprite;
        if (state.isNeedTwoTexture()) {
            cropSprite = textures.getTextures(getTopTextureLocation(state.crop));
        } else {
            cropSprite = textures.getTextures(getTextureLocation(state.crop));
        }
        if (cropSprite == null) {
            cropSprite = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
        }
        if (cropSprite != Minecraft
                .getMinecraft()
                .getTextureMapBlocks()
                .getMissingSprite() && state.isNeedTwoTexture() || !state.isNeedTwoTexture()) {
            addCroppedQuads(faceQuads, generalQuads, cropSprite, state.crop.getRender());
        }
        return new BakedBlockModel(optimizeFaceQuads(faceQuads), generalQuads.isEmpty() ? Collections.emptyList() : generalQuads,
                cropSprite
        );
    }

    private void addCroppedQuads(
            List<BakedQuad>[] faceQuads, List<BakedQuad> generalQuads, TextureAtlasSprite sprite,
            int render
    ) {

        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (render == 0) {
                addQuadForFacing(faceQuads, generalQuads, sprite, facing);
            } else {
                addSugarCaneQuads(faceQuads, generalQuads, sprite, facing);
            }
        }
    }

    private void addCroppedQuadsTop(
            List<BakedQuad>[] faceQuads, List<BakedQuad> generalQuads, TextureAtlasSprite sprite,
            int render
    ) {
        if (sprite == Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite()) {
            return;
        }
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (render == 0) {
                addQuadForFacingTop(faceQuads, generalQuads, sprite, facing);
            } else {
                addSugarCaneQuadsTop(faceQuads, generalQuads, sprite, facing);
            }
        }
    }

    private void addQuadForFacing(
            List<BakedQuad>[] faceQuads,
            List<BakedQuad> generalQuads,
            TextureAtlasSprite sprite,
            EnumFacing facing
    ) {
        int offsetX = facing.getFrontOffsetX();
        int offsetZ = facing.getFrontOffsetZ();
        float x = calculatePosition(offsetX);
        float z = calculatePosition(offsetZ);
        float xS = offsetX == 0 ? 0.0F : x;
        float xE = offsetX == 0 ? 1.0F : x;
        float zS = offsetZ == 0 ? 0.0F : z;
        float zE = offsetZ == 0 ? 1.0F : z;

        ModelCuboidUtil.addFlippedCuboidWithYOffset(xS, 0.001F, zS, xE, 1.0F, zE, -1, EnumSet.of(facing), sprite, faceQuads,
                generalQuads, -0.0625F
        );
        ModelCuboidUtil.addFlippedCuboidWithYOffset(
                xS,
                0.001F,
                zS,
                xE,
                1.0F,
                zE,
                -1,
                EnumSet.of(facing.getOpposite()),
                sprite,
                faceQuads,
                generalQuads,
                -0.0625F
        );
    }

    private void addQuadForFacingTop(
            List<BakedQuad>[] faceQuads,
            List<BakedQuad> generalQuads,
            TextureAtlasSprite sprite,
            EnumFacing facing
    ) {
        int offsetX = facing.getFrontOffsetX();
        int offsetZ = facing.getFrontOffsetZ();
        float x = calculatePosition(offsetX);
        float z = calculatePosition(offsetZ);
        float xS = offsetX == 0 ? 0.0F : x;
        float xE = offsetX == 0 ? 1.0F : x;
        float zS = offsetZ == 0 ? 0.0F : z;
        float zE = offsetZ == 0 ? 1.0F : z;

        ModelCuboidUtil.addFlippedCuboidWithYOffset(xS, 0.001F, zS, xE, 1.0F, zE, -1, EnumSet.of(facing), sprite, faceQuads,
                generalQuads, -0.0625F
        );
        ModelCuboidUtil.addFlippedCuboidWithYOffset(xS, 0.001F, zS, xE, 1.0F, zE, -1, EnumSet.of(facing.getOpposite()), sprite,
                faceQuads, generalQuads, -0.0625F
        );
    }

    private void addSugarCaneQuads(
            List<BakedQuad>[] faceQuads, List<BakedQuad> generalQuads, TextureAtlasSprite sprite,
            EnumFacing facing
    ) {
        float offsetX = facing.getFrontOffsetX();
        float offsetZ = facing.getFrontOffsetZ();


        float xOffset = 0.5F;
        float zOffset = 0.5F;

        float size = 0.5F;
        float xS = xOffset - size * offsetZ;
        float xE = xOffset + size * offsetZ;
        float zS = zOffset + size * offsetX;
        float zE = zOffset - size * offsetX;


        ModelCuboidUtil.addFlippedCuboidWithYOffset(xS, 0.001F, zS, xE, 1.0F, zE, -1, EnumSet.of(facing), sprite, faceQuads,
                generalQuads, -0.0625F
        );
        ModelCuboidUtil.addFlippedCuboidWithYOffset(xS, 0.001F, zS, xE, 1.0F, zE, -1, EnumSet.of(facing.getOpposite()), sprite,
                faceQuads, generalQuads, -0.0625F
        );


    }

    private void addSugarCaneQuadsTop(
            List<BakedQuad>[] faceQuads, List<BakedQuad> generalQuads, TextureAtlasSprite sprite,
            EnumFacing facing
    ) {
        float offsetX = facing.getFrontOffsetX();
        float offsetZ = facing.getFrontOffsetZ();


        float xOffset = 0.5F;
        float zOffset = 0.5F;

        float size = 0.5F;
        float xS = xOffset - size * offsetZ;
        float xE = xOffset + size * offsetZ;
        float zS = zOffset + size * offsetX;
        float zE = zOffset - size * offsetX;


        ModelCuboidUtil.addFlippedCuboidWithYOffset(xS, 1F, zS, xE, 2f, zE, -1, EnumSet.of(facing), sprite, faceQuads,
                generalQuads, 0
        );
        ModelCuboidUtil.addFlippedCuboidWithYOffset(xS, 1F, zS, xE, 2f, zE, -1, EnumSet.of(facing.getOpposite()), sprite,
                faceQuads, generalQuads, 0
        );


    }


    private float calculatePosition(int offset) {
        return Math.abs(offset) * (0.5F + offset * 0.25F);
    }

    private IBakedModel createStickModel(boolean crosscrop) {
        List<BakedQuad>[] faceQuads = new List[EnumFacing.HORIZONTALS.length];
        for (int i = 0; i < faceQuads.length; i++) {
            faceQuads[i] = new ArrayList<>();
        }

        List<BakedQuad> generalQuads = new ArrayList<>();
        TextureAtlasSprite stickSprite = crosscrop ? textures.get(1).getSprite() : textures.get(0).getSprite();

        addCroppedQuads(faceQuads, generalQuads, stickSprite, 0);

        return new BakedBlockModel(
                optimizeFaceQuads(faceQuads),
                generalQuads.isEmpty() ? Collections.emptyList() : generalQuads,
                stickSprite
        );
    }

    private List<BakedQuad>[] optimizeFaceQuads(List<BakedQuad>[] faceQuads) {
        int used = (int) Arrays.stream(faceQuads).filter(list -> !list.isEmpty()).count();
        return used == 0 ? null : faceQuads;
    }

    private ResourceLocation getTextureLocation(ICrop crop) {
        return crop.getTexture(crop.getStage());
    }

    private ResourceLocation getTopTextureLocation(ICrop crop) {
        return crop.getTextureTop(crop.getStage());
    }

    private ResourceLocation getTextureTopLocation(ICrop crop) {
        return crop.getTextureTop(crop.getStage());
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return textures.get(0).getSprite();
    }

    @Override
    public void onReload() {
        modelCache.invalidateAll();
    }

}
