package com.denfop.api.transport;

import com.denfop.api.energy.SystemTick;
import com.denfop.api.sytem.InfoTile;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransportNetLocal {

    final List<SystemTick<ITransportSource, TransportNetLocal.TransportPath>> senderPath = new ArrayList<>();

    private final World world;
    private final Map<BlockPos, ITransportTile> chunkCoordinatesITransportTileMap;


    TransportNetLocal(World world) {
        this.world = world;
        this.chunkCoordinatesITransportTileMap = new HashMap<>();
    }

    public void remove1(ITransportSource par1) {
        for (SystemTick<ITransportSource, TransportNetLocal.TransportPath> ticks : this.senderPath) {
            if (ticks.getSource() == par1) {
                ticks.setList(null);
                break;
            }
        }
    }

    public void remove(ITransportSource par1) {
        this.senderPath.remove(new SystemTick(par1, null));
    }

    public void removeAll(List<SystemTick<ITransportSource, TransportNetLocal.TransportPath>> par1) {
        if (par1 == null) {
            return;
        }
        for (SystemTick<ITransportSource, TransportNetLocal.TransportPath> iTransportSource : par1) {
            iTransportSource.setList(null);
        }
    }



    public List<SystemTick<ITransportSource, TransportNetLocal.TransportPath>> getSources(ITransportAcceptor par1) {
        List<SystemTick<ITransportSource, TransportNetLocal.TransportPath>> source = new ArrayList<>();
        for (SystemTick<ITransportSource, TransportNetLocal.TransportPath> entry : this.senderPath) {
            if (entry.getList() != null) {
                for (TransportNetLocal.TransportPath path : entry.getList()) {
                    if ((!(par1 instanceof ITransportConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof ITransportSink) || path.target != par1)) {
                        continue;
                    }
                    source.add(entry);
                    break;
                }
            }
        }
        return source;
    }

    public void addTile(ITransportTile tile1) {
        addTileEntity(tile1.getBlockPos(), tile1);
    }
    public boolean containsKey(final  SystemTick<ITransportSource, TransportNetLocal.TransportPath> par1) {
        return this.senderPath.contains(par1);
    }
    public void addTileEntity(BlockPos coords, ITransportTile tile) {
        if (this.chunkCoordinatesITransportTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesITransportTileMap.put(coords, tile);
        update(coords);
        if (tile instanceof ITransportAcceptor) {
            if (tile instanceof ITransportSink) {
                ITransportSink transportSink = (ITransportSink) tile;
                if (transportSink.isSink()) {
                    this.onTileEntityAdded((ITransportAcceptor) tile);
                }
            } else {
                this.onTileEntityAdded((ITransportAcceptor) tile);
            }
        }
        if (tile instanceof ITransportSource) {
            ITransportSource transportSource = (ITransportSource) tile;
            if (transportSource.isSource()) {
                this.senderPath.add(new SystemTick(tile, null));
            }
        }
    }

    public void removeTile(ITransportTile tile1) {
        removeTileEntity(tile1);
    }

    public void removeTileEntity(ITransportTile tile) {
        if (!this.chunkCoordinatesITransportTileMap.containsKey(tile.getBlockPos())) {
            return;
        }
        this.chunkCoordinatesITransportTileMap.remove(tile.getBlockPos(), tile);
        update(tile.getBlockPos());
        if (tile instanceof ITransportAcceptor) {
            this.removeAll(this.getSources((ITransportAcceptor) tile));
            this.onTileEntityRemoved((ITransportAcceptor) tile);
        }
        if (tile instanceof ITransportSource) {
            this.remove((ITransportSource) tile);
        }
    }

    public void emitTransportFrom(
            ITransportSource<ItemStack, IItemHandler> TransportSource,
            TransportItem<ItemStack> amount,
            List<TransportPath> TransportPaths,
            SystemTick<ITransportSource, TransportPath> tick
    ) {

        if (TransportPaths == null) {
            TransportPaths = discover(TransportSource);
            tick.setList(TransportPaths);
        }
        List<ItemStack> list = amount.getList();
        List<Integer> list1 = amount.getList1();

        if (!list.isEmpty()) {
            for (TransportPath TransportPath : TransportPaths) {
                if (list.isEmpty()) {
                    break;
                }
                ITransportSink<ItemStack, IItemHandler> TransportSink = TransportPath.target;
                List<Integer> demandedTransport = TransportSink.getDemanded();

                if (demandedTransport.isEmpty() || !TransportSink.canAccept(TransportPath.targetDirection.getOpposite())) {
                    continue;
                }
                for (Integer integer : demandedTransport) {
                    for (int i = 0; i < list1.size(); i++) {
                        if (!list.get(i).isEmpty()) {
                            ItemStack stack = TransportSink.getHandler().insertItem(integer, list.get(i), true);
                            if (stack.isEmpty() && stack.getCount() != list.get(i).getCount()) {
                                stack = TransportSink.getHandler().insertItem(integer, list.get(i).copy(), false);
                                if (!stack.isEmpty()) {
                                    TransportSource.draw(stack, list1.get(i));
                                    list.get(i).setCount(stack.getCount());
                                } else {
                                    TransportSource.draw(list.get(i), list1.get(i));
                                }
                                list.get(i).setCount(0);
                            } else if (!stack.isEmpty() && stack.getCount() != list.get(i).getCount()) {
                                stack = TransportSink.getHandler().insertItem(integer, list.get(i).copy(), false);
                                stack.setCount(list.get(i).getCount() - stack.getCount());
                                if (!stack.isEmpty()) {
                                    TransportSource.draw(stack, list1.get(i));
                                    list.get(i).setCount(stack.getCount());

                                } else {
                                    TransportSource.draw(list.get(i), list1.get(i));
                                }
                                list.get(i).setCount(0);
                            }
                        }
                    }
                }
            }
        }
    }

    public TileEntity getTileFromITransport(ITransportTile tile) {
        if (tile == null) {
            return null;
        }
        if (tile instanceof TileEntity) {
            return (TileEntity) tile;
        }

        return this.world.getTileEntity(tile.getBlockPos());
    }

    public List<TransportPath> discover(ITransportSource emitter) {
        Map<ITransportConductor, EnumFacing> reachedTileEntities = new HashMap<>();
        List<ITransportTile> tileEntitiesToCheck = new ArrayList<>();
        List<TransportPath> TransportPaths = new ArrayList<>();
        tileEntitiesToCheck.add(emitter);
        while (!tileEntitiesToCheck.isEmpty()) {
            ITransportTile currentTileEntity = tileEntitiesToCheck.remove(0);
            List<InfoTile<ITransportTile>> validReceivers = getValidReceivers(currentTileEntity, false);
            for (InfoTile<ITransportTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {
                    if (validReceiver.tileEntity instanceof ITransportSink) {
                        TransportPaths.add(new TransportPath((ITransportSink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }
                    if (reachedTileEntities.containsKey(validReceiver.tileEntity)) {
                        continue;
                    }
                    reachedTileEntities.put((ITransportConductor) validReceiver.tileEntity, validReceiver.direction);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }
        }
        for (TransportPath TransportPath : TransportPaths) {
            ITransportTile tileEntity = TransportPath.target;
            EnumFacing TransportBlockLink = TransportPath.targetDirection;
            if (emitter != null) {
                while (tileEntity != emitter) {
                    BlockPos te = tileEntity.getBlockPos();
                    if (TransportBlockLink != null && te != null) {
                        tileEntity = getTileEntity(te.offset(TransportBlockLink));
                    }
                    if (!(tileEntity instanceof ITransportConductor)) {
                        break;
                    }
                    TransportPath.conductors.add((ITransportConductor) tileEntity);
                    TransportBlockLink = reachedTileEntities.get(tileEntity);
                    if (TransportBlockLink != null) {
                        continue;
                    }

                }
            }
        }
        return TransportPaths;
    }

    public ITransportTile getNeighbor(ITransportTile tile, EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        return getTileEntity(tile.getBlockPos().offset(dir));
    }

    private List<InfoTile<ITransportTile>> getValidReceivers(ITransportTile emitter, boolean reverse) {
        List<InfoTile<ITransportTile>> validReceivers = new LinkedList<>();
        for (EnumFacing direction : EnumFacing.values()) {
            ITransportTile target2 = getNeighbor(emitter, direction);
            if (target2 != null) {
                EnumFacing inverseDirection2 = direction.getOpposite();
                if (reverse) {
                    if (emitter instanceof ITransportAcceptor && target2 instanceof ITransportEmitter) {
                        ITransportEmitter sender2 = (ITransportEmitter) target2;
                        ITransportAcceptor receiver2 = (ITransportAcceptor) emitter;
                        if (sender2.emitsTo(receiver2, inverseDirection2) && receiver2.acceptsFrom(sender2, direction)) {
                            validReceivers.add(new InfoTile<ITransportTile>(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof ITransportEmitter && target2 instanceof ITransportAcceptor) {
                    ITransportEmitter sender2 = (ITransportEmitter) emitter;
                    ITransportAcceptor receiver2 = (ITransportAcceptor) target2;
                    if (sender2.emitsTo(receiver2, direction) && receiver2.acceptsFrom(sender2, inverseDirection2)) {
                        validReceivers.add(new InfoTile<ITransportTile>(target2, inverseDirection2));
                    }
                }
            }
        }
        return validReceivers;
    }



    public void onTickEnd() {
        if (sourceToUpdateList.size() > 0) {
            for (ITransportSource source : sourceToUpdateList) {
                remove1(source);
            }
            sourceToUpdateList.clear();
        }
        try {
            if (this.world.getWorldTime() % 3L == 0L) {
                for (SystemTick<ITransportSource, TransportPath> tick : this.senderPath) {
                    if (tick.getSource().isItem()) {
                        ITransportSource<ItemStack, IItemHandler> entry = (ITransportSource<ItemStack, IItemHandler>) tick.getSource();


                        if (entry != null) {
                            TransportItem<ItemStack> offered = entry.getOffered(0);

                            if (!offered.getList().isEmpty()) {
                                emitTransportFrom(entry, offered, tick.getList(), tick);
                            }
                        }
                    }
                    if (tick.getSource().isFluid()) {
                        ITransportSource<FluidStack, IFluidHandler> entry = (ITransportSource<FluidStack, IFluidHandler>) tick.getSource();
                        if (entry != null) {
                            TransportItem<FluidStack> offered = entry.getOffered(1);
                            if (!offered.getList().isEmpty()) {
                                emitTransportFluidFrom(entry, offered, tick.getList(), tick);
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public void emitTransportFluidFrom(
            ITransportSource<FluidStack, IFluidHandler> TransportSource,
            TransportItem<FluidStack> transportItem,
            List<TransportPath> TransportPaths,
            SystemTick<ITransportSource, TransportPath> tick
    ) {
        if (TransportPaths == null) {
            TransportPaths = discover(TransportSource);
            tick.setList(TransportPaths);
        }
        List<FluidStack> list = transportItem.getList();
        if (!list.isEmpty()) {
            for (TransportPath TransportPath : TransportPaths) {
                if (list.isEmpty()) {
                    break;
                }
                if (!TransportPath.target.canAccept(TransportPath.targetDirection.getOpposite())) {
                    continue;
                }
                IFluidHandler handler = TransportPath.getHandler();
                for (FluidStack fluidStack : list) {
                    if (fluidStack.amount <= 0) {
                        continue;
                    }
                    int amount = handler.fill(fluidStack, false);
                    if (amount > 0) {
                        TransportSource.draw(fluidStack, handler.fill(fluidStack.copy(), true));
                    }
                }
            }
        }
    }

    public ITransportTile getTileEntity(BlockPos pos) {
        return this.chunkCoordinatesITransportTileMap.get(pos);
    }

    public void update(BlockPos pos) {
        for (EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos.offset(dir);
            ITransportTile tile = this.chunkCoordinatesITransportTileMap.get(pos1);
            if (tile instanceof ITransportConductor) {
                ((ITransportConductor) tile).update_render();
            }
        }
    }
    final List<ITransportSource> sourceToUpdateList = new ArrayList<>();
    public void onTileEntityAdded(final ITransportAcceptor tile) {
        final List<ITransportTile> tileEntitiesToCheck = new ArrayList<>();

        final List<BlockPos> blockPosList = new ArrayList<>();
        blockPosList.add(tile.getBlockPos());
        tileEntitiesToCheck.add(tile);
        while (!tileEntitiesToCheck.isEmpty()) {
            final ITransportTile currentTileEntity = tileEntitiesToCheck.remove(0);
            for (final EnumFacing direction : EnumFacing.values()) {
                final ITransportTile target2 = this.getTileEntity(currentTileEntity.getBlockPos().offset(direction));
                if (target2 != null && !blockPosList.contains(target2.getBlockPos())) {
                    blockPosList.add(target2.getBlockPos());
                    if (target2 instanceof ITransportSource) {
                        if (!sourceToUpdateList.contains((ITransportSource) target2)) {
                            sourceToUpdateList.add((ITransportSource) target2);
                        }
                        continue;
                    }
                    if (target2 instanceof ITransportConductor) {
                        tileEntitiesToCheck.add(target2);
                    }
                }
            }


        }

    }

    public void onTileEntityRemoved(final ITransportAcceptor par1) {

        this.onTileEntityAdded(par1);
    }



    public void onUnload() {
        this.senderPath.clear();
        this.chunkCoordinatesITransportTileMap.clear();
    }


    public class TransportPath {

        final List<ITransportConductor> conductors;

        final ITransportSink target;

        final EnumFacing targetDirection;

        IFluidHandler fluidHandler = null;


        TransportPath(ITransportSink sink, EnumFacing facing) {
            this.target = sink;
            this.conductors = new ArrayList<>();
            this.targetDirection = facing;
            if (this.target.getHandler() instanceof IFluidHandler) {
                this.fluidHandler = TransportNetLocal.this.getTileFromITransport(this.target).getCapability(
                        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                        this.targetDirection
                );
            }
        }

        public List<ITransportConductor> getConductors() {
            return this.conductors;
        }

        public IFluidHandler getHandler() {
            return this.fluidHandler;
        }

    }


}
