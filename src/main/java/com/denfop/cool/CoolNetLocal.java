package com.denfop.cool;

import com.denfop.api.cool.*;
import com.denfop.api.energy.ConductorInfo;
import com.denfop.api.sytem.InfoTile;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

public class CoolNetLocal {


    final CoolTickList<CoolTick<ICoolSource, Path>> senderPath = new CoolTickList<>();
    private final Map<BlockPos, ICoolTile> chunkCoordinatesICoolTileMap;
    List<ICoolSource> sourceToUpdateList = new ArrayList<>();

    CoolNetLocal() {
        this.chunkCoordinatesICoolTileMap = new HashMap<>();
    }


    public void addTile(ICoolTile tile1) {


        this.addTileEntity(tile1.getPos(), tile1);


    }

    public void addTile(ICoolTile tile, BlockEntity tileentity) {

        final BlockPos coords = tileentity.getBlockPos();
        if (this.chunkCoordinatesICoolTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesICoolTileMap.put(coords, tile);
        this.updateAdd(coords, tile);
        if (tile instanceof ICoolAcceptor) {
            this.onTileEntityAdded((ICoolAcceptor) tile);
        }
        if (tile instanceof ICoolSource) {
            senderPath.add(new CoolTick<>((ICoolSource) tile, null));

        }


    }

    private void updateAdd(BlockPos pos, ICoolTile tile) {
        for (final Direction dir : Direction.values()) {
            BlockPos pos1 = pos
                    .offset(dir.getNormal());
            final ICoolTile tile1 = this.chunkCoordinatesICoolTileMap.get(pos1);
            if (tile1 != null) {
                final Direction inverseDirection2 = dir.getOpposite();
                if (tile1 instanceof ICoolEmitter && tile instanceof ICoolAcceptor) {
                    final ICoolEmitter sender2 = (ICoolEmitter) tile1;
                    final ICoolAcceptor receiver2 = (ICoolAcceptor) tile;
                    if (sender2.emitsCoolTo(receiver2, dir.getOpposite()) && receiver2.acceptsCoolFrom(
                            sender2,
                            inverseDirection2.getOpposite()
                    )) {
                        tile1.AddCoolTile(tile, dir.getOpposite());
                        tile.AddCoolTile(tile1, dir);
                    }
                } else if (tile1 instanceof ICoolAcceptor && tile instanceof ICoolEmitter) {
                    final ICoolEmitter sender2 = (ICoolEmitter) tile;
                    final ICoolAcceptor receiver2 = (ICoolAcceptor) tile1;
                    if (sender2.emitsCoolTo(receiver2, dir) && receiver2.acceptsCoolFrom(
                            sender2,
                            inverseDirection2
                    )) {
                        tile1.AddCoolTile(tile, dir.getOpposite());
                        tile.AddCoolTile(tile1, dir);
                    }
                }
            }

        }
    }

    public void onTileEntityAdded(final ICoolAcceptor tile) {
        final LinkedList<ICoolTile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(tile);
        long id = WorldBaseGen.random.nextLong();
        this.sourceToUpdateList = new LinkedList<>();
        while (!tileEntitiesToCheck.isEmpty()) {
            final ICoolTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<ICoolTile>> validReceivers = this.getValidReceivers(currentTileEntity);
            for (final InfoTile<ICoolTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != tile && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof ICoolSource) {
                        this.sourceToUpdateList.add((ICoolSource) validReceiver.tileEntity);
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof ICoolConductor) {
                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }
        this.sourceToUpdateList = new ArrayList<>(sourceToUpdateList);
    }


    public void addTileEntity(final BlockPos coords, final ICoolTile tile) {
        if (this.chunkCoordinatesICoolTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesICoolTileMap.put(coords, tile);
        this.updateAdd(coords, tile);
        if (tile instanceof ICoolAcceptor) {
            this.onTileEntityAdded((ICoolAcceptor) tile);
        }
        if (tile instanceof ICoolSource) {
            senderPath.add(new CoolTick<>((ICoolSource) tile, null));

        }
    }

    public void removeTile(ICoolTile tile1) {

        this.removeTileEntity(tile1);

    }


    private void updateRemove(BlockPos pos, ICoolTile tile) {
        for (final Direction dir : Direction.values()) {
            BlockPos pos1 = pos
                    .offset(dir.getNormal());
            final ICoolTile tile1 = this.chunkCoordinatesICoolTileMap.get(pos1);
            if (tile1 != null) {
                tile1.RemoveCoolTile(tile, dir.getOpposite());
            }

        }
    }

    public void removeTileEntity(ICoolTile tile) {
        if (!this.chunkCoordinatesICoolTileMap.containsKey(tile.getPos())) {
            return;
        }
        final BlockPos coord = tile.getPos();
        this.chunkCoordinatesICoolTileMap.remove(coord);
        if (tile instanceof ICoolAcceptor) {
            this.removeAll(this.getSources((ICoolAcceptor) tile));
        }
        if (tile instanceof ICoolSource) {
            this.remove((ICoolSource) tile);
        }
        this.updateRemove(coord, tile);
    }


    public double emitCoolFrom(final ICoolSource CoolSource, double amount, final CoolTick<ICoolSource, Path> tick) {
        List<Path> CoolPaths = tick.getList();

        if (CoolPaths == null) {
            final Tuple<List<Path>, LinkedList<ICoolConductor>> tuple = this.discover(CoolSource, tick);
            tick.setList(tuple.getA());
            tick.setConductors(tuple.getB());
            CoolPaths = tick.getList();
        }
        boolean transmit = false;
        if (amount > 0) {
            for (final Path CoolPath : CoolPaths) {
                final ICoolSink CoolSink = CoolPath.target;
                double demandedCool = CoolSink.getDemandedCool();
                if (demandedCool <= 0.0) {
                    continue;
                }
                double adding;


                adding = Math.min(amount, demandedCool);
                if (adding <= 0.0D) {
                    continue;
                }


                CoolSink.receivedCold(adding);
                transmit = true;

                if (adding > CoolPath.min) {
                    for (ConductorInfo CoolConductor : CoolPath.conductors) {
                        if (CoolConductor.getBreakdownEnergy() < adding) {
                            ((ICoolConductor)this.getTileEntity(CoolConductor.getPos())).removeConductor();
                        } else {
                            break;
                        }
                    }
                }


            }
        }
        if (!transmit && CoolSource.isAllowed()) {
            CoolSource.setAllowed(false);
        }

        return amount;
    }

    public double emitCoolFromNotAllowed(
            final ICoolSource CoolSource, double amount,
            final CoolTick<ICoolSource, Path> tick
    ) {
        List<Path> CoolPaths = tick.getList();

        if (CoolPaths == null) {
            final Tuple<List<Path>, LinkedList<ICoolConductor>> tuple = this.discover(CoolSource, tick);
            tick.setList(tuple.getA());
            tick.setConductors(tuple.getB());
            CoolPaths = tick.getList();
        }

        for (final Path CoolPath : CoolPaths) {
            final ICoolSink CoolSink = CoolPath.target;
            double demandedCool = CoolSink.getDemandedCool();
            if (demandedCool <= 0.0) {
                continue;
            }
            if (CoolSink.needCooling()) {
                CoolSource.setAllowed(true);
            }
            double adding;


            adding = Math.min(amount, demandedCool);
            if (adding <= 0.0D) {
                continue;
            }
            CoolSink.receivedCold(adding);
            if (adding > CoolPath.min) {
                for (ConductorInfo CoolConductor : CoolPath.conductors) {
                    if (CoolConductor.getBreakdownEnergy() < adding) {
                        ((ICoolConductor)this.getTileEntity(CoolConductor.getPos())).removeConductor();
                    }
                }
            }


        }


        return amount;
    }

    public BlockEntity getTileFromICool(ICoolTile tile) {
        if (tile instanceof BlockEntity) {
            return (BlockEntity) tile;
        }
        return tile.getTile();

    }

    public Tuple<List<Path>, LinkedList<ICoolConductor>> discover(
            final ICoolSource emitter,
            final CoolTick<ICoolSource, Path> tick
    ) {
        final LinkedList<ICoolTile> tileEntitiesToCheck = new LinkedList<>();
        List<Path> energyPaths = new LinkedList<>();
        long id = WorldBaseGen.random.nextLong();
        emitter.setId(id);
        tileEntitiesToCheck.push(emitter);
        LinkedList<ICoolConductor> set = new LinkedList<>();
        while (!tileEntitiesToCheck.isEmpty()) {
            final ICoolTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<ICoolTile>> validReceivers = this.getValidReceivers(currentTileEntity);
            InfoCable cable = null;
            if (currentTileEntity instanceof ICoolConductor) {
                cable = ((ICoolConductor) currentTileEntity).getCoolCable();
            }
            for (final InfoTile<ICoolTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof ICoolSink) {
                        energyPaths.add(new Path((ICoolSink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof ICoolConductor conductor) {
                        conductor.setCoolCable(new InfoCable(conductor, validReceiver.direction, cable));

                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }
        int id1 = WorldBaseGen.random.nextInt();
        energyPaths = new ArrayList<>(energyPaths);
        for (Path energyPath : energyPaths) {
            ICoolTile tileEntity = energyPath.target;
            energyPath.target.getEnergyTickList().add(tick.getSource());
            Direction energyBlockLink = energyPath.targetDirection;
            tileEntity = tileEntity.getCoolTiles().get(energyBlockLink);
            if (!(tileEntity instanceof ICoolConductor)) {
                continue;
            }
            InfoCable cable = ((ICoolConductor) tileEntity).getCoolCable();

            while (cable != null) {

                final ICoolConductor energyConductor = cable.getConductor();
                energyPath.conductors.add(energyConductor.getCoolConductor());
                if (energyConductor.getHashCodeSource() != id1) {
                    energyConductor.setHashCodeSource(id1);
                    set.add(energyConductor);
                    if (energyConductor.getConductorBreakdownCold() - 1 < energyPath.getMin()) {
                        energyPath.setMin(energyConductor.getConductorBreakdownCold() - 1);
                    }
                }
                cable = cable.getPrev();
                if (cable == null) {
                    break;
                }
            }

        }
        return new Tuple<>(energyPaths, set);
    }

    public List<InfoTile<ICoolTile>> getValidReceivers(final ICoolTile emitter) {

        final BlockPos tile1;
        tile1 = emitter.getPos();
        if (tile1 != null) {

            return emitter.getCoolValidReceivers();


        }


        return Collections.emptyList();
    }


    public void onTickEnd() {
        if (!sourceToUpdateList.isEmpty()) {
            for (ICoolSource source : sourceToUpdateList) {
                remove1(source);
            }
            sourceToUpdateList.clear();
        }
        try {
            for (CoolTick<ICoolSource, Path> tick : this.senderPath) {
                final ICoolSource entry = tick.getSource();
                if (entry != null) {
                    double offered = entry.getOfferedCool();

                    if (offered > 0 && entry.isAllowed()) {
                        for (double i = 0; i < 1; ++i) {
                            final double removed = offered - this.emitCoolFrom(entry, offered, tick);
                            if (removed <= 0) {
                                break;
                            }
                        }
                    } else if (!entry.isAllowed()) {
                        for (double i = 0; i < 1; ++i) {
                            final double removed = offered - this.emitCoolFromNotAllowed(entry, offered, tick);
                            if (removed <= 0) {
                                break;
                            }
                        }
                    }

                }
            }
        } catch (Exception ignored) {
        }


    }


    public ICoolTile getTileEntity(BlockPos pos) {

        return this.chunkCoordinatesICoolTileMap.get(pos);
    }


    public void remove1(final ICoolSource par1) {

        for (CoolTick<ICoolSource, Path> ticks : this.senderPath) {
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

    public void remove(final ICoolSource par1) {
        final CoolTick<ICoolSource, Path> coolTick = this.senderPath.removeSource(par1);
        if (coolTick != null)
            if (coolTick.getList() != null) {
                for (Path path : coolTick.getList()) {
                    path.target.getEnergyTickList().remove(coolTick.getSource());
                }
            }
    }


    public void removeAll(final List<CoolTick<ICoolSource, Path>> par1) {
        if (par1 == null) {
            return;
        }

        for (CoolTick<ICoolSource, Path> IEnergySource : par1) {
            if (IEnergySource.getList() != null) {
                for (Path path : IEnergySource.getList()) {
                    path.target.getEnergyTickList().remove(IEnergySource.getSource());
                }
            }
            IEnergySource.setList(null);
        }
    }


    public List<CoolTick<ICoolSource, Path>> getSources(final ICoolAcceptor par1) {
        if (par1 instanceof ICoolSink) {
            List<CoolTick<ICoolSource, Path>> list = new LinkedList<>();
            for (CoolTick<ICoolSource, Path> energyTicks : senderPath) {
                if (((ICoolSink) par1).getEnergyTickList().contains(energyTicks.getSource())) {
                    list.add(energyTicks);
                }
            }
            return list;
        } else {
            if (par1 instanceof ICoolConductor) {
                List<CoolTick<ICoolSource, Path>> list = new LinkedList<>();
                for (CoolTick<ICoolSource, Path> energyTicks : senderPath) {
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
        this.chunkCoordinatesICoolTileMap.clear();
    }


}
