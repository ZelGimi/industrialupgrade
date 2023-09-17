package com.denfop.api.energy;

import com.denfop.api.sytem.InfoTile;
import com.denfop.api.transport.ITransportTile;
import com.denfop.api.transport.TransportNetGlobal;
import com.denfop.api.transport.event.TransportTileUnLoadEvent;
import com.denfop.network.packet.PacketExplosion;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EnergyNetLocal {


    public static EnergyNetLocal EMPTY = new EnergyNetLocal();
    final EnergyTickList<EnergyTick> energyTickList = new EnergyTickList<>();
    private World world;
    private final List<IEnergyController> controllerList;
    private final Map<BlockPos, IEnergyTile> chunkCoordinatesIEnergyTileMap;
    private final List<IEnergySource> energySourceList = new ArrayList<>();
    private SunCoef suncoef;
    final List<IEnergySource> sourceToUpdateList = new ArrayList<>();
    private final boolean hasrestrictions;
    private final boolean explosing;
    private final boolean ignoring;
    private final boolean losing;
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
            new PacketExplosion(explosion);

        }
    }

    public SunCoef getSuncoef() {
        if(suncoef == null)
            suncoef = new SunCoef(world);
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

    public void addTileEntity(final BlockPos coords, final IEnergyTile tile) {
        if (this.chunkCoordinatesIEnergyTileMap.containsKey(coords)) {
            return;
        }

        this.chunkCoordinatesIEnergyTileMap.put(coords, tile);

        this.update(coords);
        if (tile instanceof IEnergyAcceptor) {
            this.onTileEntityAdded((IEnergyAcceptor) tile);
        }
        if (tile instanceof IEnergySource) {
            energyTickList.add(new EnergyTick((IEnergySource) tile, null));

        }

    }



    public void removeTile(IEnergyTile tile1) {
        if (tile1 != EnergyNetGlobal.EMPTY) {
            this.removeTileEntity(tile1);
        }

    }


    public void removeTileEntity(IEnergyTile tile1, IEnergyTile tile) {

        if (!this.chunkCoordinatesIEnergyTileMap.containsKey(tile1.getBlockPos())) {
            return;
        }

        this.chunkCoordinatesIEnergyTileMap.remove(tile1.getBlockPos(), tile);
        this.update(tile1.getBlockPos());
        if (tile instanceof IEnergyAcceptor) {
            this.removeAll(this.getSources((IEnergyAcceptor) tile));
            this.onTileEntityRemoved((IEnergyAcceptor) tile);
        }
        if (tile instanceof IEnergySource) {

            this.remove((IEnergySource) tile);
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
        this.update(coord);
        if (tile instanceof IEnergyAcceptor) {
            this.removeAll(this.getSources((IEnergyAcceptor) tile));
            this.onTileEntityRemoved((IEnergyAcceptor) tile);
        }
        if (tile instanceof IEnergySource) {
            this.remove((IEnergySource) tile);
        }
    }

    public TileEntity getTileFromMap(IEnergyTile tile) {
        return tile.getTileEntity();
    }

    public double emitEnergyFrom(final IEnergySource energySource, double amount, final EnergyTick tick) {
        List<Path> energyPaths = tick.getList();
        if (energyPaths == null) {
            energyPaths = this.discover(energySource);
            for (Path path : energyPaths) {

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


        if (amount > 0) {
            for (final Path energyPath : energyPaths) {
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
                    int tier1 = EnergyNetGlobal.instance.getTierFromPower(adding);
                    if (tier1 > tier) {
                        if (energyPath.hasController) {
                            continue;
                        }
                        explodeTiles(energySink);
                        continue;
                    }
                }
                energySink.receiveEnergy(adding);
                energyPath.totalEnergyConducted = (long) adding;
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
            for (final Path energyPath : this.getPaths((IEnergyAcceptor) tileEntity)) {
                if (energyPath.conductors.contains(
                        tileEntity)) {
                    double energy = this.getTotalEnergySunken(energyPath.target);
                    ret += energy;
                    if (energy != 0) {
                        col++;
                    }
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
                for (final Path energyPath : this.getPaths((IEnergyAcceptor) tileEntity)) {
                    if (energyPath.conductors.contains(
                            tileEntity)) {
                        double energy = this.getTotalEnergySunken(energyPath.target);
                        ret += energy;
                        if (energy != 0) {
                            col++;
                        }
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

    public List<Path> discover(final IEnergySource emitter) {
        final Map<IEnergyConductor, EnumFacing> reachedTileEntities = new HashMap<>();
        final List<IEnergyTile> tileEntitiesToCheck = new ArrayList<>();
        final List<Path> energyPaths = new ArrayList<>();
        tileEntitiesToCheck.add(emitter);
        while (!tileEntitiesToCheck.isEmpty()) {
            final IEnergyTile currentTileEntity = tileEntitiesToCheck.remove(0);
            final List<InfoTile<IEnergyTile>> validReceivers = this.getValidReceivers(currentTileEntity, false);

            for (final InfoTile<IEnergyTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {
                    if (validReceiver.tileEntity instanceof IEnergySink) {
                        energyPaths.add(new Path((IEnergySink) validReceiver.tileEntity, validReceiver.direction));
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
        for (Path energyPath : energyPaths) {
            IEnergyTile tileEntity = energyPath.target;
            EnumFacing energyBlockLink = energyPath.targetDirection;
            BlockPos te;
            te = tileEntity.getBlockPos();
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
            }

        }
        return energyPaths;
    }

    public void put(final IEnergySource par1, final List<Path> par2) {
        this.energyTickList.add(new EnergyTick(par1, par2));
    }


    public boolean containsKey(final EnergyTick par1) {
        return this.energyTickList.contains(par1);
    }

    public boolean containsKey(final IEnergySource par1) {
        return this.energyTickList.contains(par1);
    }


    public void remove1(final IEnergySource par1) {

        for (EnergyTick ticks : this.energyTickList) {
            if (ticks.getSource() == par1) {
                ticks.setList(null);
                break;
            }
        }
    }

    public void remove(final IEnergySource par1) {
        this.energyTickList.remove(par1);
    }

    public void remove(final EnergyTick par1) {
        this.energyTickList.remove(par1);
    }

    public void removeAll(final List<EnergyTick> par1) {
        if (par1 == null) {
            return;
        }
        for (EnergyTick IEnergySource : par1) {
            IEnergySource.setList(null);
        }
    }


    public List<Path> getPaths(final IEnergyAcceptor par1) {
        final List<Path> paths = new ArrayList<>();

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
        final List<EnergyTick> source = new ArrayList<>();
        for (final EnergyTick entry : this.energyTickList) {
            if (entry.getList() != null) {
                for (Path path : entry.getList()) {
                    if ((!(par1 instanceof IEnergyConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof IEnergySink) || path.target != par1)) {
                        continue;
                    }
                    source.add(entry);
                    break;
                }
            }
        }
        return source;
    }


    public void clear() {
        this.energyTickList.clear();
    }




    private List<InfoTile<IEnergyTile>> getValidReceivers(final IEnergyTile emitter, final boolean reverse) {
        final List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();

        final BlockPos tile1;
        tile1 = emitter.getBlockPos();
        if (tile1 != null) {
            for (final EnumFacing direction : EnumFacing.values()) {
                final IEnergyTile target2 = this.getTileEntity(tile1.offset(direction));

                if (target2 == emitter) {
                    continue;
                }
                if (target2 != EnergyNetGlobal.EMPTY) {
                    final EnumFacing inverseDirection2 = direction.getOpposite();
                    if (reverse) {
                        if (emitter instanceof IEnergyAcceptor && target2 instanceof IEnergyEmitter) {
                            final IEnergyEmitter sender2 = (IEnergyEmitter) target2;
                            final IEnergyAcceptor receiver2 = (IEnergyAcceptor) emitter;
                            if (sender2.emitsEnergyTo(receiver2, inverseDirection2) && receiver2.acceptsEnergyFrom(
                                    sender2,
                                    direction
                            )) {
                                validReceivers.add(new InfoTile<>(target2, inverseDirection2));
                            }
                        }
                    } else if (emitter instanceof IEnergyEmitter && target2 instanceof IEnergyAcceptor) {
                        final IEnergyEmitter sender2 = (IEnergyEmitter) emitter;
                        final IEnergyAcceptor receiver2 = (IEnergyAcceptor) target2;
                        if (sender2.emitsEnergyTo(receiver2, direction) && receiver2.acceptsEnergyFrom(
                                sender2,
                                inverseDirection2
                        )) {
                            validReceivers.add(new InfoTile<>(target2, inverseDirection2));
                        }
                    }
                }
            }

        }


        return validReceivers;
    }


    public void onTickEnd() {

        for (IEnergySource source : energySourceList) {
            removeTile(source);
            explodeMachineAt(getTileFromIEnergy(source));

        }
        energySourceList.clear();
        if(suncoef == null)
            suncoef = new SunCoef(world);
        this.suncoef.calculate();

        if (sourceToUpdateList.size() > 0) {
            for (IEnergySource source : sourceToUpdateList) {
                remove1(source);
            }
            sourceToUpdateList.clear();
        }
        for (EnergyTick tick : this.energyTickList) {
            final IEnergySource entry = tick.getSource();
            tick.tick();
            if (tick.getList() != null) {
                if (tick.getList().isEmpty()) {
                    continue;
                }
            }
            int multi = entry instanceof IMultiDual ? 4 : 1;
            for (int i = 0; i < multi; i++) {
                double offer = Math.min(
                        entry.canExtractEnergy(),
                        EnergyNetGlobal.instance.getPowerFromTier(entry.getSourceTier())
                );
                if (offer > 0) {
                    final double removed = offer - this.emitEnergyFrom(entry, offer, tick);
                    entry.extractEnergy(removed);
                    tick.addEnergy(removed);
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
            for (final Path energyPath : this.getPaths((IEnergyAcceptor) energyTile)) {

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
            if (tile != EnergyNetGlobal.EMPTY) {
                if (tile instanceof IEnergyConductor) {
                    ((IEnergyConductor) tile).update_render();
                }
            }

        }
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
        final List<IEnergyTile> tileEntitiesToCheck = new ArrayList<>();

        final List<BlockPos> blockPosList = new ArrayList<>();
        blockPosList.add(tile.getBlockPos());
        tileEntitiesToCheck.add(tile);
        while (!tileEntitiesToCheck.isEmpty()) {
            final IEnergyTile currentTileEntity = tileEntitiesToCheck.remove(0);
            for (final EnumFacing direction : EnumFacing.values()) {
                final IEnergyTile target2 = this.getTileEntity(currentTileEntity.getBlockPos().offset(direction));
                if (target2 != EnergyNetGlobal.EMPTY && !blockPosList.contains(target2.getBlockPos())) {
                    blockPosList.add(target2.getBlockPos());
                    if (target2 instanceof IEnergySource) {
                        if (!sourceToUpdateList.contains((IEnergySource) target2)) {
                            sourceToUpdateList.add((IEnergySource) target2);
                        }
                        continue;
                    }
                    if (target2 instanceof IEnergyConductor) {
                        tileEntitiesToCheck.add(target2);
                    }
                }
            }


        }

    }

    public void onTileEntityRemoved(final IEnergyAcceptor par1) {

        this.onTileEntityAdded(par1);
    }


}
