package com.denfop.blocks.fluid;

import com.denfop.blocks.FluidName;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class IUFluidType extends FluidType {

    private final FluidName fluidName;

    public IUFluidType(FluidName fluidName, Properties properties) {
        super(properties);
        this.fluidName = fluidName;
    }

    @Override
    public int getLightLevel() {
        return fluidName == FluidName.fluidpahoehoe_lava ? 15 : super.getLightLevel();
    }



    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {


            @Override
            public ResourceLocation getStillTexture() {
                return fluidName.getTextureLocation(false);
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return fluidName.getTextureLocation(true);
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return fluidName.getTextureLocation(false);
            }

            public int getTintColor() {
                return IClientFluidTypeExtensions.super.getTintColor();
            }

            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return  IClientFluidTypeExtensions.super.getTintColor(state,getter,pos);
            }
        });
    }
}
