package com.denfop.blocks;


import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockTexGlass extends BlockBase {

    public static final PropertyEnum<GlassType> stateProperty = PropertyEnum.create("state", GlassType.class);


    public BlockTexGlass() {
        super("glass", Material.GLASS);
        this.setHardness(2.0F);
        this.setResistance(180.0F);
        this.setSoundType(SoundType.GLASS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(stateProperty, GlassType.reinforced));

    }

    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(stateProperty, GlassType.values()[meta]);
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(stateProperty).getId();
    }


    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, stateProperty);
    }

    public int quantityDropped(Random random) {
        return 1;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullBlock(IBlockState state) {
        return true;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean isTopSolid(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return world.getBlockState(pos.offset(side)).getBlock() != this && super.shouldSideBeRendered(
                state,
                world,
                pos,
                side
        );
    }

    public enum GlassType implements ISubEnum, IStringSerializable {
        reinforced;



        public String getName() {
            return this.name();
        }

        public int getId() {
            return this.ordinal();
        }
    }

}
