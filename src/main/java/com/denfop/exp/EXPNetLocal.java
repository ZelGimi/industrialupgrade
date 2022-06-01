package com.denfop.exp;

import com.denfop.api.exp.IEXPAcceptor;
import com.denfop.api.exp.IEXPConductor;
import com.denfop.api.exp.IEXPEmitter;
import com.denfop.api.exp.IEXPSink;
import com.denfop.api.exp.IEXPSource;
import com.denfop.api.exp.IEXPTile;
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

public class EXPNetLocal {

    private static EnumFacing[] directions;

    static {
        EXPNetLocal.directions = EnumFacing.values();
    }

    private final World world;
    private final EXPPathMap EXPSourceToEXPPathMap;
    private final Map<IEXPTile, BlockPos> chunkCoordinatesMap;
    private final Map<IEXPTile, TileEntity> EXPTileTileEntityMap;
    private final Map<BlockPos, IEXPTile> chunkCoordinatesIEXPTileMap;
    private final List<IEXPSource> sources;
    private final WaitingList waitingList;

    public EXPNetLocal(final World world) {
        this.EXPSourceToEXPPathMap = new EXPPathMap();
        this.sources = new ArrayList<>();
        this.waitingList = new WaitingList();
        this.world = world;
        this.chunkCoordinatesIEXPTileMap = new HashMap<>();
        this.chunkCoordinatesMap = new HashMap<>();
        this.EXPTileTileEntityMap = new HashMap<>();
    }

    public void addTile(IEXPTile tile1) {

        this.addTileEntity(getTileFromIEXP(tile1).getPos(), tile1);


    }

