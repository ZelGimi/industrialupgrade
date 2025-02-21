package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.FluidName;
import com.denfop.items.block.ISubItem;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ItemFluidContainer extends Item implements ISubItem<FluidName>, IModelRegister {

    protected final int capacity;

    public ItemFluidContainer(String name, int capacity) {
        super();
        this.capacity = capacity;
        this.setHasSubtypes(true);
        this.setCreativeTab(IUCore.ItemTab);
        setUnlocalizedName(name);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final NBTTagCompound nbt) {
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

    public ItemStack getItemStack(FluidName type) {
        return this.getItemStack(type.getInstance());
    }

    public ItemStack getItemStack(Fluid fluid) {
        ItemStack ret = new ItemStack(this);
        if (fluid == null) {
            return ret;
        } else {
            IFluidHandlerItem handler = FluidUtil.getFluidHandler(ret);
            if (handler == null) {
                return null;
            } else {
                return handler.fill(new FluidStack(fluid, Integer.MAX_VALUE), true) > 0 ? handler.getContainer() : null;
            }
        }
    }
    public ItemStack getItemStack(ItemStack ret,Fluid fluid) {
        if (fluid == null) {
            return ret;
        } else {
            IFluidHandlerItem handler = FluidUtil.getFluidHandler(ret);
            if (handler == null) {
                return null;
            } else {
                return handler.fill(new FluidStack(fluid, Integer.MAX_VALUE), true) > 0 ? handler.getContainer() : null;
            }
        }
    }
    public ItemStack getItemStack(String variant) {
        if (variant != null && !variant.isEmpty()) {
            Fluid fluid = FluidRegistry.getFluid(variant);
            return fluid == null ? null : this.getItemStack(fluid);
        } else {
            return new ItemStack(this);
        }
    }

    public String getVariant(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null stack");
        } else if (stack.getItem() != this) {
            throw new IllegalArgumentException("The stack " + stack + " doesn't match " + this);
        } else {
            FluidStack fs = FluidUtil.getFluidContained(stack);
            return fs != null && fs.getFluid() != null ? fs.getFluid().getName() : null;
        }
    }

    public Set<FluidName> getAllTypes() {
        return EnumSet.allOf(FluidName.class);
    }

    public Set<ItemStack> getAllStacks() {
        Set<ItemStack> ret = new HashSet<>();
        ret.add(new ItemStack(this));

        for (final Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
            ItemStack add = this.getItemStack(fluid);
            if (add != null) {
                ret.add(add);
            }
        }

        return ret;
    }


    public ItemStack getContainerItem(ItemStack stack) {
        if (!this.hasContainerItem(stack)) {
            return super.getContainerItem(stack);
        } else {
            ItemStack ret = ModUtils.setSize(stack, 1);
            IFluidHandlerItem handler = FluidUtil.getFluidHandler(ret);
            handler.drain(Integer.MAX_VALUE, true);
            return handler.getContainer();
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, world, tooltip, advanced);
        FluidStack fs = FluidUtil.getFluidContained(stack);
        if (fs != null) {
            tooltip.add("< " + fs.getLocalizedName() + ", " + fs.amount + " mB >");
        } else {
            tooltip.add(Localization.translate("iu.item.FluidContainer.Empty"));
        }

    }


    public abstract boolean canfill(Fluid var1);

}
