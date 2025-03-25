package com.denfop.api.transport;


import com.denfop.api.sytem.InfoTile;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransportNetLocal {

    final TransportTickList<TransportTick<ITransportSource, Path>> senderPath = new TransportTickList<>();
    List<ITransportSource> sourceToUpdateList = new LinkedList<>();
    private final World world;
    private final Map<BlockPos, ITransportTile> chunkCoordinatesITransportTileMap;
    byte tick;

    TransportNetLocal(World world) {
        this.world = world;
        this.chunkCoordinatesITransportTileMap = new HashMap<>();
    }

    public void remove1(final ITransportSource par1) {

        for (TransportTick<ITransportSource, Path> ticks : this.senderPath) {
            if (ticks.getSource() == par1) {
                if (ticks.getList() != null) {
                    for (Path path : ticks.getList()) {
                        path.target.getEnergyTickList().remove((Integer) ticks.getSource().hashCode());
                    }
                }
                ticks.setItemList(null);
                ticks.setFluidList(null);
                break;
            }
        }

    }

    public void remove(final ITransportSource par1) {
        final TransportTick<ITransportSource, Path> energyTick = this.senderPath.removeSource(par1);
        if (energyTick.getList() != null) {
            for (Path path : energyTick.getList()) {
                path.target.getEnergyTickList().remove((Integer) energyTick.getSource().hashCode());
            }
        }
        energyTick.setFluidList(null);
        energyTick.setItemList(null);
    }

    public void removeAll(final List<TransportTick<ITransportSource, Path>> par1) {
        if (par1 == null) {
            return;
        }

        for (TransportTick<ITransportSource, Path> IEnergySource : par1) {
            if (IEnergySource.getList() != null) {
                for (Path path : IEnergySource.getList()) {
                    path.target.getEnergyTickList().remove((Integer) IEnergySource.getSource().hashCode());
                }
            }
            IEnergySource.setFluidList(null);
            IEnergySource.setItemList(null);
        }
    }

    public boolean hasInSystem(ITransportAcceptor par1) {
        for (TransportTick<ITransportSource, Path> entry : this.senderPath) {
            if (entry.getEnergyItemPaths() != null) {
                for (Path path : entry.getEnergyItemPaths()) {
                    if (path.first.getBlockPos().equals(par1.getBlockPos()) || path.end
                            .getBlockPos()
                            .equals(par1.getBlockPos())) {
                        return true;
                    }
                }
            } else if (entry.getEnergyFluidPaths() != null) {
                for (Path path : entry.getEnergyFluidPaths()) {
                    if (path.first.getBlockPos().equals(par1.getBlockPos()) || path.end
                            .getBlockPos()
                            .equals(par1.getBlockPos())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<TransportTick<ITransportSource, Path>> getSources(final ITransportAcceptor par1) {
        if (par1 instanceof ITransportSink) {
            List<TransportTick<ITransportSource, Path>> list = new LinkedList<>();
            for (TransportTick<ITransportSource, Path> energyTicks : senderPath) {
                if (((ITransportSink) par1).getEnergyTickList().contains(energyTicks.getSource().hashCode())) {
                    list.add(energyTicks);
                }
            }
            return list;
        } else {
            if (par1 instanceof ITransportConductor) {
                List<TransportTick<ITransportSource, Path>> list = new LinkedList<>();
                for (TransportTick<ITransportSource, Path> energyTicks : senderPath) {
                    if (energyTicks.getConductors().contains(par1)) {
                        list.add(energyTicks);
                    }
                }
                return new ArrayList<>(list);
            }
            return Collections.emptyList();
        }
    }

    public void addTile(ITransportTile tile1) {
        addTileEntity(tile1.getBlockPos(), tile1);
    }

    public boolean containsKey(final TransportTick<ITransportSource, Path> par1) {
        return this.senderPath.contains(par1);
    }

    public void addTileEntity(BlockPos coords, ITransportTile tile) {
        if (this.chunkCoordinatesITransportTileMap.containsKey(coords)) {
            return;
        }
        this.chunkCoordinatesITransportTileMap.put(coords, tile);
        this.updateAdd(coords, tile);
        if (tile instanceof ITransportAcceptor) {
            this.onTileEntityAdded((ITransportAcceptor) tile);
        }
        if (tile instanceof ITransportSource) {

            this.senderPath.add(new TransportTick(tile, null));

        }
    }

    private void updateAdd(BlockPos pos, ITransportTile tile) {
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos
                    .offset(dir);
            final ITransportTile tile1 = this.chunkCoordinatesITransportTileMap.get(pos1);
            if (tile1 != null) {
                final EnumFacing inverseDirection2 = dir.getOpposite();
                if (tile1 instanceof ITransportEmitter && tile instanceof ITransportAcceptor) {
                    final ITransportEmitter sender2 = (ITransportEmitter) tile1;
                    final ITransportAcceptor receiver2 = (ITransportAcceptor) tile;
                    if (sender2.emitsTo(receiver2, dir.getOpposite()) && receiver2.acceptsFrom(
                            sender2,
                            inverseDirection2.getOpposite()
                    )) {
                        tile1.AddTile(tile, dir.getOpposite());
                        tile.AddTile(tile1, dir);
                    }
                } else if (tile1 instanceof ITransportAcceptor && tile instanceof ITransportEmitter) {
                    final ITransportEmitter sender2 = (ITransportEmitter) tile;
                    final ITransportAcceptor receiver2 = (ITransportAcceptor) tile1;
                    if (sender2.emitsTo(receiver2, dir) && receiver2.acceptsFrom(
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

    public void onTileEntityAdded(final ITransportAcceptor tile) {
        final LinkedList<ITransportTile> tileEntitiesToCheck = new LinkedList<>();
        tileEntitiesToCheck.add(tile);
        long id = WorldBaseGen.random.nextLong();
        this.sourceToUpdateList = new LinkedList<>();
        while (!tileEntitiesToCheck.isEmpty()) {
            final ITransportTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<ITransportTile>> validReceivers = currentTileEntity.getValidReceivers();
            for (final InfoTile<ITransportTile> validReceiver : validReceivers) {
                if (validReceiver.tileEntity != tile && validReceiver.tileEntity.getIdNetwork() != id) {
                    validReceiver.tileEntity.setId(id);
                    if (validReceiver.tileEntity instanceof ITransportSource) {
                        this.sourceToUpdateList.add((ITransportSource) validReceiver.tileEntity);
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof ITransportConductor) {
                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }
        this.sourceToUpdateList = new ArrayList<>(sourceToUpdateList);
    }

    public void removeTile(ITransportTile tile1) {
        removeTileEntity(tile1);
    }

    public void removeTileEntity(ITransportTile tile) {
        if (!this.chunkCoordinatesITransportTileMap.containsKey(tile.getBlockPos())) {
            return;
        }
        this.chunkCoordinatesITransportTileMap.remove(tile.getBlockPos());
        if (tile instanceof ITransportAcceptor) {
            this.removeAll(this.getSources((ITransportAcceptor) tile));
        }
        if (tile instanceof ITransportSource) {
            this.remove((ITransportSource) tile);
        }
        this.updateRemove(tile.getBlockPos(), tile);
    }

    private void updateRemove(BlockPos pos, ITransportTile tile) {
        for (final EnumFacing dir : EnumFacing.values()) {
            BlockPos pos1 = pos
                    .offset(dir);
            final ITransportTile tile1 = this.chunkCoordinatesITransportTileMap.get(pos1);
            if (tile1 != null) {
                tile1.RemoveTile(tile, dir.getOpposite());
            }

        }
    }

    public boolean canInsertOrExtract(ITransportConductor transportConductor, ItemStack stack, EnumFacing facing) {
        List<ItemStack> BlackItemStacks = transportConductor.getBlackListItems(facing);
        if (BlackItemStacks.isEmpty()) {
            List<ItemStack> WhiteItemStacks = transportConductor.getWhiteListItems(facing);
            if (!WhiteItemStacks.isEmpty()) {
                for (ItemStack stack1 : WhiteItemStacks) {
                    if (stack1.isItemEqual(stack)) {
                        return true;
                    }

                }
                return false;
            }
            return true;
        } else {
            for (ItemStack stack1 : BlackItemStacks) {
                if (stack1.isItemEqual(stack)) {
                    return false;
                }

            }
        }
        return true;
    }

    public boolean canInsertOrExtract(ITransportConductor transportConductor, FluidStack stack, EnumFacing facing) {
        List<FluidStack> BlackItemStacks = transportConductor.getBlackListFluids(facing);
        if (BlackItemStacks.isEmpty()) {
            List<FluidStack> WhiteItemStacks = transportConductor.getWhiteListFluids(facing);
            if (!WhiteItemStacks.isEmpty()) {
                for (FluidStack stack1 : WhiteItemStacks) {
                    if (stack1.isFluidEqual(stack)) {
                        return true;
                    }

                }
                return false;
            }
            return true;
        } else {
            for (FluidStack stack1 : BlackItemStacks) {
                if (stack1.isFluidEqual(stack)) {
                    return false;
                }

            }
        }
        return true;
    }

    public void emitTransportFrom(
            ITransportSource<ItemStack, IItemHandler> transportSource,
            List<Path> transportPaths
    ) {

        for (Path path : transportPaths) {
            TransportItem<ItemStack> amount = transportSource.getOffered(0, path.firstSide);
            List<ItemStack> items = amount.getList();
            List<Integer> indices = amount.getList1();

            if (items.isEmpty()) {
                continue;
            }
            if (path.end.getMax(tick) == 0) {
                continue;
            }

            if (path.first.getMax(tick) == 0) {
                continue;
            }
            ITransportSink<ItemStack, IItemHandler> transportSink = path.target;
            List<Integer> demandedSlots = transportSink.getDemanded(path.targetDirection);

            if (demandedSlots.isEmpty()) {
                continue;
            }
            if (!path.first.canWork() || !path.end.canWork()) {
                continue;
            }
            for (Integer slot : demandedSlots) {
                for (int i = 0; i < indices.size(); i++) {
                    ItemStack currentItem = items.get(i);
                    if (currentItem.isEmpty()) {
                        continue;
                    }

                    if (!canInsertOrExtract(path.first, currentItem, path.firstSide.getOpposite()) || !canInsertOrExtract(path.end,
                            currentItem, path.targetDirection.getOpposite()
                    )) {
                        continue;
                    }


                    ItemStack remainingStack = path.getHandler().insertItem(slot, currentItem, true);

                    if (remainingStack.isEmpty() || remainingStack.getCount() != currentItem.getCount()) {
                        ItemStack stack = currentItem.copy();
                        final int count = Math.min(Math.min(stack.getCount(), path.end.getMax(tick)), path.first.getMax(tick));
                        stack.setCount(count);
                        remainingStack = path.getHandler().insertItem(slot, stack, false);

                        int transferredAmount = Math.min(count, currentItem.getCount()) - remainingStack.getCount();
                        path.end.setMax(transferredAmount);
                        path.first.setMax(transferredAmount);
                        if (transferredAmount > 0) {
                            ItemStack drawnStack = currentItem.splitStack(transferredAmount);
                            transportSource.draw(drawnStack, indices.get(i), path.firstSide);
                        }


                        if (remainingStack.isEmpty()) {
                            items.set(i, ItemStack.EMPTY);
                        } else {
                            items.get(i).setCount(remainingStack.getCount());
                        }
                    }
                }
            }
        }
    }


    public TileEntity getTileFromITransport(ITransportTile tile) {
        if (tile == null) {
            return null;
        }
        if (tile instanceof TileEntity) {
            return (TileEntity) tile;
        }

        return this.world.getTileEntity(tile.getBlockPos());
    }

    public Tuple<List<Path>, LinkedList<ITransportConductor>> discover(
            final ITransportSource emitter
    ) {
        final LinkedList<ITransportTile> tileEntitiesToCheck = new LinkedList<>();
        List<Path> energyPaths = new LinkedList<>();
        long id = WorldBaseGen.random.nextLong();
        emitter.setId(id);
        tileEntitiesToCheck.push(emitter);
        LinkedList<ITransportConductor> set = new LinkedList<>();

        while (!tileEntitiesToCheck.isEmpty()) {
            final ITransportTile currentTileEntity = tileEntitiesToCheck.pop();
            final List<InfoTile<ITransportTile>> validReceivers = this.getValidReceivers(currentTileEntity);
            InfoCable cable = null;
            if (currentTileEntity instanceof ITransportConductor) {
                cable = ((ITransportConductor) currentTileEntity).getCable();
            }
            for (final InfoTile<ITransportTile> validReceiver : validReceivers) {
                if (currentTileEntity == emitter){
                    if (validReceiver.tileEntity != emitter && validReceiver.tileEntity.getIdNetwork() != id) {
                        if (validReceiver.tileEntity instanceof ITransportConductor && ((ITransportConductor<?, ?>) validReceiver.tileEntity).isOutput()) {
                            ITransportConductor conductor = (ITransportConductor) validReceiver.tileEntity;
                            validReceiver.tileEntity.setId(id);
                            conductor.setCable(new InfoCable(conductor, validReceiver.direction, cable));
                            tileEntitiesToCheck.push(validReceiver.tileEntity);

                        }
                    }
                }
                else if (validReceiver.tileEntity != emitter && validReceiver.tileEntity.getIdNetwork() != id) {
                    if (validReceiver.tileEntity instanceof ITransportSink && currentTileEntity instanceof ITransportConductor && ((ITransportConductor<?, ?>) currentTileEntity).isInput()) {
                        validReceiver.tileEntity.setId(id);
                        energyPaths.add(new Path((ITransportSink) validReceiver.tileEntity, validReceiver.direction));
                        continue;
                    }

                    if (validReceiver.tileEntity instanceof ITransportConductor) {
                        validReceiver.tileEntity.setId(id);
                        ITransportConductor conductor = (ITransportConductor) validReceiver.tileEntity;
                        conductor.setCable(new InfoCable(conductor, validReceiver.direction, cable));
                        tileEntitiesToCheck.push(validReceiver.tileEntity);

                    }
                }
            }
        }
        int id1 = WorldBaseGen.random.nextInt();
        energyPaths = new ArrayList<>(energyPaths);
        for (Path energyPath : energyPaths) {
            ITransportTile tileEntity = energyPath.target;

            EnumFacing energyBlockLink = energyPath.targetDirection;
            tileEntity = (ITransportTile) tileEntity.getTiles().get(energyBlockLink);
            if (!(tileEntity instanceof ITransportConductor)) {
                continue;
            }
            energyPath.end = ((ITransportConductor) tileEntity);
            InfoCable cable = ((ITransportConductor) tileEntity).getCable();
            int max = energyPath.end.getMax();
            while (cable != null) {

                final ITransportConductor energyConductor = cable.getConductor();
                if (energyConductor.getHashCodeSource() != id1) {
                    energyConductor.setHashCodeSource(id1);
                    set.add(energyConductor);
                }
                if (energyConductor.getMax() < max) {
                    energyPath.end = null;
                    break;
                }
                cable = cable.getPrev();
                if (cable == null) {
                    break;
                } else {
                    energyPath.first = cable.getConductor();

                }
            }
            if (energyPath.first != null) {
                energyPath.firstSide = ModUtils.getFacingFromTwoPositions(emitter.getBlockPos(), energyPath.first.getBlockPos());
            }
        }
        return new Tuple<>(energyPaths, set);
    }


    private List<InfoTile<ITransportTile>> getValidReceivers(ITransportTile emitter) {

        return emitter.getValidReceivers();
    }

    public void onTickEnd() {
        if (!sourceToUpdateList.isEmpty()) {
            for (ITransportSource source : sourceToUpdateList) {
                remove1(source);
            }
            sourceToUpdateList.clear();
        }
        try {
            for (TransportTick<ITransportSource, Path> tick : this.senderPath) {
                if (this.world.getWorldTime() % 2L == 0L) {
                    if (tick.getSource().isItem()) {
                        ITransportSource<ItemStack, IItemHandler> entry = (ITransportSource<ItemStack, IItemHandler>) tick.getSource();


                        if (entry != null) {
                            if (tick.getEnergyItemPaths() == null) {

                                Tuple<List<Path>, LinkedList<ITransportConductor>> tuple = discover(entry);
                                final List<Path> list = tuple.getFirst();
                                final List<Path> removePath = new LinkedList<>();
                                for (Path transportPaths : list) {

                                    if (transportPaths.end == null || transportPaths.first == null || (transportPaths.first == transportPaths.end)) {
                                        removePath.add(transportPaths);
                                        continue;
                                    }
                                    if (!transportPaths.target.isSink()) {
                                        removePath.add(transportPaths);
                                        continue;
                                    }
                                    if (!transportPaths.first.isOutput() || transportPaths.end.isOutput()) {
                                        removePath.add(transportPaths);
                                        continue;
                                    }
                                    if (!transportPaths.first.isInput() && !transportPaths.first.isOutput()) {
                                        removePath.add(transportPaths);
                                        continue;
                                    }
                                    if (!transportPaths.end.isInput() && !transportPaths.end.isOutput()) {
                                        removePath.add(transportPaths);
                                        continue;
                                    }
                                    if (!(tick.getSource().getHandler(transportPaths.firstSide) instanceof IItemHandler)) {
                                        removePath.add(transportPaths);
                                        continue;
                                    }
                                    transportPaths.target.getEnergyTickList().add(tick.getSource().hashCode());
                                }
                                list.removeAll(removePath);
                                tick.setItemList(list);
                                tick.setConductors(tuple.getSecond());




                            }
                            if (!tick.getEnergyItemPaths().isEmpty()) {
                                emitTransportFrom(entry, tick.getEnergyItemPaths());


                            }
                        }
                    }
                }
                if (tick.getSource().isFluid()) {
                    ITransportSource<FluidStack, IFluidHandler> entry = (ITransportSource<FluidStack, IFluidHandler>) tick.getSource();
                    if (entry != null) {
                        if (tick.getEnergyFluidPaths() == null) {
                            Tuple<List<Path>, LinkedList<ITransportConductor>> tuple = discover(entry);
                            final List<Path> list = tuple.getFirst();
                            final List<Path> removePath = new LinkedList<>();
                            for (Path transportPaths : list) {

                                if (transportPaths.end == null || transportPaths.first == null || (transportPaths.first == transportPaths.end)) {
                                    removePath.add(transportPaths);
                                    continue;
                                }
                                if (!transportPaths.first.isOutput() || transportPaths.end.isOutput()) {
                                    removePath.add(transportPaths);
                                    continue;
                                }
                                if (!transportPaths.first.isInput() && !transportPaths.first.isOutput()) {
                                    removePath.add(transportPaths);
                                    continue;
                                }
                                if (!transportPaths.end.isInput() && !transportPaths.end.isOutput()) {
                                    removePath.add(transportPaths);
                                    continue;
                                }
                                if (!transportPaths.target.isFluidSink()) {
                                    removePath.add(transportPaths);
                                    continue;
                                }
                                if (!(tick.getSource().getHandler(transportPaths.firstSide) instanceof IFluidHandler)) {
                                    removePath.add(transportPaths);
                                    continue;
                                }
                                transportPaths.target.getEnergyTickList().add(tick.getSource().hashCode());

                            }
                            list.removeAll(removePath);
                            tick.setFluidList(list);
                            tick.setConductors(tuple.getSecond());
                        }
                        if (!tick.getEnergyFluidPaths().isEmpty()) {

                            emitTransportFluidFrom(entry, tick.getEnergyFluidPaths());
                        }
                    }
                }
            }
        } catch (Exception exception) {
            System.out.println("IUERROR:" + exception.getMessage());
        }
        tick++;
    }

    public void emitTransportFluidFrom(
            ITransportSource<FluidStack, IFluidHandler> TransportSource,
            List<Path> TransportPaths
    ) {


        for (Path TransportPath : TransportPaths) {
            TransportItem<FluidStack> transportItem = TransportSource.getOffered(1, TransportPath.firstSide);
            List<FluidStack> list = transportItem.getList();
            if (list.isEmpty()) {
                break;
            }
            if (TransportPath.end.getMax(tick) == 0) {
                continue;
            }

            if (TransportPath.first.getMax(tick) == 0) {
                continue;
            }
            IFluidHandler handler = TransportPath.getFluidHandler();
            for (FluidStack fluidStack : list) {
                if (!canInsertOrExtract(TransportPath.first, fluidStack, TransportPath.firstSide.getOpposite())) {
                    continue;
                }
                if (!canInsertOrExtract(TransportPath.end, fluidStack, TransportPath.targetDirection.getOpposite())) {
                    continue;
                }
                if (fluidStack.amount <= 0) {
                    continue;
                }
                int amount = handler.fill(fluidStack, false);
                amount = Math.min(amount, Math.min(TransportPath.first.getMax(tick), TransportPath.end.getMax(tick)));
                fluidStack = fluidStack.copy();
                fluidStack.amount = amount;
                if (amount > 0) {
                    handler.fill(fluidStack, true);
                    TransportPath.first.setMax(amount);
                    TransportPath.end.setMax(amount);
                    TransportSource.draw(fluidStack, amount, TransportPath.firstSide);
                }
            }
        }

    }

    public ITransportTile getTileEntity(BlockPos pos) {
        return this.chunkCoordinatesITransportTileMap.get(pos);
    }


    public void onTileEntityRemoved(final ITransportAcceptor par1) {

        this.onTileEntityAdded(par1);
    }


    public void onUnload() {
        this.senderPath.clear();
        this.chunkCoordinatesITransportTileMap.clear();
    }


}
