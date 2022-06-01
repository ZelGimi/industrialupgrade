package com.denfop.os;

import com.denfop.api.os.IOSAcceptor;
import com.denfop.api.os.IOSConductor;
import com.denfop.api.os.IOSEmitter;
import com.denfop.api.os.IOSSink;
import com.denfop.api.os.IOSSource;
import com.denfop.api.os.IOSTile;
import com.denfop.api.os.NodeOSStats;
import ic2.core.IC2;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class OSNetLocal {

    private static EnumFacing[] directions;

    static {
        OSNetLocal.directions = EnumFacing.values();
    }

    private final World world;
    private final EnergyPathMap energySourceToEnergyPathMap;
    private final Map<BlockPos, IOSTile> registeredTiles;
    private final Map<BlockPos, IOSSource> sources;
    private final WaitingList waitingList;
    private final Map<Chunk, List<IOSSink>> registeredTilesInChunk;

    OSNetLocal(final World world) {
        this.energySourceToEnergyPathMap = new EnergyPathMap();
        this.registeredTiles = new HashMap<>();
        this.sources = new HashMap<>();
        this.waitingList = new WaitingList();
        this.world = world;
        this.registeredTilesInChunk = new HashMap<>();

    }


    public void addTile(IOSTile tile1) {
        this.addTileEntity(((TileEntity) tile1).getPos(), tile1);

    }


    public void addTileEntity(final BlockPos coords, final IOSTile tile) {
        if (this.registeredTiles.containsKey(coords)) {
            return;
        }
        this.registeredTiles.put(coords, tile);
        TileEntity te = (TileEntity) tile;
        Chunk chunk = te.getWorld().getChunkFromBlockCoords(te.getPos());
        if (tile instanceof IOSSink) {
            if (!this.registeredTilesInChunk.containsKey(chunk)) {
                List<IOSSink> l = new ArrayList<>();
                l.add((IOSSink) tile);
                this.registeredTilesInChunk.put(chunk, l);
            }
        }
        this.update(coords.getX(), coords.getY(), coords.getZ());
        if (tile instanceof IOSAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), tile);
        }
        if (tile instanceof IOSSource) {
            this.sources.put(coords, (IOSSource) tile);
        }
    }

    public void removeTile(IOSTile tile) {
        this.removeTileEntity(((TileEntity) tile).getPos(), tile);
        TileEntity te = (TileEntity) tile;
        Chunk chunk = te.getWorld().getChunkFromBlockCoords(te.getPos());
        if (tile instanceof IOSSink) {
            if (this.registeredTilesInChunk.containsKey(chunk)) {
                List<IOSSink> lst = this.registeredTilesInChunk.get(chunk);
                lst.remove(tile);
            }
        }


    }

    public void removeTileEntity(final BlockPos coords, IOSTile tile) {
        if (!this.registeredTiles.containsKey(coords)) {
            return;
        }
        this.registeredTiles.remove(coords);
        this.update(coords.getX(), coords.getY(), coords.getZ());
        if (tile instanceof IOSAcceptor) {
            this.energySourceToEnergyPathMap.removeAll(this.energySourceToEnergyPathMap.getSources((IOSAcceptor) tile));
            this.waitingList.onTileEntityRemoved(tile);
        }
        if (tile instanceof IOSSource) {
            this.sources.remove(coords);
            this.energySourceToEnergyPathMap.remove((IOSSource) tile);
        }
    }

    public double emitEnergyFrom(final IOSSource energySource, double amount) {
        if (!this.energySourceToEnergyPathMap.containsKey(energySource)) {
            final EnergyPathMap energySourceToEnergyPathMap = this.energySourceToEnergyPathMap;
            energySourceToEnergyPathMap.put(energySource, this.discover(
                    energySource)
            );
        }
        List<EnergyPath> activeEnergyPaths = new Vector<>();
        for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.get(energySource)) {
            assert energyPath.target instanceof IOSSink;
            final IOSSink energySink = (IOSSink) energyPath.target;
            if (energySink.getDemandedSE().getCapacity() - energySink.getDemandedSE().getFluidAmount() <= 0.0) {
                continue;
            }
            activeEnergyPaths.add(energyPath);
        }
        Collections.shuffle(activeEnergyPaths);
        for (double i = activeEnergyPaths.size() - amount; i > 0; --i) {
            activeEnergyPaths.remove(activeEnergyPaths.size() - 1);
        }
        final Map<EnergyPath, Double> suppliedEnergyPaths = new HashMap<>();
        while (!activeEnergyPaths.isEmpty() && amount > 0) {
            double energyConsumed = 0;
            final List<EnergyPath> currentActiveEnergyPaths = activeEnergyPaths;
            activeEnergyPaths = new Vector<>();
            for (final EnergyPath energyPath2 : currentActiveEnergyPaths) {
                final IOSSink energySink2 = (IOSSink) energyPath2.target;
                final IOSSource source = energyPath2.source;
                final double energyProvided = Math.floor(Math.round(amount));
                if (!source.getOfferedFluid().isFluidEqual(energySink2.getNeedFluid())) {
                    continue;
                }
                int col = (energySink2.getDemandedSE().getCapacity() - energySink2.getDemandedSE().getFluidAmount());
                double adding = Math.min(energyProvided, col);
                if (adding <= 0.0) {
                    continue;
                }

                double energyReturned = energySink2.injectSE(energyPath2.targetDirection,
                        new FluidStack(energySink2.getNeedFluid().getFluid(), (int) adding), 0
                );
                if (energyReturned == 0.0) {
                    activeEnergyPaths.add(energyPath2);
                } else if (energyReturned >= energyProvided) {
                    energyReturned = energyProvided;
                }
                energyConsumed += (adding - energyReturned);
                final double energyInjected = (adding - energyReturned);
                if (!suppliedEnergyPaths.containsKey(energyPath2)) {
                    suppliedEnergyPaths.put(energyPath2, energyInjected);
                } else {
                    suppliedEnergyPaths.put(energyPath2, energyInjected + suppliedEnergyPaths.get(energyPath2));
                }


            }
            if (energyConsumed == 0 && !activeEnergyPaths.isEmpty()) {
                activeEnergyPaths.remove(activeEnergyPaths.size() - 1);
            }
            amount -= energyConsumed;
        }
        for (final Map.Entry<EnergyPath, Double> entry : suppliedEnergyPaths.entrySet()) {
            final EnergyPath energyPath3 = entry.getKey();
            final double energyInjected2 = entry.getValue();
            final EnergyPath energyPath5;
            final EnergyPath energyPath4 = energyPath5 = energyPath3;
            energyPath5.totalEnergyConducted += energyInjected2;
            energyPath4.maxSendedEnergy = (long) Math.max(energyPath4.maxSendedEnergy, energyInjected2);
            if (energyInjected2 > energyPath3.minInsulationEnergyAbsorption) {
                if (energyInjected2 >= energyPath3.minInsulationBreakdownEnergy) {
                    for (final IOSConductor energyConductor2 : energyPath3.conductors) {
                        if (energyInjected2 >= energyConductor2.getInsulationBreakdownEnergy()) {
                            energyConductor2.removeInsulation();
                            if (energyConductor2.getInsulationEnergyAbsorption() >= energyPath3.minInsulationEnergyAbsorption) {
                                continue;
                            }
                            energyPath3.minInsulationEnergyAbsorption = (int) energyConductor2.getInsulationEnergyAbsorption();
                        }
                    }
                }
            }
            if (energyInjected2 >= energyPath3.minConductorBreakdownEnergy) {
                for (final IOSConductor energyConductor3 : energyPath3.conductors) {
                    if (energyInjected2 >= energyConductor3.getConductorBreakdownEnergy()) {
                        energyConductor3.removeConductor();
                    }
                }
            }
        }
        return amount;
    }

    public double getTotalEnergyEmitted(final IOSTile tileEntity) {
        double ret = 0.0;
        if (tileEntity instanceof IOSConductor) {
            for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((IOSAcceptor) tileEntity)) {
                if (energyPath.conductors.contains(tileEntity)) {
                    ret += energyPath.totalEnergyConducted;
                }
            }
        }
        if (tileEntity instanceof IOSSource && this.energySourceToEnergyPathMap.containsKey((IOSSource) tileEntity)) {
            for (final EnergyPath energyPath2 : this.energySourceToEnergyPathMap.get((IOSSource) tileEntity)) {
                ret += energyPath2.totalEnergyConducted;
            }
        }
        return ret;
    }

    public double getTotalEnergySunken(final IOSTile tileEntity) {
        double ret = 0.0;
        if (tileEntity instanceof IOSConductor || tileEntity instanceof IOSSink) {
            for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((IOSAcceptor) tileEntity)) {
                if ((tileEntity instanceof IOSSink && energyPath.target == tileEntity) || (tileEntity instanceof IOSConductor && energyPath.conductors.contains(
                        tileEntity))) {
                    ret += energyPath.totalEnergyConducted;
                }
            }
        }
        return ret;
    }

    private List<EnergyPath> discover(final IOSTile emitter) {
        final Map<IOSTile, EnergyBlockLink> reachedTileEntities = new HashMap<>();
        final LinkedList<IOSTile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(emitter);
        while (!tileEntitiesToCheck.isEmpty()) {
            final IOSTile currentTileEntity = tileEntitiesToCheck.remove();
            TileEntity tile = (TileEntity) emitter;
            if (!tile.isInvalid()) {
                double currentLoss = 0.0;
                final List<EnergyTarget> validReceivers = this.getValidReceivers(currentTileEntity, false);
                for (final EnergyTarget validReceiver : validReceivers) {
                    if (validReceiver.tileEntity != emitter) {
                        if (reachedTileEntities.containsKey(validReceiver.tileEntity) && reachedTileEntities.get(validReceiver.tileEntity).loss <= currentLoss) {
                            continue;
                        }
                        reachedTileEntities.put(validReceiver.tileEntity, new EnergyBlockLink(
                                validReceiver.direction,
                                currentLoss
                        ));
                        if (!(validReceiver.tileEntity instanceof IOSConductor)) {
                            continue;
                        }
                        tileEntitiesToCheck.remove(validReceiver.tileEntity);
                        tileEntitiesToCheck.add(validReceiver.tileEntity);
                    }
                }
            }
        }
        final List<EnergyPath> energyPaths = new LinkedList<>();
        for (final Map.Entry<IOSTile, EnergyBlockLink> entry : reachedTileEntities.entrySet()) {
            IOSTile tileEntity = entry.getKey();
            if ((tileEntity instanceof IOSSink)) {
                EnergyBlockLink energyBlockLink = entry.getValue();
                final EnergyPath energyPath = new EnergyPath();
                energyPath.loss = Math.max(energyBlockLink.loss, 0.1);
                energyPath.target = tileEntity;
                energyPath.targetDirection = energyBlockLink.direction;
                if (emitter instanceof IOSSource) {
                    while (true) {
                        TileEntity te = (TileEntity) tileEntity;
                        assert energyBlockLink != null;
                        tileEntity = this.getTileEntity(te.getPos().offset(energyBlockLink.direction));
                        if (tileEntity == emitter) {
                            break;
                        }
                        if (!(tileEntity instanceof IOSConductor)) {
                            break;
                        }
                        final IOSConductor energyConductor = (IOSConductor) tileEntity;

                        energyPath.conductors.add(energyConductor);
                        if (energyConductor.getInsulationEnergyAbsorption() < energyPath.minInsulationEnergyAbsorption) {
                            energyPath.minInsulationEnergyAbsorption = (int) energyConductor.getInsulationEnergyAbsorption();
                        }
                        if (energyConductor.getInsulationBreakdownEnergy() < energyPath.minInsulationBreakdownEnergy) {
                            energyPath.minInsulationBreakdownEnergy = (int) energyConductor.getInsulationBreakdownEnergy();
                        }
                        if (energyConductor.getConductorBreakdownEnergy() < energyPath.minConductorBreakdownEnergy) {
                            energyPath.minConductorBreakdownEnergy = (int) energyConductor.getConductorBreakdownEnergy();
                        }
                        energyPath.source = (IOSSource) emitter;
                        energyBlockLink = reachedTileEntities.get(tileEntity);
                        if (energyBlockLink != null) {
                            continue;
                        }
                        IC2.platform.displayError("An energy network pathfinding entry is corrupted.\nThis could happen due to " +
                                "incorrect Minecraft behavior or a bug.\n\n(Technical information: energyBlockLink, tile " +
                                "entities below)\nE:");
                    }
                }
                energyPaths.add(energyPath);
            }
        }
        return energyPaths;
    }


    public IOSTile getNeighbor(final IOSTile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        return this.getTileEntity(((TileEntity) tile).getPos().offset(dir));
    }

    private List<EnergyTarget> getValidReceivers(final IOSTile emitter, final boolean reverse) {
        final List<EnergyTarget> validReceivers = new LinkedList<>();
        for (final EnumFacing direction : OSNetLocal.directions) {

            final IOSTile target2 = getNeighbor(emitter, direction);
            TileEntity te1 = (TileEntity) target2;
            if (target2 != null && this.registeredTiles.containsKey((te1.getPos()))) {
                final EnumFacing inverseDirection2 = direction.getOpposite();
                if (reverse) {
                    if (emitter instanceof IOSAcceptor && target2 instanceof IOSEmitter) {
                        final IOSEmitter sender2 = (IOSEmitter) target2;
                        final IOSAcceptor receiver2 = (IOSAcceptor) emitter;
                        if (sender2.emitsSETo(receiver2, inverseDirection2) && receiver2.acceptsOSFrom(
                                sender2,
                                direction
                        )) {
                            validReceivers.add(new EnergyTarget(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof IOSEmitter && target2 instanceof IOSAcceptor) {
                    final IOSEmitter sender2 = (IOSEmitter) emitter;
                    final IOSAcceptor receiver2 = (IOSAcceptor) target2;
                    if (sender2.emitsSETo(receiver2, direction) && receiver2.acceptsOSFrom(
                            sender2,
                            inverseDirection2
                    )) {
                        validReceivers.add(new EnergyTarget(target2, inverseDirection2));
                    }
                }
            }
        }
        return validReceivers;
    }

    public List<IOSSource> discoverFirstPathOrSources(final IOSTile par1) {
        final Set<IOSTile> reached = new HashSet<>();
        final List<IOSSource> result = new ArrayList<>();
        final List<IOSTile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final IOSTile tile = workList.remove(0);
            final TileEntity te = (TileEntity) tile;
            if (!te.isInvalid()) {
                final List<EnergyTarget> targets = this.getValidReceivers(tile, true);
                for (EnergyTarget energyTarget : targets) {
                    final IOSTile target = energyTarget.tileEntity;
                    if (target != par1) {
                        if (!reached.contains(target)) {
                            reached.add(target);
                            if (target instanceof IOSSource) {
                                result.add((IOSSource) target);
                            } else if (target instanceof IOSConductor) {
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
        if (this.waitingList.hasWork()) {
            final List<IOSTile> tiles = this.waitingList.getPathTiles();
            for (final IOSTile tile : tiles) {
                final List<IOSSource> sources = this.discoverFirstPathOrSources(tile);
                if (sources.size() > 0) {
                    this.energySourceToEnergyPathMap.removeAll(sources);
                }
            }
            this.waitingList.clear();
        }
        for (Map.Entry<BlockPos, IOSSource> entry : new HashMap<>(this.sources).entrySet()) {
            if (entry != null) {
                final IOSSource source = entry.getValue();
                if (source != null) {
                    if (this.energySourceToEnergyPathMap.containsKey(source)) {
                        for (final EnergyPath path : this.energySourceToEnergyPathMap.get(source)) {
                            path.totalEnergyConducted = 0L;
                            path.maxSendedEnergy = 0L;
                        }
                    }

                    double offer = Math.min(
                            source.getOfferedSE().getCapacity() - source.getOfferedSE().getFluidAmount(),
                            Integer.MAX_VALUE
                    );
                    if (offer > 0) {

                        offer = Math.min(
                                source.getOfferedSE().getCapacity() - source.getOfferedSE().getFluidAmount(),
                                Integer.MAX_VALUE
                        );
                        if (offer < 1) {
                            break;
                        }
                        final double removed = offer - this.emitEnergyFrom(source, offer);
                        if (removed <= 0) {
                            break;
                        }

                        source.drawOS(new FluidStack(source.getOfferedFluid().getFluid(), (int) removed));

                    }
                }
            }
        }
    }


    public IOSTile getTileEntity(BlockPos pos) {
        if (this.registeredTiles.containsKey(pos)) {
            return this.registeredTiles.get(pos);
        }
        return null;
    }

    public NodeOSStats getNodeStats(final IOSTile tile) {
        final double emitted = this.getTotalEnergyEmitted(tile);
        final double received = this.getTotalEnergySunken(tile);
        return new NodeOSStats(received, emitted);
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
        this.registeredTiles.clear();
        this.sources.clear();
        this.waitingList.clear();
        this.registeredTilesInChunk.clear();
    }


    static class EnergyTarget {

        final IOSTile tileEntity;
        final EnumFacing direction;

        EnergyTarget(final IOSTile tileEntity, final EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }

    }

    static class EnergyBlockLink {

        final EnumFacing direction;
        final double loss;

        EnergyBlockLink(final EnumFacing direction, final double loss) {
            this.direction = direction;
            this.loss = loss;
        }

    }

    static class EnergyPath {

        final Set<IOSConductor> conductors;
        IOSTile target;
        IOSSource source;
        EnumFacing targetDirection;
        double loss;
        double minInsulationEnergyAbsorption;
        double minInsulationBreakdownEnergy;
        double minConductorBreakdownEnergy;
        long totalEnergyConducted;
        long maxSendedEnergy;

        EnergyPath() {
            this.target = null;
            this.source = null;
            this.conductors = new HashSet<>();
            this.loss = 0.0;
            this.minInsulationEnergyAbsorption = Integer.MAX_VALUE;
            this.minInsulationBreakdownEnergy = Integer.MAX_VALUE;
            this.minConductorBreakdownEnergy = Integer.MAX_VALUE;
            this.totalEnergyConducted = 0L;
            this.maxSendedEnergy = 0L;
        }

    }

    static class EnergyPathMap {

        final Map<IOSSource, List<EnergyPath>> senderPath;
        final Map<EnergyPath, IOSSource> pathToSender;

        EnergyPathMap() {
            this.senderPath = new HashMap<>();
            this.pathToSender = new HashMap<>();
        }

        public void put(final IOSSource par1, final List<EnergyPath> par2) {
            this.senderPath.put(par1, par2);
            for (EnergyPath energyPath : par2) {
                this.pathToSender.put(energyPath, par1);
            }
        }

        public boolean containsKey(final IOSSource par1) {
            return this.senderPath.containsKey(par1);
        }

        public List<EnergyPath> get(final IOSSource par1) {
            return this.senderPath.get(par1);
        }

        public void remove(final IOSSource par1) {
            final List<EnergyPath> paths = this.senderPath.remove(par1);
            if (paths != null) {
                for (EnergyPath path : paths) {
                    this.pathToSender.remove(path);
                }
            }
        }

        public void removeAll(final List<IOSSource> par1) {
            for (IOSSource iEnergySource : par1) {
                this.remove(iEnergySource);
            }
        }

        public List<EnergyPath> getPaths(final IOSAcceptor par1) {
            final List<EnergyPath> paths = new ArrayList<>();
            for (final IOSSource source : this.getSources(par1)) {
                if (this.containsKey(source)) {
                    paths.addAll(this.get(source));
                }
            }
            return paths;
        }

        public List<IOSSource> getSources(final IOSAcceptor par1) {
            final List<IOSSource> source = new ArrayList<>();
            for (final Map.Entry<EnergyPath, IOSSource> entry : this.pathToSender.entrySet()) {
                if (source.contains(entry.getValue())) {
                    continue;
                }
                final EnergyPath path = entry.getKey();
                if ((!(par1 instanceof IOSConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof IOSSink) || path.target != par1)) {
                    continue;
                }
                source.add(entry.getValue());
            }
            return source;
        }

        public void clear() {
            this.senderPath.clear();
            this.pathToSender.clear();
        }

    }

    static class PathLogic {

        final List<IOSTile> tiles;

        PathLogic() {
            this.tiles = new ArrayList<>();
        }

        public boolean contains(final IOSTile par1) {
            return this.tiles.contains(par1);
        }

        public void add(final IOSTile par1) {
            this.tiles.add(par1);
        }

        public void remove(final IOSTile par1) {
            this.tiles.remove(par1);
        }

        public void clear() {
            this.tiles.clear();
        }

        public IOSTile getRepresentingTile() {
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

        public void onTileEntityAdded(final List<EnergyTarget> around, final IOSTile tile) {
            if (around.isEmpty() || this.paths.isEmpty()) {
                this.createNewPath(tile);
                return;
            }
            boolean found = false;
            final List<PathLogic> logics = new ArrayList<>();
            for (final PathLogic logic : this.paths) {
                if (logic.contains(tile)) {
                    found = true;
                    if (tile instanceof IOSConductor) {
                        logics.add(logic);
                    }
                } else {
                    for (final EnergyTarget target : around) {
                        if (logic.contains(target.tileEntity)) {
                            found = true;
                            logic.add(tile);
                            if (target.tileEntity instanceof IOSConductor) {
                                logics.add(logic);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            if (logics.size() > 1 && tile instanceof IOSConductor) {
                final PathLogic newLogic = new PathLogic();
                for (final PathLogic logic2 : logics) {
                    this.paths.remove(logic2);
                    for (final IOSTile toMove : logic2.tiles) {
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

        public void onTileEntityRemoved(final IOSTile par1) {
            if (this.paths.isEmpty()) {
                return;
            }
            final List<IOSTile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); ++i) {
                final PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (final IOSTile tile : toRecalculate) {
                this.onTileEntityAdded(OSNetLocal.this.getValidReceivers(tile, true), tile);
            }
        }

        public void createNewPath(final IOSTile par1) {
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

        public List<IOSTile> getPathTiles() {
            final List<IOSTile> tiles = new ArrayList<>();
            for (PathLogic path : this.paths) {
                final IOSTile tile = path.getRepresentingTile();
                if (tile != null) {
                    tiles.add(tile);
                }
            }
            return tiles;
        }

    }

}
