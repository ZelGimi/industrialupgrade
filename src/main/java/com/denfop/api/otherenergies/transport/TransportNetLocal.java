package com.denfop.api.otherenergies.transport;

import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.*;

public class TransportNetLocal {

    private static final Direction[] DIRECTIONS = Direction.values();

    final TransportTickList<TransportTick<ITransportSource, Path>> senderPath = new TransportTickList<>();
    private final Level world;
    private final Map<BlockPos, ITransportTile> chunkCoordinatesITransportTileMap;
    private final List<ITransportSource> sourceToUpdateList = new ArrayList<>();
    private final List<ITransportSource> delete = new ArrayList<>();
    private final Map<ConductorSideKey, ItemFilterLists> itemFilterListsCache = new HashMap<>();
    private final Map<ConductorSideKey, FluidFilterLists> fluidFilterListsCache = new HashMap<>();
    private final Map<ConductorSideKey, IdentityHashMap<ItemStack, Boolean>> itemFilterResultCache = new HashMap<>();
    private final Map<ConductorSideKey, IdentityHashMap<FluidStack, Boolean>> fluidFilterResultCache = new HashMap<>();
    private final Map<SinkDemandKey, List<Integer>> demandedSlotsCache = new HashMap<>();
    public int tickUpdate = 0;
    byte tick;
    private long cacheGameTime = Long.MIN_VALUE;

    TransportNetLocal(Level world) {
        this.world = world;
        this.chunkCoordinatesITransportTileMap = new HashMap<>();
    }

    public void remove1(final ITransportSource par1) {
        final int size = this.senderPath.size();
        for (int i = 0; i < size; i++) {
            final TransportTick<ITransportSource, Path> tickData = this.senderPath.get(i);
            if (tickData.getSource() == par1) {
                clearTickData(tickData);
                break;
            }
        }
    }

    public void remove(final ITransportSource par1) {
        final TransportTick<ITransportSource, Path> energyTick = this.senderPath.removeSource(par1);
        clearTickData(energyTick);
    }

    public void removeAll(final List<TransportTick<ITransportSource, Path>> par1) {
        if (par1 == null || par1.isEmpty()) {
            return;
        }

        for (int i = 0, size = par1.size(); i < size; i++) {
            clearTickData(par1.get(i));
        }
    }

    private void clearTickData(final TransportTick<ITransportSource, Path> tickData) {
        if (tickData == null) {
            return;
        }

        final ITransportSource source = tickData.getSource();
        if (source != null) {
            final int sourceHash = source.hashCode();
            unlinkPaths(tickData.getEnergyItemPaths(), sourceHash);

            if (tickData.getEnergyFluidPaths() != tickData.getEnergyItemPaths()) {
                unlinkPaths(tickData.getEnergyFluidPaths(), sourceHash);
            }
        }

        tickData.setItemList(null);
        tickData.setFluidList(null);
        tickData.getConductors().clear();
    }

    private void unlinkPaths(final List<Path> paths, final int sourceHash) {
        if (paths == null || paths.isEmpty()) {
            return;
        }

        for (int i = 0, size = paths.size(); i < size; i++) {
            final Path path = paths.get(i);
            if (path != null && path.target != null) {
                path.target.getEnergyTickList().remove((Integer) sourceHash);
            }
        }
    }

