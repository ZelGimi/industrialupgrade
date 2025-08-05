package com.denfop.pressure;


import com.denfop.ModConfig;
import com.denfop.api.pressure.*;
import com.denfop.api.sytem.InfoTile;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;

import java.util.*;

public class PressureNetLocal implements IPressureNet {

    final PressureTickList<PressureTick<IPressureSource, PressurePath>> senderPath = new PressureTickList<>();
    private final Map<BlockPos, IPressureTile> chunkCoordinatesTileMap;
    List<IPressureSource> sourceToUpdateList = new ArrayList<>();
    private int tick;

    public PressureNetLocal() {
        this.chunkCoordinatesTileMap = new HashMap<>();
        this.tick = 0;
    }

    public void put(final IPressureSource par1, final List<PressurePath> par2) {
        this.senderPath.add(new PressureTick<>(par1, par2));
    }


    public boolean containsKey(final PressureTick<IPressureSource, PressurePath> par1) {
        return this.senderPath.contains(par1);
    }

    public boolean containsKey(final IPressureSource par1) {
        return this.senderPath.contains(new PressureTick<IPressureSource, PressurePath>(par1, null));
    }

    public void remove1(final IPressureSource par1) {

        for (PressureTick<IPressureSource, PressurePath> ticks : this.senderPath) {
            if (ticks.getSource() == par1) {
                if (ticks.getList() != null) {
                    for (PressurePath PressurePath : ticks.getList()) {
                        PressurePath.target.getEnergyTickList().remove(ticks.getSource());
                    }
                }
                ticks.setList(null);
                break;
            }
        }

    }

    public void remove(final IPressureSource par1) {
        final PressureTick<IPressureSource, PressurePath> energyTick = this.senderPath.removeSource(par1);
        if (energyTick != null)
        if (energyTick.getList() != null) {
            for (PressurePath PressurePath : energyTick.getList()) {
                PressurePath.target.getEnergyTickList().remove(energyTick.getSource());
            }
        }
    }


    public void removeAll(final List<PressureTick<IPressureSource, PressurePath>> par1) {
        if (par1 == null) {
            return;
        }

        for (PressureTick<IPressureSource, PressurePath> IEnergySource : par1) {
            if (IEnergySource.getList() != null) {
                for (PressurePath PressurePath : IEnergySource.getList()) {
                    PressurePath.target.getEnergyTickList().remove(IEnergySource.getSource());
                }
            }
            IEnergySource.setList(null);
        }
    }


    public List<PressurePath> getPaths(final IPressureAcceptor par1) {
        final List<PressurePath> paths = new ArrayList<>();
        List<PressureTick<IPressureSource, PressurePath>> sources_list = this.getSources(par1);
        if (sources_list == null || sources_list.isEmpty()) {
            return paths;
        }
        for (final PressureTick<IPressureSource, PressurePath> source : sources_list) {
            paths.addAll(source.getList());
        }
        return paths;
    }

