package com.denfop.blocks;


import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import ic2.core.block.BlockBase;
import ic2.core.init.BlocksItems;
import ic2.core.item.block.ItemBlockIC2;
import ic2.core.profile.Version;
import ic2.core.ref.BlockName;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockIUFluid extends BlockFluidClassic implements IModelRegister {

    protected final Fluid fluid;


    public BlockIUFluid(FluidName name, Fluid fluid, Material material, int color) {
        super(fluid, material);
        this.setUnlocalizedName(name.name());
        this.setCreativeTab(IUCore.SSPTab);
        this.fluid = fluid;


        ResourceLocation regName = IUCore.getIdentifier(name.name());
        BlocksItems.registerBlock(this, regName);
        BlocksItems.registerItem(new ItemBlockIC2(this), regName);

        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(BlockName name) {
        BlockBase.registerDefaultItemModel(this);
    }

    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        boolean defaultState = Version.shouldEnable(FluidName.class);

        try {
            if (Version.shouldEnable(FluidName.class.getField(super.getUnlocalizedName().substring(5)), defaultState)) {
                items.add(new ItemStack(this));
            }

        } catch (NoSuchFieldException var5) {
            throw new RuntimeException("Impossible missing enum field!", var5);
        }
    }

    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        super.updateTick(world, pos, state, random);


    }

    private static boolean isLavaBlock(Block block) {
        return block == Blocks.LAVA || block == Blocks.FLOWING_LAVA;
    }

    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos) {
        super.neighborChanged(state, world, pos, block, neighborPos);

    }

    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
    }

    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        this.onEntityWalk(worldIn, pos, entityIn);
    }

    public void onEntityWalk(World world, BlockPos pos, Entity entity) {

    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(5);
    }


}
