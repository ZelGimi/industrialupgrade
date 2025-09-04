package com.denfop.api.energy.networking;

import com.denfop.api.energy.EnergyTick;
import com.denfop.api.energy.EnergyTickList;
import com.denfop.api.energy.InfoCable;
import com.denfop.api.energy.SunCoef;
import com.denfop.api.energy.event.unload.EnergyTileUnLoadEvent;
import com.denfop.api.energy.interfaces.*;
import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.api.otherenergies.transport.ITransportTile;
import com.denfop.api.otherenergies.transport.TransportNetGlobal;
import com.denfop.api.otherenergies.transport.event.TransportTileUnLoadEvent;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.network.packet.PacketExplosion;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

public class EnergyNetLocal {


    public static EnergyNetLocal EMPTY = new EnergyNetLocal();
    final EnergyTickList<EnergyTick> energyTickList = new EnergyTickList<>();
    final List<EnergySource> sourceToUpdateList = new LinkedList<>();
    private final List<EnergyController> controllerList;
    private final Map<BlockPos, EnergyTile> chunkCoordinatesIEnergyTileMap;
    private final Map<ChunkPos, List<EnergySink>> chunkPosListMap = new HashMap<>();
    private final List<EnergySource> energySourceList = new ArrayList<>();
    private final List<EnergyConductor> conductorsRemove = new LinkedList<>();
    private final boolean hasrestrictions;
    private final boolean explosing;
    private final boolean ignoring;
    private final boolean losing;
    List<EnergySink> explodeTiles = new ArrayList<>();
    private Level world;
    private SunCoef suncoef;
    private int tick;

    EnergyNetLocal(final Level world) {
        this.world = world;
        this.controllerList = new ArrayList<>();
        this.chunkCoordinatesIEnergyTileMap = new HashMap<>();
        this.tick = 0;
        this.suncoef = new SunCoef(world);
        this.losing = EnergyNetGlobal.instance.getLosing();
        this.ignoring = EnergyNetGlobal.instance.needIgnoringTiers();
        this.explosing = EnergyNetGlobal.instance.needExplosion();
        this.hasrestrictions = EnergyNetGlobal.instance.hasRestrictions();
    }

    EnergyNetLocal() {
        this.world = null;
        this.controllerList = new ArrayList<>();
        this.chunkCoordinatesIEnergyTileMap = new HashMap<>();
        this.tick = 0;
        this.suncoef = null;
        this.losing = EnergyNetGlobal.instance.getLosing();
        this.ignoring = EnergyNetGlobal.instance.needIgnoringTiers();
        this.explosing = EnergyNetGlobal.instance.needExplosion();
        this.hasrestrictions = EnergyNetGlobal.instance.hasRestrictions();
    }

    public void setWorld(final Level world) {
        this.world = world;
    }

    public void explodeTiles(EnergySink sink) {

        if (!(sink instanceof EnergySource)) {
            explodeMachineAt(getTileFromIEnergy(sink));
            removeTile(sink);
        } else {
            energySourceList.add((EnergySource) sink);
        }

    }

