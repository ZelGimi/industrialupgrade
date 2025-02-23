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
    private IItemHandler handler;
    EnumFacing  firstSide;
    IFluidHandler fluidHandler = null;

    ITransportConductor first = null;

    ITransportConductor end = null;

    Path(ITransportSink sink, EnumFacing facing) {
        this.target = sink;
        this.targetDirection = facing;
        if (this.target.getHandler() instanceof IItemHandler) {
            this.handler = this.target.getTileEntity().getCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                    this.targetDirection
            );
            this.handler = new ItemFluidHandler(handler, null);
        }
        if (this.target.getHandler() instanceof IFluidHandler) {
            this.fluidHandler = this.target.getTileEntity().getCapability(
                    CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                    this.targetDirection
            );

            this.fluidHandler = new ItemFluidHandler(null, fluidHandler);
        }
        this.firstSide = null;
    }

    public IItemHandler getHandler() {
        return handler;
    }


    public IFluidHandler getFluidHandler() {
        return this.fluidHandler;
    }

}
