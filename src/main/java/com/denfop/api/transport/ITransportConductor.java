package com.denfop.api.transport;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface ITransportConductor<T, E> extends ITransportAcceptor<T, E>, ITransportEmitter<T, E> {


    boolean isOutput();

    void update_render();

    boolean isItem();

    List<ItemStack> getBlackListItems();

    List<ItemStack> getWhiteListItems();

    List<FluidStack> getBlackListFluids();

    List<FluidStack> getWhiteListFluids();
}


