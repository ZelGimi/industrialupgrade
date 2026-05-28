package com.denfop.api.otherenergies.transport;

import com.denfop.api.otherenergies.common.InfoTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.*;

public class TransportFluidItemSinkSource implements ITransportSource, ITransportSink {

    private static final Direction[] DIRECTIONS = Direction.values();

    private final BlockPos pos;
    private final boolean isClientSide;

    private final Map<Direction, ItemFluidHandler> handlerMap = new EnumMap<>(Direction.class);
    private final Map<Direction, Integer> slotsMap = new EnumMap<>(Direction.class);
    private final Map<Direction, List<Integer>> limitsMap = new EnumMap<>(Direction.class);
    private final Map<Direction, ITransportTile> energyConductorMap = new EnumMap<>(Direction.class);
    private final List<InfoTile<ITransportTile>> validReceivers = new ArrayList<>();
    private final List<Integer> energyTickList = new ArrayList<>();
    private boolean hasHashCode = false;
    private int hashCodeSource;
    private boolean isSink;
    private boolean isSource;
    private boolean isSinkFluid;
    private boolean isSourceFluid;
    private long id;
    private int hashCode;

    public TransportFluidItemSinkSource(BlockEntity parent, BlockPos pos) {
        this.pos = pos;
        this.isClientSide = parent.getLevel() != null && parent.getLevel().isClientSide;
        this.initializeHandlers(parent);
    }

    private void initializeHandlers(BlockEntity parent) {
        boolean foundItem = false;
        boolean foundFluid = false;

        for (Direction facing : DIRECTIONS) {
            final IItemHandler itemStorage = parent.getLevel().getCapability(
                    Capabilities.ItemHandler.BLOCK,
                    pos,
                    facing
            );
            final IFluidHandler fluidStorage = parent.getLevel().getCapability(
                    Capabilities.FluidHandler.BLOCK,
                    pos,
                    facing
            );

            if (!foundItem && itemStorage != null) {
                foundItem = true;
            }
            if (!foundFluid && fluidStorage != null) {
                foundFluid = true;
            }

            final int slots = itemStorage != null ? itemStorage.getSlots() : 0;

            this.handlerMap.put(facing, new ItemFluidHandler(itemStorage, fluidStorage));
            this.slotsMap.put(facing, slots);

            final List<Integer> listLimits = new ArrayList<>(slots);
            if (itemStorage != null) {
                for (int i = 0; i < slots; i++) {
                    listLimits.add(itemStorage.getSlotLimit(i));
                }
            }
            this.limitsMap.put(facing, listLimits);
        }

        this.isSink = foundItem;
        this.isSource = foundItem;
        this.isSinkFluid = foundFluid;
        this.isSourceFluid = foundFluid;
    }

