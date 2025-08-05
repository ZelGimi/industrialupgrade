package com.denfop.blocks;

import com.denfop.DataMultiBlock;
import com.denfop.IUItem;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import oshi.util.tuples.Pair;

import java.util.List;

public class BlockTropicalRubWood<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {

    public static final EnumProperty<RubberWoodState> stateProperty = EnumProperty.create("state", RubberWoodState.class);

    public BlockTropicalRubWood(T[] element, DataMultiBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of().mapColor(MapColor.WOOD).destroyTime(1f).randomTicks().sound(SoundType.WOOD).requiresCorrectToolForDrops(), element, dataBlock);
        this.registerDefaultState(this.stateDefinition.any().setValue(stateProperty, RubberWoodState.plain_y));
        BlockTagsProvider.list.add(this);

    }

    public static void spawnAsEntity(Level worldIn, BlockPos pos, ItemStack stack) {
        if (!worldIn.isClientSide && !stack.isEmpty() && !worldIn.restoringBlockSnapshots) {

            float f = 0.5F;
            double d0 = (double) (worldIn.random.nextFloat() * 0.5F) + 0.25D;
            double d1 = (double) (worldIn.random.nextFloat() * 0.5F) + 0.25D;
            double d2 = (double) (worldIn.random.nextFloat() * 0.5F) + 0.25D;
            ItemEntity entityitem = new ItemEntity(worldIn, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, stack);
            entityitem.setDefaultPickUpDelay();
            worldIn.addFreshEntity(entityitem);
        }
    }

    private static RubberWoodState getPlainAxisState(Direction.Axis axis) {
        return switch (axis) {
            case X -> RubberWoodState.plain_x;
            case Y -> RubberWoodState.plain_y;
            case Z -> RubberWoodState.plain_z;
        };
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        RubberWoodState rstate = state.getValue(stateProperty);
        return rstate != RubberWoodState.plain_x && rstate != RubberWoodState.plain_y && rstate != RubberWoodState.plain_z
                ? PushReaction.BLOCK
                : PushReaction.NORMAL;
    }

    @Override
    int getMetaFromState(BlockState state) {
        return state.getValue(stateProperty).getId();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(stateProperty);
    }

    @Override
    public <T extends Enum<T> & ISubEnum> BlockState getStateForPlacement(T element, BlockPlaceContext context) {
        return this.stateDefinition.any().setValue(stateProperty, getPlainAxisState(context.getClickedFace().getAxis()));
    }

    @Override
    public <T extends Enum<T> & ISubEnum> void fillItemCategory(CreativeModeTab p40569, NonNullList<ItemStack> p40570, T element) {
        p40570.add(new ItemStack(this.stateDefinition.any().setValue(stateProperty, RubberWoodState.values()[element.getId()]).getBlock()));
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_60537_, LootParams.Builder p_60538_) {
        if (!p_60538_.getLevel().isClientSide) {
            int count = 1;

            for (int j1 = 0; j1 < count; ++j1) {
                int chance = 1;
                if (!(p_60538_.getLevel().random.nextFloat() > chance)) {
                    Item item = this.getMultiData().getItem(RubberWoodState.plain_y.getId());
                    spawnAsEntity(p_60538_.getLevel(), new BlockPos((int) p_60538_.getParameter(LootContextParams.ORIGIN).x, (int) p_60538_.getParameter(LootContextParams.ORIGIN).y, (int) p_60538_.getParameter(LootContextParams.ORIGIN).z), new ItemStack(item, 1));
                    if (!p_60537_.getValue(stateProperty).isPlain() && p_60538_.getLevel().random.nextInt(6) == 0) {
                        spawnAsEntity(p_60538_.getLevel(), new BlockPos((int) p_60538_.getParameter(LootContextParams.ORIGIN).x, (int) p_60538_.getParameter(LootContextParams.ORIGIN).y, (int) p_60538_.getParameter(LootContextParams.ORIGIN).z), IUItem.latex.copy());
                    }
                }
            }

        }
        return List.of();
    }

    @Override
    public Block getBlock() {
        return this;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextInt(5) == 0) {
            RubberWoodState rwState = state.getValue(stateProperty);
            if (!rwState.canRegenerate()) {
                return;
            }

            world.setBlock(pos, state.setValue(stateProperty, rwState.getWet()), 3);
        }
    }

    @Override
    public void onRemove(BlockState p_60515_, Level world, BlockPos pos, BlockState p_60518_, boolean p_60519_) {
        super.onRemove(p_60515_, world, pos, p_60518_, p_60519_);
        int range = 4;
        BlockPos.MutableBlockPos cPos = new BlockPos.MutableBlockPos();

        for (int y = -range; y <= range; ++y) {
            for (int z = -range; z <= range; ++z) {
                for (int x = -range; x <= range; ++x) {
                    cPos.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    BlockState cState = world.getBlockState(cPos);
                    Block cBlock = cState.getBlock();
                    if (cState.is(BlockTags.LEAVES)) {
                        cBlock.destroy(world, cPos, cState);
                    }
                }
            }
        }
    }


    @Override
    public Pair<String, Integer> getHarvestLevel() {
        return new Pair<>("axe", 0);
    }

    public enum RubberWoodState implements ISubEnum {
        plain_y(Direction.Axis.Y, null, false),
        plain_x(Direction.Axis.X, null, false),
        plain_z(Direction.Axis.Z, null, false),
        dry_north(Direction.Axis.Y, Direction.NORTH, false),
        dry_south(Direction.Axis.Y, Direction.SOUTH, false),
        dry_west(Direction.Axis.Y, Direction.WEST, false),
        dry_east(Direction.Axis.Y, Direction.EAST, false),
        wet_north(Direction.Axis.Y, Direction.NORTH, true),
        wet_south(Direction.Axis.Y, Direction.SOUTH, true),
        wet_west(Direction.Axis.Y, Direction.WEST, true),
        wet_east(Direction.Axis.Y, Direction.EAST, true);

        private static final RubberWoodState[] values = values();
        public final Direction.Axis axis;
        public final Direction facing;
        public final boolean wet;

        RubberWoodState(Direction.Axis axis, Direction facing, boolean wet) {
            this.axis = axis;
            this.facing = facing;
            this.wet = wet;
        }

        public static RubberWoodState getWet(Direction facing) {
            return switch (facing) {
                case NORTH -> wet_north;
                case SOUTH -> wet_south;
                case WEST -> wet_west;
                case EAST -> wet_east;
                default -> throw new IllegalArgumentException("incompatible facing: " + facing);
            };
        }

        @Override
        public boolean registerOnlyBlock() {
            return true;
        }

        @Override
        public int getId() {
            return this.ordinal();
        }

        public String getName() {
            return this.name();
        }

        public boolean registerVariants() {
            return false;
        }

        ;

        @Override
        public String getMainPath() {
            return "tropical_rubber_wood";
        }

        public boolean isPlain() {
            return this.facing == null;
        }

        public boolean canRegenerate() {
            return !this.isPlain() && !this.wet;
        }

        public RubberWoodState getWet() {
            if (this.isPlain()) {
                return null;
            } else {
                return this.wet ? this : values[this.ordinal() + 4];
            }
        }

        public RubberWoodState getDry() {
            return !this.isPlain() && this.wet ? values[this.ordinal() - 4] : this;
        }
    }
}
