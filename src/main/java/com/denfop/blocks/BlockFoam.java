package com.denfop.blocks;

import com.denfop.DataBlock;
import com.denfop.IUItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public class BlockFoam<T extends Enum<T> & ISubEnum> extends BlockCore<T> {


    public BlockFoam(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of().mapColor(MapColor.WOOL).destroyTime(0.01f).noOcclusion().noCollission().explosionResistance(10F).sound(SoundType.WOOL).randomTicks(), elements, element, dataBlock);


    }

    public static float getHardenChance(Level world, BlockPos pos, BlockState state, FoamType type) {
        int light = world.getMaxLocalRawBrightness(pos);
        if (!state.supportsExternalFaceHiding() && state.getBlock().getLightEmission(state, world, pos) == 0) {
            Direction[] var5 = Direction.values();

            for (Direction side : var5) {
                light = Math.max(light, world.getMaxLocalRawBrightness(pos.offset(side.getNormal())));
            }
        }

        int avgTime = type.hardenTime * (16 - light);
        return 1.0F / (float) (avgTime * 20);
    }

    @Override
    public void randomTick(BlockState p_222954_, ServerLevel p_222955_, BlockPos p_222956_, RandomSource p_222957_) {
        int tickSpeed = p_222955_.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
        if (tickSpeed <= 0) {
            throw new IllegalStateException("Foam was randomly ticked when world " + p_222955_ + " isn't ticking?");
        } else {
            FoamType type = (FoamType) getElement();
            float chance = getHardenChance(p_222955_, p_222956_, p_222954_, type) * 4096.0F / (float) tickSpeed;
            if (p_222957_.nextFloat() < chance) {
                p_222955_.setBlock(p_222956_, FoamType.values()[getMetaFromState(p_222954_)].getResult(), 3);
            }

        }
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        final ItemStack handItem = p_60506_.getItemInHand(p_60507_);
        if (!handItem.isEmpty() && handItem.is(new ItemStack(Blocks.SAND).getItem())) {
            p_60504_.setBlock(p_60505_, FoamType.values()[getMetaFromState(p_60503_)].getResult(), 3);
            handItem.shrink(1);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
        return false;
    }


    @Override
    public <T extends Enum<T> & ISubEnum> BlockState getStateForPlacement(T element, BlockPlaceContext context) {
        return stateDefinition.any();
    }

    @Override
    public <T extends Enum<T> & ISubEnum> void fillItemCategory(CreativeModeTab p40569, NonNullList<ItemStack> p40570, T element) {
        p40570.add(new ItemStack(this.stateDefinition.any().getBlock()));
    }


    public enum FoamType implements ISubEnum {
        reinforced(600);

        public final int hardenTime;

        FoamType(int hardenTime) {
            this.hardenTime = hardenTime;
        }

        public String getName() {
            return this.name();
        }

        @Override
        public boolean registerOnlyBlock() {
            return true;
        }

        @Override
        public String getMainPath() {
            return "foam";
        }

        public int getId() {
            return this.ordinal();
        }


        public BlockState getResult() {
            return IUItem.blockResource.getBlock(6).defaultBlockState();
        }
    }
}
