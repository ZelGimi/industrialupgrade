package com.denfop.api.sytem;

import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.IEnergyConductor;
import com.denfop.api.energy.IEnergySource;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.NodeStats;
import com.denfop.api.energy.SystemTick;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LocalNet implements ILocalNet {

    final List<SystemTick<ISource, Path>> senderPath = new ArrayList<>();

    final List<ISource> sourceToUpdateList = new ArrayList<>();
    private final Map<BlockPos, ITile> chunkCoordinatesTileMap;
    private final EnergyType energyType;
    private final EnumFacing[] directions = EnumFacing.values();
    private int tick;

    public LocalNet(EnergyType energyType) {
        this.energyType = energyType;
        this.chunkCoordinatesTileMap = new HashMap<>();
        this.tick = 0;
    }

    public void put(final ISource par1, final List<Path> par2) {
        this.senderPath.add(new SystemTick<>(par1, par2));
    }


    public boolean containsKey(final SystemTick<ISource, Path> par1) {
        return this.senderPath.contains(par1);
    }

    public boolean containsKey(final ISource par1) {
        return this.senderPath.contains(new SystemTick<ISource, Path>(par1, null));
    }


    public void remove1(final ISource par1) {

        for (SystemTick<ISource, Path> ticks : this.senderPath) {
            if (ticks.getSource() == par1) {
                ticks.setList(null);
                break;
            }
        }
    }

    public void remove(final ISource par1) {
        this.senderPath.remove(new SystemTick<ISource, Path>(par1, null));
    }

    public void remove(final SystemTick<ISource, Path> par1) {
        this.senderPath.remove(par1);
    }

    public void removeAll(final List<SystemTick<ISource, Path>> par1) {
        if (par1 == null) {
            return;
        }
        for (SystemTick<ISource, Path> iSESource : par1) {
            iSESource.setList(null);
        }
    }

 

    public List<Path> getPaths(final IAcceptor par1) {
        final List<Path> paths = new ArrayList<>();

        List<SystemTick<ISource, Path>> sources_list = this.getSources(par1);
        if (sources_list == null || sources_list.isEmpty()) {
            return paths;
        }
        for (final SystemTick<ISource, Path> source : sources_list) {
            if (this.containsKey(source)) {
                paths.addAll(source.getList());
            }
        }
        return paths;
    }

    public List<SystemTick<ISource, Path>> getSources(final IAcceptor par1) {
        final List<SystemTick<ISource, Path>> source = new ArrayList<>();
        for (final SystemTick<ISource, Path> entry : this.senderPath) {
            if (entry.getList() != null) {
                for (Path path : entry.getList()) {
                    if ((!(par1 instanceof IConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof ISink) || path.target != par1)) {
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
        for (SystemTick<ISource, Path> entry : this.senderPath) {
            List<Path> list = entry.getList();
            if (list != null) {
                for (Path SEPath : list) {
                    SEPath.conductors.clear();
                }
            }

        }
        this.senderPath.clear();
    }


    public SystemTick<ISource, Path> get(ISource tileEntity) {
        for (SystemTick<ISource, Path> entry : this.senderPath) {
            if (entry.getSource() == tileEntity) {
                return entry;
            }
        }
        return null;
    }

    public double emitEnergyFrom(final ISource energySource, double amount, final SystemTick<ISource, Path> tick) {
        List<Path> energyPaths = tick.getList();
        if (energyPaths == null) {
            energyPaths = this.discover(energySource);
            tick.setList(energyPaths);
        }
        if (!(energySource instanceof IDual) && energySource.isSource()) {
            energySource.setPastEnergy(energySource.getPerEnergy());
        } else if (energySource instanceof IDual && (energySource.isSource())) {
            ((IDual) energySource).setPastEnergy1(((IDual) energySource).getPerEnergy1());

        }
        if (amount > 0) {
            for (final Path energyPath : energyPaths) {
                if (amount <= 0) {
                    break;
                }
                final ISink energySink = energyPath.target;
                double demandedEnergy = energySink.getDemanded();
                if (demandedEnergy <= 0.0) {
                    continue;
                }
                double energyProvided = Math.min(demandedEnergy, amount);

                energySink.receivedEnergy(energyProvided);
                if (!(energySource instanceof IDual) && energySource.isSource()) {
                    energySource.addPerEnergy(energyProvided);
                } else if ((energySource instanceof IDual) && energySource.isSource()) {
                    ((IDual) energySource).addPerEnergy1(energyProvided);
                }

                energyPath.tick(this.tick, energyProvided);
                if (this.energyType.isDraw()) {
                    amount -= energyProvided;
                    amount = Math.max(0, amount);
                }
                if (this.energyType.isBreak_conductors()) {
                    if (energyProvided > energyPath.min) {
                        for (IConductor HeatConductor : energyPath.conductors) {
                            if (HeatConductor.getConductorBreakdownEnergy(this.energyType) < energyProvided) {
                                HeatConductor.removeConductor();
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

    @Override
    public void TickEnd() {
        if (sourceToUpdateList.size() > 0) {
            for (ISource source : sourceToUpdateList) {
                remove1(source);
            }
            sourceToUpdateList.clear();
        }

        for (SystemTick<ISource, Path> tick : this.senderPath) {
            final ISource entry = tick.getSource();
            if (tick.getList() != null) {
                if (tick.getList().isEmpty()) {
                    continue;
                }
            }

            if (entry != null) {
                if (entry.isSource()) {
                    if (entry instanceof IDual) {
                        ((IDual) entry).setPastEnergy1(((IDual) entry).getPastEnergy1());
                    } else {
                        entry.setPastEnergy(entry.getPerEnergy());
                    }
                }
                double offer = entry.canProvideEnergy();
                if (offer > 0) {

                    final double removed = offer - this.emitEnergyFrom(entry, offer, tick);
                    if (this.energyType.isDraw()) {
                        entry.extractEnergy(removed);
                    }


                } else {
                    if (entry.isSource()) {
                        if (entry instanceof IDual) {
                            ((IDual) entry).setPastEnergy1(((IDual) entry).getPastEnergy1());
                        } else {
                            entry.setPastEnergy(entry.getPerEnergy());
                        }
                    }

                }
            }
        }


        this.tick++;
    }

    public double getTotalEmitted(final ITile tileEntity) {
        double ret = 0.0;
        int col = 0;
        if (tileEntity instanceof IConductor) {
            for (final Path energyPath : this.getPaths((IAcceptor) tileEntity)) {
                if (energyPath.conductors.contains(
                        tileEntity)) {
                    ret += this.getTotalAccepted(energyPath.target);
                    col++;
                }
            }

        }
        if (tileEntity instanceof ISource) {
            ISource advEnergySource = (ISource) tileEntity;
            if (!(advEnergySource instanceof IDual) && advEnergySource.isSource()) {
                ret = Math.max(0, advEnergySource.getPerEnergy() - advEnergySource.getPastEnergy());

            } else if ((advEnergySource instanceof IDual) && advEnergySource.isSource()) {
                IDual dual = (IDual) advEnergySource;

                ret = Math.max(0, dual.getPerEnergy1() - dual.getPastEnergy1());

            }
        }
        return col == 0 ? ret : ret / col;
    }



    public double getTotalAccepted(final ITile tileEntity) {
        double ret = 0.0;
        if (tileEntity instanceof ISink) {
            ISink advEnergySink = (ISink) tileEntity;
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

    public void update(BlockPos pos1) {
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos = pos1.offset(dir);
            ITile tile = this.chunkCoordinatesTileMap.get(pos);
            if (tile instanceof IConductor) {
                ((IConductor) tile).update_render();
            }
        }
    }

    public NodeStats getNodeStats(final ITile tile) {
        final double emitted = this.getTotalEmitted(tile);
        final double received = this.getTotalAccepted(tile);
        return new NodeStats(received, emitted);
    }

    public void onUnload() {
        this.senderPath.clear();
        this.chunkCoordinatesTileMap.clear();
    }

    @Override
    public ITile getTileEntity(final BlockPos pos) {
        return this.chunkCoordinatesTileMap.get(pos);
    }

    @Override
    public void addTile(final ITile tile1) {
        this.addTileEntity(tile1.getBlockPos(), tile1);
    }

    public ITile getNeighbor(final ITile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }

        return this.getTileEntity(tile.getBlockPos().offset(dir));
    }

    public void removeTile(ITile tile1) {

        this.removeTileEntity(tile1);

    }

    public void removeTileEntity(ITile tile) {
        if (!this.chunkCoordinatesTileMap.containsKey(tile.getBlockPos())) {
            return;
        }
        final BlockPos coord = tile.getBlockPos();
        this.chunkCoordinatesTileMap.remove(coord);
        this.update(coord);
        if (tile instanceof IAcceptor) {
            this.removeAll(this.getSources((IAcceptor) tile));
            this.onTileEntityRemoved((IAcceptor) tile);
        }
        if (tile instanceof ISource) {
            this.remove((ISource) tile);

        }
    }

    public List<Path> discover(final ISource emitter) {
        final Map<IConductor, EnumFacing> reachedTileEntities = new HashMap<>();
        final List<ITile> tileEntitiesToCheck = new ArrayList<>();
        final List<Path> Paths = new ArrayList<>();

        tileEntitiesToCheck.add(emitter);

        while (!tileEntitiesToCheck.isEmpty()) {
            final ITile currentTileEntity = tileEntitiesToCheck.remove(0);
            final List<InfoTile<ITile>> validReceivers = this.getValidReceivers(currentTileEntity, false);
            for (final InfoTile<ITile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {
                    if (validReceiver.tileEntity instanceof ISink) {
                        Paths.add(new Path((ISink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }
                    if (reachedTileEntities.containsKey((IConductor) validReceiver.tileEntity)) {
                        continue;
                    }

                    reachedTileEntities.put((IConductor) validReceiver.tileEntity, validReceiver.direction);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }


        }
        for (Path Path : Paths) {
            ITile tileEntity = Path.target;
            EnumFacing QEBlockLink = Path.targetDirection;
            BlockPos te = Path.target.getBlockPos();
            if (emitter != null) {
                while (tileEntity != emitter) {
                    if (QEBlockLink != null && te != null) {
                        tileEntity = this.getTileEntity(te.offset(QEBlockLink));
                        te = te.offset(QEBlockLink);
                    }
                    if (!(tileEntity instanceof IConductor)) {
                        break;
                    }
                    final IConductor Conductor = (IConductor) tileEntity;
                    Path.conductors.add(Conductor);
                    if (Conductor.getConductorBreakdownEnergy(this.energyType) - 1 < Path.getMin()) {
                        Path.setMin(Conductor.getConductorBreakdownEnergy(this.energyType) - 1);
                    }
                    QEBlockLink = reachedTileEntities.get(tileEntity);
                    if (QEBlockLink != null) {
                        continue;
                    }
                    assert te != null;
                    throw new NullPointerException("BlockPos is null");
                }
            }
        }
        return Paths;
    }

    public List<InfoTile<ITile>> getValidReceivers(final ITile emitter, final boolean reverse) {
        final List<InfoTile<ITile>> validReceivers = new LinkedList<>();

        for (final EnumFacing direction : directions) {
            final ITile target2 = getNeighbor(emitter, direction);
            if (target2 != null) {
                final EnumFacing inverseDirection2 = direction.getOpposite();
                if (reverse) {
                    if (emitter instanceof IAcceptor && target2 instanceof IEmitter) {
                        final IEmitter sender2 = (IEmitter) target2;
                        final IAcceptor receiver2 = (IAcceptor) emitter;
                        if (sender2.emitsTo(receiver2, inverseDirection2) && receiver2.acceptsFrom(
                                sender2,
                                direction
                        )) {
                            validReceivers.add(new InfoTile<>(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof IEmitter && target2 instanceof IAcceptor) {
                    final IEmitter sender2 = (IEmitter) emitter;
                    final IAcceptor receiver2 = (IAcceptor) target2;
                    if (sender2.emitsTo(receiver2, direction) && receiver2.acceptsFrom(
                            sender2,
                            inverseDirection2
                    )) {
                        validReceivers.add(new InfoTile<>(target2, inverseDirection2));
                    }
                }
            }
        }


        return validReceivers;
    }

    public void addTileEntity(final BlockPos coords, final ITile tile) {
        if (this.chunkCoordinatesTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesTileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof IAcceptor) {
            this.onTileEntityAdded((IAcceptor) tile);
        }
        if (tile instanceof ISource) {
            senderPath.add(new SystemTick<>((ISource) tile, null));

        }
    }

    public void onTileEntityAdded(final IAcceptor tile) {
        final List<ITile> tileEntitiesToCheck = new ArrayList<>();

        final List<BlockPos> blockPosList = new ArrayList<>();
        blockPosList.add(tile.getBlockPos());
        tileEntitiesToCheck.add(tile);
        while (!tileEntitiesToCheck.isEmpty()) {
            final ITile currentTileEntity = tileEntitiesToCheck.remove(0);
            for (final EnumFacing direction : EnumFacing.values()) {
                final ITile target2 = this.getTileEntity(currentTileEntity.getBlockPos().offset(direction));
                if (target2 != null && !blockPosList.contains(target2.getBlockPos())) {
                    blockPosList.add(target2.getBlockPos());
                    if (target2 instanceof ISource) {
                        if (!sourceToUpdateList.contains((ISource) target2)) {
                            sourceToUpdateList.add((ISource) target2);
                        }
                        continue;
                    }
                    if (target2 instanceof IConductor) {
                        tileEntitiesToCheck.add(target2);
                    }
                }
            }


        }

    }

    public void onTileEntityRemoved(final IAcceptor par1) {

        this.onTileEntityAdded(par1);
    }



}




