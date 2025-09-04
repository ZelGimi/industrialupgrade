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

public class BlocksAlloy1<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {


    public BlocksAlloy1(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of(Material.STONE).destroyTime(3f).explosionResistance(5F).sound(SoundType.STONE).requiresCorrectToolForDrops(), elements, element, dataBlock);
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
        stellite(0),
        hafnium_boride_alloy(1),
        woods(2),
        nimonic(3),
        tantalum_tungsten_hafnium(4),
        permalloy(5),
        aluminium_lithium_alloy(6),
        cobalt_chrome(7),
        hafnium_carbide(8),
        molybdenum_steel(9),
        niobium_titanium(10),
        osmiridium(11),
        superalloy_haynes(12),
        superalloy_rene(13),
        yttrium_aluminium_garnet(14),
        gallium_arcenic(15),
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
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "baseblockalloy1";
        }

        public int getLight() {
            return 0;
        }


    }
}
