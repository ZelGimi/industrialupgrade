package com.denfop.cool;

import aroma1997.uncomplication.enet.ChunkCoordinates;
import com.denfop.Config;
import com.denfop.api.Recipes;
import com.denfop.api.cooling.*;
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

public class CoolNetLocal {

    public static double minConductionLoss;
    public static EnergyTransferList list;
    private static EnumFacing[] directions;

    static {
        CoolNetLocal.minConductionLoss = 1.0E-4;
        CoolNetLocal.directions = EnumFacing.values();
    }

    private final World world;
    private final EnergyPathMap energySourceToEnergyPathMap;
    private final Map<ChunkCoordinates, ICoolTile> registeredTiles;
    private final Map<ChunkCoordinates, ICoolSource> sources;
    private final WaitingList waitingList;
    private final Map<Chunk, List<ICoolSink>> registeredTilesInChunk;
    private final Map<Chunk, List<ICoolWirelessSource>> registeredWirelessSourcesInChunk;

    CoolNetLocal(final World world) {
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

    public void addTile(ICoolTile tile1) {
        this.addTileEntity(coords((TileEntity) tile1), tile1);

    }

    public List<ICoolSink> getListCoolInChunk(Chunk chunk) {
        return registeredTilesInChunk.containsKey(chunk) ? registeredTilesInChunk.get(chunk) : new ArrayList<>();
    }

    public void addTileEntity(final ChunkCoordinates coords, final ICoolTile tile) {
        if (this.registeredTiles.containsKey(coords)) {
            return;
        }
        this.registeredTiles.put(coords, tile);
        TileEntity te = (TileEntity) tile;
        Chunk chunk = te.getWorld().getChunkFromBlockCoords(te.getPos());
        if (tile instanceof ICoolSink) {
            if (this.registeredTilesInChunk.containsKey(chunk)) {
                List<ICoolSink> lst = this.registeredTilesInChunk.get(chunk);
                lst.add((ICoolSink) tile);
                if (registeredWirelessSourcesInChunk.containsKey(chunk)) {
                    List<ICoolWirelessSource> lst1 = this.registeredWirelessSourcesInChunk.get(chunk);
                    for (ICoolWirelessSource wirelessSource : lst1) {
                        wirelessSource.setList(lst);
                    }

                }
            } else {
                List<ICoolSink> l = new ArrayList<>();
                l.add((ICoolSink) tile);
                this.registeredTilesInChunk.put(chunk, l);
            }
        } else {
            if (tile instanceof ICoolWirelessSource) {
                if (this.registeredWirelessSourcesInChunk.containsKey(chunk)) {
                    List<ICoolWirelessSource> lst = this.registeredWirelessSourcesInChunk.get(chunk);
                    lst.add((ICoolWirelessSource) tile);
                    ((ICoolWirelessSource) tile).setList(getListCoolInChunk(((ICoolWirelessSource) tile).getChunk()));
                } else {
                    List<ICoolWirelessSource> l = new ArrayList<>();
                    l.add((ICoolWirelessSource) tile);
                    this.registeredWirelessSourcesInChunk.put(chunk, l);
                }
            }
        }
        this.update(coords.x, coords.y, coords.z);
        if (tile instanceof ICoolAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), tile);
        }
        if (tile instanceof ICoolSource) {
            this.sources.put(coords, (ICoolSource) tile);
        }
    }

    public void removeTile(ICoolTile tile) {
        this.removeTileEntity(coords((TileEntity) tile), tile);
        TileEntity te = (TileEntity) tile;
        Chunk chunk = te.getWorld().getChunkFromBlockCoords(te.getPos());
        if (tile instanceof ICoolSink) {
            if (this.registeredTilesInChunk.containsKey(chunk)) {
                List<ICoolSink> lst = this.registeredTilesInChunk.get(chunk);
                lst.remove(tile);
            }
        }
        if (tile instanceof ICoolWirelessSource) {
            if (this.registeredWirelessSourcesInChunk.containsKey(chunk)) {
                List<ICoolWirelessSource> lst = this.registeredWirelessSourcesInChunk.get(chunk);
                lst.remove((ICoolWirelessSource) tile);
            }
        }

    }

    public void removeTileEntity(final ChunkCoordinates coords, ICoolTile tile) {
        if (!this.registeredTiles.containsKey(coords)) {
            return;
        }
        this.registeredTiles.remove(coords);
        this.update(coords.x, coords.y, coords.z);
        if (tile instanceof ICoolAcceptor) {
            this.energySourceToEnergyPathMap.removeAll(this.energySourceToEnergyPathMap.getSources((ICoolAcceptor) tile));
            this.waitingList.onTileEntityRemoved(tile);
        }
        if (tile instanceof ICoolSource) {
            this.sources.remove(coords);
            this.energySourceToEnergyPathMap.remove((ICoolSource) tile);
        }
    }

    public double emitEnergyFrom(final ChunkCoordinates coords, final ICoolSource energySource, double amount) {
        if (!this.registeredTiles.containsKey(coords)) {
            return amount;
        }
        if (!this.energySourceToEnergyPathMap.containsKey(energySource)) {
            final EnergyPathMap energySourceToEnergyPathMap = this.energySourceToEnergyPathMap;
            energySourceToEnergyPathMap.put(energySource, this.discover(
                    energySource,
                    EnergyTransferList.getMaxEnergy(energySource, energySource.getOfferedCool())
            ));
        }
        List<EnergyPath> activeEnergyPaths = new Vector<>();
        double totalInvLoss = 0.0;
        for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.get(energySource)) {
            assert energyPath.target instanceof ICoolSink;
            final ICoolSink energySink = (ICoolSink) energyPath.target;
            if (energySink.getDemandedCool() <= 0.0) {
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
                final ICoolSink energySink2 = (ICoolSink) energyPath2.target;
                final double energyProvided = Math.floor(Math.round(amount / totalInvLoss / energyPath2.loss));
                final double energyLoss = Math.floor(energyPath2.loss);
                if (energyProvided > energyLoss) {
                    final double providing = energyProvided - energyLoss;

                    double adding = Math.min(providing, energySink2.getDemandedCool());
                    if (adding <= 0.0 && EnergyTransferList.hasOverrideInput(energySink2)) {
                        adding = EnergyTransferList.getOverrideInput(energySink2);
                    }
                    if (adding <= 0.0) {
                        continue;
                    }

                    double energyReturned = energySink2.injectCool(energyPath2.targetDirection, adding, 0);
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
                    for (final ICoolConductor energyConductor : energyPath3.conductors) {
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
                    for (final ICoolConductor energyConductor2 : energyPath3.conductors) {
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
                for (final ICoolConductor energyConductor3 : energyPath3.conductors) {
                    if (energyInjected2 >= energyConductor3.getConductorBreakdownEnergy() && !Config.enableIC2EasyMode) {
                        energyConductor3.removeConductor();
                    }
                }
            }
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

    private List<EnergyPath> discover(final ICoolTile emitter, final double lossLimit) {
        final Map<ICoolTile, EnergyBlockLink> reachedTileEntities = new HashMap<>();
        final LinkedList<ICoolTile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(emitter);
        while (!tileEntitiesToCheck.isEmpty()) {
            final ICoolTile currentTileEntity = tileEntitiesToCheck.remove();
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
                        if (validReceiver.tileEntity instanceof ICoolConductor) {
                            additionalLoss = ((ICoolConductor) validReceiver.tileEntity).getConductionLoss();
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
                energyPath.loss = Math.max(energyBlockLink.loss, 0.1);
                energyPath.target = tileEntity;
                energyPath.targetDirection = energyBlockLink.direction;
                if (emitter instanceof ICoolSource) {
                    while (true) {
                        TileEntity te = (TileEntity) tileEntity;
                        tileEntity = this.getTileEntity(te.getPos().offset(energyBlockLink.direction));
                        if (tileEntity == emitter) {
                            break;
                        }
                        if (!(tileEntity instanceof ICoolConductor)) {
                            break;
                        }
                        final ICoolConductor energyConductor = (ICoolConductor) tileEntity;
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

    private boolean conductorToWeak(final Set<ICoolConductor> par1, final double energyToSend) {
        boolean flag = false;
        for (final ICoolConductor cond : par1) {
            if (cond.getConductorBreakdownEnergy() <= energyToSend) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public ICoolTile getNeighbor(final ICoolTile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        return this.getTileEntity(((TileEntity) tile).getPos().offset(dir));
    }

    private List<EnergyTarget> getValidReceivers(final ICoolTile emitter, final boolean reverse) {
        final List<EnergyTarget> validReceivers = new LinkedList<>();
        for (final EnumFacing direction : CoolNetLocal.directions) {

            final ICoolTile target2 = getNeighbor(emitter, direction);
            TileEntity te1 = (TileEntity) target2;
            if (target2 != null && this.registeredTiles.containsKey(coords(te1))) {
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
                    if (sender2.emitsCoolTo(receiver2, direction) && receiver2.acceptsCoolFrom(
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

    public List<ICoolSource> discoverFirstPathOrSources(final ICoolTile par1) {
        final Set<ICoolTile> reached = new HashSet<>();
        final List<ICoolSource> result = new ArrayList<>();
        final List<ICoolTile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final ICoolTile tile = workList.remove(0);
            final TileEntity te = (TileEntity) tile;
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
        for (Map.Entry<ChunkCoordinates, ICoolSource> entry : new HashMap<>(this.sources).entrySet()) {
            if (entry != null) {
                final ICoolSource source = entry.getValue();
                if (source != null) {
                    if (this.energySourceToEnergyPathMap.containsKey(source)) {
                        for (final EnergyPath path : this.energySourceToEnergyPathMap.get(source)) {
                            path.totalEnergyConducted = 0L;
                            path.maxSendedEnergy = 0L;
                        }
                    }

                    double offer = Math.min(
                            source.getOfferedCool(),
                            Integer.MAX_VALUE
                    );
                    if (offer > 0) {
                        for (double packetAmount = this.getPacketAmount(source), i = 0; i < packetAmount; ++i) {
                            offer = Math.min(
                                    source.getOfferedCool(),
                                    Integer.MAX_VALUE
                            );
                            if (offer < 1) {
                                break;
                            }
                            final double removed = offer - this.emitEnergyFrom(entry.getKey(), source, offer);
                            if (removed <= 0) {
                                break;
                            }

                            source.drawCool(removed);
                        }
                    }
                }
            }
        }
    }

    private double getPacketAmount(final ICoolSource source) {

        return 1;
    }

    public void explodeTiles(final ICoolSink sink) {

    }

    public ICoolTile getTileEntity(final int x, final int y, final int z) {
        final ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
        if (this.registeredTiles.containsKey(coords)) {
            return this.registeredTiles.get(coords);
        }
        return null;
    }

    public ICoolTile getTileEntity(BlockPos pos) {
        final ChunkCoordinates coords = new ChunkCoordinates(pos);
        if (this.registeredTiles.containsKey(coords)) {
            return this.registeredTiles.get(coords);
        }
        return null;
    }

    public NodeCoolStats getNodeStats(final ICoolTile tile) {
        final double emitted = this.getTotalEnergyEmitted(tile);
        final double received = this.getTotalEnergySunken(tile);
        return new NodeCoolStats(received, emitted);
    }

    private double getVoltage(final ICoolTile tileEntity) {
        double voltage = 0.0;
        if (tileEntity instanceof ICoolSource && this.energySourceToEnergyPathMap.containsKey((ICoolSource) tileEntity)) {
            for (final EnergyPath energyPath2 : this.energySourceToEnergyPathMap.get((ICoolSource) tileEntity)) {
                voltage = Math.max(voltage, (double) energyPath2.maxSendedEnergy);
            }
        }
        if (tileEntity instanceof ICoolConductor || tileEntity instanceof ICoolSink) {
            for (final EnergyPath energyPath3 : this.energySourceToEnergyPathMap.getPaths((ICoolAcceptor) tileEntity)) {
                if ((tileEntity instanceof ICoolSink && energyPath3.target == tileEntity) || (tileEntity instanceof ICoolConductor && energyPath3.conductors.contains(
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

    public void transferTemperatureWireless(ICoolWirelessSource source) {
        if (!this.registeredTilesInChunk.containsKey(source.getChunk())) {
            return;
        }
        List<ICoolSink> tiles = this.registeredTilesInChunk.get(source.getChunk());
        if (tiles == null) {
            return;
        }

    // TODO: check
     /*   for (ICoolSink tile : tiles) {
            if (!source.getITemperature().reveiver()) {
                Recipes.mechanism.transfer(tile.getITemperature(), source.getITemperature());
            }
        }

      */
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
        final double loss;

        EnergyBlockLink(final EnumFacing direction, final double loss) {
            this.direction = direction;
            this.loss = loss;
        }

    }

    static class EnergyPath {

        final Set<ICoolConductor> conductors;
        ICoolTile target;
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

        final Map<ICoolSource, List<EnergyPath>> senderPath;
        final Map<EnergyPath, ICoolSource> pathToSender;

        EnergyPathMap() {
            this.senderPath = new HashMap<>();
            this.pathToSender = new HashMap<>();
        }

        public void put(final ICoolSource par1, final List<EnergyPath> par2) {
            this.senderPath.put(par1, par2);
            for (EnergyPath energyPath : par2) {
                this.pathToSender.put(energyPath, par1);
            }
        }

        public boolean containsKey(final ICoolSource par1) {
            return this.senderPath.containsKey(par1);
        }

        public List<EnergyPath> get(final ICoolSource par1) {
            return this.senderPath.get(par1);
        }

        public void remove(final ICoolSource par1) {
            final List<EnergyPath> paths = this.senderPath.remove(par1);
            if (paths != null) {
                for (EnergyPath path : paths) {
                    this.pathToSender.remove(path);
                }
            }
        }

        public void removeAll(final List<ICoolSource> par1) {
            for (ICoolSource iEnergySource : par1) {
                this.remove(iEnergySource);
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
            for (final Map.Entry<EnergyPath, ICoolSource> entry : this.pathToSender.entrySet()) {
                if (source.contains(entry.getValue())) {
                    continue;
                }
                final EnergyPath path = entry.getKey();
                if ((!(par1 instanceof ICoolConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof ICoolSink) || path.target != par1)) {
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
