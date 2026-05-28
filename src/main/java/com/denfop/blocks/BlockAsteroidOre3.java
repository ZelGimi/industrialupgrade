package com.denfop.blocks;

import com.denfop.IUItem;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.dataregistry.DataBlock;
import com.denfop.world.WorldBaseGen;
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

public class BlockAsteroidOre3<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {

    public BlockAsteroidOre3(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
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
            case RAW_IRON:
                return Collections.singletonList(new ItemStack(Items.RAW_IRON, count));
            case RAW_GOLD:
                return Collections.singletonList(new ItemStack(Items.RAW_GOLD, count));
            case DIAMOND:
                return Collections.singletonList(new ItemStack(Items.DIAMOND, WorldBaseGen.random.nextInt(fortune + 1) + 1));
            case EMERALD:
                return Collections.singletonList(new ItemStack(Items.EMERALD, WorldBaseGen.random.nextInt(fortune + 1) + 1));
            case LAPIS:
                return Collections.singletonList(new ItemStack(Items.LAPIS_LAZULI, WorldBaseGen.random.nextInt(fortune + 2) + 1));
            case REDSTONE:
                return Collections.singletonList(new ItemStack(Items.REDSTONE, 4 + WorldBaseGen.random.nextInt(2) + WorldBaseGen.random.nextInt(fortune + 1)));
            case QUARTZ:
                return Collections.singletonList(new ItemStack(Items.QUARTZ, WorldBaseGen.random.nextInt(fortune + 2) + 1));
            case APATITE:
                return Collections.singletonList(new ItemStack(IUItem.apatite.getItem(WorldBaseGen.random.nextDouble() < 0.5 ? 1 : 0), 1));
            case SULFUR:
                ItemStack sulfur = IUItem.smallSulfurDust.copy();
                sulfur.setCount(4 + WorldBaseGen.random.nextInt(fortune + 1));
                return Collections.singletonList(sulfur);
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
        RAW_IRON,
        RAW_GOLD,
        DIAMOND,
        EMERALD,
        LAPIS,
        REDSTONE,
        QUARTZ,
        APATITE,
        SULFUR,
        SELF
    }

    public enum Type implements ISubEnum {
        asteroid_vanadium_ore(0, DropType.RAW, 2),
        asteroid_yttrium_ore(1, DropType.RAW, 31),
        asteroid_zinc_ore(2, DropType.RAW, 12),
        asteroid_zirconium_ore(3, DropType.RAW, 39);

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
            return "asteroidore3";
        }

        public int getLight() {
            return 0;
        }
    }
}
