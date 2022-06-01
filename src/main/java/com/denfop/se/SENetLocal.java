package com.denfop.se;

import com.denfop.api.se.ISEAcceptor;
import com.denfop.api.se.ISEConductor;
import com.denfop.api.se.ISEEmitter;
import com.denfop.api.se.ISESink;
import com.denfop.api.se.ISESource;
import com.denfop.api.se.ISETile;
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

public class SENetLocal {

    private static EnumFacing[] directions;

    static {
        SENetLocal.directions = EnumFacing.values();
    }

    private final World world;
    private final SEPathMap SESourceToSEPathMap;
    private final Map<ISETile, BlockPos> chunkCoordinatesMap;
    private final Map<ISETile, TileEntity> SETileTileEntityMap;
    private final Map<BlockPos, ISETile> chunkCoordinatesISETileMap;
    private final List<ISESource> sources;
    private final WaitingList waitingList;

    public SENetLocal(final World world) {
        this.SESourceToSEPathMap = new SEPathMap();
        this.sources = new ArrayList<>();
        this.waitingList = new WaitingList();
        this.world = world;
        this.chunkCoordinatesISETileMap = new HashMap<>();
        this.chunkCoordinatesMap = new HashMap<>();
        this.SETileTileEntityMap = new HashMap<>();
    }

    public void addTile(ISETile tile1) {

        this.addTileEntity(getTileFromISE(tile1).getPos(), tile1);


    }

