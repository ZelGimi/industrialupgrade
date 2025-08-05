package com.denfop.api.transport;


import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class Path {


    final ITransportSink target;

    final Direction targetDirection;
    private final Object handler;
    Direction firstSide;

    ITransportConductor first = null;

    ITransportConductor end = null;

    Path(ITransportSink sink, Direction facing) {
        this.target = sink;
        this.targetDirection = facing;
        this.handler = sink.getHandler(facing);
        this.firstSide = null;
    }

    public IItemHandler getHandler() {
        return (handler instanceof IItemHandler) ? (IItemHandler) handler : null;
    }


    public IFluidHandler getFluidHandler() {
        return (handler instanceof IFluidHandler) ? (IFluidHandler) handler : null;
    }

}
