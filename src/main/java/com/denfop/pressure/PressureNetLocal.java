package com.denfop.pressure;

import com.denfop.api.energy.SystemTick;
import com.denfop.api.pressure.IPressureAcceptor;
import com.denfop.api.pressure.IPressureConductor;
import com.denfop.api.pressure.IPressureEmitter;
import com.denfop.api.pressure.IPressureSink;
import com.denfop.api.pressure.IPressureSource;
import com.denfop.api.pressure.IPressureTile;
import com.denfop.api.sytem.InfoTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PressureNetLocal {


    final List<SystemTick<IPressureSource, PressurePath>> senderPath = new ArrayList<>();
    final List<IPressureSource> sourceToUpdateList = new ArrayList<>();
    private final Map<BlockPos, IPressureTile> chunkCoordinatesIPressureTileMap;

    PressureNetLocal() {
        this.chunkCoordinatesIPressureTileMap = new HashMap<>();
    }


    public void addTile(IPressureTile tile1) {


        this.addTileEntity(getTileFromIPressure(tile1).getPos(), tile1);


    }

    public void addTile(IPressureTile tile, TileEntity tileentity) {

        final BlockPos coords = tileentity.getPos();
        if (this.chunkCoordinatesIPressureTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesIPressureTileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof IPressureAcceptor) {
            this.onTileEntityAdded((IPressureAcceptor) tile);
        }
        if (tile instanceof IPressureSource) {
            senderPath.add(new SystemTick<>((IPressureSource) tile, null));

        }


    }

    public List<PressurePath> getPaths(final IPressureAcceptor par1) {
        final List<PressurePath> paths = new ArrayList<>();

        List<SystemTick<IPressureSource, PressurePath>> sources_list = this.getSources(par1);
        if (sources_list == null || sources_list.isEmpty()) {
            return paths;
        }
        for (final SystemTick<IPressureSource, PressurePath> source : sources_list) {
            if (this.containsKey(source)) {
                paths.addAll(source.getList());
            }
        }
        return paths;
    }

    public BlockPos getPos(final IPressureTile tile) {
        if (tile == null) {
            return null;
        }
        return tile.getBlockPos();
    }

    public void addTileEntity(final BlockPos coords, final IPressureTile tile) {
        if (this.chunkCoordinatesIPressureTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesIPressureTileMap.put(coords, tile);
        this.update(coords);
        if (tile instanceof IPressureAcceptor) {
            this.onTileEntityAdded((IPressureAcceptor) tile);
        }
        if (tile instanceof IPressureSource) {
            senderPath.add(new SystemTick<>((IPressureSource) tile, null));

        }
    }

    public void removeTile(IPressureTile tile1) {

        this.removeTileEntity(tile1);

    }


    public void removeTileEntity(IPressureTile tile) {
        if (!this.chunkCoordinatesIPressureTileMap.containsKey(tile.getBlockPos())) {
            return;
        }
        final BlockPos coord = tile.getBlockPos();
        this.chunkCoordinatesIPressureTileMap.remove(coord, tile);
        this.update(coord);
        if (tile instanceof IPressureAcceptor) {
            this.removeAll(this.getSources((IPressureAcceptor) tile));
            this.onTileEntityRemoved((IPressureAcceptor) tile);
        }
        if (tile instanceof IPressureSource) {
            this.remove((IPressureSource) tile);
        }
    }

    public void emitPressureFrom(
            final IPressureSource PressureSource,
            double amount,
            final SystemTick<IPressureSource, PressurePath> tick
    ) {
        List<PressurePath> PressurePaths = tick.getList();

        if (PressurePaths == null) {
            PressurePaths = this.discover(PressureSource);
            tick.setList(PressurePaths);
        }
        boolean allow = false;
        if (amount > 0) {
            for (final PressurePath PressurePath : PressurePaths) {
                final IPressureSink PressureSink = PressurePath.target;
                double demandedPressure = PressureSink.getDemandedPressure();
                if (demandedPressure <= 0.0) {
                    continue;
                }
                if (demandedPressure != amount) {
                    continue;
                }
                double adding;


                adding = Math.min(amount, demandedPressure);
                if (adding <= 0.0D) {
                    continue;
                }
                allow = allow || PressureSink.needTemperature();

                PressureSink.receivedPressure(adding);


                if (adding > PressurePath.min) {
                    for (IPressureConductor PressureConductor : PressurePath.conductors) {
                        if (Integer.MAX_VALUE < adding) {
                            PressureConductor.removeConductor();
                        } else {
                            break;
                        }
                    }
                }


            }
        }

        if (!allow) {
            PressureSource.setAllowed(false);
        }
    }


    public TileEntity getTileFromIPressure(IPressureTile tile) {
        if (tile == null) {
            return null;
        }
        return tile.getTile();
    }

    public List<PressurePath> discover(final IPressureSource emitter) {
        final Map<IPressureConductor, EnumFacing> reachedTileEntities = new HashMap<>();
        final List<IPressureTile> tileEntitiesToCheck = new ArrayList<>();
        final List<PressurePath> PressurePaths = new ArrayList<>();

        tileEntitiesToCheck.add(emitter);

        while (!tileEntitiesToCheck.isEmpty()) {
            final IPressureTile currentTileEntity = tileEntitiesToCheck.remove(0);
            final List<InfoTile<IPressureTile>> validReceivers = this.getValidReceivers(currentTileEntity, false);
            for (final InfoTile<IPressureTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != emitter) {
                    if (validReceiver.tileEntity instanceof IPressureSink) {
                        PressurePaths.add(new PressurePath((IPressureSink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }
                    if (reachedTileEntities.containsKey((IPressureConductor) validReceiver.tileEntity)) {
                        continue;
                    }

                    reachedTileEntities.put((IPressureConductor) validReceiver.tileEntity, validReceiver.direction);
                    tileEntitiesToCheck.add(validReceiver.tileEntity);
                }
            }


        }
        for (PressurePath PressurePath : PressurePaths) {
            IPressureTile tileEntity = PressurePath.target;
            EnumFacing PressureBlockLink = PressurePath.targetDirection;
            BlockPos te = tileEntity.getBlockPos();
            if (emitter != null) {
                while (tileEntity != emitter) {
                    if (PressureBlockLink != null && te != null) {
                        tileEntity = this.getTileEntity(te.offset(PressureBlockLink));
                        te = te.offset(PressureBlockLink);
                    }
                    if (!(tileEntity instanceof IPressureConductor)) {
                        break;
                    }
                    final IPressureConductor PressureConductor = (IPressureConductor) tileEntity;
                    PressurePath.conductors.add(PressureConductor);
                    PressurePath.setMin(Integer.MAX_VALUE);
                    PressureBlockLink = reachedTileEntities.get(tileEntity);
                    if (PressureBlockLink != null) {
                        continue;
                    }

                }
            }
        }
        return PressurePaths;
    }

    public IPressureTile getNeighbor(final IPressureTile tile, final EnumFacing dir) {
        if (tile == null) {
            return null;
        }
        return this.getTileEntity(tile.getBlockPos().offset(dir));
    }

    private List<InfoTile<IPressureTile>> getValidReceivers(final IPressureTile emitter, final boolean reverse) {
        final List<InfoTile<IPressureTile>> validReceivers = new LinkedList<>();

        for (final EnumFacing direction : EnumFacing.values()) {
            final IPressureTile target2 = getNeighbor(emitter, direction);
            if (target2 != null) {
                final EnumFacing inverseDirection2 = direction.getOpposite();
                if (reverse) {
                    if (emitter instanceof IPressureAcceptor && target2 instanceof IPressureEmitter) {
                        final IPressureEmitter sender2 = (IPressureEmitter) target2;
                        final IPressureAcceptor receiver2 = (IPressureAcceptor) emitter;
                        if (sender2.emitsPressureTo(receiver2, inverseDirection2) && receiver2.acceptsPressureFrom(
                                sender2,
                                direction
                        )) {
                            validReceivers.add(new InfoTile<IPressureTile>(target2, inverseDirection2));
                        }
                    }
                } else if (emitter instanceof IPressureEmitter && target2 instanceof IPressureAcceptor) {
                    final IPressureEmitter sender2 = (IPressureEmitter) emitter;
                    final IPressureAcceptor receiver2 = (IPressureAcceptor) target2;
                    if (sender2.emitsPressureTo(receiver2, direction) && receiver2.acceptsPressureFrom(
                            sender2,
                            inverseDirection2
                    )) {
                        validReceivers.add(new InfoTile<IPressureTile>(target2, inverseDirection2));
                    }
                }
            }

        }
        //

        return validReceivers;
    }


    public void onTickEnd() {
        if (!sourceToUpdateList.isEmpty()) {
            for (IPressureSource source : sourceToUpdateList) {
                remove1(source);
            }
            sourceToUpdateList.clear();
        }

        try {
            for (SystemTick<IPressureSource, PressurePath> tick : this.senderPath) {
                final IPressureSource entry = tick.getSource();
                if (entry != null) {
                    double offered = entry.getOfferedPressure();

                    this.emitPressureFrom(entry, offered, tick);

                }
            }
        } catch (Exception ignored) {
        }


    }

    public void emitPressureFromNotAllowed(
            final IPressureSource PressureSource,
            double amount,
            final SystemTick<IPressureSource, PressurePath> tick
    ) {
        List<PressurePath> PressurePaths = tick.getList();

        if (PressurePaths == null) {
            PressurePaths = this.discover(PressureSource);
            tick.setList(PressurePaths);
        }

        for (final PressurePath PressurePath : PressurePaths) {
            final IPressureSink PressureSink = PressurePath.target;
            double demandedPressure = PressureSink.getDemandedPressure();
            if (PressureSink.needTemperature()) {
                PressureSource.setAllowed(true);
            }
            if (demandedPressure <= 0.0) {
                continue;
            }
            if (demandedPressure != amount) {
                continue;
            }
            double adding;


            adding = Math.min(amount, demandedPressure);
            if (adding <= 0.0D) {
                continue;
            }


            PressureSink.receivedPressure(adding);


            if (adding > PressurePath.min) {
                for (IPressureConductor PressureConductor : PressurePath.conductors) {
                    if (Integer.MAX_VALUE < adding) {
                        PressureConductor.removeConductor();
                    } else {
                        break;
                    }
                }
            }


        }


    }


    public IPressureTile getTileEntity(BlockPos pos) {

        return this.chunkCoordinatesIPressureTileMap.get(pos);
    }


    public void update(BlockPos pos) {
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos
                    .offset(dir);
            final IPressureTile tile = this.chunkCoordinatesIPressureTileMap.get(pos1);
            if (tile != null) {
                if (tile instanceof IPressureConductor) {
                    ((IPressureConductor) tile).update_render();
                }
            }

        }
    }

    public void put(final IPressureSource par1, final List<PressurePath> par2) {
        this.senderPath.add(new SystemTick<>(par1, par2));
    }


    public boolean containsKey(final SystemTick<IPressureSource, PressurePath> par1) {
        return this.senderPath.contains(par1);
    }

    public boolean containsKey(final IPressureSource par1) {
        return this.senderPath.contains(new SystemTick<IPressureSource, PressurePath>(par1, null));
    }


    public void remove1(final IPressureSource par1) {

        for (SystemTick<IPressureSource, PressurePath> ticks : this.senderPath) {
            if (ticks.getSource() == par1) {
                ticks.setList(null);
                break;
            }
        }
    }

    public void remove(final IPressureSource par1) {
        this.senderPath.remove(new SystemTick<IPressureSource, PressurePath>(par1, null));
    }

    public void remove(final SystemTick<IPressureSource, PressurePath> par1) {
        this.senderPath.remove(par1);
    }

    public void removeAll(final List<SystemTick<IPressureSource, PressurePath>> par1) {
        if (par1 == null) {
            return;
        }
        for (SystemTick<IPressureSource, PressurePath> iPressureSource : par1) {
            iPressureSource.setList(null);
        }
    }


    public List<SystemTick<IPressureSource, PressurePath>> getSources(final IPressureAcceptor par1) {
        final List<SystemTick<IPressureSource, PressurePath>> source = new ArrayList<>();
        for (final SystemTick<IPressureSource, PressurePath> entry : this.senderPath) {
            if (entry.getList() != null) {
                for (PressurePath path : entry.getList()) {
                    if ((!(par1 instanceof IPressureConductor) || !path.conductors.contains(par1)) && (!(par1 instanceof IPressureSink) || path.target != par1)) {
                        continue;
                    }
                    source.add(entry);
                    break;
                }
            }
        }
        return source;
    }


    public SystemTick<IPressureSource, PressurePath> get(IPressureSource tileEntity) {
        for (SystemTick<IPressureSource, PressurePath> entry : this.senderPath) {
            if (entry.getSource() == tileEntity) {
                return entry;
            }
        }
        return null;
    }

    public void onUnload() {
        this.senderPath.clear();
        this.chunkCoordinatesIPressureTileMap.clear();
    }

    public void onTileEntityAdded(final IPressureAcceptor tile) {
        final List<IPressureTile> tileEntitiesToCheck = new ArrayList<>();

        final List<BlockPos> blockPosList = new ArrayList<>();
        blockPosList.add(tile.getBlockPos());
        tileEntitiesToCheck.add(tile);
        while (!tileEntitiesToCheck.isEmpty()) {
            final IPressureTile currentTileEntity = tileEntitiesToCheck.remove(0);
            for (final EnumFacing direction : EnumFacing.values()) {
                final IPressureTile target2 = this.getTileEntity(currentTileEntity.getBlockPos().offset(direction));
                if (target2 != null && !blockPosList.contains(target2.getBlockPos())) {
                    blockPosList.add(target2.getBlockPos());
                    if (target2 instanceof IPressureSource) {
                        if (!sourceToUpdateList.contains((IPressureSource) target2)) {
                            sourceToUpdateList.add((IPressureSource) target2);
                        }
                        continue;
                    }
                    if (target2 instanceof IPressureConductor) {
                        tileEntitiesToCheck.add(target2);
                    }
                }
            }


        }

    }

    public void onTileEntityRemoved(final IPressureAcceptor par1) {

        this.onTileEntityAdded(par1);
    }


    public static class PressurePath {

        final List<IPressureConductor> conductors;
        final IPressureSink target;
        final EnumFacing targetDirection;
        double min = Double.MAX_VALUE;

        PressurePath(IPressureSink sink, EnumFacing facing) {
            this.target = sink;
            this.conductors = new ArrayList<>();
            this.targetDirection = facing;


        }

        public List<IPressureConductor> getConductors() {
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
