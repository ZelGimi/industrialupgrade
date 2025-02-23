package com.denfop.heat;

import com.denfop.api.cool.CoolTick;
import com.denfop.api.cool.ICoolAcceptor;
import com.denfop.api.cool.ICoolConductor;
import com.denfop.api.cool.ICoolEmitter;
import com.denfop.api.cool.ICoolSink;
import com.denfop.api.cool.ICoolSource;
import com.denfop.api.cool.ICoolTile;
import com.denfop.api.energy.SystemTick;
import com.denfop.api.heat.HeatTick;
import com.denfop.api.heat.HeatTickList;
import com.denfop.api.heat.IHeatAcceptor;
import com.denfop.api.heat.IHeatConductor;
import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.heat.IHeatSink;
import com.denfop.api.heat.IHeatSource;
import com.denfop.api.heat.IHeatTile;
import com.denfop.api.heat.InfoCable;
import com.denfop.api.heat.Path;
import com.denfop.api.sytem.ISource;
import com.denfop.api.sytem.InfoTile;
import com.denfop.world.WorldBaseGen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HeatNetLocal {


    final HeatTickList<HeatTick<IHeatSource, Path>> senderPath = new HeatTickList<>();
    List<IHeatSource> sourceToUpdateList = new ArrayList<>();
    private final Map<BlockPos, IHeatTile> chunkCoordinatesIHeatTileMap;

    HeatNetLocal() {
        this.chunkCoordinatesIHeatTileMap = new HashMap<>();
    }


    public void addTile(IHeatTile tile1) {


        this.addTileEntity(getTileFromIHeat(tile1).getPos(), tile1);


    }

    public void addTile(IHeatTile tile, TileEntity tileentity) {

        final BlockPos coords = tileentity.getPos();
        if (this.chunkCoordinatesIHeatTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesIHeatTileMap.put(coords, tile);
        this.updateAdd(coords, tile);
        if (tile instanceof IHeatAcceptor) {
            this.onTileEntityAdded((IHeatAcceptor) tile);
        }
        if (tile instanceof IHeatSource) {
            senderPath.add(new HeatTick<>((IHeatSource) tile, null));

        }


    }

    private void updateAdd(BlockPos pos, IHeatTile tile) {
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos
                    .offset(dir);
            final IHeatTile tile1 = this.chunkCoordinatesIHeatTileMap.get(pos1);
            if (tile1 != null) {
                final EnumFacing inverseDirection2 = dir.getOpposite();
                if (tile1 instanceof IHeatEmitter && tile instanceof IHeatAcceptor) {
                    final IHeatEmitter sender2 = (IHeatEmitter) tile1;
                    final IHeatAcceptor receiver2 = (IHeatAcceptor) tile;
                    if (sender2.emitsHeatTo(receiver2, dir) && receiver2.acceptsHeatFrom(
                            sender2,
                            inverseDirection2
                    )) {
                        tile1.AddHeatTile(tile, dir.getOpposite());
                        tile.AddHeatTile(tile1, dir);
                    }
                } else if (tile1 instanceof IHeatAcceptor && tile instanceof IHeatEmitter) {
                    final IHeatEmitter sender2 = (IHeatEmitter) tile;
                    final IHeatAcceptor receiver2 = (IHeatAcceptor) tile1;
                    if (sender2.emitsHeatTo(receiver2, dir.getOpposite()) && receiver2.acceptsHeatFrom(
                            sender2,
                            inverseDirection2.getOpposite()
                    )) {
                        tile1.AddHeatTile(tile, dir.getOpposite());
                        tile.AddHeatTile(tile1, dir);
                    }
                }
            }

        }
    }

    public void addTileEntity(final BlockPos coords, final IHeatTile tile) {
        if (this.chunkCoordinatesIHeatTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesIHeatTileMap.put(coords, tile);
        this.updateAdd(coords,tile);
        if (tile instanceof IHeatAcceptor) {
            this.onTileEntityAdded((IHeatAcceptor) tile);
        }
        if (tile instanceof IHeatSource) {
            senderPath.add(new HeatTick<>((IHeatSource) tile, null));

        }
    }
    public void onTileEntityAdded(final IHeatAcceptor tile) {
        final LinkedList<IHeatTile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(tile);
        long id = WorldBaseGen.random.nextLong();
        this.sourceToUpdateList = new LinkedList<>();
        while (!tileEntitiesToCheck.isEmpty()) {
            final IHeatTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<IHeatTile>> validReceivers = this.getValidReceivers(currentTileEntity);
            for (final InfoTile<IHeatTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != tile && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof IHeatSource) {
                        this.sourceToUpdateList.add((IHeatSource) validReceiver.tileEntity);
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof IHeatConductor) {
                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }
        this.sourceToUpdateList = new ArrayList<>(sourceToUpdateList);
    }
    private void updateRemove(BlockPos pos, IHeatTile tile) {
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos
                    .offset(dir);
            final IHeatTile tile1 = this.chunkCoordinatesIHeatTileMap.get(pos1);
            if (tile1 != null) {
                tile1.RemoveHeatTile(tile, dir.getOpposite());
            }

        }
    }
    public void removeTile(IHeatTile tile1) {

        this.removeTileEntity(tile1);

    }


    public void removeTileEntity(IHeatTile tile) {
        if (!this.chunkCoordinatesIHeatTileMap.containsKey(tile.getBlockPos())) {
            return;
        }
        final BlockPos coord = tile.getBlockPos();
        this.chunkCoordinatesIHeatTileMap.remove(coord);
        if (tile instanceof IHeatAcceptor) {
            this.removeAll(this.getSources((IHeatAcceptor) tile));
        }
        if (tile instanceof IHeatSource) {
            this.remove((IHeatSource) tile);
        }
        this.updateRemove(coord, tile);
    }

    public void emitHeatFrom(final IHeatSource HeatSource, double amount, final HeatTick<IHeatSource, Path> tick) {
        List<Path> HeatPaths = tick.getList();

        if (HeatPaths == null) {
            final Tuple<List<Path>, LinkedList<IHeatConductor>> tuple = this.discover(HeatSource, tick);
            tick.setList(tuple.getFirst());
            tick.setConductors(tuple.getSecond());
            HeatPaths = tick.getList();
        }
        boolean allow = false;
        if (amount > 0) {
            for (final Path HeatPath : HeatPaths) {
                final IHeatSink HeatSink = HeatPath.target;
                double demandedHeat = HeatSink.getDemandedHeat();
                if (demandedHeat <= 0.0) {
                    continue;
                }
                double adding;


                adding = Math.min(amount, demandedHeat);
                if (adding <= 0.0D) {
                    continue;
                }
                allow = allow || HeatSink.needTemperature();

                HeatSink.receivedHeat(adding);


                if (adding > HeatPath.min) {
                    for (IHeatConductor HeatConductor : HeatPath.conductors) {
                        if (HeatConductor.getConductorBreakdownHeat() < adding) {
                            HeatConductor.removeConductor();
                        } else {
                            break;
                        }
                    }
                }


            }
        }

        if (!allow) {
            HeatSource.setAllowed(false);
        }
    }


    public TileEntity getTileFromIHeat(IHeatTile tile) {
        if (tile == null) {
            return null;
        }
        return tile.getTile();
    }

    public Tuple<List<Path>,LinkedList<IHeatConductor>> discover(final IHeatSource emitter,
                                                                 final HeatTick<IHeatSource, Path> tick) {
        final LinkedList<IHeatTile> tileEntitiesToCheck = new LinkedList<>();
        List<Path> energyPaths = new LinkedList<>();
        long id = WorldBaseGen.random.nextLong();
        emitter.setId(id);
        tileEntitiesToCheck.push(emitter);
        LinkedList<IHeatConductor> set = new LinkedList<>();
        while (!tileEntitiesToCheck.isEmpty()) {
            final IHeatTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<IHeatTile>> validReceivers = this.getValidReceivers(currentTileEntity);
            InfoCable cable = null;
            if (currentTileEntity instanceof IHeatConductor) {
                cable = ((IHeatConductor) currentTileEntity).getHeatCable();
            }
            for (final InfoTile<IHeatTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof IHeatSink) {
                        energyPaths.add(new Path((IHeatSink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof IHeatConductor) {
                        IHeatConductor conductor = (IHeatConductor) validReceiver.tileEntity;
                        conductor.setHeatCable(new InfoCable(conductor, validReceiver.direction, cable));

                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }
        energyPaths = new ArrayList<>(energyPaths);
        int id1 = WorldBaseGen.random.nextInt();
        for (Path energyPath : energyPaths) {
            IHeatTile tileEntity = energyPath.target;
            energyPath.target.getEnergyTickList().add(tick.getSource());
            EnumFacing energyBlockLink = energyPath.targetDirection;
            tileEntity = tileEntity.getHeatTiles().get(energyBlockLink);
            if (!(tileEntity instanceof IHeatConductor)) {
                continue;
            }
            InfoCable cable = ((IHeatConductor) tileEntity).getHeatCable();

            while (cable != null) {

                final IHeatConductor energyConductor = cable.getConductor();
                energyPath.conductors.add(energyConductor);
                if (energyConductor.getHashCodeSource() != id1){
                    energyConductor.setHashCodeSource(id1);
                    set.add(energyConductor);
                    if (energyConductor.getConductorBreakdownHeat() - 1 < energyPath.getMin()) {
                        energyPath.setMin(energyConductor.getConductorBreakdownHeat() - 1);
                    }
                }
                cable = cable.getPrev();
                if (cable == null) {
                    break;
                }
            }

        }
        return new Tuple<>(energyPaths,set);
    }



    public List<InfoTile<IHeatTile>> getValidReceivers(final IHeatTile emitter) {

        final BlockPos tile1;
        tile1 = emitter.getBlockPos();
        if (tile1 != null) {

            return emitter.getHeatValidReceivers();


        }


        return Collections.emptyList();
    }


    public void onTickEnd() {
        if (!sourceToUpdateList.isEmpty()) {
            for (IHeatSource source : sourceToUpdateList) {
                remove1(source);
            }
            sourceToUpdateList.clear();
        }

        try {
            for (HeatTick<IHeatSource, Path> tick : this.senderPath) {
                final IHeatSource entry = tick.getSource();
                if (entry != null) {
                    double offered = entry.getOfferedHeat();

                    if (entry.isAllowed()) {

                        this.emitHeatFrom(entry, offered, tick);


                    } else {
                        this.emitHeatFromNotAllowed(entry, offered, tick);

                    }

                }
            }
        } catch (Exception ignored) {
        }


    }

    public void emitHeatFromNotAllowed(
            final IHeatSource HeatSource,
            double amount,
            final HeatTick<IHeatSource, Path> tick
    ) {
        List<Path> HeatPaths = tick.getList();

        if (HeatPaths == null) {
            final Tuple<List<Path>, LinkedList<IHeatConductor>> tuple = this.discover(HeatSource, tick);
            tick.setList(tuple.getFirst());
            tick.setConductors(tuple.getSecond());
            HeatPaths = tick.getList();
        }

        for (final Path HeatPath : HeatPaths) {
            final IHeatSink HeatSink = HeatPath.target;
            double demandedHeat = HeatSink.getDemandedHeat();
            if (HeatSink.needTemperature()) {
                HeatSource.setAllowed(true);
            }
            if (demandedHeat <= 0.0) {
                continue;
            }

            double adding;


            adding = Math.min(amount, demandedHeat);
            if (adding <= 0.0D) {
                continue;
            }


            HeatSink.receivedHeat(adding);


            if (adding > HeatPath.min) {
                for (IHeatConductor HeatConductor : HeatPath.conductors) {
                    if (HeatConductor.getConductorBreakdownHeat() < adding) {
                        HeatConductor.removeConductor();
                    } else {
                        break;
                    }
                }
            }


        }


    }


    public IHeatTile getTileEntity(BlockPos pos) {

        return this.chunkCoordinatesIHeatTileMap.get(pos);
    }






    public void remove1(final IHeatSource par1) {

        for (HeatTick<IHeatSource, Path> ticks : this.senderPath) {
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

    public void remove(final IHeatSource par1) {
        final HeatTick<IHeatSource, Path> heatTick = this.senderPath.removeSource(par1);
        if (heatTick.getList() != null) {
            for (Path path : heatTick.getList()) {
                path.target.getEnergyTickList().remove(heatTick.getSource());
            }
        }
    }



    public void removeAll(final List<HeatTick<IHeatSource, Path>> par1) {
        if (par1 == null) {
            return;
        }
        for (HeatTick<IHeatSource,Path> iHeatSource : par1) {
            if (iHeatSource.getList() != null) {
                for (Path path : iHeatSource.getList()) {
                    path.target.getEnergyTickList().remove(iHeatSource.getSource());
                }
            }
            iHeatSource.setList(null);
        }
    }


    public List<HeatTick<IHeatSource, Path>> getSources(final IHeatAcceptor par1) {
        if (par1 instanceof IHeatSink) {
            List<HeatTick<IHeatSource, Path>> list = new LinkedList<>();
            for (HeatTick<IHeatSource, Path> energyTicks : senderPath) {
                if (((IHeatSink) par1).getEnergyTickList().contains(energyTicks.getSource())) {
                    list.add(energyTicks);
                }
            }
            return list;
        } else {
            if (par1 instanceof ICoolConductor) {
                List<HeatTick<IHeatSource, Path>> list = new LinkedList<>();
                for (HeatTick<IHeatSource, Path> energyTicks : senderPath) {
                    if (energyTicks.getConductors().contains(par1)) {
                        list.add(energyTicks);
                    }
                }
                return list;
            }
            return Collections.emptyList();
        }
    }



    public void onUnload() {
        this.senderPath.clear();
        this.chunkCoordinatesIHeatTileMap.clear();
    }




}
