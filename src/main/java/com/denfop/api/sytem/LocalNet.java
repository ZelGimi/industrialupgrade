package com.denfop.api.sytem;


import com.denfop.api.energy.NodeStats;
import com.denfop.api.energy.SystemTick;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;

import java.util.*;

public class LocalNet implements ILocalNet {

    final SystemTickList<SystemTick<ISource, Path>> senderPath = new SystemTickList<>();
    private final Map<BlockPos, ITile> chunkCoordinatesTileMap;
    private final EnergyType energyType;
    List<ISource> sourceToUpdateList = new ArrayList<>();
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
                if (ticks.getList() != null) {
                    for (Path path : ticks.getList()) {
                        path.target.getEnergyTickList().remove(ticks.getSource());
                    }
                }
                ticks.setList(null);
                break;
            }
        }

    }

    public void remove(final ISource par1) {
        final SystemTick<ISource, Path> energyTick = this.senderPath.removeSource(par1);
        if (energyTick != null)
        if (energyTick.getList() != null) {
            for (Path path : energyTick.getList()) {
                path.target.getEnergyTickList().remove(energyTick.getSource());
            }
        }
    }


    public void removeAll(final List<SystemTick<ISource, Path>> par1) {
        if (par1 == null) {
            return;
        }

        for (SystemTick<ISource, Path> IEnergySource : par1) {
            if (IEnergySource.getList() != null) {
                for (Path path : IEnergySource.getList()) {
                    path.target.getEnergyTickList().remove(IEnergySource.getSource());
                }
            }
            IEnergySource.setList(null);
        }
    }


    public List<Path> getPaths(final IAcceptor par1) {
        final List<Path> paths = new ArrayList<>();
        List<SystemTick<ISource, Path>> sources_list = this.getSources(par1);
        if (sources_list == null || sources_list.isEmpty()) {
            return paths;
        }
        for (final SystemTick<ISource, Path> source : sources_list) {
            paths.addAll(source.getList());
        }
        return paths;
    }

    public List<SystemTick<ISource, Path>> getSources(final IAcceptor par1) {
        if (par1 instanceof ISink) {
            List<SystemTick<ISource, Path>> list = new LinkedList<>();
            for (SystemTick<ISource, Path> energyTicks : senderPath) {
                if (((ISink) par1).getEnergyTickList().contains(energyTicks.getSource())) {
                    list.add(energyTicks);
                }
            }
            return list;
        } else {
            if (par1 instanceof IConductor) {
                List<SystemTick<ISource, Path>> list = new LinkedList<>();
                for (SystemTick<ISource, Path> energyTicks : senderPath) {
                    if (energyTicks.getConductors().contains(par1)) {
                        list.add(energyTicks);
                    }
                }
                return new ArrayList<>(list);
            }
            return Collections.emptyList();
        }
    }


    public void clear() {
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
            final Tuple<List<Path>, LinkedList<IConductor>> tuples = this.discover(energySource, tick);
            energyPaths = tuples.getA();
            List<IConductor> conductors = tick.getConductors();
            if (conductors == null) {
                tick.setConductors(tuples.getB());
            } else {
                tick.setConductors(tuples.getB());
            }
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
            }
        }

        return amount;
    }

    @Override
    public void TickEnd() {
        if (!sourceToUpdateList.isEmpty()) {
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
        this.addTileEntity(tile1.getPos(), tile1);
    }


    public void removeTile(ITile tile1) {

        this.removeTileEntity(tile1);

    }

    private void updateRemove(BlockPos pos, ITile tile) {
        for (final Direction dir : Direction.values()) {
            BlockPos pos1 = pos
                    .offset(dir.getNormal());
            final ITile tile1 = this.chunkCoordinatesTileMap.get(pos1);
            if (tile1 != null) {
                tile1.RemoveTile(energyType, tile, dir.getOpposite());
            }

        }
    }

    public void removeTileEntity(ITile tile) {
        if (!this.chunkCoordinatesTileMap.containsKey(tile.getPos())) {
            return;
        }
        final BlockPos coord = tile.getPos();
        this.chunkCoordinatesTileMap.remove(coord);
        if (tile instanceof IAcceptor) {
            this.removeAll(this.getSources((IAcceptor) tile));
        }
        if (tile instanceof ISource) {
            this.remove((ISource) tile);

        }
        this.updateRemove(coord, tile);
    }

    public Tuple<List<Path>, LinkedList<IConductor>> discover(final ISource emitter, final SystemTick<ISource, Path> tick) {
        final LinkedList<ITile> tileEntitiesToCheck = new LinkedList<>();
        List<Path> energyPaths = new LinkedList<>();
        long id = WorldBaseGen.random.nextLong();
        emitter.setId(id);
        tileEntitiesToCheck.push(emitter);
        LinkedList<IConductor> set = new LinkedList<>();
        while (!tileEntitiesToCheck.isEmpty()) {
            final ITile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<ITile>> validReceivers = this.getValidReceivers(currentTileEntity);
            InfoCable cable = null;
            if (currentTileEntity instanceof IConductor) {
                cable = ((IConductor) currentTileEntity).getCable(this.energyType);
            }
            for (final InfoTile<ITile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof ISink) {
                        energyPaths.add(new Path((ISink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof IConductor) {
                        IConductor conductor = (IConductor) validReceiver.tileEntity;
                        conductor.setCable(energyType, new InfoCable(conductor, validReceiver.direction, cable));
                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }
        int id1 = WorldBaseGen.random.nextInt();
        energyPaths = new ArrayList<>(energyPaths);
        for (Path energyPath : energyPaths) {
            ITile tileEntity = energyPath.target;
            energyPath.target.getEnergyTickList().add(tick.getSource());
            Direction energyBlockLink = energyPath.targetDirection;
            tileEntity = tileEntity.getTiles(energyType).get(energyBlockLink);
            if (!(tileEntity instanceof IConductor)) {
                continue;
            }
            InfoCable cable = ((IConductor) tileEntity).getCable(energyType);

            while (cable != null) {
                final IConductor energyConductor = cable.getConductor();
                if (energyConductor.getHashCodeSource() != id1) {
                    energyConductor.setHashCodeSource(id1);
                    set.add(energyConductor);
                }
                cable = cable.getPrev();
                if (cable == null) {
                    break;
                }
            }

        }
        return new Tuple<>(energyPaths, set);
    }

    public List<InfoTile<ITile>> getValidReceivers(final ITile emitter) {

        final BlockPos tile1;
        tile1 = emitter.getPos();
        if (tile1 != null) {
            if (emitter instanceof IDual) {
                final List<InfoTile<ITile>> validReceivers = new LinkedList<>();
                for (InfoTile<ITile> entry : emitter.getValidReceivers(energyType)) {
                    ITile target2;
                    target2 = entry.tileEntity;
                    if (target2 == emitter) {
                        continue;
                    }
                    if (target2 != null) {
                        final Direction inverseDirection2 = entry.direction;
                        if (target2 instanceof IAcceptor) {
                            final IEmitter sender2 = (IEmitter) emitter;
                            final IAcceptor receiver2 = (IAcceptor) target2;
                            if (sender2.emitsTo(receiver2, inverseDirection2.getOpposite()) && receiver2.acceptsFrom(
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
                final List<InfoTile<ITile>> validReceivers = new LinkedList<>();
                for (InfoTile<ITile> entry : emitter.getValidReceivers(energyType)) {
                    ITile target2;
                    target2 = entry.tileEntity;
                    if (target2 == emitter) {
                        continue;
                    }
                    if (target2 != null) {
                        final Direction inverseDirection2 = entry.direction;
                        if (target2 instanceof IAcceptor && !(target2 instanceof IConductor && emitter instanceof IConductor)) {
                            final IEmitter sender2 = (IEmitter) emitter;
                            final IAcceptor receiver2 = (IAcceptor) target2;
                            if (sender2.emitsTo(receiver2, inverseDirection2.getOpposite()) && receiver2.acceptsFrom(
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

    public void addTileEntity(final BlockPos coords, final ITile tile) {
        if (this.chunkCoordinatesTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesTileMap.put(coords, tile);
        this.updateAdd(coords, tile);
        if (tile instanceof IAcceptor) {
            this.onTileEntityAdded((IAcceptor) tile);
        }
        if (tile instanceof ISource) {
            senderPath.add(new SystemTick<>((ISource) tile, null));

        }
    }

    private void updateAdd(BlockPos pos, ITile tile) {
        for (final Direction dir : Direction.values()) {
            BlockPos pos1 = pos
                    .offset(dir.getNormal());
            final ITile tile1 = this.chunkCoordinatesTileMap.get(pos1);
            if (tile1 != null) {
                final Direction inverseDirection2 = dir.getOpposite();
                if (tile instanceof IDual) {
                    tile1.AddTile(energyType, tile, inverseDirection2);
                    tile.AddTile(energyType, tile1, dir);
                } else if (tile1 instanceof IEmitter && tile instanceof IAcceptor) {
                    final IEmitter sender2 = (IEmitter) tile1;
                    final IAcceptor receiver2 = (IAcceptor) tile;
                    if (tile1 instanceof IDual) {
                        tile1.AddTile(energyType, tile, inverseDirection2);
                        tile.AddTile(energyType, tile1, dir);
                    } else if (sender2.emitsTo(receiver2, dir.getOpposite()) && receiver2.acceptsFrom(
                            sender2,
                            inverseDirection2.getOpposite()
                    )) {
                        tile1.AddTile(energyType, tile, dir.getOpposite());
                        tile.AddTile(energyType, tile1, dir);
                    }
                } else if (tile1 instanceof IAcceptor && tile instanceof IEmitter) {
                    final IEmitter sender2 = (IEmitter) tile;
                    final IAcceptor receiver2 = (IAcceptor) tile1;
                    if (tile1 instanceof IDual) {
                        tile1.AddTile(energyType, tile, inverseDirection2);
                        tile.AddTile(energyType, tile1, dir);
                    } else if (sender2.emitsTo(receiver2, dir) && receiver2.acceptsFrom(
                            sender2,
                            inverseDirection2
                    )) {
                        tile1.AddTile(energyType, tile, dir.getOpposite());
                        tile.AddTile(energyType, tile1, dir);
                    }
                }
            }

        }
    }

    public void onTileEntityAdded(final IAcceptor tile) {
        final LinkedList<ITile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(tile);
        long id = WorldBaseGen.random.nextLong();
        this.sourceToUpdateList = new LinkedList<>();
        while (!tileEntitiesToCheck.isEmpty()) {
            final ITile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<ITile>> validReceivers = currentTileEntity.getValidReceivers(energyType);
            for (final InfoTile<ITile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != tile && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof ISource) {
                        this.sourceToUpdateList.add((ISource) validReceiver.tileEntity);
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof IConductor) {
                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }
        this.sourceToUpdateList = new ArrayList<>(sourceToUpdateList);
    }


}




