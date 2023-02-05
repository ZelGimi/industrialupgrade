package com.denfop.se;

import com.denfop.api.energy.SystemTick;
import com.denfop.api.se.ISEAcceptor;
import com.denfop.api.se.ISEConductor;
import com.denfop.api.se.ISEEmitter;
import com.denfop.api.se.ISESink;
import com.denfop.api.se.ISESource;
import com.denfop.api.se.ISETile;
import ic2.api.info.ILocatable;
import ic2.core.IC2;
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


    private final World world;
    private final SEPathMap SESourceToSEPathMap;
    private final Map<BlockPos, ISETile> chunkCoordinatesISETileMap;
    private final WaitingList waitingList;


    SENetLocal(final World world) {
        this.SESourceToSEPathMap = new SEPathMap();
        this.waitingList = new WaitingList();
        this.world = world;
        this.chunkCoordinatesISETileMap = new HashMap<>();
    }


    public void addTile(ISETile tile1) {


        this.addTileEntity(getTileFromISE(tile1).getPos(), tile1);


    }

    public void addTile(ISETile tile, TileEntity tileentity) {

        final BlockPos coords = tileentity.getPos();
        if (this.chunkCoordinatesISETileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesISETileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof ISEAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), (ISEAcceptor) tile);
        }
        if (tile instanceof ISESource) {
            SESourceToSEPathMap.senderPath.add(new SystemTick<>((ISESource) tile, null));

        }


    }

    public BlockPos getPos(final ISETile tile) {
        if (tile == null) {
            return null;
        }
        return tile.getBlockPos();
    }

    public void addTileEntity(final BlockPos coords, final ISETile tile) {
        if (this.chunkCoordinatesISETileMap.containsKey(coords)) {
            return;
        }

        this.chunkCoordinatesISETileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof ISEAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), (ISEAcceptor) tile);
        }
        if (tile instanceof ISESource) {
            SESourceToSEPathMap.senderPath.add(new SystemTick<>((ISESource) tile, null));

        }
    }

    public void removeTile(ISETile tile1) {

        this.removeTileEntity(tile1);

    }


    public void removeTileEntity(ISETile tile) {
        if (!this.chunkCoordinatesISETileMap.containsKey(tile.getBlockPos())) {
            return;
        }
        final BlockPos coord = tile.getBlockPos();
        this.chunkCoordinatesISETileMap.remove(coord, tile);
        this.update(coord);
        if (tile instanceof ISEAcceptor) {
            this.SESourceToSEPathMap.removeAll(this.SESourceToSEPathMap.getSources((ISEAcceptor) tile));
            this.waitingList.onTileEntityRemoved((ISEAcceptor) tile);
        }
        if (tile instanceof ISESource) {
            this.SESourceToSEPathMap.remove((ISESource) tile);
        }
    }

    public double emitSEFrom(final ISESource SESource, double amount, final SystemTick<ISESource, SEPath> tick) {
        List<SEPath> SEPaths = tick.getList();

        if (SEPaths == null) {
            SEPaths = this.discover(SESource);
            tick.setList(SEPaths);
        }
        if (amount > 0) {
            for (final SEPath SEPath : SEPaths) {
                if (amount <= 0) {
                    break;
                }
                final ISESink SESink = SEPath.target;
                double demandedSE = SESink.getDemandedSE();
                if (demandedSE <= 0.0) {
                    continue;
                }
                double SEProvided = amount;
                double adding;


                adding = Math.min(SEProvided, demandedSE);
                if (adding <= 0.0D) {
                    continue;
                }


                adding -= SESink.injectSE(SEPath.targetDirection, adding, 0);
                SEPath.totalSEConducted = (long) adding;
                amount -= adding;
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

    public List<SEPath> discover(final ISESource emitter) {
        final Map<ISEConductor, EnumFacing> reachedTileEntities = new HashMap<>();
        final List<ISETile> tileEntitiesToCheck = new ArrayList<>();
        final List<SEPath> SEPaths = new ArrayList<>();

        tileEntitiesToCheck.add(emitter);

        while (!tileEntitiesToCheck.isEmpty()) {
            final ISETile currentTileEntity = tileEntitiesToCheck.remove(0);
            final List<SETarget> validReceivers = this.getValidReceivers(currentTileEntity, false);
            for (final SETarget validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {
                    if (validReceiver.tileEntity instanceof ISESink) {
                        SEPaths.add(new SEPath((ISESink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }
                    if (reachedTileEntities.containsKey((ISEConductor) validReceiver.tileEntity)) {
                        continue;
                    }

                    reachedTileEntities.put((ISEConductor) validReceiver.tileEntity, validReceiver.direction);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }


        }
        for (SEPath SEPath : SEPaths) {
            ISETile tileEntity = SEPath.target;
            EnumFacing SEBlockLink = SEPath.targetDirection;
            if (emitter != null) {
                while (tileEntity != emitter) {
                    BlockPos te = tileEntity.getBlockPos();
                    if (SEBlockLink != null && te != null) {
                        tileEntity = this.getTileEntity(te.offset(SEBlockLink));
                    }
                    if (!(tileEntity instanceof ISEConductor)) {
                        break;
                    }
                    final ISEConductor SEConductor = (ISEConductor) tileEntity;
                    SEPath.conductors.add(SEConductor);
                    if (SEConductor.getConductorBreakdownSolariumEnergy() - 1 < SEPath.getMin()) {
                        SEPath.setMin(SEConductor.getConductorBreakdownSolariumEnergy() - 1);
                    }
                    SEBlockLink = reachedTileEntities.get(tileEntity);
                    if (SEBlockLink != null) {
                        continue;
                    }
                    assert te != null;
                    IC2.platform.displayError("An SE network pathfinding entry is corrupted.\nThis could happen due to " +
                            "incorrect Minecraft behavior or a bug.\n\n(Technical information: SEBlockLink, tile " +
                            "entities below)\nE: " + emitter + " (" + te.getX() + "," + te.getY() + "," + te

                            .getZ() + ")\n" + "C: " + tileEntity + " (" + te.getX() + "," + te

                            .getY() + "," + te

                            .getZ() + ")\n" + "R: " + SEPath.target + " (" + SEPath.target
                            .getBlockPos()
                            .getX() + "," + SEPath.target.getBlockPos().getY() + "," + SEPath.target.getBlockPos().getZ() + ")");
                }
            }
        }
        return SEPaths;
    }

    public ISETile getNeighbor(final ISETile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        return this.getTileEntity(tile.getBlockPos().offset(dir));
    }

    private List<SETarget> getValidReceivers(final ISETile emitter, final boolean reverse) {
        final List<SETarget> validReceivers = new LinkedList<>();

        for (final EnumFacing direction : EnumFacing.values()) {
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
        //

        return validReceivers;
    }

    public List<ISESource> discoverFirstPathOrSources(final ISETile par1) {
        final Set<ISETile> reached = new HashSet<>();
        final List<ISESource> result = new ArrayList<>();
        final List<ISETile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final ISETile tile = workList.remove(0);
            final TileEntity te = tile.getTile();
            if (te == null) {
                continue;
            }
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


    public void onTickEnd() {
        if (this.waitingList.hasWork()) {
            final List<ISETile> tiles = this.waitingList.getPathTiles();
            for (final ISETile tile : tiles) {
                final List<ISESource> sources = this.discoverFirstPathOrSources(tile);
                if (sources.size() > 0) {
                    this.SESourceToSEPathMap.removeAllSource1(sources);
                }
            }
            this.waitingList.clear();

        }
        try {
            for (SystemTick<ISESource, SEPath> tick : this.SESourceToSEPathMap.senderPath) {
                final ISESource entry = tick.getSource();
                if (entry != null) {
                    double offered = entry.getOfferedSE();

                    if (offered > 0) {
                        for (double i = 0; i < getPacketAmount(); ++i) {
                            final double removed = offered - this.emitSEFrom(entry, offered, tick);
                            entry.drawSE(removed);
                            if (removed <= 0) {
                                break;
                            }

                        }
                    }


                }
            }
        } catch (Exception ignored) {
        }


    }

    private double getPacketAmount() {

        return 1.0D;
    }

    public ISETile getTileEntity(BlockPos pos) {

        return this.chunkCoordinatesISETileMap.get(pos);
    }


    public void update(BlockPos pos) {
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos
                    .offset(dir);
            final ISETile tile = this.chunkCoordinatesISETileMap.get(pos1);
            if (tile != null) {
                if (tile instanceof ISEConductor) {
                    ((ISEConductor) tile).update_render();
                }
            }

        }
    }

    public void onUnload() {
        this.SESourceToSEPathMap.clear();
        this.waitingList.clear();
        this.chunkCoordinatesISETileMap.clear();
    }

    static class SETarget {

        final ISETile tileEntity;
        final EnumFacing direction;

        SETarget(final ISETile tileEntity, final EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }

    }

    public static class SEPath {

        final List<ISEConductor> conductors;
        final ISESink target;
        final EnumFacing targetDirection;
        long totalSEConducted;
        double min = Double.MAX_VALUE;

        SEPath(ISESink sink, EnumFacing facing) {
            this.target = sink;
            this.conductors = new ArrayList<>();
            this.totalSEConducted = 0L;
            this.targetDirection = facing;


        }

        public List<ISEConductor> getConductors() {
            return conductors;
        }


        public double getMin() {
            return min;
        }

        public void setMin(final double min) {
            this.min = min;
        }


    }


    static class SEPathMap {

        final List<SystemTick<ISESource, SEPath>> senderPath;

        SEPathMap() {
            this.senderPath = new ArrayList<>();
        }

        public void put(final ISESource par1, final List<SEPath> par2) {
            this.senderPath.add(new SystemTick<>(par1, par2));
        }


        public boolean containsKey(final SystemTick<ISESource, SEPath> par1) {
            return this.senderPath.contains(par1);
        }

        public boolean containsKey(final ISESource par1) {
            return this.senderPath.contains(new SystemTick<ISESource, SEPath>(par1, null));
        }


        public void remove1(final ISESource par1) {

            for (SystemTick<ISESource, SEPath> ticks : this.senderPath) {
                if (ticks.getSource() == par1) {
                    ticks.setList(null);
                    break;
                }
            }
        }

        public void remove(final ISESource par1) {
            this.senderPath.remove(new SystemTick<ISESource, SEPath>(par1, null));
        }

        public void remove(final SystemTick<ISESource, SEPath> par1) {
            this.senderPath.remove(par1);
        }

        public void removeAll(final List<SystemTick<ISESource, SEPath>> par1) {
            if (par1 == null) {
                return;
            }
            for (SystemTick<ISESource, SEPath> iSESource : par1) {
                iSESource.setList(null);
            }
        }

        public void removeAllSource1(final List<ISESource> par1) {
            if (par1 == null) {
                return;
            }
            for (ISESource iSESource : par1) {
                this.remove1(iSESource);
            }
        }

        public List<SystemTick<ISESource, SEPath>> getSources(final ISEAcceptor par1) {
            final List<SystemTick<ISESource, SEPath>> source = new ArrayList<>();
            for (final SystemTick<ISESource, SEPath> entry : this.senderPath) {
                if (source.contains(entry)) {
                    continue;
                }
                if (entry.getList() != null) {
                    for (SEPath path : entry.getList()) {
                        if ((!(par1 instanceof ISEConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof ISESink) || path.target != par1)) {
                            continue;
                        }
                        source.add(entry);
                    }
                }
            }
            return source;
        }


        public void clear() {
            for (SystemTick<ISESource, SEPath> entry : this.senderPath) {
                List<SEPath> list = entry.getList();
                if (list != null) {
                    for (SEPath SEPath : list) {
                        SEPath.conductors.clear();
                    }
                }

            }
            this.senderPath.clear();
        }


        public SystemTick<ISESource, SEPath> get(ISESource tileEntity) {
            for (SystemTick<ISESource, SEPath> entry : this.senderPath) {
                if (entry.getSource() == tileEntity) {
                    return entry;
                }
            }
            return null;
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

        public void onTileEntityAdded(final List<SETarget> around, final ISEAcceptor tile) {
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
                }
                this.paths.add(newLogic);
            }
            if (!found) {
                this.createNewPath(tile);
            }
        }

        public void onTileEntityRemoved(final ISEAcceptor par1) {
            if (this.paths.isEmpty()) {
                return;
            }

            List<ISETile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); i++) {
                PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (final ISETile tile : toRecalculate) {
                this.onTileEntityAdded(SENetLocal.this.getValidReceivers(tile, true), (ISEAcceptor) tile);
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
