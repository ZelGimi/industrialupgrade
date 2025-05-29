package com.denfop.blocks;

import com.denfop.DataBlock;
import com.denfop.api.Recipes;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.NonNullList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BlockSpace<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {


    public BlockSpace(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of().mapColor(MapColor.STONE).destroyTime(1f).explosionResistance(5F).sound(SoundType.STONE).requiresCorrectToolForDrops(), elements, element, dataBlock);
        BlockTagsProvider.list.add(this);

    }

    @Override
    int getMetaFromState(BlockState state) {
        return getElement().getId();
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder p_60538_) {
        RandomSource rand = p_60538_.getLevel().random;

        List<ItemStack> list = new ArrayList<>();
        ItemStack stack1 = p_60538_.getOptionalParameter(LootContextParams.TOOL);
        int fortune = 0;
        if (stack1 != null)
            fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack1);
        NonNullList<ItemStack> ret = NonNullList.create();
        Type type = (Type) this.getElement();
        if (type.ordinal() != 3 && type.ordinal() != 4 && type.ordinal() != 5 && type.ordinal() != 0 && type.ordinal() != 10) {
            ItemStack stack = type.getStack();
            if (stack == null) {
                stack = Recipes.inputFactory.getInput(type.getRaw()).getInputs().get(0);
                type.setStack(stack);
            }
            stack = stack.copy();
            stack.setCount(1 + getDrop(fortune));
            ret.add(stack);
        } else if (type.ordinal() == 5) {
            final int i = quantityRedstoneDroppedWithBonus(fortune, WorldBaseGen.random);
            ret.add(new ItemStack(Items.REDSTONE, i));
        } else if (type.ordinal() == 3) {
            final int i = WorldBaseGen.random.nextInt(fortune + 2) + 1;
            ret.add(new ItemStack(Items.LAPIS_LAZULI, i));
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
                return WorldBaseGen.random.nextDouble() < 0.25 ? 1 : 0;
            case 2:
                return WorldBaseGen.random.nextDouble() < 0.5 ? 1 : 0;
            default:
                return WorldBaseGen.random.nextDouble() < 0.75 ? 1 : 0;
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
        ariel_draconium(0),
        asteroid_barium_ore(1),
        asteroid_cadmium_ore(2),
        callisto_lapis_ore(3),
        callisto_lithium_ore(4),
        callisto_redstone_ore(5),
        ceres_copper_ore(6),
        ceres_iron_ore(7),
        charon_arsenic(8),
        charon_zinc(9),
        deimos_orichalcum_ore(10),
        dione_lead_ore(11),
        dione_platinum_ore(12),
        enceladus_magnesium_ore(13),
        eris_magnesium(14),
        eris_manganese(15),
        ;;

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
            return "blockspace";
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
