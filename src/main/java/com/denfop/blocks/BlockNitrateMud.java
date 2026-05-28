package com.denfop.blocks;

import com.denfop.IUItem;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.dataregistry.DataBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BlockNitrateMud<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);
    public static final int STAGE_TIME = (int) (20 * 120);

    public BlockNitrateMud(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY)
                .strength(0.6F)
                .sound(SoundType.GRAVEL), elements, element, dataBlock);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
        BlockTagsProvider.list.add(this);

    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder p_60538_) {
        return Collections.singletonList(new ItemStack(this.stateDefinition.any().getBlock()));
    }

    @Override
    public <T extends Enum<T> & ISubEnum> BlockState getStateForPlacement(T element, BlockPlaceContext context) {
        return this.stateDefinition.any().setValue(STAGE, 0);
    }

    @Override
    public <T extends Enum<T> & ISubEnum> void fillItemCategory(CreativeModeTab p40569, NonNullList<ItemStack> p40570, T element) {
        p40570.add(new ItemStack(this.stateDefinition.any().setValue(STAGE, 0).getBlock()));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        if (!level.isClientSide && !state.is(oldState.getBlock())) {
            level.scheduleTick(pos, this, STAGE_TIME);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int stage = state.getValue(STAGE);

        if (stage < 3) {
            BlockState nextState = state.setValue(STAGE, stage + 1);
            level.setBlock(pos, nextState, Block.UPDATE_ALL);
            level.scheduleTick(pos, this, STAGE_TIME);
        } else {
            level.setBlock(pos, IUItem.raw_saltpeter.getDefaultState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return new ItemStack(Items.MUD);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader p_304395_, BlockPos p_49824_, BlockState p_49825_) {
        return new ItemStack(Items.MUD);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public Block getBlock() {
        return this;
    }

    @Override
    public Pair<String, Integer> getHarvestLevel() {
        return new Pair<>("shovel", 1);
    }

    public enum Type implements ISubEnum {
        nitrate_mud(0),
        ;

        private final int metadata;
        private final String name;

        Type(int metadata) {
            this.metadata = metadata;
            this.name = this.name().toLowerCase(Locale.US);
        }

        public static Type getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public int getMetadata() {
            return this.metadata;
        }

        @Override
        public int getId() {
            return this.metadata;
        }


        @Override
        public String getOtherPart() {
            return "type=";
        }

        @Nonnull
        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "nitrate_mud";
        }

        public int getLight() {
            return 0;
        }


    }
}
