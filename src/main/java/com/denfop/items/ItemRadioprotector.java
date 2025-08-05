package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.blocks.FluidName;
import com.denfop.network.packet.PacketRadiationUpdateValue;
import com.denfop.utils.FluidHandlerFix;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class ItemRadioprotector extends ItemFluidContainer implements IProperties, IItemTab {

    public ItemRadioprotector() {
        super(1000, 1);
        IUCore.proxy.addProperties(this);

    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }


    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            p_41392_.add(new ItemStack(this));
            p_41392_.add(this.getItemStack(FluidName.fluidazurebrilliant.getInstance().get()));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            CompoundTag nbt = player.getPersistentData();
            IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(stack);
            FluidStack fluid = handler.getFluidInTank(0);

            if (!fluid.isEmpty() && fluid.getAmount() == 1000 && nbt.getDouble("radiation") > 0D) {

                handler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                nbt.putDouble("radiation", 0);


                new PacketRadiationUpdateValue(player, 0);

                player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 1200)); // HASTE
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600)); // REGENERATION
                player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 600)); // SATURATION
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300)); // RESISTANCE
                return InteractionResultHolder.success(player.getItemInHand(hand));
            }

        }

        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }


    public boolean canfill(Fluid fluid) {
        return fluid == FluidName.fluidazurebrilliant.getInstance().get();
    }


    @Override
    public String[] properties() {
        return new String[]{"full"};
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack itemStack, ClientLevel level, LivingEntity entity, int p174679, String property) {
        return itemStack.getCapability(Capabilities.FluidHandler.ITEM).getFluidInTank(0).isEmpty() ? 0 : 1;
    }
}
