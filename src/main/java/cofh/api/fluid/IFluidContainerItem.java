package cofh.api.fluid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidContainerItem {

    FluidStack getFluid(ItemStack var1);

    int getCapacity(ItemStack var1);

    int fill(ItemStack var1, FluidStack var2, boolean var3);

    FluidStack drain(ItemStack var1, int var2, boolean var3);

}
