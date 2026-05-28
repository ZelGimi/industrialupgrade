package com.denfop.blocks;

import com.denfop.IUItem;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.dataregistry.DataBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BlockDeepOre2<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {

    public BlockDeepOre2(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of().mapColor(MapColor.STONE).destroyTime(1f).explosionResistance(5F).sound(SoundType.STONE).requiresCorrectToolForDrops(), elements, element, dataBlock);
        BlockTagsProvider.list.add(this);
    }

    @Override
    int getMetaFromState(BlockState state) {
        return getElement().getId();
    }

    @Override
    public List<ItemStack> getDrops(@Nonnull final Level world, @Nonnull final BlockPos pos, @Nonnull final BlockState state, final int fortune) {
        Type type = (Type) this.getElement();
        int count = 1 + getDrop(fortune);

        switch (type.getDropType()) {
            case RAW:
                return Collections.singletonList(new ItemStack(IUItem.rawMetals.getStack(type.getDropMeta()), count));
            case DUST:
                return Collections.singletonList(new ItemStack(IUItem.iudust.getStack(type.getDropMeta()), count));
            case PRECIOUS:
                return Collections.singletonList(new ItemStack(IUItem.preciousgem.getItemFromMeta(type.getDropMeta()), count));
            case QUARTZ:
                return Collections.singletonList(new ItemStack(Items.QUARTZ, count));
            default:
                return super.getDrops(world, pos, state, fortune);
        }
    }

    private int getDrop(int fortune) {
        switch (fortune) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 3;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_);
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
        return new Pair<>("pickaxe", 2);
    }

    private enum DropType {
        RAW,
        DUST,
        PRECIOUS,
        QUARTZ,
        SELF
    }

    public enum Type implements ISubEnum {
        deep_strontium_ore(0, DropType.RAW, 37),
        deep_thallium_ore(1, DropType.RAW, 38),
        deep_sulfur_ore(2, DropType.DUST, 31),
        deep_tin_ore(3, DropType.RAW, 20),
        deep_lead_ore(4, DropType.RAW, 19),
        deep_uranium_ore(5, DropType.SELF, 0),
        deep_americium_ore(6, DropType.SELF, 0),
        deep_neptunium_ore(7, DropType.SELF, 0),
        deep_curium_ore(8, DropType.SELF, 0),
        deep_thorium_ore(9, DropType.SELF, 0),
        deep_ruby_ore(10, DropType.PRECIOUS, 0),
        deep_sapphire_ore(11, DropType.PRECIOUS, 1),
        deep_topaz_ore(12, DropType.PRECIOUS, 2),
        deep_quartz_ore(13, DropType.QUARTZ, 0),
        deep_fluorapatite_ore(14, DropType.SELF, 0),
        deep_nepheline_ore(15, DropType.SELF, 0);

        private final int metadata;
        private final String name;
        private final DropType dropType;
        private final int dropMeta;

        Type(int metadata, DropType dropType, int dropMeta) {
            this.metadata = metadata;
            this.name = this.name().toLowerCase(Locale.US);
            this.dropType = dropType;
            this.dropMeta = dropMeta;
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

        public DropType getDropType() {
            return dropType;
        }

        public int getDropMeta() {
            return dropMeta;
        }

        @Nonnull
        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "deepore2";
        }

        public int getLight() {
            return 0;
        }
    }
}
