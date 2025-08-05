package com.denfop.blocks;

import com.denfop.DataBlock;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockRaws<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {


    public BlockRaws(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of().mapColor(MapColor.STONE).destroyTime(5.0F).explosionResistance(5F).sound(SoundType.STONE).requiresCorrectToolForDrops(), elements, element, dataBlock);
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
        mikhail(0),
        aluminium(1),
        vanady(2),
        tungsten(3),
        cobalt(4),
        magnesium(5),
        nickel(6),
        platinum(7),
        titanium(8),
        chromium(9),
        spinel(10),
        silver(11),
        zinc(12),
        manganese(13),
        iridium(14),
        germanium(15),
        osmium(16),
        tantalum(17),
        cadmium(18),
        arsenic(19),
        barium(20),
        bismuth(21),
        gadolinium(22),
        gallium(23),
        hafnium(24),
        yttrium(25),
        molybdenum(26),
        neodymium(27),
        niobium(28),
        palladium(29),
        polonium(30),
        strontium(31),
        thallium(32),
        zirconium(33),
        tin(34),
        lead(35),
        ;;

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
            return "raw_"+this.name;
        }

        @Override
        public String getMainPath() {
            return "raw_block".toLowerCase();
        }

        public int getLight() {
            return 0;
        }


    }
}
