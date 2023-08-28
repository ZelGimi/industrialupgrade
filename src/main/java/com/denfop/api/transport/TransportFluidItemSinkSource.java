package com.denfop.api.transport;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class TransportFluidItemSinkSource implements ITransportSource, ITransportSink {

    private final BlockPos pos;
    private final ItemFluidHandler handler;
    private final int slots;
    private final List<Integer> list_limits;
    private int sinks;
    private int sources;
    private int sinksfluid;
    private int sourcesfluid;
    private boolean isSink;
    private boolean isSource;
    private boolean isSinkFluid;
    private boolean isSourceFluid;
    private boolean need_update = false;
    private List<EnumFacing> facingListSink = new ArrayList<>();
    private List<EnumFacing> facingListSource = new ArrayList<>();

    public TransportFluidItemSinkSource(
            BlockPos pos, IItemHandler handler, IFluidHandler handler1,
            boolean isSink, boolean isSource, boolean isSinkFluid, boolean isSourceFluid
    ) {
        int slots1;
        this.pos = pos;
        this.handler = new ItemFluidHandler(handler, handler1);
        try {
            slots1 = handler.getSlots();
        } catch (Exception exception) {
            slots1 = 0;
        }
        this.slots = slots1;
        this.isSink = isSink;
        this.isSource = isSource;
        this.isSinkFluid = isSinkFluid;
        this.isSourceFluid = isSourceFluid;
        this.list_limits = new ArrayList<>();
        for (int i = 0; i < this.slots; i++) {
            this.list_limits.add(handler.getSlotLimit(i));
        }
        this.sinks = isSink ? 1 : 0;
        this.sources = isSource ? 1 : 0;
        this.sinksfluid = isSinkFluid ? 1 : 0;
        this.sourcesfluid = isSourceFluid ? 1 : 0;

    }

    public void setFacingListSink(final List<EnumFacing> facingListSink) {
        this.facingListSink = facingListSink;
    }

    public void setFacingListSource(final List<EnumFacing> facingListSource) {
        this.facingListSource = facingListSource;
    }

    public boolean need_delete() {
        return !this.isSink && !this.isSinkFluid && !this.isSourceFluid && !this.isSource;
    }

    public boolean isNeed_update() {
        return need_update;
    }

    public void setNeed_update(final boolean need_update) {
        this.need_update = need_update;
    }

    public boolean isSinkFluid() {
        return isSinkFluid;
    }

    public void setSinkFluid(final boolean sinkFluid) {
        if (!sinkFluid) {
            if (sinksfluid == 1) {
                isSinkFluid = false;
                need_update = true;
            }
            sinksfluid--;
        } else {
            isSinkFluid = true;
            if (sinksfluid == 0) {
                need_update = true;
            }
            sinksfluid++;
        }
    }

    public boolean isSourceFluid() {
        return isSourceFluid;
    }

    public void setSourceFluid(final boolean sourceFluid) {
        if (!sourceFluid) {
            if (sourcesfluid == 1) {
                isSourceFluid = false;
                need_update = true;
            }
            sourcesfluid--;
        } else {
            isSourceFluid = true;
            if (sourcesfluid == 0) {
                need_update = true;
            }
            sourcesfluid++;
        }
    }

    public IItemHandler getItemHandler() {
        return this.handler.getItemHandler();
    }

    public IFluidHandler getFluidHandler() {
        return this.handler.getFluidHandler();
    }

    @Override
    public boolean emitsTo(final ITransportAcceptor var1, final EnumFacing var2) {
        Object handler = var1.getHandler();
        if (this.isSource && handler instanceof IItemHandler) {
            return true;
        }
        return this.isSourceFluid && handler instanceof IFluidHandler;
    }

    @Override
    public TransportItem getOffered(final int type) {
        if (type == 0) {
            TransportItem<ItemStack> transportItem = new TransportItem<>();
            List<ItemStack> itemStackList = new ArrayList<>();
            List<Integer> integerList = new ArrayList<>();
            for (int i = 0; i < this.slots; i++) {
                ItemStack stack = this.handler.extractItem(i, this.list_limits.get(i), true);
                if (!stack.isEmpty()) {
                    itemStackList.add(stack);
                    integerList.add(i);
                }
            }
            transportItem.setList(itemStackList);
            transportItem.setList1(integerList);
            return transportItem;
        } else {
            TransportItem transportItem = new TransportItem();
            List<FluidStack> fluidStackList = new ArrayList<>();
            final IFluidTankProperties[] fluidTanks = this.handler.getTankProperties();
            for (IFluidTankProperties fluidTankProperties : fluidTanks) {
                if (fluidTankProperties.canDrain() && fluidTankProperties.getContents() != null) {
                    fluidStackList.add(fluidTankProperties.getContents());
                }
            }
            transportItem.setList(fluidStackList);
            return transportItem;
        }
    }

    @Override
    public void draw(final Object var, final int col) {
        if (this.isSource && var instanceof ItemStack) {
            this.handler.extractItem(col, ((ItemStack) var).getCount(), false);
        }
        if (this.isSourceFluid && var instanceof FluidStack) {
            FluidStack fluidStack = (FluidStack) var;
            fluidStack = fluidStack.copy();
            fluidStack.amount = col;
            this.handler.drain(fluidStack, true);
        }
    }

    @Override
    public boolean isItem() {
        return this.isSource;
    }

    @Override
    public boolean isFluid() {
        return this.isSourceFluid;
    }

    @Override
    public boolean isSource() {
        return this.isSource || isSourceFluid;
    }

    public void setSource(final boolean source) {
        if (!source) {
            if (sources == 1) {
                isSource = false;
                need_update = true;
            }
            sources--;
        } else {
            isSource = true;
            if (sources == 0) {
                need_update = true;
            }
            sources++;
        }
    }

    @Override
    public Object getHandler() {
        if (this.handler.getFluidHandler() != null && this.handler.getItemHandler() != null) {
            return this.handler;
        }
        if (this.handler.getFluidHandler() == null) {
            return this.handler.getItemHandler();
        }
        if (this.handler.getItemHandler() == null) {
            return this.handler.getFluidHandler();
        }
        return this.handler;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

    @Override
    public boolean acceptsFrom(final ITransportEmitter var1, final EnumFacing var2) {
        Object handler = var1.getHandler();
        if (this.isSink && handler instanceof IItemHandler) {
            return true;
        }
        return this.isSinkFluid && handler instanceof IFluidHandler;
    }

    @Override
    public List<Integer> getDemanded() {
        if (this.isSink) {
            int i = 0;
            List<Integer> list = new ArrayList<>();
            for (Integer integer : this.list_limits) {
                final ItemStack stack = this.handler.getStackInSlot(i);
                if (stack.isEmpty() || (stack.getCount() < integer && integer <= stack.getMaxStackSize()) || (stack.getCount() < stack.getMaxStackSize() && integer < stack.getMaxStackSize())) {
                    list.add(i);
                }
                i++;
            }
            return list;
        }
        return null;
    }

    @Override
    public boolean isSink() {
        return this.isSink || this.isSinkFluid;
    }

    public void setSink(final boolean sink) {
        if (!sink) {
            if (sinks == 1) {
                isSink = false;
                need_update = true;
            }
            sinks--;
        } else {
            isSink = true;
            if (sinks == 0) {
                need_update = true;
            }
            sinks++;
        }
    }

    @Override
    public List getItemStackFromFacing(final EnumFacing facing) {
        return null;
    }

    @Override
    public boolean canAccept(final EnumFacing facing) {

        return this.facingListSink.contains(facing);
    }

    @Override
    public void removeFacing(final EnumFacing facing) {
        this.facingListSink.remove(facing);
    }

    @Override
    public boolean canAdd(final EnumFacing facing) {
        if (!this.facingListSink.contains(facing)) {
            this.facingListSink.add(facing);
            return true;
        }
        return false;
    }

    @Override
    public List<EnumFacing> getFacingList() {
        return this.facingListSink;
    }

}
