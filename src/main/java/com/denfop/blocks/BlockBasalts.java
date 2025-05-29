package com.denfop.blocks;

import com.denfop.DataBlock;
import com.denfop.IUItem;
import com.denfop.IUPotion;
import com.denfop.api.item.IVolcanoArmor;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BlockBasalts<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {


    public static final BooleanProperty BOOL_PROPERTY = BooleanProperty.create("hasdamage");

    public BlockBasalts(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of().mapColor(MapColor.STONE).randomTicks().lightLevel((blockState) -> element.getId() == 4 ? 8 : 0).destroyTime(((BlockBasalts.Type) element).getHardness()).explosionResistance(((BlockBasalts.Type) element).getExplosionResistance()).sound(((BlockBasalts.Type) element).metal ? SoundType.METAL : SoundType.STONE).requiresCorrectToolForDrops(), elements, element, dataBlock);
        BlockTagsProvider.list.add(this);

    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        Random rand = WorldBaseGen.random;
        List<ItemStack> drops = new ArrayList<>();

        int meta = this.getElement().getId();
        ItemStack tool = builder.getParameter(LootContextParams.TOOL);
        int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
        if (meta == 6) {
            drops.add(new ItemStack(IUItem.iudust.getItemFromMeta(31), rand.nextInt(2) + 1 + rand.nextInt(fortune + 1)));
        } else if (meta == 7) {
            drops.add(new ItemStack(IUItem.crafting_elements.getItemFromMeta(477), rand.nextInt(2) + 1 + rand.nextInt(fortune + 1)));
        } else if (meta == 5) {
            drops.add(new ItemStack(IUItem.basalts.getItem(2), 1));
        } else if (meta == 1) {
            drops.add(new ItemStack(IUItem.basalts.getItem(8), 1));
        } else {
            drops.add(new ItemStack(this.getBlock()));
        }

        return drops;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isClientSide) return;

        boolean damage = state.getValue(BOOL_PROPERTY);
        if (!damage) return;

        AABB axisAlignedBB = new AABB(pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
        List<Player> players = level.getEntitiesOfClass(Player.class, axisAlignedBB);

        for (Player player : players) {
            if (!IVolcanoArmor.hasCompleteHazmat(player)) {
                player.addEffect(new MobEffectInstance(IUPotion.poison_gas, 200, 0));
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        state = state.setValue(BOOL_PROPERTY, false);
        level.setBlockAndUpdate(pos, state);
    }

    @Override
    int getMetaFromState(BlockState state) {
        return getElement().getId();
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_);
        p_49915_.add(BOOL_PROPERTY);
    }

    @Override
    public <T extends Enum<T> & ISubEnum> BlockState getStateForPlacement(T element, BlockPlaceContext context) {
        return this.stateDefinition.any().setValue(BOOL_PROPERTY, true);
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
        basalt(5, 45.0F, false),
        basalt_cobblestone(5, 45.0F, false),
        basalt_melted(10, 45.0F, false),
        basalt_blocked(5, 45.0F, false),
        basalt_magma(2.5F, 45.0F, false),
        basalt_pylon(5, 45.0F, false),
        basalt_sulfur_ore(5, 45.0F, false),
        basalt_boron_ore(10, 45.0F, false),
        basalt_spongy(5, 45.0F, false),
        basalt_smooth(5, 45.0F, false),
        ;
        private final float hardness;
        private final float explosionResistance;
        private final boolean metal;
        private final int metadata;
        private final String name;

        Type(float hardness, float explosionResistance, boolean metal) {
            this.metadata = this.ordinal();
            this.name = this.name().toLowerCase(Locale.US);
            this.hardness = hardness;
            this.explosionResistance = explosionResistance;
            this.metal = metal;
        }

        public static Type getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public float getExplosionResistance() {
            return explosionResistance;
        }

        public float getHardness() {
            return hardness;
        }

        public boolean isMetal() {
            return metal;
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
            return "blockbasalts";
        }

        public int getLight() {
            return 0;
        }


    }
}