    public void addTileEntity(final BlockPos coords, final ISETile tile) {
        if (this.chunkCoordinatesISETileMap.containsKey(coords)) {
            return;
        }

        TileEntity te = getTileFromISE(tile);
        this.SETileTileEntityMap.put(tile, te);
        this.chunkCoordinatesMap.put(tile, coords);
        this.chunkCoordinatesISETileMap.put(coords, tile);
        this.update(coords.getX(), coords.getY(), coords.getZ());
        if (tile instanceof ISEAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), tile);
        }
        if (tile instanceof ISESource) {
            this.sources.add((ISESource) tile);
        }
    }


    public void removeTile(ISETile tile1) {

        this.removeTileEntity(tile1);

    }

    public void removeTileEntity(ISETile tile) {
        if (!this.SETileTileEntityMap.containsKey(tile)) {
            return;
        }
        final BlockPos coord = this.chunkCoordinatesMap.get(tile);
        this.chunkCoordinatesMap.remove(tile);
        this.SETileTileEntityMap.remove(tile, this.SETileTileEntityMap.get(tile));
        this.chunkCoordinatesISETileMap.remove(coord, tile);
        this.update(coord.getX(), coord.getY(), coord.getZ());
        if (tile instanceof ISEAcceptor) {
            this.SESourceToSEPathMap.removeAll(this.SESourceToSEPathMap.getSources((ISEAcceptor) tile));
            this.waitingList.onTileEntityRemoved(tile);
        }
        if (tile instanceof ISESource) {
            this.sources.remove((ISESource) tile);
            this.SESourceToSEPathMap.remove((ISESource) tile);
        }
    }

    public TileEntity getTileFromMap(ISETile tile) {
        return this.SETileTileEntityMap.get(tile);
    }

    public double emitSEFrom(final ISESource SESource, double amount) {
        List<SEPath> SEPaths = this.SESourceToSEPathMap.get(SESource);
        if (SEPaths == null) {
            this.SESourceToSEPathMap.put(SESource, this.discover(SESource));
            SEPaths = this.SESourceToSEPathMap.get(SESource);
        }
        if (amount > 0) {
            for (final SEPath SEPath : SEPaths) {
                if (amount <= 0) {
                    break;
                }
                final ISESink SESink = SEPath.target;
                if (SESink.getDemandedSE() <= 0.0) {
                    continue;
                }
                double SEConsumed = 0;
                double SEProvided = Math.floor(Math.round(amount));
                double adding = Math.min(SEProvided, SESink.getDemandedSE());
                if (adding <= 0.0D) {
                    adding = SESink.getDemandedSE();
                }
                if (adding <= 0.0D) {
                    continue;
                }
                double SEReturned = SESink.injectSE(SEPath.targetDirection, adding, 0);
                if (SEReturned >= SEProvided) {
                    SEReturned = SEProvided;
                }
                SEConsumed += adding;
                SEConsumed -= SEReturned;

                amount -= SEConsumed;
                amount = Math.max(0, amount);

            }
        }

        return amount;
    }

    public TileEntity getTileFromISE(ISETile tile) {
        if (tile instanceof TileEntity) {
            return (TileEntity) tile;
        }
        if (tile instanceof ILocatable) {
            return this.world.getTileEntity(((ILocatable) tile).getPosition());
        }

        return null;
    }

    private List<SEPath> discover(final ISESource emitter) {
        final Map<ISETile, SEBlockLink> reachedTileEntities = new HashMap<>();
        final LinkedList<ISETile> tileEntitiesToCheck = new LinkedList<>();

        tileEntitiesToCheck.add(emitter);


        while (!tileEntitiesToCheck.isEmpty()) {
            final ISETile currentTileEntity = tileEntitiesToCheck.remove();
            final List<SETarget> validReceivers = this.getValidReceivers(currentTileEntity, false);
            for (final SETarget validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {

                    if (reachedTileEntities.containsKey(validReceiver.tileEntity)) {
                        continue;
                    }
                    reachedTileEntities.put(validReceiver.tileEntity, new SEBlockLink(validReceiver.direction));
                    if (!(validReceiver.tileEntity instanceof ISEConductor)) {
                        continue;
                    }
                    tileEntitiesToCheck.remove(validReceiver.tileEntity);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }


        }
        final List<SEPath> SEPaths = new LinkedList<>();
        for (final Map.Entry<ISETile, SEBlockLink> entry : reachedTileEntities.entrySet()) {
            ISETile tileEntity = entry.getKey();
            if ((tileEntity instanceof ISESink)) {
                SEBlockLink SEBlockLink = entry.getValue();
                final SEPath SEPath = new SEPath((ISESink) tileEntity, SEBlockLink.direction);
                if (emitter != null) {
                    while (true) {
                        BlockPos te = this.chunkCoordinatesMap.get(tileEntity);
                        if (SEBlockLink != null) {
                            tileEntity = this.getTileEntity(te.offset(SEBlockLink.direction));
                        }
                        if (tileEntity == emitter) {
                            break;
                        }
                        if (!(tileEntity instanceof ISEConductor)) {
                            break;
                        }
                        final ISEConductor SEConductor = (ISEConductor) tileEntity;
                        SEPath.conductors.add(SEConductor);
                        SEBlockLink = reachedTileEntities.get(tileEntity);
                        if (SEBlockLink != null) {
                            continue;
                        }
                        IC2.platform.displayError("An SE network pathfinding entry is corrupted.\nThis could happen due to " +
                                "incorrect Minecraft behavior or a bug.\n\n(Technical information: SEBlockLink, tile " +
                                "entities below)\nE: " + emitter + " (" + te.getX() + "," + te.getY() + "," + te

                                .getZ() + ")\n" + "C: " + tileEntity + " (" + te.getX() + "," + te

                                .getY() + "," + te

                                .getZ() + ")\n" + "R: " + SEPath.target + " (" + this.SETileTileEntityMap
                                .get(SEPath.target)
                                .getPos()
                                .getX() + "," + getTileFromMap(SEPath.target).getPos().getY() + "," + getTileFromISE(
                                SEPath.target).getPos().getZ() + ")");
                    }
                }
                SEPaths.add(SEPath);
            }
        }
        return SEPaths;
    }

    public ISETile getNeighbor(final ISETile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        return this.getTileEntity(this.SETileTileEntityMap.get(tile).getPos().offset(dir));
    }

    private List<SETarget> getValidReceivers(final ISETile emitter, final boolean reverse) {
        final List<SETarget> validReceivers = new LinkedList<>();

        for (final EnumFacing direction : SENetLocal.directions) {
            final ISETile target2 = getNeighbor(emitter, direction);
            if (target2 != null) {
                final EnumFacing inverseDirection2 = direction.getOpposite();
                if (reverse) {
                    if (emitter instanceof ISEAcceptor && target2 instanceof ISEEmitter) {
                        final ISEEmitter sender2 = (ISEEmitter) target2;
                        final ISEAcceptor receiver2 = (ISEAcceptor) emitter;
                        if (sender2.emitsSETo(receiver2, inverseDirection2) && receiver2.acceptsSEFrom(
                                sender2,
                                direction
                        )) {
                            validReceivers.add(new SETarget(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof ISEEmitter && target2 instanceof ISEAcceptor) {
                    final ISEEmitter sender2 = (ISEEmitter) emitter;
                    final ISEAcceptor receiver2 = (ISEAcceptor) target2;
                    if (sender2.emitsSETo(receiver2, direction) && receiver2.acceptsSEFrom(
                            sender2,
                            inverseDirection2
                    )) {
                        validReceivers.add(new SETarget(target2, inverseDirection2));
                    }
                }
            }
        }


        return validReceivers;
    }

    public List<ISESource> discoverFirstPathOrSources(final ISETile par1) {
        final Set<ISETile> reached = new HashSet<>();
        final List<ISESource> result = new ArrayList<>();
        final List<ISETile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final ISETile tile = workList.remove(0);
            final TileEntity te = this.SETileTileEntityMap.get(tile);
            if (!te.isInvalid()) {
                final List<SETarget> targets = this.getValidReceivers(tile, true);
                for (SETarget SETarget : targets) {
                    final ISETile target = SETarget.tileEntity;
                    if (target != par1) {
                        if (!reached.contains(target)) {
                            reached.add(target);
                            if (target instanceof ISESource) {
                                result.add((ISESource) target);
                            } else if (target instanceof ISEConductor) {
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
                final List<ISETile> tiles = this.waitingList.getPathTiles();
                for (final ISETile tile : tiles) {
                    final List<ISESource> sources = this.discoverFirstPathOrSources(tile);
                    if (sources.size() > 0) {
                        this.SESourceToSEPathMap.removeAll(sources);
                    }
                }
                this.waitingList.clear();
            }
        }
        if (this.world.provider.getWorldTime() % 2 == 0) {
            for (ISESource entry : this.sources) {
                if (entry != null) {
                    double offer = entry.getOfferedSE();
                    if (offer > 0) {
                        for (double packetAmount = 1, i = 0; i < packetAmount; ++i) {
                            offer = entry.getOfferedSE();
                            if (offer < 1) {
                                break;
                            }
                            final double removed = offer - this.emitSEFrom(entry, offer);
                            if (removed <= 0) {
                                break;
                            }

                            entry.drawSE(removed);
                        }
                    }

                }
            }
        }
    }

    public ISETile getTileEntity(BlockPos pos) {

        return this.chunkCoordinatesISETileMap.get(pos);
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
        this.SESourceToSEPathMap.clear();
        this.sources.clear();
        this.waitingList.clear();
        this.chunkCoordinatesISETileMap.clear();
        this.chunkCoordinatesMap.clear();
        this.SETileTileEntityMap.clear();
    }

    static class SETarget {

        final ISETile tileEntity;
        final EnumFacing direction;

        SETarget(final ISETile tileEntity, final EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }

    }

    static class SEBlockLink {

        final EnumFacing direction;

        SEBlockLink(final EnumFacing direction) {
            this.direction = direction;
        }

    }

    static class SEPath {

        final Set<ISEConductor> conductors;
        final ISESink target;
        final EnumFacing targetDirection;

        SEPath(ISESink sink, EnumFacing facing) {
            this.target = sink;
            this.conductors = new HashSet<>();
            this.targetDirection = facing;
        }

    }

    static class SEPathMap {

        final Map<ISESource, List<SEPath>> senderPath;

        SEPathMap() {
            this.senderPath = new HashMap<>();
        }

        public void put(final ISESource par1, final List<SEPath> par2) {
            this.senderPath.put(par1, par2);
        }


        public boolean containsKey(final ISESource par1) {
            return this.senderPath.containsKey(par1);
        }

        public List<SEPath> get(final ISESource par1) {
            return this.senderPath.get(par1);
        }


        public void remove(final ISESource par1) {
            this.senderPath.remove(par1);
        }

        public void removeAll(final List<ISESource> par1) {
            for (ISESource iSESource : par1) {
                this.remove(iSESource);
            }
        }


        public List<ISESource> getSources(final ISEAcceptor par1) {
            final List<ISESource> source = new ArrayList<>();
            for (final Map.Entry<ISESource, List<SEPath>> entry : this.senderPath.entrySet()) {
                if (source.contains(entry.getKey())) {
                    continue;
                }
                for (SEPath path : entry.getValue()) {
                    if ((!(par1 instanceof ISEConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof ISESink) || path.target != par1)) {
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

        final List<ISETile> tiles;

        PathLogic() {
            this.tiles = new ArrayList<>();
        }

        public boolean contains(final ISETile par1) {
            return this.tiles.contains(par1);
        }

        public void add(final ISETile par1) {
            this.tiles.add(par1);
        }

        public void remove(final ISETile par1) {
            this.tiles.remove(par1);
        }

        public void clear() {
            this.tiles.clear();
        }

        public ISETile getRepresentingTile() {
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

        public void onTileEntityAdded(final List<SETarget> around, final ISETile tile) {
            if (around.isEmpty() || this.paths.isEmpty()) {
                this.createNewPath(tile);
                return;
            }
            boolean found = false;
            final List<PathLogic> logics = new ArrayList<>();
            for (final PathLogic logic : this.paths) {
                if (logic.contains(tile)) {
                    found = true;
                    if (tile instanceof ISEConductor) {
                        logics.add(logic);
                    }
                } else {
                    for (final SETarget target : around) {
                        if (logic.contains(target.tileEntity)) {
                            found = true;
                            logic.add(tile);
                            if (target.tileEntity instanceof ISEConductor) {
                                logics.add(logic);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            if (logics.size() > 1 && tile instanceof ISEConductor) {
                final PathLogic newLogic = new PathLogic();
                for (final PathLogic logic2 : logics) {
                    this.paths.remove(logic2);
                    for (final ISETile toMove : logic2.tiles) {
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

        public void onTileEntityRemoved(final ISETile par1) {
            if (this.paths.isEmpty()) {
                return;
            }
            final List<ISETile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); ++i) {
                final PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (final ISETile tile : toRecalculate) {
                this.onTileEntityAdded(SENetLocal.this.getValidReceivers(tile, true), tile);
            }
        }

        public void createNewPath(final ISETile par1) {
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

        public List<ISETile> getPathTiles() {
            final List<ISETile> tiles = new ArrayList<>();
            for (PathLogic path : this.paths) {
                final ISETile tile = path.getRepresentingTile();
                if (tile != null) {
                    tiles.add(tile);
                }
            }
            return tiles;
        }

    }

}
