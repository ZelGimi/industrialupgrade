package com.denfop.cool;

import com.denfop.api.cool.ICoolAcceptor;
import com.denfop.api.cool.ICoolConductor;
import com.denfop.api.cool.ICoolEmitter;
import com.denfop.api.cool.ICoolSink;
import com.denfop.api.cool.ICoolSource;
import com.denfop.api.cool.ICoolTile;
import com.denfop.api.energy.SystemTick;
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

public class CoolNetLocal {


    private final World world;
    private final CoolPathMap CoolSourceToCoolPathMap;
    private final Map<ICoolTile, BlockPos> chunkCoordinatesMap;
    private final Map<ICoolTile, TileEntity> CoolTileTileEntityMap;
    private final Map<BlockPos, ICoolTile> chunkCoordinatesICoolTileMap;
    private final WaitingList waitingList;


    CoolNetLocal(final World world) {
        this.CoolSourceToCoolPathMap = new CoolPathMap();
        this.waitingList = new WaitingList();
        this.world = world;
        this.chunkCoordinatesICoolTileMap = new HashMap<>();
        this.chunkCoordinatesMap = new HashMap<>();
        this.CoolTileTileEntityMap = new HashMap<>();
    }


    public void addTile(ICoolTile tile1) {


        this.addTileEntity(getTileFromICool(tile1).getPos(), tile1);


    }

