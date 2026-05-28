package com.denfop.blocks;

import com.denfop.api.collision.MultiCellCollisionShapeHelper;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.collision.BlockEntityCollisionProxy;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class BlockCollisionProxy extends BaseEntityBlock implements EntityBlock {

    public static final MapCodec<BlockCollisionProxy> CODEC = simpleCodec((p_304364_) -> new BlockCollisionProxy());

    public BlockCollisionProxy() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.NONE)
                .strength(-1.0F, 3600000.0F)
                .noOcclusion()
                .sound(SoundType.STONE)
                .noLootTable());
    }

    private static double snap(double value) {
        double eps = 1.0E-6D;
        if (Math.abs(value) < eps) return 0.0D;
        if (Math.abs(value - 1.0D) < eps) return 1.0D;
        return value;
    }

    private static VoxelShape simplifyWalkableShape(VoxelShape shape) {
        AABB bounds = shape.bounds();

        double minX = snap(bounds.minX);
        double minY = snap(bounds.minY);
        double minZ = snap(bounds.minZ);
        double maxX = snap(bounds.maxX);
        double maxY = snap(bounds.maxY);
        double maxZ = snap(bounds.maxZ);

        if (minX == 0.0D && maxX == 1.0D && minZ == 0.0D && maxZ == 1.0D) {
            return Shapes.box(minX, minY, minZ, maxX, maxY, maxZ);
        }

        return shape;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public float getExplosionResistance() {
        return 3600000.0F;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return false;
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getDelegatedShape(level, pos, false);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {

        return getDelegatedShape(level, pos, true);
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return getDelegatedShape(level, pos, false);
    }

    @Override
    public @NotNull VoxelShape getBlockSupportShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return getDelegatedShape(level, pos, true);
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canBeReplaced(BlockState state, net.minecraft.world.item.context.BlockPlaceContext useContext) {
        return false;
    }

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        BlockPos masterPos = getMasterPos(level, pos);
        if (masterPos != null) {
            BlockState masterState = level.getBlockState(masterPos);
            return masterState.getDestroyProgress(player, level, masterPos);
        }
        return 0.0F;
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        BlockPos masterPos = getMasterPos(level, pos);
        if (masterPos != null) {
            BlockState masterState = level.getBlockState(masterPos);
            masterState.attack(level, masterPos, player);
            return;
        }
        super.attack(state, level, pos, player);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos masterPos = getMasterPos(level, pos);
        if (masterPos != null) {
            BlockState masterState = level.getBlockState(masterPos);
            masterState.getBlock().playerWillDestroy(level, masterPos, masterState, player);
            return masterState;
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        BlockPos masterPos = getMasterPos(level, pos);
        if (masterPos != null) {
            if (!level.isClientSide) {
                BlockState masterState = level.getBlockState(masterPos);
                if (!masterState.isAir()) {
                    return level.destroyBlock(masterPos, true, player);
                }
            }
            return true;
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        BlockPos masterPos = getMasterPos(level, pos);
        if (masterPos != null) {
            BlockState masterState = level.getBlockState(masterPos);
            Block masterBlock = masterState.getBlock();
            return masterBlock.getCloneItemStack(masterState, target, level, masterPos, player);
        }
        return ItemStack.EMPTY;
    }


    private VoxelShape getDelegatedShape(BlockGetter level, BlockPos pos, boolean collision) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof BlockEntityCollisionProxy proxy)) {
            return Shapes.empty();
        }

        BlockPos masterPos = proxy.getMasterPos();
        if (masterPos == null) {
            return Shapes.empty();
        }

        BlockEntity masterBe = level.getBlockEntity(masterPos);
        if (!(masterBe instanceof BlockEntityBase master)) {
            return Shapes.empty();
        }

        return MultiCellCollisionShapeHelper.buildClippedShapeForCell(master, masterPos, pos, collision);
    }

    private @Nullable BlockPos getMasterPos(BlockGetter level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof BlockEntityCollisionProxy proxy) {
            return proxy.getMasterPos();
        }
        return null;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BlockEntityCollisionProxy proxy) {
                if (!level.isClientSide) {
                    BlockPos masterPos = proxy.getMasterPos();
                    if (masterPos != null) {
                        BlockEntity masterBe = level.getBlockEntity(masterPos);
                        if (masterBe instanceof BlockEntityBase master) {
                            master.setChanged();
                        }
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack p_316304_, BlockState p_316362_, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockPos masterPos = getMasterPos(level, pos);
        if (masterPos != null) {
            BlockState masterState = level.getBlockState(masterPos);
            return masterState.useItemOn(p_316304_, level, player, hand, new BlockHitResult(
                    hit.getLocation(),
                    hit.getDirection(),
                    masterPos,
                    hit.isInside()
            ));
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState p_60503_, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        BlockPos masterPos = getMasterPos(level, pos);
        if (masterPos != null) {
            BlockState masterState = level.getBlockState(masterPos);
            return masterState.useWithoutItem(level, player, new BlockHitResult(
                    hit.getLocation(),
                    hit.getDirection(),
                    masterPos,
                    hit.isInside()
            ));
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityCollisionProxy(pos, state);
    }
}