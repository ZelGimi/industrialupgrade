package com.denfop.cool;

import com.denfop.api.cool.ICoolAcceptor;
import com.denfop.api.cool.ICoolConductor;
import com.denfop.api.cool.ICoolEmitter;
import com.denfop.api.cool.ICoolSink;
import com.denfop.api.cool.ICoolSource;
import com.denfop.api.cool.ICoolTile;
import com.denfop.api.energy.SystemTick;
import com.denfop.api.sytem.InfoTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CoolNetLocal {


    final List<SystemTick<ICoolSource, CoolPath>> senderPath = new ArrayList<>();
    private final Map<BlockPos, ICoolTile> chunkCoordinatesICoolTileMap;
    final List<ICoolSource> sourceToUpdateList = new ArrayList<>();
    CoolNetLocal() {
        this.chunkCoordinatesICoolTileMap = new HashMap<>();
    }


    public void addTile(ICoolTile tile1) {


        this.addTileEntity(getTileFromICool(tile1).getPos(), tile1);


    }

    public void addTile(ICoolTile tile, TileEntity tileentity) {

        final BlockPos coords = tileentity.getPos();
        if (this.chunkCoordinatesICoolTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesICoolTileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof ICoolAcceptor) {
            this.onTileEntityAdded((ICoolAcceptor) tile);
        }
        if (tile instanceof ICoolSource) {
            senderPath.add(new SystemTick<>((ICoolSource) tile, null));

        }


    }

    public BlockPos getPos(final ICoolTile tile) {
        if (tile == null) {
            return null;
        }
        return tile.getBlockPos();
    }

    public void addTileEntity(final BlockPos coords, final ICoolTile tile) {
        if (this.chunkCoordinatesICoolTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesICoolTileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof ICoolAcceptor) {
            this.onTileEntityAdded((ICoolAcceptor) tile);
        }
        if (tile instanceof ICoolSource) {
            senderPath.add(new SystemTick<>((ICoolSource) tile, null));

        }
    }

    public void removeTile(ICoolTile tile1) {

        this.removeTileEntity(tile1);

    }

    public List<CoolPath> getPaths(final ICoolAcceptor par1) {
        final List<CoolPath> paths = new ArrayList<>();

        List<SystemTick<ICoolSource,CoolPath>> sources_list = this.getSources(par1);
        if (sources_list == null || sources_list.isEmpty()) {
            return paths;
        }
        for (final SystemTick<ICoolSource,CoolPath> source : sources_list) {
            if (this.containsKey(source)) {
                paths.addAll(source.getList());
            }
        }
        return paths;
    }
    public void removeTileEntity(ICoolTile tile) {
        if (!this.chunkCoordinatesICoolTileMap.containsKey(tile.getBlockPos())) {
            return;
        }
        final BlockPos coord = tile.getBlockPos();
        this.chunkCoordinatesICoolTileMap.remove(coord, tile);
        this.update(coord);
        if (tile instanceof ICoolAcceptor) {
            this.removeAll(this.getSources((ICoolAcceptor) tile));
            this.onTileEntityRemoved((ICoolAcceptor) tile);
        }
        if (tile instanceof ICoolSource) {
            this.remove((ICoolSource) tile);
        }
    }


    public double emitCoolFrom(final ICoolSource CoolSource, double amount, final SystemTick<ICoolSource, CoolPath> tick) {
        List<CoolPath> CoolPaths = tick.getList();

        if (CoolPaths == null) {
            CoolPaths = this.discover(CoolSource);
            tick.setList(CoolPaths);
        }
        boolean transmit = false;
        if (amount > 0) {
            for (final CoolPath CoolPath : CoolPaths) {
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
                    for (ICoolConductor CoolConductor : CoolPath.conductors) {
                        if (CoolConductor.getConductorBreakdownCold() < adding) {
                            CoolConductor.removeConductor();
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
            final SystemTick<ICoolSource, CoolPath> tick
    ) {
        List<CoolPath> CoolPaths = tick.getList();

        if (CoolPaths == null) {
            CoolPaths = this.discover(CoolSource);
            tick.setList(CoolPaths);
        }

        for (final CoolPath CoolPath : CoolPaths) {
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
                for (ICoolConductor CoolConductor : CoolPath.conductors) {
                    if (CoolConductor.getConductorBreakdownCold() < adding) {
                        CoolConductor.removeConductor();
                    } else {
                        break;
                    }
                }
            }


        }


        return amount;
    }

    public TileEntity getTileFromICool(ICoolTile tile) {
        if (tile instanceof TileEntity) {
            return (TileEntity) tile;
        }
        return tile.getTile();

    }

    public List<CoolPath> discover(final ICoolSource emitter) {
        final Map<ICoolConductor, EnumFacing> reachedTileEntities = new HashMap<>();
        final List<ICoolTile> tileEntitiesToCheck = new ArrayList<>();
        final List<CoolPath> CoolPaths = new ArrayList<>();

        tileEntitiesToCheck.add(emitter);

        while (!tileEntitiesToCheck.isEmpty()) {
            final ICoolTile currentTileEntity = tileEntitiesToCheck.remove(0);
            final List<InfoTile<ICoolTile>> validReceivers = this.getValidReceivers(currentTileEntity, false);
            for (final InfoTile<ICoolTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {
                    if (validReceiver.tileEntity instanceof ICoolSink) {
                        CoolPaths.add(new CoolPath((ICoolSink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }
                    if (reachedTileEntities.containsKey((ICoolConductor) validReceiver.tileEntity)) {
                        continue;
                    }

                    reachedTileEntities.put((ICoolConductor) validReceiver.tileEntity, validReceiver.direction);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }


        }
        for (CoolPath CoolPath : CoolPaths) {
            ICoolTile tileEntity = CoolPath.target;
            EnumFacing CoolBlockLink = CoolPath.targetDirection;
            BlockPos te = tileEntity.getBlockPos();
            if (emitter != null) {
                while (tileEntity != emitter) {
                    if (CoolBlockLink != null && te != null) {
                        tileEntity = this.getTileEntity(te.offset(CoolBlockLink));
                        te = te.offset(CoolBlockLink);
                    }
                    if (!(tileEntity instanceof ICoolConductor)) {
                        break;
                    }
                    final ICoolConductor CoolConductor = (ICoolConductor) tileEntity;
                    CoolPath.conductors.add(CoolConductor);
                    if (CoolConductor.getConductorBreakdownCold() - 1 < CoolPath.getMin()) {
                        CoolPath.setMin(CoolConductor.getConductorBreakdownCold() - 1);
                    }
                    CoolBlockLink = reachedTileEntities.get(tileEntity);
                    if (CoolBlockLink != null) {
                        continue;
                    }

                }
            }
        }
        return CoolPaths;
    }

    public ICoolTile getNeighbor(final ICoolTile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        return this.getTileEntity(tile.getBlockPos().offset(dir));
    }

    private List<InfoTile<ICoolTile>> getValidReceivers(final ICoolTile emitter, final boolean reverse) {
        final List<InfoTile<ICoolTile>> validReceivers = new LinkedList<>();

        for (final EnumFacing direction : EnumFacing.values()) {
            final ICoolTile target2 = getNeighbor(emitter, direction);
            if (target2 != null) {
                final EnumFacing inverseDirection2 = direction.getOpposite();
                if (reverse) {
                    if (emitter instanceof ICoolAcceptor && target2 instanceof ICoolEmitter) {
                        final ICoolEmitter sender2 = (ICoolEmitter) target2;
                        final ICoolAcceptor receiver2 = (ICoolAcceptor) emitter;
                        if (sender2.emitsCoolTo(receiver2, inverseDirection2) && receiver2.acceptsCoolFrom(
                                sender2,
                                direction
                        )) {
                            validReceivers.add(new InfoTile<ICoolTile>(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof ICoolEmitter && target2 instanceof ICoolAcceptor) {
                    final ICoolEmitter sender2 = (ICoolEmitter) emitter;
                    final ICoolAcceptor receiver2 = (ICoolAcceptor) target2;
                    if (sender2.emitsCoolTo(receiver2, direction) && receiver2.acceptsCoolFrom(
                            sender2,
                            inverseDirection2
                    )) {
                        validReceivers.add(new InfoTile<>(target2, inverseDirection2));
                    }
                }
            }

        }
        //

        return validReceivers;
    }




    public void onTickEnd() {
        if (sourceToUpdateList.size() > 0) {
            for (ICoolSource source : sourceToUpdateList) {
                remove1(source);
            }
            sourceToUpdateList.clear();
        }
        try {
            for (SystemTick<ICoolSource, CoolPath> tick : this.senderPath) {
                final ICoolSource entry = tick.getSource();
                if (entry != null) {
                    double offered = entry.getOfferedCool();

                    if (offered > 0 && entry.isAllowed()) {
                        for (double i = 0; i < getPacketAmount(); ++i) {
                            final double removed = offered - this.emitCoolFrom(entry, offered, tick);
                            if (removed <= 0) {
                                break;
                            }
                        }
                    } else if (!entry.isAllowed()) {
                        for (double i = 0; i < getPacketAmount(); ++i) {
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

    private double getPacketAmount() {

        return 1.0D;
    }

    public ICoolTile getTileEntity(BlockPos pos) {

        return this.chunkCoordinatesICoolTileMap.get(pos);
    }


    public void update(BlockPos pos) {
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos
                    .offset(dir);
            final ICoolTile tile = this.chunkCoordinatesICoolTileMap.get(pos1);
            if (tile != null) {
                if (tile instanceof ICoolConductor) {
                    ((ICoolConductor) tile).update_render();
                }
            }

        }
    }

    public void put(final ICoolSource par1, final List<CoolPath> par2) {
        this.senderPath.add(new SystemTick<>(par1, par2));
    }


    public boolean containsKey(final SystemTick<ICoolSource, CoolPath> par1) {
        return this.senderPath.contains(par1);
    }

    public boolean containsKey(final ICoolSource par1) {
        return this.senderPath.contains(new SystemTick<ICoolSource, CoolPath>(par1, null));
    }


    public void remove1(final ICoolSource par1) {

        for (SystemTick<ICoolSource, CoolPath> ticks : this.senderPath) {
            if (ticks.getSource() == par1) {
                ticks.setList(null);
                break;
            }
        }
    }

    public void remove(final ICoolSource par1) {
        this.senderPath.remove(new SystemTick<ICoolSource, CoolPath>(par1, null));
    }

    public void remove(final SystemTick<ICoolSource, CoolPath> par1) {
        this.senderPath.remove(par1);
    }

    public void removeAll(final List<SystemTick<ICoolSource, CoolPath>> par1) {
        if (par1 == null) {
            return;
        }
        for (SystemTick<ICoolSource, CoolPath> iCoolSource : par1) {
            iCoolSource.setList(null);
        }
    }


    public List<SystemTick<ICoolSource, CoolPath>> getSources(final ICoolAcceptor par1) {
        final List<SystemTick<ICoolSource, CoolPath>> source = new ArrayList<>();
        for (final SystemTick<ICoolSource, CoolPath> entry : this.senderPath) {
            if (entry.getList() != null) {
                for (CoolPath path : entry.getList()) {
                    if ((!(par1 instanceof ICoolConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof ICoolSink) || path.target != par1)) {
                        continue;
                    }
                    source.add(entry);
                    break;
                }
            }
        }
        return source;
    }


    public SystemTick<ICoolSource, CoolPath> get(ICoolSource tileEntity) {
        for (SystemTick<ICoolSource, CoolPath> entry : this.senderPath) {
            if (entry.getSource() == tileEntity) {
                return entry;
            }
        }
        return null;
    }

    public void onUnload() {
        this.senderPath.clear();
        this.chunkCoordinatesICoolTileMap.clear();
    }

    public void onTileEntityAdded(final ICoolAcceptor tile) {
        final List<ICoolTile> tileEntitiesToCheck = new ArrayList<>();

        final List<BlockPos> blockPosList = new ArrayList<>();
        blockPosList.add(tile.getBlockPos());
        tileEntitiesToCheck.add(tile);
        while (!tileEntitiesToCheck.isEmpty()) {
            final ICoolTile currentTileEntity = tileEntitiesToCheck.remove(0);
            for (final EnumFacing direction : EnumFacing.values()) {
                final ICoolTile target2 = this.getTileEntity(currentTileEntity.getBlockPos().offset(direction));
                if (target2 != null && !blockPosList.contains(target2.getBlockPos())) {
                    blockPosList.add(target2.getBlockPos());
                    if (target2 instanceof ICoolSource) {
                        if (!sourceToUpdateList.contains((ICoolSource) target2)) {
                            sourceToUpdateList.add((ICoolSource) target2);
                        }
                        continue;
                    }
                    if (target2 instanceof ICoolConductor) {
                        tileEntitiesToCheck.add(target2);
                    }
                }
            }


        }

    }

    public void onTileEntityRemoved(final ICoolAcceptor par1) {

        this.onTileEntityAdded(par1);
    }




    public static class CoolPath {

        final List<ICoolConductor> conductors;
        final ICoolSink target;
        final EnumFacing targetDirection;
        double min = Double.MAX_VALUE;

        CoolPath(ICoolSink sink, EnumFacing facing) {
            this.target = sink;
            this.conductors = new ArrayList<>();
            this.targetDirection = facing;


        }

        public List<ICoolConductor> getConductors() {
            return conductors;
        }


        public double getMin() {
            return min;
        }

        public void setMin(final double min) {
            this.min = min;
        }


    }


}
