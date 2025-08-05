package com.denfop.blocks;

import com.denfop.DataBlock;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BlockSpace1<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {


    public BlockSpace1(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of().mapColor(MapColor.STONE).destroyTime(1f).explosionResistance(5F).sound(SoundType.STONE).requiresCorrectToolForDrops(), elements, element, dataBlock);
        BlockTagsProvider.list.add(this);

    }

    @Override
    int getMetaFromState(BlockState state) {
        return getElement().getId();
    }

    @Override
    public List<ItemStack> getDrops(  @Nonnull final Level world,
                                      @Nonnull final BlockPos pos,
                                      @Nonnull final BlockState state,
                                      final int fortune) {
        RandomSource rand = world.random;
        //
        NonNullList<ItemStack> ret = NonNullList.create();
        BlockSpace1.Type type = (Type) this.getElement();
        if (type.ordinal() == 0 || type.ordinal() == 1 || type.ordinal() == 4 || type.ordinal() == 7 || type.ordinal() == 12 || type.ordinal() == 13 || type.ordinal() == 14) {
            ItemStack stack = type.getStack();
            if (stack == null) {
                stack = Recipes.inputFactory.getInput(type.getRaw()).getInputs().get(0);
                type.setStack(stack);
            }
            stack = stack.copy();
            stack.setCount(1 + getDrop(fortune));
            ret.add(stack);
        } else if (type.ordinal() == 6) {
            final int i = WorldBaseGen.random.nextInt(fortune + 1) + 1;
            ret.add(new ItemStack(Items.DIAMOND, i));
        } else if (type.ordinal() == 10) {
            final int i = WorldBaseGen.random.nextInt(fortune + 1) + 1;
            ret.add(new ItemStack(Items.EMERALD, i));
        } else if (type.ordinal() == 11) {
            ret.add(ModUtils.setSize(IUItem.smallSulfurDust, 4 + WorldBaseGen.random.nextInt(fortune + 1)));
        } else if (type.ordinal() == 9) {
            final int i = WorldBaseGen.random.nextInt(fortune + 2) + 1;
            ret.add(new ItemStack(Items.QUARTZ, i));
        } else if (type.ordinal() == 5) {
            if (WorldBaseGen.random.nextDouble() < 0.5)
                ret.add(new ItemStack(IUItem.apatite.getItem(1), 1));
            else
                ret.add(new ItemStack(IUItem.apatite.getItem(0), 1));

        } else {
            ret.add(new ItemStack(this, 1));
        }
        return ret;
    }

    public int quantityDropped(int fortune, RandomSource random) {

        return quantityDroppedWithBonus(fortune, random);
    }

    public int quantityDroppedWithBonus(int fortune, @Nonnull RandomSource random) {
        return (fortune == 0) ? 1
                : (1 + random.nextInt(fortune));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {

    }

    public int quantityRedstoneDroppedWithBonus(int fortune, Random random) {
        return this.quantityRedstoneDropped(random) + random.nextInt(fortune + 1);
    }

    public int quantityRedstoneDropped(Random random) {
        return 4 + random.nextInt(2);
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
        europa_mikhail_ore(0),
        ganymede_aluminium_ore(1),
        ganymede_beryllium(2),
        ganymede_calcium(3),
        ganymede_tungsten_ore(4),
        haumea_apatite(5),
        haumea_diamond(6),
        haumea_iridium(7),
        haumea_lithium(8),
        haumea_quartz(9),
        io_emerald_ore(10),
        io_sulfur(11),
        makemake_bismuth(12),
        makemake_gold(13),
        makemake_silver(14),
        mars_adamantium_ore(15);

        private final int metadata;
        private final String name;
        ItemStack stack;
        private String raw;

        Type(int metadata) {
            this.metadata = metadata;
            this.name = this.name().toLowerCase(Locale.US);
            setRaw(this.getName().split("_")[1]);
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

        public String getRaw() {
            return raw;
        }

        public void setRaw(String name) {
            this.raw = "forge:raw_materials/" + name;
        }

        @Nonnull
        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "blockspace2";
        }

        public int getLight() {
            return 0;
        }

        public ItemStack getStack() {
            return stack;
        }

        public void setStack(ItemStack stack) {
            this.stack = stack;
        }

    }
}
