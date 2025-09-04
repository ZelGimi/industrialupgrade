package com.denfop.items;


import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.denfop.IUCore.fluidCellTab;

public class ItemSmallFluidCell extends ItemFluidContainer {
    public ItemSmallFluidCell() {
        super(new Properties().stacksTo(64).setNoRepair().tab(fluidCellTab), 500);
    }

    public boolean canfill(Fluid fluid) {
        return true;
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