    void explodeMachineAt(BlockEntity entity) {
        if (entity == null)
            return;
        if (this.explosing) {
            final BlockPos pos = entity.getBlockPos();
            final ITransportTile transportTile = TransportNetGlobal.instance.getSubTile(world, pos);
            if (transportTile != null) {
                MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                        world,
                        transportTile
                ));
            }
            if (!(entity instanceof BlockEntityBase)) {
                EnergyTile energyTile = EnergyNetGlobal.instance.getTile(world, pos);
                if (energyTile != null) {
                    MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(
                            world,
                            energyTile
                    ));
                }
            }
            Explosion explosion = new Explosion(this.world, null, pos.getX(), pos.getY() + 1, pos.getZ(), 4, false, Explosion.BlockInteraction.NONE);
            this.world.playSound(
                    null,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    SoundEvents.GENERIC_EXPLODE,
                    SoundSource.BLOCKS,
                    4.0F,
                    (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F
            );

            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            new PacketExplosion(explosion, 4, false, false);

        }
    }

    public SunCoef getSuncoef() {
        if (suncoef == null) {
            suncoef = new SunCoef(world);
        }
        return suncoef;
    }

    public void addController(EnergyController tile1) {
        this.controllerList.add(tile1);
    }

    public void removeController(EnergyController tile1) {
        this.controllerList.remove(tile1);
        tile1.unload();
    }

    public void addTile(EnergyTile tile1) {

        try {
            this.addTileEntity(tile1.getPos(), tile1);
        } catch (Exception ignored) {

        }


    }

    public void addTile(EnergyTile tile, BlockEntity tileentity) {

        try {
            this.addTileEntity(tileentity.getBlockPos(), tile);
        } catch (Exception ignored) {

        }


    }

    public BlockPos getPos(final EnergyTile tile) {
        return tile.getPos();
    }

    public Map<ChunkPos, List<EnergySink>> getChunkPosListMap() {
        return chunkPosListMap;
    }

    public void addTileEntity(final BlockPos coords, final EnergyTile tile) {
        if (this.chunkCoordinatesIEnergyTileMap.containsKey(coords)) {
            return;
        }

        this.chunkCoordinatesIEnergyTileMap.put(coords, tile);

        this.updateAdd(coords, tile);
        if (tile instanceof EnergyAcceptor) {
            this.onTileEntityAdded((EnergyAcceptor) tile);
            if (tile instanceof EnergySink) {
                ChunkPos pos = new ChunkPos(tile.getPos());
                List<EnergySink> iEnergySinkList = this.chunkPosListMap.get(pos);
                if (iEnergySinkList == null) {
                    iEnergySinkList = new LinkedList<>();
                    iEnergySinkList.add((EnergySink) tile);
                    this.chunkPosListMap.put(pos, iEnergySinkList);
                } else {
                    iEnergySinkList.add((EnergySink) tile);
                }
            }
        }
        if (tile instanceof EnergySource) {
            energyTickList.add(new EnergyTick((EnergySource) tile, null));

        }

    }

    private void updateAdd(BlockPos pos, EnergyTile tile) {
        for (final Direction dir : Direction.values()) {
            BlockPos pos1 = pos
                    .offset(dir.getNormal());
            final EnergyTile tile1 = this.chunkCoordinatesIEnergyTileMap.get(pos1);
            if (tile1 != EnergyNetGlobal.EMPTY && tile1 != null) {
                final Direction inverseDirection2 = dir.getOpposite();
                if (tile instanceof Dual) {
                    tile1.AddTile(tile, inverseDirection2);
                    tile.AddTile(tile1, dir);
                } else if (tile1 instanceof EnergyEmitter && tile instanceof EnergyAcceptor) {
                    final EnergyEmitter sender2 = (EnergyEmitter) tile1;
                    final EnergyAcceptor receiver2 = (EnergyAcceptor) tile;
                    if (tile1 instanceof Dual) {
                        tile1.AddTile(tile, inverseDirection2);
                        tile.AddTile(tile1, dir);
                    } else if (sender2.emitsEnergyTo(receiver2, dir.getOpposite()) && receiver2.acceptsEnergyFrom(
                            sender2,
                            inverseDirection2.getOpposite()
                    )) {
                        tile1.AddTile(tile, dir.getOpposite());
                        tile.AddTile(tile1, dir);
                    }
                } else if (tile1 instanceof EnergyAcceptor && tile instanceof EnergyEmitter) {
                    final EnergyEmitter sender2 = (EnergyEmitter) tile;
                    final EnergyAcceptor receiver2 = (EnergyAcceptor) tile1;
                    if (tile1 instanceof Dual) {
                        tile1.AddTile(tile, inverseDirection2);
                        tile.AddTile(tile1, dir);
                    } else if (sender2.emitsEnergyTo(receiver2, dir) && receiver2.acceptsEnergyFrom(
                            sender2,
                            inverseDirection2
                    )) {
                        tile1.AddTile(tile, dir.getOpposite());
                        tile.AddTile(tile1, dir);
                    }
                }
            }

        }
    }


    public void removeTile(EnergyTile tile1) {
        if (tile1 != EnergyNetGlobal.EMPTY) {
            this.removeTileEntity(tile1);
        }

    }


    public void removeTileEntity(EnergyTile tile) {
        if (tile.getPos() != null) {
            if (!this.chunkCoordinatesIEnergyTileMap.containsKey(tile.getPos())) {
                return;
            }
        } else {
            return;
        }
        BlockPos coord;
        coord = tile.getPos();

        this.chunkCoordinatesIEnergyTileMap.remove(coord);
        if (tile instanceof EnergyAcceptor) {
            this.removeAll(this.getSources((EnergyAcceptor) tile));
            if (tile instanceof EnergySink) {
                ChunkPos pos = new ChunkPos(tile.getPos());
                List<EnergySink> iEnergySinkList = this.chunkPosListMap.get(pos);
                if (iEnergySinkList != null) {
                    iEnergySinkList.remove((EnergySink) tile);
                }
            }
        }
        if (tile instanceof EnergySource) {
            this.remove((EnergySource) tile);
        }

        this.updateRemove(coord, tile);
    }

    private void updateRemove(BlockPos pos, EnergyTile tile) {
        for (final Direction dir : Direction.values()) {
            BlockPos pos1 = pos
                    .offset(dir.getNormal());
            final EnergyTile tile1 = this.chunkCoordinatesIEnergyTileMap.get(pos1);
            if (tile1 != EnergyNetGlobal.EMPTY && tile1 != null) {
                tile1.RemoveTile(tile, dir.getOpposite());
            }

        }
    }


    public double emitEnergyFrom(final EnergySource energySource, double amount, final EnergyTick tick) {
        List<Path> energyPaths = tick.getList();
        if (energyPaths == null) {
            final Tuple<List<Path>, LinkedList<Integer>> triple = this.discover(
                    energySource,
                    tick
            );
            energyPaths = triple.getA();
            List<Integer> conductors = tick.getConductors();
            if (conductors == null) {
                tick.setConductors(triple.getB());
            } else {
                tick.setConductors(triple.getB());
            }


            tick.setList(energyPaths);
            tick.rework();
            if (!this.controllerList.isEmpty()) {
                this.controllerList.forEach(EnergyController::work);
            }
        }


        if (amount > 0) {
            for (Path energyPath : energyPaths) {
                if (energySource.canExtractEnergy(energyPath.sourceDirection) <= 0) {
                    continue;
                }
                final EnergySink energySink = energyPath.target;
                double demandedEnergy = energySink.getDemandedEnergy(energyPath.targetDirection);
                if (demandedEnergy <= 0.0) {
                    continue;
                }
                double energyProvided = energySource.canExtractEnergy(energyPath.sourceDirection);

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

                    int tier = energySink.getSinkTier(energyPath.targetDirection);
                    if (!(energySink instanceof MultiDual)) {
                        int tier1 = EnergyNetGlobal.instance.getTierFromPower(adding);
                        if (tier1 > tier) {
                            if (energyPath.hasController) {
                                continue;
                            }
                            explodeTiles.add(energySink);
                            continue;
                        }
                    } else {
                        int tier1 = energySource.getSourceTier(energyPath.sourceDirection);
                        if (tier1 > tier) {
                            if (energyPath.hasController) {
                                continue;
                            }
                            explodeTiles.add(energySink);
                            continue;
                        }
                    }
                }
                energySink.receiveEnergy(energyPath.targetDirection, adding);
                energyPath.adding = adding;
                energyPath.tick(this.tick, adding);
                energySource.extractEnergy(energyPath.sourceDirection, adding);
                tick.addEnergy(adding);
                amount -= adding;
                amount -= energyPath.loss;
                amount = Math.max(0, amount);
                for (ConductorInfo energyConductor : energyPath.getConductorList()) {
                    energyConductor.addEnergy((byte) this.tick, adding);
                    if (this.hasrestrictions && this.explosing) {
                        if (adding > energyPath.min) {
                            if (energyConductor.getBreakdownEnergy() < adding) {
                                conductorsRemove.add((EnergyConductor) this.getTileEntity(energyConductor.getPos()));
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

    public double getTotalEnergyEmitted(final EnergyTile tileEntity) {
        double ret = 0.0;
        int col = 0;
        if (tileEntity instanceof EnergyConductor) {
            ret = Math.max(((EnergyConductor) tileEntity).getInfo().getEnergy(this.tick), ret);
            return ret;
        }

        if ((tileEntity instanceof EnergySource)) {
            EnergySource advEnergySource = (EnergySource) tileEntity;
            if (!(advEnergySource instanceof Dual) && advEnergySource.isSource()) {
                ret = Math.max(0, advEnergySource.getPerEnergy() - advEnergySource.getPastEnergy());
            } else if ((advEnergySource instanceof Dual) && advEnergySource.isSource()) {
                Dual dual = (Dual) advEnergySource;
                ret = Math.max(0, dual.getPerEnergy1() - dual.getPastEnergy1());

            }
        }

        return ret;
    }

    public double getTotalEnergySunken(final EnergyTile tileEntity) {
        double ret = 0.0;
        int col = 0;
        if (!(tileEntity instanceof EnergySink)) {
            if (tileEntity instanceof EnergyConductor) {
                if (tileEntity instanceof EnergyConductor) {
                    ret = Math.max(((EnergyConductor) tileEntity).getInfo().getEnergy(this.tick), ret);
                    return ret;
                }
            }
        } else {
            EnergySink advEnergySink = (EnergySink) tileEntity;
            if (advEnergySink.isSink()) {
                if (this.tick - 1 == advEnergySink.getTick()
                        || this.tick == advEnergySink.getTick()
                        || this.tick + 1 == advEnergySink.getTick()
                ) {
                    ret = Math.max(0, advEnergySink.getPerEnergy() - advEnergySink.getPastEnergy());
                }

            }

        }
        return ret;
    }

    public BlockEntity getTileFromIEnergy(EnergyTile tile) {
        if (tile instanceof BlockEntity) {
            return (BlockEntity) tile;
        }


        return world.getBlockEntity(tile.getPos());
    }

    public Tuple<List<Path>, LinkedList<Integer>> discover(
            final EnergySource emitter,
            final EnergyTick tick
    ) {
        final LinkedList<EnergyTile> tileEntitiesToCheck = new LinkedList<>();
        List<Path> energyPaths = new LinkedList<>();
        long id = WorldBaseGen.random.nextLong();
        emitter.setId(id);
        tileEntitiesToCheck.push(emitter);
        LinkedList<Integer> set = new LinkedList<>();
        while (!tileEntitiesToCheck.isEmpty()) {
            final EnergyTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<EnergyTile>> validReceivers = this.getValidReceivers(currentTileEntity);
            InfoCable cable = null;
            if (currentTileEntity instanceof EnergyConductor) {
                cable = ((EnergyConductor) currentTileEntity).getCable();
            }
            for (final InfoTile<EnergyTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof EnergySink) {
                        energyPaths.add(new Path((EnergySink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof EnergyConductor) {
                        EnergyConductor conductor = (EnergyConductor) validReceiver.tileEntity;
                        conductor.setCable(new InfoCable(conductor, validReceiver.direction, cable));
                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }
        int id1 = WorldBaseGen.random.nextInt();
        energyPaths = new ArrayList<>(energyPaths);

        for (Path energyPath : energyPaths) {
            EnergyTile tileEntity = energyPath.target;
            List<ConductorInfo> pathConductorsList = new LinkedList<>();
            energyPath.target.getEnergyTickList().add(tick.getSource().hashCode());
            Direction energyBlockLink = energyPath.targetDirection;
            energyPath.sourceDirection = null;
            Direction direction = null;
            tileEntity = tileEntity.getTiles().get(energyBlockLink);
            if (!(tileEntity instanceof EnergyConductor)) {
                continue;
            }
            InfoCable prev;
            InfoCable cable = ((EnergyConductor) tileEntity).getCable();
            while (cable != null) {

                final EnergyConductor energyConductor = cable.getConductor();
                pathConductorsList.add(energyConductor.getInfo());
                if (energyConductor.getHashCodeSource() != id1) {
                    energyConductor.setHashCodeSource(id1);
                    set.add(energyConductor.hashCode());
                }
                if (energyConductor.getConductorBreakdownEnergy() - 1 < energyPath.getMin()) {
                    energyPath.setMin(energyConductor.getConductorBreakdownEnergy() - 1);
                }
                if (this.losing) {
                    energyPath.loss += energyConductor.getConductionLoss();
                }

                prev = cable;
                cable = cable.getPrev();

                if (cable == null) {
                    direction = prev.getConductor().getTiles().entrySet().stream()
                            .filter(entry -> entry.getValue() == emitter)
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse(null);
                    break;
                }
            }
            if (direction != null)
                energyPath.sourceDirection = direction.getOpposite();
            energyPath.conductorList = new ArrayList<>(pathConductorsList);

        }
        return new Tuple<>(energyPaths, set);
    }

    public List<InfoTile<EnergyTile>> getValidReceivers(final EnergyTile emitter) {

        final BlockPos tile1;
        tile1 = emitter.getPos();
        if (tile1 != null) {
            if (emitter instanceof Dual) {
                final List<InfoTile<EnergyTile>> validReceivers = new LinkedList<>();
                for (InfoTile<EnergyTile> entry : emitter.getValidReceivers()) {
                    EnergyTile target2;
                    target2 = entry.tileEntity;
                    if (target2 == emitter) {
                        continue;
                    }
                    if (target2 != EnergyNetGlobal.EMPTY) {
                        final Direction inverseDirection2 = entry.direction;
                        if (target2 instanceof EnergyAcceptor) {
                            final EnergyEmitter sender2 = (EnergyEmitter) emitter;
                            final EnergyAcceptor receiver2 = (EnergyAcceptor) target2;
                            if (sender2.emitsEnergyTo(receiver2, inverseDirection2.getOpposite()) && receiver2.acceptsEnergyFrom(
                                    sender2,
                                    inverseDirection2
                            )) {
                                validReceivers.add(entry);
                            }
                        } else {
                            validReceivers.add(entry);
                        }
                    }
                }
                return validReceivers;
            } else {
                final List<InfoTile<EnergyTile>> validReceivers = new LinkedList<>();
                for (InfoTile<EnergyTile> entry : emitter.getValidReceivers()) {
                    EnergyTile target2;
                    target2 = entry.tileEntity;
                    if (target2 == emitter) {
                        continue;
                    }
                    if (target2 != EnergyNetGlobal.EMPTY) {
                        final Direction inverseDirection2 = entry.direction;
                        if (target2 instanceof EnergyAcceptor && !(target2 instanceof EnergyConductor && emitter instanceof EnergyConductor)) {
                            final EnergyEmitter sender2 = (EnergyEmitter) emitter;
                            final EnergyAcceptor receiver2 = (EnergyAcceptor) target2;
                            if (sender2.emitsEnergyTo(receiver2, inverseDirection2.getOpposite()) && receiver2.acceptsEnergyFrom(
                                    sender2,
                                    inverseDirection2
                            )) {
                                validReceivers.add(entry);
                            }
                        } else {
                            validReceivers.add(entry);
                        }
                    }
                }
                return validReceivers;
            }

        }


        return Collections.emptyList();
    }


    public void remove1(final EnergySource par1) {

        for (EnergyTick ticks : this.energyTickList) {
            if (ticks.getSource() == par1) {
                if (ticks.getList() != null) {
                    for (Path path : ticks.getList()) {
                        path.target.getEnergyTickList().remove((Integer) ticks.getSource().hashCode());
                    }
                }
                ticks.setList(null);
                break;
            }
        }

    }

    public void remove(final EnergySource par1) {
        final EnergyTick energyTick = this.energyTickList.removeSource(par1);
        if (energyTick != null)
            if (energyTick.getList() != null) {
                for (Path path : energyTick.getList()) {
                    path.target.getEnergyTickList().remove((Object) energyTick.hashCode());
                }


            }
    }


    public void removeAll(final List<EnergyTick> par1) {
        if (par1 == null) {
            return;
        }

        for (EnergyTick IEnergySource : par1) {
            if (IEnergySource.getList() != null) {
                for (Path path : IEnergySource.getList()) {
                    path.target.getEnergyTickList().remove((Object) IEnergySource.hashCode());
                }
                IEnergySource.conductors.clear();
            }
            IEnergySource.setList(null);
        }
    }


    public List<Path> getPaths(final EnergyAcceptor par1) {
        final List<Path> paths = new ArrayList<>();
        if (par1 instanceof EnergyConductor) {
            for (EnergyTick tick : energyTickList) {
                final List<Path> paths1 = tick.getList();
                if (paths1 != null) {
                    for (Path path : paths1) {
                        if (path.getConductorList().contains(((EnergyConductor) par1).getInfo())) {
                            paths.add(path);
                            break;
                        }
                    }

                }
            }
            return paths;
        }
        List<EnergyTick> sources_list = this.getSources(par1);
        if (sources_list == null || sources_list.isEmpty()) {
            return paths;
        }
        for (final EnergyTick source : sources_list) {
            paths.addAll(source.getList());
        }
        return paths;
    }

    public List<EnergyTick> getSources(final EnergyAcceptor par1) {
        if (par1 instanceof EnergySink) {
            List<EnergyTick> list = new LinkedList<>();
            for (EnergyTick energyTicks : energyTickList) {
                if (((EnergySink) par1).getEnergyTickList().contains(energyTicks.getSource().hashCode())) {
                    list.add(energyTicks);
                }
            }
            return list;
        } else {
            if (par1 instanceof EnergyConductor) {
                List<EnergyTick> list = new LinkedList<>();
                for (EnergyTick energyTicks : energyTickList) {
                    if (energyTicks.conductors.contains(par1.hashCode())) {
                        list.add(energyTicks);
                    }
                }
                return new ArrayList<>(list);
            }
            return Collections.emptyList();
        }
    }


    public void clear() {
        this.energyTickList.clear();
    }


    public List<InfoTile<EnergyTile>> getValidReceiversSubstitute(final EnergyTile emitter) {
        final BlockPos tile1;
        tile1 = emitter.getPos();
        if (tile1 != null) {
            if (emitter instanceof Dual) {
                final List<InfoTile<EnergyTile>> validReceivers = new LinkedList<>();
                for (InfoTile<EnergyTile> entry : emitter.getValidReceivers()) {
                    EnergyTile target2;
                    target2 = entry.tileEntity;
                    if (target2 == emitter) {
                        continue;
                    }
                    if (target2 != EnergyNetGlobal.EMPTY) {
                        final Direction inverseDirection2 = entry.direction;
                        if (target2 instanceof EnergyAcceptor) {
                            final EnergyEmitter sender2 = (EnergyEmitter) emitter;
                            final EnergyAcceptor receiver2 = (EnergyAcceptor) target2;
                            if (sender2.emitsEnergyTo(receiver2, inverseDirection2.getOpposite()) && receiver2.acceptsEnergyFrom(
                                    sender2,
                                    inverseDirection2
                            )) {
                                validReceivers.add(entry);
                            }
                        } else {
                            validReceivers.add(entry);
                        }
                    }
                }
                return validReceivers;
            } else {
                return emitter.getValidReceivers();
            }

        }


        return Collections.emptyList();
    }


    public void onTickEnd() {
        for (EnergySource source : energySourceList) {
            removeTile(source);
            explodeMachineAt(getTileFromIEnergy(source));
        }
        for (EnergyConductor conductor : conductorsRemove) {
            BlockEntityBase tile = (BlockEntityBase) conductor;
            tile.onUnloaded();
            this.world.removeBlock(conductor.getPos(), false);
        }
        conductorsRemove.clear();
        energySourceList.clear();
        if (suncoef == null) {
            suncoef = new SunCoef(world);
        }
        this.suncoef.calculate();
        if (!sourceToUpdateList.isEmpty()) {
            for (EnergySource source : sourceToUpdateList) {
                remove1(source);
            }
            sourceToUpdateList.clear();
        }

        for (EnergyTick tick : new ArrayList<>(energyTickList)) {
            final EnergySource entry = tick.getSource();
            tick.tick();
            if (entry.isRemovedTile()) {
                sourceToUpdateList.add(entry);
                continue;
            }
            if (tick.getList() == null || !tick.getList().isEmpty()) {
                int multi = entry instanceof MultiDual ? 4 : 1;
                for (int k = 0; k < multi; k++) {
                    double offer = Math.min(
                            entry.canExtractEnergy(),
                            EnergyNetGlobal.instance.getPowerFromTier(entry.getSourceTier())
                    );
                    if (offer > 0) {
                        emitEnergyFrom(entry, offer, tick);
                    } else {
                        if (tick.isAdv() && tick.getAdvSource().isSource()) {
                            tick.getAdvSource().setPastEnergy(tick.getAdvSource().getPerEnergy());
                        }
                    }
                }


            }
        }

        for (EnergySink sink : explodeTiles) {
            explodeTiles(sink);
        }
        explodeTiles.clear();
        this.tick++;
        if (tick > 100)
            tick = 0;
    }

    public EnergyTile getTileEntity(BlockPos pos) {
        return this.chunkCoordinatesIEnergyTileMap.getOrDefault(pos, EnergyNetGlobal.EMPTY);
    }

    public NodeStats getNodeStats(final EnergyTile tile) {

        final double emitted = this.getTotalEnergyEmitted(tile);
        final double received = this.getTotalEnergySunken(tile);
        return new NodeStats(received, emitted);

    }

    public List<Path> getEnergyPaths(EnergyTile energyTile) {
        List<Path> energyPathList = new ArrayList<>();
        if (energyTile instanceof EnergySource) {
            return energyPathList;
        }
        if (energyTile instanceof EnergyConductor) {
            return this.getPaths((EnergyAcceptor) energyTile);
        }
        return energyPathList;
    }


    public Map<BlockPos, EnergyTile> getChunkCoordinatesIEnergyTileMap() {
        return chunkCoordinatesIEnergyTileMap;
    }


    public void onUnload() {
        this.energyTickList.clear();
        this.chunkCoordinatesIEnergyTileMap.clear();
        this.controllerList.clear();
        suncoef = null;

    }

    public void onTileEntityAdded(final EnergyAcceptor tile) {
        final LinkedList<EnergyTile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(tile);
        long id = WorldBaseGen.random.nextLong();
        while (!tileEntitiesToCheck.isEmpty()) {
            final EnergyTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<EnergyTile>> validReceivers = currentTileEntity.getValidReceivers();
            for (final InfoTile<EnergyTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != tile && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof EnergySource) {
                        this.sourceToUpdateList.add((EnergySource) validReceiver.tileEntity);
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof EnergyConductor) {
                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }

    }

    private void removeTile(BlockPos coord) {
        final EnergyTile tile = this.chunkCoordinatesIEnergyTileMap.remove(coord);
        if (tile != null) {
            this.updateRemove(coord, tile);
        }
    }

    public void onTileEntityRemoved(final EnergyAcceptor par1) {

        this.onTileEntityAdded(par1);
    }


}
