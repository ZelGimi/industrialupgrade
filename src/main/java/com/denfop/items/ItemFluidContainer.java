package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class ItemFluidContainer extends Item {
    public final int capacity;
    protected String nameItem;

    public ItemFluidContainer(int capacity) {
        super(new Properties().tab(IUCore.ItemTab).stacksTo(64).setNoRepair());
        this.capacity = capacity;
    }

    public ItemFluidContainer(Properties properties, int capacity) {
        super(properties);
        this.capacity = capacity;
    }

    public ItemFluidContainer(int capacity, int amount) {
        super(new Properties().tab(IUCore.ItemTab).stacksTo(amount).setNoRepair());
        this.capacity = capacity;
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem + ".name";
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, list, tooltipFlag);
        IFluidHandlerItem fs = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse((IFluidHandlerItem) this.initCapabilities(stack, stack.getTag()));
        if (fs.getFluidInTank(0).getFluid() != Fluids.EMPTY) {
            list.add(Component.literal("< " + Localization.translate(fs.getFluidInTank(0).getFluid().getFluidType().getDescriptionId()) + ", " + fs.getFluidInTank(0).getAmount() + " mB >"));
        } else {
            list.add(Component.literal(Localization.translate("iu.item.FluidContainer.Empty")));
        }
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        if (!this.hasCraftingRemainingItem(stack)) {
            return super.getCraftingRemainingItem(stack);
        } else {
            ItemStack ret = ModUtils.setSize(stack, 1);
            IFluidHandlerItem handler = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse((IFluidHandlerItem) this.initCapabilities(stack, stack.getTag()));
            handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
            return handler.getContainer();
        }

    }

    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new CapabilityFluidHandlerItem(stack, ItemFluidContainer.this.capacity) {
            public boolean canFillFluidType(FluidStack fluid) {
                return fluid != null && ItemFluidContainer.this.canfill(fluid.getFluid());
            }

            public boolean canDrainFluidType(FluidStack fluid) {
                return fluid != null && ItemFluidContainer.this.canfill(fluid.getFluid()) && ItemFluidContainer.this.canDrain(fluid);
            }
        };
    }

    public boolean canDrain(FluidStack fluid) {
        return true;
    }

    public List<ItemStack> getAllStacks() {
        List<ItemStack> ret = new LinkedList<>();
        ret.add(new ItemStack(this));
        for (final Fluid fluid : ForgeRegistries.FLUIDS.getValues()) {
            ItemStack add = this.getItemStack(fluid);
            if (add != ItemStack.EMPTY && !add.isEmpty() && !FluidHandlerFix.getFluidHandler(add).getFluidInTank(0).isEmpty()) {
                ret.add(add);
            }
        }

        return ret;
    }

    public boolean canfill(Fluid var1) {
        return true;
    }

    ;

    public ItemStack getItemStack(ItemStack ret, Fluid fluid) {
        if (fluid == null) {
            return ret;
        } else {
            IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(ret);
            if (handler == null) {
                return null;
            } else {
                return handler.fill(new FluidStack(fluid, Integer.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE) > 0 ? handler.getContainer() : null;
            }
        }
    }

    public ItemStack getItemStack(Fluid fluid) {
        ItemStack ret = new ItemStack(this);
        if (fluid == null || fluid == Fluids.EMPTY || !fluid.isSource(fluid.defaultFluidState())) {
            return ret;
        } else {
            IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(ret);
            if (handler.fill(new FluidStack(fluid, Integer.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE) > 0) {
                return handler.getContainer();
            } else {
                return ret;
            }
        }
    }

}