    @Override
    public long getIdNetwork() {
        return id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public void RemoveTile(ITransportTile tile, final Direction facing1) {
        if (isClientSide) {
            return;
        }

        this.energyConductorMap.remove(facing1);

        final Iterator<InfoTile<ITransportTile>> iter = validReceivers.iterator();
        while (iter.hasNext()) {
            final InfoTile<ITransportTile> tileInfoTile = iter.next();
            if (tileInfoTile.tileEntity.getPos().equals(tile.getPos())) {
                iter.remove();
                break;
            }
        }
    }

    @Override
    public int hashCode() {
        if (!hasHashCode) {
            hasHashCode = true;
            this.hashCode = System.identityHashCode(this);
        }
        return hashCode;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        this.hashCodeSource = hashCode;
    }

    @Override
    public Map<Direction, ITransportTile> getTiles() {
        return energyConductorMap;
    }

    @Override
    public List<InfoTile<ITransportTile>> getValidReceivers() {
        return validReceivers;
    }

    @Override
    public void AddTile(ITransportTile tile, final Direction facing1) {
        if (isClientSide) {
            return;
        }

        if (this.energyConductorMap.putIfAbsent(facing1, tile) == null) {
            validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
        }
    }

    @Override
    public boolean emitsTo(final ITransportAcceptor var1, final Direction var2) {
        final ItemFluidHandler handler = this.handlerMap.get(var2);
        if (handler == null) {
            return false;
        }

        if (this.isSource && handler.getItemHandler() != null && var1 instanceof ITransportConductor) {
            return true;
        }

        return this.isSourceFluid && handler.getFluidHandler() != null && var1 instanceof ITransportConductor;
    }

    @Override
    public TransportItem<?> getOffered(final int type, Direction facing) {
        final ItemFluidHandler sideHandler = this.handlerMap.get(facing);
        if (sideHandler == null) {
            return new TransportItem<>();
        }

        if (type == 0) {
            final TransportItem<ItemStack> itemTransportItem = new TransportItem<>();
            final List<ItemStack> itemStackList = new ArrayList<>();
            final List<Integer> integerList = new ArrayList<>();

            final IItemHandler itemHandler = sideHandler.getItemHandler();
            if (itemHandler == null) {
                itemTransportItem.setList(itemStackList);
                itemTransportItem.setList1(integerList);
                return itemTransportItem;
            }

            final int slots = this.slotsMap.getOrDefault(facing, 0);
            final List<Integer> listLimits = this.limitsMap.getOrDefault(facing, Collections.emptyList());

            for (int i = 0; i < slots; i++) {
                final int limit = listLimits.get(i);
                if (limit <= 0) {
                    continue;
                }

                final ItemStack stack = itemHandler.extractItem(i, limit, true);
                if (!stack.isEmpty()) {
                    itemStackList.add(stack);
                    integerList.add(i);
                }
            }

            itemTransportItem.setList(itemStackList);
            itemTransportItem.setList1(integerList);
            return itemTransportItem;
        } else {
            final TransportItem<FluidStack> fluidTransportItem = new TransportItem<>();
            final List<FluidStack> fluidStackList = new ArrayList<>();

            final IFluidHandler handler = sideHandler.getFluidHandler();
            if (handler == null) {
                fluidTransportItem.setList(fluidStackList);
                return fluidTransportItem;
            }

            final int fluidTanks = handler.getTanks();
            for (int i = 0; i < fluidTanks; i++) {
                final FluidStack contents = handler.getFluidInTank(i);
                if (!contents.isEmpty() && !handler.drain(contents, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
                    fluidStackList.add(contents.copy());
                }
            }

            fluidTransportItem.setList(fluidStackList);
            return fluidTransportItem;
        }
    }

    @Override
    public void draw(final Object var, final int col, Direction facing) {
        final ItemFluidHandler handler = this.handlerMap.get(facing);
        if (handler == null) {
            return;
        }

        if (this.isSource && var instanceof ItemStack) {
            final IItemHandler itemHandler = handler.getItemHandler();
            if (itemHandler != null) {
                itemHandler.extractItem(col, ((ItemStack) var).getCount(), false);
            }
        }

        if (this.isSourceFluid && var instanceof FluidStack) {
            final IFluidHandler fluidHandler = handler.getFluidHandler();
            if (fluidHandler != null) {
                final FluidStack fluidStack = ((FluidStack) var).copy();
                fluidStack.setAmount(col);
                fluidHandler.drain(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            }
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
        return this.isSource || this.isSourceFluid;
    }

    @Override
    public Object getHandler(Direction facing) {
        final ItemFluidHandler handler = this.handlerMap.get(facing);
        if (handler == null) {
            return null;
        }

        final IFluidHandler fluidHandler = handler.getFluidHandler();
        final IItemHandler itemHandler = handler.getItemHandler();

        if (fluidHandler != null && itemHandler != null) {
            return handler;
        }
        if (fluidHandler == null) {
            return itemHandler;
        }
        if (itemHandler == null) {
            return fluidHandler;
        }

        return handler;
    }

    @Override
    public BlockPos getPos() {
        return this.pos;
    }

    @Override
    public boolean acceptsFrom(final ITransportEmitter var1, final Direction var2) {
        final Object handler = var1.getHandler(var2);
        if (handler == null) {
            return false;
        }

        if (this.isSink && handler instanceof IItemHandler && var1 instanceof ITransportConductor) {
            return true;
        }

        return this.isSinkFluid && handler instanceof IFluidHandler && var1 instanceof ITransportConductor;
    }

    @Override
    public List<Integer> getDemanded(Direction facing) {
        if (!this.isSink) {
            return Collections.emptyList();
        }

        final ItemFluidHandler handler = handlerMap.get(facing);
        if (handler == null || handler.getItemHandler() == null) {
            return Collections.emptyList();
        }

        final List<Integer> listLimits = limitsMap.getOrDefault(facing, Collections.emptyList());
        if (listLimits.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Integer> demandedSlots = new ArrayList<>(listLimits.size());

        for (int i = 0, size = listLimits.size(); i < size; i++) {
            final ItemStack stack = handler.getStackInSlot(i);
            final int limit = listLimits.get(i);
            final int maxStackSize = stack.getMaxStackSize();

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