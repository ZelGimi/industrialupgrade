package com.denfop.blocks;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.blockentity.Wrenchable;
import com.denfop.api.collision.IMultiCellCollisionProvider;
import com.denfop.api.collision.MultiCellCollisionShapeHelper;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.blocks.state.TypeProperty;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.items.energy.ItemGraviTool;
import com.denfop.items.energy.ItemToolWrench;
import com.denfop.utils.ModUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.denfop.api.blockentity.MultiBlockEntity.CABLE;

public class BlockTileEntity<T extends Enum<T> & MultiBlockEntity> extends Block implements EntityBlock, Wrenchable, IPlantable, IBlockTag {

    public static final Property<Direction> ALL_FACING_PROPERTY = DirectionProperty.create("facing", ModUtils.allFacings);
    public static final Property<Direction> HORIZONTAL_FACING_PROPERTY = DirectionProperty.create("facing", ModUtils.horizontalFacings);
    public static final Property<Direction> VERTICAL_FACING_PROPERTY = DirectionProperty.create("facing", ModUtils.verticalFacings);
    public static final Property<Direction> DOWN_FACING_PROPERTY = DirectionProperty.create("facing", ModUtils.downSideFacings);

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION =
            ImmutableMap.copyOf(Util.make(Maps.newEnumMap(Direction.class), map -> {
                map.put(Direction.NORTH, NORTH);
                map.put(Direction.EAST, EAST);
                map.put(Direction.SOUTH, SOUTH);
                map.put(Direction.WEST, WEST);
                map.put(Direction.UP, UP);
                map.put(Direction.DOWN, DOWN);
            }));

    private static final BlockEntityTicker<BlockEntityBase> TICKER = (level, blockPos, blockState, tileEntityBlock) -> {
        tileEntityBlock.tick();
    };

    public static TypeProperty currentTypeProperty;
    private static MultiBlockEntity preValue;

    public final Property<Direction> facingProperty;
    public final com.denfop.blocks.TileBlockCreator.InfoAboutTile<?> teInfo;
    private final ResourceLocation identifier;
    private final T value;
    public ItemBlockTileEntity<T> item;
    String descriptionId;    public TypeProperty typeProperty = this.getTypeProperty();

    public BlockTileEntity(
            BlockBehaviour.Properties properties,
            T value,
            ResourceLocation identifier,
            com.denfop.blocks.TileBlockCreator.InfoAboutTile<T> teInfo
    ) {
        super(properties);
        this.value = value;
        this.teInfo = teInfo;
        this.identifier = identifier;
        this.facingProperty = (Property<Direction>) this.stateDefinition.getProperty("facing");

        BlockState state = this.defaultBlockState()
                .setValue(this.typeProperty, this.typeProperty.getState(value, ""));

        if (facingProperty != null) {
            state = state.setValue(facingProperty, getPlacementFacing(null, Direction.SOUTH));
        }

        if (value.getMaterial() == CABLE) {
            state = state.setValue(NORTH, false)
                    .setValue(SOUTH, false)
                    .setValue(WEST, false)
                    .setValue(EAST, false)
                    .setValue(UP, false)
                    .setValue(DOWN, false);
        }

        this.registerDefaultState(state);
        value.setDefaultState(this.defaultBlockState());
        BlockTagsProvider.list.add(this);
    }

