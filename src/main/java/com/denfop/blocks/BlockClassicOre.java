package com.denfop.blocks;

import com.denfop.IUItem;
import com.denfop.api.item.armor.HazmatLike;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.dataregistry.DataBlock;
import com.denfop.network.packet.PacketUpdateRadiationValue;
import com.denfop.potion.IUPotion;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BlockClassicOre<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {


    public static final BooleanProperty BOOL_PROPERTY = BooleanProperty.create("hasdamage");

    public BlockClassicOre(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of(Material.STONE).destroyTime(1f).randomTicks().explosionResistance(5F).sound(SoundType.STONE).requiresCorrectToolForDrops(), elements, element, dataBlock);
        BlockTagsProvider.list.add(this);
        if (element.getId() == 3)
            this.registerDefaultState(this.stateDefinition.any().setValue(BOOL_PROPERTY, false));

    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {

        if (world.isClientSide || this.getElement() != Type.uranium) {
            return;
        }

        boolean damage = state.getValue(BOOL_PROPERTY);
        if (!damage) {
            return;
        }


        ChunkPos chunkPos = new ChunkPos(pos);
        PacketUpdateRadiationValue packet = new PacketUpdateRadiationValue(chunkPos, 1);
        AABB axisAlignedBB = new AABB(
                pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2,
                pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3
        );


        List<Player> players = world.getEntitiesOfClass(Player.class, axisAlignedBB);
        for (Player player : players) {
            boolean canAffect = !HazmatLike.hasCompleteHazmat(player);
            if (canAffect) {
                player.addEffect(new MobEffectInstance(IUPotion.radiation, 400, 0));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 0));
            }
        }
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
        return this.stateDefinition.any().setValue(BOOL_PROPERTY, false);
    }

    @Override
    public <T extends Enum<T> & ISubEnum> void fillItemCategory(CreativeModeTab p40569, NonNullList<ItemStack> p40570, T element) {
        p40570.add(new ItemStack(this.stateDefinition.any().getBlock()));
    }

    @Override
    public List<ItemStack> getDrops(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, int fortune) {
        final int meta = getElement().getId();
        if (meta == 0) {
            return Collections.singletonList((new ItemStack(IUItem.rawMetals.getStack(16), 1 + getDrop(fortune))));
        } else if (meta == 1) {
            return Collections.singletonList(new ItemStack(IUItem.rawMetals.getStack(20), 1 + getDrop(fortune)));
        } else if (meta == 2) {
            return Collections.singletonList(new ItemStack(IUItem.rawMetals.getStack(19), 1 + getDrop(fortune)));
        } else if (meta == 3) {
            return super.getDrops(world, pos, state, fortune);
        }
        return super.getDrops(world, pos, state, fortune);
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
    public Block getBlock() {
        return this;
    }

    @Override
    public Pair<String, Integer> getHarvestLevel() {
        return new Pair<>("pickaxe", 1);
    }

    public enum Type implements ISubEnum {
        copper(0),
        tin(1),
        lead(2),
        uranium(3);

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
            return "classicore";
        }

        public int getLight() {
            return 0;
        }

        @Override
        public boolean register() {
            return this != copper;
        }
    }
}
