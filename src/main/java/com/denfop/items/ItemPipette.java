package com.denfop.items;


import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Localization;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemPipette extends ItemFluidContainer {

    public ItemPipette() {
        super(10000, 1);

    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, list, tooltipFlag);
        list.add(Component.literal(Localization.translate("iu.pipette.info")));
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            p_41392_.add(new ItemStack(this));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide && player.isShiftKeyDown()) {
            ItemStack stack = player.getItemInHand(hand);
            IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(stack);
            FluidStack fluid = handler.getFluidInTank(0);

            if (!fluid.isEmpty()) {

                handler.drain(fluid.getAmount(), IFluidHandler.FluidAction.EXECUTE);
                return InteractionResultHolder.success(player.getItemInHand(hand));
            }

        }

        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }


    public boolean canfill(Fluid fluid) {
        return true;
    }


}