    public static <T extends Enum<T> & MultiBlockEntity> BlockTileEntity<T> create(
            T value,
            ResourceLocation identifier,
            com.denfop.blocks.TileBlockCreator.InfoAboutTile<T> infoAboutTile
    ) {
        currentTypeProperty = new TypeProperty(identifier, value);

        preValue = value;
        Properties prop = Properties.of()
                .mapColor(value.getMaterial())
                .instrument(NoteBlockInstrument.FLUTE)
                .strength(value.getHardness())
                .isRedstoneConductor((state, level, pos) -> {
                    BlockEntityBase te = getTe(level, pos);
                    return te != null && te.canConnectRedstone();
                })
                .noOcclusion().forceSolidOn()
                .sound(value.getMaterial() == MapColor.WOOL ? SoundType.WOOL : SoundType.STONE);

        if (value.getMaterial() == MapColor.PLANT) {
            prop = prop.noCollission();
        }

        if (value.getMaterial() == CABLE) {
            prop = prop.explosionResistance(300);
        }

        if (value.getHarvestTool() != HarvestTool.None) {
            prop = prop.requiresCorrectToolForDrops();
        }

        BlockTileEntity<T> ret = new BlockTileEntity<>(prop, value, identifier, infoAboutTile);
        currentTypeProperty = null;
        return ret;
    }

    private static BlockEntityBase getTe(Level world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        return te instanceof BlockEntityBase ? (BlockEntityBase) te : null;
    }

    private static BlockEntityBase getTe(BlockGetter getter, BlockPos pos) {
        BlockEntity blockEntity = getter.getBlockEntity(pos);
        if (blockEntity instanceof BlockEntityBase te) {
            return te;
        }
        return null;
    }

    private static VoxelShape buildShapeFromAabbs(@Nullable List<AABB> boxes) {
        if (boxes == null || boxes.isEmpty()) {
            return Shapes.empty();
        }

        VoxelShape shape = Shapes.empty();
        for (AABB box : boxes) {
            if (box == null) {
                continue;
            }
            shape = Shapes.or(shape, Shapes.create(box));
        }
        return shape.optimize();
    }

    private VoxelShape getTileShape(BlockGetter level, BlockPos pos, boolean collision) {
        BlockEntityBase te = getTe(level, pos);
        if (te == null) {
            return Shapes.empty();
        }
        return buildShapeFromAabbs(te.getAabbs(collision));
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (!this.canHarvestBlock(state, level, pos, player)) {
            return 0;
        }
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return this.getOcclusionShape(state, level, pos).isEmpty();
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction side) {
        if (!adjacentState.hasProperty(this.typeProperty)) {
            return super.skipRendering(state, adjacentState, side);
        }
        MultiBlockEntity type = adjacentState.getValue(this.typeProperty).teBlock;
        return this.value == type || super.skipRendering(state, adjacentState, side);
    }

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return false;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    public void setItem(ItemBlockTileEntity<T> item) {
        this.item = item;
    }

