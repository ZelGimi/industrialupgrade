package com.denfop.blocks;

import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.dataregistry.DataBlock;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockSpaceStone<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {


    public BlockSpaceStone(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of(Material.STONE).destroyTime(1f).sound(SoundType.STONE).requiresCorrectToolForDrops(), elements, element, dataBlock);
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
    public Block getBlock() {
        return this;
    }

    @Override
    public Pair<String, Integer> getHarvestLevel() {
        return new Pair<>("pickaxe", 1);
    }

    public enum Type implements ISubEnum {
        ariel_stone(0),
        asteroids_stone(1),
        callisto_stone(2),
        ceres_stone(3),
        charon_stone(4),
        deimos_stone(5),
        dione_stone(6),
        enceladus_stone(7),
        eris_stone(8),
        europe_stone(9),
        ganymede_stone(10),
        haumea_stone(11),
        io_stone(12),
        makemake_stone(13),
        mars_stone(14),
        mercury_stone(15),

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
            return "spacestone";
        }

        public int getLight() {
            return 0;
        }


    }
}