    public void addTileEntity(final BlockPos coords, final IEXPTile tile) {
        if (this.chunkCoordinatesIEXPTileMap.containsKey(coords)) {
            return;
        }

        TileEntity te = getTileFromIEXP(tile);
        this.EXPTileTileEntityMap.put(tile, te);
        this.chunkCoordinatesMap.put(tile, coords);
        this.chunkCoordinatesIEXPTileMap.put(coords, tile);
        this.update(coords.getX(), coords.getY(), coords.getZ());
        if (tile instanceof IEXPAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), tile);
        }
        if (tile instanceof IEXPSource) {
            this.sources.add((IEXPSource) tile);
        }
    }


    public void removeTile(IEXPTile tile1) {

        this.removeTileEntity(tile1);

    }

    public void removeTileEntity(IEXPTile tile) {
        if (!this.EXPTileTileEntityMap.containsKey(tile)) {
            return;
        }
        final BlockPos coord = this.chunkCoordinatesMap.get(tile);
        this.chunkCoordinatesMap.remove(tile);
        this.EXPTileTileEntityMap.remove(tile, this.EXPTileTileEntityMap.get(tile));
        this.chunkCoordinatesIEXPTileMap.remove(coord, tile);
        this.update(coord.getX(), coord.getY(), coord.getZ());
        if (tile instanceof IEXPAcceptor) {
            this.EXPSourceToEXPPathMap.removeAll(this.EXPSourceToEXPPathMap.getSources((IEXPAcceptor) tile));
            this.waitingList.onTileEntityRemoved(tile);
        }
        if (tile instanceof IEXPSource) {
            this.sources.remove((IEXPSource) tile);
            this.EXPSourceToEXPPathMap.remove((IEXPSource) tile);
        }
    }

    public TileEntity getTileFromMap(IEXPTile tile) {
        return this.EXPTileTileEntityMap.get(tile);
    }

    public double emitEXPFrom(final IEXPSource EXPSource, double amount) {
        List<EXPPath> EXPPaths = this.EXPSourceToEXPPathMap.get(EXPSource);
        if (EXPPaths == null) {
            this.EXPSourceToEXPPathMap.put(EXPSource, this.discover(EXPSource));
            EXPPaths = this.EXPSourceToEXPPathMap.get(EXPSource);
        }
        if (amount > 0) {
            for (final EXPPath EXPPath : EXPPaths) {
                if (amount <= 0) {
                    break;
                }
                final IEXPSink EXPSink = EXPPath.target;
                if (EXPSink.getDemandedEXP() <= 0.0) {
                    continue;
                }
                double EXPConsumed = 0;
                double EXPProvided = Math.floor(Math.round(amount));
                double adding = Math.min(EXPProvided, EXPSink.getDemandedEXP());
                if (adding <= 0.0D) {
                    adding = EXPSink.getDemandedEXP();
                }
                if (adding <= 0.0D) {
                    continue;
                }
                double EXPReturned = EXPSink.injectEXP(EXPPath.targetDirection, adding, 0);
                if (EXPReturned >= EXPProvided) {
                    EXPReturned = EXPProvided;
                }
                EXPConsumed += adding;
                EXPConsumed -= EXPReturned;

                amount -= EXPConsumed;
                amount = Math.max(0, amount);

            }
        }

        return amount;
    }

    public TileEntity getTileFromIEXP(IEXPTile tile) {
        if (tile instanceof TileEntity) {
            return (TileEntity) tile;
        }
        if (tile instanceof ILocatable) {
            return this.world.getTileEntity(((ILocatable) tile).getPosition());
        }

        return null;
    }

    private List<EXPPath> discover(final IEXPSource emitter) {
        final Map<IEXPTile, EXPBlockLink> reachedTileEntities = new HashMap<>();
        final LinkedList<IEXPTile> tileEntitiesToCheck = new LinkedList<>();

        tileEntitiesToCheck.add(emitter);


        while (!tileEntitiesToCheck.isEmpty()) {
            final IEXPTile currentTileEntity = tileEntitiesToCheck.remove();
            final List<EXPTarget> validReceivers = this.getValidReceivers(currentTileEntity, false);
            for (final EXPTarget validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {

                    if (reachedTileEntities.containsKey(validReceiver.tileEntity)) {
                        continue;
                    }
                    reachedTileEntities.put(validReceiver.tileEntity, new EXPBlockLink(validReceiver.direction));
                    if (!(validReceiver.tileEntity instanceof IEXPConductor)) {
                        continue;
                    }
                    tileEntitiesToCheck.remove(validReceiver.tileEntity);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }


        }
        final List<EXPPath> EXPPaths = new LinkedList<>();
        for (final Map.Entry<IEXPTile, EXPBlockLink> entry : reachedTileEntities.entrySet()) {
            IEXPTile tileEntity = entry.getKey();
            if ((tileEntity instanceof IEXPSink)) {
                EXPBlockLink EXPBlockLink = entry.getValue();
                final EXPPath EXPPath = new EXPPath((IEXPSink) tileEntity, EXPBlockLink.direction);
                if (emitter != null) {
                    while (true) {
                        BlockPos te = this.chunkCoordinatesMap.get(tileEntity);
                        if (EXPBlockLink != null) {
                            tileEntity = this.getTileEntity(te.offset(EXPBlockLink.direction));
                        }
                        if (tileEntity == emitter) {
                            break;
                        }
                        if (!(tileEntity instanceof IEXPConductor)) {
                            break;
                        }
                        final IEXPConductor EXPConductor = (IEXPConductor) tileEntity;
                        EXPPath.conductors.add(EXPConductor);
                        EXPBlockLink = reachedTileEntities.get(tileEntity);
                        if (EXPBlockLink != null) {
                            continue;
                        }
                        IC2.platform.displayError("An EXP network pathfinding entry is corrupted.\nThis could happen due to " +
                                "incorrect Minecraft behavior or a bug.\n\n(Technical information: EXPBlockLink, tile " +
                                "entities below)\nE: " + emitter + " (" + te.getX() + "," + te.getY() + "," + te

                                .getZ() + ")\n" + "C: " + tileEntity + " (" + te.getX() + "," + te

                                .getY() + "," + te

                                .getZ() + ")\n" + "R: " + EXPPath.target + " (" + this.EXPTileTileEntityMap
                                .get(EXPPath.target)
                                .getPos()
                                .getX() + "," + getTileFromMap(EXPPath.target).getPos().getY() + "," + getTileFromIEXP(
                                EXPPath.target).getPos().getZ() + ")");
                    }
                }
                EXPPaths.add(EXPPath);
            }
        }
        return EXPPaths;
    }

    public IEXPTile getNeighbor(final IEXPTile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        return this.getTileEntity(this.EXPTileTileEntityMap.get(tile).getPos().offset(dir));
    }

    private List<EXPTarget> getValidReceivers(final IEXPTile emitter, final boolean reverEXP) {
        final List<EXPTarget> validReceivers = new LinkedList<>();

        for (final EnumFacing direction : EXPNetLocal.directions) {
            final IEXPTile target2 = getNeighbor(emitter, direction);
            if (target2 != null) {
                final EnumFacing inverEXPDirection2 = direction.getOpposite();
                if (reverEXP) {
                    if (emitter instanceof IEXPAcceptor && target2 instanceof IEXPEmitter) {
                        final IEXPEmitter EXPnder2 = (IEXPEmitter) target2;
                        final IEXPAcceptor receiver2 = (IEXPAcceptor) emitter;
                        if (EXPnder2.emitsEXPTo(receiver2, inverEXPDirection2) && receiver2.acceptsEXPFrom(
                                EXPnder2,
                                direction
                        )) {
                            validReceivers.add(new EXPTarget(target2, inverEXPDirection2));
                        }
                    }
                } else if (emitter instanceof IEXPEmitter && target2 instanceof IEXPAcceptor) {
                    final IEXPEmitter EXPnder2 = (IEXPEmitter) emitter;
                    final IEXPAcceptor receiver2 = (IEXPAcceptor) target2;
                    if (EXPnder2.emitsEXPTo(receiver2, direction) && receiver2.acceptsEXPFrom(
                            EXPnder2,
                            inverEXPDirection2
                    )) {
                        validReceivers.add(new EXPTarget(target2, inverEXPDirection2));
                    }
                }
            }
        }


        return validReceivers;
    }

    public List<IEXPSource> discoverFirstPathOrSources(final IEXPTile par1) {
        final Set<IEXPTile> reached = new HashSet<>();
        final List<IEXPSource> result = new ArrayList<>();
        final List<IEXPTile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final IEXPTile tile = workList.remove(0);
            final TileEntity te = this.EXPTileTileEntityMap.get(tile);
            if (!te.isInvalid()) {
                final List<EXPTarget> targets = this.getValidReceivers(tile, true);
                for (EXPTarget EXPTarget : targets) {
                    final IEXPTile target = EXPTarget.tileEntity;
                    if (target != par1) {
                        if (!reached.contains(target)) {
                            reached.add(target);
                            if (target instanceof IEXPSource) {
                                result.add((IEXPSource) target);
                            } else if (target instanceof IEXPConductor) {
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
                final List<IEXPTile> tiles = this.waitingList.getPathTiles();
                for (final IEXPTile tile : tiles) {
                    final List<IEXPSource> sources = this.discoverFirstPathOrSources(tile);
                    if (sources.size() > 0) {
                        this.EXPSourceToEXPPathMap.removeAll(sources);
                    }
                }
                this.waitingList.clear();
            }
        }
        if (this.world.provider.getWorldTime() % 2 == 0) {
            for (IEXPSource entry : this.sources) {
                if (entry != null) {
                    double offer = entry.getOfferedEXP();
                    if (offer > 0) {
                        for (double packetAmount = 1, i = 0; i < packetAmount; ++i) {
                            offer = entry.getOfferedEXP();
                            if (offer < 1) {
                                break;
                            }
                            final double removed = offer - this.emitEXPFrom(entry, offer);
                            if (removed <= 0) {
                                break;
                            }

                            entry.drawEXP(removed);
                        }
                    }

                }
            }
        }
    }

    public IEXPTile getTileEntity(BlockPos pos) {

        return this.chunkCoordinatesIEXPTileMap.get(pos);
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
        this.EXPSourceToEXPPathMap.clear();
        this.sources.clear();
        this.waitingList.clear();
        this.chunkCoordinatesIEXPTileMap.clear();
        this.chunkCoordinatesMap.clear();
        this.EXPTileTileEntityMap.clear();
    }

    static class EXPTarget {

        final IEXPTile tileEntity;
        final EnumFacing direction;

        EXPTarget(final IEXPTile tileEntity, final EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }

    }

    static class EXPBlockLink {

        final EnumFacing direction;

        EXPBlockLink(final EnumFacing direction) {
            this.direction = direction;
        }

    }

    static class EXPPath {

        final Set<IEXPConductor> conductors;
        final IEXPSink target;
        final EnumFacing targetDirection;

        EXPPath(IEXPSink sink, EnumFacing facing) {
            this.target = sink;
            this.conductors = new HashSet<>();
            this.targetDirection = facing;
        }

    }

    static class EXPPathMap {

        final Map<IEXPSource, List<EXPPath>> EXPnderPath;

        EXPPathMap() {
            this.EXPnderPath = new HashMap<>();
        }

        public void put(final IEXPSource par1, final List<EXPPath> par2) {
            this.EXPnderPath.put(par1, par2);
        }


        public boolean containsKey(final IEXPSource par1) {
            return this.EXPnderPath.containsKey(par1);
        }

        public List<EXPPath> get(final IEXPSource par1) {
            return this.EXPnderPath.get(par1);
        }


        public void remove(final IEXPSource par1) {
            this.EXPnderPath.remove(par1);
        }

        public void removeAll(final List<IEXPSource> par1) {
            for (IEXPSource iEXPSource : par1) {
                this.remove(iEXPSource);
            }
        }


        public List<IEXPSource> getSources(final IEXPAcceptor par1) {
            final List<IEXPSource> source = new ArrayList<>();
            for (final Map.Entry<IEXPSource, List<EXPPath>> entry : this.EXPnderPath.entrySet()) {
                if (source.contains(entry.getKey())) {
                    continue;
                }
                for (EXPPath path : entry.getValue()) {
                    if ((!(par1 instanceof IEXPConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof IEXPSink) || path.target != par1)) {
                        continue;
                    }
                    source.add(entry.getKey());
                }
            }
            return source;
        }

        public void clear() {
            this.EXPnderPath.clear();
        }

    }

    static class PathLogic {

        final List<IEXPTile> tiles;

        PathLogic() {
            this.tiles = new ArrayList<>();
        }

        public boolean contains(final IEXPTile par1) {
            return this.tiles.contains(par1);
        }

        public void add(final IEXPTile par1) {
            this.tiles.add(par1);
        }

        public void remove(final IEXPTile par1) {
            this.tiles.remove(par1);
        }

        public void clear() {
            this.tiles.clear();
        }

        public IEXPTile getRepreEXPntingTile() {
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

        public void onTileEntityAdded(final List<EXPTarget> around, final IEXPTile tile) {
            if (around.isEmpty() || this.paths.isEmpty()) {
                this.createNewPath(tile);
                return;
            }
            boolean found = false;
            final List<PathLogic> logics = new ArrayList<>();
            for (final PathLogic logic : this.paths) {
                if (logic.contains(tile)) {
                    found = true;
                    if (tile instanceof IEXPConductor) {
                        logics.add(logic);
                    }
                } else {
                    for (final EXPTarget target : around) {
                        if (logic.contains(target.tileEntity)) {
                            found = true;
                            logic.add(tile);
                            if (target.tileEntity instanceof IEXPConductor) {
                                logics.add(logic);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            if (logics.size() > 1 && tile instanceof IEXPConductor) {
                final PathLogic newLogic = new PathLogic();
                for (final PathLogic logic2 : logics) {
                    this.paths.remove(logic2);
                    for (final IEXPTile toMove : logic2.tiles) {
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

        public void onTileEntityRemoved(final IEXPTile par1) {
            if (this.paths.isEmpty()) {
                return;
            }
            final List<IEXPTile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); ++i) {
                final PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (final IEXPTile tile : toRecalculate) {
                this.onTileEntityAdded(EXPNetLocal.this.getValidReceivers(tile, true), tile);
            }
        }

        public void createNewPath(final IEXPTile par1) {
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

        public List<IEXPTile> getPathTiles() {
            final List<IEXPTile> tiles = new ArrayList<>();
            for (PathLogic path : this.paths) {
                final IEXPTile tile = path.getRepreEXPntingTile();
                if (tile != null) {
                    tiles.add(tile);
                }
            }
            return tiles;
        }

    }

}
