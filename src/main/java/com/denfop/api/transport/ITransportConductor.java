package com.denfop.api.transport;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface ITransportConductor<T, E> extends ITransportAcceptor<T, E>, ITransportEmitter<T, E> {


    boolean isOutput();

    boolean isInput();

    InfoCable getCable();

    void setCable(InfoCable cable);

    boolean isItem();

    List<ItemStack> getBlackListItems(EnumFacing facing);

    List<ItemStack> getWhiteListItems(EnumFacing facing);

    List<FluidStack> getBlackListFluids(EnumFacing facing);

    List<FluidStack> getWhiteListFluids(EnumFacing facing);

    boolean canWork();

    int getMax();

    void setMax(int value);

    int getMax(byte tick);

}


