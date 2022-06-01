package com.denfop.qe;

import com.denfop.api.qe.IQEAcceptor;
import com.denfop.api.qe.IQEConductor;
import com.denfop.api.qe.IQEEmitter;
import com.denfop.api.qe.IQESink;
import com.denfop.api.qe.IQESource;
import com.denfop.api.qe.IQETile;
import com.denfop.api.qe.NodeQEStats;
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

public class QENetLocal {

    private static EnumFacing[] directions;

    static {
        QENetLocal.directions = EnumFacing.values();
    }

    private final World world;
    private final QEPathMap QESourceToQEPathMap;
    private final Map<IQETile, BlockPos> chunkCoordinatesMap;
    private final Map<IQETile, TileEntity> QETileTileEntityMap;
    private final Map<BlockPos, IQETile> chunkCoordinatesIQETileMap;
    private final List<IQESource> sources;
    private final WaitingList waitingList;
    private int tick;

    public QENetLocal(final World world) {
        this.QESourceToQEPathMap = new QEPathMap();
        this.sources = new ArrayList<>();
        this.waitingList = new WaitingList();
        this.world = world;
        this.chunkCoordinatesIQETileMap = new HashMap<>();
        this.chunkCoordinatesMap = new HashMap<>();
        this.QETileTileEntityMap = new HashMap<>();
        this.tick = 0;
    }

    public void addTile(IQETile tile1) {

        this.addTileEntity(getTileFromIQE(tile1).getPos(), tile1);


    }

