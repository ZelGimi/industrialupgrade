package com.denfop.api.energy;

import com.denfop.api.transport.ITransportTile;
import com.denfop.api.transport.TransportNetGlobal;
import com.denfop.api.transport.event.TransportTileUnLoadEvent;
import ic2.api.info.ILocatable;
import ic2.core.ExplosionIC2;
import ic2.core.IC2;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.IItemHandler;

import java.util.*;

public class EnergyNetLocal {


    public static EnergyNetLocal EMPTY = new EnergyNetLocal();

    private final World world;
    private final EnergyPathMap energySourceToEnergyPathMap;
    private final List<IEnergyController> controllerList;
    private final Map<BlockPos, IAdvEnergyTile> chunkCoordinatesIAdvEnergyTileMap;

    private final Map<ChunkPos, List< IAdvEnergyTile>> chunkPosIAdvEnergyTileMap;

    private final WaitingList waitingList;
    private final List<IAdvEnergySource> energySourceList = new ArrayList<>();
    private final SunCoef suncoef;
    private final boolean hasrestrictions;
    private final boolean explosing;
    private final boolean ignoring;
    private final boolean losing;
    private int tick;

    EnergyNetLocal(final World world) {
        this.energySourceToEnergyPathMap = new EnergyPathMap();
        this.waitingList = new WaitingList();
        this.world = world;
        this.controllerList = new ArrayList<>();
        this.chunkCoordinatesIAdvEnergyTileMap = new HashMap<>();
        this.chunkPosIAdvEnergyTileMap = new HashMap<>();
        this.tick = 0;
        this.suncoef = new SunCoef(world);
        this.losing = EnergyNetGlobal.instance.getLosing();
        this.ignoring = EnergyNetGlobal.instance.needIgnoringTiers();
        this.explosing = EnergyNetGlobal.instance.needExplosion();
        this.hasrestrictions = EnergyNetGlobal.instance.hasRestrictions();
    }

    EnergyNetLocal() {
        this.energySourceToEnergyPathMap = new EnergyPathMap();
        this.waitingList = new WaitingList();
        this.world = null;
        this.controllerList = new ArrayList<>();
        this.chunkCoordinatesIAdvEnergyTileMap = new HashMap<>();
        this.tick = 0;
        this.suncoef = null;
        this.losing = EnergyNetGlobal.instance.getLosing();
        this.ignoring = EnergyNetGlobal.instance.needIgnoringTiers();
        this.explosing = EnergyNetGlobal.instance.needExplosion();
        this.hasrestrictions = EnergyNetGlobal.instance.hasRestrictions();
        this.chunkPosIAdvEnergyTileMap = new HashMap<>();
    }

    public Map<ChunkPos, List< IAdvEnergyTile>> getChunkPosIAdvEnergyTileMap() {
        return chunkPosIAdvEnergyTileMap;
    }

    public void explodeTiles(IAdvEnergySink sink) {

        if (!(sink instanceof IAdvEnergySource)) {
            explodeMachineAt(getTileFromIEnergy(sink));
            removeTile(sink);
        } else {
            energySourceList.add((IAdvEnergySource) sink);
        }

    }

