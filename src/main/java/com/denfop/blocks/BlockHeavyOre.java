package com.denfop.blocks;

import com.denfop.DataBlock;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockHeavyOre<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag, IMineral {


    public BlockHeavyOre(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of().mapColor(MapColor.STONE).destroyTime(1f).explosionResistance(5F).sound(SoundType.STONE).requiresCorrectToolForDrops(), elements, element, dataBlock);
        BlockTagsProvider.list.add(this);

    }

    @Override
    public    int getMetaFromState(BlockState state) {
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

    /**
     * @param meta
     * @return
     */
    @Override
    public BlockState getStateMeta(int meta) {
        return this.stateDefinition.any();
    }

    @Override
    public Block getBlock() {
        return this;
    }

    @Override
    public Pair<String, Integer> getHarvestLevel() {
        return new Pair<>("pickaxe", 2);
    }

    public enum Type implements ISubEnum {
        magnetite(0),
        calaverite(1),
        galena(2),
        nickelite(3),
        pyrite(4),
        quartzite(5),
        uranite(6),
        azurite(7),
        rhodonite(8),
        alfildit(9),
        euxenite(10),
        smithsonite(11),
        ilmenite(12),
        todorokite(13),
        ferroaugite(14),
        sheelite(15),

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
            return "heavyore";
        }

        public int getLight() {
            return 0;
        }


    }
}
