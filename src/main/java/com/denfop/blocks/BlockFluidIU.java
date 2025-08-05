package com.denfop.blocks;

import com.denfop.IUPotion;
import com.denfop.api.item.IHazmatLike;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public class BlockFluidIU extends LiquidBlock {

    public BlockFluidIU(java.util.function.Supplier<? extends FlowingFluid> p_54694, Properties p_54695_) {
        super(p_54694.get(), p_54695_);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);

        if (!(entity instanceof LivingEntity)) return;

        Fluid fluid = this.fluid.getSource();


        if (fluid == FluidName.fluidcoolant.getInstance().get() || fluid == FluidName.fluidazot.getInstance().get()) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(IUPotion.frost, 200, 0));
        }


        if (!fluid.getFluidType().canDrownIn((LivingEntity) entity) && entity instanceof Player player) {
            if (!IHazmatLike.hasCompleteHazmat(player)) {
                player.addEffect(new MobEffectInstance(IUPotion.poison, 200, 0));
            }
        }
        if (fluid == FluidName.fluidpahoehoe_lava.getInstance().get()) {
            if (!entity.fireImmune()) {
                entity.hurt(level.damageSources().lava(), 2.0F);
                entity.igniteForSeconds(15);
            }
        }
    }

}
