package com.denfop.api.transport;

import com.denfop.api.energy.SystemTick;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.sytem.Logic;
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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TransportNetLocal {

    final List<SystemTick<ITransportSource, TransportNetLocal.TransportPath>> senderPath = new ArrayList<>();
    final List<Logic<ITransportTile>> paths = new ArrayList<>();
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

    public void removeAllSource1(List<ITransportSource> par1) {
        if (par1 == null) {
            return;
        }
        for (ITransportSource iTransportSource : par1) {
            remove1(iTransportSource);
        }
    }

    public List<SystemTick<ITransportSource, TransportNetLocal.TransportPath>> getSources(ITransportAcceptor par1) {
        List<SystemTick<ITransportSource, TransportNetLocal.TransportPath>> source = new ArrayList<>();
        for (SystemTick<ITransportSource, TransportNetLocal.TransportPath> entry : this.senderPath) {
            if (source.contains(entry)) {
                continue;
            }
            if (entry.getList() != null) {
                for (TransportNetLocal.TransportPath path : entry.getList()) {
                    if ((!(par1 instanceof ITransportConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof ITransportSink) || path.target != par1)) {
                        continue;
                    }
                    source.add(entry);
                }
            }
        }
        return source;
    }

    public void addTile(ITransportTile tile1) {
        addTileEntity(tile1.getBlockPos(), tile1);
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

    public List<ITransportSource> discoverFirstPathOrSources(ITransportTile par1) {
        Set<ITransportTile> reached = new HashSet<>();
        List<ITransportSource> result = new ArrayList<>();
        List<ITransportTile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            ITransportTile tile = workList.remove(0);
            List<InfoTile<ITransportTile>> targets = getValidReceivers(tile, true);
            for (InfoTile<ITransportTile> TransportTarget : targets) {
                ITransportTile target = TransportTarget.tileEntity;
                if (target != par1 &&
                        !reached.contains(target)) {
                    reached.add(target);
                    if (target instanceof ITransportSource) {
                        result.add((ITransportSource) target);
                        continue;
                    }
                    if (target instanceof ITransportConductor) {
                        workList.add(target);
                    }
                }
            }
        }
        return result;
    }

    public void onTickEnd() {
        if (this.paths.size() > 0) {
            List<ITransportTile> tiles = this.getPathTiles();
            for (ITransportTile tile : tiles) {
                List<ITransportSource> sources = discoverFirstPathOrSources(tile);
                if (sources.size() > 0) {
                    this.removeAllSource1(sources);
                }
            }
            this.paths.clear();
        }
        try {
            if (this.world.getWorldTime() % 5L == 0L) {
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

    public void onTileEntityAdded(ITransportAcceptor tile) {
        if (this.paths.isEmpty()) {
            this.createNewPath(tile);
            return;
        }
        List<Logic<ITransportTile>> logics = new ArrayList<>();

        paths.removeIf(logic -> {
            if (logic.contains(tile)) {
                if (tile instanceof ITransportConductor) {
                    Logic<ITransportTile> newLogic = new Logic<>();
                    logics.add(newLogic);
                    logic.tiles.forEach(toMove -> {
                        if (!newLogic.contains(toMove)) {
                            newLogic.add(toMove);
                        }
                    });
                }
                return true;
            }
            return false;
        });

        paths.addAll(logics);
    }

    public void onTileEntityRemoved(ITransportAcceptor par1) {
        List<ITransportTile> toRecalculate = new ArrayList<>(paths).stream()
                .filter(logic -> logic.contains(par1))
                .peek(logic -> {
                    logic.remove(par1);
                    paths.remove(logic);
                })
                .flatMap(logic -> logic.tiles.stream())
                .collect(Collectors.toList());

        toRecalculate.forEach(tile -> this.onTileEntityAdded((ITransportAcceptor) tile));
    }

    public void createNewPath(ITransportTile par1) {
        Logic<ITransportTile> logic = new Logic<ITransportTile>();
        logic.add(par1);
        this.paths.add(logic);
    }


    public List<ITransportTile> getPathTiles() {
        List<ITransportTile> tiles = new ArrayList<>();
        for (Logic<ITransportTile> path : this.paths) {
            ITransportTile tile = path.getRepresentingTile();
            if (tile != null) {
                tiles.add(tile);
            }
        }
        return tiles;
    }

    public void onUnload() {
        this.senderPath.clear();
        this.paths.clear();
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
