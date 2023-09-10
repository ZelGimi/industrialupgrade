package com.denfop.blocks;


import com.denfop.IUItem;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockFoam extends BlockBase {

    public static final PropertyEnum<FoamType> VARIANT = PropertyEnum.create("state", FoamType.class);


    public BlockFoam() {
        super("foam", Material.CLOTH);
        this.setTickRandomly(true);
        this.setHardness(0.01F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.CLOTH);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, FoamType.reinforced));

    }

    public static float getHardenChance(World world, BlockPos pos, IBlockState state, FoamType type) {
        int light = world.getLightFromNeighbors(pos);
        if (!state.useNeighborBrightness() && state.getBlock().getLightOpacity(state, world, pos) == 0) {
            EnumFacing[] var5 = EnumFacing.VALUES;
            int var6 = var5.length;

            for (EnumFacing side : var5) {
                light = Math.max(light, world.getLight(pos.offset(side), false));
            }
        }

        int avgTime = type.hardenTime * (16 - light);
        return 1.0F / (float) (avgTime * 20);
    }

    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, FoamType.values()[meta]);
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getId();
    }

    public IBlockState getState(FoamType type) {
        if (type == null) {
            throw new IllegalArgumentException("invalid type: " + type);
        } else {
            return this.getDefaultState().withProperty(VARIANT, type);
        }
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
        return null;
    }

    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        int tickSpeed = world.getGameRules().getInt("randomTickSpeed");
        if (tickSpeed <= 0) {
            throw new IllegalStateException("Foam was randomly ticked when world " + world + " isn't ticking?");
        } else {
            FoamType type = state.getValue(VARIANT);
            float chance = getHardenChance(world, pos, state, type) * 4096.0F / (float) tickSpeed;
            if (random.nextFloat() < chance) {
                world.setBlockState(pos, state.getValue(VARIANT).getResult());
            }

        }
    }

    public boolean onBlockActivated(
            World world,
            BlockPos pos,
            IBlockState state,
            EntityPlayer player,
            EnumHand hand,
            EnumFacing side,
            float hitX,
            float hitY,
            float hitZ
    ) {
        final ItemStack handItem = player.getHeldItem(hand);
        if (!handItem.isEmpty() && handItem.isItemEqual(new ItemStack(Blocks.SAND))) {
            world.setBlockState(pos, state.getValue(VARIANT).getResult());
            handItem.shrink(1);
            return true;
        } else {
            return false;
        }

    }

    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return super.getDrops(world, pos, state, fortune);
    }

    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }

    public enum FoamType implements IStringSerializable, ISubEnum {
        reinforced(600);

        public final int hardenTime;

        FoamType(int hardenTime) {
            this.hardenTime = hardenTime;
        }

        public String getName() {
            return this.name();
        }

        public int getId() {
            return this.ordinal();
        }


        public IBlockState getResult() {
            return IUItem.blockResource.getStateFromMeta(BlockResource.Type.reinforced_stone.getMetadata());
        }
    }

}
