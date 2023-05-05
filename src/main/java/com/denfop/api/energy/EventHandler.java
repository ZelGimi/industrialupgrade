package com.denfop.api.energy;


import appeng.tile.powersink.IExternalPowerSink;
import com.denfop.api.energy.event.EnergyMultiTileLoadEvent;
import com.denfop.api.energy.event.EnergyMultiTileUnLoadEvent;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.energy.event.EventLoadController;
import com.denfop.api.energy.event.EventUnloadController;
import com.denfop.integration.gc.GCIntegration;
import com.denfop.proxy.CommonProxy;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
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
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.denfop.api.energy.EnergyNetLocal.EMPTY;

public class EventHandler {

    public EventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(BlockEvent.PlaceEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if ((event.getWorld()).isRemote) {
            return;
        }
        BlockPos pos = event.getPos();
        Block block = event.getPlacedBlock().getBlock();
        if (event.getPlacedBlock().getMaterial() != Material.AIR && event.getState() != null &&
                block.hasTileEntity(event.getPlacedBlock())) {
            TileEntity tile = event.getWorld().getTileEntity(pos);

            if (tile == null) {
                return;
            }
            if (CommonProxy.gc) {
                boolean need = GCIntegration.check(tile);
                if (need) {
                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(event
                            .getWorld(), tile, new EnergyGJSink((TileBaseUniversalElectrical) tile)));
                    EnergyNetGlobal.addEnergyTile(event.getWorld(), tile.getPos());


                    return;
                }
            }
            if (tile instanceof ic2.core.block.TileEntityBlock || tile instanceof IAdvEnergyTile) {
                return;
            }

            IAdvEnergyTile iEnergyTile = EnergyNetGlobal.instance.getSubTile(event.getWorld(), tile.getPos());
            if (iEnergyTile != EnergyNetGlobal.EMPTY) {
                return;
            }
            if (tile instanceof cofh.redstoneflux.api.IEnergyHandler) {
                if (tile instanceof cofh.redstoneflux.api.IEnergyProvider && tile instanceof cofh.redstoneflux.api.IEnergyReceiver) {
                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(event
                            .getWorld(), tile, new EnergyRFSinkSource(tile)));
                } else if (tile instanceof cofh.redstoneflux.api.IEnergyProvider) {
                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(event
                            .getWorld(), tile, new EnergyRFSource(tile)));
                } else if (tile instanceof cofh.redstoneflux.api.IEnergyReceiver) {
                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(event
                            .getWorld(), tile, new EnergyRFSink(tile)));
                }
                EnergyNetGlobal.addEnergyTile(event.getWorld(), tile.getPos());
                return;
            }
            if (tile instanceof IExternalPowerSink) {
                MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(event
                        .getWorld(), tile, new EnergyAESink((IExternalPowerSink) tile, tile)));
                EnergyNetGlobal.addEnergyTile(event.getWorld(), tile.getPos());
                return;
            }

            for (EnumFacing facing : EnumFacing.values()) {
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
                        MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(event.getWorld(), tile,
                                new EnergyFESource(energy_storage, tile)
                        ));
                    } else {
                        MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(event
                                .getWorld(), tile, new EnergyFESink(energy_storage, tile)));
                    }
                    EnergyNetGlobal.addEnergyTile(event.getWorld(), tile.getPos());
                    break;
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(BlockEvent.BreakEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if ((event.getWorld()).isRemote) {
            return;
        }
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();
        if (block != Blocks.AIR && event.getState() != null &&
                block.hasTileEntity(event.getState())) {
            TileEntity tile = event.getWorld().getTileEntity(pos);
            if (tile == null) {
                return;
            }
            if (CommonProxy.gc) {
                boolean need = GCIntegration.check(tile);
                if (need) {
                    IAdvEnergyTile iEnergyTile = EnergyNetGlobal.instance.getSubTile(event.getWorld(), pos);
                    if (iEnergyTile == EMPTY) {
                        return;
                    }
                    MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(event.getWorld(), iEnergyTile));
                    EnergyNetGlobal.removeEnergyTile(event.getWorld().provider.getDimension(), pos);
                    return;
                }
            }
            if (tile instanceof ic2.core.block.TileEntityBlock || tile instanceof IAdvEnergyTile) {
                return;
            }
            EnergyNetGlobal.removeEnergyTile(event.getWorld().provider.getDimension(), pos);
            if (tile instanceof cofh.redstoneflux.api.IEnergyHandler) {
                IAdvEnergyTile iEnergyTile = EnergyNetGlobal.instance.getSubTile(event.getWorld(), pos);
                if (iEnergyTile != EMPTY) {
                    MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(event.getWorld(), iEnergyTile));
                }
                return;
            }
            for (EnumFacing facing : EnumFacing.values()) {
                if (tile.hasCapability(CapabilityEnergy.ENERGY, facing)) {
                    IAdvEnergyTile iEnergyTile = EnergyNetGlobal.instance.getSubTile(event.getWorld(), pos);
                    if (iEnergyTile != EMPTY) {
                        MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(event.getWorld(), iEnergyTile));
                    }
                }
            }

        }
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(EventLoadController event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.addController((IEnergyController) event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(EventUnloadController event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.removeController((IEnergyController) event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(EnergyTileLoadEvent event) {
        if ((event.getWorld()).isRemote) {
            return;
        }

        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {

            local.addTile(event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(EnergyMultiTileLoadEvent event) {
        if ((event.getWorld()).isRemote) {
            return;
        }

        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {

            local.addTileEntity(event.tile, event.subject);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(EnergyMultiTileUnLoadEvent event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.removeTileEntity(event.subject, event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnLoad(EnergyTileUnLoadEvent event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.removeTile(event.tile);
        }
    }


    @SubscribeEvent
    public void tick(TickEvent.WorldTickEvent event) {
        if (event.world.isRemote) {
            return;
        }

        if (event.phase == TickEvent.Phase.END) {
            EnergyNetGlobal.onTickEnd(event.world);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        EnergyNetGlobal.onWorldUnload(event.getWorld());
    }

}
