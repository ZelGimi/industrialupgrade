package com.denfop.api.transport;

import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.sytem.InfoTile;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransportFluidItemSinkSource implements ITransportSource, ITransportSink {

    private final BlockPos pos;
    private final ItemFluidHandler handler;
    private final int slots;
    private final List<Integer> list_limits;
    private final TileEntity parent;
    private boolean isSink;
    private boolean isSource;
    private boolean isSinkFluid;
    private boolean isSourceFluid;
    private long id;

    public TransportFluidItemSinkSource(TileEntity parent,
            BlockPos pos, IItemHandler handler, IFluidHandler handler1,
            boolean isSink, boolean isSource, boolean isSinkFluid, boolean isSourceFluid
    ) {
        int slots1;
        this.parent = parent;
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

    }
    public long getIdNetwork() {
        return id;
    }


    public void setId(final long id) {
        this.id = id;
    }

    Map<EnumFacing, ITransportTile> energyConductorMap = new HashMap<>();

    public void RemoveTile(ITransportTile tile, final EnumFacing facing1) {
        if (!this.parent.getWorld().isRemote) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<ITransportTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<ITransportTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }
    private int hashCode;
    boolean hasHashCode = false;

    @Override
    public int hashCode() {
        if (!hasHashCode) {
            hasHashCode = true;
            this.hashCode = super.hashCode();
            return hashCode;
        } else {
            return hashCode;
        }
    }
    int hashCodeSource;
    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public Map<EnumFacing, ITransportTile> getTiles() {
        return energyConductorMap;
    }

    List<InfoTile<ITransportTile>> validReceivers = new LinkedList<>();


    public List<InfoTile<ITransportTile>> getValidReceivers() {
        return validReceivers;
    }

    @Override
    public TileEntity getTileEntity() {
        return parent;
    }

    public void AddTile(ITransportTile tile, final EnumFacing facing1) {
        if (!this.parent.getWorld().isRemote) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }

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
    public TransportItem<?> getOffered(final int type) {
        TransportItem<?> transportItem;

        if (type == 0) {
            TransportItem<ItemStack> itemTransportItem = new TransportItem<>();
            List<ItemStack> itemStackList = new LinkedList<>();
            List<Integer> integerList = new LinkedList<>();

            for (int i = 0; i < this.slots; i++) {
                ItemStack stack = this.handler.extractItem(i, this.list_limits.get(i), true);
                if (!stack.isEmpty()) {
                    itemStackList.add(stack);
                    integerList.add(i);
                }
            }

            itemTransportItem.setList(itemStackList);
            itemTransportItem.setList1(integerList);
            transportItem = itemTransportItem;

        } else {
            TransportItem<FluidStack> fluidTransportItem = new TransportItem<>();
            List<FluidStack> fluidStackList = new LinkedList<>();
            IFluidTankProperties[] fluidTanks = this.handler.getTankProperties();

            for (IFluidTankProperties fluidTankProperties : fluidTanks) {
                FluidStack contents = fluidTankProperties.getContents();
                if (fluidTankProperties.canDrain() && contents != null) {
                    fluidStackList.add(contents);
                }
            }

            fluidTransportItem.setList(fluidStackList);
            transportItem = fluidTransportItem;
        }

        return transportItem;
    }


    @Override
    public void draw(final Object var, final int col) {
        if (this.isSource && var instanceof ItemStack) {
            this.handler.extractItem(col, ((ItemStack) var).getCount(), false);
        }
        if (this.isSourceFluid && var instanceof FluidStack) {
            FluidStack fluidStack = (FluidStack) var;
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
    public List<Integer> getDemanded(IItemHandler handler) {
        if (!this.isSink) {
            return Collections.emptyList();
        }

        List<Integer> demandedSlots = new LinkedList<>();

        for (int i = 0; i < this.list_limits.size(); i++) {
            final ItemStack stack = handler.getStackInSlot(i);
            int limit = this.list_limits.get(i);
            int maxStackSize = stack.getMaxStackSize();

            if (stack.isEmpty() || stack.getCount() < Math.min(limit, maxStackSize)) {
                demandedSlots.add(i);
            }
        }

        return demandedSlots;
    }

    @Override
    public boolean isSink() {
        return this.isSink || this.isSinkFluid;
    }
    List<Integer> energyTickList = new LinkedList<>();
    @Override
    public List<Integer> getEnergyTickList() {
        return energyTickList;
    }

    @Override
    public boolean isItemSink() {
        return isSink;
    }

    @Override
    public boolean isFluidSink() {
        return isSinkFluid;
    }


}
