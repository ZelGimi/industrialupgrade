package com.denfop.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class InputFluidStack implements IInputItemStack {

    private static volatile FluidHandlerInfo fluidHandlerInfo;

    static {
        fluidHandlerInfo = new FluidHandlerInfo(Collections.emptyList(), LoaderState.PREINITIALIZATION);
    }

    private final Fluid fluid;
    private final int amount;

    InputFluidStack(Fluid fluid) {
        this(fluid, 1000);
    }

    InputFluidStack(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }

    public static List<ItemStack> getFluidContainer(Fluid fluid) {
        FluidHandlerInfo info = fluidHandlerInfo;
        ArrayList<ItemStack> ret;
        Iterator<Item> var3;
        if (info.loaderState != LoaderState.AVAILABLE && info.loaderState != Loader.instance().getLoaderState()) {
            ret = new ArrayList<>();
            var3 = ForgeRegistries.ITEMS.iterator();

            while (var3.hasNext()) {
                Item item = var3.next();
                ItemStack stack = new ItemStack(item);
                IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack);
                if (handler != null) {
                    handler.drain(Integer.MAX_VALUE, true);
                    ItemStack container = handler.getContainer();
                    if (FluidUtil.getFluidContained(container) == null) {
                        ret.add(stack);
                    }
                }
            }

            LoaderState state = Loader.instance().hasReachedState(LoaderState.AVAILABLE)
                    ? LoaderState.AVAILABLE
                    : Loader.instance().getLoaderState();
            fluidHandlerInfo = info = new FluidHandlerInfo(Collections.unmodifiableList(ret), state);
        }

        if (fluid == null) {
            return info.items;
        } else {
            ret = new ArrayList<>();

            for (final ItemStack stack : info.items) {
                IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack.copy());
                if (handler != null && handler.fill(new FluidStack(fluid, Integer.MAX_VALUE), true) > 0) {
                    ItemStack container = handler.getContainer();
                    if (FluidUtil.getFluidContained(container) != null) {
                        ret.add(container);
                    }
                }
            }

            return ret;
        }
    }

    public boolean matches(ItemStack subject) {
        FluidStack fs = FluidUtil.getFluidContained(subject);
        return fs == null && this.fluid == null || fs != null && fs.getFluid() == this.fluid && fs.amount >= this.amount;
    }

    public int getAmount() {
        return 1;
    }

    public List<ItemStack> getInputs() {
        return getFluidContainer(this.fluid);
    }

    public boolean equals(Object obj) {
        InputFluidStack other;
        return obj != null && this.getClass() == obj.getClass() && (other = (InputFluidStack) obj).fluid == this.fluid && other.amount == this.amount;
    }

    private static class FluidHandlerInfo {

        final List<ItemStack> items;
        final LoaderState loaderState;

        FluidHandlerInfo(List<ItemStack> items, LoaderState loaderState) {
            this.items = items;
            this.loaderState = loaderState;
        }

    }

}