    public boolean hasInSystem(ITransportAcceptor par1) {
        final BlockPos targetPos = par1.getPos();

        for (int i = 0, size = this.senderPath.size(); i < size; i++) {
            final TransportTick<ITransportSource, Path> entry = this.senderPath.get(i);

            if (containsPos(entry.getEnergyItemPaths(), targetPos)) {
                return true;
            }
            if (containsPos(entry.getEnergyFluidPaths(), targetPos)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsPos(final List<Path> paths, final BlockPos pos) {
        if (paths == null || paths.isEmpty()) {
            return false;
        }

        for (int i = 0, size = paths.size(); i < size; i++) {
            final Path path = paths.get(i);
            if (path == null) {
                continue;
            }

            if (path.first != null && pos.equals(path.first.getPos())) {
                return true;
            }
            if (path.end != null && pos.equals(path.end.getPos())) {
                return true;
            }
        }

        return false;
    }

    public List<TransportTick<ITransportSource, Path>> getSources(final ITransportAcceptor par1) {
        if (par1 instanceof ITransportSink) {
            final List<TransportTick<ITransportSource, Path>> list = new ArrayList<>();
            final List<Integer> energyTickList = ((ITransportSink) par1).getEnergyTickList();

            for (int i = 0, size = senderPath.size(); i < size; i++) {
                final TransportTick<ITransportSource, Path> energyTicks = senderPath.get(i);
                if (energyTickList.contains(energyTicks.getSource().hashCode())) {
                    list.add(energyTicks);
                }
            }
            return list;
        }

        if (par1 instanceof ITransportConductor) {
            final List<TransportTick<ITransportSource, Path>> list = new ArrayList<>();

            for (int i = 0, size = senderPath.size(); i < size; i++) {
                final TransportTick<ITransportSource, Path> energyTicks = senderPath.get(i);
                if (energyTicks.getConductors().contains(par1)) {
                    list.add(energyTicks);
                }
            }

            return list;
        }

        return Collections.emptyList();
    }

    public void addTile(ITransportTile tile1) {
        addTileEntity(tile1.getPos(), tile1);
    }

    public boolean containsKey(final TransportTick<ITransportSource, Path> par1) {
        return this.senderPath.contains(par1);
    }

    public void addTileEntity(BlockPos coords, ITransportTile tile) {
        if (this.chunkCoordinatesITransportTileMap.putIfAbsent(coords, tile) != null) {
            return;
        }

        this.updateAdd(coords, tile);

        if (tile instanceof ITransportAcceptor) {
            this.onTileEntityAdded((ITransportAcceptor) tile);
        }

        if (tile instanceof ITransportSource) {
            this.senderPath.add(new TransportTick<>((ITransportSource) tile, null));
        }

        clearTransientCaches();
    }

    private void updateAdd(BlockPos pos, ITransportTile tile) {
        for (Direction dir : DIRECTIONS) {
            final BlockPos pos1 = pos.offset(dir.getNormal());
            final ITransportTile tile1 = this.chunkCoordinatesITransportTileMap.get(pos1);

            if (tile1 == null) {
                continue;
            }

            final Direction opposite = dir.getOpposite();

            if (tile1 instanceof ITransportEmitter && tile instanceof ITransportAcceptor) {
                final ITransportEmitter sender2 = (ITransportEmitter) tile1;
                final ITransportAcceptor receiver2 = (ITransportAcceptor) tile;

                if (sender2.emitsTo(receiver2, opposite) && receiver2.acceptsFrom(sender2, dir)) {
                    tile1.AddTile(tile, opposite);
                    tile.AddTile(tile1, dir);
                }
            } else if (tile1 instanceof ITransportAcceptor && tile instanceof ITransportEmitter) {
                final ITransportEmitter sender2 = (ITransportEmitter) tile;
                final ITransportAcceptor receiver2 = (ITransportAcceptor) tile1;

                if (sender2.emitsTo(receiver2, dir) && receiver2.acceptsFrom(sender2, opposite)) {
                    tile1.AddTile(tile, opposite);
                    tile.AddTile(tile1, dir);
                }
            }
        }
    }

    public void onTileEntityAdded(final ITransportAcceptor tile) {
        final ArrayDeque<ITransportTile> tileEntitiesToCheck = new ArrayDeque<>();
        tileEntitiesToCheck.addLast(tile);

        final long id = WorldBaseGen.random.nextLong();
        this.sourceToUpdateList.clear();

        while (!tileEntitiesToCheck.isEmpty()) {
            final ITransportTile currentTileEntity = tileEntitiesToCheck.removeLast();
            final List<InfoTile<ITransportTile>> validReceivers = currentTileEntity.getValidReceivers();

            if (validReceivers == null || validReceivers.isEmpty()) {
                continue;
            }

            for (int i = 0, size = validReceivers.size(); i < size; i++) {
                final InfoTile<ITransportTile> validReceiver = validReceivers.get(i);
                final ITransportTile next = validReceiver.tileEntity;

                if (next == tile || next.getIdNetwork() == id) {
                    continue;
                }

                next.setId(id);

                if (next instanceof ITransportSource) {
                    this.sourceToUpdateList.add((ITransportSource) next);
                } else if (next instanceof ITransportConductor) {
                    tileEntitiesToCheck.addLast(next);
                }
            }
        }
    }

    public void removeTile(ITransportTile tile1) {
        removeTileEntity(tile1);
    }

    public void removeTileEntity(ITransportTile tile) {
        if (this.chunkCoordinatesITransportTileMap.remove(tile.getPos()) == null) {
            return;
        }

        if (tile instanceof ITransportAcceptor) {
            this.removeAll(this.getSources((ITransportAcceptor) tile));
        }

        if (tile instanceof ITransportSource) {
            this.remove((ITransportSource) tile);
        }

        this.updateRemove(tile.getPos(), tile);
        clearTransientCaches();
    }

    private void updateRemove(BlockPos pos, ITransportTile tile) {
        for (Direction dir : DIRECTIONS) {
            final BlockPos pos1 = pos.offset(dir.getNormal());
            final ITransportTile tile1 = this.chunkCoordinatesITransportTileMap.get(pos1);

            if (tile1 != null) {
                tile1.RemoveTile(tile, dir.getOpposite());
            }
        }
    }

    public boolean canInsertOrExtract(ITransportConductor transportConductor, ItemStack stack, Direction facing) {
        if (stack.isEmpty()) {
            return false;
        }

        final ConductorSideKey sideKey = new ConductorSideKey(transportConductor, facing);

        ItemFilterLists lists = itemFilterListsCache.get(sideKey);
        if (lists == null) {
            final List<ItemStack> blackList = safeItemList(transportConductor.getBlackListItems(facing));
            final List<ItemStack> whiteList = safeItemList(transportConductor.getWhiteListItems(facing));
            lists = new ItemFilterLists(blackList, whiteList);
            itemFilterListsCache.put(sideKey, lists);
        }

        if (lists.blackList.isEmpty() && lists.whiteList.isEmpty()) {
            return true;
        }

        IdentityHashMap<ItemStack, Boolean> stackResults = itemFilterResultCache.get(sideKey);
        if (stackResults == null) {
            stackResults = new IdentityHashMap<>();
            itemFilterResultCache.put(sideKey, stackResults);
        }

        final Boolean cached = stackResults.get(stack);
        if (cached != null) {
            return cached;
        }

        final boolean result = evaluateItemFilter(lists, stack);
        stackResults.put(stack, result);
        return result;
    }

    private boolean evaluateItemFilter(final ItemFilterLists lists, final ItemStack stack) {
        if (!lists.blackList.isEmpty()) {
            for (int i = 0, size = lists.blackList.size(); i < size; i++) {
                if (ModUtils.checkItemEquality(lists.blackList.get(i), stack)) {
                    return false;
                }
            }
            return true;
        }

        if (!lists.whiteList.isEmpty()) {
            for (int i = 0, size = lists.whiteList.size(); i < size; i++) {
                if (ModUtils.checkItemEquality(lists.whiteList.get(i), stack)) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    public boolean canInsertOrExtract(ITransportConductor transportConductor, FluidStack stack, Direction facing) {
        if (stack.isEmpty()) {
            return false;
        }

        final ConductorSideKey sideKey = new ConductorSideKey(transportConductor, facing);

        FluidFilterLists lists = fluidFilterListsCache.get(sideKey);
        if (lists == null) {
            final List<FluidStack> blackList = safeFluidList(transportConductor.getBlackListFluids(facing));
            final List<FluidStack> whiteList = safeFluidList(transportConductor.getWhiteListFluids(facing));
            lists = new FluidFilterLists(blackList, whiteList);
            fluidFilterListsCache.put(sideKey, lists);
        }

        if (lists.blackList.isEmpty() && lists.whiteList.isEmpty()) {
            return true;
        }

        IdentityHashMap<FluidStack, Boolean> stackResults = fluidFilterResultCache.get(sideKey);
        if (stackResults == null) {
            stackResults = new IdentityHashMap<>();
            fluidFilterResultCache.put(sideKey, stackResults);
        }

        final Boolean cached = stackResults.get(stack);
        if (cached != null) {
            return cached;
        }

        final boolean result = evaluateFluidFilter(lists, stack);
        stackResults.put(stack, result);
        return result;
    }

    private boolean evaluateFluidFilter(final FluidFilterLists lists, final FluidStack stack) {
        if (!lists.blackList.isEmpty()) {
            for (int i = 0, size = lists.blackList.size(); i < size; i++) {
                if (FluidStack.isSameFluid(lists.blackList.get(i), stack)) {
                    return false;
                }
            }
            return true;
        }

        if (!lists.whiteList.isEmpty()) {
            for (int i = 0, size = lists.whiteList.size(); i < size; i++) {
                if (FluidStack.isSameFluid(lists.whiteList.get(i), stack)) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    private List<ItemStack> safeItemList(final List<ItemStack> list) {
        return list == null ? Collections.emptyList() : list;
    }

    private List<FluidStack> safeFluidList(final List<FluidStack> list) {
        return list == null ? Collections.emptyList() : list;
    }

    private List<Integer> getDemandedSlotsCached(final ITransportSink sink, final Direction side) {
        final SinkDemandKey key = new SinkDemandKey(sink, side);
        List<Integer> cached = demandedSlotsCache.get(key);
        if (cached != null) {
            return cached;
        }

        final List<Integer> demanded = sink.getDemanded(side);
        if (demanded == null || demanded.isEmpty()) {
            cached = Collections.emptyList();
        } else {
            cached = new ArrayList<>(demanded);
        }

        demandedSlotsCache.put(key, cached);
        return cached;
    }

    private void invalidateDemandedSlots(final ITransportSink sink, final Direction side) {
        demandedSlotsCache.remove(new SinkDemandKey(sink, side));
    }

    private void clearTransientCaches() {
        itemFilterListsCache.clear();
        fluidFilterListsCache.clear();
        itemFilterResultCache.clear();
        fluidFilterResultCache.clear();
        demandedSlotsCache.clear();
        cacheGameTime = Long.MIN_VALUE;
    }

    private void prepareTickCaches(final long gameTime) {
        if (cacheGameTime != gameTime) {
            cacheGameTime = gameTime;
            itemFilterListsCache.clear();
            fluidFilterListsCache.clear();
            itemFilterResultCache.clear();
            fluidFilterResultCache.clear();
            demandedSlotsCache.clear();
        }
    }

    @SuppressWarnings("unchecked")
    public void emitTransportFrom(
            ITransportSource<ItemStack, IItemHandler> transportSource,
            List<Path> transportPaths
    ) {
        if (transportPaths == null || transportPaths.isEmpty()) {
            return;
        }

        final EnumMap<Direction, TransportItem<ItemStack>> offeredCache = new EnumMap<>(Direction.class);

        for (int p = 0, pathSize = transportPaths.size(); p < pathSize; p++) {
            final Path path = transportPaths.get(p);

            if (path == null || path.first == null || path.end == null || path.firstSide == null) {
                continue;
            }

            if (path.getHandler() == null) {
                continue;
            }

            if (path.end.getMax(tick) == 0 || path.first.getMax(tick) == 0) {
                continue;
            }

            if (!path.first.canWork() || !path.end.canWork()) {
                continue;
            }

            final List<Integer> demandedSlots = getDemandedSlotsCached(path.target, path.targetDirection);
            if (demandedSlots.isEmpty()) {
                continue;
            }

            TransportItem<ItemStack> amount = offeredCache.get(path.firstSide);
            if (amount == null) {
                amount = (TransportItem<ItemStack>) transportSource.getOffered(0, path.firstSide);
                offeredCache.put(path.firstSide, amount);
            }

            if (amount == null) {
                continue;
            }

            final List<ItemStack> items = amount.getList();
            final List<Integer> indices = amount.getList1();

            if (items == null || indices == null || items.isEmpty() || indices.isEmpty()) {
                continue;
            }

            final Direction sourceCheckFacing = path.firstSide.getOpposite();
            final Direction targetCheckFacing = path.targetDirection.getOpposite();

            boolean insertedAnythingIntoThisSinkSide = false;

            for (int demandedIndex = 0, demandedSize = demandedSlots.size(); demandedIndex < demandedSize; demandedIndex++) {
                if (path.end.getMax(tick) == 0 || path.first.getMax(tick) == 0) {
                    break;
                }

                final int slot = demandedSlots.get(demandedIndex);

                for (int i = 0, itemSize = items.size(); i < itemSize; i++) {
                    if (path.end.getMax(tick) == 0 || path.first.getMax(tick) == 0) {
                        break;
                    }

                    final ItemStack currentItem = items.get(i);
                    if (currentItem.isEmpty()) {
                        continue;
                    }

                    if (!canInsertOrExtract(path.first, currentItem, sourceCheckFacing)
                            || !canInsertOrExtract(path.end, currentItem, targetCheckFacing)) {
                        continue;
                    }

                    final ItemStack simulateRemaining = path.getHandler().insertItem(slot, currentItem, true);
                    if (!simulateRemaining.isEmpty() && simulateRemaining.getCount() == currentItem.getCount()) {
                        continue;
                    }

                    final int maxTransfer = Math.min(
                            currentItem.getCount(),
                            Math.min(path.end.getMax(tick), path.first.getMax(tick))
                    );

                    if (maxTransfer <= 0) {
                        continue;
                    }

                    final ItemStack toInsert = currentItem.copy();
                    toInsert.setCount(maxTransfer);

                    final ItemStack realRemaining = path.getHandler().insertItem(slot, toInsert, false);
                    final int transferredAmount = maxTransfer - realRemaining.getCount();

                    if (transferredAmount <= 0) {
                        continue;
                    }

                    path.end.setMax(transferredAmount);
                    path.first.setMax(transferredAmount);

                    final ItemStack drawnStack = currentItem.split(transferredAmount);
                    transportSource.draw(drawnStack, indices.get(i), path.firstSide);

                    if (currentItem.isEmpty()) {
                        items.set(i, ItemStack.EMPTY);
                    }

                    insertedAnythingIntoThisSinkSide = true;
                }
            }

            if (insertedAnythingIntoThisSinkSide) {
                invalidateDemandedSlots(path.target, path.targetDirection);
            }
        }
    }

    public BlockEntity getTileFromITransport(ITransportTile tile) {
        if (tile == null) {
            return null;
        }

        if (tile instanceof BlockEntity) {
            return (BlockEntity) tile;
        }

        return this.world.getBlockEntity(tile.getPos());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Tuple<List<Path>, LinkedList<ITransportConductor>> discover(final ITransportSource emitter) {
        final ArrayDeque<ITransportTile> tileEntitiesToCheck = new ArrayDeque<>();
        final ArrayList<Path> energyPaths = new ArrayList<>();
        final LinkedList<ITransportConductor> conductors = new LinkedList<>();

        final long id = WorldBaseGen.random.nextLong();
        emitter.setId(id);
        tileEntitiesToCheck.addLast(emitter);

        while (!tileEntitiesToCheck.isEmpty()) {
            final ITransportTile currentTileEntity = tileEntitiesToCheck.removeLast();
            final List<InfoTile<ITransportTile>> validReceivers = currentTileEntity.getValidReceivers();

            if (validReceivers == null || validReceivers.isEmpty()) {
                continue;
            }

            final InfoCable cable = currentTileEntity instanceof ITransportConductor
                    ? ((ITransportConductor) currentTileEntity).getCable()
                    : null;

            for (int i = 0, size = validReceivers.size(); i < size; i++) {
                final InfoTile<ITransportTile> validReceiver = validReceivers.get(i);
                final ITransportTile next = validReceiver.tileEntity;

                if (next == emitter || next.getIdNetwork() == id) {
                    continue;
                }

                if (currentTileEntity == emitter) {
                    if (next instanceof ITransportConductor && ((ITransportConductor<?, ?>) next).isOutput()) {
                        next.setId(id);
                        final ITransportConductor conductor = (ITransportConductor) next;
                        conductor.setCable(new InfoCable(conductor, validReceiver.direction, cable));
                        tileEntitiesToCheck.addLast(next);
                    }
                } else {
                    if (next instanceof ITransportSink
                            && currentTileEntity instanceof ITransportConductor
                            && ((ITransportConductor<?, ?>) currentTileEntity).isInput()) {
                        next.setId(id);
                        energyPaths.add(new Path((ITransportSink) next, validReceiver.direction));
                        continue;
                    }

                    if (next instanceof ITransportConductor) {
                        next.setId(id);
                        final ITransportConductor conductor = (ITransportConductor) next;
                        conductor.setCable(new InfoCable(conductor, validReceiver.direction, cable));
                        tileEntitiesToCheck.addLast(next);
                    }
                }
            }
        }

        final int conductorMark = WorldBaseGen.random.nextInt();

        for (int i = 0, size = energyPaths.size(); i < size; i++) {
            final Path energyPath = energyPaths.get(i);

            ITransportTile tileEntity = (ITransportTile) energyPath.target.getTiles().get(energyPath.targetDirection);
            if (!(tileEntity instanceof ITransportConductor)) {
                energyPath.end = null;
                continue;
            }

            energyPath.end = (ITransportConductor) tileEntity;
            InfoCable cable = energyPath.end.getCable();
            final int max = energyPath.end.getMax();

            while (cable != null) {
                final ITransportConductor energyConductor = cable.getConductor();

                if (energyConductor.getHashCodeSource() != conductorMark) {
                    energyConductor.setHashCodeSource(conductorMark);
                    conductors.add(energyConductor);
                }

                if (energyConductor.getMax() < max) {
                    energyPath.end = null;
                    break;
                }

                cable = cable.getPrev();
                if (cable != null) {
                    energyPath.first = cable.getConductor();
                }
            }

            if (energyPath.first != null) {
                energyPath.firstSide = ModUtils.getFacingFromTwoPositions(emitter.getPos(), energyPath.first.getPos());
            }
        }

        return new Tuple<>(energyPaths, conductors);
    }

    public void onTickEnd() {
        if (!sourceToUpdateList.isEmpty()) {
            for (int i = 0, size = sourceToUpdateList.size(); i < size; i++) {
                remove1(sourceToUpdateList.get(i));
            }
            sourceToUpdateList.clear();
        }

        if (!delete.isEmpty()) {
            for (int i = 0, size = delete.size(); i < size; i++) {
                removeTile(delete.get(i));
            }
            delete.clear();
        }

        if (tickUpdate == 0) {
            tickUpdate = WorldBaseGen.random.nextInt(20 * 20) + 400;
        }

        try {
            final long gameTime = this.world.getGameTime();
            prepareTickCaches(gameTime);

            final boolean updateWrappedTiles = gameTime % tickUpdate == 0L;
            final boolean validateBlockEntities = gameTime % 40L == 0L;
            final boolean itemPhase = (gameTime & 1L) == 0L;

            if (updateWrappedTiles) {
                tickUpdate = WorldBaseGen.random.nextInt(20 * 20) + 400;
                refreshWrappedTiles();
            }

            for (int i = 0, size = this.senderPath.size(); i < size; i++) {
                final TransportTick<ITransportSource, Path> tickData = this.senderPath.get(i);
                final ITransportSource source = tickData.getSource();

                if (source.getValidReceivers().isEmpty() || source.getTiles().isEmpty()) {
                    delete.add(source);
                    continue;
                }

                if (validateBlockEntities) {
                    final BlockEntity entity = world.getBlockEntity(source.getPos());
                    if (entity == null || entity.isRemoved()) {
                        delete.add(source);
                        continue;
                    }
                }

                if (itemPhase && source.isItem()) {
                    processItemSourceTick(tickData, (ITransportSource<ItemStack, IItemHandler>) source);
                }

                if (source.isFluid()) {
                    processFluidSourceTick(tickData, (ITransportSource<FluidStack, IFluidHandler>) source);
                }
            }
        } catch (Exception exception) {
            System.out.println("IUERROR:" + exception.getMessage());
        }

        tick++;
    }

    private void refreshWrappedTiles() {
        final List<ITransportTile> tilesSnapshot = new ArrayList<>(this.chunkCoordinatesITransportTileMap.values());

        for (int i = 0, size = tilesSnapshot.size(); i < size; i++) {
            final ITransportTile tile = tilesSnapshot.get(i);

            if (tile instanceof ITransportConductor<?, ?>) {
                continue;
            }

            this.removeTile(tile);

            final BlockEntity blockEntity = this.world.getBlockEntity(tile.getPos());
            if (blockEntity != null && !blockEntity.isRemoved()) {
                this.addTile(new TransportFluidItemSinkSource(blockEntity, tile.getPos()));
            }
        }
    }

    private void processItemSourceTick(
            final TransportTick<ITransportSource, Path> tickData,
            final ITransportSource<ItemStack, IItemHandler> source
    ) {
        if (tickData.getEnergyItemPaths() == null) {
            final Tuple<List<Path>, LinkedList<ITransportConductor>> tuple = discover(source);
            final List<Path> discoveredPaths = tuple.getA();
            final ArrayList<Path> validPaths = new ArrayList<>(discoveredPaths.size());

            for (int i = 0, size = discoveredPaths.size(); i < size; i++) {
                final Path path = discoveredPaths.get(i);

                if (!isValidItemPath(tickData, path)) {
                    continue;
                }

                path.target.getEnergyTickList().add(tickData.getSource().hashCode());
                validPaths.add(path);
            }

            tickData.setItemList(validPaths);
            tickData.setConductors(tuple.getB());
        }

        final List<Path> itemPaths = tickData.getEnergyItemPaths();
        if (itemPaths != null && !itemPaths.isEmpty()) {
            emitTransportFrom(source, itemPaths);
        }
    }

    private void processFluidSourceTick(
            final TransportTick<ITransportSource, Path> tickData,
            final ITransportSource<FluidStack, IFluidHandler> source
    ) {
        if (tickData.getEnergyFluidPaths() == null) {
            final Tuple<List<Path>, LinkedList<ITransportConductor>> tuple = discover(source);
            final List<Path> discoveredPaths = tuple.getA();
            final ArrayList<Path> validPaths = new ArrayList<>(discoveredPaths.size());

            for (int i = 0, size = discoveredPaths.size(); i < size; i++) {
                final Path path = discoveredPaths.get(i);

                if (!isValidFluidPath(tickData, path)) {
                    continue;
                }

                path.target.getEnergyTickList().add(tickData.getSource().hashCode());
                validPaths.add(path);
            }

            tickData.setFluidList(validPaths);
            tickData.setConductors(tuple.getB());
        }

        final List<Path> fluidPaths = tickData.getEnergyFluidPaths();
        if (fluidPaths != null && !fluidPaths.isEmpty()) {
            emitTransportFluidFrom(source, fluidPaths);
        }
    }

    private boolean isValidItemPath(final TransportTick<ITransportSource, Path> tickData, final Path transportPath) {
        if (transportPath == null
                || transportPath.end == null
                || transportPath.first == null
                || transportPath.first == transportPath.end
                || transportPath.firstSide == null) {
            return false;
        }

        if (!transportPath.target.isSink()) {
            return false;
        }

        if (!transportPath.first.isOutput() || transportPath.end.isOutput()) {
            return false;
        }

        if (!transportPath.first.isInput() && !transportPath.first.isOutput()) {
            return false;
        }

        if (!transportPath.first.isItem() || !transportPath.end.isItem()) {
            return false;
        }

        if (!transportPath.end.isInput() && !transportPath.end.isOutput()) {
            return false;
        }

        if (!(tickData.getSource().getHandler(transportPath.firstSide) instanceof IItemHandler)) {
            return false;
        }

        return transportPath.getHandler() != null;
    }

    private boolean isValidFluidPath(final TransportTick<ITransportSource, Path> tickData, final Path transportPath) {
        if (transportPath == null
                || transportPath.end == null
                || transportPath.first == null
                || transportPath.first == transportPath.end
                || transportPath.firstSide == null) {
            return false;
        }

        if (!transportPath.first.isOutput() || transportPath.end.isOutput()) {
            return false;
        }

        if (!transportPath.first.isInput() && !transportPath.first.isOutput()) {
            return false;
        }

        if (!transportPath.end.isInput() && !transportPath.end.isOutput()) {
            return false;
        }

        if (!transportPath.target.isFluidSink()) {
            return false;
        }

        if (transportPath.first.isItem() || transportPath.end.isItem()) {
            return false;
        }

        if (!(tickData.getSource().getHandler(transportPath.firstSide) instanceof IFluidHandler)) {
            return false;
        }

        return transportPath.getFluidHandler() != null;
    }

    public void emitTransportFluidFrom(
            ITransportSource<FluidStack, IFluidHandler> transportSource,
            List<Path> transportPaths
    ) {
        if (transportPaths == null || transportPaths.isEmpty()) {
            return;
        }

        final EnumMap<Direction, TransportItem<FluidStack>> offeredCache = new EnumMap<>(Direction.class);

        for (int p = 0, pathSize = transportPaths.size(); p < pathSize; p++) {
            final Path transportPath = transportPaths.get(p);

            if (transportPath == null || transportPath.first == null || transportPath.end == null || transportPath.firstSide == null) {
                continue;
            }

            final IFluidHandler handler = transportPath.getFluidHandler();
            if (handler == null) {
                continue;
            }

            if (transportPath.end.getMax(tick) == 0 || transportPath.first.getMax(tick) == 0) {
                continue;
            }

            if (!transportPath.first.canWork() || !transportPath.end.canWork()) {
                continue;
            }

            TransportItem<FluidStack> transportItem = offeredCache.get(transportPath.firstSide);
            if (transportItem == null) {
                @SuppressWarnings("unchecked") final TransportItem<FluidStack> offered =
                        (TransportItem<FluidStack>) transportSource.getOffered(1, transportPath.firstSide);
                transportItem = offered;
                offeredCache.put(transportPath.firstSide, transportItem);
            }

            if (transportItem == null) {
                continue;
            }

            final List<FluidStack> list = transportItem.getList();
            if (list == null || list.isEmpty()) {
                continue;
            }

            final Direction sourceCheckFacing = transportPath.firstSide.getOpposite();
            final Direction targetCheckFacing = transportPath.targetDirection.getOpposite();

            for (int i = 0, size = list.size(); i < size; i++) {
                if (transportPath.end.getMax(tick) == 0 || transportPath.first.getMax(tick) == 0) {
                    break;
                }

                final FluidStack fluidStack = list.get(i);
                if (fluidStack.isEmpty() || fluidStack.getAmount() <= 0) {
                    continue;
                }

                if (!canInsertOrExtract(transportPath.first, fluidStack, sourceCheckFacing)) {
                    continue;
                }

                if (!canInsertOrExtract(transportPath.end, fluidStack, targetCheckFacing)) {
                    continue;
                }

                int amount = handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE);
                amount = Math.min(amount, Math.min(transportPath.first.getMax(tick), transportPath.end.getMax(tick)));

                if (amount <= 0) {
                    continue;
                }

                final FluidStack toFill = fluidStack.copy();
                toFill.setAmount(amount);

                final int filled = handler.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
                if (filled <= 0) {
                    continue;
                }

                transportPath.first.setMax(filled);
                transportPath.end.setMax(filled);

                final FluidStack toDraw = fluidStack.copy();
                toDraw.setAmount(filled);
                transportSource.draw(toDraw, filled, transportPath.firstSide);

                fluidStack.shrink(filled);
            }
        }
    }

    public ITransportTile getTileEntity(BlockPos pos) {
        return this.chunkCoordinatesITransportTileMap.get(pos);
    }

    public void onTileEntityRemoved(final ITransportAcceptor par1) {
        this.onTileEntityAdded(par1);
    }

    public void onUnload() {
        this.senderPath.clear();
        this.chunkCoordinatesITransportTileMap.clear();
        this.sourceToUpdateList.clear();
        this.delete.clear();
        clearTransientCaches();
    }

    private static final class ItemFilterLists {
        private final List<ItemStack> blackList;
        private final List<ItemStack> whiteList;

        private ItemFilterLists(List<ItemStack> blackList, List<ItemStack> whiteList) {
            this.blackList = blackList;
            this.whiteList = whiteList;
        }
    }

    private static final class FluidFilterLists {
        private final List<FluidStack> blackList;
        private final List<FluidStack> whiteList;

        private FluidFilterLists(List<FluidStack> blackList, List<FluidStack> whiteList) {
            this.blackList = blackList;
            this.whiteList = whiteList;
        }
    }

    private static final class ConductorSideKey {
        private final ITransportConductor conductor;
        private final Direction side;
        private final int hash;

        private ConductorSideKey(ITransportConductor conductor, Direction side) {
            this.conductor = conductor;
            this.side = side;
            this.hash = 31 * System.identityHashCode(conductor) + side.ordinal();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ConductorSideKey other)) {
                return false;
            }
            return this.conductor == other.conductor && this.side == other.side;
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    private static final class SinkDemandKey {
        private final ITransportSink sink;
        private final Direction side;
        private final int hash;

        private SinkDemandKey(ITransportSink sink, Direction side) {
            this.sink = sink;
            this.side = side;
            this.hash = 31 * System.identityHashCode(sink) + side.ordinal();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof SinkDemandKey other)) {
                return false;
            }
            return this.sink == other.sink && this.side == other.side;
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }
}