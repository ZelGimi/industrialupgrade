package com.denfop.api.energy;

import com.denfop.api.transport.ITransportTile;
import com.denfop.api.transport.TransportNetGlobal;
import com.denfop.api.transport.event.TransportTileUnLoadEvent;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.NodeStats;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.tile.IMetaDelegate;
import ic2.api.info.ILocatable;
import ic2.core.ExplosionIC2;
import ic2.core.IC2;
import ic2.core.block.wiring.TileEntityCable;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class EnergyNetLocal {


    private final World world;
    private final EnergyPathMap energySourceToEnergyPathMap;
    private final Map<IEnergyTile, BlockPos> chunkCoordinatesMap;
    private final Map<IEnergyTile, TileEntity> energyTileTileEntityMap;
    private final List<IEnergyController> controllerList;
    private final Map<BlockPos, IEnergyTile> chunkCoordinatesIEnergyTileMap;
    private final WaitingList waitingList;
    private final List<IEnergySource> energySourceList = new ArrayList<>();
    private final SunCoef suncoef;
    private final boolean hasrestrictions;
    private final boolean explosing;
    private final boolean ignoring;
    private final boolean losing;
    private int tick;

    EnergyNetLocal(final World world) {
        this.energySourceToEnergyPathMap = new EnergyPathMap();
        this.waitingList = new WaitingList();
        this.world = world;
        this.controllerList = new ArrayList<>();
        this.chunkCoordinatesIEnergyTileMap = new HashMap<>();
        this.chunkCoordinatesMap = new HashMap<>();
        this.energyTileTileEntityMap = new HashMap<>();
        this.tick = 0;
        this.suncoef = new SunCoef(world);
        this.losing = EnergyNetGlobal.instance.getLosing();
        this.ignoring = EnergyNetGlobal.instance.needIgnoringTiers();
        this.explosing = EnergyNetGlobal.instance.needExplosion();
        this.hasrestrictions = EnergyNetGlobal.instance.hasRestrictions();
    }


    public void explodeTiles(IEnergySink sink) {

        if (sink instanceof IMetaDelegate) {
            removeTile(sink);
            IMetaDelegate meta = (IMetaDelegate) sink;
            for (IEnergyTile tile : meta.getSubTiles()) {
                explodeMachineAt(getTileFromIEnergy(tile));
            }
        } else {
            if (!(sink instanceof IEnergySource)) {
                explodeMachineAt(getTileFromIEnergy(sink));
                removeTile(sink);
            } else {
                energySourceList.add((IEnergySource) sink);
            }
        }
    }

    void explodeMachineAt(TileEntity entity) {
        if (this.explosing) {
            final BlockPos pos = entity.getPos();
            final ITransportTile iEnergyTile = TransportNetGlobal.instance.getSubTile(world, pos);
            if (iEnergyTile != null) {
                if (iEnergyTile.getHandler() instanceof IItemHandler) {
                    MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                            world,
                            iEnergyTile
                    ));
                }
            }
            this.world.setBlockToAir(pos);
            float power = 1.0F;
            ExplosionIC2 explosion = new ExplosionIC2(
                    this.world,
                    null,
                    0.5D + pos.getX(),
                    0.5D + pos.getY(),
                    0.5D + pos.getZ(),
                    power,
                    0.75F
            );
            explosion.doExplosion();
        }
    }

    public SunCoef getSuncoef() {
        return suncoef;
    }

    public void addController(IEnergyController tile1) {
        this.controllerList.add(tile1);
    }

    public void removeController(IEnergyController tile1) {
        this.controllerList.remove(tile1);
        tile1.unload();
    }

    public void addTile(IEnergyTile tile1) {

        if (tile1 instanceof IMetaDelegate) {
            final List<IEnergyTile> tiles = ((IMetaDelegate) tile1).getSubTiles();
            for (final IEnergyTile tile : tiles) {
                this.addTileEntity(getTileFromIEnergy(tile).getPos(), tile1, tile);
            }
            if (tile1 instanceof IEnergySource) {
                energySourceToEnergyPathMap.senderPath.add(new EnergyTick((IEnergySource) tile1, null));
            }


        } else {
            try {
                this.addTileEntity(getTileFromIEnergy(tile1).getPos(), tile1);
            } catch (Exception ignored) {
            }
        }


    }

    public void addTile(IEnergyTile tile, TileEntity tileentity) {

        final BlockPos coords = tileentity.getPos();
        if (this.chunkCoordinatesIEnergyTileMap.containsKey(coords)) {
            return;
        }
        if (!(tile instanceof IAdvEnergyTile)) {
            this.energyTileTileEntityMap.put(tile, tileentity);
            this.chunkCoordinatesMap.put(tile, coords);
        }
        this.chunkCoordinatesIEnergyTileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof IEnergyAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), (IEnergyAcceptor) tile);
        }
        if (tile instanceof IEnergySource && !(tile instanceof IMetaDelegate)) {
            energySourceToEnergyPathMap.senderPath.add(new EnergyTick((IEnergySource) tile, null));

        }


    }

    public BlockPos getPos(final IEnergyTile tile) {
        return this.chunkCoordinatesMap.get(tile);
    }

    public void addTileEntity(final BlockPos coords, final IEnergyTile tile) {
        if (this.chunkCoordinatesIEnergyTileMap.containsKey(coords)) {
            return;
        }

        TileEntity te = getTileFromIEnergy(tile);
        if (!(tile instanceof IAdvEnergyTile)) {
            this.energyTileTileEntityMap.put(tile, te);
            this.chunkCoordinatesMap.put(tile, coords);
        }
        this.chunkCoordinatesIEnergyTileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof IEnergyAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), (IEnergyAcceptor) tile);
        }
        if (tile instanceof IEnergySource && !(tile instanceof IMetaDelegate)) {
            energySourceToEnergyPathMap.senderPath.add(new EnergyTick((IEnergySource) tile, null));

        }
    }

    public void addTileEntity(final BlockPos coords, final IEnergyTile tile, final IEnergyTile tile1) {
        if (this.chunkCoordinatesIEnergyTileMap.containsKey(coords)) {
            return;
        }

        TileEntity te = getTileFromIEnergy(tile);
        TileEntity te1 = getTileFromIEnergy(tile1);
        this.energyTileTileEntityMap.put(tile, te);
        this.energyTileTileEntityMap.put(tile1, te1);
        this.chunkCoordinatesMap.put(tile, coords);
        this.chunkCoordinatesIEnergyTileMap.put(coords, tile);

        this.update(coords);
        if (tile instanceof IEnergyAcceptor) {
            this.waitingList.onTileEntityAdded(this.getValidReceivers(tile, true), (IEnergyAcceptor) tile);
        }


    }

    public void removeTile(IEnergyTile tile1) {
        if (tile1 instanceof IMetaDelegate) {
            final List<IEnergyTile> tiles = ((IMetaDelegate) tile1).getSubTiles();
            for (final IEnergyTile tile : tiles) {
                BlockPos coord1;
                if (this.energyTileTileEntityMap.containsKey(tile)) {
                    coord1 = this.energyTileTileEntityMap.get(tile).getPos();
                } else {
                    coord1 = getTileFromIEnergy(tile).getPos();
                }
                this.removeTileEntity(coord1, tile1, tile);

            }
        } else {
            this.removeTileEntity(tile1);
        }
    }


    public void removeTileEntity(BlockPos coord, IEnergyTile tile, IEnergyTile tile1) {
        if (!this.chunkCoordinatesIEnergyTileMap.containsKey(coord)) {
            return;
        }
        this.chunkCoordinatesMap.remove(tile, coord);

        this.chunkCoordinatesIEnergyTileMap.remove(coord);
        this.energyTileTileEntityMap.remove(tile1, this.energyTileTileEntityMap.get(tile1));
        this.energyTileTileEntityMap.remove(tile, this.energyTileTileEntityMap.get(tile));
        this.update(coord);
        if (tile instanceof IEnergyAcceptor) {
            this.energySourceToEnergyPathMap.removeAll(this.energySourceToEnergyPathMap.getSources((IEnergyAcceptor) tile));
            this.waitingList.onTileEntityRemoved((IEnergyAcceptor) tile);
        }
        if (tile instanceof IEnergySource) {

            this.energySourceToEnergyPathMap.remove((IEnergySource) tile);
        }
    }

    public void removeTileEntity(IEnergyTile tile) {
        if (tile instanceof IAdvEnergyTile) {
            if (!this.chunkCoordinatesIEnergyTileMap.containsKey(((IAdvEnergyTile) tile).getBlockPos())) {
                return;
            }
        } else if (!this.energyTileTileEntityMap.containsKey(tile)) {
            return;
        }
        BlockPos coord = this.chunkCoordinatesMap.get(tile);
        if (tile instanceof IAdvEnergyTile) {
            coord = ((IAdvEnergyTile) tile).getBlockPos();
        }
        if (!(tile instanceof IAdvEnergyTile)) {
            this.chunkCoordinatesMap.remove(tile);
            this.energyTileTileEntityMap.remove(tile, this.energyTileTileEntityMap.get(tile));
        }
        this.chunkCoordinatesIEnergyTileMap.remove(coord, tile);
        this.update(coord);
        if (tile instanceof IEnergyAcceptor) {
            this.energySourceToEnergyPathMap.removeAll(this.energySourceToEnergyPathMap.getSources((IEnergyAcceptor) tile));
            this.waitingList.onTileEntityRemoved((IEnergyAcceptor) tile);
        }
        if (tile instanceof IEnergySource) {
            this.energySourceToEnergyPathMap.remove((IEnergySource) tile);
        }
    }

    public TileEntity getTileFromMap(IEnergyTile tile) {
        return this.energyTileTileEntityMap.get(tile);
    }

    public double emitEnergyFrom(final IEnergySource energySource, double amount, final EnergyTick tick) {
        List<EnergyPath> energyPaths = tick.getList();
        if (energyPaths == null) {
            energyPaths = this.discover(energySource);
            for (EnergyPath path : energyPaths) {

                boolean isSorted = false;
                IEnergyConductor buf;
                while (!isSorted) {
                    isSorted = true;
                    for (int i = 0; i < path.getConductors().size() - 1; i++) {
                        if (path.getConductors().get(i).getConductorBreakdownEnergy() > path
                                .getConductors()
                                .get(i + 1)
                                .getConductorBreakdownEnergy()) {
                            isSorted = false;
                            buf = path.getConductors().get(i);
                            path.getConductors().set(i, path.getConductors().get(i + 1));
                            path.getConductors().set(i + 1, buf);
                        }
                    }
                }
            }

            tick.setList(energyPaths);

            tick.rework();
            if (!this.controllerList.isEmpty()) {
                this.controllerList.forEach(IEnergyController::work);
            }
        }
        tick.tick();

        if (amount > 0) {
            for (final EnergyPath energyPath : energyPaths) {
                if (amount <= 0) {
                    break;
                }
                final IEnergySink energySink = energyPath.target;
                double demandedEnergy = energySink.getDemandedEnergy();
                if (demandedEnergy <= 0.0) {
                    continue;
                }
                double energyProvided = amount;

                double adding;
                if (this.hasrestrictions && !this.explosing) {
                    adding = Math.min(energyProvided, Math.min(demandedEnergy, energyPath.min) + energyPath.loss);
                } else if (this.hasrestrictions) {
                    adding = Math.min(energyProvided, demandedEnergy + energyPath.loss);
                } else {
                    adding = Math.min(energyProvided, demandedEnergy + energyPath.loss);
                }
                adding -= energyPath.loss;
                if (energyPath.isLimit) {
                    adding = Math.min(adding, energyPath.limit_amount);
                }
                if (adding <= 0.0D) {
                    continue;
                }

                if (this.ignoring) {

                    int tier = energySink.getSinkTier();
                    int tier1 = EnergyNet.instance.getTierFromPower(adding);
                    if (tier1 > tier) {
                        if (energyPath.hasController) {
                            continue;
                        }
                        explodeTiles(energySink);
                        continue;
                    }
                }
                adding -= energySink.injectEnergy(energyPath.targetDirection, adding, 0);
                energyPath.totalEnergyConducted = (long) adding;

                tick.addEnergy(adding);
                energyPath.tick(this.tick, adding);
                amount -= adding;
                amount -= energyPath.loss;
                amount = Math.max(0, amount);
                if (this.hasrestrictions && this.explosing) {
                    if (adding > energyPath.min) {
                        for (IEnergyConductor energyConductor : energyPath.conductors) {
                            if (energyConductor.getConductorBreakdownEnergy() < adding) {
                                energyConductor.removeConductor();
                            } else {
                                break;
                            }
                        }
                    }
                }

            }
        }

        return amount;
    }

    public double getTotalEnergyEmitted(final IEnergyTile tileEntity) {
        double ret = 0.0;
        int col = 0;
        if (tileEntity instanceof IEnergyConductor) {
            for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((IEnergyAcceptor) tileEntity)) {
                if (energyPath.conductors.contains(
                        tileEntity)) {
                    ret += energyPath.totalEnergyConducted;
                    col++;
                }
            }

        }
        if (!(tileEntity instanceof IAdvEnergySource)) {
            if (tileEntity instanceof IEnergySource) {
                final EnergyTick energyTick = this.energySourceToEnergyPathMap.get((IEnergySource) tileEntity);
                if (energyTick != null) {
                    if (energyTick.getList() != null) {
                        for (final EnergyPath energyPath2 : energyTick.getList()) {
                            ret += energyPath2.totalEnergyConducted;
                        }
                    }
                }

            }
        } else {
            IAdvEnergySource advEnergySource = (IAdvEnergySource) tileEntity;
            if (!(advEnergySource instanceof IAdvDual) && advEnergySource.isSource()) {
                ret = Math.max(0, advEnergySource.getPerEnergy() - advEnergySource.getPastEnergy());
            } else if ((advEnergySource instanceof IAdvDual) && advEnergySource.isSource()) {
                IAdvDual dual = (IAdvDual) advEnergySource;
                ret = Math.max(0, dual.getPerEnergy1() - dual.getPastEnergy1());

            }
        }
        if (tileEntity instanceof IEnergyConductor) {
            try {
                return ret / col;
            } catch (Exception e) {
                return 0;
            }
        }

        return ret;
    }

    public double getTotalEnergySunken(final IEnergyTile tileEntity) {
        double ret = 0.0;
        int col = 0;
        if (!(tileEntity instanceof IAdvEnergySink)) {
            if (tileEntity instanceof IEnergyConductor || tileEntity instanceof IEnergySink) {
                for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((IEnergyAcceptor) tileEntity)) {
                    if ((tileEntity instanceof IEnergySink && energyPath.target == tileEntity) || (tileEntity instanceof IEnergyConductor && energyPath.conductors.contains(
                            tileEntity))) {
                        ret += energyPath.totalEnergyConducted;
                        col++;
                    }
                }
            }
        } else {
            IAdvEnergySink advEnergySink = (IAdvEnergySink) tileEntity;
            if (advEnergySink.isSink()) {
                if (this.tick - 1 == advEnergySink.getTick()
                        || this.tick == advEnergySink.getTick()
                        || this.tick + 1 == advEnergySink.getTick()
                ) {
                    ret = Math.max(0, advEnergySink.getPerEnergy() - advEnergySink.getPastEnergy());
                }

            }

        }
        if (tileEntity instanceof IEnergyConductor) {
            try {
                return ret / col;
            } catch (Exception e) {
                return 0;
            }
        }
        return ret;
    }

    public TileEntity getTileFromIEnergy(IEnergyTile tile) {
        if (tile instanceof TileEntity) {
            return (TileEntity) tile;
        }
        if (tile instanceof ILocatable) {
            return this.world.getTileEntity(((ILocatable) tile).getPosition());
        }

        return null;
    }

    public List<EnergyPath> discover(final IEnergySource emitter) {
        final Map<IEnergyConductor, EnumFacing> reachedTileEntities = new HashMap<>();
        final List<IEnergyTile> tileEntitiesToCheck = new ArrayList<>();
        final List<EnergyPath> energyPaths = new ArrayList<>();
        if (!(emitter instanceof IMetaDelegate)) {
            tileEntitiesToCheck.add(emitter);
        } else {
            tileEntitiesToCheck.addAll(((IMetaDelegate) emitter).getSubTiles());
        }
        while (!tileEntitiesToCheck.isEmpty()) {
            final IEnergyTile currentTileEntity = tileEntitiesToCheck.remove(0);
            final List<EnergyTarget> validReceivers = this.getValidReceivers(currentTileEntity, false);

            for (final EnergyTarget validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {
                    if (validReceiver.tileEntity instanceof IEnergySink) {
                        energyPaths.add(new EnergyPath((IEnergySink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }
                    if (reachedTileEntities.containsKey((IEnergyConductor) validReceiver.tileEntity)) {
                        continue;
                    }

                    reachedTileEntities.put((IEnergyConductor) validReceiver.tileEntity, validReceiver.direction);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }


        }
        for (EnergyPath energyPath : energyPaths) {
            IEnergyTile tileEntity = energyPath.target;
            IAdvEnergySink tile = energyPath.advEnergySink;
            EnumFacing energyBlockLink = energyPath.targetDirection;
            BlockPos te;
            if (tile != null) {
                te = tile.getBlockPos();
            } else {
                te = this.chunkCoordinatesMap.get(tileEntity);
            }
            if (emitter != null) {
                while (tileEntity != emitter) {

                    if (energyBlockLink != null && te != null) {
                        tileEntity = this.getTileEntity(te.offset(energyBlockLink));
                        te = te.offset(energyBlockLink);
                    }
                    if (!(tileEntity instanceof IEnergyConductor)) {
                        break;
                    }
                    final IEnergyConductor energyConductor = (IEnergyConductor) tileEntity;
                    energyPath.conductors.add(energyConductor);
                    if (energyConductor.getConductorBreakdownEnergy() - 1 < energyPath.getMin()) {
                        energyPath.setMin(energyConductor.getConductorBreakdownEnergy() - 1);
                    }
                    if (this.losing) {
                        energyPath.loss += energyConductor.getConductionLoss();
                    }
                    energyBlockLink = reachedTileEntities.get(tileEntity);
                    if (energyBlockLink != null) {
                        continue;
                    }
                    assert te != null;
                    IC2.platform.displayError("An energy network pathfinding entry is corrupted.\nThis could happen due to " +
                            "incorrect Minecraft behavior or a bug.\n\n(Technical information: energyBlockLink, tile " +
                            "entities below)\nE: " + emitter + " (" + te.getX() + "," + te.getY() + "," + te

                            .getZ() + ")\n" + "C: " + tileEntity + " (" + te.getX() + "," + te

                            .getY() + "," + te

                            .getZ() + ")\n" + "R: " + energyPath.target + " (" + this.energyTileTileEntityMap
                            .get(energyPath.target)
                            .getPos()
                            .getX() + "," + getTileFromMap(energyPath.target).getPos().getY() + "," + getTileFromIEnergy(
                            energyPath.target).getPos().getZ() + ")");
                }
            }
        }
        return energyPaths;
    }

    public IEnergyTile getNeighbor(final IEnergyTile tile, final EnumFacing dir, List<IEnergyTile> tiles) {
        if (tile == null) {
            return null;
        }
        final TileEntity tile2 = this.energyTileTileEntityMap.get(tile);
        if (tile2 == null) {
            return null;
        }
        IEnergyTile tile1 = this.getTileEntity(tile2.getPos().offset(dir));
        if (tiles.contains(tile1)) {
            return null;
        }
        return tile1;
    }

    private List<EnergyTarget> getValidReceivers(final IEnergyTile emitter, final boolean reverse) {
        final List<EnergyTarget> validReceivers = new LinkedList<>();
        if (emitter instanceof IMetaDelegate) {
            final IMetaDelegate meta = (IMetaDelegate) emitter;
            final List<IEnergyTile> targets = meta.getSubTiles();
            for (final IEnergyTile tile : targets) {
                for (final EnumFacing direction : EnumFacing.values()) {
                    final IEnergyTile target = getNeighbor(tile, direction, targets);

                    if (target == emitter) {
                        continue;
                    }
                    if (target == null) {
                        continue;
                    }
                    final EnumFacing inverseDirection = direction.getOpposite();
                    if (reverse) {
                        if (!(emitter instanceof IEnergyAcceptor) || !(target instanceof IEnergyEmitter)) {
                            continue;
                        }
                        final IEnergyEmitter sender = (IEnergyEmitter) target;
                        final IEnergyAcceptor receiver = (IEnergyAcceptor) emitter;
                        if (!sender.emitsEnergyTo(receiver, inverseDirection) || !receiver.acceptsEnergyFrom(
                                sender,
                                direction
                        )) {
                            continue;
                        }
                    } else {
                        if (!(emitter instanceof IEnergyEmitter) || !(target instanceof IEnergyAcceptor)) {
                            continue;
                        }
                        final IEnergyEmitter sender = (IEnergyEmitter) emitter;
                        final IEnergyAcceptor receiver = (IEnergyAcceptor) target;
                        if (!sender.emitsEnergyTo(receiver, direction) || !receiver.acceptsEnergyFrom(
                                sender,
                                inverseDirection
                        )) {
                            continue;
                        }
                    }
                    validReceivers.add(new EnergyTarget(target, inverseDirection));

                }
            }
        } else {
            final BlockPos tile1;
            if (emitter instanceof IAdvEnergyTile) {
                tile1 = ((IAdvEnergyTile) emitter).getBlockPos();
            } else {
                tile1 = this.chunkCoordinatesMap.get(emitter);
            }
            if (tile1 != null) {
                for (final EnumFacing direction : EnumFacing.values()) {
                    final IEnergyTile target2 = this.getTileEntity(tile1.offset(direction));

                    if (target2 == emitter) {
                        continue;
                    }
                    if (target2 != null) {
                        final EnumFacing inverseDirection2 = direction.getOpposite();
                        if (reverse) {
                            if (emitter instanceof IEnergyAcceptor && target2 instanceof IEnergyEmitter) {
                                final IEnergyEmitter sender2 = (IEnergyEmitter) target2;
                                final IEnergyAcceptor receiver2 = (IEnergyAcceptor) emitter;
                                if (sender2.emitsEnergyTo(receiver2, inverseDirection2) && receiver2.acceptsEnergyFrom(
                                        sender2,
                                        direction
                                )) {
                                    validReceivers.add(new EnergyTarget(target2, inverseDirection2));
                                }
                            }
                        } else if (emitter instanceof IEnergyEmitter && target2 instanceof IEnergyAcceptor) {
                            final IEnergyEmitter sender2 = (IEnergyEmitter) emitter;
                            final IEnergyAcceptor receiver2 = (IEnergyAcceptor) target2;
                            if (sender2.emitsEnergyTo(receiver2, direction) && receiver2.acceptsEnergyFrom(
                                    sender2,
                                    inverseDirection2
                            )) {
                                validReceivers.add(new EnergyTarget(target2, inverseDirection2));
                            }
                        }
                    }
                }
            }
        }
        //

        return validReceivers;
    }

    public List<IEnergySource> discoverFirstPathOrSources(final IEnergyTile par1) {
        final Set<IEnergyTile> reached = new HashSet<>();
        final List<IEnergySource> result = new ArrayList<>();
        final List<IEnergyTile> workList = new ArrayList<>();
        workList.add(par1);
        while (workList.size() > 0) {
            final IEnergyTile tile = workList.remove(0);
            final TileEntity te;
            if (tile instanceof IAdvEnergyTile) {
                te = ((IAdvEnergyTile) tile).getTileEntity();
            } else {
                te = this.energyTileTileEntityMap.get(tile);
            }
            if (te == null) {
                continue;
            }
            if (!te.isInvalid()) {
                final List<EnergyTarget> targets = this.getValidReceivers(tile, true);

                for (EnergyTarget energyTarget : targets) {
                    final IEnergyTile target = energyTarget.tileEntity;
                    if (target != par1) {
                        if (!reached.contains(target)) {
                            reached.add(target);
                            if (target instanceof IEnergySource) {
                                result.add((IEnergySource) target);
                            } else if (target instanceof IEnergyConductor) {
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
        for (IEnergySource source : energySourceList) {
            removeTile(source);
            explodeMachineAt(getTileFromIEnergy(source));

        }
        energySourceList.clear();
        this.suncoef.calculate();

        if (this.waitingList.hasWork()) {
            final List<IEnergyTile> tiles = this.waitingList.getPathTiles();

            for (final IEnergyTile tile : tiles) {
                final List<IEnergySource> sources = this.discoverFirstPathOrSources(tile);

                if (sources.size() > 0) {
                    this.energySourceToEnergyPathMap.removeAllSource1(sources);
                }
            }
            this.waitingList.clear();

        }
        for (EnergyTick tick : this.energySourceToEnergyPathMap.senderPath) {
            final IEnergySource entry = tick.getSource();
            if (tick.getList() != null) {
                if (tick.getList().isEmpty()) {
                    continue;
                }
            }
            if (entry != null) {

                double offer = Math.min(
                        entry.getOfferedEnergy(),
                        EnergyNet.instance.getPowerFromTier(entry.getSourceTier())
                );
                if (offer > 0) {

                    final double removed = offer - this.emitEnergyFrom(entry, offer, tick);
                    entry.drawEnergy(removed);


                } else {

                    if (tick.isAdv()) {
                        if (tick.getAdvSource().isSource()) {
                            tick.getAdvSource().setPastEnergy(tick.getAdvSource().getPerEnergy());
                        }
                    }
                }

            }

        }


        this.tick++;
    }

    public IEnergyTile getTileEntity(BlockPos pos) {
        return this.chunkCoordinatesIEnergyTileMap.get(pos);
    }

    public NodeStats getNodeStats(final IEnergyTile tile) {
        final double emitted = this.getTotalEnergyEmitted(tile);
        final double received = this.getTotalEnergySunken(tile);
        return new NodeStats(received, emitted, 0);
    }

    public List<EnergyPath> getEnergyPaths(IEnergyTile energyTile) {
        List<EnergyPath> energyPathList = new ArrayList<>();
        if (energyTile instanceof IEnergySource) {
            return energyPathList;
        }
        if (energyTile instanceof IEnergyConductor) {
            for (final EnergyPath energyPath : this.energySourceToEnergyPathMap.getPaths((IEnergyAcceptor) energyTile)) {

                if (energyPath.conductors.contains(
                        energyTile)) {
                    energyPathList.add(energyPath);
                }
            }
        }
        return energyPathList;
    }

    public void update(BlockPos pos) {
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos
                    .offset(dir);
            final IEnergyTile tile = this.chunkCoordinatesIEnergyTileMap.get(pos1);
            if (tile != null) {
                if (tile instanceof IEnergyConductor) {
                    if (!(tile instanceof IAdvConductor)) {
                        if (tile instanceof TileEntityCable) {
                            ((TileEntityCable) tile).onNeighborChange(null, null);
                        } else {
                            this.world.neighborChanged(pos1, Blocks.AIR, pos1);
                        }
                    } else {
                        ((IAdvConductor) tile).update_render();
                    }
                }
            }

        }
    }

    public Map<BlockPos, IEnergyTile> getChunkCoordinatesIEnergyTileMap() {
        return chunkCoordinatesIEnergyTileMap;
    }


    public void onUnload() {
        this.energySourceToEnergyPathMap.clear();
        this.waitingList.clear();
        this.chunkCoordinatesIEnergyTileMap.clear();
        this.chunkCoordinatesMap.clear();
        this.energyTileTileEntityMap.clear();
        this.controllerList.clear();
    }

    static class EnergyTarget {

        final IEnergyTile tileEntity;
        final EnumFacing direction;

        EnergyTarget(final IEnergyTile tileEntity, final EnumFacing direction) {
            this.tileEntity = tileEntity;
            this.direction = direction;
        }

    }

    public static class EnergyPath {

        final List<IEnergyConductor> conductors;
        final IEnergySink target;
        final boolean isAdv;
        final EnumFacing targetDirection;
        IAdvEnergySink advEnergySink = null;
        long totalEnergyConducted;
        double min = Double.MAX_VALUE;
        double loss = 0.0D;
        boolean hasController = false;
        boolean isLimit = false;
        double limit_amount = Double.MAX_VALUE;

        EnergyPath(IEnergySink sink, EnumFacing facing) {
            this.target = sink;
            this.conductors = new ArrayList<>();
            this.totalEnergyConducted = 0L;
            this.targetDirection = facing;
            this.isAdv = sink instanceof IAdvEnergySink;
            if (isAdv) {
                advEnergySink = (IAdvEnergySink) sink;
            }

        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || (getClass() != o.getClass() && !(o instanceof IEnergySink))) {
                return false;
            }
            if (o instanceof IEnergySink) {
                IEnergySink energySink = (IEnergySink) o;
                return energySink == target;
            }
            EnergyPath path = (EnergyPath) o;
            return target == path.target;
        }

        @Override
        public int hashCode() {
            return Objects.hash(target);
        }

        public List<IEnergyConductor> getConductors() {
            return conductors;
        }

        public void setHasController(final boolean hasController) {
            this.hasController = hasController;
        }

        public double getMin() {
            return min;
        }

        public void setMin(final double min) {
            this.min = min;
        }

        public void tick(int tick, double adding) {
            if (this.isAdv) {
                if (this.advEnergySink.isSink()) {
                    if (this.advEnergySink.getTick() != tick) {
                        this.advEnergySink.addTick(tick);
                        this.advEnergySink.setPastEnergy(this.advEnergySink.getPerEnergy());
                    }
                    this.advEnergySink.addPerEnergy(adding);
                }
            }
        }

    }


    static class EnergyPathMap {

        final List<EnergyTick> senderPath;

        EnergyPathMap() {
            this.senderPath = new ArrayList<>();
        }

        public void put(final IEnergySource par1, final List<EnergyPath> par2) {
            this.senderPath.add(new EnergyTick(par1, par2));
        }


        public boolean containsKey(final EnergyTick par1) {
            return this.senderPath.contains(par1);
        }

        public boolean containsKey(final IEnergySource par1) {
            return this.senderPath.contains(new EnergyTick(par1, null));
        }


        public void remove1(final IEnergySource par1) {

            for (EnergyTick ticks : this.senderPath) {
                if (ticks.getSource() == par1) {
                    ticks.setList(null);
                    break;
                }
            }
        }

        public void remove(final IEnergySource par1) {
            this.senderPath.remove(new EnergyTick(par1, null));
        }

        public void remove(final EnergyTick par1) {
            this.senderPath.remove(par1);
        }

        public void removeAll(final List<EnergyTick> par1) {
            if (par1 == null) {
                return;
            }
            for (EnergyTick iEnergySource : par1) {
                iEnergySource.setList(null);
            }
        }

        public void removeAllSource1(final List<IEnergySource> par1) {
            if (par1 == null) {
                return;
            }
            for (IEnergySource iEnergySource : par1) {
                this.remove1(iEnergySource);
            }
        }

        public List<EnergyPath> getPaths(final IEnergyAcceptor par1) {
            final List<EnergyPath> paths = new ArrayList<>();

            List<EnergyTick> sources_list = this.getSources(par1);
            if (sources_list == null || sources_list.isEmpty()) {
                return paths;
            }
            for (final EnergyTick source : sources_list) {
                if (this.containsKey(source)) {
                    paths.addAll(source.getList());
                }
            }
            return paths;
        }

        public List<EnergyTick> getSenderPath() {
            return senderPath;
        }

        public List<EnergyTick> getSources(final IEnergyAcceptor par1) {
            final List<EnergyTick> source = new ArrayList<>();
            for (final EnergyTick entry : this.senderPath) {
                if (source.contains(entry)) {
                    continue;
                }
                if (entry.getList() != null) {
                    for (EnergyPath path : entry.getList()) {
                        if ((!(par1 instanceof IEnergyConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof IEnergySink) || path.target != par1)) {
                            continue;
                        }
                        source.add(entry);
                    }
                }
            }
            return source;
        }


        public void clear() {
            this.senderPath.clear();
        }


        public EnergyTick get(IEnergySource tileEntity) {
            for (EnergyTick entry : this.senderPath) {
                if (entry.getSource() == tileEntity) {
                    return entry;
                }
            }
            return null;
        }

    }


    static class PathLogic {

        final List<IEnergyTile> tiles;

        PathLogic() {
            this.tiles = new ArrayList<>();
        }

        public boolean contains(final IEnergyTile par1) {
            return this.tiles.contains(par1);
        }

        public void add(final IEnergyTile par1) {
            this.tiles.add(par1);
        }

        public void remove(final IEnergyTile par1) {
            this.tiles.remove(par1);
        }

        public void clear() {
            this.tiles.clear();
        }

        public IEnergyTile getRepresentingTile() {
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

        public void onTileEntityAdded(final List<EnergyTarget> around, final IEnergyAcceptor tile) {
            if (around.isEmpty() || this.paths.isEmpty()) {
                this.createNewPath(tile);
                return;
            }
            boolean found = false;
            final List<PathLogic> logics = new ArrayList<>();
            for (final PathLogic logic : this.paths) {
                if (logic.contains(tile)) {
                    found = true;
                    if (tile instanceof IEnergyConductor) {
                        logics.add(logic);
                    }
                } else {
                    for (final EnergyTarget target : around) {
                        if (logic.contains(target.tileEntity)) {
                            found = true;
                            logic.add(tile);
                            if (target.tileEntity instanceof IEnergyConductor) {
                                logics.add(logic);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            if (logics.size() > 1 && tile instanceof IEnergyConductor) {
                final PathLogic newLogic = new PathLogic();
                for (final PathLogic logic2 : logics) {
                    this.paths.remove(logic2);
                    for (final IEnergyTile toMove : logic2.tiles) {
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

        public void onTileEntityRemoved(final IEnergyAcceptor par1) {
            if (this.paths.isEmpty()) {
                return;
            }

            List<IEnergyTile> toRecalculate = new ArrayList<>();
            for (int i = 0; i < this.paths.size(); i++) {
                PathLogic logic = this.paths.get(i);
                if (logic.contains(par1)) {
                    logic.remove(par1);
                    toRecalculate.addAll(logic.tiles);
                    this.paths.remove(i--);
                }
            }
            for (final IEnergyTile tile : toRecalculate) {
                this.onTileEntityAdded(EnergyNetLocal.this.getValidReceivers(tile, true), (IEnergyAcceptor) tile);
            }
        }

        public void createNewPath(final IEnergyTile par1) {
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

        public List<IEnergyTile> getPathTiles() {
            final List<IEnergyTile> tiles = new ArrayList<>();
            for (PathLogic path : this.paths) {
                final IEnergyTile tile = path.getRepresentingTile();
                if (tile != null) {
                    tiles.add(tile);
                }
            }
            return tiles;
        }

    }

}
