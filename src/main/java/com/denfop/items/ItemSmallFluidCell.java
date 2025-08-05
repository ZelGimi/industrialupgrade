package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.KeyboardIU;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSmallFluidCell extends ItemFluidContainer implements IItemTab {
    public ItemSmallFluidCell() {
        super(500);
    }

    public boolean canfill(Fluid fluid) {
        return true;
    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.fluidCellTab;
    }
    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            p_41392_.addAll(getAllStacks());

        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, list, tooltipFlag);



    }

    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, CompoundTag nbt) {
        return new CapabilityFluidHandlerItem(stack, 500) {
            public boolean canFillFluidType(FluidStack fluid) {
                return fluid != null && ItemSmallFluidCell.this.canfill(fluid.getFluid());
            }

            public boolean canDrainFluidType(FluidStack fluid) {
                return fluid != null && ItemSmallFluidCell.this.canfill(fluid.getFluid());
            }

            @Override
            public int fill(FluidStack resource, FluidAction doFill) {
                if (resource == null) {
                    return 0;
                }
                return super.fill(resource, doFill);
            }

            @Override
            public @NotNull FluidStack drain(int maxDrain, FluidAction doDrain) {

                return super.drain(maxDrain, doDrain);
            }

            @Override
            public @NotNull FluidStack drain(FluidStack resource, FluidAction doDrain) {
                if (resource == FluidStack.EMPTY) {
                    return FluidStack.EMPTY;
                }
                return super.drain(resource, doDrain);
            }

            @NotNull
            @Override
            public ItemStack getContainer() {
                return getFluid() == FluidStack.EMPTY ? new ItemStack(this.container.getItem()) : container;
            }
        };
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {

        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }



}
