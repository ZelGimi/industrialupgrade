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

public class BlockDeepOre<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {

    public BlockDeepOre(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
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
        deep_mikhail_ore(0, DropType.RAW, 0),
        deep_aluminium_ore(1, DropType.RAW, 1),
        deep_vanadium_ore(2, DropType.RAW, 2),
        deep_tungsten_ore(3, DropType.RAW, 3),
        deep_cobalt_ore(4, DropType.RAW, 4),
        deep_magnesium_ore(5, DropType.RAW, 5),
        deep_nickel_ore(6, DropType.RAW, 6),
        deep_platinum_ore(7, DropType.RAW, 7),
        deep_titanium_ore(8, DropType.RAW, 8),
        deep_chromium_ore(9, DropType.RAW, 9),
        deep_spinel_ore(10, DropType.RAW, 10),
        deep_silver_ore(11, DropType.RAW, 11),
        deep_zinc_ore(12, DropType.RAW, 12),
        deep_manganese_ore(13, DropType.RAW, 13),
        deep_iridium_ore(14, DropType.RAW, 14),
        deep_germanium_ore(15, DropType.RAW, 15);

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
            return "deepore";
        }

        public int getLight() {
            return 0;
        }
    }
}
