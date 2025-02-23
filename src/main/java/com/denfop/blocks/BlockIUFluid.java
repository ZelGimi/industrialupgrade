package com.denfop.blocks;


import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IHazmatLike;
import com.denfop.damagesource.IUDamageSource;
import com.denfop.items.block.ItemBlockIU;
import com.denfop.register.Register;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockIUFluid extends BlockFluidClassic implements IModelRegister {

    protected final Fluid fluid;


    public BlockIUFluid(FluidName name, Fluid fluid, Material material) {
        super(fluid, ((IUFluid) fluid).getFluid().canBeLava() ? Material.LAVA : material);
        this.setUnlocalizedName(name.name());
        this.setCreativeTab(IUCore.FluidBlockTab);
        this.fluid = fluid;
        if (name == FluidName.fluidpahoehoe_lava) {
            setMaxScaledLight(10);
            this.setQuantaPerBlock(5);
        }
        ResourceLocation regName = IUCore.getIdentifier(name.name());
        Register.registerBlock(this, regName);
        Register.registerItem(new ItemBlockIU(this), regName);

        IUCore.proxy.addIModelRegister(this);
    }

    private static boolean isFluid(@Nonnull IBlockState blockstate) {
        return blockstate.getMaterial().isLiquid() || blockstate.getBlock() instanceof IFluidBlock;
    }

    @Override
    @Nonnull
    public IBlockState getExtendedState(@Nonnull IBlockState oldState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        IExtendedBlockState state = (IExtendedBlockState) oldState;
        state = state.withProperty(FLOW_DIRECTION, (float) getFlowDirection(world, pos));
        IBlockState[][] upBlockState = new IBlockState[3][3];
        float[][] height = new float[3][3];
        float[][] corner = new float[][]{{0, 0}, {0, 0}};
        upBlockState[1][1] = world.getBlockState(pos.down(densityDir));
        height[1][1] = getFluidHeightForRender(world, pos, upBlockState[1][1]);
        if (height[1][1] == 1) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    corner[i][j] = 1;
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (i != 1 || j != 1) {
                        upBlockState[i][j] = world.getBlockState(pos.add(i - 1, 0, j - 1).down(densityDir));
                        height[i][j] = getFluidHeightForRender(world, pos.add(i - 1, 0, j - 1), upBlockState[i][j]);
                    }
                }
            }
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    corner[i][j] = getFluidHeightAverage(height[i][j], height[i][j + 1], height[i + 1][j], height[i + 1][j + 1]);
                }
            }
            //check for downflow above corners
            boolean n = isFluid(upBlockState[0][1]);
            boolean s = isFluid(upBlockState[2][1]);
            boolean w = isFluid(upBlockState[1][0]);
            boolean e = isFluid(upBlockState[1][2]);
            boolean nw = isFluid(upBlockState[0][0]);
            boolean ne = isFluid(upBlockState[0][2]);
            boolean sw = isFluid(upBlockState[2][0]);
            boolean se = isFluid(upBlockState[2][2]);
            if (nw || n || w) {
                corner[0][0] = 1;
            }
            if (ne || n || e) {
                corner[0][1] = 1;
            }
            if (sw || s || w) {
                corner[1][0] = 1;
            }
            if (se || s || e) {
                corner[1][1] = 1;
            }
        }

        for (int i = 0; i < 4; i++) {
            EnumFacing side = EnumFacing.getHorizontal(i);
            BlockPos offset = pos.offset(side);
        }
        try {
            state = state.withProperty(LEVEL_CORNERS[0], corner[0][0]);
            state = state.withProperty(LEVEL_CORNERS[1], corner[0][1]);
            state = state.withProperty(LEVEL_CORNERS[2], corner[1][1]);
            state = state.withProperty(LEVEL_CORNERS[3], corner[1][0]);
        } catch (Exception e) {
            System.out.println(e);
        }

        return state;
    }

    private boolean isWithinFluid(IBlockAccess world, BlockPos pos, Vec3d vec) {
        float filled = getFilledPercentage(world, pos);
        return filled < 0 ? vec.y > pos.getY() + filled + 1
                : vec.y < pos.getY() + filled;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(
            World world,
            BlockPos pos,
            IBlockState state,
            Entity entity,
            Vec3d originalColor,
            float partialTicks
    ) {
        if (!isWithinFluid(world, pos, ActiveRenderInfo.projectViewFromEntity(entity, partialTicks))) {
            if (state.getMaterial() == Material.WATER) {
                float f12 = 0.0F;

                if (entity instanceof net.minecraft.entity.EntityLivingBase) {
                    net.minecraft.entity.EntityLivingBase ent = (net.minecraft.entity.EntityLivingBase) entity;
                    f12 = (float) net.minecraft.enchantment.EnchantmentHelper.getRespirationModifier(ent) * 0.2F;

                    if (ent.isPotionActive(net.minecraft.init.MobEffects.WATER_BREATHING)) {
                        f12 = f12 * 0.3F + 0.6F;
                    }
                }
                return new Vec3d(0.02F + f12, 0.02F + f12, 0.2F + f12);
            } else if (state.getMaterial() == Material.LAVA) {
                return new Vec3d(0.6F, 0.1F, 0.0F);
            }
            return originalColor;
        }

        if (getFluid() != null) {
            int color = getFluid().getColor();
            float red = (color >> 16 & 0xFF) / 255.0F;
            float green = (color >> 8 & 0xFF) / 255.0F;
            float blue = (color & 0xFF) / 255.0F;
            return new Vec3d(red, green, blue);
        }

        return super.getFogColor(world, pos, state, entity, originalColor, partialTicks);
    }

    @Override
    public boolean isReplaceable(final IBlockAccess worldIn, final BlockPos pos) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        BlockBase.registerDefaultItemModel(this);
    }


    public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (this.fluid != FluidName.fluidlava.getInstance() && this.fluid != FluidName.fluidwater.getInstance()) {
            items.add(new ItemStack(this));
        }

    }


    public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random random) {
        super.updateTick(worldIn, pos, state, random);


    }


    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return ((IUFluid) this.fluid).getFluid().canBeLava() ? 300 : 0;
    }

    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 25;
    }

    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    public boolean isFireSource(World world, BlockPos pos, EnumFacing face) {
        return ((IUFluid) this.fluid).getFluid().canBeLava();
    }


    @Override
    public boolean canDisplace(final IBlockAccess world, final BlockPos pos) {
        return !world.getBlockState(pos).getMaterial().isLiquid() && super.canDisplace(world, pos);
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
        if (entityIn instanceof EntityPlayer) {
            if (state.getBlock() instanceof BlockIUFluid) {
                if (((BlockIUFluid) state.getBlock()).getFluid() == FluidName.fluidcoolant.getInstance() || ((BlockIUFluid) state.getBlock()).getFluid() == FluidName.fluidazot.getInstance()) {
                    ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(IUPotion.frostbite, 200, 0));

                }
                if (((BlockIUFluid) state.getBlock()).getFluid().isGaseous()) {
                    if (!IHazmatLike.hasCompleteHazmat((EntityLivingBase) entityIn)) {
                        ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(IUPotion.poison_gas, 200, 0));
                    }

                }
                if (((BlockIUFluid) state.getBlock()).getFluid() == FluidName.fluidpahoehoe_lava.getInstance()) {
                    if (!entityIn.isImmuneToFire) {
                        entityIn.attackEntityFrom(DamageSource.LAVA, 2.0F);
                        entityIn.setFire(15);
                    }
                }
            }
        }

    }

    public void onEntityWalk(World world, @Nonnull BlockPos pos, @Nonnull Entity entity) {

    }


    @Nonnull
    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(5);
    }


}