    public void addTileEntity(final BlockPos coords, final IQETile tile) {
        if (this.chunkCoordinatesIQETileMap.containsKey(coords)) {
            return;
        }

        TileEntity te = getTileFromIQE(tile);
        this.QETileTileEntityMap.put(tile, te);
        this.chunkCoordinatesMap.put(tile, coords);
        this.chunkCoordinatesIQETileMap.put(coords, tile);
        this.update(coords.getX(), coords.getY(), coords.getZ());
        if (tile instanceof IQEAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), tile);
        }
        if (tile instanceof IQESource) {
            this.sources.add((IQESource) tile);
        }
    }


    public void removeTile(IQETile tile1) {

        this.removeTileEntity(tile1);

    }

    public void removeTileEntity(IQETile tile) {
        if (!this.QETileTileEntityMap.containsKey(tile)) {
            return;
        }
        final BlockPos coord = this.chunkCoordinatesMap.get(tile);
        this.chunkCoordinatesMap.remove(tile);
        this.QETileTileEntityMap.remove(tile, this.QETileTileEntityMap.get(tile));
        this.chunkCoordinatesIQETileMap.remove(coord, tile);
        this.update(coord.getX(), coord.getY(), coord.getZ());
        if (tile instanceof IQEAcceptor) {
            this.QESourceToQEPathMap.removeAll(this.QESourceToQEPathMap.getSources((IQEAcceptor) tile));
            this.waitingList.onTileEntityRemoved(tile);
        }
        if (tile instanceof IQESource) {
            this.sources.remove((IQESource) tile);
            this.QESourceToQEPathMap.remove((IQESource) tile);
        }
    }

    public TileEntity getTileFromMap(IQETile tile) {
        return this.QETileTileEntityMap.get(tile);
    }

    public double emitQEFrom(final IQESource QESource, double amount) {
        List<QEPath> QEPaths = this.QESourceToQEPathMap.get(QESource);
        if (QEPaths == null) {
            this.QESourceToQEPathMap.put(QESource, this.discover(QESource));
            QEPaths = this.QESourceToQEPathMap.get(QESource);
        }
        if (amount > 0) {
            for (final QEPath QEPath : QEPaths) {
                if (amount <= 0) {
                    break;
                }
                final IQESink QESink = QEPath.target;
                if (QESink.getDemandedQE() <= 0.0) {
                    continue;
                }
                double QEConsumed = 0;
                double QEProvided = Math.floor(Math.round(amount));
                double adding = Math.min(QEProvided, QESink.getDemandedQE());
                if (adding <= 0.0D) {
                    adding = QESink.getDemandedQE();
                }
                if (adding <= 0.0D) {
                    continue;
                }
                double QEReturned = QESink.injectQE(QEPath.targetDirection, adding, 0);
                if (QEReturned >= QEProvided) {
                    QEReturned = QEProvided;
                }
                QEConsumed += adding;
                QEConsumed -= QEReturned;

                double QEInjected = adding - QEReturned;
                QEPath.totalQEConducted = (long) QEInjected;
                amount -= QEConsumed;
                amount = Math.max(0, amount);

            }
        }

        return amount;
    }

    public double getTotalQEEmitted(final IQETile tileEntity) {
        double ret = 0.0;
        if (tileEntity instanceof IQEConductor) {
            for (final QEPath QEPath : this.QESourceToQEPathMap.getPaths((IQEAcceptor) tileEntity)) {
                if (QEPath.conductors.contains(tileEntity)) {
                    ret += QEPath.totalQEConducted;
                }
            }
        }
        if (tileEntity instanceof IQESource) {
            IQESource advEnergySink = (IQESource) tileEntity;
            if (advEnergySink.isSource()) {
                ret = advEnergySink.getPerEnergy() - advEnergySink.getPastEnergy();
            }
        }
        return ret;
    }

    public double getTotalQESunken(final IQETile tileEntity) {
        double ret = 0.0;
        if (tileEntity instanceof IQEConductor) {
            for (final QEPath QEPath : this.QESourceToQEPathMap.getPaths((IQEAcceptor) tileEntity)) {
                if (QEPath.conductors.contains(
                        tileEntity)) {
                    ret += QEPath.totalQEConducted;
                }
            }
        }
        if (tileEntity instanceof IQESink) {
            IQESink advEnergySink = (IQESink) tileEntity;
            if (advEnergySink.isSink()) {
                ret = advEnergySink.getPerEnergy() - advEnergySink.getPastEnergy();
            }
        }
        return ret;
    }

    public TileEntity getTileFromIQE(IQETile tile) {
        if (tile instanceof TileEntity) {
            return (TileEntity) tile;
        }
        if (tile instanceof ILocatable) {
            return this.world.getTileEntity(((ILocatable) tile).getPosition());
        }

        return null;
    }

    private List<QEPath> discover(final IQESource emitter) {
        final Map<IQETile, QEBlockLink> reachedTileEntities = new HashMap<>();
        final LinkedList<IQETile> tileEntitiesToCheck = new LinkedList<>();

        tileEntitiesToCheck.add(emitter);


        while (!tileEntitiesToCheck.isEmpty()) {
            final IQETile currentTileEntity = tileEntitiesToCheck.remove();
            final List<QETarget> validReceivers = this.getValidReceivers(currentTileEntity, false);
            for (final QETarget validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {

                    if (reachedTileEntities.containsKey(validReceiver.tileEntity)) {
                        continue;
                    }
                    reachedTileEntities.put(validReceiver.tileEntity, new QEBlockLink(validReceiver.direction));
                    if (!(validReceiver.tileEntity instanceof IQEConductor)) {
                        continue;
                    }
                    tileEntitiesToCheck.remove(validReceiver.tileEntity);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }


        }
        final List<QEPath> QEPaths = new LinkedList<>();
        for (final Map.Entry<IQETile, QEBlockLink> entry : reachedTileEntities.entrySet()) {
            IQETile tileEntity = entry.getKey();
            if ((tileEntity instanceof IQESink)) {
                QEBlockLink QEBlockLink = entry.getValue();
                final QEPath QEPath = new QEPath((IQESink) tileEntity, QEBlockLink.direction);
                if (emitter != null) {
                    while (true) {
                        BlockPos te = this.chunkCoordinatesMap.get(tileEntity);
                        if (QEBlockLink != null) {
                            tileEntity = this.getTileEntity(te.offset(QEBlockLink.direction));
                        }
                        if (tileEntity == emitter) {
                            break;
                        }
                        if (!(tileEntity instanceof IQEConductor)) {
                            break;
                        }
                        final IQEConductor QEConductor = (IQEConductor) tileEntity;
                        QEPath.conductors.add(QEConductor);
                        QEBlockLink = reachedTileEntities.get(tileEntity);
                        if (QEBlockLink != null) {
                            continue;
                        }
                        IC2.platform.displayError("An QE network pathfinding entry is corrupted.\nThis could happen due to " +
                                "incorrect Minecraft behavior or a bug.\n\n(Technical information: QEBlockLink, tile " +
                                "entities below)\nE: " + emitter + " (" + te.getX() + "," + te.getY() + "," + te

                                .getZ() + ")\n" + "C: " + tileEntity + " (" + te.getX() + "," + te

                                .getY() + "," + te

                                .getZ() + ")\n" + "R: " + QEPath.target + " (" + this.QETileTileEntityMap
                                .get(QEPath.target)
                                .getPos()
                                .getX() + "," + getTileFromMap(QEPath.target).getPos().getY() + "," + getTileFromIQE(
                                QEPath.target).getPos().getZ() + ")");
                    }
                }
                QEPaths.add(QEPath);
            }
        }
        return QEPaths;
    }

    public IQETile getNeighbor(final IQETile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        return this.getTileEntity(this.QETileTileEntityMap.get(tile).getPos().offset(dir));
    }

    private List<QETarget> getValidReceivers(final IQETile emitter, final boolean reverse) {
        final List<QETarget> validReceivers = new LinkedList<>();

        for (final EnumFacing direction : QENetLocal.directions) {
            final IQETile target2 = getNeighbor(emitter, direction);
            if (target2 != null) {
                final EnumFacing inverseDirection2 = direction.getOpposite();
                if (reverse) {
                    if (emitter instanceof IQEAcceptor && target2 instanceof IQEEmitter) {
                        final IQEEmitter sender2 = (IQEEmitter) target2;
                        final IQEAcceptor receiver2 = (IQEAcceptor) emitter;
                        if (sender2.emitsQETo(receiver2, inverseDirection2) && receiver2.acceptsQEFrom(
                                sender2,
                                direction
                        )) {
                            validReceivers.add(new QETarget(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof IQEEmitter && target2 instanceof IQEAcceptor) {
                    final IQEEmitter sender2 = (IQEEmitter) emitter;
                    final IQEAcceptor receiver2 = (IQEAcceptor) target2;
                    if (sender2.emitsQETo(receiver2, direction) && receiver2.acceptsQEFrom(
                            sender2,
                            inverseDirection2
                    )) {
                        validReceivers.add(new QETarget(target2, inverseDirection2));
                    }
                }
            }
        }


        return validReceivers;
    }

    public List<IQESource> discoverFirstPathOrSources(final IQETile par1) {
        final Set<IQETile> reached = new HashSet<>();
        final List<IQESource> result = new ArrayList<>();
        final List<IQETile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final IQETile tile = workList.remove(0);
            final TileEntity te = this.QETileTileEntityMap.get(tile);
            if (!te.isInvalid()) {
                final List<QETarget> targets = this.getValidReceivers(tile, true);
                for (QETarget QETarget : targets) {
                    final IQETile target = QETarget.tileEntity;
                    if (target != par1) {
                        if (!reached.contains(target)) {
                            reached.add(target);
                            if (target instanceof IQESource) {
                                result.add((IQESource) target);
                            } else if (target instanceof IQEConductor) {
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
                final List<IQETile> tiles = this.waitingList.getPathTiles();
                for (final IQETile tile : tiles) {
                    final List<IQESource> sources = this.discoverFirstPathOrSources(tile);
                    if (sources.size() > 0) {
                        this.QESourceToQEPathMap.removeAll(sources);
                    }
                }
                this.waitingList.clear();
            }
        }
        if (this.world.provider.getWorldTime() % 2 == 0) {
            for (IQESource entry : this.sources) {
                if (entry != null) {
                    double offer = entry.getOfferedQE();
                    if (offer > 0) {
                        for (double packetAmount = 1, i = 0; i < packetAmount; ++i) {
                            offer = entry.getOfferedQE();
                            if (offer < 1) {
                                break;
                            }
                            final double removed = offer - this.emitQEFrom(entry, offer);
                            if (removed <= 0) {
                                break;
                            }

                            entry.drawQE(removed);
                        }
                    } else {
                        entry.setPastEnergy(entry.getPerEnergy());
                    }

                }
            }
            this.tick++;
        }
    }

    public IQETile getTileEntity(BlockPos pos) {

        return this.chunkCoordinatesIQETileMap.get(pos);
    }

    public NodeQEStats getNodeStats(final IQETile tile) {
        final double emitted = this.getTotalQEEmitted(tile);
        final double received = this.getTotalQESunken(tile);
        return new NodeQEStats(received, emitted);
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
        this.QESourceToQEPathMap.clear();
        this.sources.clear();
        this.waitingList.clear();
        this.chunkCoordinatesIQETileMap.clear();
        this.chunkCoordinatesMap.clear();
        this.QETileTileEntityMap.clear();
    }

    static class QETarget {

        final IQETile tileEntity;
        final EnumFacing direction;

        QETarget(final IQETile tileEntity, final EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }

    }

    static class QEBlockLink {

        final EnumFacing direction;

        QEBlockLink(final EnumFacing direction) {
            this.direction = direction;
        }

    }

    static class QEPath {

        final Set<IQEConductor> conductors;
        final IQESink target;
        final EnumFacing targetDirection;
        long totalQEConducted;

        QEPath(IQESink sink, EnumFacing facing) {
            this.target = sink;
            this.conductors = new HashSet<>();
            this.totalQEConducted = 0L;
            this.targetDirection = facing;
        }

    }

    static class QEPathMap {

        final Map<IQESource, List<QEPath>> senderPath;

        QEPathMap() {
            this.senderPath = new HashMap<>();
        }

        public void put(final IQESource par1, final List<QEPath> par2) {
            this.senderPath.put(par1, par2);
        }


        public boolean containsKey(final IQESource par1) {
            return this.senderPath.containsKey(par1);
        }

        public List<QEPath> get(final IQESource par1) {
            return this.senderPath.get(par1);
        }


        public void remove(final IQESource par1) {
            this.senderPath.remove(par1);
        }

        public void removeAll(final List<IQESource> par1) {
            for (IQESource iQESource : par1) {
                this.remove(iQESource);
            }
        }


        public List<QEPath> getPaths(final IQEAcceptor par1) {
            final List<QEPath> paths = new ArrayList<>();
            for (final IQESource source : this.getSources(par1)) {
                if (this.containsKey(source)) {
                    paths.addAll(this.get(source));
                }
            }
            return paths;
        }

        public List<IQESource> getSources(final IQEAcceptor par1) {
            final List<IQESource> source = new ArrayList<>();
            for (final Map.Entry<IQESource, List<QEPath>> entry : this.senderPath.entrySet()) {
                if (source.contains(entry.getKey())) {
                    continue;
                }
                for (QEPath path : entry.getValue()) {
                    if ((!(par1 instanceof IQEConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof IQESink) || path.target != par1)) {
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

        final List<IQETile> tiles;

        PathLogic() {
            this.tiles = new ArrayList<>();
        }

        public boolean contains(final IQETile par1) {
            return this.tiles.contains(par1);
        }

        public void add(final IQETile par1) {
            this.tiles.add(par1);
        }

        public void remove(final IQETile par1) {
            this.tiles.remove(par1);
        }

        public void clear() {
            this.tiles.clear();
        }

        public IQETile getRepresentingTile() {
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

        public void onTileEntityAdded(final List<QETarget> around, final IQETile tile) {
            if (around.isEmpty() || this.paths.isEmpty()) {
                this.createNewPath(tile);
                return;
            }
            boolean found = false;
            final List<PathLogic> logics = new ArrayList<>();
            for (final PathLogic logic : this.paths) {
                if (logic.contains(tile)) {
                    found = true;
                    if (tile instanceof IQEConductor) {
                        logics.add(logic);
                    }
                } else {
                    for (final QETarget target : around) {
                        if (logic.contains(target.tileEntity)) {
                            found = true;
                            logic.add(tile);
                            if (target.tileEntity instanceof IQEConductor) {
                                logics.add(logic);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            if (logics.size() > 1 && tile instanceof IQEConductor) {
                final PathLogic newLogic = new PathLogic();
                for (final PathLogic logic2 : logics) {
                    this.paths.remove(logic2);
                    for (final IQETile toMove : logic2.tiles) {
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

        public void onTileEntityRemoved(final IQETile par1) {
            if (this.paths.isEmpty()) {
                return;
            }
            final List<IQETile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); ++i) {
                final PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (final IQETile tile : toRecalculate) {
                this.onTileEntityAdded(QENetLocal.this.getValidReceivers(tile, true), tile);
            }
        }

        public void createNewPath(final IQETile par1) {
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

        public List<IQETile> getPathTiles() {
            final List<IQETile> tiles = new ArrayList<>();
            for (PathLogic path : this.paths) {
                final IQETile tile = path.getRepresentingTile();
                if (tile != null) {
                    tiles.add(tile);
                }
            }
            return tiles;
        }

    }

}