    void explodeMachineAt(TileEntity entity) {
        if (this.explosing) {
            final BlockPos pos = entity.getPos();
            final ITransportTile IAdvEnergyTile = TransportNetGlobal.instance.getSubTile(world, pos);
            if (IAdvEnergyTile != null) {
                if (IAdvEnergyTile.getHandler() instanceof IItemHandler) {
                    MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                            world,
                            IAdvEnergyTile
                    ));
                }
            }
            this.world.setBlockToAir(pos);
            float power = 1.0F;
            ExplosionIC2 explosion = new ExplosionIC2(
                    this.world,
                    null,
                    0.5D + pos.getX(),
                    0.5D + pos.getY(),
                    0.5D + pos.getZ(),
                    power,
                    0.75F
            );
            explosion.doExplosion();
        }
    }

    public SunCoef getSuncoef() {
        return suncoef;
    }

    public void addController(IEnergyController tile1) {
        this.controllerList.add(tile1);
    }

    public void removeController(IEnergyController tile1) {
        this.controllerList.remove(tile1);
        tile1.unload();
    }

    public void addTile(IAdvEnergyTile tile1) {

        try {
            this.addTileEntity(tile1.getBlockPos(), tile1);
        } catch (Exception ignored) {

        }


    }

    public void addTile(IAdvEnergyTile tile, TileEntity tileentity) {

        try {
            this.addTileEntity(tileentity.getPos(), tile);
        } catch (Exception ignored) {

        }


    }

    public BlockPos getPos(final IAdvEnergyTile tile) {
        return tile.getBlockPos();
    }

    public void addTileEntity(final BlockPos coords, final IAdvEnergyTile tile) {
        if (this.chunkCoordinatesIAdvEnergyTileMap.containsKey(coords)) {
            return;
        }

        this.chunkCoordinatesIAdvEnergyTileMap.put(coords, tile);

        this.update(coords);
        if (tile instanceof IEnergyAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), (IEnergyAcceptor) tile);
        }
        if (tile instanceof IAdvEnergySource) {
            energySourceToEnergyPathMap.senderPath.add(new EnergyTick((IAdvEnergySource) tile, null));

        }

    }

    public void addTileEntity(final IAdvEnergyTile tile, final IAdvEnergyTile tile1) {
        if (this.chunkCoordinatesIAdvEnergyTileMap.containsKey(tile1.getBlockPos())) {
            return;
        }
        this.chunkCoordinatesIAdvEnergyTileMap.put(tile1.getBlockPos(), tile);

        this.update(tile1.getBlockPos());
        if (tile instanceof IEnergyAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), (IEnergyAcceptor) tile);
        }
        if (tile instanceof IAdvEnergySource) {
            final EnergyTick source = this.energySourceToEnergyPathMap.get((IAdvEnergySource) tile);
            if (source != null) {
                source.setList(null);
            }
        }
    }

    public void removeTile(IAdvEnergyTile tile1) {
        if (tile1 != EnergyNetGlobal.EMPTY) {
            this.removeTileEntity(tile1);
        }

    }


    public void removeTileEntity(IAdvEnergyTile tile1, IAdvEnergyTile tile) {

        if (!this.chunkCoordinatesIAdvEnergyTileMap.containsKey(tile1.getBlockPos())) {
            return;
        }

        this.chunkCoordinatesIAdvEnergyTileMap.remove(tile1.getBlockPos(), tile);
        this.update(tile1.getBlockPos());
        if (tile instanceof IEnergyAcceptor) {
            this.energySourceToEnergyPathMap.removeAll(this.energySourceToEnergyPathMap.getSources((IEnergyAcceptor) tile));
            this.waitingList.onTileEntityRemoved((IEnergyAcceptor) tile);
        }
        if (tile instanceof IAdvEnergySource) {

            this.energySourceToEnergyPathMap.remove((IAdvEnergySource) tile);
        }
    }

    public void removeTileEntity(IAdvEnergyTile tile) {
        if (tile.getBlockPos() != null) {
            if (!this.chunkCoordinatesIAdvEnergyTileMap.containsKey(tile.getBlockPos())) {
                return;
            }
        } else {
            if (!this.chunkCoordinatesIAdvEnergyTileMap.containsKey(tile.getTileEntity().getPos())) {
                return;
            }
        }
        BlockPos coord;
        coord = tile.getBlockPos();

        this.chunkCoordinatesIAdvEnergyTileMap.remove(coord);
        this.update(coord);
        if (tile instanceof IEnergyAcceptor) {
            this.energySourceToEnergyPathMap.removeAll(this.energySourceToEnergyPathMap.getSources((IEnergyAcceptor) tile));
            this.waitingList.onTileEntityRemoved((IEnergyAcceptor) tile);
        }
        if (tile instanceof IAdvEnergySource) {
            this.energySourceToEnergyPathMap.remove((IAdvEnergySource) tile);
        }
    }

    public TileEntity getTileFromMap(IAdvEnergyTile tile) {
        return tile.getTileEntity();
    }

    public double emitEnergyFrom(final IAdvEnergySource energySource, double amount, final EnergyTick tick) {
        List<EnergyPath> energyPaths = tick.getList();
        if (energyPaths == null) {
            energyPaths = this.discover(energySource);
            for (EnergyPath path : energyPaths) {

                boolean isSorted = false;
                IAdvConductor buf;
                while (!isSorted) {
                    isSorted = true;
                    for (int i = 0; i < path.getConductors().size() - 1; i++) {
                        if (path.getConductors().get(i).getConductorBreakdownEnergy() > path
                                .getConductors()
                                .get(i + 1)
                                .getConductorBreakdownEnergy()) {
                            isSorted = false;
                            buf = path.getConductors().get(i);
                            path.getConductors().set(i, path.getConductors().get(i + 1));
                            path.getConductors().set(i + 1, buf);
                        }
                    }
                }
            }

            tick.setList(energyPaths);

            tick.rework();
            if (!this.controllerList.isEmpty()) {
                this.controllerList.forEach(IEnergyController::work);
            }
        }


        if (amount > 0) {
            for (final EnergyPath energyPath : energyPaths) {
                if (amount <= 0) {
                    break;
                }
                final IAdvEnergySink energySink = energyPath.target;
                double demandedEnergy = energySink.getDemandedEnergy();
                if (demandedEnergy <= 0.0) {
                    continue;
                }
                double energyProvided = amount;

                double adding;
                if (this.hasrestrictions && !this.explosing) {
                    adding = Math.min(energyProvided, Math.min(demandedEnergy, energyPath.min) + energyPath.loss);
                } else if (this.hasrestrictions) {
                    adding = Math.min(energyProvided, demandedEnergy + energyPath.loss);
                } else {
                    adding = Math.min(energyProvided, demandedEnergy + energyPath.loss);
                }
                adding -= energyPath.loss;
                if (energyPath.isLimit) {
                    adding = Math.min(adding, energyPath.limit_amount);
                }
                if (adding <= 0.0D) {
                    continue;
                }

                if (this.ignoring) {

                    int tier = energySink.getSinkTier();
                    int tier1 = EnergyNetGlobal.instance.getTierFromPower(adding);
                    if (tier1 > tier) {
                        if (energyPath.hasController) {
                            continue;
                        }
                        explodeTiles(energySink);
                        continue;
                    }
                }
                adding -= energySink.injectEnergy(energyPath.targetDirection, adding, 0);
                energyPath.totalEnergyConducted = (long) adding;
                energyPath.tick(this.tick, adding);
                amount -= adding;
                amount -= energyPath.loss;
                amount = Math.max(0, amount);
                if (this.hasrestrictions && this.explosing) {
                    if (adding > energyPath.min) {
                        for (IAdvConductor energyConductor : energyPath.conductors) {
                            if (energyConductor.getConductorBreakdownEnergy() < adding) {
                                energyConductor.removeConductor();
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

    public double getTotalEnergyEmitted(final IAdvEnergyTile tileEntity) {
        double ret = 0.0;
        int col = 0;
        if (tileEntity instanceof IAdvConductor) {
            for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((IEnergyAcceptor) tileEntity)) {
                if (energyPath.conductors.contains(
                        tileEntity)) {
                    ret += energyPath.totalEnergyConducted;
                    col++;
                }
            }

        }

        if ((tileEntity instanceof IAdvEnergySource)) {
            IAdvEnergySource advEnergySource = (IAdvEnergySource) tileEntity;
            if (!(advEnergySource instanceof IAdvDual) && advEnergySource.isSource()) {
                ret = Math.max(0, advEnergySource.getPerEnergy() - advEnergySource.getPastEnergy());
            } else if ((advEnergySource instanceof IAdvDual) && advEnergySource.isSource()) {
                IAdvDual dual = (IAdvDual) advEnergySource;
                ret = Math.max(0, dual.getPerEnergy1() - dual.getPastEnergy1());

            }
        }
        if (tileEntity instanceof IAdvConductor) {
            try {
                return ret / col;
            } catch (Exception e) {
                return 0;
            }
        }

        return ret;
    }

    public double getTotalEnergySunken(final IAdvEnergyTile tileEntity) {
        double ret = 0.0;
        int col = 0;
        if (!(tileEntity instanceof IAdvEnergySink)) {
            if (tileEntity instanceof IAdvConductor) {
                for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((IEnergyAcceptor) tileEntity)) {
                    if (energyPath.conductors.contains(
                            tileEntity)) {
                        ret += energyPath.totalEnergyConducted;
                        col++;
                    }
                }
            }
        } else {
            IAdvEnergySink advEnergySink = (IAdvEnergySink) tileEntity;
            if (advEnergySink.isSink()) {
                if (this.tick - 1 == advEnergySink.getTick()
                        || this.tick == advEnergySink.getTick()
                        || this.tick + 1 == advEnergySink.getTick()
                ) {
                    ret = Math.max(0, advEnergySink.getPerEnergy() - advEnergySink.getPastEnergy());
                }

            }

        }
        if (tileEntity instanceof IAdvConductor) {
            try {
                return ret / col;
            } catch (Exception e) {
                return 0;
            }
        }
        return ret;
    }

    public TileEntity getTileFromIEnergy(IAdvEnergyTile tile) {
        if (tile instanceof TileEntity) {
            return (TileEntity) tile;
        }
        if (tile instanceof ILocatable) {
            return this.world.getTileEntity(((ILocatable) tile).getPosition());
        }

        return null;
    }

    public List<EnergyPath> discover(final IAdvEnergySource emitter) {
        final Map<IAdvConductor, EnumFacing> reachedTileEntities = new HashMap<>();
        final List<IAdvEnergyTile> tileEntitiesToCheck = new ArrayList<>();
        final List<EnergyPath> energyPaths = new ArrayList<>();
        tileEntitiesToCheck.add(emitter);
        while (!tileEntitiesToCheck.isEmpty()) {
            final IAdvEnergyTile currentTileEntity = tileEntitiesToCheck.remove(0);
            final List<EnergyTarget> validReceivers = this.getValidReceivers(currentTileEntity, false);

            for (final EnergyTarget validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {
                    if (validReceiver.tileEntity instanceof IAdvEnergySink) {
                        energyPaths.add(new EnergyPath((IAdvEnergySink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }
                    if (reachedTileEntities.containsKey((IAdvConductor) validReceiver.tileEntity)) {
                        continue;
                    }

                    reachedTileEntities.put((IAdvConductor) validReceiver.tileEntity, validReceiver.direction);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }


        }
        for (EnergyPath energyPath : energyPaths) {
            IAdvEnergyTile tileEntity = energyPath.target;
            EnumFacing energyBlockLink = energyPath.targetDirection;
            BlockPos te;
            te = tileEntity.getBlockPos();
            while (tileEntity != emitter) {

                if (energyBlockLink != null && te != null) {
                    tileEntity = this.getTileEntity(te.offset(energyBlockLink));
                    te = te.offset(energyBlockLink);
                }
                if (!(tileEntity instanceof IAdvConductor)) {
                    break;
                }
                final IAdvConductor energyConductor = (IAdvConductor) tileEntity;
                energyPath.conductors.add(energyConductor);
                if (energyConductor.getConductorBreakdownEnergy() - 1 < energyPath.getMin()) {
                    energyPath.setMin(energyConductor.getConductorBreakdownEnergy() - 1);
                }
                if (this.losing) {
                    energyPath.loss += energyConductor.getConductionLoss();
                }
                energyBlockLink = reachedTileEntities.get(tileEntity);
                if (energyBlockLink != null) {
                    continue;
                }
                assert te != null;
                IC2.platform.displayError("An energy network pathfinding entry is corrupted.\nThis could happen due to " +
                        "incorrect Minecraft behavior or a bug.\n\n(Technical information: energyBlockLink, tile " +
                        "entities below)\nE: " + emitter + " (" + te.getX() + "," + te.getY() + "," + te

                        .getZ() + ")\n" + "C: " + tileEntity + " (" + te.getX() + "," + te

                        .getY() + "," + te

                        .getZ() + ")\n" + "R: " + energyPath.target + " (" + energyPath.target
                        .getBlockPos()
                        .getX() + "," + getTileFromMap(energyPath.target).getPos().getY() + "," + getTileFromIEnergy(
                        energyPath.target).getPos().getZ() + ")");
            }

        }
        return energyPaths;
    }

    private List<EnergyTarget> getValidReceivers(final IAdvEnergyTile emitter, final boolean reverse) {
        final List<EnergyTarget> validReceivers = new LinkedList<>();

        final BlockPos tile1;
        tile1 = emitter.getBlockPos();
        if (tile1 != null) {
            for (final EnumFacing direction : EnumFacing.values()) {
                final IAdvEnergyTile target2 = this.getTileEntity(tile1.offset(direction));

                if (target2 == emitter) {
                    continue;
                }
                if (target2 != EnergyNetGlobal.EMPTY) {
                    final EnumFacing inverseDirection2 = direction.getOpposite();
                    if (reverse) {
                        if (emitter instanceof IEnergyAcceptor && target2 instanceof IEnergyEmitter) {
                            final IEnergyEmitter sender2 = (IEnergyEmitter) target2;
                            final IEnergyAcceptor receiver2 = (IEnergyAcceptor) emitter;
                            if (sender2.emitsEnergyTo(receiver2, inverseDirection2) && receiver2.acceptsEnergyFrom(
                                    sender2,
                                    direction
                            )) {
                                validReceivers.add(new EnergyTarget(target2, inverseDirection2));
                            }
                        }
                    } else if (emitter instanceof IEnergyEmitter && target2 instanceof IEnergyAcceptor) {
                        final IEnergyEmitter sender2 = (IEnergyEmitter) emitter;
                        final IEnergyAcceptor receiver2 = (IEnergyAcceptor) target2;
                        if (sender2.emitsEnergyTo(receiver2, direction) && receiver2.acceptsEnergyFrom(
                                sender2,
                                inverseDirection2
                        )) {
                            validReceivers.add(new EnergyTarget(target2, inverseDirection2));
                        }
                    }
                }
            }

        }


        return validReceivers;
    }

    public List<IAdvEnergySource> discoverFirstPathOrSources(final IAdvEnergyTile par1) {
        final Set<IAdvEnergyTile> reached = new HashSet<>();
        final List<IAdvEnergySource> result = new ArrayList<>();
        final List<IAdvEnergyTile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final IAdvEnergyTile tile = workList.remove(0);
            final TileEntity te;
            te = tile.getTileEntity();
            if (te == null) {
                continue;
            }
            if (!te.isInvalid()) {
                final List<EnergyTarget> targets = this.getValidReceivers(tile, true);

                for (EnergyTarget energyTarget : targets) {
                    final IAdvEnergyTile target = energyTarget.tileEntity;
                    if (target != par1) {
                        if (!reached.contains(target)) {
                            reached.add(target);
                            if (target instanceof IAdvEnergySource) {
                                result.add((IAdvEnergySource) target);
                            } else if (target instanceof IAdvConductor) {
                                workList.add(target);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }


    public void onTickEnd() {

        for (IAdvEnergySource source : energySourceList) {
            removeTile(source);
            explodeMachineAt(getTileFromIEnergy(source));

        }
        energySourceList.clear();
        this.suncoef.calculate();

        if (this.waitingList.hasWork()) {
            final List<IAdvEnergyTile> tiles = this.waitingList.getPathTiles();

            for (final IAdvEnergyTile tile : tiles) {
                final List<IAdvEnergySource> sources = this.discoverFirstPathOrSources(tile);

                if (sources.size() > 0) {
                    this.energySourceToEnergyPathMap.removeAllSource1(sources);
                }
            }
            this.waitingList.clear();

        }
        for (EnergyTick tick : this.energySourceToEnergyPathMap.senderPath) {
            final IAdvEnergySource entry = tick.getSource();
            tick.tick();
            if (tick.getList() != null) {
                if (tick.getList().isEmpty()) {
                    continue;
                }
            }
            int multi = entry instanceof IMultiDual ? 4 : 1;
            for (int i = 0; i < multi; i++) {
                double offer = Math.min(
                        entry.getOfferedEnergy(),
                        EnergyNetGlobal.instance.getPowerFromTier(entry.getSourceTier())
                );
                if (offer > 0) {
                    final double removed = offer - this.emitEnergyFrom(entry, offer, tick);
                    entry.drawEnergy(removed);
                    tick.addEnergy(removed);
                } else {

                    if (tick.isAdv()) {
                        if (tick.getAdvSource().isSource()) {
                            tick.getAdvSource().setPastEnergy(tick.getAdvSource().getPerEnergy());
                        }
                    }
                }

            }

        }


        this.tick++;
    }

    public IAdvEnergyTile getTileEntity(BlockPos pos) {
        return this.chunkCoordinatesIAdvEnergyTileMap.getOrDefault(pos, EnergyNetGlobal.EMPTY);
    }

    public NodeStats getNodeStats(final IAdvEnergyTile tile) {
        final double emitted = this.getTotalEnergyEmitted(tile);
        final double received = this.getTotalEnergySunken(tile);
        return new NodeStats(received, emitted, 0);
    }

    public List<EnergyPath> getEnergyPaths(IAdvEnergyTile energyTile) {
        List<EnergyPath> energyPathList = new ArrayList<>();
        if (energyTile instanceof IAdvEnergySource) {
            return energyPathList;
        }
        if (energyTile instanceof IAdvConductor) {
            for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((IEnergyAcceptor) energyTile)) {

                if (energyPath.conductors.contains(
                        energyTile)) {
                    energyPathList.add(energyPath);
                }
            }
        }
        return energyPathList;
    }

    public void update(BlockPos pos) {
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos
                    .offset(dir);
            final IAdvEnergyTile tile = this.chunkCoordinatesIAdvEnergyTileMap.get(pos1);
            if (tile != EnergyNetGlobal.EMPTY) {
                if (tile instanceof IAdvConductor) {
                    ((IAdvConductor) tile).update_render();
                }
            }

        }
    }

    public Map<BlockPos, IAdvEnergyTile> getChunkCoordinatesIAdvEnergyTileMap() {
        return chunkCoordinatesIAdvEnergyTileMap;
    }


    public void onUnload() {
        this.energySourceToEnergyPathMap.clear();
        this.waitingList.clear();
        this.chunkCoordinatesIAdvEnergyTileMap.clear();
        this.controllerList.clear();


    }

    static class EnergyTarget {

        final IAdvEnergyTile tileEntity;
        final EnumFacing direction;

        EnergyTarget(final IAdvEnergyTile tileEntity, final EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }

    }

    public static class EnergyPath {

        final List<IAdvConductor> conductors;
        final IAdvEnergySink target;
        final EnumFacing targetDirection;
        long totalEnergyConducted;
        double min = Double.MAX_VALUE;
        double loss = 0.0D;
        boolean hasController = false;
        boolean isLimit = false;
        double limit_amount = Double.MAX_VALUE;

        EnergyPath(IAdvEnergySink sink, EnumFacing facing) {
            this.target = sink;
            this.conductors = new ArrayList<>();
            this.totalEnergyConducted = 0L;
            this.targetDirection = facing;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || (getClass() != o.getClass() && !(o instanceof IAdvEnergySink))) {
                return false;
            }
            if (o instanceof IAdvEnergySink) {
                IAdvEnergySink energySink = (IAdvEnergySink) o;
                return energySink == target;
            }
            EnergyPath path = (EnergyPath) o;
            return target == path.target;
        }

        @Override
        public int hashCode() {
            return Objects.hash(target);
        }

        public List<IAdvConductor> getConductors() {
            return conductors;
        }

        public void setHasController(final boolean hasController) {
            this.hasController = hasController;
        }

        public double getMin() {
            return min;
        }

        public void setMin(final double min) {
            this.min = min;
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

    }


    static class EnergyPathMap {

        final List<EnergyTick> senderPath;

        EnergyPathMap() {
            this.senderPath = new ArrayList<>();
        }

        public void put(final IAdvEnergySource par1, final List<EnergyPath> par2) {
            this.senderPath.add(new EnergyTick(par1, par2));
        }


        public boolean containsKey(final EnergyTick par1) {
            return this.senderPath.contains(par1);
        }

        public boolean containsKey(final IAdvEnergySource par1) {
            return this.senderPath.contains(new EnergyTick(par1, null));
        }


        public void remove1(final IAdvEnergySource par1) {

            for (EnergyTick ticks : this.senderPath) {
                if (ticks.getSource() == par1) {
                    ticks.setList(null);
                    break;
                }
            }
        }

        public void remove(final IAdvEnergySource par1) {
            this.senderPath.remove(new EnergyTick(par1, null));
        }

        public void remove(final EnergyTick par1) {
            this.senderPath.remove(par1);
        }

        public void removeAll(final List<EnergyTick> par1) {
            if (par1 == null) {
                return;
            }
            for (EnergyTick IAdvEnergySource : par1) {
                IAdvEnergySource.setList(null);
            }
        }

        public void removeAllSource1(final List<IAdvEnergySource> par1) {
            if (par1 == null) {
                return;
            }
            for (IAdvEnergySource IAdvEnergySource : par1) {
                this.remove1(IAdvEnergySource);
            }
        }

        public List<EnergyPath> getPaths(final IEnergyAcceptor par1) {
            final List<EnergyPath> paths = new ArrayList<>();

            List<EnergyTick> sources_list = this.getSources(par1);
            if (sources_list == null || sources_list.isEmpty()) {
                return paths;
            }
            for (final EnergyTick source : sources_list) {
                if (this.containsKey(source)) {
                    paths.addAll(source.getList());
                }
            }
            return paths;
        }

        public List<EnergyTick> getSenderPath() {
            return senderPath;
        }

        public List<EnergyTick> getSources(final IEnergyAcceptor par1) {
            final List<EnergyTick> source = new ArrayList<>();
            for (final EnergyTick entry : this.senderPath) {
                if (source.contains(entry)) {
                    continue;
                }
                if (entry.getList() != null) {
                    for (EnergyPath path : entry.getList()) {
                        if ((!(par1 instanceof IAdvConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof IAdvEnergySink) || path.target != par1)) {
                            continue;
                        }
                        source.add(entry);
                    }
                }
            }
            return source;
        }


        public void clear() {
            this.senderPath.clear();
        }


        public EnergyTick get(IAdvEnergySource tileEntity) {
            for (EnergyTick entry : this.senderPath) {
                if (entry.getSource() == tileEntity) {
                    return entry;
                }
            }
            return null;
        }

    }


    static class PathLogic {

        final List<IAdvEnergyTile> tiles;

        PathLogic() {
            this.tiles = new ArrayList<>();
        }

        public boolean contains(final IAdvEnergyTile par1) {
            return this.tiles.contains(par1);
        }

        public void add(final IAdvEnergyTile par1) {
            this.tiles.add(par1);
        }

        public void remove(final IAdvEnergyTile par1) {
            this.tiles.remove(par1);
        }

        public void clear() {
            this.tiles.clear();
        }

        public IAdvEnergyTile getRepresentingTile() {
            if (this.tiles.isEmpty()) {
                return EnergyNetGlobal.EMPTY;
            }
            return this.tiles.get(0);
        }

    }

    class WaitingList {

        final List<PathLogic> paths;

        WaitingList() {
            this.paths = new ArrayList<>();
        }

        public void onTileEntityAdded(final List<EnergyTarget> around, final IEnergyAcceptor tile) {
            if (around.isEmpty() || this.paths.isEmpty()) {
                this.createNewPath(tile);
                return;
            }
            boolean found = false;
            final List<PathLogic> logics = new ArrayList<>();
            for (final PathLogic logic : this.paths) {
                if (logic.contains(tile)) {
                    found = true;
                    if (tile instanceof IAdvConductor) {
                        logics.add(logic);
                    }
                } else {
                    for (final EnergyTarget target : around) {
                        if (logic.contains(target.tileEntity)) {
                            found = true;
                            logic.add(tile);
                            if (target.tileEntity instanceof IAdvConductor) {
                                logics.add(logic);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            if (logics.size() > 1 && tile instanceof IAdvConductor) {
                final PathLogic newLogic = new PathLogic();
                for (final PathLogic logic2 : logics) {
                    this.paths.remove(logic2);
                    for (final IAdvEnergyTile toMove : logic2.tiles) {
                        if (!newLogic.contains(toMove)) {
                            newLogic.add(toMove);
                        }
                    }
                }
                this.paths.add(newLogic);
            }

            if (!found) {

                this.createNewPath(tile);

            }
        }

        public void onTileEntityRemoved(final IEnergyAcceptor par1) {
            if (this.paths.isEmpty()) {
                return;
            }

            List<IAdvEnergyTile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); i++) {
                PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (final IAdvEnergyTile tile : toRecalculate) {
                this.onTileEntityAdded(EnergyNetLocal.this.getValidReceivers(tile, true), (IEnergyAcceptor) tile);
            }
        }

        public void createNewPath(final IAdvEnergyTile par1) {
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

        public List<IAdvEnergyTile> getPathTiles() {
            final List<IAdvEnergyTile> tiles = new ArrayList<>();
            for (PathLogic path : this.paths) {
                final IAdvEnergyTile tile = path.getRepresentingTile();
                if (tile != EnergyNetGlobal.EMPTY) {
                    tiles.add(tile);
                }
            }
            return tiles;
        }

    }

}
