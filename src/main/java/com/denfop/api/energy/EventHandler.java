package com.denfop.api.energy;


import appeng.tile.powersink.AEBasePoweredTile;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.energy.event.EventLoadController;
import com.denfop.api.energy.event.EventUnloadController;
import com.denfop.api.energy.event.TileLoadEvent;
import com.denfop.api.energy.event.TileUnLoadEvent;
import com.denfop.api.energy.event.TilesUpdateEvent;
import com.denfop.integration.ae.AESink;
import com.denfop.items.relocator.RelocatorNetwork;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class EventHandler {

    public EventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
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
    public void worldLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            List<TileEntity> list = new LinkedList<TileEntity>(world.loadedTileEntityList) {
                @Override
                public boolean removeAll(final Collection<?> c) {
                    return super.removeAll(c);
                }

                @Override
                public boolean addAll(final Collection<? extends TileEntity> c) {
                    return super.addAll(c);
                }

                @Override
                public boolean add(final TileEntity o) {
                    final boolean b = super.add(o);
                    if (b) {
                        MinecraftForge.EVENT_BUS.post(new TileLoadEvent(
                                event.getWorld(),
                                o
                        ));
                    }
                    return b;
                }
                @Override
                public boolean remove(final Object o) {
                    final boolean b =  super.remove(o);
                    if (b)
                    MinecraftForge.EVENT_BUS.post(new TileUnLoadEvent(
                            event.getWorld(),
                            (TileEntity) o
                    ));

                    return b;
                }

                @Override
                public TileEntity remove(final int index) {
                    final TileEntity tile =  super.remove(index);
                    MinecraftForge.EVENT_BUS.post(new TileUnLoadEvent(
                            event.getWorld(),
                            tile
                    ));

                    return tile;
                }

            };
            world.loadedTileEntityList = list;
            MinecraftForge.EVENT_BUS.post(new TilesUpdateEvent(
                    event.getWorld(),
                    list
            ));
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
