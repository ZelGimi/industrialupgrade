package com.denfop.qe;

import aroma1997.uncomplication.enet.ChunkCoordinates;
import com.denfop.Config;
import com.denfop.api.Recipes;
import com.denfop.api.qe.*;
import com.denfop.tiles.mechanism.TileEntityCable;
import ic2.core.IC2;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class QENetLocal {

    public static double minConductionLoss;
    public static EnergyTransferList list;
    private static EnumFacing[] directions;

    static {
        QENetLocal.minConductionLoss = 1.0E-4;
        QENetLocal.directions = EnumFacing.values();
    }

    private final World world;
    private final EnergyPathMap energySourceToEnergyPathMap;
    private final Map<ChunkCoordinates, IQETile> registeredTiles;
    private final Map<ChunkCoordinates, IQESource> sources;
    private final WaitingList waitingList;
    private final Map<Chunk, List<IQESink>> registeredTilesInChunk;
    private final Map<Chunk, List<IQEWirelessSource>> registeredWirelessSourcesInChunk;

    QENetLocal(final World world) {
        this.energySourceToEnergyPathMap = new EnergyPathMap();
        this.registeredTiles = new HashMap<>();
        this.sources = new HashMap<>();
        this.waitingList = new WaitingList();
        this.world = world;
        this.registeredTilesInChunk = new HashMap<>();
        this.registeredWirelessSourcesInChunk = new HashMap<>();
    }

    public static ChunkCoordinates coords(final TileEntity par1) {
        if (par1 == null) {
            return null;
        }
        return new ChunkCoordinates(par1.getPos());
    }

    public void addTile(IQETile tile1) {
        this.addTileEntity(coords((TileEntity) tile1), tile1);

    }

    public List<IQESink> getListQEInChunk(Chunk chunk) {
        return registeredTilesInChunk.containsKey(chunk) ? registeredTilesInChunk.get(chunk) : new ArrayList<>();
    }

    public void addTileEntity(final ChunkCoordinates coords, final IQETile tile) {
        if (this.registeredTiles.containsKey(coords)) {
            return;
        }
        this.registeredTiles.put(coords, tile);
        TileEntity te = (TileEntity) tile;
        Chunk chunk = te.getWorld().getChunkFromBlockCoords(te.getPos());
        if (tile instanceof IQESink) {
            if (this.registeredTilesInChunk.containsKey(chunk)) {
                List<IQESink> lst = this.registeredTilesInChunk.get(chunk);
                lst.add((IQESink) tile);
                if (registeredWirelessSourcesInChunk.containsKey(chunk)) {
                    List<IQEWirelessSource> lst1 = this.registeredWirelessSourcesInChunk.get(chunk);
                    for (IQEWirelessSource wirelessSource : lst1) {
                        wirelessSource.setList(lst);
                    }

                }
            } else {
                List<IQESink> l = new ArrayList<>();
                l.add((IQESink) tile);
                this.registeredTilesInChunk.put(chunk, l);
            }
        } else {
            if (tile instanceof IQEWirelessSource) {
                if (this.registeredWirelessSourcesInChunk.containsKey(chunk)) {
                    List<IQEWirelessSource> lst = this.registeredWirelessSourcesInChunk.get(chunk);
                    lst.add((IQEWirelessSource) tile);
                    ((IQEWirelessSource) tile).setList(getListQEInChunk(((IQEWirelessSource) tile).getChunk()));
                } else {
                    List<IQEWirelessSource> l = new ArrayList<>();
                    l.add((IQEWirelessSource) tile);
                    this.registeredWirelessSourcesInChunk.put(chunk, l);
                }
            }
        }
        this.update(coords.x, coords.y, coords.z);
        if (tile instanceof IQEAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), tile);
        }
        if (tile instanceof IQESource) {
            this.sources.put(coords, (IQESource) tile);
        }
    }

    public void removeTile(IQETile tile) {
        this.removeTileEntity(coords((TileEntity) tile), tile);
        TileEntity te = (TileEntity) tile;
        Chunk chunk = te.getWorld().getChunkFromBlockCoords(te.getPos());
        if (tile instanceof IQESink) {
            if (this.registeredTilesInChunk.containsKey(chunk)) {
                List<IQESink> lst = this.registeredTilesInChunk.get(chunk);
                lst.remove(tile);
            }
        }
        if (tile instanceof IQEWirelessSource) {
            if (this.registeredWirelessSourcesInChunk.containsKey(chunk)) {
                List<IQEWirelessSource> lst = this.registeredWirelessSourcesInChunk.get(chunk);
                lst.remove((IQEWirelessSource) tile);
            }
        }

    }

    public void removeTileEntity(final ChunkCoordinates coords, IQETile tile) {
        if (!this.registeredTiles.containsKey(coords)) {
            return;
        }
        this.registeredTiles.remove(coords);
        this.update(coords.x, coords.y, coords.z);
        if (tile instanceof IQEAcceptor) {
            this.energySourceToEnergyPathMap.removeAll(this.energySourceToEnergyPathMap.getSources((IQEAcceptor) tile));
            this.waitingList.onTileEntityRemoved(tile);
        }
        if (tile instanceof IQESource) {
            this.sources.remove(coords);
            this.energySourceToEnergyPathMap.remove((IQESource) tile);
        }
    }

    public double emitEnergyFrom(final ChunkCoordinates coords, final IQESource energySource, double amount) {
        if (!this.registeredTiles.containsKey(coords)) {
            return amount;
        }
        if (!this.energySourceToEnergyPathMap.containsKey(energySource)) {
            final EnergyPathMap energySourceToEnergyPathMap = this.energySourceToEnergyPathMap;
            energySourceToEnergyPathMap.put(energySource, this.discover(
                    energySource,
                    EnergyTransferList.getMaxEnergy(energySource, energySource.getOfferedQE())
            ));
        }
        List<EnergyPath> activeEnergyPaths = new Vector<>();
        double totalInvLoss = 0.0;
        for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.get(energySource)) {
            assert energyPath.target instanceof IQESink;
            final IQESink energySink = (IQESink) energyPath.target;
            if (energySink.getDemandedQE() <= 0.0) {
                continue;
            }
            if (energyPath.loss >= amount) {
                continue;
            }
            if (Config.enableIC2EasyMode && this.conductorToWeak(energyPath.conductors, amount)) {
                continue;
            }
            totalInvLoss += 1.0 / energyPath.loss;
            activeEnergyPaths.add(energyPath);
        }
        Collections.shuffle(activeEnergyPaths);
        for (double i = activeEnergyPaths.size() - amount; i > 0; --i) {
            final EnergyPath removedEnergyPath = activeEnergyPaths.remove(activeEnergyPaths.size() - 1);
            totalInvLoss -= 1.0 / removedEnergyPath.loss;
        }
        final Map<EnergyPath, Double> suppliedEnergyPaths = new HashMap<>();
        while (!activeEnergyPaths.isEmpty() && amount > 0) {
            double energyConsumed = 0;
            double newTotalInvLoss = 0.0;
            final List<EnergyPath> currentActiveEnergyPaths = activeEnergyPaths;
            activeEnergyPaths = new Vector<>();
            for (final EnergyPath energyPath2 : currentActiveEnergyPaths) {
                final IQESink energySink2 = (IQESink) energyPath2.target;
                final double energyProvided = Math.floor(Math.round(amount / totalInvLoss / energyPath2.loss));
                final double energyLoss = Math.floor(energyPath2.loss);
                if (energyProvided > energyLoss) {
                    final double providing = energyProvided - energyLoss;

                    double adding = Math.min(providing, energySink2.getDemandedQE());
                    if (adding <= 0.0 && EnergyTransferList.hasOverrideInput(energySink2)) {
                        adding = EnergyTransferList.getOverrideInput(energySink2);
                    }
                    if (adding <= 0.0) {
                        continue;
                    }

                    double energyReturned = energySink2.injectQE(energyPath2.targetDirection, adding, 0);
                    if (energyReturned == 0.0) {
                        activeEnergyPaths.add(energyPath2);
                        newTotalInvLoss += 1.0 / energyPath2.loss;
                    } else if (energyReturned >= energyProvided - energyLoss) {
                        energyReturned = energyProvided - energyLoss;
                    }
                    energyConsumed += (adding - energyReturned + energyLoss);
                    final double energyInjected = (adding - energyReturned);
                    if (!suppliedEnergyPaths.containsKey(energyPath2)) {
                        suppliedEnergyPaths.put(energyPath2, energyInjected);
                    } else {
                        suppliedEnergyPaths.put(energyPath2, energyInjected + suppliedEnergyPaths.get(energyPath2));
                    }

                } else {
                    activeEnergyPaths.add(energyPath2);
                    newTotalInvLoss += 1.0 / energyPath2.loss;
                }
            }
            if (energyConsumed == 0 && !activeEnergyPaths.isEmpty()) {
                final EnergyPath removedEnergyPath2 = activeEnergyPaths.remove(activeEnergyPaths.size() - 1);
                newTotalInvLoss -= 1.0 / removedEnergyPath2.loss;
            }
            totalInvLoss = newTotalInvLoss;
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
                final List<EntityLivingBase> entitiesNearEnergyPath =
                        this.world.getEntitiesWithinAABB(
                                EntityLivingBase.class,
                                new AxisAlignedBB(energyPath3.minX - 1, energyPath3.minY - 1, energyPath3.minZ - 1,
                                        energyPath3.maxX + 2, energyPath3.maxY + 2, energyPath3.maxZ + 2
                                )
                        );
                for (final EntityLivingBase entityLiving : entitiesNearEnergyPath) {
                    double maxShockEnergy = 0;
                    for (final IQEConductor energyConductor : energyPath3.conductors) {
                        final TileEntity te = (TileEntity) energyConductor;
                        if (entityLiving.boundingBox.intersects(new AxisAlignedBB(te.getPos().getX() - 1, te.getPos().getY() - 1,
                                te.getPos().getZ() - 1,
                                te.getPos().getX() + 2, te.getPos().getY() + 2, te.getPos().getZ() + 2
                        ))) {
                            if (te instanceof TileEntityCable) {
                                continue;
                            }

                            final double shockEnergy = (energyInjected2 - energyConductor.getInsulationEnergyAbsorption());
                            if (shockEnergy > maxShockEnergy) {
                                maxShockEnergy = shockEnergy;
                            }

                            if (energyConductor.getInsulationEnergyAbsorption() == energyPath3.minInsulationEnergyAbsorption) {
                                break;
                            }
                        }
                    }

                }
                if (energyInjected2 >= energyPath3.minInsulationBreakdownEnergy) {
                    for (final IQEConductor energyConductor2 : energyPath3.conductors) {
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
                for (final IQEConductor energyConductor3 : energyPath3.conductors) {
                    if (energyInjected2 >= energyConductor3.getConductorBreakdownEnergy() && !Config.enableIC2EasyMode) {
                        energyConductor3.removeConductor();
                    }
                }
            }
        }
        return amount;
    }

    public double getTotalEnergyEmitted(final IQETile tileEntity) {
        double ret = 0.0;
        if (tileEntity instanceof IQEConductor) {
            for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((IQEAcceptor) tileEntity)) {
                if (energyPath.conductors.contains(tileEntity)) {
                    ret += energyPath.totalEnergyConducted;
                }
            }
        }
        if (tileEntity instanceof IQESource && this.energySourceToEnergyPathMap.containsKey((IQESource) tileEntity)) {
            for (final EnergyPath energyPath2 : this.energySourceToEnergyPathMap.get((IQESource) tileEntity)) {
                ret += energyPath2.totalEnergyConducted;
            }
        }
        return ret;
    }

    public double getTotalEnergySunken(final IQETile tileEntity) {
        double ret = 0.0;
        if (tileEntity instanceof IQEConductor || tileEntity instanceof IQESink) {
            for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((IQEAcceptor) tileEntity)) {
                if ((tileEntity instanceof IQESink && energyPath.target == tileEntity) || (tileEntity instanceof IQEConductor && energyPath.conductors.contains(
                        tileEntity))) {
                    ret += energyPath.totalEnergyConducted;
                }
            }
        }
        return ret;
    }

    private List<EnergyPath> discover(final IQETile emitter, final double lossLimit) {
        final Map<IQETile, EnergyBlockLink> reachedTileEntities = new HashMap<>();
        final LinkedList<IQETile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(emitter);
        while (!tileEntitiesToCheck.isEmpty()) {
            final IQETile currentTileEntity = tileEntitiesToCheck.remove();
            TileEntity tile = (TileEntity) emitter;
            if (!tile.isInvalid()) {
                double currentLoss = 0.0;
                if (this.registeredTiles.get(coords(tile)) != null && this.registeredTiles.get(coords(tile)) != emitter && reachedTileEntities.containsKey(
                        currentTileEntity)) {
                    currentLoss = reachedTileEntities.get(currentTileEntity).loss;
                }
                final List<EnergyTarget> validReceivers = this.getValidReceivers(currentTileEntity, false);
                for (final EnergyTarget validReceiver : validReceivers) {
                    if (validReceiver.tileEntity != emitter) {
                        double additionalLoss = 0.0;
                        if (validReceiver.tileEntity instanceof IQEConductor) {
                            additionalLoss = ((IQEConductor) validReceiver.tileEntity).getConductionLoss();
                            if (additionalLoss < 1.0E-4) {
                                additionalLoss = 1.0E-4;
                            }
                            if (currentLoss + additionalLoss >= lossLimit) {
                                continue;
                            }
                        }
                        if (reachedTileEntities.containsKey(validReceiver.tileEntity) && reachedTileEntities.get(validReceiver.tileEntity).loss <= currentLoss + additionalLoss) {
                            continue;
                        }
                        reachedTileEntities.put(validReceiver.tileEntity, new EnergyBlockLink(
                                validReceiver.direction,
                                currentLoss + additionalLoss
                        ));
                        if (!(validReceiver.tileEntity instanceof IQEConductor)) {
                            continue;
                        }
                        tileEntitiesToCheck.remove(validReceiver.tileEntity);
                        tileEntitiesToCheck.add(validReceiver.tileEntity);
                    }
                }
            }
        }
        final List<EnergyPath> energyPaths = new LinkedList<>();
        for (final Map.Entry<IQETile, EnergyBlockLink> entry : reachedTileEntities.entrySet()) {
            IQETile tileEntity = entry.getKey();
            if ((tileEntity instanceof IQESink)) {
                EnergyBlockLink energyBlockLink = entry.getValue();
                final EnergyPath energyPath = new EnergyPath();
                energyPath.loss = Math.max(energyBlockLink.loss, 0.1);
                energyPath.target = tileEntity;
                energyPath.targetDirection = energyBlockLink.direction;
                if (emitter instanceof IQESource) {
                    while (true) {
                        TileEntity te = (TileEntity) tileEntity;
                        tileEntity = this.getTileEntity(te.getPos().offset(energyBlockLink.direction));
                        if (tileEntity == emitter) {
                            break;
                        }
                        if (!(tileEntity instanceof IQEConductor)) {
                            break;
                        }
                        final IQEConductor energyConductor = (IQEConductor) tileEntity;
                        if (te.getPos().getX() < energyPath.minX) {
                            energyPath.minX = te.getPos().getX();
                        }
                        if (te.getPos().getY() < energyPath.minY) {
                            energyPath.minY = te.getPos().getY();
                        }
                        if (te.getPos().getZ() < energyPath.minZ) {
                            energyPath.minZ = te.getPos().getZ();
                        }
                        if (te.getPos().getX() > energyPath.maxX) {
                            energyPath.maxX = te.getPos().getX();
                        }
                        if (te.getPos().getY() > energyPath.maxY) {
                            energyPath.maxY = te.getPos().getY();
                        }
                        if (te.getPos().getZ() > energyPath.maxZ) {
                            energyPath.maxZ = te.getPos().getZ();
                        }
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

    private boolean conductorToWeak(final Set<IQEConductor> par1, final double energyToSend) {
        boolean flag = false;
        for (final IQEConductor cond : par1) {
            if (cond.getConductorBreakdownEnergy() <= energyToSend) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public IQETile getNeighbor(final IQETile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        return this.getTileEntity(((TileEntity) tile).getPos().offset(dir));
    }

    private List<EnergyTarget> getValidReceivers(final IQETile emitter, final boolean reverse) {
        final List<EnergyTarget> validReceivers = new LinkedList<>();
        for (final EnumFacing direction : QENetLocal.directions) {

            final IQETile target2 = getNeighbor(emitter, direction);
            TileEntity te1 = (TileEntity) target2;
            if (target2 != null && this.registeredTiles.containsKey(coords(te1))) {
                final EnumFacing inverseDirection2 = direction.getOpposite();
                if (reverse) {
                    if (emitter instanceof IQEAcceptor && target2 instanceof IQEEmitter) {
                        final IQEEmitter sender2 = (IQEEmitter) target2;
                        final IQEAcceptor receiver2 = (IQEAcceptor) emitter;
                        if (sender2.emitsQETo(receiver2, inverseDirection2) && receiver2.acceptsQEFrom(
                                sender2,
                                direction
                        )) {
                            validReceivers.add(new EnergyTarget(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof IQEEmitter && target2 instanceof IQEAcceptor) {
                    final IQEEmitter sender2 = (IQEEmitter) emitter;
                    final IQEAcceptor receiver2 = (IQEAcceptor) target2;
                    if (sender2.emitsQETo(receiver2, direction) && receiver2.acceptsQEFrom(
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

    public List<IQESource> discoverFirstPathOrSources(final IQETile par1) {
        final Set<IQETile> reached = new HashSet<>();
        final List<IQESource> result = new ArrayList<>();
        final List<IQETile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final IQETile tile = workList.remove(0);
            final TileEntity te = (TileEntity) tile;
            if (!te.isInvalid()) {
                final List<EnergyTarget> targets = this.getValidReceivers(tile, true);
                for (EnergyTarget energyTarget : targets) {
                    final IQETile target = energyTarget.tileEntity;
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
        if (this.waitingList.hasWork()) {
            final List<IQETile> tiles = this.waitingList.getPathTiles();
            for (final IQETile tile : tiles) {
                final List<IQESource> sources = this.discoverFirstPathOrSources(tile);
                if (sources.size() > 0) {
                    this.energySourceToEnergyPathMap.removeAll(sources);
                }
            }
            this.waitingList.clear();
        }
        for (Map.Entry<ChunkCoordinates, IQESource> entry : new HashMap<>(this.sources).entrySet()) {
            if (entry != null) {
                final IQESource source = entry.getValue();
                if (source != null) {
                    if (this.energySourceToEnergyPathMap.containsKey(source)) {
                        for (final EnergyPath path : this.energySourceToEnergyPathMap.get(source)) {
                            path.totalEnergyConducted = 0L;
                            path.maxSendedEnergy = 0L;
                        }
                    }

                    double offer = Math.min(
                            source.getOfferedQE(),
                            Integer.MAX_VALUE
                    );
                    if (offer > 0) {
                        for (double packetAmount = this.getPacketAmount(source), i = 0; i < packetAmount; ++i) {
                            offer = Math.min(
                                    source.getOfferedQE(),
                                    Integer.MAX_VALUE
                            );
                            if (offer < 1) {
                                break;
                            }
                            final double removed = offer - this.emitEnergyFrom(entry.getKey(), source, offer);
                            if (removed <= 0) {
                                break;
                            }

                            source.drawQE(removed);
                        }
                    }
                }
            }
        }
    }

    private double getPacketAmount(final IQESource source) {

        return 1;
    }

    public void explodeTiles(final IQESink sink) {

    }

    public IQETile getTileEntity(final int x, final int y, final int z) {
        final ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
        if (this.registeredTiles.containsKey(coords)) {
            return this.registeredTiles.get(coords);
        }
        return null;
    }

    public IQETile getTileEntity(BlockPos pos) {
        final ChunkCoordinates coords = new ChunkCoordinates(pos);
        if (this.registeredTiles.containsKey(coords)) {
            return this.registeredTiles.get(coords);
        }
        return null;
    }

    public NodeQEStats getNodeStats(final IQETile tile) {
        final double emitted = this.getTotalEnergyEmitted(tile);
        final double received = this.getTotalEnergySunken(tile);
        return new NodeQEStats(received, emitted);
    }

    private double getVoltage(final IQETile tileEntity) {
        double voltage = 0.0;
        if (tileEntity instanceof IQESource && this.energySourceToEnergyPathMap.containsKey((IQESource) tileEntity)) {
            for (final EnergyPath energyPath2 : this.energySourceToEnergyPathMap.get((IQESource) tileEntity)) {
                voltage = Math.max(voltage, (double) energyPath2.maxSendedEnergy);
            }
        }
        if (tileEntity instanceof IQEConductor || tileEntity instanceof IQESink) {
            for (final EnergyPath energyPath3 : this.energySourceToEnergyPathMap.getPaths((IQEAcceptor) tileEntity)) {
                if ((tileEntity instanceof IQESink && energyPath3.target == tileEntity) || (tileEntity instanceof IQEConductor && energyPath3.conductors.contains(
                        tileEntity))) {
                    voltage = Math.max(voltage, (double) energyPath3.maxSendedEnergy);
                }
            }
        }
        return voltage;
    }

    void update(final int x, final int y, final int z) {
        for (final EnumFacing dir : EnumFacing.values()) {
            if (this.world.isChunkGeneratedAt(x + dir.getFrontOffsetX() >> 4, z + dir.getFrontOffsetZ() >> 4)) {
                BlockPos pos = new BlockPos(x, y,
                        z).offset(dir);
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
        this.registeredWirelessSourcesInChunk.clear();
    }

    public void transferTemperatureWireless(IQEWirelessSource source) {
        if (!this.registeredTilesInChunk.containsKey(source.getChunk())) {
            return;
        }
        List<IQESink> tiles = this.registeredTilesInChunk.get(source.getChunk());
        if (tiles == null) {
            return;
        }

        // TODO: check
     /*   for (IQESink tile : tiles) {
            if (!source.getITemperature().reveiver()) {
                Recipes.mechanism.transfer(tile.getITemperature(), source.getITemperature());
            }
        }

      */
    }


    static class EnergyTarget {

        final IQETile tileEntity;
        final EnumFacing direction;

        EnergyTarget(final IQETile tileEntity, final EnumFacing direction) {
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

        final Set<IQEConductor> conductors;
        IQETile target;
        EnumFacing targetDirection;
        int minX;
        int minY;
        int minZ;
        int maxX;
        int maxY;
        int maxZ;
        double loss;
        double minInsulationEnergyAbsorption;
        double minInsulationBreakdownEnergy;
        double minConductorBreakdownEnergy;
        long totalEnergyConducted;
        long maxSendedEnergy;

        EnergyPath() {
            this.target = null;
            this.conductors = new HashSet<>();
            this.minX = Integer.MAX_VALUE;
            this.minY = Integer.MAX_VALUE;
            this.minZ = Integer.MAX_VALUE;
            this.maxX = Integer.MIN_VALUE;
            this.maxY = Integer.MIN_VALUE;
            this.maxZ = Integer.MIN_VALUE;
            this.loss = 0.0;
            this.minInsulationEnergyAbsorption = Integer.MAX_VALUE;
            this.minInsulationBreakdownEnergy = Integer.MAX_VALUE;
            this.minConductorBreakdownEnergy = Integer.MAX_VALUE;
            this.totalEnergyConducted = 0L;
            this.maxSendedEnergy = 0L;
        }

    }

    static class EnergyPathMap {

        final Map<IQESource, List<EnergyPath>> senderPath;
        final Map<EnergyPath, IQESource> pathToSender;

        EnergyPathMap() {
            this.senderPath = new HashMap<>();
            this.pathToSender = new HashMap<>();
        }

        public void put(final IQESource par1, final List<EnergyPath> par2) {
            this.senderPath.put(par1, par2);
            for (EnergyPath energyPath : par2) {
                this.pathToSender.put(energyPath, par1);
            }
        }

        public boolean containsKey(final IQESource par1) {
            return this.senderPath.containsKey(par1);
        }

        public List<EnergyPath> get(final IQESource par1) {
            return this.senderPath.get(par1);
        }

        public void remove(final IQESource par1) {
            final List<EnergyPath> paths = this.senderPath.remove(par1);
            if (paths != null) {
                for (EnergyPath path : paths) {
                    this.pathToSender.remove(path);
                }
            }
        }

        public void removeAll(final List<IQESource> par1) {
            for (IQESource iEnergySource : par1) {
                this.remove(iEnergySource);
            }
        }

        public List<EnergyPath> getPaths(final IQEAcceptor par1) {
            final List<EnergyPath> paths = new ArrayList<>();
            for (final IQESource source : this.getSources(par1)) {
                if (this.containsKey(source)) {
                    paths.addAll(this.get(source));
                }
            }
            return paths;
        }

        public List<IQESource> getSources(final IQEAcceptor par1) {
            final List<IQESource> source = new ArrayList<>();
            for (final Map.Entry<EnergyPath, IQESource> entry : this.pathToSender.entrySet()) {
                if (source.contains(entry.getValue())) {
                    continue;
                }
                final EnergyPath path = entry.getKey();
                if ((!(par1 instanceof IQEConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof IQESink) || path.target != par1)) {
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

        public void onTileEntityAdded(final List<EnergyTarget> around, final IQETile tile) {
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
                    for (final EnergyTarget target : around) {
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
            for (PathLogic path : this.paths) {
                path.clear();
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
