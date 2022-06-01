package com.denfop.heat;

import com.denfop.api.heat.IHeatAcceptor;
import com.denfop.api.heat.IHeatConductor;
import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.heat.IHeatSink;
import com.denfop.api.heat.IHeatSource;
import com.denfop.api.heat.IHeatTile;
import ic2.core.IC2;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class HeatNetLocal {

    private static final EnumFacing[] directions = EnumFacing.values();

    private final World world;

    private final EnergyPathMap energySourceToEnergyPathMap;

    private final Map<BlockPos, IHeatTile> registeredTiles;

    private final Map<BlockPos, IHeatSource> sources;

    private final WaitingList waitingList;


    HeatNetLocal(World world) {
        this.energySourceToEnergyPathMap = new EnergyPathMap();
        this.registeredTiles = new HashMap<>();
        this.sources = new HashMap<>();
        this.waitingList = new WaitingList();
        this.world = world;
    }


    public void addTile(IHeatTile tile1) {
        addTileEntity(((TileEntity) tile1).getPos(), tile1);
    }


    public void addTileEntity(BlockPos coords, IHeatTile tile) {
        if (this.registeredTiles.containsKey(coords)) {
            return;
        }
        this.registeredTiles.put(coords, tile);
        update(coords.getX(), coords.getY(), coords.getZ());
        if (tile instanceof IHeatAcceptor) {
            this.waitingList.onTileEntityAdded(getValidReceivers(tile, true), tile);
        }
        if (tile instanceof IHeatSource) {
            this.sources.put(coords, (IHeatSource) tile);
        }
    }

    public void removeTile(IHeatTile tile) {
        removeTileEntity(((TileEntity) tile).getPos(), tile);
    }

    public void removeTileEntity(BlockPos coords, IHeatTile tile) {
        if (!this.registeredTiles.containsKey(coords)) {
            return;
        }
        this.registeredTiles.remove(coords);
        update(coords.getX(), coords.getY(), coords.getZ());
        if (tile instanceof IHeatAcceptor) {
            this.energySourceToEnergyPathMap.removeAll(this.energySourceToEnergyPathMap.getSources((IHeatAcceptor) tile));
            this.waitingList.onTileEntityRemoved(tile);
        }
        if (tile instanceof IHeatSource) {
            this.sources.remove(coords);
            this.energySourceToEnergyPathMap.remove((IHeatSource) tile);
        }
    }

    public double emitEnergyFrom(IHeatSource energySource, double amount) {
        if (!this.energySourceToEnergyPathMap.containsKey(energySource)) {
            EnergyPathMap energySourceToEnergyPathMap = this.energySourceToEnergyPathMap;
            energySourceToEnergyPathMap.put(energySource, discover(
                    energySource,

                    energySource.getOfferedHeat()
            ));
        }
        List<EnergyPath> activeEnergyPaths = new Vector<>();
        for (EnergyPath energyPath : this.energySourceToEnergyPathMap.get(energySource)) {
            assert energyPath.target instanceof IHeatSink;
            IHeatSink energySink = (IHeatSink) energyPath.target;
            if (energySink.getDemandedHeat() <= 0.0D) {
                continue;
            }

            activeEnergyPaths.add(energyPath);
        }
        Collections.shuffle(activeEnergyPaths);
        double i;
        for (i = activeEnergyPaths.size() - amount; i > 0.0D; i--) {
            activeEnergyPaths.remove(activeEnergyPaths.size() - 1);
        }
        while (!activeEnergyPaths.isEmpty() && amount > 0.0D) {
            double energyConsumed = 0.0D;
            List<EnergyPath> currentActiveEnergyPaths = activeEnergyPaths;
            activeEnergyPaths = new Vector<>();
            for (EnergyPath energyPath2 : currentActiveEnergyPaths) {
                IHeatSink energySink2 = (IHeatSink) energyPath2.target;
                double energyProvided = Math.floor(Math.round(amount));
                double energyLoss = 0;
                if (energyProvided > energyLoss) {
                    double providing = energyProvided - energyLoss;
                    double adding = Math.min(providing, energySink2.getDemandedHeat());
                    if (adding <= 0.0D) {
                        continue;
                    }
                    double energyReturned = energySink2.injectHeat(energyPath2.targetDirection, adding + 1, 0.0D);
                    if (energyReturned == 0.0D) {
                        activeEnergyPaths.add(energyPath2);
                    } else if (energyReturned >= energyProvided - energyLoss) {
                        energyReturned = energyProvided - energyLoss;
                    }
                    energyConsumed += adding - energyReturned + energyLoss;
                    for (IHeatConductor energyConductor3 : energyPath2.conductors) {
                        if (energySource.getOfferedHeat() >= energyConductor3.getConductorBreakdownEnergy()) {
                            energyConductor3.removeConductor();
                        }
                    }
                    continue;
                }
                activeEnergyPaths.add(energyPath2);
            }
            if (energyConsumed == 0.0D && !activeEnergyPaths.isEmpty()) {
                activeEnergyPaths.remove(activeEnergyPaths.size() - 1);
            }
            amount -= energyConsumed;
        }
        return amount;
    }

    private List<EnergyPath> discover(IHeatTile emitter, double lossLimit) {
        Map<IHeatTile, EnergyBlockLink> reachedTileEntities = new HashMap<>();
        LinkedList<IHeatTile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(emitter);
        while (!tileEntitiesToCheck.isEmpty()) {
            IHeatTile currentTileEntity = tileEntitiesToCheck.remove();
            TileEntity tile = (TileEntity) emitter;
            if (!tile.isInvalid()) {
                double currentLoss = 0.0D;
                List<EnergyTarget> validReceivers = getValidReceivers(currentTileEntity, false);
                for (EnergyTarget validReceiver : validReceivers) {
                    if (validReceiver.tileEntity != emitter) {
                        double additionalLoss = 0.0D;
                        if (validReceiver.tileEntity instanceof IHeatConductor) {
                            additionalLoss = ((IHeatConductor) validReceiver.tileEntity).getConductionLoss();
                            if (additionalLoss < 1.0E-4D) {
                                additionalLoss = 1.0E-4D;
                            }
                            if (currentLoss + additionalLoss >= lossLimit) {
                                continue;
                            }
                        }
                        if (reachedTileEntities.containsKey(validReceiver.tileEntity) && reachedTileEntities.get(
                                validReceiver.tileEntity).loss <= currentLoss + additionalLoss) {
                            continue;
                        }
                        reachedTileEntities.put(
                                validReceiver.tileEntity,
                                new EnergyBlockLink(validReceiver.direction, currentLoss + additionalLoss)
                        );
                        if (!(validReceiver.tileEntity instanceof IHeatConductor)) {
                            continue;
                        }
                        tileEntitiesToCheck.remove(validReceiver.tileEntity);
                        tileEntitiesToCheck.add(validReceiver.tileEntity);
                    }
                }
            }
        }
        List<EnergyPath> energyPaths = new LinkedList<>();
        for (Map.Entry<IHeatTile, EnergyBlockLink> entry : reachedTileEntities.entrySet()) {
            IHeatTile tileEntity = entry.getKey();
            if (tileEntity instanceof IHeatSink) {
                EnergyBlockLink energyBlockLink = entry.getValue();
                EnergyPath energyPath = new EnergyPath();
                energyPath.target = tileEntity;
                energyPath.targetDirection = energyBlockLink.direction;
                if (emitter instanceof IHeatSource) {
                    while (true) {
                        TileEntity te = (TileEntity) tileEntity;
                        assert energyBlockLink != null;
                        tileEntity = getTileEntity(te.getPos().offset(energyBlockLink.direction));
                        if (tileEntity == emitter) {
                            break;
                        }
                        if (!(tileEntity instanceof IHeatConductor)) {
                            break;
                        }
                        IHeatConductor energyConductor = (IHeatConductor) tileEntity;
                        energyPath.conductors.add(energyConductor);
                        energyBlockLink = reachedTileEntities.get(tileEntity);
                        if (energyBlockLink != null) {
                            continue;
                        }
                        IC2.platform.displayError(
                                "An energy network pathfinding entry is corrupted.\nThis could happen due to incorrect Minecraft behavior or a bug.\n\n(Technical information: energyBlockLink, tile entities below)\nE:"
                        );
                    }
                }
                energyPaths.add(energyPath);
            }
        }
        return energyPaths;
    }


    public IHeatTile getNeighbor(IHeatTile tile, EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        return getTileEntity(((TileEntity) tile).getPos().offset(dir));
    }

    private List<EnergyTarget> getValidReceivers(IHeatTile emitter, boolean reverse) {
        List<EnergyTarget> validReceivers = new LinkedList<>();
        for (EnumFacing direction : directions) {
            IHeatTile target2 = getNeighbor(emitter, direction);
            if (target2 != null) {
                EnumFacing inverseDirection2 = direction.getOpposite();
                if (reverse) {
                    if (emitter instanceof IHeatAcceptor && target2 instanceof IHeatEmitter) {
                        IHeatEmitter sender2 = (IHeatEmitter) target2;
                        IHeatAcceptor receiver2 = (IHeatAcceptor) emitter;
                        if (sender2.emitsHeatTo(receiver2, inverseDirection2) && receiver2.acceptsHeatFrom(sender2, direction)) {
                            validReceivers.add(new EnergyTarget(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof IHeatEmitter && target2 instanceof IHeatAcceptor) {
                    IHeatEmitter sender2 = (IHeatEmitter) emitter;
                    IHeatAcceptor receiver2 = (IHeatAcceptor) target2;
                    if (sender2.emitsHeatTo(receiver2, direction) && receiver2.acceptsHeatFrom(sender2, inverseDirection2)) {
                        validReceivers.add(new EnergyTarget(target2, inverseDirection2));
                    }
                }
            }
        }
        return validReceivers;
    }

    public List<IHeatSource> discoverFirstPathOrSources(IHeatTile par1) {
        Set<IHeatTile> reached = new HashSet<>();
        List<IHeatSource> result = new ArrayList<>();
        List<IHeatTile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            IHeatTile tile = workList.remove(0);
            TileEntity te = (TileEntity) tile;
            if (!te.isInvalid()) {
                List<EnergyTarget> targets = getValidReceivers(tile, true);
                for (EnergyTarget energyTarget : targets) {
                    IHeatTile target = energyTarget.tileEntity;
                    if (target != par1 &&
                            !reached.contains(target)) {
                        reached.add(target);
                        if (target instanceof IHeatSource) {
                            result.add((IHeatSource) target);
                            continue;
                        }
                        if (target instanceof IHeatConductor) {
                            workList.add(target);
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
                List<IHeatTile> tiles = this.waitingList.getPathTiles();
                for (IHeatTile tile : tiles) {
                    List<IHeatSource> sources = discoverFirstPathOrSources(tile);
                    if (sources.size() > 0) {
                        this.energySourceToEnergyPathMap.removeAll(sources);
                    }
                }
                this.waitingList.clear();
            }
        }
        for (Map.Entry<BlockPos, IHeatSource> entry : (new HashMap<>(this.sources)).entrySet()) {
            if (entry != null) {
                IHeatSource source = entry.getValue();
                if (source != null) {

                    double offer = Math.min(source
                            .getOfferedHeat(), 2.147483647E9D);
                    if (offer > 0.0D) {
                        double i = 0;
                        for (double packetAmount = 1; i < packetAmount; i++) {
                            offer = Math.min(source
                                    .getOfferedHeat(), 2.147483647E9D);
                            if (offer < 1.0D) {
                                break;
                            }
                            double removed = offer - emitEnergyFrom(source, offer);
                            if (removed <= 0.0D) {
                                break;
                            }
                            source.drawHeat(removed);
                        }
                    }
                }
            }
        }
    }


    public IHeatTile getTileEntity(BlockPos pos) {
        if (this.registeredTiles.containsKey(pos)) {
            return this.registeredTiles.get(pos);
        }
        return null;
    }


    void update(int x, int y, int z) {
        for (EnumFacing dir : EnumFacing.values()) {
            if (this.world.isChunkGeneratedAt(x + dir.getFrontOffsetX() >> 4, z + dir.getFrontOffsetZ() >> 4)) {
                BlockPos pos = (new BlockPos(x, y, z)).offset(dir);
                this.world.neighborChanged(pos, Blocks.AIR, pos);
            }
        }
    }

    public void onUnload() {
        this.energySourceToEnergyPathMap.clear();
        this.registeredTiles.clear();
        this.sources.clear();
        this.waitingList.clear();
    }


    static class EnergyTarget {

        final IHeatTile tileEntity;

        final EnumFacing direction;

        EnergyTarget(IHeatTile tileEntity, EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }

    }

    static class EnergyPath {

        final Set<IHeatConductor> conductors = new HashSet<>();
        IHeatTile target = null;
        EnumFacing targetDirection;

    }

    static class EnergyBlockLink {

        final EnumFacing direction;

        final double loss;

        EnergyBlockLink(EnumFacing direction, double loss) {
            this.direction = direction;
            this.loss = loss;
        }

    }

    static class EnergyPathMap {

        final Map<IHeatSource, List<EnergyPath>> senderPath;

        EnergyPathMap() {
            this.senderPath = new HashMap<>();

        }

        public void put(final IHeatSource par1, final List<EnergyPath> par2) {
            this.senderPath.put(par1, par2);


        }

        public boolean containsKey(final IHeatSource par1) {
            return this.senderPath.containsKey(par1);
        }

        public List<EnergyPath> get(final IHeatSource par1) {
            return this.senderPath.get(par1);
        }

        public void remove(final IHeatSource par1) {
            this.senderPath.remove(par1);
        }

        public void removeAll(final List<IHeatSource> par1) {
            for (IHeatSource IHeatSource : par1) {
                this.remove(IHeatSource);
            }
        }


        public List<IHeatSource> getSources(final IHeatAcceptor par1) {
            final List<IHeatSource> source = new ArrayList<>();
            for (final Map.Entry<IHeatSource, List<EnergyPath>> entry : this.senderPath.entrySet()) {
                if (source.contains(entry.getKey())) {
                    continue;
                }
                for (EnergyPath path : entry.getValue()) {
                    if ((!(par1 instanceof IHeatConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof IHeatSink) || path.target != par1)) {
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

        final List<IHeatTile> tiles = new ArrayList<>();

        public boolean contains(IHeatTile par1) {
            return this.tiles.contains(par1);
        }

        public void add(IHeatTile par1) {
            this.tiles.add(par1);
        }

        public void remove(IHeatTile par1) {
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

        final List<HeatNetLocal.PathLogic> paths = new ArrayList<>();

        public void onTileEntityAdded(List<HeatNetLocal.EnergyTarget> around, IHeatTile tile) {
            if (around.isEmpty() || this.paths.isEmpty()) {
                createNewPath(tile);
                return;
            }
            boolean found = false;
            List<HeatNetLocal.PathLogic> logics = new ArrayList<>();
            for (HeatNetLocal.PathLogic logic : this.paths) {
                if (logic.contains(tile)) {
                    found = true;
                    if (tile instanceof IHeatConductor) {
                        logics.add(logic);
                    }
                    continue;
                }
                for (HeatNetLocal.EnergyTarget target : around) {
                    if (logic.contains(target.tileEntity)) {
                        found = true;
                        logic.add(tile);
                        if (target.tileEntity instanceof IHeatConductor) {
                            logics.add(logic);
                        }
                    }
                }
            }
            if (logics.size() > 1 && tile instanceof IHeatConductor) {
                HeatNetLocal.PathLogic newLogic = new HeatNetLocal.PathLogic();
                for (HeatNetLocal.PathLogic logic2 : logics) {
                    this.paths.remove(logic2);
                    for (IHeatTile toMove : logic2.tiles) {
                        if (!newLogic.contains(toMove)) {
                            newLogic.add(toMove);
                        }
                    }
                    logic2.clear();
                }
                this.paths.add(newLogic);
            }
            if (!found) {
                createNewPath(tile);
            }
        }

        public void onTileEntityRemoved(IHeatTile par1) {
            if (this.paths.isEmpty()) {
                return;
            }
            List<IHeatTile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); i++) {
                HeatNetLocal.PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (IHeatTile tile : toRecalculate) {
                onTileEntityAdded(HeatNetLocal.this.getValidReceivers(tile, true), tile);
            }
        }

        public void createNewPath(IHeatTile par1) {
            HeatNetLocal.PathLogic logic = new HeatNetLocal.PathLogic();
            logic.add(par1);
            this.paths.add(logic);
        }

        public void clear() {
            if (this.paths.isEmpty()) {
                return;
            }
            for (HeatNetLocal.PathLogic path : this.paths) {
                path.clear();
            }
            this.paths.clear();
        }

        public boolean hasWork() {
            return (this.paths.size() > 0);
        }

        public List<IHeatTile> getPathTiles() {
            List<IHeatTile> tiles = new ArrayList<>();
            for (HeatNetLocal.PathLogic path : this.paths) {
                IHeatTile tile = path.getRepresentingTile();
                if (tile != null) {
                    tiles.add(tile);
                }
            }
            return tiles;
        }

    }

}
