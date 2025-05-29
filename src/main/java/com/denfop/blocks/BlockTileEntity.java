package com.denfop.blocks;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.tile.IWrenchable;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.blocks.state.TypeProperty;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.tiles.base.TileEntityBlock;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
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

import static com.denfop.api.tile.IMultiTileBlock.CABLE;

public class BlockTileEntity<T extends Enum<T> & IMultiTileBlock> extends Block implements EntityBlock, IWrenchable, IPlantable, IBlockTag {

    public static final Map<BlockPos, TileEntityBlock> teBlockDrop = new HashMap<>();
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
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap.copyOf(Util.make(Maps.newEnumMap(Direction.class), (p_55164_) -> {
        p_55164_.put(Direction.NORTH, NORTH);
        p_55164_.put(Direction.EAST, EAST);
        p_55164_.put(Direction.SOUTH, SOUTH);
        p_55164_.put(Direction.WEST, WEST);
        p_55164_.put(Direction.UP, UP);
        p_55164_.put(Direction.DOWN, DOWN);
    }));
    private static final BlockEntityTicker<TileEntityBlock> TICKER = (level, blockPos, blockState, tileEntityBlock) -> {
        tileEntityBlock.tick();
    };
    public static TypeProperty currentTypeProperty;
    private static IMultiTileBlock preValue;
    public final Property<Direction> facingProperty;
    public final com.denfop.blocks.TileBlockCreator.InfoAboutTile<?> teInfo;
    private final ResourceLocation identifier;
    private final T value;
    public ItemBlockTileEntity<T> item;

    public BlockTileEntity(BlockBehaviour.Properties properties, T value,
                           ResourceLocation identifier,
                           com.denfop.blocks.TileBlockCreator.InfoAboutTile<T> teInfo) {
        super(properties);
        this.value = value;
        this.teInfo = teInfo;
        this.identifier = identifier;
        this.facingProperty = (Property<Direction>) this.stateDefinition.getProperty("facing");
        BlockState state = this.defaultBlockState()
                .setValue(this.typeProperty, this.typeProperty.getState(value, ""));
        if (facingProperty != null)
            state = state.setValue(facingProperty, getPlacementFacing(null, Direction.SOUTH));
        if (value.getMaterial() == CABLE) {
            state.setValue(NORTH, false)
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

    public static <T extends Enum<T> & IMultiTileBlock> BlockTileEntity<T> create(
            T value,
            ResourceLocation identifier,
            com.denfop.blocks.TileBlockCreator.InfoAboutTile<T> infoAboutTile
    ) {
        currentTypeProperty = new TypeProperty(identifier, value);
        preValue = value;
        Properties prop = Properties.of().mapColor(value.getMaterial()).instrument(NoteBlockInstrument.FLUTE).strength(value.getHardness()).isRedstoneConductor((p_61036_, p_61037_, p_61038_) -> {
            TileEntityBlock te = getTe(p_61037_, p_61038_);
            return te != null && te.canConnectRedstone();
        }).noOcclusion().sound(value.getMaterial() == MapColor.WOOL ? SoundType.WOOL : SoundType.STONE);
        if (value.getMaterial() == MapColor.PLANT)
            prop = prop.noCollission();
        if (value.getMaterial() == CABLE)
            prop = prop.explosionResistance(300);
        if (value.getHarvestTool() != HarvestTool.None)
            prop = prop.requiresCorrectToolForDrops();
        BlockTileEntity<T> ret = new BlockTileEntity<>(prop,value, identifier, infoAboutTile);
        currentTypeProperty = null;
        return ret;
    }
    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        return true;
    }

    public boolean skipRendering(BlockState p_53972_, BlockState p_53973_, Direction p_53974_) {
        if (!p_53973_.hasProperty(this.typeProperty))
            return super.skipRendering(p_53972_, p_53973_, p_53974_);
        IMultiTileBlock type = p_53973_.getValue(this.typeProperty).teBlock;
        return this.value == type || super.skipRendering(p_53972_, p_53973_, p_53974_);
    }
    private static TileEntityBlock getTe(Level world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        return te instanceof TileEntityBlock ? (TileEntityBlock) te : null;
    }    public TypeProperty typeProperty = this.getTypeProperty();

    private static TileEntityBlock getTe(BlockGetter getter, BlockPos pos) {
        BlockEntity blockEntity = getter.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityBlock te) {
            return te;
        }
        return null;
    }

