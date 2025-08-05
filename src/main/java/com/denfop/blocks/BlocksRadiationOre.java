package com.denfop.blocks;

import com.denfop.DataBlock;
import com.denfop.IUPotion;
import com.denfop.api.item.IHazmatLike;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.network.packet.PacketUpdateRadiationValue;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class BlocksRadiationOre<T extends Enum<T> & ISubEnum> extends BlockCore<T> implements IBlockTag {

    public static final BooleanProperty BOOL_PROPERTY = BooleanProperty.create("hasdamage");

    public BlocksRadiationOre(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of().mapColor(MapColor.STONE).destroyTime(1f).randomTicks().explosionResistance(5F).sound(SoundType.STONE).requiresCorrectToolForDrops(), elements, element, dataBlock);
        BlockTagsProvider.list.add(this);
        this.registerDefaultState(this.stateDefinition.any().setValue(BOOL_PROPERTY, false));

    }

    @Override
    int getMetaFromState(BlockState state) {
        return getElement().getId();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(BOOL_PROPERTY);
    }

    @Override
    public <T extends Enum<T> & ISubEnum> BlockState getStateForPlacement(T element, BlockPlaceContext context) {
        return this.stateDefinition.any().setValue(BOOL_PROPERTY, false);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        // Ensure this code runs only on the server side
        if (world.isClientSide) {
            return;
        }

        // Check the boolean property
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
            boolean canAffect = !IHazmatLike.hasCompleteHazmat(player);
            if (canAffect) {
                player.addEffect(new MobEffectInstance(IUPotion.radiation, 400, 0));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 0));
            }
        }
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
        americium_ore(0),
        neptunium_ore(1),
        curium_ore(2),

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
            return "blockradiationore";
        }

        public int getLight() {
            return 0;
        }


    }
}
