package com.denfop.api.transport;

import com.denfop.api.energy.SystemTick;
import ic2.api.info.ILocatable;
import ic2.core.IC2;
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

public class TransportNetLocal {

    private final World world;

    private final TransportPathMap TransportSourceToTransportPathMap;

    private final Map<BlockPos, ITransportTile> chunkCoordinatesITransportTileMap;

    private final WaitingList waitingList;

    TransportNetLocal(World world) {
        this.TransportSourceToTransportPathMap = new TransportPathMap();
        this.waitingList = new WaitingList();
        this.world = world;
        this.chunkCoordinatesITransportTileMap = new HashMap<>();
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
                    this.waitingList.onTileEntityAdded(getValidReceivers(tile, true), (ITransportAcceptor) tile);
                }
            } else {
                this.waitingList.onTileEntityAdded(getValidReceivers(tile, true), (ITransportAcceptor) tile);
            }
        }
        if (tile instanceof ITransportSource) {
            ITransportSource transportSource = (ITransportSource) tile;
            if (transportSource.isSource()) {
                this.TransportSourceToTransportPathMap.senderPath.add(new SystemTick(tile, null));
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
            this.TransportSourceToTransportPathMap.removeAll(this.TransportSourceToTransportPathMap.getSources((ITransportAcceptor) tile));
            this.waitingList.onTileEntityRemoved((ITransportAcceptor) tile);
        }
        if (tile instanceof ITransportSource) {
            this.TransportSourceToTransportPathMap.remove((ITransportSource) tile);
        }
    }

    public void emitTransportFrom(
            ITransportSource<ItemStack, IItemHandler> TransportSource,
            TransportItem<ItemStack> amount,
            List<TransportPath> TransportPaths,
            SystemTick tick
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
        if (tile instanceof ILocatable) {
            return this.world.getTileEntity(((ILocatable) tile).getPosition());
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
            List<TransportTarget> validReceivers = getValidReceivers(currentTileEntity, false);
            for (TransportTarget validReceiver : validReceivers) {
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
                    assert te != null;
                    IC2.platform.displayError(
                            "An Transport network pathfinding entry is corrupted.\nThis could happen due to incorrect Minecraft behavior or a bug.\n\n(Technical information: TransportBlockLink, tile entities below)\nE: " + emitter + " (" + te

                                    .getX() + "," + te.getY() + "," + te

                                    .getZ() + ")\nC: " + tileEntity + " (" + te.getX() + "," + te

                                    .getY() + "," + te

                                    .getZ() + ")\nR: " + TransportPath.target + " (" + TransportPath.target.getBlockPos()

                                    .getX() + "," + TransportPath.target.getBlockPos().getY() + "," + TransportPath.target
                                    .getBlockPos()
                                    .getZ() + ")");
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

    private List<TransportTarget> getValidReceivers(ITransportTile emitter, boolean reverse) {
        List<TransportTarget> validReceivers = new LinkedList<>();
        for (EnumFacing direction : EnumFacing.values()) {
            ITransportTile target2 = getNeighbor(emitter, direction);
            if (target2 != null) {
                EnumFacing inverseDirection2 = direction.getOpposite();
                if (reverse) {
                    if (emitter instanceof ITransportAcceptor && target2 instanceof ITransportEmitter) {
                        ITransportEmitter sender2 = (ITransportEmitter) target2;
                        ITransportAcceptor receiver2 = (ITransportAcceptor) emitter;
                        if (sender2.emitsTo(receiver2, inverseDirection2) && receiver2.acceptsFrom(sender2, direction)) {
                            validReceivers.add(new TransportTarget(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof ITransportEmitter && target2 instanceof ITransportAcceptor) {
                    ITransportEmitter sender2 = (ITransportEmitter) emitter;
                    ITransportAcceptor receiver2 = (ITransportAcceptor) target2;
                    if (sender2.emitsTo(receiver2, direction) && receiver2.acceptsFrom(sender2, inverseDirection2)) {
                        validReceivers.add(new TransportTarget(target2, inverseDirection2));
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
            List<TransportTarget> targets = getValidReceivers(tile, true);
            for (TransportTarget TransportTarget : targets) {
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
        if (this.waitingList.hasWork()) {
            List<ITransportTile> tiles = this.waitingList.getPathTiles();
            for (ITransportTile tile : tiles) {
                List<ITransportSource> sources = discoverFirstPathOrSources(tile);
                if (sources.size() > 0) {
                    this.TransportSourceToTransportPathMap.removeAllSource1(sources);
                }
            }
            this.waitingList.clear();
        }
        try {
            if (this.world.getWorldTime() % 5L == 0L) {
                for (SystemTick<ITransportSource, TransportPath> tick : this.TransportSourceToTransportPathMap.senderPath) {
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
                if (list.isEmpty() ) {
                    break;
                }
                if(!TransportPath.target.canAccept(TransportPath.targetDirection.getOpposite()))
                    continue;
                IFluidHandler handler = TransportPath.getHandler();
                for (FluidStack fluidStack : list) {
                    if (fluidStack.amount <= 0 ) {
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

    public void onUnload() {
        this.TransportSourceToTransportPathMap.clear();
        this.waitingList.clear();
        this.chunkCoordinatesITransportTileMap.clear();
    }

    static class TransportTarget {

        final ITransportTile tileEntity;

        final EnumFacing direction;

        TransportTarget(ITransportTile tileEntity, EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }

    }

    static class TransportPathMap {

        final List<SystemTick<ITransportSource, TransportNetLocal.TransportPath>> senderPath = new ArrayList<>();

        public void put(ITransportSource par1, List<TransportNetLocal.TransportPath> par2) {
            this.senderPath.add(new SystemTick(par1, par2));
        }

        public boolean containsKey(SystemTick<ITransportSource, TransportNetLocal.TransportPath> par1) {
            return this.senderPath.contains(par1);
        }

        public boolean containsKey(ITransportSource par1) {
            return this.senderPath.contains(new SystemTick(par1, null));
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

        public void remove(SystemTick<ITransportSource, TransportNetLocal.TransportPath> par1) {
            this.senderPath.remove(par1);
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

        public void clear() {
            for (SystemTick<ITransportSource, TransportNetLocal.TransportPath> entry : this.senderPath) {
                List<TransportNetLocal.TransportPath> list = entry.getList();
                if (list != null) {
                    for (TransportNetLocal.TransportPath TransportPath : list) {
                        TransportPath.conductors.clear();
                    }
                }
            }
            this.senderPath.clear();
        }

        public SystemTick<ITransportSource, TransportNetLocal.TransportPath> get(ITransportSource tileEntity) {
            for (SystemTick<ITransportSource, TransportNetLocal.TransportPath> entry : this.senderPath) {
                if (entry.getSource() == tileEntity) {
                    return entry;
                }
            }
            return null;
        }

    }

    static class PathLogic {

        final List<ITransportTile> tiles = new ArrayList<>();

        public boolean contains(ITransportTile par1) {
            return this.tiles.contains(par1);
        }

        public void add(ITransportTile par1) {
            this.tiles.add(par1);
        }

        public void remove(ITransportTile par1) {
            this.tiles.remove(par1);
        }

        public void clear() {
            this.tiles.clear();
        }

        public ITransportTile getRepresentingTile() {
            if (this.tiles.isEmpty()) {
                return null;
            }
            return this.tiles.get(0);
        }

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
                this

                        .fluidHandler = TransportNetLocal.this.getTileFromITransport(this.target).getCapability(
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

    class WaitingList {

        final List<TransportNetLocal.PathLogic> paths = new ArrayList<>();

        public void onTileEntityAdded(List<TransportNetLocal.TransportTarget> around, ITransportAcceptor tile) {
            if (around.isEmpty() || this.paths.isEmpty()) {
                createNewPath(tile);
                return;
            }
            boolean found = false;
            List<TransportNetLocal.PathLogic> logics = new ArrayList<>();
            for (TransportNetLocal.PathLogic logic : this.paths) {
                if (logic.contains(tile)) {
                    found = true;
                    if (tile instanceof ITransportConductor) {
                        logics.add(logic);
                    }
                    continue;
                }
                for (TransportNetLocal.TransportTarget target : around) {
                    if (logic.contains(target.tileEntity)) {
                        found = true;
                        logic.add(tile);
                        if (target.tileEntity instanceof ITransportConductor) {
                            logics.add(logic);
                        }
                    }
                }
            }
            if (logics.size() > 1 && tile instanceof ITransportConductor) {
                TransportNetLocal.PathLogic newLogic = new TransportNetLocal.PathLogic();
                for (TransportNetLocal.PathLogic logic2 : logics) {
                    this.paths.remove(logic2);
                    for (ITransportTile toMove : logic2.tiles) {
                        if (!newLogic.contains(toMove)) {
                            newLogic.add(toMove);
                        }
                    }
                }
                this.paths.add(newLogic);
            }
            if (!found) {
                createNewPath(tile);
            }
        }

        public void onTileEntityRemoved(ITransportAcceptor par1) {
            if (this.paths.isEmpty()) {
                return;
            }
            List<ITransportTile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); i++) {
                TransportNetLocal.PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (ITransportTile tile : toRecalculate) {
                onTileEntityAdded(TransportNetLocal.this.getValidReceivers(tile, true), (ITransportAcceptor) tile);
            }
        }

        public void createNewPath(ITransportTile par1) {
            TransportNetLocal.PathLogic logic = new TransportNetLocal.PathLogic();
            logic.add(par1);
            this.paths.add(logic);
        }

        public void clear() {
            if (this.paths.isEmpty()) {
                return;
            }
            this.paths.clear();
        }

        public boolean hasWork() {
            return (this.paths.size() > 0);
        }

        public List<ITransportTile> getPathTiles() {
            List<ITransportTile> tiles = new ArrayList<>();
            for (TransportNetLocal.PathLogic path : this.paths) {
                ITransportTile tile = path.getRepresentingTile();
                if (tile != null) {
                    tiles.add(tile);
                }
            }
            return tiles;
        }

    }

}