    public void addTile(ICoolTile tile, TileEntity tileentity) {

        final BlockPos coords = tileentity.getPos();
        if (this.chunkCoordinatesICoolTileMap.containsKey(coords)) {
            return;
        }
        this.CoolTileTileEntityMap.put(tile, tileentity);
        this.chunkCoordinatesMap.put(tile, coords);
        this.chunkCoordinatesICoolTileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof ICoolAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), (ICoolAcceptor) tile);
        }
        if (tile instanceof ICoolSource) {
            CoolSourceToCoolPathMap.senderPath.add(new SystemTick<>((ICoolSource) tile, null));

        }


    }

    public BlockPos getPos(final ICoolTile tile) {
        return this.chunkCoordinatesMap.get(tile);
    }

    public void addTileEntity(final BlockPos coords, final ICoolTile tile) {
        if (this.chunkCoordinatesICoolTileMap.containsKey(coords)) {
            return;
        }

        TileEntity te = getTileFromICool(tile);
        this.CoolTileTileEntityMap.put(tile, te);
        this.chunkCoordinatesMap.put(tile, coords);
        this.chunkCoordinatesICoolTileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof ICoolAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), (ICoolAcceptor) tile);
        }
        if (tile instanceof ICoolSource) {
            CoolSourceToCoolPathMap.senderPath.add(new SystemTick<>((ICoolSource) tile, null));

        }
    }

    public void removeTile(ICoolTile tile1) {

        this.removeTileEntity(tile1);

    }


    public void removeTileEntity(ICoolTile tile) {
        if (!this.CoolTileTileEntityMap.containsKey(tile)) {
            return;
        }
        final BlockPos coord = this.chunkCoordinatesMap.get(tile);
        this.chunkCoordinatesMap.remove(tile);
        this.CoolTileTileEntityMap.remove(tile, this.CoolTileTileEntityMap.get(tile));
        this.chunkCoordinatesICoolTileMap.remove(coord, tile);
        this.update(coord);
        if (tile instanceof ICoolAcceptor) {
            this.CoolSourceToCoolPathMap.removeAll(this.CoolSourceToCoolPathMap.getSources((ICoolAcceptor) tile));
            this.waitingList.onTileEntityRemoved((ICoolAcceptor) tile);
        }
        if (tile instanceof ICoolSource) {
            this.CoolSourceToCoolPathMap.remove((ICoolSource) tile);
        }
    }

    public TileEntity getTileFromMap(ICoolTile tile) {
        return this.CoolTileTileEntityMap.get(tile);
    }

    public double emitCoolFrom(final ICoolSource CoolSource, double amount, final SystemTick<ICoolSource, CoolPath> tick) {
        List<CoolPath> CoolPaths = tick.getList();

        if (CoolPaths == null) {
            CoolPaths = this.discover(CoolSource);
            tick.setList(CoolPaths);
        }
        boolean transmit = false;
        if (amount > 0) {
            for (final CoolPath CoolPath : CoolPaths) {
                final ICoolSink CoolSink = CoolPath.target;
                double demandedCool = CoolSink.getDemandedCool();
                if (demandedCool <= 0.0) {
                    continue;
                }
                double CoolProvided = amount;
                double adding;


                adding = Math.min(CoolProvided, demandedCool);
                if (adding <= 0.0D) {
                    continue;
                }


                adding -= CoolSink.injectCool(CoolPath.targetDirection, adding, 0);
                CoolPath.totalCoolConducted = (long) adding;
                transmit = true;

                if (adding > CoolPath.min) {
                    for (ICoolConductor CoolConductor : CoolPath.conductors) {
                        if (CoolConductor.getConductorBreakdownCold() < adding) {
                            CoolConductor.removeConductor();
                        } else {
                            break;
                        }
                    }
                }


            }
        }
        if (!transmit && CoolSource.isAllowed()) {
            CoolSource.setAllowed(false);
        }

        return amount;
    }

    public double emitCoolFromNotAllowed(
            final ICoolSource CoolSource, double amount,
            final SystemTick<ICoolSource, CoolPath> tick
    ) {
        List<CoolPath> CoolPaths = tick.getList();

        if (CoolPaths == null) {
            CoolPaths = this.discover(CoolSource);
            tick.setList(CoolPaths);
        }

        for (final CoolPath CoolPath : CoolPaths) {
            final ICoolSink CoolSink = CoolPath.target;
            double demandedCool = CoolSink.getDemandedCool();
            if (demandedCool <= 0.0) {
                continue;
            }
            if (CoolSink.needCooling()) {
                CoolSource.setAllowed(true);
            }
            double CoolProvided = amount;
            double adding;


            adding = Math.min(CoolProvided, demandedCool);
            if (adding <= 0.0D) {
                continue;
            }


            adding -= CoolSink.injectCool(CoolPath.targetDirection, adding, 0);
            CoolPath.totalCoolConducted = (long) adding;


            if (adding > CoolPath.min) {
                for (ICoolConductor CoolConductor : CoolPath.conductors) {
                    if (CoolConductor.getConductorBreakdownCold() < adding) {
                        CoolConductor.removeConductor();
                    } else {
                        break;
                    }
                }
            }


        }


        return amount;
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

    public List<CoolPath> discover(final ICoolSource emitter) {
        final Map<ICoolConductor, EnumFacing> reachedTileEntities = new HashMap<>();
        final List<ICoolTile> tileEntitiesToCheck = new ArrayList<>();
        final List<CoolPath> CoolPaths = new ArrayList<>();

        tileEntitiesToCheck.add(emitter);

        while (!tileEntitiesToCheck.isEmpty()) {
            final ICoolTile currentTileEntity = tileEntitiesToCheck.remove(0);
            final List<CoolTarget> validReceivers = this.getValidReceivers(currentTileEntity, false);
            for (final CoolTarget validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {
                    if (validReceiver.tileEntity instanceof ICoolSink) {
                        CoolPaths.add(new CoolPath((ICoolSink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }
                    if (reachedTileEntities.containsKey((ICoolConductor) validReceiver.tileEntity)) {
                        continue;
                    }

                    reachedTileEntities.put((ICoolConductor) validReceiver.tileEntity, validReceiver.direction);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }


        }
        for (CoolPath CoolPath : CoolPaths) {
            ICoolTile tileEntity = CoolPath.target;
            EnumFacing CoolBlockLink = CoolPath.targetDirection;
            if (emitter != null) {
                while (tileEntity != emitter) {
                    BlockPos te = this.chunkCoordinatesMap.get(tileEntity);
                    if (CoolBlockLink != null && te != null) {
                        tileEntity = this.getTileEntity(te.offset(CoolBlockLink));
                    }
                    if (!(tileEntity instanceof ICoolConductor)) {
                        break;
                    }
                    final ICoolConductor CoolConductor = (ICoolConductor) tileEntity;
                    CoolPath.conductors.add(CoolConductor);
                    if (CoolConductor.getConductorBreakdownCold() - 1 < CoolPath.getMin()) {
                        CoolPath.setMin(CoolConductor.getConductorBreakdownCold() - 1);
                    }
                    CoolBlockLink = reachedTileEntities.get(tileEntity);
                    if (CoolBlockLink != null) {
                        continue;
                    }
                    assert te != null;
                    IC2.platform.displayError("An Cool network pathfinding entry is corrupted.\nThis could happen due to " +
                            "incorrect Minecraft behavior or a bug.\n\n(Technical information: CoolBlockLink, tile " +
                            "entities below)\nE: " + emitter + " (" + te.getX() + "," + te.getY() + "," + te

                            .getZ() + ")\n" + "C: " + tileEntity + " (" + te.getX() + "," + te

                            .getY() + "," + te

                            .getZ() + ")\n" + "R: " + CoolPath.target + " (" + this.CoolTileTileEntityMap
                            .get(CoolPath.target)
                            .getPos()
                            .getX() + "," + getTileFromMap(CoolPath.target).getPos().getY() + "," + getTileFromICool(
                            CoolPath.target).getPos().getZ() + ")");
                }
            }
        }
        return CoolPaths;
    }

    public ICoolTile getNeighbor(final ICoolTile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        final TileEntity tile1 = this.CoolTileTileEntityMap.get(tile);
        if (tile1 == null) {
            return null;
        }
        return this.getTileEntity(tile1.getPos().offset(dir));
    }

    private List<CoolTarget> getValidReceivers(final ICoolTile emitter, final boolean reverse) {
        final List<CoolTarget> validReceivers = new LinkedList<>();

        for (final EnumFacing direction : EnumFacing.values()) {
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
                            validReceivers.add(new CoolTarget(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof ICoolEmitter && target2 instanceof ICoolAcceptor) {
                    final ICoolEmitter sender2 = (ICoolEmitter) emitter;
                    final ICoolAcceptor receiver2 = (ICoolAcceptor) target2;
                    if (sender2.emitsCoolTo(receiver2, direction) && receiver2.acceptsCoolFrom(
                            sender2,
                            inverseDirection2
                    )) {
                        validReceivers.add(new CoolTarget(target2, inverseDirection2));
                    }
                }
            }

        }
        //

        return validReceivers;
    }

    public List<ICoolSource> discoverFirstPathOrSources(final ICoolTile par1) {
        final Set<ICoolTile> reached = new HashSet<>();
        final List<ICoolSource> result = new ArrayList<>();
        final List<ICoolTile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final ICoolTile tile = workList.remove(0);
            final TileEntity te = this.CoolTileTileEntityMap.get(tile);
            if (te == null) {
                continue;
            }
            if (!te.isInvalid()) {
                final List<CoolTarget> targets = this.getValidReceivers(tile, true);
                for (CoolTarget CoolTarget : targets) {
                    final ICoolTile target = CoolTarget.tileEntity;
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


    public void onTickEnd() {
        if (this.waitingList.hasWork()) {
            final List<ICoolTile> tiles = this.waitingList.getPathTiles();
            for (final ICoolTile tile : tiles) {
                final List<ICoolSource> sources = this.discoverFirstPathOrSources(tile);
                if (sources.size() > 0) {
                    this.CoolSourceToCoolPathMap.removeAllSource1(sources);
                }
            }
            this.waitingList.clear();

        }
        try {
            for (SystemTick<ICoolSource, CoolPath> tick : this.CoolSourceToCoolPathMap.senderPath) {
                final ICoolSource entry = tick.getSource();
                if (entry != null) {
                    double offered = entry.getOfferedCool();

                    if (offered > 0 && entry.isAllowed()) {
                        for (double i = 0; i < getPacketAmount(); ++i) {
                            final double removed = offered - this.emitCoolFrom(entry, offered, tick);
                            if (removed <= 0) {
                                break;
                            }
                        }
                    } else if (!entry.isAllowed()) {
                        for (double i = 0; i < getPacketAmount(); ++i) {
                            final double removed = offered - this.emitCoolFromNotAllowed(entry, offered, tick);
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

    public ICoolTile getTileEntity(BlockPos pos) {

        return this.chunkCoordinatesICoolTileMap.get(pos);
    }


    public void update(BlockPos pos) {
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos
                    .offset(dir);
            final ICoolTile tile = this.chunkCoordinatesICoolTileMap.get(pos1);
            if (tile != null) {
                if (tile instanceof ICoolConductor) {
                    ((ICoolConductor) tile).update_render();
                }
            }

        }
    }

    public void onUnload() {
        this.CoolSourceToCoolPathMap.clear();
        this.waitingList.clear();
        this.chunkCoordinatesICoolTileMap.clear();
        this.chunkCoordinatesMap.clear();
        this.CoolTileTileEntityMap.clear();
    }

    static class CoolTarget {

        final ICoolTile tileEntity;
        final EnumFacing direction;

        CoolTarget(final ICoolTile tileEntity, final EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }

    }

    public static class CoolPath {

        final List<ICoolConductor> conductors;
        final ICoolSink target;
        final EnumFacing targetDirection;
        long totalCoolConducted;
        double min = Double.MAX_VALUE;

        CoolPath(ICoolSink sink, EnumFacing facing) {
            this.target = sink;
            this.conductors = new ArrayList<>();
            this.totalCoolConducted = 0L;
            this.targetDirection = facing;


        }

        public List<ICoolConductor> getConductors() {
            return conductors;
        }


        public double getMin() {
            return min;
        }

        public void setMin(final double min) {
            this.min = min;
        }


    }


    static class CoolPathMap {

        final List<SystemTick<ICoolSource, CoolPath>> senderPath;

        CoolPathMap() {
            this.senderPath = new ArrayList<>();
        }

        public void put(final ICoolSource par1, final List<CoolPath> par2) {
            this.senderPath.add(new SystemTick<>(par1, par2));
        }


        public boolean containsKey(final SystemTick<ICoolSource, CoolPath> par1) {
            return this.senderPath.contains(par1);
        }

        public boolean containsKey(final ICoolSource par1) {
            return this.senderPath.contains(new SystemTick<ICoolSource, CoolPath>(par1, null));
        }


        public void remove1(final ICoolSource par1) {

            for (SystemTick<ICoolSource, CoolPath> ticks : this.senderPath) {
                if (ticks.getSource() == par1) {
                    ticks.setList(null);
                    break;
                }
            }
        }

        public void remove(final ICoolSource par1) {
            this.senderPath.remove(new SystemTick<ICoolSource, CoolPath>(par1, null));
        }

        public void remove(final SystemTick<ICoolSource, CoolPath> par1) {
            this.senderPath.remove(par1);
        }

        public void removeAll(final List<SystemTick<ICoolSource, CoolPath>> par1) {
            if (par1 == null) {
                return;
            }
            for (SystemTick<ICoolSource, CoolPath> iCoolSource : par1) {
                iCoolSource.setList(null);
            }
        }

        public void removeAllSource1(final List<ICoolSource> par1) {
            if (par1 == null) {
                return;
            }
            for (ICoolSource iCoolSource : par1) {
                this.remove1(iCoolSource);
            }
        }

        public List<SystemTick<ICoolSource, CoolPath>> getSources(final ICoolAcceptor par1) {
            final List<SystemTick<ICoolSource, CoolPath>> source = new ArrayList<>();
            for (final SystemTick<ICoolSource, CoolPath> entry : this.senderPath) {
                if (source.contains(entry)) {
                    continue;
                }
                if (entry.getList() != null) {
                    for (CoolPath path : entry.getList()) {
                        if ((!(par1 instanceof ICoolConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof ICoolSink) || path.target != par1)) {
                            continue;
                        }
                        source.add(entry);
                    }
                }
            }
            return source;
        }


        public void clear() {
            for (SystemTick<ICoolSource, CoolPath> entry : this.senderPath) {
                List<CoolPath> list = entry.getList();
                if (list != null) {
                    for (CoolPath CoolPath : list) {
                        CoolPath.conductors.clear();
                    }
                }

            }
            this.senderPath.clear();
        }


        public SystemTick<ICoolSource, CoolPath> get(ICoolSource tileEntity) {
            for (SystemTick<ICoolSource, CoolPath> entry : this.senderPath) {
                if (entry.getSource() == tileEntity) {
                    return entry;
                }
            }
            return null;
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

        public void onTileEntityAdded(final List<CoolTarget> around, final ICoolAcceptor tile) {
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
                    for (final CoolTarget target : around) {
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
                }
                this.paths.add(newLogic);
            }
            if (!found) {
                this.createNewPath(tile);
            }
        }

        public void onTileEntityRemoved(final ICoolAcceptor par1) {
            if (this.paths.isEmpty()) {
                return;
            }

            List<ICoolTile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); i++) {
                PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (final ICoolTile tile : toRecalculate) {
                this.onTileEntityAdded(CoolNetLocal.this.getValidReceivers(tile, true), (ICoolAcceptor) tile);
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
