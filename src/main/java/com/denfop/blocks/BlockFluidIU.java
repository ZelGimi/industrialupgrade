package com.denfop.blocks;

import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.api.item.IHazmatLike;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class BlockFluidIU extends LiquidBlock implements IFluidBlock {

    public BlockFluidIU(java.util.function.Supplier<? extends FlowingFluid> p_54694, Properties p_54695_) {
        super(p_54694, p_54695_);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return this.getFluid().getFluidType().getLightLevel();
    }

    @Override
    public int getLightBlock(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
       return this.getFluid().getFluidType().getLightLevel();
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_49812_, NonNullList<ItemStack> p_49813_) {
        if (p_49812_ == IUCore.IUTab)
            p_49813_.add(new ItemStack(this));
    }
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);

        if (!(entity instanceof LivingEntity)) return;

        Fluid fluid = this.getFluid();


        if (fluid == FluidName.fluidcoolant.getInstance().get() || fluid == FluidName.fluidazot.getInstance().get()) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(IUPotion.frostbite, 200, 0));
        }


        if (!fluid.getFluidType().canDrownIn((LivingEntity) entity) && entity instanceof Player player) {
            if (!IHazmatLike.hasCompleteHazmat(player)) {
                player.addEffect(new MobEffectInstance(IUPotion.poison_gas, 200, 0));
            }
        }
        if (fluid == FluidName.fluidpahoehoe_lava.getInstance().get()) {
            if (!entity.fireImmune()) {
                entity.hurt(DamageSource.IN_FIRE, 2.0F);
                entity.setSecondsOnFire(15);
            }
        }
    }


    @Override
    public int place(Level level, BlockPos pos, @NotNull FluidStack fluidStack, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public @NotNull FluidStack drain(Level level, BlockPos pos, IFluidHandler.FluidAction action) {
        return FluidStack.EMPTY;
    }

    @Override
    public boolean canDrain(Level level, BlockPos pos) {
        BlockState p_153774_ = level.getBlockState(pos);
        return p_153774_.getValue(LEVEL) == 0;
    }

    @Override
    public float getFilledPercentage(Level level, BlockPos pos) {
        return 0;
    }
}