    public List<PressureTick<IPressureSource, PressurePath>> getSources(final IPressureAcceptor par1) {
        if (par1 instanceof IPressureSink) {
            List<PressureTick<IPressureSource, PressurePath>> list = new LinkedList<>();
            for (PressureTick<IPressureSource, PressurePath> energyTicks : senderPath) {
                if (((IPressureSink) par1).getEnergyTickList().contains(energyTicks.getSource())) {
                    list.add(energyTicks);
                }
            }
            return list;
        } else {
            if (par1 instanceof IPressureConductor) {
                List<PressureTick<IPressureSource, PressurePath>> list = new LinkedList<>();
                for (PressureTick<IPressureSource, PressurePath> energyTicks : senderPath) {
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


    public PressureTick<IPressureSource, PressurePath> get(IPressureSource tileEntity) {
        for (PressureTick<IPressureSource, PressurePath> entry : this.senderPath) {
            if (entry.getSource() == tileEntity) {
                return entry;
            }
        }
        return null;
    }

    public double emitEnergyFrom(final IPressureSource energySource, double amount, final PressureTick<IPressureSource, PressurePath> tick) {
        List<PressurePath> energyPaths = tick.getList();
        if (energyPaths == null) {
            final Tuple<List<PressurePath>, LinkedList<IPressureConductor>> tuples = this.discover(energySource, tick);
            energyPaths = tuples.getA();
            List<IPressureConductor> conductors = tick.getConductors();
            if (conductors == null) {
                tick.setConductors(tuples.getB());
            } else {
                tick.setConductors(tuples.getB());
            }
            tick.setList(energyPaths);
        }


        if (amount > 0) {
            for (final PressurePath energyPath : energyPaths) {
                if (amount <= 0) {
                    break;
                }
                final IPressureSink energySink = energyPath.target;
                double demandedEnergy = energySink.getDemandedPressure();
                if (demandedEnergy <= 0.0) {
                    continue;
                }
                if (demandedEnergy != amount && ModConfig.COMMON.pressureWork.get()) {
                    continue;
                }
                double energyProvided = Math.min(demandedEnergy, amount);

                energySink.receivedPressure(energyProvided);


                energyPath.tick(this.tick, energyProvided);
            }
        }

        return amount;
    }


    public void onTickEnd() {
        if (!sourceToUpdateList.isEmpty()) {
            for (IPressureSource source : sourceToUpdateList) {
                remove1(source);
            }
            sourceToUpdateList.clear();
        }

        for (PressureTick<IPressureSource, PressurePath> tick : this.senderPath) {
            final IPressureSource entry = tick.getSource();
            if (tick.getList() != null) {
                if (tick.getList().isEmpty()) {
                    continue;
                }
            }

            if (entry != null) {
                double offer = entry.getOfferedPressure();
                if (offer > 0) {
                    this.emitEnergyFrom(entry, offer, tick);
                }
            }
        }


        this.tick++;
    }







    public void onUnload() {
        this.senderPath.clear();
        this.chunkCoordinatesTileMap.clear();
    }




    public void removeTile(IPressureTile tile1) {

        this.removeTileEntity(tile1);

    }

    private void updateRemove(BlockPos pos, IPressureTile tile) {
        for (final Direction dir : Direction.values()) {
            BlockPos pos1 = pos
                    .offset(dir.getNormal());
            final IPressureTile tile1 = this.chunkCoordinatesTileMap.get(pos1);
            if (tile1 != null) {
                tile1.RemoveTile(tile, dir.getOpposite());
            }

        }
    }

    public void removeTileEntity(IPressureTile tile) {
        if (!this.chunkCoordinatesTileMap.containsKey(tile.getPos())) {
            return;
        }
        final BlockPos coord = tile.getPos();
        this.chunkCoordinatesTileMap.remove(coord);
        if (tile instanceof IPressureAcceptor) {
            this.removeAll(this.getSources((IPressureAcceptor) tile));
        }
        if (tile instanceof IPressureSource) {
            this.remove((IPressureSource) tile);

        }
        this.updateRemove(coord, tile);
    }

    public Tuple<List<PressurePath>, LinkedList<IPressureConductor>> discover(final IPressureSource emitter, final PressureTick<IPressureSource, PressurePath> tick) {
        final LinkedList<IPressureTile> tileEntitiesToCheck = new LinkedList<>();
        List<PressurePath> energyPaths = new LinkedList<>();
        long id = WorldBaseGen.random.nextLong();
        emitter.setId(id);
        tileEntitiesToCheck.push(emitter);
        LinkedList<IPressureConductor> set = new LinkedList<>();
        while (!tileEntitiesToCheck.isEmpty()) {
            final IPressureTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<IPressureTile>> validReceivers = this.getValidReceivers(currentTileEntity);
            for (final InfoTile<IPressureTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof IPressureSink) {
                        energyPaths.add(new PressurePath((IPressureSink) validReceiver.tileEntity, validReceiver.direction));
                    }
                }
            }
        }
         energyPaths = new ArrayList<>(energyPaths);
        for (PressurePath energyPath : energyPaths) {
            energyPath.target.getEnergyTickList().add(tick.getSource());

        }
        return new Tuple<>(energyPaths, set);
    }

    public List<InfoTile<IPressureTile>> getValidReceivers(final IPressureTile emitter) {

        final BlockPos tile1;
        tile1 = emitter.getPos();
        return  emitter.getValidReceivers();

    }

    public void addTileEntity(final BlockPos coords, final IPressureTile tile) {
        if (this.chunkCoordinatesTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesTileMap.put(coords, tile);
        this.updateAdd(coords, tile);
        if (tile instanceof IPressureAcceptor) {
            this.onTileEntityAdded((IPressureAcceptor) tile);
        }
        if (tile instanceof IPressureSource) {
            senderPath.add(new PressureTick<>((IPressureSource) tile, null));

        }
    }

    private void updateAdd(BlockPos pos, IPressureTile tile) {
        for (final Direction dir : Direction.values()) {
            BlockPos pos1 = pos
                    .offset(dir.getNormal());
            final IPressureTile tile1 = this.chunkCoordinatesTileMap.get(pos1);
            if (tile1 != null) {
                final Direction inverseDirection2 = dir.getOpposite();
                if (tile1 instanceof IPressureEmitter && tile instanceof IPressureAcceptor) {
                    final IPressureEmitter sender2 = (IPressureEmitter) tile1;
                    final IPressureAcceptor receiver2 = (IPressureAcceptor) tile;
                    if (sender2.emitsPressureTo(receiver2, dir.getOpposite()) && receiver2.acceptsPressureFrom(
                            sender2,
                            inverseDirection2.getOpposite()
                    )) {
                        tile1.AddTile(tile, dir.getOpposite());
                        tile.AddTile(tile1, dir);
                    }
                } else if (tile1 instanceof IPressureAcceptor && tile instanceof IPressureEmitter) {
                    final IPressureEmitter sender2 = (IPressureEmitter) tile;
                    final IPressureAcceptor receiver2 = (IPressureAcceptor) tile1;
                    if (sender2.emitsPressureTo(receiver2, dir) && receiver2.acceptsPressureFrom(
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

    public void onTileEntityAdded(final IPressureAcceptor tile) {
        final LinkedList<IPressureTile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(tile);
        long id = WorldBaseGen.random.nextLong();
        this.sourceToUpdateList = new LinkedList<>();
        while (!tileEntitiesToCheck.isEmpty()) {
            final IPressureTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<IPressureTile>> validReceivers = currentTileEntity.getValidReceivers();
            for (final InfoTile<IPressureTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != tile && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof IPressureSource) {
                        this.sourceToUpdateList.add((IPressureSource) validReceiver.tileEntity);
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof IPressureConductor) {
                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }
        this.sourceToUpdateList = new ArrayList<>(sourceToUpdateList);
    }



    @Override
    public IPressureTile getSubTile(Level var1, BlockPos var2) {
        return this.chunkCoordinatesTileMap.get(var2);
    }
}




