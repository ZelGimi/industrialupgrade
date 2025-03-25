package com.denfop.api.transport;

import com.denfop.api.transport.ITransportConductor;
import com.denfop.api.transport.ITransportSink;
import com.denfop.api.transport.ItemFluidHandler;
import com.denfop.api.transport.TransportNetLocal;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.LinkedList;
import java.util.List;

public class Path {


    final ITransportSink target;

    final EnumFacing targetDirection;
    private final Object handler;
    EnumFacing  firstSide;

    ITransportConductor first = null;

    ITransportConductor end = null;

    Path(ITransportSink sink, EnumFacing facing) {
        this.target = sink;
        this.targetDirection = facing;
        this.handler = sink.getHandler(facing);
        this.firstSide = null;
    }

    public IItemHandler getHandler() {
        return ( handler instanceof IItemHandler) ? (IItemHandler) handler : null;
    }


    public IFluidHandler getFluidHandler() {
        return ( handler instanceof IFluidHandler) ? (IFluidHandler) handler : null;
    }

}
