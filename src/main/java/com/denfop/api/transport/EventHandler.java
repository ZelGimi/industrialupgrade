package com.denfop.api.transport;

import com.denfop.api.transport.event.TransportTileLoadEvent;
import com.denfop.api.transport.event.TransportTileUnLoadEvent;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {

    EnumFacing[] enumFacings = EnumFacing.VALUES;

    public EventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(final BlockEvent.PlaceEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if (event.getWorld().isRemote) {
            return;
        }
        BlockPos pos = event.getPos();
        Block block = event.getPlacedBlock().getBlock();
        if (block.hasTileEntity(event.getPlacedBlock())) {
            TileEntity tile = event.getWorld().getTileEntity(pos);
            if (tile != null) {
                if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) && tile.hasCapability(
                        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                        null
                )) {
                    IItemHandler item_storage = tile.getCapability(
                            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                            null
                    );
                    IFluidHandler fluid_storage = tile.getCapability(
                            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                            null
                    );
                    boolean isSink = false;
                    boolean isSource = false;
                    boolean isSinkFluid = false;
                    boolean isSourceFluid = false;
                    List<EnumFacing> facingListSink = new ArrayList<>();
                    List<EnumFacing> facingListSource = new ArrayList<>();
                    for (EnumFacing facing : this.enumFacings) {
                        TileEntity tile1 = event.getWorld().getTileEntity(pos.offset(facing));
                        if (tile1 instanceof ITransportConductor) {
                            final ITransportConductor transport = (ITransportConductor) tile1;
                            final Object handler = transport.getHandler();

                            if (handler instanceof IItemHandler) {
                                item_storage = tile.getCapability(
                                        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                                        facing.getOpposite()
                                );
                                if (transport.isOutput()) {
                                    isSource = true;
                                    facingListSource.add(facing.getOpposite());
                                } else {
                                    isSink = true;
                                    facingListSink.add(facing.getOpposite());
                                }
                            } else if (handler instanceof IFluidHandler) {
                                fluid_storage = tile.getCapability(
                                        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                                        facing.getOpposite()
                                );
                                if (transport.isOutput()) {
                                    isSourceFluid = true;
                                    facingListSource.add(facing.getOpposite());
                                } else {
                                    isSinkFluid = true;
                                    facingListSink.add(facing.getOpposite());
                                }
                            }
                        }
                    }
                    if (!(!isSink && !isSinkFluid && !isSource && !isSourceFluid)) {
                        final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(
                                pos,
                                item_storage,
                                fluid_storage,
                                isSink,
                                isSource,
                                isSinkFluid,
                                isSourceFluid
                        );

                        transport.setFacingListSink(facingListSink);
                        transport.setFacingListSource(facingListSource);
                        MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                event.getWorld(), transport

                        ));
                    }
                } else if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                    IItemHandler item_storage = tile.getCapability(
                            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                            null
                    );

                    if (item_storage == null) {
                        return;
                    }
                    List<EnumFacing> facingListSink = new ArrayList<>();
                    List<EnumFacing> facingListSource = new ArrayList<>();

                    boolean isSink = false;
                    boolean isSource = false;
                    for (EnumFacing facing : this.enumFacings) {
                        TileEntity tile1 = event.getWorld().getTileEntity(pos.offset(facing));
                        if (tile1 instanceof ITransportConductor) {
                            final ITransportConductor transport = (ITransportConductor) tile1;

                            if (transport.isItem()) {
                                item_storage = tile.getCapability(
                                        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                                        facing.getOpposite()
                                );
                                if (transport.isOutput()) {
                                    facingListSource.add(facing.getOpposite());
                                    isSource = true;
                                } else {
                                    facingListSink.add(facing.getOpposite());
                                    isSink = true;
                                }
                            }
                        }
                    }
                    if (isSink || isSource) {
                        final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(pos,
                                item_storage, null, isSink, isSource,
                                false, false
                        );
                        transport.setFacingListSink(facingListSink);
                        transport.setFacingListSource(facingListSource);
                        MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                event.getWorld(), transport


                        ));
                    }
                } else if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
                    boolean isSink = false;
                    boolean isSource = false;
                    IFluidHandler item_storage = null;
                    List<EnumFacing> facingListSink = new ArrayList<>();
                    List<EnumFacing> facingListSource = new ArrayList<>();

                    for (EnumFacing facing : this.enumFacings) {
                        TileEntity tile1 = event.getWorld().getTileEntity(pos.offset(facing));
                        if (tile1 instanceof ITransportConductor) {
                            final ITransportConductor transport = (ITransportConductor) tile1;
                            final Object handler = transport.getHandler();

                            if (handler instanceof IFluidHandler) {
                                item_storage = tile.getCapability(
                                        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                                        facing.getOpposite()
                                );
                                if (transport.isOutput()) {
                                    facingListSource.add(facing.getOpposite());
                                    isSource = true;
                                } else {
                                    facingListSink.add(facing.getOpposite());
                                    isSink = true;
                                }
                                break;
                            }
                        }
                    }
                    if (isSink || isSource) {
                        final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(pos,
                                null, item_storage, false, false,
                                isSink, isSource
                        );
                        transport.setFacingListSink(facingListSink);
                        transport.setFacingListSource(facingListSource);
                        MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                                event.getWorld(), transport

                        ));
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(final BlockEvent.BreakEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if (event.getWorld().isRemote) {
            return;
        }
        if (event.getPlayer() == null) {
            return;
        }

        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();
        if (block.hasTileEntity(event.getState())) {
            TileEntity tile = event.getWorld().getTileEntity(pos);
            if (tile != null) {
                if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) && tile.hasCapability(
                        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                        null
                )) {
                    final ITransportTile iEnergyTile = TransportNetGlobal.instance.getSubTile(event.getWorld(), pos);
                    if (iEnergyTile != null) {
                        MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent(
                                event.getWorld(),
                                iEnergyTile
                        ));
                    }
                } else if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {

                    final ITransportTile iEnergyTile = TransportNetGlobal.instance.getSubTile(event.getWorld(), pos);
                    if (iEnergyTile != null) {
                        MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent<ItemStack, IItemHandler>(
                                event.getWorld(),
                                iEnergyTile
                        ));
                    }


                } else if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
                    final ITransportTile iEnergyTile = TransportNetGlobal.instance.getSubTile(event.getWorld(), pos);
                    if (iEnergyTile != null) {
                        MinecraftForge.EVENT_BUS.post(new TransportTileUnLoadEvent<FluidStack, IFluidHandler>(
                                event.getWorld(),
                                iEnergyTile
                        ));
                    }
                }
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(final TransportTileLoadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }

        final TransportNetLocal local = TransportNetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.addTile(event.tile);
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(final TransportTileUnLoadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }

        final TransportNetLocal local = TransportNetGlobal.getForWorld(event.getWorld());

        if (local != null) {
            local.removeTile(event.tile);
        }
    }


    @SubscribeEvent
    public void tick(final TickEvent.WorldTickEvent event) {
        if (event.world.isRemote) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            TransportNetGlobal.onTickEnd(event.world);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        if (event.getWorld().isRemote) {
            return;
        }
        TransportNetGlobal.onWorldUnload(event.getWorld());
    }

}
