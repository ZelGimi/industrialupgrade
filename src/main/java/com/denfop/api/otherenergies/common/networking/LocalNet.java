package com.denfop.api.otherenergies.common.networking;


import com.denfop.api.energy.SystemTick;
import com.denfop.api.energy.networking.ConductorInfo;
import com.denfop.api.energy.networking.NodeStats;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.otherenergies.common.InfoCable;
import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.api.otherenergies.common.interfaces.*;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;

import java.util.*;

public class LocalNet implements com.denfop.api.otherenergies.common.interfaces.LocalNet {

    final SystemTickList<SystemTick<Source, Path>> senderPath = new SystemTickList<>();
    private final Map<BlockPos, Tile> chunkCoordinatesTileMap;
    private final EnergyType energyType;
    List<Source> sourceToUpdateList = new ArrayList<>();
    private int tick;

    public LocalNet(EnergyType energyType) {
        this.energyType = energyType;
        this.chunkCoordinatesTileMap = new HashMap<>();
        this.tick = 0;
    }

    public void put(final Source par1, final List<Path> par2) {
        this.senderPath.add(new SystemTick<>(par1, par2));
    }


    public boolean containsKey(final SystemTick<Source, Path> par1) {
        return this.senderPath.contains(par1);
    }

    public boolean containsKey(final Source par1) {
        return this.senderPath.contains(new SystemTick<Source, Path>(par1, null));
    }

