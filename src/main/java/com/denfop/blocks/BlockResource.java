package com.denfop.blocks;

import com.denfop.DataBlock;
import com.denfop.IUItem;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class BlockResource<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {


    public BlockResource(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of().mapColor(MapColor.STONE).noOcclusion().destroyTime(3f).explosionResistance(5F).sound(SoundType.STONE).requiresCorrectToolForDrops(), elements, element, dataBlock);
        BlockTagsProvider.list.add(this);

    }

    @Override
    public List<ItemStack> getDrops(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, int fortune) {
        if (this.getElement().getId() != 10)
            return super.getDrops(world, pos, state, fortune);
        else {
            RandomSource rand = world.random;

            int count = rand.nextInt(3) + 1;
            return List.of(new ItemStack(IUItem.peat_balls.getItem(), count));
        }
    }

    @Override
    public   int getMetaFromState(BlockState state) {
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
        if (this.getElement().canAddToTab())
            p40570.add(new ItemStack(this.stateDefinition.any().getBlock()));
    }

    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return this.getElement() == Type.tempered_glass ? 1.0F : super.getShadeBrightness(p_48731_, p_48732_, p_48733_);
    }

    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        return this.getElement() == Type.tempered_glass ? true : super.propagatesSkylightDown(p_48740_, p_48741_, p_48742_);
    }

    public boolean skipRendering(BlockState p_53972_, BlockState p_53973_, Direction p_53974_) {
        return this.getElement() == Type.tempered_glass ? (p_53973_.is(this) || super.skipRendering(p_53972_, p_53973_, p_53974_)) : super.skipRendering(p_53972_, p_53973_, p_53974_);
    }

    @Override
    public Block getBlock() {
        return this;
    }

    @Override
    public Pair<String, Integer> getHarvestLevel() {
        if (this.getElement() == Type.peat || this.getElement() == Type.untreated_peat)
            return new Pair<>("shovel", 1);
        return new Pair<>("pickaxe", 1);
    }

    public enum Type implements ISubEnum {
        cryogen(0),
        bronze_block(1),
        copper_block(2),
        lead_block(3),
        steel_block(4),
        tin_block(5),
        uranium_block(6),
        reinforced_stone(7),
        machine(8),
        advanced_machine(9),
        peat(10),
        untreated_peat(11),
        steam_machine(12),
        tempered_glass(13),
        bio_machine(14),
        asphalt(15),

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
            return "blockresource";
        }

        public int getLight() {
            return 0;
        }

        @Override
        public boolean canAddToTab() {
            return this != copper_block;
        }

    }
}
