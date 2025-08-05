package com.denfop.blocks;

import com.denfop.DataBlock;
import com.denfop.IUItem;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BlockOres2<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {


    public BlockOres2(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of(Material.STONE).destroyTime(1f).explosionResistance(5F).sound(SoundType.STONE).requiresCorrectToolForDrops(), elements, element, dataBlock);
        BlockTagsProvider.list.add(this);

    }

    @Override
    int getMetaFromState(BlockState state) {
        return getElement().getId();
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {

    }

    @Override
    public <T extends Enum<T> & ISubEnum> BlockState getStateForPlacement(T element, BlockPlaceContext context) {
        return this.stateDefinition.any();
    }

    @Override
    public <T extends Enum<T> & ISubEnum> void fillItemCategory(CreativeModeTab p40569, NonNullList<ItemStack> p40570, T element) {
        p40570.add(new ItemStack(this.stateDefinition.any().getBlock()));
    }

    @Override
    public List<ItemStack> getDrops(
            @Nonnull final Level world,
            @Nonnull final BlockPos pos,
            @Nonnull final BlockState state,
            final int fortune
    ) {
        Random rand = WorldBaseGen.random;

        final int meta = this.getMetaFromState(state);
        if (meta >= 3 && meta <= 5) {

            return Collections.singletonList((new ItemStack(IUItem.rawMetals.getStack(getMetaFromState(state) - 3 + 22), 1 + getDrop(fortune))));

        }
        return super.getDrops(world, pos, state, fortune);
    }

    private int getDrop(int fortune) {
        switch (fortune) {
            case 0:
                return 0;
            case 1:
                return WorldBaseGen.random.nextInt(100) < 50 ? 1 : 0;
            case 2:
                return WorldBaseGen.random.nextInt(100) < 100 ? 1 : 1;
            default:
                return WorldBaseGen.random.nextInt(100) < 50 ? 2 : 1;
        }
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        final int meta = this.getMetaFromState(state);
        if (meta == 0)
            return SoundType.SAND;
        else if (meta > 2)
            return SoundType.STONE;
        return super.getSoundType(state, level, pos, entity);
    }


    @Override
    public Block getBlock() {
        return this;
    }

    @Override
    public Pair<String, Integer> getHarvestLevel() {
        if (this.getElement().getId() >= 3 && this.getElement().getId() < 6)
            return new Pair<>("pickaxe", 1);
        return new Pair<>("shovel", 1);
    }

    public enum Type implements ISubEnum {
        lithium(0),
        beryllium(1),
        bor(2),

        osmium(3),
        tantalum(4),
        cadmium(5),
        saltpeter(6),
        calcium(7);;

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
            return "baseore1";
        }

        public int getLight() {
            return 0;
        }


    }
}