    public void remove1(final Source par1) {

        for (SystemTick<Source, Path> ticks : this.senderPath) {
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

    public void remove(final Source par1) {
        final SystemTick<Source, Path> energyTick = this.senderPath.removeSource(par1);
        if (energyTick != null)
            if (energyTick.getList() != null) {
                for (Path path : energyTick.getList()) {
                    path.target.getEnergyTickList().remove(energyTick.getSource());
                }
            }
    }


    public void removeAll(final List<SystemTick<Source, Path>> par1) {
        if (par1 == null) {
            return;
        }

        for (SystemTick<Source, Path> IEnergySource : par1) {
            if (IEnergySource.getList() != null) {
                for (Path path : IEnergySource.getList()) {
                    path.target.getEnergyTickList().remove(IEnergySource.getSource());
                }
            }
            IEnergySource.setList(null);
        }
    }


    public List<Path> getPaths(final Acceptor par1) {
        final List<Path> paths = new ArrayList<>();
        List<SystemTick<Source, Path>> sources_list = this.getSources(par1);
        if (sources_list == null || sources_list.isEmpty()) {
            return paths;
        }
        for (final SystemTick<Source, Path> source : sources_list) {
            paths.addAll(source.getList());
        }
        return paths;
    }

    public List<SystemTick<Source, Path>> getSources(final Acceptor par1) {
        if (par1 instanceof Sink) {
            List<SystemTick<Source, Path>> list = new LinkedList<>();
            for (SystemTick<Source, Path> energyTicks : senderPath) {
                if (((Sink) par1).getEnergyTickList().contains(energyTicks.getSource())) {
                    list.add(energyTicks);
                }
            }
            return list;
        } else {
            if (par1 instanceof Conductor) {
                List<SystemTick<Source, Path>> list = new LinkedList<>();
                for (SystemTick<Source, Path> energyTicks : senderPath) {
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


    public SystemTick<Source, Path> get(Source tileEntity) {
        for (SystemTick<Source, Path> entry : this.senderPath) {
            if (entry.getSource() == tileEntity) {
                return entry;
            }
        }
        return null;
    }

    public double emitEnergyFrom(final Source energySource, double amount, final SystemTick<Source, Path> tick) {
        List<Path> energyPaths = tick.getList();
        if (energyPaths == null) {
            final Tuple<List<Path>, LinkedList<Conductor>> tuples = this.discover(energySource, tick);
            energyPaths = tuples.getA();
            List<Conductor> conductors = tick.getConductors();
            if (conductors == null) {
                tick.setConductors(tuples.getB());
            } else {
                tick.setConductors(tuples.getB());
            }
            tick.setList(energyPaths);
        }
        if (!(energySource instanceof Dual) && energySource.isSource()) {
            energySource.setPastEnergy(energySource.getPerEnergy());
        } else if (energySource instanceof Dual && (energySource.isSource())) {
            ((Dual) energySource).setPastEnergy1(((Dual) energySource).getPerEnergy1());

        }
        if (amount > 0) {
            for (final Path energyPath : energyPaths) {
                if (amount <= 0) {
                    break;
                }
                final Sink energySink = energyPath.target;
                double demandedEnergy = energySink.getDemanded();
                if (demandedEnergy <= 0.0) {
                    continue;
                }
                double energyProvided = Math.min(demandedEnergy, amount);

                energySink.receivedEnergy(energyProvided);
                if (!(energySource instanceof Dual) && energySource.isSource()) {
                    energySource.addPerEnergy(energyProvided);
                } else if ((energySource instanceof Dual) && energySource.isSource()) {
                    ((Dual) energySource).addPerEnergy1(energyProvided);
                }

                energyPath.tick(this.tick, energyProvided);
                if (this.energyType.isDraw()) {
                    amount -= energyProvided;
                    amount = Math.max(0, amount);
                }
                for (ConductorInfo energyConductor : energyPath.conductorList) {
                    energyConductor.addEnergy((byte) this.tick, energyProvided);
                }
            }
        }

        return amount;
    }

    @Override
    public void TickEnd() {
        if (!sourceToUpdateList.isEmpty()) {
            for (Source source : sourceToUpdateList) {
                remove1(source);
            }
            sourceToUpdateList.clear();
        }

        for (SystemTick<Source, Path> tick : this.senderPath) {
            final Source entry = tick.getSource();
            if (tick.getList() != null) {
                if (tick.getList().isEmpty()) {
                    continue;
                }
            }

            if (entry != null) {
                if (entry.isSource()) {
                    if (entry instanceof Dual) {
                        ((Dual) entry).setPastEnergy1(((Dual) entry).getPastEnergy1());
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
                        if (entry instanceof Dual) {
                            ((Dual) entry).setPastEnergy1(((Dual) entry).getPastEnergy1());
                        } else {
                            entry.setPastEnergy(entry.getPerEnergy());
                        }
                    }

                }
            }
        }


        this.tick++;
    }

    public double getTotalEmitted(final Tile tileEntity) {
        double ret = 0.0;
        int col = 0;
        if (tileEntity instanceof Conductor) {
            ret = Math.max(((Conductor) tileEntity).getInfo(energyType).getEnergy(this.tick), ret);
            return ret;
        }
        if (tileEntity instanceof Source) {
            Source advEnergySource = (Source) tileEntity;
            if (!(advEnergySource instanceof Dual) && advEnergySource.isSource()) {
                ret = Math.max(0, advEnergySource.getPerEnergy() - advEnergySource.getPastEnergy());

            } else if ((advEnergySource instanceof Dual) && advEnergySource.isSource()) {
                Dual dual = (Dual) advEnergySource;

                ret = Math.max(0, dual.getPerEnergy1() - dual.getPastEnergy1());

            }
        }
        return col == 0 ? ret : ret / col;
    }


    public double getTotalAccepted(final Tile tileEntity) {
        double ret = 0.0;
        if (tileEntity instanceof Conductor) {
            ret = Math.max(((Conductor) tileEntity).getInfo(energyType).getEnergy(this.tick), ret);
            return ret;
        }
        if (tileEntity instanceof Sink) {
            Sink advEnergySink = (Sink) tileEntity;
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


    public NodeStats getNodeStats(final Tile tile) {
        final double emitted = this.getTotalEmitted(tile);
        final double received = this.getTotalAccepted(tile);
        return new NodeStats(received, emitted);
    }

    public void onUnload() {
        this.senderPath.clear();
        this.chunkCoordinatesTileMap.clear();
    }

    @Override
    public Tile getTileEntity(final BlockPos pos) {
        return this.chunkCoordinatesTileMap.get(pos);
    }

    @Override
    public void addTile(final Tile tile1) {
        this.addTileEntity(tile1.getPos(), tile1);
    }


    public void removeTile(Tile tile1) {

        this.removeTileEntity(tile1);

    }

    private void updateRemove(BlockPos pos, Tile tile) {
        for (final Direction dir : Direction.values()) {
            BlockPos pos1 = pos
                    .offset(dir.getNormal());
            final Tile tile1 = this.chunkCoordinatesTileMap.get(pos1);
            if (tile1 != null) {
                tile1.RemoveTile(energyType, tile, dir.getOpposite());
            }

        }
    }

    public void removeTileEntity(Tile tile) {
        if (!this.chunkCoordinatesTileMap.containsKey(tile.getPos())) {
            return;
        }
        final BlockPos coord = tile.getPos();
        this.chunkCoordinatesTileMap.remove(coord);
        if (tile instanceof Acceptor) {
            this.removeAll(this.getSources((Acceptor) tile));
        }
        if (tile instanceof Source) {
            this.remove((Source) tile);

        }
        this.updateRemove(coord, tile);
    }

    public Tuple<List<Path>, LinkedList<Conductor>> discover(final Source emitter, final SystemTick<Source, Path> tick) {
        final LinkedList<Tile> tileEntitiesToCheck = new LinkedList<>();
        List<Path> energyPaths = new LinkedList<>();
        long id = WorldBaseGen.random.nextLong();
        emitter.setId(id);
        tileEntitiesToCheck.push(emitter);
        LinkedList<Conductor> set = new LinkedList<>();
        while (!tileEntitiesToCheck.isEmpty()) {
            final Tile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<Tile>> validReceivers = this.getValidReceivers(currentTileEntity);
            InfoCable cable = null;
            if (currentTileEntity instanceof Conductor) {
                cable = ((Conductor) currentTileEntity).getCable(this.energyType);
            }
            for (final InfoTile<Tile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof Sink) {
                        energyPaths.add(new Path((Sink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof Conductor) {
                        Conductor conductor = (Conductor) validReceiver.tileEntity;
                        conductor.setCable(energyType, new InfoCable(conductor, validReceiver.direction, cable));
                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }
        int id1 = WorldBaseGen.random.nextInt();
        energyPaths = new ArrayList<>(energyPaths);
        for (Path energyPath : energyPaths) {
            Tile tileEntity = energyPath.target;

            List<ConductorInfo> pathConductorsList = new LinkedList<>();
            energyPath.target.getEnergyTickList().add(tick.getSource());
            Direction energyBlockLink = energyPath.targetDirection;
            tileEntity = tileEntity.getTiles(energyType).get(energyBlockLink);
            if (!(tileEntity instanceof Conductor)) {
                continue;
            }
            InfoCable cable = ((Conductor) tileEntity).getCable(energyType);

            while (cable != null) {
                final Conductor energyConductor = cable.getConductor();
                pathConductorsList.add(energyConductor.getInfo(energyType));
                if (energyConductor.getHashCodeSource() != id1) {
                    energyConductor.setHashCodeSource(id1);
                    set.add(energyConductor);
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

    public List<InfoTile<Tile>> getValidReceivers(final Tile emitter) {

        final BlockPos tile1;
        tile1 = emitter.getPos();
        if (tile1 != null) {
            if (emitter instanceof Dual) {
                final List<InfoTile<Tile>> validReceivers = new LinkedList<>();
                for (InfoTile<Tile> entry : emitter.getValidReceivers(energyType)) {
                    Tile target2;
                    target2 = entry.tileEntity;
                    if (target2 == emitter) {
                        continue;
                    }
                    if (target2 != null) {
                        final Direction inverseDirection2 = entry.direction;
                        if (target2 instanceof Acceptor) {
                            final Emitter sender2 = (Emitter) emitter;
                            final Acceptor receiver2 = (Acceptor) target2;
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
                final List<InfoTile<Tile>> validReceivers = new LinkedList<>();
                for (InfoTile<Tile> entry : emitter.getValidReceivers(energyType)) {
                    Tile target2;
                    target2 = entry.tileEntity;
                    if (target2 == emitter) {
                        continue;
                    }
                    if (target2 != null) {
                        final Direction inverseDirection2 = entry.direction;
                        if (target2 instanceof Acceptor && !(target2 instanceof Conductor && emitter instanceof Conductor)) {
                            final Emitter sender2 = (Emitter) emitter;
                            final Acceptor receiver2 = (Acceptor) target2;
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

    public void addTileEntity(final BlockPos coords, final Tile tile) {
        if (this.chunkCoordinatesTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesTileMap.put(coords, tile);
        this.updateAdd(coords, tile);
        if (tile instanceof Acceptor) {
            this.onTileEntityAdded((Acceptor) tile);
        }
        if (tile instanceof Source) {
            senderPath.add(new SystemTick<>((Source) tile, null));

        }
    }

    private void updateAdd(BlockPos pos, Tile tile) {
        for (final Direction dir : Direction.values()) {
            BlockPos pos1 = pos
                    .offset(dir.getNormal());
            final Tile tile1 = this.chunkCoordinatesTileMap.get(pos1);
            if (tile1 != null) {
                final Direction inverseDirection2 = dir.getOpposite();
                if (tile instanceof Dual) {
                    tile1.AddTile(energyType, tile, inverseDirection2);
                    tile.AddTile(energyType, tile1, dir);
                } else if (tile1 instanceof Emitter && tile instanceof Acceptor) {
                    final Emitter sender2 = (Emitter) tile1;
                    final Acceptor receiver2 = (Acceptor) tile;
                    if (tile1 instanceof Dual) {
                        tile1.AddTile(energyType, tile, inverseDirection2);
                        tile.AddTile(energyType, tile1, dir);
                    } else if (sender2.emitsTo(receiver2, dir.getOpposite()) && receiver2.acceptsFrom(
                            sender2,
                            inverseDirection2.getOpposite()
                    )) {
                        tile1.AddTile(energyType, tile, dir.getOpposite());
                        tile.AddTile(energyType, tile1, dir);
                    }
                } else if (tile1 instanceof Acceptor && tile instanceof Emitter) {
                    final Emitter sender2 = (Emitter) tile;
                    final Acceptor receiver2 = (Acceptor) tile1;
                    if (tile1 instanceof Dual) {
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

    public void onTileEntityAdded(final Acceptor tile) {
        final LinkedList<Tile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(tile);
        long id = WorldBaseGen.random.nextLong();
        this.sourceToUpdateList = new LinkedList<>();
        while (!tileEntitiesToCheck.isEmpty()) {
            final Tile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<Tile>> validReceivers = currentTileEntity.getValidReceivers(energyType);
            for (final InfoTile<Tile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != tile && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof Source) {
                        this.sourceToUpdateList.add((Source) validReceiver.tileEntity);
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof Conductor) {
                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }
        this.sourceToUpdateList = new ArrayList<>(sourceToUpdateList);
    }


}




