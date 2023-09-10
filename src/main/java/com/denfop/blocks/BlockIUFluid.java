package com.denfop.blocks;


import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.damagesource.IUDamageSource;
import com.denfop.items.block.ItemBlockIU;
import com.denfop.register.Register;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockIUFluid extends BlockFluidClassic implements IModelRegister {

    protected final Fluid fluid;


    public BlockIUFluid(FluidName name, Fluid fluid, Material material) {
        super(fluid, material);
        this.setUnlocalizedName(name.name());
        this.setCreativeTab(IUCore.IUTab);
        this.fluid = fluid;


        ResourceLocation regName = IUCore.getIdentifier(name.name());
        Register.registerBlock(this, regName);
        Register.registerItem(new ItemBlockIU(this), regName);

        IUCore.proxy.addIModelRegister(this);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        BlockBase.registerDefaultItemModel(this);
    }


    public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {

        items.add(new ItemStack(this));

    }

    public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random random) {
        super.updateTick(world, pos, state, random);


    }

    public void neighborChanged(
            @Nonnull IBlockState state,
            @Nonnull World world,
            @Nonnull BlockPos pos,
            @Nonnull Block block,
            @Nonnull BlockPos neighborPos
    ) {
        super.neighborChanged(state, world, pos, block, neighborPos);

    }

    public void onBlockAdded(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        super.onBlockAdded(world, pos, state);
    }

    public void onBlockPlacedBy(
            @Nonnull World world,
            @Nonnull BlockPos pos,
            @Nonnull IBlockState state,
            @Nonnull EntityLivingBase placer,
            @Nonnull ItemStack stack
    ) {
        if (!world.isRemote) {
            world.setBlockToAir(pos);
            world.playSound(
                    null,
                    pos,
                    SoundEvents.ITEM_FIRECHARGE_USE,
                    SoundCategory.BLOCKS,
                    1.0F,
                    RANDOM.nextFloat() * 0.4F + 0.8F
            );


        }
    }

    public void onEntityCollidedWithBlock(
            @Nonnull World worldIn,
            @Nonnull BlockPos pos,
            @Nonnull IBlockState state,
            @Nonnull Entity entityIn
    ) {
        if (state.getBlock() instanceof BlockIUFluid) {
            if (((BlockIUFluid) state.getBlock()).getFluid() == FluidName.fluidcoolant.getInstance() || ((BlockIUFluid) state.getBlock()).getFluid() == FluidName.fluidazot.getInstance()) {
                this.onEntityWalk(worldIn, pos, entityIn);
            }
        }

    }

    public void onEntityWalk(World world, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        entity.attackEntityFrom(IUDamageSource.frostbite, 0.5F);
    }


    @Nonnull
    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(5);
    }


}
