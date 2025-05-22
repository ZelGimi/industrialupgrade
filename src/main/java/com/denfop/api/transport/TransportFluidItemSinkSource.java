package com.denfop.api.transport;

import com.denfop.api.sytem.InfoTile;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
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
    private final TileEntity parent;
    Map<EnumFacing, ItemFluidHandler> handlerMap = new HashMap<>();
    Map<EnumFacing, Integer> slotsMap = new HashMap<>();
    Map<EnumFacing, List<Integer>> limitsMap = new HashMap<>();
    Map<EnumFacing, ITransportTile> energyConductorMap = new HashMap<>();
    boolean hasHashCode = false;
    int hashCodeSource;
    List<InfoTile<ITransportTile>> validReceivers = new LinkedList<>();
    List<Integer> energyTickList = new LinkedList<>();
    private boolean isSink;
    private boolean isSource;
    private boolean isSinkFluid;
    private boolean isSourceFluid;
    private long id;
    private int hashCode;
    public TransportFluidItemSinkSource(
            TileEntity parent,
            BlockPos pos
    ) {
        int slots1;
        this.parent = parent;
        this.pos = pos;
        boolean isItem = false;
        boolean isFluid = false;
        for (EnumFacing facing : EnumFacing.VALUES) {
            IItemHandler item_storage = parent.getCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                    facing
            );
            IFluidHandler fluid_storage = parent.getCapability(
                    CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                    facing
            );
            if (!isFluid && fluid_storage != null) {
                isFluid = true;
            }
            if (!isItem && item_storage != null) {
                isItem = true;
            }
            try {
                slots1 = item_storage.getSlots();
            } catch (Exception exception) {
                slots1 = 0;
            }
            handlerMap.put(facing, new ItemFluidHandler(item_storage, fluid_storage));
            slotsMap.put(facing, slots1);
            List<Integer> list_limits = new ArrayList<>();
            for (int i = 0; i < slots1; i++) {
                list_limits.add(item_storage.getSlotLimit(i));
            }
            limitsMap.put(facing, list_limits);
        }
        this.isSink = isItem;
        this.isSource = isItem;
        this.isSinkFluid = isFluid;
        this.isSourceFluid = isFluid;


    }

    public long getIdNetwork() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void RemoveTile(ITransportTile tile, final EnumFacing facing1) {
        if (!this.parent.getWorld().isRemote) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<ITransportTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<ITransportTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity.getBlockPos().equals(tile.getBlockPos())) {
                    iter.remove();
                    break;
                }
            }
        }
    }

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

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    @Override
    public Map<EnumFacing, ITransportTile> getTiles() {
        return energyConductorMap;
    }

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

    @Override
    public boolean emitsTo(final ITransportAcceptor var1, final EnumFacing var2) {
        if (this.isSource && handlerMap
                .get(var2)
                .getItemHandler() instanceof IItemHandler && var1 instanceof ITransportConductor) {
            return true;
        }
        return this.isSourceFluid && handlerMap
                .get(var2)
                .getFluidHandler() instanceof IFluidHandler && var1 instanceof ITransportConductor;
    }

    @Override
    public TransportItem<?> getOffered(final int type, EnumFacing facing) {
        TransportItem<?> transportItem;

        if (type == 0) {
            TransportItem<ItemStack> itemTransportItem = new TransportItem<>();
            List<ItemStack> itemStackList = new LinkedList<>();
            List<Integer> integerList = new LinkedList<>();
            int slots = this.slotsMap.get(facing);
            final List<Integer> list_limits = this.limitsMap.get(facing);
            for (int i = 0; i < slots; i++) {
                ItemStack stack = this.handlerMap.get(facing).getItemHandler().extractItem(i, list_limits.get(i), true);
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
            IFluidTankProperties[] fluidTanks = this.handlerMap.get(facing).getFluidHandler().getTankProperties();

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
    public void draw(final Object var, final int col, EnumFacing facing) {
        if (this.isSource && var instanceof ItemStack) {
            this.handlerMap.get(facing).getItemHandler().extractItem(col, ((ItemStack) var).getCount(), false);
        }
        if (this.isSourceFluid && var instanceof FluidStack) {
            FluidStack fluidStack = (FluidStack) var;
            fluidStack.amount = col;
            this.handlerMap.get(facing).getFluidHandler().drain(fluidStack, true);
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
    public Object getHandler(EnumFacing facing) {
        final ItemFluidHandler handler = this.handlerMap.get(facing);
        if (handler.getFluidHandler() != null && handler.getItemHandler() != null) {
            return handler;
        }
        if (handler.getFluidHandler() == null) {
            return handler.getItemHandler();
        }
        if (handler.getItemHandler() == null) {
            return handler.getFluidHandler();
        }
        return handler;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

    @Override
    public boolean acceptsFrom(final ITransportEmitter var1, final EnumFacing var2) {
        Object handler = var1.getHandler(var2);
        if (this.isSink && handler instanceof IItemHandler && var1 instanceof ITransportConductor) {
            return true;
        }
        return this.isSinkFluid && handler instanceof IFluidHandler && var1 instanceof ITransportConductor;
    }

    @Override
    public List<Integer> getDemanded(EnumFacing facing) {
        if (!this.isSink) {
            return Collections.emptyList();
        }

        List<Integer> demandedSlots = new LinkedList<>();
        final List<Integer> list_limits = limitsMap.get(facing);
        for (int i = 0; i < list_limits.size(); i++) {
            final ItemStack stack = handlerMap.get(facing).getStackInSlot(i);
            int limit = list_limits.get(i);
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
