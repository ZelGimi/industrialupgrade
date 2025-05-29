package com.denfop.api.transport;


import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface ITransportConductor<T, E> extends ITransportAcceptor<T, E>, ITransportEmitter<T, E> {


    boolean isOutput();

    boolean isInput();

    InfoCable getCable();

    void setCable(InfoCable cable);

    boolean isItem();

    List<ItemStack> getBlackListItems(Direction facing);

    List<ItemStack> getWhiteListItems(Direction facing);

    List<FluidStack> getBlackListFluids(Direction facing);

    List<FluidStack> getWhiteListFluids(Direction facing);

    boolean canWork();

    int getMax();

    void setMax(int value);

    int getMax(byte tick);

}