    public T getValue() {
        return value;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BlockEntityBase te) {
            te.onEntityCollision(entity);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        BlockEntityBase blockEntityBase = getTe(level, blockPos);
        if (blockEntityBase != null) {
            blockEntityBase.onPlaced(itemStack, livingEntity, blockEntityBase.getFacing());
        }
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof BlockEntityBase blockEntityBase) {
            return blockEntityBase.getLightOpacity();
        }
        return 0;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof BlockEntityBase blockEntityBase) {
            return blockEntityBase.getComparatorInputOverride();
        }
        return 0;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
        BlockEntityBase te = getTe(world, pos);
        if (te != null) {
            if (useMultiCellCollision(world, pos)) {
                VoxelShape local = MultiCellCollisionShapeHelper.buildClippedShapeForCell(te, pos, pos, false);
                return local.isEmpty() ? Shapes.empty() : local;
            }

            VoxelShape shape = Shapes.empty();
            for (AABB box : te.getAabbs(false)) {
                shape = Shapes.or(shape, Shapes.create(box));
            }
            return shape.optimize();
        }
        return super.getShape(state, world, pos, collisionContext);
    }

    @Override
    public boolean hasDynamicShape() {
        return true;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        BlockEntityBase te = getTe(world, pos);
        if (te != null) {
            if (useMultiCellCollision(world, pos)) {
                VoxelShape local = MultiCellCollisionShapeHelper.buildClippedShapeForCell(te, pos, pos, false);
                return local.isEmpty() ? Shapes.empty() : local;
            }

            VoxelShape shape = Shapes.empty();
            for (AABB box : te.getAabbs(false)) {
                shape = Shapes.or(shape, Shapes.create(box));
            }
            return shape.optimize();
        }
        return super.getInteractionShape(state, world, pos);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (!this.hasCollision) {
            return Shapes.empty();
        }

        BlockEntityBase te = getTe(world, pos);
        if (te != null) {
            if (useMultiCellCollision(world, pos)) {
                VoxelShape local = MultiCellCollisionShapeHelper.buildClippedShapeForCell(te, pos, pos, true);
                return local.isEmpty() ? Shapes.empty() : local;
            }

            VoxelShape shape = Shapes.empty();
            for (AABB box : te.getAabbs(true)) {
                shape = Shapes.or(shape, Shapes.create(box));
            }
            return shape.optimize();
        }

        return super.getCollisionShape(state, world, pos, context);
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return false;
    }

    @Override
    public @NotNull VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        BlockEntityBase te = getTe(world, pos);
        if (te != null) {
            if (useMultiCellCollision(world, pos)) {
                VoxelShape local = MultiCellCollisionShapeHelper.buildClippedShapeForCell(te, pos, pos, true);
                return local.isEmpty() ? Shapes.empty() : local;
            }

            VoxelShape shape = Shapes.empty();
            for (AABB box : te.getAabbs(true)) {
                shape = Shapes.or(shape, Shapes.create(box));
            }
            return shape.optimize();
        }
        return super.getBlockSupportShape(state, world, pos);
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    private TypeProperty getTypeProperty() {
        if (this.typeProperty != null) {
            return this.typeProperty;
        }
        this.typeProperty = currentTypeProperty;
        return typeProperty;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ALL_FACING_PROPERTY);
        builder.add(this.getTypeProperty());

        if (preValue.getMaterial() == CABLE) {
            PROPERTY_BY_DIRECTION.values().forEach(builder::add);
        }
    }

    @Override
    public Direction getFacing(Level world, BlockPos pos) {
        BlockEntityBase te = getTe(world, pos);
        return te == null ? Direction.DOWN : te.getFacing();
    }

    @Override
    public boolean setFacing(Level world, BlockPos pos, Direction newDirection, Player player) {
        BlockEntityBase te = getTe(world, pos);
        return te != null && te.canSetFacingWrench(newDirection, player);
    }

    @Override
    public boolean wrenchCanRemove(Level world, BlockPos pos, Player player) {
        BlockEntityBase te = getTe(world, pos);
        return te != null && te.wrenchCanRemove(player);
    }

    @Override
    public List<ItemStack> getWrenchDrops(Level level, BlockPos pos, BlockState state, BlockEntity te, Player player, int fortune) {
        return ((BlockEntityBase) te).getWrenchDrops(player, fortune);
    }

    @Override
    public void wrenchBreak(Level world, BlockPos pos) {
        BlockEntityBase blockEntityBase = (BlockEntityBase) world.getBlockEntity(pos);
        if (blockEntityBase != null) {
            blockEntityBase.wrenchBreak();
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        try {
            return Objects.requireNonNull(this.value.getTeClass())
                    .getConstructor(BlockPos.class, BlockState.class)
                    .newInstance(blockPos, blockState);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        this.spawnDestroyParticles(level, player, pos, state);
        level.playSound(player, pos, this.soundType.getBreakSound(), SoundSource.BLOCKS, this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockEntity te = level.getBlockEntity(pos);
        if (te instanceof BlockEntityBase blockEntityBase) {
            return blockEntityBase.getWeakPower(direction);
        }
        return 0;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        BlockEntityBase te = (BlockEntityBase) blockEntity;
        if (te != null) {
            List<ItemStack> ret = new ArrayList<>();
            boolean wasWrench = false;

            ItemStack stack = player.getMainHandItem();
            if (!stack.isEmpty()) {
                wasWrench = stack.is(ItemTags.create(new ResourceLocation("forge", "tools/wrench")));
            }

            final int chance = te.getLevel().random.nextInt(100);
            ret.addAll(te.getSelfDrops(chance, wasWrench));
            ret.addAll(te.getAuxDrops(chance));

            for (ItemStack drop : ret) {
                if (!drop.isEmpty()) {
                    popResource(level, pos, drop);
                }
            }
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        BlockEntityBase te = getTe(world, pos);
        if (te != null && !te.onRemovedByPlayer(player, willHarvest)) {
            return false;
        }
        return super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    public ItemStack getItemStack() {
        return new ItemStack(this.item, 1);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos neighbor, boolean movedByPiston) {
        this.onNeighborChange(state, level, pos, neighbor);
    }

    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BlockEntityBase te) {
            te.onNeighborChange(level.getBlockState(neighbor), neighbor);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends BlockEntity> BlockEntityTicker<E> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<E> blockEntityType) {
        return (BlockEntityTicker<E>) TICKER;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BlockEntityBase te) {
            return te.getLightValue();
        }
        return super.getLightEmission(state, level, pos);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    private Direction getPlacementFacing(LivingEntity livingEntity, Direction direction) {
        Set<Direction> set = this.value.getSupportedFacings();

        if (set.isEmpty()) {
            return Direction.DOWN;
        } else if (livingEntity != null) {
            Vec3 vec3 = livingEntity.getLookAngle();
            Direction bestDirection = null;
            double bestDot = Double.NEGATIVE_INFINITY;

            for (Direction candidate : set) {
                double dot = vec3.dot(Vec3.atLowerCornerOf(candidate.getOpposite().getNormal()));
                if (dot > bestDot) {
                    bestDot = dot;
                    bestDirection = candidate;
                }
            }

            return bestDirection;
        } else {
            return direction != null && set.contains(direction.getOpposite())
                    ? direction.getOpposite()
                    : this.value.getSupportedFacings().iterator().next();
        }
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState blockState = super.getStateForPlacement(context);

        if (facingProperty != null) {
            blockState = blockState.setValue(facingProperty, this.getPlacementFacing(context.getPlayer(), context.getNearestLookingDirection()));
        }

        blockState = blockState.setValue(typeProperty, typeProperty.getState(value));

        if (value.getMaterial() == CABLE) {
            blockState = blockState.setValue(NORTH, false)
                    .setValue(SOUTH, false)
                    .setValue(WEST, false)
                    .setValue(EAST, false)
                    .setValue(UP, false)
                    .setValue(DOWN, false);
        }

        return blockState;
    }

    private boolean isWrench(Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        return stack.getItem() instanceof ItemToolWrench || stack.getItem() instanceof ItemGraviTool;
    }

    @Override
    public @NotNull InteractionResult use(
            @NotNull BlockState blockState,
            @NotNull Level level,
            @NotNull BlockPos blockPos,
            Player player,
            @NotNull InteractionHand interactionHand,
            @NotNull BlockHitResult blockHitResult
    ) {
        BlockEntityBase te = getTe(level, blockPos);

        if (player.isSecondaryUseActive()) {
            return te == null || isWrench(player, interactionHand)
                    ? InteractionResult.PASS
                    : getResult(te.onSneakingActivated(player, interactionHand, blockHitResult.getDirection(), blockHitResult.getLocation()));
        } else {
            return te == null || isWrench(player, interactionHand)
                    ? InteractionResult.PASS
                    : te.getCooldownTracker().getTick() == 0
                    ? getResult(te.onActivated(player, interactionHand, blockHitResult.getDirection(), blockHitResult.getLocation()))
                    : InteractionResult.PASS;
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        BlockEntityBase te = getTe(level, pos);
        return te == null ? ModUtils.emptyStack : te.getPickBlock(player, target);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = null;
        try {
            blockEntity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
        } catch (Exception ignored) {
        }

        if (blockEntity == null) {
            Vec3 vec3 = builder.getParameter(LootContextParams.ORIGIN);
            return this.getDrops(builder.getLevel(), new BlockPos((int) vec3.x, (int) vec3.y, (int) vec3.z), state, builder.getParameter(LootContextParams.THIS_ENTITY));
        }

        Entity entity = null;
        try {
            entity = builder.getParameter(LootContextParams.THIS_ENTITY);
        } catch (Exception ignored) {
        }

        return this.getDrops(builder.getLevel(), blockEntity.getBlockPos(), state, entity);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        boolean ret = super.canHarvestBlock(state, world, pos, player);

        BlockEntityBase te = getTe(world, pos);
        if (te == null) {
            return false;
        }

        if (!te.canEntityDestroy(player)) {
            return false;
        }

        switch (te.teBlock.getHarvestTool()) {
            case None:
                return true;

            case Axe: {
                ItemStack stack = player.getMainHandItem();
                return !stack.isEmpty() && stack.is(ItemTags.create(new ResourceLocation("minecraft", "axes"))) && ret;
            }

            case Pickaxe: {
                ItemStack stack = player.getMainHandItem();
                return !stack.isEmpty() && stack.is(ItemTags.create(new ResourceLocation("minecraft", "pickaxes"))) && ret;
            }

            case Shovel: {
                ItemStack stack = player.getMainHandItem();
                return !stack.isEmpty() && stack.is(ItemTags.create(new ResourceLocation("minecraft", "shovels"))) && ret;
            }

            case Wrench: {
                ItemStack stack = player.getMainHandItem();
                return !stack.isEmpty() && stack.is(ItemTags.create(new ResourceLocation("minecraft", "wrench"))) && ret;
            }

            default:
                return false;
        }
    }

    @Override
    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = this.item.getDescriptionId();
        }
        return this.descriptionId;
    }

    public List<ItemStack> getDrops(Level world, BlockPos pos, BlockState state, Entity player) {
        BlockEntityBase te = getTe(world, pos);
        if (te == null) {
            return new ArrayList<>();
        }

        List<ItemStack> ret = new ArrayList<>();
        boolean wasWrench = false;

        if (player instanceof Player p) {
            ItemStack stack = p.getMainHandItem();
            if (!stack.isEmpty()) {
                wasWrench = stack.is(ItemTags.create(new ResourceLocation("forge", "tools/wrench")));
            }
        }

        final int chance = te.getLevel().random.nextInt(100);
        ret.addAll(te.getSelfDrops(chance, wasWrench));
        ret.addAll(te.getAuxDrops(chance));
        return ret;
    }

    @Override
    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, BlockState newState, boolean movedByPiston) {
        BlockEntityBase te = getTe(level, blockPos);
        if (te != null && newState.getBlock() != blockState.getBlock()) {
            te.onBlockBreak(false);
            te.onUnloaded();
            level.removeBlock(te.getPos(), false);
        }

        super.onRemove(blockState, level, blockPos, newState, movedByPiston);
    }

    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        super.attack(state, world, pos, player);
        BlockEntityBase te = getTe(world, pos);
        if (te != null) {
            te.onClicked(player);
        }
    }

    public InteractionResult getResult(boolean result) {
        return result ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    @Override
    public BlockState getPlant(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos);
    }

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        BlockEntityBase te = (BlockEntityBase) level.getBlockEntity(pos);
        return te == null ? BlockEntityBase.noCrop : te.getPlantType();
    }

    private boolean useMultiCellCollision(BlockGetter level, BlockPos pos) {
        BlockEntityBase te = getTe(level, pos);
        if (te instanceof IMultiCellCollisionProvider) {
            return ((IMultiCellCollisionProvider) te).useMultiCellCollision();
        }
        return false;
    }

    @Override
    public Block getBlock() {
        return this;
    }

    @Override
    public Pair<String, Integer> getHarvestLevel() {
        return new Pair<>(value.getHarvestTool().toolClass, 1);
    }


}