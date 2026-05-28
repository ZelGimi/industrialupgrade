package com.denfop.blocks;


import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.dataregistry.DataBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import oshi.util.tuples.Pair;

import javax.annotation.Nullable;

public abstract class AbstractLayerDustBlock<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {

    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

    protected static final VoxelShape[] LAYER_SHAPES = new VoxelShape[]{
            Shapes.empty(),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    protected AbstractLayerDustBlock(
            Properties properties,
            T[] elements,
            T element,
            DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock
    ) {
        super(properties.noOcclusion().sound(SoundType.SNOW).strength(0.2F), elements, element, dataBlock);
        BlockTagsProvider.list.add(this);
        this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, 1));
    }


    @Override
    public int getMetaFromState(BlockState state) {
        return getElement().getId();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState existing = context.getLevel().getBlockState(context.getClickedPos());
        if (existing.is(this)) {
            int layers = existing.getValue(LAYERS);
            return existing.setValue(LAYERS, Math.min(8, layers + 1));
        }
        return this.defaultBlockState().setValue(LAYERS, 1);
    }

    @Override
    public <E extends Enum<E> & ISubEnum> BlockState getStateForPlacement(E element, BlockPlaceContext context) {
        return this.getStateForPlacement(context);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        int layers = state.getValue(LAYERS);
        if (useContext.getItemInHand().is(this.asItem()) && layers < 8) {
            return useContext.getClickedFace() == Direction.UP || useContext.replacingClickedOnBlock();
        }
        return layers == 1;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (belowState.is(this)) {
            return belowState.getValue(LAYERS) == 8;
        }

        if (!belowState.getFluidState().isEmpty()) {
            return false;
        }

        return belowState.isFaceSturdy(level, belowPos, Direction.UP);
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        if (!state.canSurvive(level, pos)) {
            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return LAYER_SHAPES[state.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return LAYER_SHAPES[state.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return LAYER_SHAPES[state.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int layers = state.getValue(LAYERS);

        if (context instanceof EntityCollisionContext entityContext) {
            Entity entity = entityContext.getEntity();
            if (entity != null) {
                if (layers <= getPassThroughLayerThreshold()) {
                    return Shapes.empty();
                }

                if (layers >= getSinkIntoLayerThreshold() && entity instanceof LivingEntity) {
                    return Shapes.empty();
                }
            }
        }

        return LAYER_SHAPES[layers];
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }


    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType p_60478_) {
        return state.getValue(LAYERS) <= 2;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || !(entity instanceof LivingEntity living)) {
            return;
        }

        int layers = state.getValue(LAYERS);

        entity.makeStuckInBlock(
                state,
                new Vec3(
                        0.90D,
                        layers >= getSinkIntoLayerThreshold() ? 0.80D : 0.92D,
                        0.90D
                )
        );
        entity.fallDistance = 0.0F;

        applyDustEffects(state, level, pos, living, layers);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) != 0) {
            return;
        }

        double x = pos.getX() + 0.15D + random.nextDouble() * 0.70D;
        double y = pos.getY() + (state.getValue(LAYERS) / 8.0D) + 0.02D;
        double z = pos.getZ() + 0.15D + random.nextDouble() * 0.70D;

        level.addParticle(getAmbientParticle(), x, y, z, 0.0D, 0.01D, 0.0D);
    }

    protected int getPassThroughLayerThreshold() {
        return 2;
    }

    protected int getSinkIntoLayerThreshold() {
        return 5;
    }

    protected abstract void applyDustEffects(BlockState state, Level level, BlockPos pos, LivingEntity living, int layers);

    protected abstract ParticleOptions getAmbientParticle();

    public abstract int getTopTint();

    public abstract int getSideTint();

    @Override
    public Block getBlock() {
        return this;
    }

    @Override
    public Pair<String, Integer> getHarvestLevel() {
        return new Pair<>("shovel", 0);
    }

    @Override
    public abstract <E extends Enum<E> & ISubEnum> void fillItemCategory(CreativeModeTab tab, net.minecraft.core.NonNullList<ItemStack> items, E element);
}