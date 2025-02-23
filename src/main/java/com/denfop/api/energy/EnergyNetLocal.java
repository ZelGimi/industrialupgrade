package com.denfop.api.energy;

import com.denfop.api.sytem.InfoTile;
import com.denfop.api.transport.ITransportTile;
import com.denfop.api.transport.TransportNetGlobal;
import com.denfop.api.transport.event.TransportTileUnLoadEvent;
import com.denfop.network.packet.PacketExplosion;
import com.denfop.utils.Triple;
import com.denfop.world.WorldBaseGen;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private World world;
    private SunCoef suncoef;
    private int tick;

    EnergyNetLocal(final World world) {
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

    public void setWorld(final World world) {
        this.world = world;
    }

    List<IEnergySink> explodeTiles = new ArrayList<>();

    public void explodeTiles(IEnergySink sink) {

        if (!(sink instanceof IEnergySource)) {
            explodeMachineAt(getTileFromIEnergy(sink));
            removeTile(sink);
        } else {
            energySourceList.add((IEnergySource) sink);
        }

    }

    void explodeMachineAt(TileEntity entity) {
        if (this.explosing) {
            final BlockPos pos = entity.getPos();
            final ITransportTile IEnergyTile = TransportNetGlobal.instance.getSubTile(world, pos);
            if (IEnergyTile != null) {
                if (IEnergyTile.getHandler() instanceof IItemHandler) {
                    MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                            world,
                            IEnergyTile
                    ));
                }
            }

            Explosion explosion = new Explosion(this.world, null, pos.getX(), pos.getY() + 1, pos.getZ(), 4, false, false);
            this.world.playSound(
                    null,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    SoundEvents.ENTITY_GENERIC_EXPLODE,
                    SoundCategory.BLOCKS,
                    4.0F,
                    (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F
            );

            world.setBlockToAir(pos);
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
            this.addTileEntity(tile1.getBlockPos(), tile1);
        } catch (Exception ignored) {

        }


    }

    public void addTile(IEnergyTile tile, TileEntity tileentity) {

        try {
            this.addTileEntity(tileentity.getPos(), tile);
        } catch (Exception ignored) {

        }


    }

    public BlockPos getPos(final IEnergyTile tile) {
        return tile.getBlockPos();
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
                ChunkPos pos = new ChunkPos(tile.getBlockPos());
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
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos
                    .offset(dir);
            final IEnergyTile tile1 = this.chunkCoordinatesIEnergyTileMap.get(pos1);
            if (tile1 != EnergyNetGlobal.EMPTY && tile1 != null) {
                final EnumFacing inverseDirection2 = dir.getOpposite();
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
        if (tile.getBlockPos() != null) {
            if (!this.chunkCoordinatesIEnergyTileMap.containsKey(tile.getBlockPos())) {
                return;
            }
        } else {
            if (!this.chunkCoordinatesIEnergyTileMap.containsKey(tile.getTileEntity().getPos())) {
                return;
            }
        }
        BlockPos coord;
        coord = tile.getBlockPos();

        this.chunkCoordinatesIEnergyTileMap.remove(coord);
        if (tile instanceof IEnergyAcceptor) {
            this.removeAll(this.getSources((IEnergyAcceptor) tile));
            if (tile instanceof IEnergySink) {
                ChunkPos pos = new ChunkPos(tile.getBlockPos());
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
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos
                    .offset(dir);
            final IEnergyTile tile1 = this.chunkCoordinatesIEnergyTileMap.get(pos1);
            if (tile1 != EnergyNetGlobal.EMPTY && tile1 != null) {
                tile1.RemoveTile(tile, dir.getOpposite());
            }

        }
    }

    public TileEntity getTileFromMap(IEnergyTile tile) {
        return tile.getTileEntity();
    }

    public double emitEnergyFrom(final IEnergySource energySource, double amount, final EnergyTick tick) {
        List<Path> energyPaths = tick.getList();
        if (energyPaths == null) {
            final Tuple<List<Path>, LinkedList<Integer>> triple = this.discover(
                    energySource,
                    tick
            );
            energyPaths = triple.getFirst();
            List<Integer> conductors = tick.getConductors();
            if (conductors == null) {
                tick.setConductors(triple.getSecond());
            } else {
                tick.setConductors(triple.getSecond());
            }


            tick.setList(energyPaths);
            tick.rework();
            if (!this.controllerList.isEmpty()) {
                this.controllerList.forEach(IEnergyController::work);
            }
        }


        if (amount > 0) {
            for (Path energyPath : energyPaths) {
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
                        int tier1 = energySource.getSourceTier();
                        if (tier1 > tier) {
                            if (energyPath.hasController) {
                                continue;
                            }
                            explodeTiles.add(energySink);
                            continue;
                        }
                    }
                }
                energySink.receiveEnergy(adding);
                energyPath.adding = adding;
                energyPath.tick(this.tick, adding);
                amount -= adding;
                amount -= energyPath.loss;
                amount = Math.max(0, amount);
                if (this.hasrestrictions && this.explosing) {
                    if (adding > energyPath.min) {
                        for (IEnergyConductor energyConductor : energyPath.getConductorList()) {
                            if (energyConductor.getConductorBreakdownEnergy() < adding) {
                                conductorsRemove.add(energyConductor);
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
            for (EnergyTick tick : energyTickList) {
                double energy = 0;
                if (tick.getList() != null) {
                    final List<Path> paths = tick.getList();
                    for (Path path : paths) {
                        if (path.getConductorList().contains(tileEntity)) {
                            energy += path.adding;
                            break;
                        }

                    }
                }
                ret += energy;
                if (energy != 0) {
                    col++;
                }

            }

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
        if (!(tileEntity instanceof IEnergySink)) {
            if (tileEntity instanceof IEnergyConductor) {
                for (EnergyTick tick : energyTickList) {
                    double energy = 0;
                    final List<Path> paths = tick.getList();
                    if (paths != null) {
                        for (Path path : paths) {
                            if (path.getConductorList().contains(tileEntity)) {
                                energy += path.adding;
                                break;
                            }

                        }
                    }
                    ret += energy;
                    if (energy != 0) {
                        col++;
                    }

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


        return tile.getTileEntity();
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
            List<IEnergyConductor> pathConductorsList = new LinkedList<>();
            energyPath.target.getEnergyTickList().add(tick.getSource().hashCode());
            EnumFacing energyBlockLink = energyPath.targetDirection;
            tileEntity = tileEntity.getTiles().get(energyBlockLink);
             if (!(tileEntity instanceof IEnergyConductor)) {
                continue;
            }
            InfoCable cable = ((IEnergyConductor) tileEntity).getCable();
            while (cable != null) {

                final IEnergyConductor energyConductor = cable.getConductor();
                pathConductorsList.add(energyConductor);
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
                cable = cable.getPrev();
                if (cable == null) {
                    break;
                }
            }
            energyPath.conductorList = new ArrayList<>(pathConductorsList);

        }
        return new Tuple<>(energyPaths, set);
    }

    public List<InfoTile<IEnergyTile>> getValidReceivers(final IEnergyTile emitter) {

        final BlockPos tile1;
        tile1 = emitter.getBlockPos();
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
                        final EnumFacing inverseDirection2 = entry.direction;
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
                        final EnumFacing inverseDirection2 = entry.direction;
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
                        path.target.getEnergyTickList().remove((Integer)ticks.getSource().hashCode());
                    }
                }
                ticks.setList(null);
                break;
            }
        }

    }

    public void remove(final IEnergySource par1) {
        final EnergyTick energyTick = this.energyTickList.removeSource(par1);
        if (energyTick.getList() != null) {
            for (Path path : energyTick.getList()) {
                path.target.getEnergyTickList().remove((Object)energyTick.hashCode());
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
                    path.target.getEnergyTickList().remove((Object)IEnergySource.hashCode());
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
                for (Path path : paths1) {

                    if (path.getConductorList().contains(par1)) {
                        paths.add(path);
                        break;
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
        tile1 = emitter.getBlockPos();
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
                        final EnumFacing inverseDirection2 = entry.direction;
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

    private static final int numThreads = Runtime.getRuntime().availableProcessors();


    public void onTickEnd() {
        for (IEnergySource source : energySourceList) {
            removeTile(source);
            explodeMachineAt(getTileFromIEnergy(source));
        }
        for (IEnergyConductor conductor : conductorsRemove) {
            conductor.removeConductor();
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

        for (EnergyTick tick : energyTickList) {
            final IEnergySource entry = tick.getSource();
            tick.tick();
            if (tick.getList() == null || !tick.getList().isEmpty()) {
                int multi = entry instanceof IMultiDual ? 4 : 1;
                for (int k = 0; k < multi; k++) {
                    double offer = Math.min(
                            entry.canExtractEnergy(),
                            EnergyNetGlobal.instance.getPowerFromTier(entry.getSourceTier())
                    );
                    if (offer > 0) {
                        final double removed = offer - emitEnergyFrom(entry, offer, tick);
                        entry.extractEnergy(removed);
                        tick.addEnergy(removed);
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
    }

    public IEnergyTile getTileEntity(BlockPos pos) {
        return this.chunkCoordinatesIEnergyTileMap.getOrDefault(pos, EnergyNetGlobal.EMPTY);
    }

    public NodeStats getNodeStats(final IEnergyTile tile) {
        if (tile instanceof IEnergyConductor) {
            return new NodeStats(0, 0);
        } else {
            final double emitted = this.getTotalEnergyEmitted(tile);
            final double received = this.getTotalEnergySunken(tile);
            return new NodeStats(received, emitted);
        }
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
