package com.denfop.cool;

import com.denfop.api.cooling.ICoolAcceptor;
import com.denfop.api.cooling.ICoolConductor;
import com.denfop.api.cooling.ICoolEmitter;
import com.denfop.api.cooling.ICoolSink;
import com.denfop.api.cooling.ICoolSource;
import com.denfop.api.cooling.ICoolTile;
import ic2.api.energy.NodeStats;
import ic2.api.info.ILocatable;
import ic2.core.IC2;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class CoolNetLocal {

    private static final EnumFacing[] directions;

    static {
        directions = EnumFacing.values();
    }

    private final World world;
    private final EnergyPathMap energySourceToEnergyPathMap;
    private final Map<ICoolTile, BlockPos> chunkCoordinatesMap;
    private final Map<ICoolTile, TileEntity> energyTileTileEntityMap;
    private final Map<BlockPos, ICoolTile> chunkCoordinatesICoolTileMap;
    private final List<ICoolSource> sources;
    private final List<ICoolTile> energyTileList;
    private final WaitingList waitingList;
    private final List<BlockPos> listFromCoord;

    CoolNetLocal(final World world) {
        this.energySourceToEnergyPathMap = new EnergyPathMap();
        this.sources = new ArrayList<>();
        this.waitingList = new WaitingList();
        this.world = world;
        this.chunkCoordinatesICoolTileMap = new HashMap<>();
        this.chunkCoordinatesMap = new HashMap<>();
        this.energyTileList = new ArrayList<>();
        this.energyTileTileEntityMap = new HashMap<>();
        this.listFromCoord = new ArrayList<>();
    }

    public void addTile(ICoolTile tile1) {
        this.addTileEntity(getTileFromICool(tile1).getPos(), tile1);
        this.energyTileList.add(tile1);

    }

    public void addTileEntity(final BlockPos coords, final ICoolTile tile) {
        if (this.listFromCoord.contains(coords)) {
            return;
        }

        TileEntity te = getTileFromICool(tile);
        this.listFromCoord.add(coords);
        this.energyTileTileEntityMap.put(tile, te);
        this.chunkCoordinatesMap.put(tile, coords);
        this.chunkCoordinatesICoolTileMap.put(coords, tile);
        this.update(coords.getX(), coords.getY(), coords.getZ());
        if (tile instanceof ICoolAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), tile);
        }
        if (tile instanceof ICoolSource) {
            this.sources.add((ICoolSource) tile);
        }
    }

    public void removeTile(ICoolTile tile1) {
        this.removeTileEntity(tile1);
        this.energyTileList.remove(tile1);

    }

    public void removeTileEntity(ICoolTile tile) {
        if (!this.energyTileList.contains(tile)) {
            return;
        }
        final BlockPos coord = this.chunkCoordinatesMap.get(tile);
        this.listFromCoord.remove(coord);
        this.chunkCoordinatesMap.remove(tile);
        this.energyTileTileEntityMap.remove(tile, this.energyTileTileEntityMap.get(tile));
        this.chunkCoordinatesICoolTileMap.remove(coord, tile);
        this.update(coord.getX(), coord.getY(), coord.getZ());
        if (tile instanceof ICoolAcceptor) {
            this.energySourceToEnergyPathMap.removeAll(this.energySourceToEnergyPathMap.getSources((ICoolAcceptor) tile));
            this.waitingList.onTileEntityRemoved(tile);
        }
        if (tile instanceof ICoolSource) {
            this.sources.remove((ICoolSource) tile);
            this.energySourceToEnergyPathMap.remove((ICoolSource) tile);
        }
    }

    public double emitEnergyFrom(final ICoolSource energySource, double amount) {
        if (!this.energySourceToEnergyPathMap.containsKey(energySource)) {
            this.energySourceToEnergyPathMap.put(energySource, this.discover(energySource));
        }
        List<EnergyPath> activeEnergyPaths = new Vector<>();
        for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.get(energySource)) {
            assert energyPath.target instanceof ICoolSink;
            final ICoolSink energySink = (ICoolSink) energyPath.target;
            if (energySink.getDemandedCool() <= 0.0) {
                continue;
            }


            activeEnergyPaths.add(energyPath);
        }
        while (!activeEnergyPaths.isEmpty() && amount > 0) {
            double energyConsumed = 0;

            final List<EnergyPath> currentActiveEnergyPaths = activeEnergyPaths;
            activeEnergyPaths = new Vector<>();
            for (final EnergyPath energyPath2 : currentActiveEnergyPaths) {
                final ICoolSink energySink2 = (ICoolSink) energyPath2.target;


                double adding = Math.min(amount, energySink2.getDemandedCool());

                if (adding <= 0.0) {
                    continue;
                }


                double energyReturned = energySink2.injectCool(energyPath2.targetDirection, adding, 0);
                if (energyReturned == 0.0) {
                    activeEnergyPaths.add(energyPath2);
                } else if (energyReturned >= amount) {
                    energyReturned = amount;
                }
                energyConsumed += (adding - energyReturned);
                for (ICoolConductor energyConductor3 : energyPath2.conductors) {
                    if (energySource.getOfferedCool() >= energyConductor3.getConductorBreakdownEnergy()) {
                        energyConductor3.removeConductor();
                    }
                }
            }
            if (energyConsumed == 0 && !activeEnergyPaths.isEmpty()) {
                activeEnergyPaths.remove(activeEnergyPaths.size() - 1);
            }

            amount -= energyConsumed;

        }
        return amount;
    }

    public double getTotalEnergyEmitted(final ICoolTile tileEntity) {
        double ret = 0.0;
        if (tileEntity instanceof ICoolConductor) {
            for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((ICoolAcceptor) tileEntity)) {
                if (energyPath.conductors.contains(tileEntity)) {
                    ret += energyPath.totalEnergyConducted;
                }
            }
        }
        if (tileEntity instanceof ICoolSource && this.energySourceToEnergyPathMap.containsKey((ICoolSource) tileEntity)) {
            for (final EnergyPath energyPath2 : this.energySourceToEnergyPathMap.get((ICoolSource) tileEntity)) {
                ret += energyPath2.totalEnergyConducted;
            }
        }
        return ret;
    }

    public double getTotalEnergySunken(final ICoolTile tileEntity) {
        double ret = 0.0;
        if (tileEntity instanceof ICoolConductor || tileEntity instanceof ICoolSink) {
            for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((ICoolAcceptor) tileEntity)) {
                if ((tileEntity instanceof ICoolSink && energyPath.target == tileEntity) || (tileEntity instanceof ICoolConductor && energyPath.conductors.contains(
                        tileEntity))) {
                    ret += energyPath.totalEnergyConducted;
                }
            }
        }
        return ret;
    }

    public TileEntity getTileFromICool(ICoolTile tile) {
        if (tile instanceof TileEntity) {
            return (TileEntity) tile;
        }
        if (tile instanceof ILocatable) {
            return this.world.getTileEntity(((ILocatable) tile).getPosition());
        }

        return null;
    }

    private List<EnergyPath> discover(final ICoolSource emitter) {
        final Map<ICoolTile, EnergyBlockLink> reachedTileEntities = new HashMap<>();
        final LinkedList<ICoolTile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(emitter);


        while (!tileEntitiesToCheck.isEmpty()) {
            final ICoolTile currentTileEntity = tileEntitiesToCheck.remove();

            TileEntity tile = this.energyTileTileEntityMap.get(currentTileEntity);

            if (!tile.isInvalid()) {
                final List<EnergyTarget> validReceivers = this.getValidReceivers(currentTileEntity, false);
                for (final EnergyTarget validReceiver : validReceivers) {
                    if (validReceiver.tileEntity != emitter) {

                        if (reachedTileEntities.containsKey(validReceiver.tileEntity)) {
                            continue;
                        }
                        reachedTileEntities.put(validReceiver.tileEntity, new EnergyBlockLink(validReceiver.direction));
                        if (!(validReceiver.tileEntity instanceof ICoolConductor)) {
                            continue;
                        }
                        tileEntitiesToCheck.remove(validReceiver.tileEntity);
                        tileEntitiesToCheck.add(validReceiver.tileEntity);
                    }
                }
            }

        }
        final List<EnergyPath> energyPaths = new LinkedList<>();
        for (final Map.Entry<ICoolTile, EnergyBlockLink> entry : reachedTileEntities.entrySet()) {
            ICoolTile tileEntity = entry.getKey();
            if ((tileEntity instanceof ICoolSink)) {
                EnergyBlockLink energyBlockLink = entry.getValue();
                final EnergyPath energyPath = new EnergyPath();
                energyPath.target = tileEntity;
                energyPath.targetDirection = energyBlockLink.direction;
                if (emitter != null) {
                    while (true) {
                        TileEntity te = this.energyTileTileEntityMap.get(tileEntity);
                        if (energyBlockLink != null) {
                            tileEntity = this.getTileEntity(te.getPos().offset(energyBlockLink.direction));
                        }
                        if (tileEntity == emitter) {
                            break;
                        }
                        if (!(tileEntity instanceof ICoolConductor)) {
                            break;
                        }
                        final ICoolConductor energyConductor = (ICoolConductor) tileEntity;
                        energyPath.conductors.add(energyConductor);
                        energyBlockLink = reachedTileEntities.get(tileEntity);
                        if (energyBlockLink != null) {
                            continue;
                        }
                        IC2.platform.displayError("An energy network pathfinding entry is corrupted.\nThis could happen due to " +
                                "incorrect Minecraft behavior or a bug.\n\n(Technical information: energyBlockLink, tile " +
                                "entities below)\nE: " + emitter + " (" + te.getPos().getX() + "," + te.getPos().getY() + "," + te
                                .getPos()
                                .getZ() + ")\n" + "C: " + tileEntity + " (" + te.getPos().getX() + "," + te
                                .getPos()
                                .getY() + "," + te
                                .getPos()
                                .getZ() + ")\n" + "R: " + energyPath.target + " (" + this.energyTileTileEntityMap
                                .get(energyPath.target)
                                .getPos()
                                .getX() + "," + getTileFromICool(energyPath.target).getPos().getY() + "," + getTileFromICool(
                                energyPath.target).getPos().getZ() + ")");
                    }
                }
                energyPaths.add(energyPath);
            }
        }
        return energyPaths;
    }

    public ICoolTile getNeighbor(final ICoolTile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        return this.getTileEntity(this.energyTileTileEntityMap.get(tile).getPos().offset(dir));
    }

    private List<EnergyTarget> getValidReceivers(final ICoolTile emitter, final boolean reverse) {
        final List<EnergyTarget> validReceivers = new LinkedList<>();

        for (final EnumFacing direction : directions) {
            final ICoolTile target2 = getNeighbor(emitter, direction);
            if (target2 != null) {
                final EnumFacing inverseDirection2 = direction.getOpposite();
                if (reverse) {
                    if (emitter instanceof ICoolAcceptor && target2 instanceof ICoolEmitter) {
                        final ICoolEmitter sender2 = (ICoolEmitter) target2;
                        final ICoolAcceptor receiver2 = (ICoolAcceptor) emitter;
                        if (sender2.emitsCoolTo(receiver2, inverseDirection2) && receiver2.acceptsCoolFrom(
                                sender2,
                                direction
                        )) {
                            validReceivers.add(new EnergyTarget(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof ICoolEmitter && target2 instanceof ICoolAcceptor) {
                    final ICoolEmitter sender2 = (ICoolEmitter) emitter;
                    final ICoolAcceptor receiver2 = (ICoolAcceptor) target2;
                    if (sender2.emitsCoolTo(receiver2, direction) && receiver2.acceptsCoolFrom(sender2, inverseDirection2)) {
                        validReceivers.add(new EnergyTarget(target2, inverseDirection2));
                    }
                }
            }
        }


        return validReceivers;
    }

    public List<ICoolSource> discoverFirstPathOrSources(final ICoolTile par1) {
        final Set<ICoolTile> reached = new HashSet<>();
        final List<ICoolSource> result = new ArrayList<>();
        final List<ICoolTile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final ICoolTile tile = workList.remove(0);
            final TileEntity te = this.energyTileTileEntityMap.get(tile);
            if (!te.isInvalid()) {
                final List<EnergyTarget> targets = this.getValidReceivers(tile, true);
                for (EnergyTarget energyTarget : targets) {
                    final ICoolTile target = energyTarget.tileEntity;
                    if (target != par1) {
                        if (!reached.contains(target)) {
                            reached.add(target);
                            if (target instanceof ICoolSource) {
                                result.add((ICoolSource) target);
                            } else if (target instanceof ICoolConductor) {
                                workList.add(target);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public void onTickStart() {


    }

    public void onTickEnd() {
        if (this.world.provider.getWorldTime() % 20 == 0) {
            if (this.waitingList.hasWork()) {
                final List<ICoolTile> tiles = this.waitingList.getPathTiles();
                for (final ICoolTile tile : tiles) {
                    final List<ICoolSource> sources = this.discoverFirstPathOrSources(tile);
                    if (sources.size() > 0) {
                        this.energySourceToEnergyPathMap.removeAll(sources);
                    }
                }
                this.waitingList.clear();
            }
        }
        for (ICoolSource entry : this.sources) {
            if (entry != null) {


                double offer = entry.getOfferedCool();
                if (offer > 0) {
                    for (double packetAmount = 1, i = 0; i < packetAmount; ++i) {
                        offer = entry.getOfferedCool();
                        if (offer < 1) {
                            break;
                        }
                        final double removed = offer - this.emitEnergyFrom(entry, offer);
                        if (removed <= 0) {
                            break;
                        }

                        entry.drawCool(removed);
                    }
                }

            }
        }
    }

    public ICoolTile getTileEntity(BlockPos pos) {

        return this.chunkCoordinatesICoolTileMap.get(pos);
    }

    public NodeStats getNodeStats(final ICoolTile tile) {
        final double emitted = this.getTotalEnergyEmitted(tile);
        final double received = this.getTotalEnergySunken(tile);
        return new NodeStats(received, emitted, 0);
    }

    void update(final int x, final int y, final int z) {
        for (final EnumFacing dir : EnumFacing.values()) {
            if (this.world.isChunkGeneratedAt(x + dir.getFrontOffsetX() >> 4, z + dir.getFrontOffsetZ() >> 4)) {
                BlockPos pos = new BlockPos(x, y,
                        z
                ).offset(dir);
                this.world.neighborChanged(pos, Blocks.AIR, pos);

            }
        }
    }

    public void onUnload() {
        this.energySourceToEnergyPathMap.clear();
        this.sources.clear();
        this.waitingList.clear();
        this.chunkCoordinatesICoolTileMap.clear();
        this.chunkCoordinatesMap.clear();
        this.energyTileList.clear();
        this.energyTileTileEntityMap.clear();
        this.listFromCoord.clear();
    }

    static class EnergyTarget {

        final ICoolTile tileEntity;
        final EnumFacing direction;

        EnergyTarget(final ICoolTile tileEntity, final EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }

    }

    static class EnergyBlockLink {

        final EnumFacing direction;

        EnergyBlockLink(final EnumFacing direction) {
            this.direction = direction;
        }

    }

    static class EnergyPath {

        final Set<ICoolConductor> conductors;
        ICoolTile target;
        EnumFacing targetDirection;
        long totalEnergyConducted;

        EnergyPath() {
            this.target = null;
            this.conductors = new HashSet<>();
            this.totalEnergyConducted = 0L;
        }

    }

    static class EnergyPathMap {

        final Map<ICoolSource, List<EnergyPath>> senderPath;

        EnergyPathMap() {
            this.senderPath = new HashMap<>();
        }

        public void put(final ICoolSource par1, final List<EnergyPath> par2) {
            this.senderPath.put(par1, par2);


        }

        public boolean containsKey(final ICoolSource par1) {
            return this.senderPath.containsKey(par1);
        }

        public List<EnergyPath> get(final ICoolSource par1) {
            return this.senderPath.get(par1);
        }

        public void remove(final ICoolSource par1) {
            this.senderPath.remove(par1);
        }

        public void removeAll(final List<ICoolSource> par1) {
            for (ICoolSource ICoolSource : par1) {
                this.remove(ICoolSource);
            }
        }

        public List<EnergyPath> getPaths(final ICoolAcceptor par1) {
            final List<EnergyPath> paths = new ArrayList<>();
            for (final ICoolSource source : this.getSources(par1)) {
                if (this.containsKey(source)) {
                    paths.addAll(this.get(source));
                }
            }
            return paths;
        }

        public List<ICoolSource> getSources(final ICoolAcceptor par1) {
            final List<ICoolSource> source = new ArrayList<>();
            for (final Map.Entry<ICoolSource, List<EnergyPath>> entry : this.senderPath.entrySet()) {
                if (source.contains(entry.getKey())) {
                    continue;
                }
                for (EnergyPath path : entry.getValue()) {
                    if ((!(par1 instanceof ICoolConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof ICoolSink) || path.target != par1)) {
                        continue;
                    }
                    source.add(entry.getKey());
                }
            }
            return source;
        }

        public void clear() {
            this.senderPath.clear();
        }

    }

    static class PathLogic {

        final List<ICoolTile> tiles;

        PathLogic() {
            this.tiles = new ArrayList<>();
        }

        public boolean contains(final ICoolTile par1) {
            return this.tiles.contains(par1);
        }

        public void add(final ICoolTile par1) {
            this.tiles.add(par1);
        }

        public void remove(final ICoolTile par1) {
            this.tiles.remove(par1);
        }

        public void clear() {
            this.tiles.clear();
        }

        public ICoolTile getRepresentingTile() {
            if (this.tiles.isEmpty()) {
                return null;
            }
            return this.tiles.get(0);
        }

    }

    class WaitingList {

        final List<PathLogic> paths;

        WaitingList() {
            this.paths = new ArrayList<>();
        }

        public void onTileEntityAdded(final List<EnergyTarget> around, final ICoolTile tile) {
            if (around.isEmpty() || this.paths.isEmpty()) {
                this.createNewPath(tile);
                return;
            }
            boolean found = false;
            final List<PathLogic> logics = new ArrayList<>();
            for (final PathLogic logic : this.paths) {
                if (logic.contains(tile)) {
                    found = true;
                    if (tile instanceof ICoolConductor) {
                        logics.add(logic);
                    }
                } else {
                    for (final EnergyTarget target : around) {
                        if (logic.contains(target.tileEntity)) {
                            found = true;
                            logic.add(tile);
                            if (target.tileEntity instanceof ICoolConductor) {
                                logics.add(logic);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            if (logics.size() > 1 && tile instanceof ICoolConductor) {
                final PathLogic newLogic = new PathLogic();
                for (final PathLogic logic2 : logics) {
                    this.paths.remove(logic2);
                    for (final ICoolTile toMove : logic2.tiles) {
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

        public void onTileEntityRemoved(final ICoolTile par1) {
            if (this.paths.isEmpty()) {
                return;
            }
            final List<ICoolTile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); ++i) {
                final PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (final ICoolTile tile : toRecalculate) {
                this.onTileEntityAdded(CoolNetLocal.this.getValidReceivers(tile, true), tile);
            }
        }

        public void createNewPath(final ICoolTile par1) {
            final PathLogic logic = new PathLogic();
            logic.add(par1);
            this.paths.add(logic);
        }

        public void clear() {
            if (this.paths.isEmpty()) {
                return;
            }
            for (PathLogic path : this.paths) {
                path.clear();
            }
            this.paths.clear();
        }

        public boolean hasWork() {
            return this.paths.size() > 0;
        }

        public List<ICoolTile> getPathTiles() {
            final List<ICoolTile> tiles = new ArrayList<>();
            for (PathLogic path : this.paths) {
                final ICoolTile tile = path.getRepresentingTile();
                if (tile != null) {
                    tiles.add(tile);
                }
            }
            return tiles;
        }

    }

}
