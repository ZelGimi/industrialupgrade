package com.denfop.api.energy;

import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.transport.ITransportTile;
import com.denfop.api.transport.TransportNetGlobal;
import com.denfop.api.transport.event.TransportTileUnLoadEvent;
import com.denfop.network.packet.PacketExplosion;
import com.denfop.tiles.base.TileEntityBlock;
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
    final List<IEnergySource> sourceToUpdateList = new LinkedList<>();
    private final List<IEnergyController> controllerList;
    private final Map<BlockPos, IEnergyTile> chunkCoordinatesIEnergyTileMap;
    private final Map<ChunkPos, List<IEnergySink>> chunkPosListMap = new HashMap<>();
    private final List<IEnergySource> energySourceList = new ArrayList<>();
    private final List<IEnergyConductor> conductorsRemove = new LinkedList<>();
    private final boolean hasrestrictions;
    private final boolean explosing;
    private final boolean ignoring;
    private final boolean losing;
    List<IEnergySink> explodeTiles = new ArrayList<>();
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

    public void explodeTiles(IEnergySink sink) {

        if (!(sink instanceof IEnergySource)) {
            explodeMachineAt(getTileFromIEnergy(sink));
            removeTile(sink);
        } else {
            energySourceList.add((IEnergySource) sink);
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
            if (!(entity instanceof TileEntityBlock)) {
                IEnergyTile energyTile = EnergyNetGlobal.instance.getTile(world, pos);
                if (energyTile != null) {
                    MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(
                            world,
                            energyTile
                    ));
                }
            }
            Explosion explosion = new Explosion(this.world, null, pos.getX(), pos.getY() + 1, pos.getZ(), 4, false, Explosion.BlockInteraction.KEEP);
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

    public void addController(IEnergyController tile1) {
        this.controllerList.add(tile1);
    }

    public void removeController(IEnergyController tile1) {
        this.controllerList.remove(tile1);
        tile1.unload();
    }

    public void addTile(IEnergyTile tile1) {

        try {
            this.addTileEntity(tile1.getPos(), tile1);
        } catch (Exception ignored) {

        }


    }

    public void addTile(IEnergyTile tile, BlockEntity tileentity) {

        try {
            this.addTileEntity(tileentity.getBlockPos(), tile);
        } catch (Exception ignored) {

        }


    }

    public BlockPos getPos(final IEnergyTile tile) {
        return tile.getPos();
    }

    public Map<ChunkPos, List<IEnergySink>> getChunkPosListMap() {
        return chunkPosListMap;
    }

    public void addTileEntity(final BlockPos coords, final IEnergyTile tile) {
        if (this.chunkCoordinatesIEnergyTileMap.containsKey(coords)) {
            return;
        }

        this.chunkCoordinatesIEnergyTileMap.put(coords, tile);

        this.updateAdd(coords, tile);
        if (tile instanceof IEnergyAcceptor) {
            this.onTileEntityAdded((IEnergyAcceptor) tile);
            if (tile instanceof IEnergySink) {
                ChunkPos pos = new ChunkPos(tile.getPos());
                List<IEnergySink> iEnergySinkList = this.chunkPosListMap.get(pos);
                if (iEnergySinkList == null) {
                    iEnergySinkList = new LinkedList<>();
                    iEnergySinkList.add((IEnergySink) tile);
                    this.chunkPosListMap.put(pos, iEnergySinkList);
                } else {
                    iEnergySinkList.add((IEnergySink) tile);
                }
            }
        }
        if (tile instanceof IEnergySource) {
            energyTickList.add(new EnergyTick((IEnergySource) tile, null));

        }

    }

    private void updateAdd(BlockPos pos, IEnergyTile tile) {
        for (final Direction dir : Direction.values()) {
            BlockPos pos1 = pos
                    .offset(dir.getNormal());
            final IEnergyTile tile1 = this.chunkCoordinatesIEnergyTileMap.get(pos1);
            if (tile1 != EnergyNetGlobal.EMPTY && tile1 != null) {
                final Direction inverseDirection2 = dir.getOpposite();
                if (tile instanceof IDual) {
                    tile1.AddTile(tile, inverseDirection2);
                    tile.AddTile(tile1, dir);
                } else if (tile1 instanceof IEnergyEmitter && tile instanceof IEnergyAcceptor) {
                    final IEnergyEmitter sender2 = (IEnergyEmitter) tile1;
                    final IEnergyAcceptor receiver2 = (IEnergyAcceptor) tile;
                    if (tile1 instanceof IDual) {
                        tile1.AddTile(tile, inverseDirection2);
                        tile.AddTile(tile1, dir);
                    } else if (sender2.emitsEnergyTo(receiver2, dir.getOpposite()) && receiver2.acceptsEnergyFrom(
                            sender2,
                            inverseDirection2.getOpposite()
                    )) {
                        tile1.AddTile(tile, dir.getOpposite());
                        tile.AddTile(tile1, dir);
                    }
                } else if (tile1 instanceof IEnergyAcceptor && tile instanceof IEnergyEmitter) {
                    final IEnergyEmitter sender2 = (IEnergyEmitter) tile;
                    final IEnergyAcceptor receiver2 = (IEnergyAcceptor) tile1;
                    if (tile1 instanceof IDual) {
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


    public void removeTile(IEnergyTile tile1) {
        if (tile1 != EnergyNetGlobal.EMPTY) {
            this.removeTileEntity(tile1);
        }

    }


    public void removeTileEntity(IEnergyTile tile) {
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
        if (tile instanceof IEnergyAcceptor) {
            this.removeAll(this.getSources((IEnergyAcceptor) tile));
            if (tile instanceof IEnergySink) {
                ChunkPos pos = new ChunkPos(tile.getPos());
                List<IEnergySink> iEnergySinkList = this.chunkPosListMap.get(pos);
                if (iEnergySinkList != null) {
                    iEnergySinkList.remove((IEnergySink) tile);
                }
            }
        }
        if (tile instanceof IEnergySource) {
            this.remove((IEnergySource) tile);
        }

        this.updateRemove(coord, tile);
    }

    private void updateRemove(BlockPos pos, IEnergyTile tile) {
        for (final Direction dir : Direction.values()) {
            BlockPos pos1 = pos
                    .offset(dir.getNormal());
            final IEnergyTile tile1 = this.chunkCoordinatesIEnergyTileMap.get(pos1);
            if (tile1 != EnergyNetGlobal.EMPTY && tile1 != null) {
                tile1.RemoveTile(tile, dir.getOpposite());
            }

        }
    }


    public double emitEnergyFrom(final IEnergySource energySource, double amount, final EnergyTick tick) {
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
                this.controllerList.forEach(IEnergyController::work);
            }
        }


        if (amount > 0) {
            for (Path energyPath : energyPaths) {
                if (energySource.canExtractEnergy(energyPath.sourceDirection) <= 0) {
                    continue;
                }
                final IEnergySink energySink = energyPath.target;
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
                    if (!(energySink instanceof IMultiDual)) {
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
                                conductorsRemove.add((IEnergyConductor) this.getTileEntity(energyConductor.getPos()));
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
            ret = Math.max(((IEnergyConductor) tileEntity).getInfo().getEnergy(this.tick), ret);
            return ret;
        }

        if ((tileEntity instanceof IEnergySource)) {
            IEnergySource advEnergySource = (IEnergySource) tileEntity;
            if (!(advEnergySource instanceof IDual) && advEnergySource.isSource()) {
                ret = Math.max(0, advEnergySource.getPerEnergy() - advEnergySource.getPastEnergy());
            } else if ((advEnergySource instanceof IDual) && advEnergySource.isSource()) {
                IDual dual = (IDual) advEnergySource;
                ret = Math.max(0, dual.getPerEnergy1() - dual.getPastEnergy1());

            }
        }

        return ret;
    }

    public double getTotalEnergySunken(final IEnergyTile tileEntity) {
        double ret = 0.0;
        int col = 0;
        if (!(tileEntity instanceof IEnergySink)) {
            if (tileEntity instanceof IEnergyConductor) {
                if (tileEntity instanceof IEnergyConductor) {
                    ret = Math.max(((IEnergyConductor) tileEntity).getInfo().getEnergy(this.tick), ret);
                    return ret;
                }
            }
        } else {
            IEnergySink advEnergySink = (IEnergySink) tileEntity;
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

    public BlockEntity getTileFromIEnergy(IEnergyTile tile) {
        if (tile instanceof BlockEntity) {
            return (BlockEntity) tile;
        }


        return world.getBlockEntity(tile.getPos());
    }

    public Tuple<List<Path>, LinkedList<Integer>> discover(
            final IEnergySource emitter,
            final EnergyTick tick
    ) {
        final LinkedList<IEnergyTile> tileEntitiesToCheck = new LinkedList<>();
        List<Path> energyPaths = new LinkedList<>();
        long id = WorldBaseGen.random.nextLong();
        emitter.setId(id);
        tileEntitiesToCheck.push(emitter);
        LinkedList<Integer> set = new LinkedList<>();
        while (!tileEntitiesToCheck.isEmpty()) {
            final IEnergyTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<IEnergyTile>> validReceivers = this.getValidReceivers(currentTileEntity);
            InfoCable cable = null;
            if (currentTileEntity instanceof IEnergyConductor) {
                cable = ((IEnergyConductor) currentTileEntity).getCable();
            }
            for (final InfoTile<IEnergyTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof IEnergySink) {
                        energyPaths.add(new Path((IEnergySink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof IEnergyConductor) {
                        IEnergyConductor conductor = (IEnergyConductor) validReceiver.tileEntity;
                        conductor.setCable(new InfoCable(conductor, validReceiver.direction, cable));
                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }
        int id1 = WorldBaseGen.random.nextInt();
        energyPaths = new ArrayList<>(energyPaths);

        for (Path energyPath : energyPaths) {
            IEnergyTile tileEntity = energyPath.target;
            List<ConductorInfo> pathConductorsList = new LinkedList<>();
            energyPath.target.getEnergyTickList().add(tick.getSource().hashCode());
            Direction energyBlockLink = energyPath.targetDirection;
            energyPath.sourceDirection = null;
            Direction direction = null;
            tileEntity = tileEntity.getTiles().get(energyBlockLink);
            if (!(tileEntity instanceof IEnergyConductor)) {
                continue;
            }
            InfoCable prev;
            InfoCable cable = ((IEnergyConductor) tileEntity).getCable();
            while (cable != null) {

                final IEnergyConductor energyConductor = cable.getConductor();
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

    public List<InfoTile<IEnergyTile>> getValidReceivers(final IEnergyTile emitter) {

        final BlockPos tile1;
        tile1 = emitter.getPos();
        if (tile1 != null) {
            if (emitter instanceof IDual) {
                final List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();
                for (InfoTile<IEnergyTile> entry : emitter.getValidReceivers()) {
                    IEnergyTile target2;
                    target2 = entry.tileEntity;
                    if (target2 == emitter) {
                        continue;
                    }
                    if (target2 != EnergyNetGlobal.EMPTY) {
                        final Direction inverseDirection2 = entry.direction;
                        if (target2 instanceof IEnergyAcceptor) {
                            final IEnergyEmitter sender2 = (IEnergyEmitter) emitter;
                            final IEnergyAcceptor receiver2 = (IEnergyAcceptor) target2;
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
                final List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();
                for (InfoTile<IEnergyTile> entry : emitter.getValidReceivers()) {
                    IEnergyTile target2;
                    target2 = entry.tileEntity;
                    if (target2 == emitter) {
                        continue;
                    }
                    if (target2 != EnergyNetGlobal.EMPTY) {
                        final Direction inverseDirection2 = entry.direction;
                        if (target2 instanceof IEnergyAcceptor && !(target2 instanceof IEnergyConductor && emitter instanceof IEnergyConductor)) {
                            final IEnergyEmitter sender2 = (IEnergyEmitter) emitter;
                            final IEnergyAcceptor receiver2 = (IEnergyAcceptor) target2;
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


    public void remove1(final IEnergySource par1) {

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

    public void remove(final IEnergySource par1) {
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


    public List<Path> getPaths(final IEnergyAcceptor par1) {
        final List<Path> paths = new ArrayList<>();
        if (par1 instanceof IEnergyConductor) {
            for (EnergyTick tick : energyTickList) {
                final List<Path> paths1 = tick.getList();
                if (paths1 != null) {
                    for (Path path : paths1) {
                        if (path.getConductorList().contains(((IEnergyConductor) par1).getInfo())) {
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

    public List<EnergyTick> getSources(final IEnergyAcceptor par1) {
        if (par1 instanceof IEnergySink) {
            List<EnergyTick> list = new LinkedList<>();
            for (EnergyTick energyTicks : energyTickList) {
                if (((IEnergySink) par1).getEnergyTickList().contains(energyTicks.getSource().hashCode())) {
                    list.add(energyTicks);
                }
            }
            return list;
        } else {
            if (par1 instanceof IEnergyConductor) {
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


    public List<InfoTile<IEnergyTile>> getValidReceiversSubstitute(final IEnergyTile emitter) {
        final BlockPos tile1;
        tile1 = emitter.getPos();
        if (tile1 != null) {
            if (emitter instanceof IDual) {
                final List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();
                for (InfoTile<IEnergyTile> entry : emitter.getValidReceivers()) {
                    IEnergyTile target2;
                    target2 = entry.tileEntity;
                    if (target2 == emitter) {
                        continue;
                    }
                    if (target2 != EnergyNetGlobal.EMPTY) {
                        final Direction inverseDirection2 = entry.direction;
                        if (target2 instanceof IEnergyAcceptor) {
                            final IEnergyEmitter sender2 = (IEnergyEmitter) emitter;
                            final IEnergyAcceptor receiver2 = (IEnergyAcceptor) target2;
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
        for (IEnergySource source : energySourceList) {
            removeTile(source);
            explodeMachineAt(getTileFromIEnergy(source));
        }
        for (IEnergyConductor conductor : conductorsRemove) {
            TileEntityBlock tile = (TileEntityBlock) conductor;
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
            for (IEnergySource source : sourceToUpdateList) {
                remove1(source);
            }
            sourceToUpdateList.clear();
        }

        for (EnergyTick tick : new ArrayList<>(energyTickList)) {
            final IEnergySource entry = tick.getSource();
            tick.tick();
            if (entry.isRemovedTile()) {
                sourceToUpdateList.add(entry);
                continue;
            }
            if (tick.getList() == null || !tick.getList().isEmpty()) {
                int multi = entry instanceof IMultiDual ? 4 : 1;
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

        for (IEnergySink sink : explodeTiles) {
            explodeTiles(sink);
        }
        explodeTiles.clear();
        this.tick++;
        if (tick > 100)
            tick = 0;
    }

    public IEnergyTile getTileEntity(BlockPos pos) {
        return this.chunkCoordinatesIEnergyTileMap.getOrDefault(pos, EnergyNetGlobal.EMPTY);
    }

    public NodeStats getNodeStats(final IEnergyTile tile) {

        final double emitted = this.getTotalEnergyEmitted(tile);
        final double received = this.getTotalEnergySunken(tile);
        return new NodeStats(received, emitted);

    }

    public List<Path> getEnergyPaths(IEnergyTile energyTile) {
        List<Path> energyPathList = new ArrayList<>();
        if (energyTile instanceof IEnergySource) {
            return energyPathList;
        }
        if (energyTile instanceof IEnergyConductor) {
            return this.getPaths((IEnergyAcceptor) energyTile);
        }
        return energyPathList;
    }


    public Map<BlockPos, IEnergyTile> getChunkCoordinatesIEnergyTileMap() {
        return chunkCoordinatesIEnergyTileMap;
    }


    public void onUnload() {
        this.energyTickList.clear();
        this.chunkCoordinatesIEnergyTileMap.clear();
        this.controllerList.clear();
        suncoef = null;

    }

    public void onTileEntityAdded(final IEnergyAcceptor tile) {
        final LinkedList<IEnergyTile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(tile);
        long id = WorldBaseGen.random.nextLong();
        while (!tileEntitiesToCheck.isEmpty()) {
            final IEnergyTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<IEnergyTile>> validReceivers = currentTileEntity.getValidReceivers();
            for (final InfoTile<IEnergyTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != tile && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof IEnergySource) {
                        this.sourceToUpdateList.add((IEnergySource) validReceiver.tileEntity);
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof IEnergyConductor) {
                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }

    }

    private void removeTile(BlockPos coord) {
        final IEnergyTile tile = this.chunkCoordinatesIEnergyTileMap.remove(coord);
        if (tile != null) {
            this.updateRemove(coord, tile);
        }
    }

    public void onTileEntityRemoved(final IEnergyAcceptor par1) {

        this.onTileEntityAdded(par1);
    }


}
