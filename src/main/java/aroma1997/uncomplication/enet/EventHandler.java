package aroma1997.uncomplication.enet;


import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.info.ILocatable;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler {
    public EventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }



    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileLoad(final EnergyTileLoadEvent event) {
        final IEnergyTile tile = event.tile;
        if (tile instanceof IEnergySource) {
            EnergyTransferList.initIEnergySource((IEnergySource) event.tile);
        }
        final EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());

        if (local != null) {
            local.addTile(event.tile);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEnergyTileUnload(final EnergyTileUnloadEvent event) {
        final EnergyNetLocal local = EnergyNetGlobal.getForWorld(event.getWorld());
        if (local != null) {
            local.removeTile(event.tile);
        }
    }

    @SubscribeEvent
    public void tick(final TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            EnergyNetGlobal.onTickStart(event.world);
        } else if (event.phase == TickEvent.Phase.END) {
            EnergyNetGlobal.onTickEnd(event.world);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        EnergyNetGlobal.onWorldUnload(event.getWorld());
    }
}
