
package com.denfop.render.base;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.math.Transformation;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.model.CompositeModel;
import net.minecraftforge.client.model.CompositeModel.Baked;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.geometry.*;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DynamicFluidContainerModel implements IUnbakedGeometry<DynamicFluidContainerModel> {
    private static final Transformation FLUID_TRANSFORM = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1.0F, 1.0F, 1.002F), new Quaternionf());
    private static final Transformation COVER_TRANSFORM = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1.0F, 1.0F, 1.004F), new Quaternionf());
    private final Fluid fluid;
    private final boolean flipGas;
    private final boolean coverIsMask;
    private final boolean applyFluidLuminosity;

    private DynamicFluidContainerModel(Fluid fluid, boolean flipGas, boolean coverIsMask, boolean applyFluidLuminosity) {
        this.fluid = fluid;
        this.flipGas = flipGas;
        this.coverIsMask = coverIsMask;
        this.applyFluidLuminosity = applyFluidLuminosity;
    }

    public static RenderTypeGroup getLayerRenderTypes(boolean unlit) {
        return new RenderTypeGroup(RenderType.translucent(), unlit ? ForgeRenderTypes.ITEM_UNSORTED_UNLIT_TRANSLUCENT.get() : ForgeRenderTypes.ITEM_UNSORTED_TRANSLUCENT.get());
    }

    public DynamicFluidContainerModel withFluid(Fluid newFluid) {
        return new DynamicFluidContainerModel(newFluid, this.flipGas, this.coverIsMask, this.applyFluidLuminosity);
    }

    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        Material particleLocation = context.hasMaterial("particle") ? context.getMaterial("particle") : null;
        Material baseLocation = context.hasMaterial("base") ? context.getMaterial("base") : null;
        Material fluidMaskLocation = context.hasMaterial("fluid") ? context.getMaterial("fluid") : null;
        Material coverLocation = context.hasMaterial("cover") ? context.getMaterial("cover") : null;
        TextureAtlasSprite baseSprite = baseLocation != null ? (TextureAtlasSprite)spriteGetter.apply(baseLocation) : null;
        TextureAtlasSprite fluidSprite = this.fluid != Fluids.EMPTY ? (TextureAtlasSprite)spriteGetter.apply(ForgeHooksClient.getBlockMaterial(IClientFluidTypeExtensions.of(this.fluid).getStillTexture())) : null;
        TextureAtlasSprite coverSprite = coverLocation == null || this.coverIsMask && baseLocation == null ? null : (TextureAtlasSprite)spriteGetter.apply(coverLocation);
        TextureAtlasSprite particleSprite = particleLocation != null ? (TextureAtlasSprite)spriteGetter.apply(particleLocation) : null;
        if (particleSprite == null) {
            particleSprite = fluidSprite;
        }

        if (particleSprite == null) {
            particleSprite = baseSprite;
        }

        if (particleSprite == null && !this.coverIsMask) {
            particleSprite = coverSprite;
        }

        if (this.flipGas && this.fluid != Fluids.EMPTY && this.fluid.getFluidType().isLighterThanAir()) {
            modelState = new SimpleModelState(((ModelState)modelState).getRotation().compose(new Transformation((Vector3f)null, new Quaternionf(0.0F, 0.0F, 1.0F, 0.0F), (Vector3f)null, (Quaternionf)null)));
        }

        StandaloneGeometryBakingContext itemContext = StandaloneGeometryBakingContext.builder(context).withGui3d(false).withUseBlockLight(false).build(modelLocation);
        CompositeModel.Baked.Builder modelBuilder = Baked.builder(itemContext, particleSprite, new ContainedFluidOverrideHandler(overrides, baker, itemContext, this), context.getTransforms());
        RenderTypeGroup normalRenderTypes = getLayerRenderTypes(false);
        if (baseLocation != null && baseSprite != null) {
            List<BlockElement> unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, baseSprite.contents());
            List<BakedQuad> quads = UnbakedGeometryHelper.bakeElements(unbaked, ($) -> {
                return baseSprite;
            }, (ModelState)modelState, modelLocation);
            modelBuilder.addQuads(normalRenderTypes, quads);
        }

        List unbaked;
        List quads;
        TextureAtlasSprite sprite;
        SimpleModelState transformedState;
        if (fluidMaskLocation != null && fluidSprite != null) {
            sprite = (TextureAtlasSprite)spriteGetter.apply(fluidMaskLocation);
            if (sprite != null) {
                transformedState = new SimpleModelState(((ModelState)modelState).getRotation().compose(FLUID_TRANSFORM), ((ModelState)modelState).isUvLocked());
                unbaked = UnbakedGeometryHelper.createUnbakedItemMaskElements(1, sprite.contents());
                quads = UnbakedGeometryHelper.bakeElements(unbaked, ($) -> {
                    return fluidSprite;
                }, transformedState, modelLocation);
                boolean emissive = this.applyFluidLuminosity && this.fluid.getFluidType().getLightLevel() > 0;
                RenderTypeGroup renderTypes = getLayerRenderTypes(emissive);
                if (emissive) {
                    QuadTransformers.settingMaxEmissivity().processInPlace(quads);
                }

                modelBuilder.addQuads(renderTypes, quads);
            }
        }

        if (coverSprite != null) {
            sprite = this.coverIsMask ? baseSprite : coverSprite;
            if (sprite != null) {
                transformedState = new SimpleModelState(((ModelState)modelState).getRotation().compose(COVER_TRANSFORM), ((ModelState)modelState).isUvLocked());
                unbaked = UnbakedGeometryHelper.createUnbakedItemMaskElements(2, coverSprite.contents());
                TextureAtlasSprite finalSprite = sprite;
                quads = UnbakedGeometryHelper.bakeElements(unbaked, material -> finalSprite, transformedState, modelLocation);
                modelBuilder.addQuads(normalRenderTypes, quads);
            }
        }

        modelBuilder.setParticle(particleSprite);
        return modelBuilder.build();
    }

    private static final class ContainedFluidOverrideHandler extends ItemOverrides {
        private final Map<String, BakedModel> cache = Maps.newHashMap();
        private final ItemOverrides nested;
        private final ModelBaker baker;
        private final IGeometryBakingContext owner;
        private final DynamicFluidContainerModel parent;

        private ContainedFluidOverrideHandler(ItemOverrides nested, ModelBaker baker, IGeometryBakingContext owner, DynamicFluidContainerModel parent) {
            this.nested = nested;
            this.baker = baker;
            this.owner = owner;
            this.parent = parent;
        }

        public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            BakedModel overridden = this.nested.resolve(originalModel, stack, level, entity, seed);
            return overridden != originalModel ? overridden : (BakedModel)FluidUtil.getFluidContained(stack).map((fluidStack) -> {
                Fluid fluid = fluidStack.getFluid();
                String name = ForgeRegistries.FLUIDS.getKey(fluid).toString();
                if (!this.cache.containsKey(name)) {
                    DynamicFluidContainerModel unbaked = this.parent.withFluid(fluid);
                    BakedModel bakedModel = unbaked.bake(this.owner, this.baker, Material::sprite, BlockModelRotation.X0_Y0, this, new ResourceLocation("forge:bucket_override"));
                    this.cache.put(name, bakedModel);
                    return bakedModel;
                } else {
                    return (BakedModel)this.cache.get(name);
                }
            }).orElse(originalModel);
        }
    }

    public static class Colors implements ItemColor {
        public Colors() {
        }

        public int getColor(@NotNull ItemStack stack, int tintIndex) {
            return tintIndex != 1 ? -1 : (Integer)FluidUtil.getFluidContained(stack).map((fluidStack) -> {
                return IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
            }).orElse(-1);
        }
    }

    public static final class Loader implements IGeometryLoader<DynamicFluidContainerModel> {
        public static final Loader INSTANCE = new Loader();

        private Loader() {
        }

        public DynamicFluidContainerModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {
            if (!jsonObject.has("fluid")) {
                throw new RuntimeException("Bucket model requires 'fluid' value.");
            } else {
                ResourceLocation fluidName = new ResourceLocation(jsonObject.get("fluid").getAsString());
                Fluid fluid = (Fluid)ForgeRegistries.FLUIDS.getValue(fluidName);
                boolean flip = GsonHelper.getAsBoolean(jsonObject, "flip_gas", false);
                boolean coverIsMask = GsonHelper.getAsBoolean(jsonObject, "cover_is_mask", true);
                boolean applyFluidLuminosity = GsonHelper.getAsBoolean(jsonObject, "apply_fluid_luminosity", true);
                return new DynamicFluidContainerModel(fluid, flip, coverIsMask, applyFluidLuminosity);
            }
        }
    }
}
