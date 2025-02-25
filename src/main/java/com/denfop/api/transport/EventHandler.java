package com.denfop.api.transport;

import com.denfop.api.energy.event.TileLoadEvent;
import com.denfop.api.energy.event.TileUnLoadEvent;
import com.denfop.api.energy.event.TilesUpdateEvent;
import com.denfop.api.transport.event.TransportTileLoadEvent;
import com.denfop.api.transport.event.TransportTileUnLoadEvent;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
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


    @SubscribeEvent(priority = EventPriority.LOW)
    public void load(TileLoadEvent event) {
        TileEntity tile = event.tileentity;
        final BlockPos pos = tile.getPos();
        if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) && tile.hasCapability(
                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                null
        )) {
            IItemHandler item_storage = tile.getCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                    EnumFacing.UP
            );
            IFluidHandler fluid_storage = tile.getCapability(
                    CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                    EnumFacing.UP
            );
            boolean isSink = true;
            boolean isSource = true;
            boolean isSinkFluid = true;
            boolean isSourceFluid = true;
            final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(tile,
                    pos,
                    item_storage,
                    fluid_storage,
                    isSink,
                    isSource,
                    isSinkFluid,
                    isSourceFluid
            );

            MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                    event.getWorld(), transport

            ));
        } else if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler item_storage = tile.getCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                    EnumFacing.UP
            );

            if (item_storage == null) {
                return;
            }
            final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(tile,pos,
                    item_storage, null, true, true,
                    false, false
            );

            MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                    event.getWorld(), transport

            ));
        } else if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
            boolean isSink = true;
            boolean isSource = true;

            IFluidHandler fluid_storage = tile.getCapability(
                    CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                    EnumFacing.UP
            );
            final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(tile,pos,
                    null, fluid_storage, false, false,
                    isSink, isSource
            );

            MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                    event.getWorld(), transport

            ));

        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void unLoad(TileUnLoadEvent event) {
        TileEntity tile = event.tileentity;
        final BlockPos pos = tile.getPos();
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

    @SubscribeEvent(priority = EventPriority.LOW)
    public void update(TilesUpdateEvent event) {

        for (TileEntity tile : new ArrayList<>(event.tiles)) {
            final BlockPos pos = tile.getPos();
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
                boolean isSink = true;
                boolean isSource = true;
                boolean isSinkFluid = true;
                boolean isSourceFluid = true;

                final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(tile,
                        pos,
                        item_storage,
                        fluid_storage,
                        isSink,
                        isSource,
                        isSinkFluid,
                        isSourceFluid
                );


                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                        event.getWorld(), transport

                ));
            } else if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                IItemHandler item_storage = tile.getCapability(
                        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                        null
                );

                if (item_storage == null) {
                    return;
                }


                boolean isSink = true;
                boolean isSource = true;
                final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(tile, pos,
                        item_storage, null, isSink, isSource,
                        false, false
                );

                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                        event.getWorld(), transport


                ));
            } else if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
                boolean isSink = true;
                boolean isSource = true;
                IFluidHandler item_storage = null;
                final TransportFluidItemSinkSource transport = new TransportFluidItemSinkSource(tile, pos,
                        null, item_storage, false, false,
                        isSink, isSource
                );
                MinecraftForge.EVENT_BUS.post(new TransportTileLoadEvent(
                        event.getWorld(), transport

                ));
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
