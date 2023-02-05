package com.denfop.heat;

import com.denfop.api.energy.SystemTick;
import com.denfop.api.heat.IHeatAcceptor;
import com.denfop.api.heat.IHeatConductor;
import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.heat.IHeatSink;
import com.denfop.api.heat.IHeatSource;
import com.denfop.api.heat.IHeatTile;
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

public class HeatNetLocal {


    private final World world;
    private final HeatPathMap HeatSourceToHeatPathMap;
    private final Map<BlockPos, IHeatTile> chunkCoordinatesIHeatTileMap;
    private final WaitingList waitingList;


    HeatNetLocal(final World world) {
        this.HeatSourceToHeatPathMap = new HeatPathMap();
        this.waitingList = new WaitingList();
        this.world = world;
        this.chunkCoordinatesIHeatTileMap = new HashMap<>();
    }


    public void addTile(IHeatTile tile1) {


        this.addTileEntity(getTileFromIHeat(tile1).getPos(), tile1);


    }

    public void addTile(IHeatTile tile, TileEntity tileentity) {

        final BlockPos coords = tileentity.getPos();
        if (this.chunkCoordinatesIHeatTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesIHeatTileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof IHeatAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), (IHeatAcceptor) tile);
        }
        if (tile instanceof IHeatSource) {
            HeatSourceToHeatPathMap.senderPath.add(new SystemTick<>((IHeatSource) tile, null));

        }


    }

    public BlockPos getPos(final IHeatTile tile) {
        if (tile == null) {
            return null;
        }
        return tile.getBlockPos();
    }

    public void addTileEntity(final BlockPos coords, final IHeatTile tile) {
        if (this.chunkCoordinatesIHeatTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesIHeatTileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof IHeatAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), (IHeatAcceptor) tile);
        }
        if (tile instanceof IHeatSource) {
            HeatSourceToHeatPathMap.senderPath.add(new SystemTick<>((IHeatSource) tile, null));

        }
    }

    public void removeTile(IHeatTile tile1) {

        this.removeTileEntity(tile1);

    }


    public void removeTileEntity(IHeatTile tile) {
        if (!this.chunkCoordinatesIHeatTileMap.containsKey(tile.getBlockPos())) {
            return;
        }
        final BlockPos coord = tile.getBlockPos();
        this.chunkCoordinatesIHeatTileMap.remove(coord, tile);
        this.update(coord);
        if (tile instanceof IHeatAcceptor) {
            this.HeatSourceToHeatPathMap.removeAll(this.HeatSourceToHeatPathMap.getSources((IHeatAcceptor) tile));
            this.waitingList.onTileEntityRemoved((IHeatAcceptor) tile);
        }
        if (tile instanceof IHeatSource) {
            this.HeatSourceToHeatPathMap.remove((IHeatSource) tile);
        }
    }

    public void emitHeatFrom(final IHeatSource HeatSource, double amount, final SystemTick<IHeatSource, HeatPath> tick) {
        List<HeatPath> HeatPaths = tick.getList();

        if (HeatPaths == null) {
            HeatPaths = this.discover(HeatSource);
            tick.setList(HeatPaths);
        }
        boolean allow = false;
        if (amount > 0) {
            for (final HeatPath HeatPath : HeatPaths) {
                final IHeatSink HeatSink = HeatPath.target;
                double demandedHeat = HeatSink.getDemandedHeat();
                if (demandedHeat <= 0.0) {
                    continue;
                }
                double adding;


                adding = Math.min(amount, demandedHeat);
                if (adding <= 0.0D) {
                    continue;
                }
                allow = allow || HeatSink.needTemperature();

                adding -= HeatSink.injectHeat(HeatPath.targetDirection, adding, 0);
                HeatPath.totalHeatConducted = (long) adding;


                if (adding > HeatPath.min) {
                    for (IHeatConductor HeatConductor : HeatPath.conductors) {
                        if (HeatConductor.getConductorBreakdownHeat() < adding) {
                            HeatConductor.removeConductor();
                        } else {
                            break;
                        }
                    }
                }


            }
        }

        if (!allow) {
            HeatSource.setAllowed(false);
        }
    }


    public TileEntity getTileFromIHeat(IHeatTile tile) {
        if (tile == null) {
            return null;
        }
        return tile.getTile();
    }

    public List<HeatPath> discover(final IHeatSource emitter) {
        final Map<IHeatConductor, EnumFacing> reachedTileEntities = new HashMap<>();
        final List<IHeatTile> tileEntitiesToCheck = new ArrayList<>();
        final List<HeatPath> HeatPaths = new ArrayList<>();

        tileEntitiesToCheck.add(emitter);

        while (!tileEntitiesToCheck.isEmpty()) {
            final IHeatTile currentTileEntity = tileEntitiesToCheck.remove(0);
            final List<HeatTarget> validReceivers = this.getValidReceivers(currentTileEntity, false);
            for (final HeatTarget validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {
                    if (validReceiver.tileEntity instanceof IHeatSink) {
                        HeatPaths.add(new HeatPath((IHeatSink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }
                    if (reachedTileEntities.containsKey((IHeatConductor) validReceiver.tileEntity)) {
                        continue;
                    }

                    reachedTileEntities.put((IHeatConductor) validReceiver.tileEntity, validReceiver.direction);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }


        }
        for (HeatPath HeatPath : HeatPaths) {
            IHeatTile tileEntity = HeatPath.target;
            EnumFacing HeatBlockLink = HeatPath.targetDirection;
            BlockPos te = tileEntity.getBlockPos();
            if (emitter != null) {
                while (tileEntity != emitter) {
                    if (HeatBlockLink != null && te != null) {
                        tileEntity = this.getTileEntity(te.offset(HeatBlockLink));
                        te = te.offset(HeatBlockLink);
                    }
                    if (!(tileEntity instanceof IHeatConductor)) {
                        break;
                    }
                    final IHeatConductor HeatConductor = (IHeatConductor) tileEntity;
                    HeatPath.conductors.add(HeatConductor);
                    if (HeatConductor.getConductorBreakdownHeat() - 1 < HeatPath.getMin()) {
                        HeatPath.setMin(HeatConductor.getConductorBreakdownHeat() - 1);
                    }
                    HeatBlockLink = reachedTileEntities.get(tileEntity);
                    if (HeatBlockLink != null) {
                        continue;
                    }
                    assert te != null;
                    IC2.platform.displayError("An Heat network pathfinding entry is corrupted.\nThis could happen due to " +
                            "incorrect Minecraft behavior or a bug.\n\n(Technical information: HeatBlockLink, tile " +
                            "entities below)\nE: " + emitter + " (" + te.getX() + "," + te.getY() + "," + te

                            .getZ() + ")\n" + "C: " + tileEntity + " (" + te.getX() + "," + te

                            .getY() + "," + te

                            .getZ() + ")\n" + "R: " + HeatPath.target + " (" + HeatPath.target
                            .getBlockPos()
                            .getX() + "," + HeatPath.target.getBlockPos().getY() + "," + HeatPath.target.getBlockPos().getZ() + ")");
                }
            }
        }
        return HeatPaths;
    }

    public IHeatTile getNeighbor(final IHeatTile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        return this.getTileEntity(tile.getBlockPos().offset(dir));
    }

    private List<HeatTarget> getValidReceivers(final IHeatTile emitter, final boolean reverse) {
        final List<HeatTarget> validReceivers = new LinkedList<>();

        for (final EnumFacing direction : EnumFacing.values()) {
            final IHeatTile target2 = getNeighbor(emitter, direction);
            if (target2 != null) {
                final EnumFacing inverseDirection2 = direction.getOpposite();
                if (reverse) {
                    if (emitter instanceof IHeatAcceptor && target2 instanceof IHeatEmitter) {
                        final IHeatEmitter sender2 = (IHeatEmitter) target2;
                        final IHeatAcceptor receiver2 = (IHeatAcceptor) emitter;
                        if (sender2.emitsHeatTo(receiver2, inverseDirection2) && receiver2.acceptsHeatFrom(
                                sender2,
                                direction
                        )) {
                            validReceivers.add(new HeatTarget(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof IHeatEmitter && target2 instanceof IHeatAcceptor) {
                    final IHeatEmitter sender2 = (IHeatEmitter) emitter;
                    final IHeatAcceptor receiver2 = (IHeatAcceptor) target2;
                    if (sender2.emitsHeatTo(receiver2, direction) && receiver2.acceptsHeatFrom(
                            sender2,
                            inverseDirection2
                    )) {
                        validReceivers.add(new HeatTarget(target2, inverseDirection2));
                    }
                }
            }

        }
        //

        return validReceivers;
    }

    public List<IHeatSource> discoverFirstPathOrSources(final IHeatTile par1) {
        final Set<IHeatTile> reached = new HashSet<>();
        final List<IHeatSource> result = new ArrayList<>();
        final List<IHeatTile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final IHeatTile tile = workList.remove(0);
            final TileEntity te = tile.getTile();
            if (te == null) {
                continue;
            }
            if (!te.isInvalid()) {
                final List<HeatTarget> targets = this.getValidReceivers(tile, true);
                for (HeatTarget HeatTarget : targets) {
                    final IHeatTile target = HeatTarget.tileEntity;
                    if (target != par1) {
                        if (!reached.contains(target)) {
                            reached.add(target);
                            if (target instanceof IHeatSource) {
                                result.add((IHeatSource) target);
                            } else if (target instanceof IHeatConductor) {
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
            final List<IHeatTile> tiles = this.waitingList.getPathTiles();
            for (final IHeatTile tile : tiles) {
                final List<IHeatSource> sources = this.discoverFirstPathOrSources(tile);
                if (sources.size() > 0) {
                    this.HeatSourceToHeatPathMap.removeAllSource1(sources);
                }
            }
            this.waitingList.clear();

        }
        try {
            for (SystemTick<IHeatSource, HeatPath> tick : this.HeatSourceToHeatPathMap.senderPath) {
                final IHeatSource entry = tick.getSource();
                if (entry != null) {
                    double offered = entry.getOfferedHeat();

                    if (entry.isAllowed()) {

                        this.emitHeatFrom(entry, offered, tick);


                    } else {
                        this.emitHeatFromNotAllowed(entry, offered, tick);

                    }

                }
            }
        } catch (Exception ignored) {
        }


    }

    public void emitHeatFromNotAllowed(
            final IHeatSource HeatSource,
            double amount,
            final SystemTick<IHeatSource, HeatPath> tick
    ) {
        List<HeatPath> HeatPaths = tick.getList();

        if (HeatPaths == null) {
            HeatPaths = this.discover(HeatSource);
            tick.setList(HeatPaths);
        }

        for (final HeatPath HeatPath : HeatPaths) {
            final IHeatSink HeatSink = HeatPath.target;
            double demandedHeat = HeatSink.getDemandedHeat();
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


            adding -= HeatSink.injectHeat(HeatPath.targetDirection, adding, 0);
            HeatPath.totalHeatConducted = (long) adding;


            if (adding > HeatPath.min) {
                for (IHeatConductor HeatConductor : HeatPath.conductors) {
                    if (HeatConductor.getConductorBreakdownHeat() < adding) {
                        HeatConductor.removeConductor();
                    } else {
                        break;
                    }
                }
            }


        }


    }


    public IHeatTile getTileEntity(BlockPos pos) {

        return this.chunkCoordinatesIHeatTileMap.get(pos);
    }


    public void update(BlockPos pos) {
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos
                    .offset(dir);
            final IHeatTile tile = this.chunkCoordinatesIHeatTileMap.get(pos1);
            if (tile != null) {
                if (tile instanceof IHeatConductor) {
                    ((IHeatConductor) tile).update_render();
                }
            }

        }
    }

    public void onUnload() {
        this.HeatSourceToHeatPathMap.clear();
        this.waitingList.clear();
        this.chunkCoordinatesIHeatTileMap.clear();
    }

    static class HeatTarget {

        final IHeatTile tileEntity;
        final EnumFacing direction;

        HeatTarget(final IHeatTile tileEntity, final EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }

    }

    public static class HeatPath {

        final List<IHeatConductor> conductors;
        final IHeatSink target;
        final EnumFacing targetDirection;
        long totalHeatConducted;
        double min = Double.MAX_VALUE;

        HeatPath(IHeatSink sink, EnumFacing facing) {
            this.target = sink;
            this.conductors = new ArrayList<>();
            this.totalHeatConducted = 0L;
            this.targetDirection = facing;


        }

        public List<IHeatConductor> getConductors() {
            return conductors;
        }


        public double getMin() {
            return min;
        }

        public void setMin(final double min) {
            this.min = min;
        }


    }


    static class HeatPathMap {

        final List<SystemTick<IHeatSource, HeatPath>> senderPath;

        HeatPathMap() {
            this.senderPath = new ArrayList<>();
        }

        public void put(final IHeatSource par1, final List<HeatPath> par2) {
            this.senderPath.add(new SystemTick<>(par1, par2));
        }


        public boolean containsKey(final SystemTick<IHeatSource, HeatPath> par1) {
            return this.senderPath.contains(par1);
        }

        public boolean containsKey(final IHeatSource par1) {
            return this.senderPath.contains(new SystemTick<IHeatSource, HeatPath>(par1, null));
        }


        public void remove1(final IHeatSource par1) {

            for (SystemTick<IHeatSource, HeatPath> ticks : this.senderPath) {
                if (ticks.getSource() == par1) {
                    ticks.setList(null);
                    break;
                }
            }
        }

        public void remove(final IHeatSource par1) {
            this.senderPath.remove(new SystemTick<IHeatSource, HeatPath>(par1, null));
        }

        public void remove(final SystemTick<IHeatSource, HeatPath> par1) {
            this.senderPath.remove(par1);
        }

        public void removeAll(final List<SystemTick<IHeatSource, HeatPath>> par1) {
            if (par1 == null) {
                return;
            }
            for (SystemTick<IHeatSource, HeatPath> iHeatSource : par1) {
                iHeatSource.setList(null);
            }
        }

        public void removeAllSource1(final List<IHeatSource> par1) {
            if (par1 == null) {
                return;
            }
            for (IHeatSource iHeatSource : par1) {
                this.remove1(iHeatSource);
            }
        }

        public List<SystemTick<IHeatSource, HeatPath>> getSources(final IHeatAcceptor par1) {
            final List<SystemTick<IHeatSource, HeatPath>> source = new ArrayList<>();
            for (final SystemTick<IHeatSource, HeatPath> entry : this.senderPath) {
                if (source.contains(entry)) {
                    continue;
                }
                if (entry.getList() != null) {
                    for (HeatPath path : entry.getList()) {
                        if ((!(par1 instanceof IHeatConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof IHeatSink) || path.target != par1)) {
                            continue;
                        }
                        source.add(entry);
                    }
                }
            }
            return source;
        }


        public void clear() {
            for (SystemTick<IHeatSource, HeatPath> entry : this.senderPath) {
                List<HeatPath> list = entry.getList();
                if (list != null) {
                    for (HeatPath HeatPath : list) {
                        HeatPath.conductors.clear();
                    }
                }

            }
            this.senderPath.clear();
        }


        public SystemTick<IHeatSource, HeatPath> get(IHeatSource tileEntity) {
            for (SystemTick<IHeatSource, HeatPath> entry : this.senderPath) {
                if (entry.getSource() == tileEntity) {
                    return entry;
                }
            }
            return null;
        }

    }


    static class PathLogic {

        final List<IHeatTile> tiles;

        PathLogic() {
            this.tiles = new ArrayList<>();
        }

        public boolean contains(final IHeatTile par1) {
            return this.tiles.contains(par1);
        }

        public void add(final IHeatTile par1) {
            this.tiles.add(par1);
        }

        public void remove(final IHeatTile par1) {
            this.tiles.remove(par1);
        }

        public void clear() {
            this.tiles.clear();
        }

        public IHeatTile getRepresentingTile() {
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

        public void onTileEntityAdded(final List<HeatTarget> around, final IHeatAcceptor tile) {
            if (around.isEmpty() || this.paths.isEmpty()) {
                this.createNewPath(tile);
                return;
            }
            boolean found = false;
            final List<PathLogic> logics = new ArrayList<>();
            for (final PathLogic logic : this.paths) {
                if (logic.contains(tile)) {
                    found = true;
                    if (tile instanceof IHeatConductor) {
                        logics.add(logic);
                    }
                } else {
                    for (final HeatTarget target : around) {
                        if (logic.contains(target.tileEntity)) {
                            found = true;
                            logic.add(tile);
                            if (target.tileEntity instanceof IHeatConductor) {
                                logics.add(logic);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            if (logics.size() > 1 && tile instanceof IHeatConductor) {
                final PathLogic newLogic = new PathLogic();
                for (final PathLogic logic2 : logics) {
                    this.paths.remove(logic2);
                    for (final IHeatTile toMove : logic2.tiles) {
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

        public void onTileEntityRemoved(final IHeatAcceptor par1) {
            if (this.paths.isEmpty()) {
                return;
            }

            List<IHeatTile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); i++) {
                PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (final IHeatTile tile : toRecalculate) {
                this.onTileEntityAdded(HeatNetLocal.this.getValidReceivers(tile, true), (IHeatAcceptor) tile);
            }
        }

        public void createNewPath(final IHeatTile par1) {
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

        public List<IHeatTile> getPathTiles() {
            final List<IHeatTile> tiles = new ArrayList<>();
            for (PathLogic path : this.paths) {
                final IHeatTile tile = path.getRepresentingTile();
                if (tile != null) {
                    tiles.add(tile);
                }
            }
            return tiles;
        }

    }

}
