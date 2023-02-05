package com.denfop.api.energy;


import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.energy.event.EventLoadController;
import com.denfop.api.energy.event.EventUnloadController;
import com.denfop.integration.ae.AEIntegration;
import com.denfop.integration.gc.GCIntegration;
import com.denfop.proxy.CommonProxy;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
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
                    IEnergyTile iEnergyTile = EnergyNet.instance.getSubTile(event.getWorld(), pos);
                    MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(event.getWorld(), iEnergyTile));
                    if (tile instanceof IEnergySink) {
                        MinecraftForge.EVENT_BUS.post(new com.denfop.api.energy.event.EnergyTileLoadEvent(event
                                .getWorld(), tile, new EnergyGJSink((TileBaseUniversalElectrical) tile)));
                        EnergyNetGlobal.addEnergyTile(event.getWorld(), tile.getPos());
                    }

                    return;
                }
            }
            if (tile instanceof ic2.core.block.TileEntityBlock || tile instanceof IEnergyTile) {
                return;
            }
            if (CommonProxy.ae2) {
                boolean need = AEIntegration.check(tile);
                if (need) {
                    return;
                }
            }

            IEnergyTile iEnergyTile = EnergyNet.instance.getSubTile(event.getWorld(), tile.getPos());
            if (iEnergyTile != null) {
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
                    IEnergyTile iEnergyTile = EnergyNet.instance.getSubTile(event.getWorld(), pos);
                    MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(event.getWorld(), iEnergyTile));
                    EnergyNetGlobal.removeEnergyTile(event.getWorld().provider.getDimension(), pos);
                    return;
                }
            }
            if (tile instanceof ic2.core.block.TileEntityBlock || tile instanceof IEnergyTile) {
                return;
            }
            for (EnumFacing facing : EnumFacing.values()) {
                if (tile.hasCapability(CapabilityEnergy.ENERGY, facing)) {
                    IEnergyTile iEnergyTile = EnergyNet.instance.getSubTile(event.getWorld(), pos);
                    MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(event.getWorld(), iEnergyTile));
                    EnergyNetGlobal.removeEnergyTile(event.getWorld().provider.getDimension(), pos);
                    return;
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
    public void onEnergyTileLoad(com.denfop.api.energy.event.EnergyTileLoadEvent event) {
        if ((event.getWorld()).isRemote) {
            return;
        }
        EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.addTile(event.tile, event.tileentity);
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

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnload(EnergyTileUnloadEvent event) {
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
