package com.denfop.api.energy;


import cofh.redstoneflux.api.IEnergyHandler;
import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.energy.event.EventLoadController;
import com.denfop.api.energy.event.EventUnloadController;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;
import ic2.core.block.TileEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;

public class EventHandler {

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
        if(event.getPlacedBlock().getMaterial() != Material.AIR && event.getState() != null)
        if (block.hasTileEntity(event.getPlacedBlock())) {
            TileEntity tile = event.getWorld().getTileEntity(pos);
            if(tile == null)
                return;
            if (tile instanceof TileEntityBlock) {
                return;
            }
            if (tile instanceof IEnergyHandler) {


                if (tile instanceof IEnergyProvider && tile instanceof IEnergyReceiver) {
                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(
                            event.getWorld(),
                            tile,
                            new EnergyRFSinkSource(tile)
                    ));
                } else if (tile instanceof IEnergyProvider) {

                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(
                            event.getWorld(),
                            tile,
                            new EnergyRFSource(tile)
                    ));
                } else if (tile instanceof IEnergyReceiver) {
                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(
                            event.getWorld(),
                            tile,
                            new EnergyRFSink(tile)
                    ));

                }

            }

            for(EnumFacing facing : EnumFacing.values())
             if (tile.hasCapability(CapabilityEnergy.ENERGY, facing)) {

                IEnergyStorage energy_storage = tile.getCapability(CapabilityEnergy.ENERGY, facing);
                if (energy_storage == null) {
                    return;
                }
                if (energy_storage.canExtract() && energy_storage.canReceive()) {
                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(event.getWorld(), tile,
                            new EnergyFESinkSource(energy_storage, tile)
                    ));
                } else if (energy_storage.canExtract()) {

                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(event.getWorld(), tile,
                            new EnergyFESource(energy_storage, tile)
                    ));
                } else {
                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(
                            event.getWorld(),
                            tile,
                            new EnergyFESink(energy_storage, tile)
                    ));

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


        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();
        if(block != Blocks.AIR && event.getState() != null)
        if (block.hasTileEntity(event.getState())) {
            TileEntity tile = event.getWorld().getTileEntity(pos);
            if (tile == null) {
                return;
            }
            if (tile instanceof TileEntityBlock) {
                return;
            }
            if (tile instanceof IEnergyHandler) {

                IEnergyTile iEnergyTile = EnergyNet.instance.getSubTile(event.getWorld(), pos);
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(event.getWorld(), iEnergyTile));


            }

            for(EnumFacing facing : EnumFacing.values())
            if (tile.hasCapability(CapabilityEnergy.ENERGY, facing)) {

                IEnergyTile iEnergyTile = EnergyNet.instance.getSubTile(event.getWorld(), pos);
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(event.getWorld(), iEnergyTile));

            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyChunkLoad(final ChunkEvent.Load event) {
        if (event.getWorld().isRemote) {
            return;
        }
        if (event.getChunk().tileEntities == null || event.getChunk().tileEntities.isEmpty()) {
            return;
        }

        final Map<BlockPos, TileEntity> map = event.getChunk().tileEntities;
        for (Map.Entry<BlockPos, TileEntity> entry : map.entrySet()) {
            TileEntity tile = entry.getValue();
            if (tile instanceof TileEntityBlock) {
                continue;
            }
            if (tile instanceof IEnergyHandler) {

                if (tile instanceof IEnergyProvider && tile instanceof IEnergyReceiver) {
                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(
                            event.getWorld(),
                            tile,
                            new EnergyRFSinkSource(tile)
                    ));
                } else if (tile instanceof IEnergyProvider) {

                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(
                            event.getWorld(),
                            tile,
                            new EnergyRFSource(tile)
                    ));
                } else if (tile instanceof IEnergyReceiver) {
                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(
                            event.getWorld(),
                            tile,
                            new EnergyRFSink(tile)
                    ));

                }
                continue;
            }
            for(EnumFacing facing : EnumFacing.values())
                if (tile.hasCapability(CapabilityEnergy.ENERGY, facing)) {


                IEnergyStorage energy_storage = tile.getCapability(CapabilityEnergy.ENERGY, facing);
                if (energy_storage == null) {
                    continue;
                }
                if (energy_storage.canExtract() && energy_storage.canReceive()) {
                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(event.getWorld(), tile,
                            new EnergyFESinkSource(energy_storage, tile)
                    ));
                } else if (energy_storage.canExtract()) {

                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(
                            event.getWorld(),
                            tile,
                            new EnergyFESource(energy_storage, tile)
                    ));
                } else {
                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(
                            event.getWorld(),
                            tile,
                            new EnergyFESink(energy_storage, tile)
                    ));

                }
                break;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyChunkUnLoad(final ChunkEvent.Unload event) {
        if (event.getWorld().isRemote) {
            return;
        }

        if (event.getChunk().tileEntities == null || event.getChunk().tileEntities.isEmpty()) {
            return;
        }

        final Map<BlockPos, TileEntity> map = event.getChunk().tileEntities;

        for (Map.Entry<BlockPos, TileEntity> entry : map.entrySet()) {
            TileEntity tile = entry.getValue();
            BlockPos pos = entry.getKey();
            if (tile instanceof IEnergyHandler) {
                if (tile instanceof TileEntityBlock) {
                    continue;
                }
                if (tile instanceof IEnergyProvider && tile instanceof IEnergyReceiver) {

                    IEnergyTile iEnergyTile = EnergyNet.instance.getSubTile(event.getWorld(), pos);
                    MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(event.getWorld(), iEnergyTile));

                } else if (tile instanceof IEnergyReceiver) {
                    IEnergyTile iEnergyTile = EnergyNet.instance.getSubTile(event.getWorld(), pos);
                    MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(event.getWorld(), iEnergyTile));

                }
            }
            if (tile instanceof TileEntityBlock) {
                continue;
            }
            for(EnumFacing facing : EnumFacing.values())
             if (tile.hasCapability(CapabilityEnergy.ENERGY, facing)) {

                IEnergyTile iEnergyTile = EnergyNet.instance.getSubTile(event.getWorld(), pos);
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(event.getWorld(), iEnergyTile));
                 break;
            }
        }

    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(final EventLoadController event) {
        if (event.getWorld().isRemote) {
            return;
        }

        final EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());

        if (local != null) {
            local.addController((IEnergyController) event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(final EventUnloadController event) {
        if (event.getWorld().isRemote) {
            return;
        }

        final EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());

        if (local != null) {
            local.removeController((IEnergyController) event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(final EnergyTileLoadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }

        final EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());

        if (local != null) {
            local.addTile(event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(final com.denfop.api.energy.event.EnergyTileLoadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }

        final EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());

        if (local != null) {
            local.addTile(event.tile, event.tileentity);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(final EnergyTileUnLoadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }

        final EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());

        if (local != null) {
            local.removeTile(event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnload(final EnergyTileUnloadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        final EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
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
            EnergyNetGlobal.onTickEnd(event.world);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        if (event.getWorld().isRemote) {
            return;
        }
        EnergyNetGlobal.onWorldUnload(event.getWorld());
    }

}