    public void setItem(ItemBlockTileEntity<T> item) {
        this.item = item;
    }

    public T getValue() {
        return value;
    }

    public void entityInside(BlockState state, Level level, BlockPos pos,  Entity entity) {


        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityBlock te) {
            te.onEntityCollision(entity);
        }

    }

    public void setPlacedBy(Level level,BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        TileEntityBlock tileEntityBlock = getTe(level, blockPos);
        if (tileEntityBlock != null) {
            tileEntityBlock.onPlaced(itemStack, livingEntity, tileEntityBlock.getFacing());
        }
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityBlock) {
            return ((TileEntityBlock) te).getLightOpacity();
        }
        return 0;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityBlock) {
            return ((TileEntityBlock) te).getComparatorInputOverride();
        }
        return 0;
    }

    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world,  BlockPos pos, CollisionContext collisionContext) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? super.getShape(state, world, pos, collisionContext) : Shapes.create(te.getVisualBoundingBox());

    }

    @Override
    public boolean hasDynamicShape() {
        return true;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityBlock) {
            return Shapes.create(((TileEntityBlock) te).getOutlineBoundingBox());
        }
        return super.getInteractionShape(state, world, pos);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if(!this.hasCollision)
            return Shapes.empty();
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityBlock) {
            return Shapes.create(((TileEntityBlock) te).getPhysicsBoundingBox());
        }
        return super.getCollisionShape(state, world, pos, context);
    }

    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? super.getOcclusionShape(state, world, pos) : Shapes.create(te.getPhysicsBoundingBox());

    }

    private TypeProperty getTypeProperty() {
        if (this.typeProperty != null)
            return this.typeProperty;
        this.typeProperty = currentTypeProperty;
        return typeProperty;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_);
        Set<Direction> set = preValue.getSupportedFacings();
        if (set.equals(ModUtils.allFacings)) {
            p_49915_.add(ALL_FACING_PROPERTY);
        } else if (set.equals(ModUtils.horizontalFacings)) {
            p_49915_.add(HORIZONTAL_FACING_PROPERTY);
        } else if (set.equals(ModUtils.verticalFacings)) {
            p_49915_.add(VERTICAL_FACING_PROPERTY);
        } else if (set.equals(ModUtils.downSideFacings)) {
            p_49915_.add(DOWN_FACING_PROPERTY);
        }
        p_49915_.add(this.getTypeProperty());
        if (preValue.getMaterial() == CABLE)
            PROPERTY_BY_DIRECTION.values().forEach(p_49915_::add);



    }

    @Override
    public Direction getFacing(Level world, BlockPos pos) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? Direction.DOWN : te.getFacing();
    }

    @Override
    public boolean setFacing(Level world, BlockPos pos, Direction newDirection, Player player) {
        TileEntityBlock te = getTe(world, pos);
        return te != null && te.canSetFacingWrench(newDirection, player);
    }


    @Override
    public boolean wrenchCanRemove(Level world, BlockPos pos, Player player) {
        TileEntityBlock te = getTe(world, pos);
        return te != null && te.wrenchCanRemove(player);
    }


    @Override
    public List<ItemStack> getWrenchDrops(Level var1, BlockPos var2, BlockState var3, BlockEntity te, Player player, int fortune) {
        final List<ItemStack> list = ((TileEntityBlock) te).getWrenchDrops(
                player,
                fortune
        );
        return list;
    }


    @Override
    public void wrenchBreak(Level world, BlockPos pos) {
        TileEntityBlock tileEntityBlock = (TileEntityBlock) world.getBlockEntity(pos);
        if (tileEntityBlock != null) {
            tileEntityBlock.wrenchBreak();
        }
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        try {
            return Objects.requireNonNull(this.value.getTeClass()).getConstructor(BlockPos.class, BlockState.class).newInstance(blockPos, blockState);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    public void destroy(LevelAccessor p_49860_, BlockPos p_49861_, BlockState p_49862_) {

    }

    @Override
    public void playerWillDestroy(Level p_176208_1_, BlockPos p_176208_2_, BlockState p_176208_3_, Player p_176208_4_) {
        this.spawnDestroyParticles(p_176208_1_, p_176208_4_, p_176208_2_, p_176208_3_);
        p_176208_1_.playSound(p_176208_4_,p_176208_2_,this.soundType.getBreakSound(), SoundSource.BLOCKS, this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);

    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState p_60457_) {
        return true;
    }


    @Override
    public int getSignal(BlockState p_60483_, BlockGetter p_60484_, BlockPos p_60485_, Direction p_60486_) {
        BlockEntity te = p_60484_.getBlockEntity(p_60485_);
        if (te instanceof TileEntityBlock) {
            return ((TileEntityBlock) te).getWeakPower(p_60486_);
        }
        return 0;
    }

    @Override
    public void playerDestroy(Level p_49827_, Player p_49828_, BlockPos p_49829_, BlockState p_49830_, @Nullable BlockEntity p_49831_, ItemStack p_49832_) {
        super.playerDestroy(p_49827_, p_49828_, p_49829_, p_49830_, p_49831_, p_49832_);
    }

    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        TileEntityBlock te = getTe(world, pos);
        if (te != null) {
            if (!te.onRemovedByPlayer(player, willHarvest)) {
                return false;
            }
            teBlockDrop.put(pos, te);
        }

        return super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    public ItemStack getItemStack() {
        return new ItemStack(this.item, 1);
    }

    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos neighbor, boolean p_60514_) {
        this.onNeighborChange(state, level, pos, neighbor);
    }

    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityBlock te) {
            te.onNeighborChange(state, neighbor);
        }

    }

    public <E extends BlockEntity> BlockEntityTicker<E> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<E> blockEntityType) {
        return (BlockEntityTicker<E>) TICKER;
    }

    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityBlock te) {
            return te.getLightValue();
        } else {
            return super.getLightEmission(state, level, pos);
        }
    }

    private Direction getPlacementFacing(LivingEntity livingEntity, Direction direction) {
        Set<Direction> set = this.value.getSupportedFacings();
        if (set.isEmpty()) {
            return Direction.DOWN;
        } else if (livingEntity != null) {
            Vec3 vec3 = livingEntity.getLookAngle();
            Direction direction2 = null;
            double d = Double.NEGATIVE_INFINITY;

            for (Direction direction3 : set) {
                double d2 = vec3.dot(Vec3.atLowerCornerOf(direction3.getOpposite().getNormal()));
                if (d2 > d) {
                    d = d2;
                    direction2 = direction3;
                }
            }

            return direction2;
        } else {
            return direction != null && set.contains(direction.getOpposite()) ? direction.getOpposite() : (Direction) this.value.getSupportedFacings().iterator().next();
        }
    }

    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        BlockState blockState = super.getStateForPlacement(blockPlaceContext);
        if (facingProperty != null)
            blockState = blockState.setValue(facingProperty, this.getPlacementFacing(blockPlaceContext.getPlayer(), blockPlaceContext.getNearestLookingDirection()));
        blockState = blockState.setValue(typeProperty, typeProperty.getState(value));
        if (value.getMaterial() == CABLE) {
            blockState = blockState.setValue(NORTH, Boolean.valueOf(false))
                    .setValue(SOUTH, Boolean.valueOf(false))
                    .setValue(WEST, Boolean.valueOf(false))
                    .setValue(EAST, Boolean.valueOf(false))
                    .setValue(UP, Boolean.valueOf(false))
                    .setValue(DOWN, Boolean.valueOf(false));
        }
        return blockState;
    }

    public @NotNull InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (player.isSecondaryUseActive()) {
            TileEntityBlock te = getTe(level, blockPos);
            return te == null ? InteractionResult.PASS : getResult(te.onSneakingActivated(player, interactionHand, blockHitResult.getDirection(), blockHitResult.getLocation()));
        } else {
            TileEntityBlock te = getTe(level, blockPos);
            return te == null ? InteractionResult.PASS : te.getCooldownTracker().getTick() == 0 ? getResult(te.onActivated(player, interactionHand, blockHitResult.getDirection(), blockHitResult.getLocation())) : InteractionResult.PASS;
        }

    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        TileEntityBlock te = getTe(level, pos);

        return te == null ? ModUtils.emptyStack : te.getPickBlock(player, target);
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_60537_, LootParams.Builder p_60538_) {
        BlockEntity blockEntity = null;
        try {
            blockEntity = p_60538_.getParameter(LootContextParams.BLOCK_ENTITY);
        }catch (Exception e){};
        if (blockEntity == null) {
            Vec3 vec3 = p_60538_.getParameter(LootContextParams.ORIGIN);
            return this.getDrops(p_60538_.getLevel(), new BlockPos((int) vec3.x, (int) vec3.y, (int) vec3.z), p_60537_, p_60538_.getParameter(LootContextParams.THIS_ENTITY));
        }
        Entity entity = null;
        try {
            entity = p_60538_.getParameter(LootContextParams.THIS_ENTITY);
        }catch (Exception e){};
        return this.getDrops(p_60538_.getLevel(), blockEntity.getBlockPos(), p_60537_,entity);

    }



    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        boolean ret = super.canHarvestBlock(state, world, pos, player);
        if (ret) {
            return ret;
        } else {
            TileEntityBlock te = getTe(world, pos);
            if (te == null) {
                return false;
            } else {
                if (te.canEntityDestroy(player)) {
                    switch (te.teBlock.getHarvestTool()) {
                        case None:
                            return true;
                        case Axe:
                            ItemStack stack = player.getMainHandItem();
                            if (!stack.isEmpty()) {
                                return stack.is(ItemTags.create(new ResourceLocation("forge", "tools/axes")));
                            }
                            break;
                        case Pickaxe:
                            stack = player.getMainHandItem();
                            if (!stack.isEmpty()) {
                                return stack.is(ItemTags.create(new ResourceLocation("forge", "tools/pickaxes")));
                            }
                            break;
                        case Shovel:
                            stack = player.getMainHandItem();
                            if (!stack.isEmpty()) {
                                return stack.is(ItemTags.create(new ResourceLocation("forge", "tools/shovels")));
                            }
                            break;
                        case Wrench:
                            stack = player.getMainHandItem();
                            if (!stack.isEmpty()) {
                                return stack.is(ItemTags.create(new ResourceLocation("forge", "tools/wrench")));
                            }
                            break;
                        default:
                            return false;
                    }
                }
            }
        }
        return false;
    }

    public List<ItemStack> getDrops(Level world, BlockPos pos, BlockState state, Entity player) {
        TileEntityBlock te = getTe(world, pos);
        if (te == null) {
            te = teBlockDrop.get(pos);
            if (te == null) {
                return new ArrayList<>();
            } else {
                List<ItemStack> ret = new ArrayList<>();
                boolean wasWrench = false;
                if (player instanceof Player) {
                    ItemStack stack = ((Player) player).getMainHandItem();
                    if (!stack.isEmpty()) {
                        wasWrench = stack.is(ItemTags.create(new ResourceLocation("forge", "tools/wrench")));
                    }
                }
                final int chance = te.getLevel().random.nextInt(100);
                ret.addAll(te.getSelfDrops(chance, wasWrench));
                ret.addAll(te.getAuxDrops(chance));
                teBlockDrop.remove(pos);
                return ret;
            }
        }
        List<ItemStack> ret = new ArrayList<>();
        boolean wasWrench = false;
        if (player instanceof Player) {
            ItemStack stack = ((Player) player).getMainHandItem();
            if (!stack.isEmpty()) {
                wasWrench = stack.is(ItemTags.create(new ResourceLocation("forge", "tools/wrench")));
            }
        }
        final int chance = te.getLevel().random.nextInt(100);
        ret.addAll(te.getSelfDrops(chance, wasWrench));
        ret.addAll(te.getAuxDrops(chance));
        return ret;
    }

    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, BlockState blockState2, boolean b) {
        TileEntityBlock te = getTe(level, blockPos);
        if (te != null && blockState2.getBlock() != blockState.getBlock()) {
            te.onBlockBreak(false);
            te.onUnloaded();
            level.removeBlock(te.getPos(), false);
        }

        super.onRemove(blockState, level, blockPos, blockState2, b);

    }

    @Override
    public void attack(BlockState p_60499_, Level world, BlockPos pos, Player player) {
        super.attack(p_60499_, world, pos, player);
        TileEntityBlock te = getTe(world, pos);
        if (te != null)
            te.onClicked(player);
    }



    @Override
    public boolean isCollisionShapeFullBlock(BlockState p_181242_, BlockGetter p_181243_, BlockPos p_181244_) {
        return super.isCollisionShapeFullBlock(p_181242_, p_181243_, p_181244_);
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
        TileEntityBlock te = (TileEntityBlock) level.getBlockEntity(pos);
        return te == null ? TileEntityBlock.noCrop : te.getPlantType();
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
