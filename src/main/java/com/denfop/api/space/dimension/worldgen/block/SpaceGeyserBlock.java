package com.denfop.api.space.dimension.worldgen.block;

import com.denfop.api.space.dimension.SpaceVentType;
import com.denfop.api.space.dimension.worldgen.SpaceWorldgenContent;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class SpaceGeyserBlock extends BaseEntityBlock {

    public static final EnumProperty<SpaceVentType> VENT_TYPE = EnumProperty.create("vent_type", SpaceVentType.class);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final MapCodec<SpaceGeyserBlock> CODEC = simpleCodec((p_304364_) -> new SpaceGeyserBlock());

    public SpaceGeyserBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(3.5F, 8.0F)
                .sound(SoundType.STONE)
                .lightLevel(state -> state.getValue(VENT_TYPE) == SpaceVentType.LAVA ? 12 : 0)
                .pushReaction(PushReaction.BLOCK));

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(VENT_TYPE, SpaceVentType.STEAM)
                .setValue(ACTIVE, Boolean.FALSE));
    }

    private static boolean isValidSourceState(final BlockState sourceState) {
        return sourceState != null
                && !sourceState.isAir()
                && !(sourceState.getBlock() instanceof SpaceGeyserBlock);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(final BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new SpaceGeyserBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            final Level level,
            final BlockState state,
            final BlockEntityType<T> type
    ) {
        return createTickerHelper(
                type,
                SpaceWorldgenContent.SPACE_GEYSER_BE.get(),
                level.isClientSide ? SpaceGeyserBlockEntity::clientTick : SpaceGeyserBlockEntity::serverTick
        );
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VENT_TYPE, ACTIVE);
    }

    @Override
    public void setPlacedBy(
            final Level level,
            final BlockPos pos,
            final BlockState state,
            @Nullable final LivingEntity placer,
            final ItemStack stack
    ) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (!level.isClientSide) {
            final BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SpaceGeyserBlockEntity geyser) {
                geyser.captureSourceState(true);
            }
        }
    }

    @Override
    public void onPlace(
            final BlockState state,
            final Level level,
            final BlockPos pos,
            final BlockState oldState,
            final boolean movedByPiston
    ) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (!level.isClientSide && !oldState.is(state.getBlock())) {
            final BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SpaceGeyserBlockEntity geyser) {
                geyser.captureSourceState(true);
            }
        }
    }

    @Override
    public void neighborChanged(
            final BlockState state,
            final Level level,
            final BlockPos pos,
            final Block neighborBlock,
            final BlockPos fromPos,
            final boolean isMoving
    ) {
        super.neighborChanged(state, level, pos, neighborBlock, fromPos, isMoving);

        if (!level.isClientSide && fromPos.equals(pos.below())) {
            final BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SpaceGeyserBlockEntity geyser) {
                geyser.captureSourceState(true);
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(final BlockState state, final LootParams.Builder builder) {
        final BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof SpaceGeyserBlockEntity geyser) {
            final BlockState sourceState = geyser.getSourceState();
            if (isValidSourceState(sourceState)) {
                final Item sourceItem = sourceState.getBlock().asItem();
                if (sourceItem != Items.AIR) {
                    return List.of(new ItemStack(sourceItem));
                }
            }
        }

        return super.getDrops(state, builder);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        final BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof SpaceGeyserBlockEntity geyser) {
            final BlockState sourceState = geyser.getSourceState();
            if (isValidSourceState(sourceState)) {
                final BlockPos sourcePos = pos.below();
                final BlockHitResult redirectedHit = new BlockHitResult(
                        Vec3.atCenterOf(sourcePos),
                        target instanceof BlockHitResult bhr ? bhr.getDirection() : net.minecraft.core.Direction.UP,
                        sourcePos,
                        false
                );

                final ItemStack delegated = sourceState.getCloneItemStack(redirectedHit, level, sourcePos, player);
                if (!delegated.isEmpty()) {
                    return delegated;
                }

                final Item sourceItem = sourceState.getBlock().asItem();
                if (sourceItem != Items.AIR) {
                    return new ItemStack(sourceItem);
                }
            }
        }
        return super.getCloneItemStack(state, target, level, pos, player);
    }
}