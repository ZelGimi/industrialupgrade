package com.denfop.mixin;

import com.denfop.IUItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ComposterBlock.class)
public abstract class ComposterBlockMixin {

    private static boolean shouldProducePeat(Level level, BlockPos pos) {
        boolean swampBiome =
                level.getBiome(pos).is(Biomes.SWAMP) ||
                        level.getBiome(pos).is(Biomes.MANGROVE_SWAMP);

        boolean nearWater = isNearWater(level, pos);

        return swampBiome || nearWater;
    }

    private static boolean isNearWater(Level level, BlockPos pos) {


        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getFluidState(pos.relative(direction)).is(FluidTags.WATER)) {
                return true;
            }
        }

        return false;
    }

    @Inject(
            method = "useWithoutItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void industrialupgrade$replaceBoneMealWithPeat(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (state.getValue(ComposterBlock.LEVEL) != 8) {
            return;
        }

        if (!shouldProducePeat(level, pos)) {
            return;
        }

        if (level.isClientSide) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        ItemStack result = new ItemStack(IUItem.blockResource.getItem(10));

        Containers.dropItemStack(
                level,
                pos.getX() + 0.5D,
                pos.getY() + 1.0D,
                pos.getZ() + 0.5D,
                result
        );

        BlockState emptyState = state.setValue(ComposterBlock.LEVEL, 0);
        level.setBlock(pos, emptyState, 3);
        level.playSound(null, pos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

        cir.setReturnValue(InteractionResult.CONSUME);
    }
}