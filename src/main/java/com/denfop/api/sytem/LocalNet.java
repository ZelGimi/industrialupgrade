package com.denfop.api.sytem;

import com.denfop.api.energy.SystemTick;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocalNet implements ILocalNet {

    private final PathMap SourceToPathMap;
    private final Map<BlockPos, ITile> chunkCoordinatesTileMap;
    private final WaitingList waitingList;
    private final EnergyType energyType;
    private final EnumFacing[] directions = EnumFacing.values();
    private int tick;

    public LocalNet(EnergyType energyType) {
        this.SourceToPathMap = new PathMap();
        this.waitingList = new WaitingList();
        this.energyType = energyType;
        this.chunkCoordinatesTileMap = new HashMap<>();
        this.tick = 0;
    }

    public double emitEnergyFrom(final ISource energySource, double amount, final SystemTick<ISource, Path> tick) {
        List<Path> energyPaths = tick.getList();
        if (energyPaths == null) {
            energyPaths = this.discover(energySource);
            tick.setList(energyPaths);
        }
        if (!(energySource instanceof IDual) && energySource.isSource()) {
            energySource.setPastEnergy(energySource.getPerEnergy());
        } else if (energySource instanceof IDual && (energySource.isSource())) {
            ((IDual) energySource).setPastEnergy1(((IDual) energySource).getPerEnergy1());

        }
        if (amount > 0) {
            for (final Path energyPath : energyPaths) {
                if (amount <= 0) {
                    break;
                }
                final ISink energySink = energyPath.target;
                double demandedEnergy = energySink.getDemanded();
                if (demandedEnergy <= 0.0) {
                    continue;
                }
                double energyProvided = Math.min(demandedEnergy, amount);

                energySink.inject(energyPath.targetDirection, energyProvided, 0);
                if (!(energySource instanceof IDual) && energySource.isSource()) {
                    energySource.addPerEnergy(energyProvided);
                } else if ((energySource instanceof IDual) && energySource.isSource()) {
                    ((IDual) energySource).addPerEnergy1(energyProvided);
                }

                energyPath.tick(this.tick, energyProvided);
                if (this.energyType.isDraw()) {
                    amount -= energyProvided;
                    amount = Math.max(0, amount);
                }
                if (this.energyType.isBreak_conductors()) {
                    if (energyProvided > energyPath.min) {
                        for (IConductor HeatConductor : energyPath.conductors) {
                            if (HeatConductor.getConductorBreakdownEnergy(this.energyType) < energyProvided) {
                                HeatConductor.removeConductor();
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }

        return amount;
    }

    @Override
    public void TickEnd() {
        if (this.waitingList.hasWork()) {
            final List<ITile> tiles = this.waitingList.getPathTiles();

            for (final ITile tile : tiles) {
                final List<ISource> sources = this.discoverFirstPathOrSources(tile);
                if (sources.size() > 0) {
                    this.SourceToPathMap.removeAllSource1(sources);
                }
            }
            this.waitingList.clear();

        }

        for (SystemTick<ISource, Path> tick : this.SourceToPathMap.senderPath) {
            final ISource entry = tick.getSource();
            if (tick.getList() != null) {
                if (tick.getList().isEmpty()) {
                    continue;
                }
            }

            if (entry != null) {
                if (entry.isSource()) {
                    if (entry instanceof IDual) {
                        ((IDual) entry).setPastEnergy1(((IDual) entry).getPastEnergy1());
                    } else {
                        entry.setPastEnergy(entry.getPerEnergy());
                    }
                }
                double offer = entry.getOffered();
                if (offer > 0) {

                    final double removed = offer - this.emitEnergyFrom(entry, offer, tick);
                    if (this.energyType.isDraw()) {
                        entry.draw(removed);
                    }


                } else {
                    if (entry.isSource()) {
                        if (entry instanceof IDual) {
                            ((IDual) entry).setPastEnergy1(((IDual) entry).getPastEnergy1());
                        } else {
                            entry.setPastEnergy(entry.getPerEnergy());
                        }
                    }

                }
            }
        }


        this.tick++;
    }

    public void emitHeatFromNotAllowed(
            final ISourceTemperature HeatSource,
            double amount,
            final SystemTick<ISource, Path> tick
    ) {
        List<Path> HeatPaths = tick.getList();

        if (HeatPaths == null) {
            HeatPaths = this.discover(HeatSource);
            tick.setList(HeatPaths);
        }

        for (final Path HeatPath : HeatPaths) {
            final ISinkTemperature HeatSink = (ISinkTemperature) HeatPath.target;
            double demandedHeat = HeatSink.getDemanded();
            if (HeatSink.needTemperature()) {
                HeatSource.setAllowed(true);
            }
            if (demandedHeat <= 0.0) {
                continue;
            }

            double adding;


            adding = Math.min(amount, demandedHeat);
            if (adding <= 0.0D) {
                continue;
            }


            adding -= HeatSink.inject(HeatPath.targetDirection, adding, 0);
            HeatPath.totalConducted = (long) adding;

            if (this.energyType.isBreak_conductors()) {
                if (adding > HeatPath.min) {
                    for (IConductor HeatConductor : HeatPath.conductors) {
                        if (HeatConductor.getConductorBreakdownEnergy(this.energyType) < adding) {
                            HeatConductor.removeConductor();
                        } else {
                            break;
                        }
                    }
                }
            }


        }


    }

    public void emitHeatFrom(
            final ISourceTemperature HeatSource, double amount,
            final SystemTick<ISource, Path> tick
    ) {
        List<Path> HeatPaths = tick.getList();

        if (HeatPaths == null) {
            HeatPaths = this.discover(HeatSource);
            tick.setList(HeatPaths);
        }
        boolean allow = false;
        if (amount > 0) {
            for (final Path HeatPath : HeatPaths) {
                final ISinkTemperature HeatSink = (ISinkTemperature) HeatPath.target;
                double demandedHeat = HeatSink.getDemanded();
                if (demandedHeat <= 0.0) {
                    continue;
                }
                double adding;


                adding = Math.min(amount, demandedHeat);
                if (adding <= 0.0D) {
                    continue;
                }
                allow = allow || HeatSink.needTemperature();

                adding -= HeatSink.inject(HeatPath.targetDirection, adding, 0);
                HeatPath.totalConducted = (long) adding;

                if (this.energyType.isBreak_conductors()) {
                    if (adding > HeatPath.min) {
                        for (IConductor HeatConductor : HeatPath.conductors) {
                            if (HeatConductor.getConductorBreakdownEnergy(this.energyType) < adding) {
                                HeatConductor.removeConductor();
                            } else {
                                break;
                            }
                        }
                    }
                }


            }
        }

        if (!allow) {
            HeatSource.setAllowed(false);
        }
    }

    public double getTotalEmitted(final ITile tileEntity) {
        double ret = 0.0;
        int col = 0;
        if (tileEntity instanceof IConductor) {
            for (final Path energyPath : this.SourceToPathMap.getPaths((IAcceptor) tileEntity)) {
                if (energyPath.conductors.contains(
                        tileEntity)) {
                    ret += this.getTotalAccepted(energyPath.target);
                    col++;
                }
            }

        }
        if (tileEntity instanceof ISource) {
            ISource advEnergySource = (ISource) tileEntity;
            if (!(advEnergySource instanceof IDual) && advEnergySource.isSource()) {
                ret = Math.max(0, advEnergySource.getPerEnergy() - advEnergySource.getPastEnergy());

            } else if ((advEnergySource instanceof IDual) && advEnergySource.isSource()) {
                IDual dual = (IDual) advEnergySource;

                ret = Math.max(0, dual.getPerEnergy1() - dual.getPastEnergy1());

            }
        }
        return col == 0 ? ret : ret / col;
    }

    public List<ISource> discoverFirstPathOrSources(final ITile par1) {
        final Set<ITile> reached = new HashSet<>();
        final List<ISource> result = new ArrayList<>();
        final List<ITile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final ITile tile = workList.remove(0);
            final TileEntity te = tile.getTile();
            if (te == null) {
                continue;
            }
            if (!te.isInvalid()) {
                final List<Target> targets = this.getValidReceivers(tile, true);
                for (Target QETarget : targets) {
                    final ITile target = QETarget.tileEntity;
                    if (target != par1) {
                        if (!reached.contains(target)) {
                            reached.add(target);
                            if (target instanceof ISource) {
                                result.add((ISource) target);
                            } else if (target instanceof IConductor) {
                                workList.add(target);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public double getTotalAccepted(final ITile tileEntity) {
        double ret = 0.0;
        if (tileEntity instanceof ISink) {
            ISink advEnergySink = (ISink) tileEntity;
            if (advEnergySink.isSink()) {
                if (this.tick - 1 == advEnergySink.getTick()
                        || this.tick == advEnergySink.getTick()
                        || this.tick + 1 == advEnergySink.getTick()
                ) {
                    ret = Math.max(0, advEnergySink.getPerEnergy() - advEnergySink.getPastEnergy());
                }

            }
        }
        return ret;
    }

    public void update(BlockPos pos1) {
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos = pos1.offset(dir);
            ITile tile = this.chunkCoordinatesTileMap.get(pos);
            if (tile instanceof IConductor) {
                ((IConductor) tile).update_render();
            }
        }
    }

    public NodeStats getNodeStats(final ITile tile) {
        final double emitted = this.getTotalEmitted(tile);
        final double received = this.getTotalAccepted(tile);
        return new NodeStats(received, emitted);
    }

    public void onUnload() {
        this.SourceToPathMap.clear();
        this.waitingList.clear();
        this.chunkCoordinatesTileMap.clear();
    }

    @Override
    public ITile getTileEntity(final BlockPos pos) {
        return this.chunkCoordinatesTileMap.get(pos);
    }

    @Override
    public void addTile(final ITile tile1) {
        this.addTileEntity(tile1.getBlockPos(), tile1);
    }

    public ITile getNeighbor(final ITile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }

        return this.getTileEntity(tile.getBlockPos().offset(dir));
    }

    public void removeTile(ITile tile1) {

        this.removeTileEntity(tile1);

    }

    public void removeTileEntity(ITile tile) {
        if (!this.chunkCoordinatesTileMap.containsKey(tile.getBlockPos())) {
            return;
        }
        final BlockPos coord = tile.getBlockPos();
        this.chunkCoordinatesTileMap.remove(coord, tile);
        this.update(coord);
        if (tile instanceof IAcceptor) {
            this.SourceToPathMap.removeAll(this.SourceToPathMap.getSources((IAcceptor) tile));
            this.waitingList.onTileEntityRemoved(tile);
        }
        if (tile instanceof ISource) {
            this.SourceToPathMap.remove((ISource) tile);

        }
    }

    public List<Path> discover(final ISource emitter) {
        final Map<IConductor, EnumFacing> reachedTileEntities = new HashMap<>();
        final List<ITile> tileEntitiesToCheck = new ArrayList<>();
        final List<Path> Paths = new ArrayList<>();

        tileEntitiesToCheck.add(emitter);

        while (!tileEntitiesToCheck.isEmpty()) {
            final ITile currentTileEntity = tileEntitiesToCheck.remove(0);
            final List<Target> validReceivers = this.getValidReceivers(currentTileEntity, false);
            for (final Target validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {
                    if (validReceiver.tileEntity instanceof ISink) {
                        Paths.add(new Path((ISink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }
                    if (reachedTileEntities.containsKey((IConductor) validReceiver.tileEntity)) {
                        continue;
                    }

                    reachedTileEntities.put((IConductor) validReceiver.tileEntity, validReceiver.direction);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }


        }
        for (Path Path : Paths) {
            ITile tileEntity = Path.target;
            EnumFacing QEBlockLink = Path.targetDirection;
            BlockPos te = Path.target.getBlockPos();
            if (emitter != null) {
                while (tileEntity != emitter) {
                    if (QEBlockLink != null && te != null) {
                        tileEntity = this.getTileEntity(te.offset(QEBlockLink));
                        te = te.offset(QEBlockLink);
                    }
                    if (!(tileEntity instanceof IConductor)) {
                        break;
                    }
                    final IConductor Conductor = (IConductor) tileEntity;
                    Path.conductors.add(Conductor);
                    if (Conductor.getConductorBreakdownEnergy(this.energyType) - 1 < Path.getMin()) {
                        Path.setMin(Conductor.getConductorBreakdownEnergy(this.energyType) - 1);
                    }
                    QEBlockLink = reachedTileEntities.get(tileEntity);
                    if (QEBlockLink != null) {
                        continue;
                    }
                    assert te != null;
                    throw new NullPointerException("BlockPos is null");
                }
            }
        }
        return Paths;
    }

    private List<Target> getValidReceivers(final ITile emitter, final boolean reverse) {
        final List<Target> validReceivers = new LinkedList<>();

        for (final EnumFacing direction : directions) {
            final ITile target2 = getNeighbor(emitter, direction);
            if (target2 != null) {
                final EnumFacing inverseDirection2 = direction.getOpposite();
                if (reverse) {
                    if (emitter instanceof IAcceptor && target2 instanceof IEmitter) {
                        final IEmitter sender2 = (IEmitter) target2;
                        final IAcceptor receiver2 = (IAcceptor) emitter;
                        if (sender2.emitsTo(receiver2, inverseDirection2) && receiver2.acceptsFrom(
                                sender2,
                                direction
                        )) {
                            validReceivers.add(new Target(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof IEmitter && target2 instanceof IAcceptor) {
                    final IEmitter sender2 = (IEmitter) emitter;
                    final IAcceptor receiver2 = (IAcceptor) target2;
                    if (sender2.emitsTo(receiver2, direction) && receiver2.acceptsFrom(
                            sender2,
                            inverseDirection2
                    )) {
                        validReceivers.add(new Target(target2, inverseDirection2));
                    }
                }
            }
        }


        return validReceivers;
    }

    public void addTileEntity(final BlockPos coords, final ITile tile) {
        if (this.chunkCoordinatesTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesTileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof IAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), tile);
        }
        if (tile instanceof ISource) {
            SourceToPathMap.senderPath.add(new SystemTick<>((ISource) tile, null));

        }
    }

    class Path {

        final Set<IConductor> conductors;
        final ISink target;
        final EnumFacing targetDirection;
        long totalConducted = 0;
        double min = Double.MAX_VALUE;

        Path(ISink sink, EnumFacing facing) {
            this.target = sink;
            this.conductors = new HashSet<>();
            this.targetDirection = facing;
        }

        public void tick(int tick, double adding) {
            if (this.target.isSink()) {
                if (this.target.getTick() != tick) {
                    this.target.addTick(tick);
                    this.target.setPastEnergy(this.target.getPerEnergy());

                }
                this.target.addPerEnergy(adding);
            }
        }

        public double getMin() {
            return min;
        }

        public void setMin(final double min) {
            this.min = min;
        }

        public Set<IConductor> getConductors() {
            return conductors;
        }

    }

    class Target {

        final ITile tileEntity;
        final EnumFacing direction;

        Target(final ITile tileEntity, final EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }

    }

    class WaitingList {

        final List<PathLogic> paths;

        WaitingList() {
            this.paths = new ArrayList<>();
        }

        public void onTileEntityAdded(final List<Target> around, final ITile tile) {
            if (around.isEmpty() || this.paths.isEmpty()) {
                this.createNewPath(tile);
                return;
            }
            boolean found = false;
            final List<PathLogic> logics = new ArrayList<>();
            for (final PathLogic logic : this.paths) {
                if (logic.contains(tile)) {
                    found = true;
                    if (tile instanceof IConductor) {
                        logics.add(logic);
                    }
                } else {
                    for (final Target target : around) {
                        if (logic.contains(target.tileEntity)) {
                            found = true;
                            logic.add(tile);
                            if (target.tileEntity instanceof IConductor) {
                                logics.add(logic);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            if (logics.size() > 1 && tile instanceof IConductor) {
                final PathLogic newLogic = new PathLogic();
                for (final PathLogic logic2 : logics) {
                    this.paths.remove(logic2);
                    for (final ITile toMove : logic2.tiles) {
                        if (!newLogic.contains(toMove)) {
                            newLogic.add(toMove);
                        }
                    }
                    logic2.clear();
                }
                this.paths.add(newLogic);
            }
            if (!found) {
                this.createNewPath(tile);
            }
        }

        public void onTileEntityRemoved(final ITile par1) {
            if (this.paths.isEmpty()) {
                return;
            }
            final List<ITile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); ++i) {
                final PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (final ITile tile : toRecalculate) {
                this.onTileEntityAdded(LocalNet.this.getValidReceivers(tile, true), tile);
            }
        }

        public void createNewPath(final ITile par1) {
            final PathLogic logic = new PathLogic();
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
            return this.paths.size() > 0;
        }

        public List<ITile> getPathTiles() {
            final List<ITile> tiles = new ArrayList<>();
            for (PathLogic path : this.paths) {
                final ITile tile = path.getRepresentingTile();
                if (tile != null) {
                    tiles.add(tile);
                }
            }
            return tiles;
        }

    }

    class PathLogic {

        final List<ITile> tiles;

        PathLogic() {
            this.tiles = new ArrayList<>();
        }

        public boolean contains(final ITile par1) {
            return this.tiles.contains(par1);
        }

        public void add(final ITile par1) {
            this.tiles.add(par1);
        }

        public void remove(final ITile par1) {
            this.tiles.remove(par1);
        }

        public void clear() {
            this.tiles.clear();
        }

        public ITile getRepresentingTile() {
            if (this.tiles.isEmpty()) {
                return null;
            }
            return this.tiles.get(0);
        }

    }

    class PathMap {

        final List<SystemTick<ISource, Path>> senderPath;

        PathMap() {
            this.senderPath = new ArrayList<>();
        }

        public void put(final ISource par1, final List<Path> par2) {
            this.senderPath.add(new SystemTick<>(par1, par2));
        }


        public boolean containsKey(final SystemTick<ISource, Path> par1) {
            return this.senderPath.contains(par1);
        }

        public boolean containsKey(final ISource par1) {
            return this.senderPath.contains(new SystemTick<ISource, Path>(par1, null));
        }


        public void remove1(final ISource par1) {

            for (SystemTick<ISource, Path> ticks : this.senderPath) {
                if (ticks.getSource() == par1) {
                    ticks.setList(null);
                    break;
                }
            }
        }

        public void remove(final ISource par1) {
            this.senderPath.remove(new SystemTick<ISource, Path>(par1, null));
        }

        public void remove(final SystemTick<ISource, Path> par1) {
            this.senderPath.remove(par1);
        }

        public void removeAll(final List<SystemTick<ISource, Path>> par1) {
            if (par1 == null) {
                return;
            }
            for (SystemTick<ISource, Path> iSESource : par1) {
                iSESource.setList(null);
            }
        }

        public void removeAllSource1(final List<ISource> par1) {
            if (par1 == null) {
                return;
            }
            for (ISource iSESource : par1) {
                this.remove1(iSESource);
            }
        }

        public List<Path> getPaths(final IAcceptor par1) {
            final List<Path> paths = new ArrayList<>();

            List<SystemTick<ISource, Path>> sources_list = this.getSources(par1);
            if (sources_list == null || sources_list.isEmpty()) {
                return paths;
            }
            for (final SystemTick<ISource, Path> source : sources_list) {
                if (this.containsKey(source)) {
                    paths.addAll(source.getList());
                }
            }
            return paths;
        }

        public List<SystemTick<ISource, Path>> getSources(final IAcceptor par1) {
            final List<SystemTick<ISource, Path>> source = new ArrayList<>();
            for (final SystemTick<ISource, Path> entry : this.senderPath) {
                if (source.contains(entry)) {
                    continue;
                }
                if (entry.getList() != null) {
                    for (Path path : entry.getList()) {
                        if ((!(par1 instanceof IConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof ISink) || path.target != par1)) {
                            continue;
                        }
                        source.add(entry);
                    }
                }
            }
            return source;
        }


        public void clear() {
            for (SystemTick<ISource, Path> entry : this.senderPath) {
                List<Path> list = entry.getList();
                if (list != null) {
                    for (Path SEPath : list) {
                        SEPath.conductors.clear();
                    }
                }

            }
            this.senderPath.clear();
        }


        public SystemTick<ISource, Path> get(ISource tileEntity) {
            for (SystemTick<ISource, Path> entry : this.senderPath) {
                if (entry.getSource() == tileEntity) {
                    return entry;
                }
            }
            return null;
        }

    }

}
