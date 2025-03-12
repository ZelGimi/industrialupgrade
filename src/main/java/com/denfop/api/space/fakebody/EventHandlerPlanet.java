package com.denfop.api.space.fakebody;


import com.denfop.Constants;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.research.api.IResearchTable;
import com.denfop.api.space.research.api.IRocketLaunchPad;
import com.denfop.api.space.research.event.ResearchTableLoadEvent;
import com.denfop.api.space.research.event.ResearchTableUnLoadEvent;
import com.denfop.api.space.research.event.RocketPadLoadEvent;
import com.denfop.api.space.research.event.RocketPadUnLoadEvent;
import com.denfop.events.WorldSavedDataIU;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;
import java.util.UUID;

public class EventHandlerPlanet {

    private final boolean load;
    public static WorldSavedDataIU data;

    public EventHandlerPlanet() {
        this.load = false;
    }

    @SubscribeEvent
    public void tick(final TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.world.provider.getDimension() == 0 && event.world.provider.getWorldTime() % 20 == 0) {
                SpaceNet.instance.getFakeSpaceSystem().working();
                SpaceNet.instance.getColonieNet().working();
            }
        }


    }



    @SubscribeEvent
    public void load(final ResearchTableLoadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        final Map<UUID, IResearchTable> map = SpaceNet.instance
                .getFakeSpaceSystem()
                .getResearchTableMap();
        if (!map.containsKey(event.table.getPlayer())) {
            map.put(event.table.getPlayer(), event.table);
        } else {
            map.replace(event.table.getPlayer(), event.table);
        }

    }

    @SubscribeEvent
    public void unLoad(final ResearchTableUnLoadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        final Map<UUID, IResearchTable> map = SpaceNet.instance
                .getFakeSpaceSystem()
                .getResearchTableMap();
        map.remove(event.table.getPlayer());

    }

    @SubscribeEvent
    public void loadRocketPad(final RocketPadLoadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        final Map<UUID, IRocketLaunchPad> map = SpaceNet.instance
                .getFakeSpaceSystem()
                .getRocketPadMap();
        if (!map.containsKey(event.table.getPlayer())) {
            map.put(event.table.getPlayer(), event.table);
        } else {
            map.replace(event.table.getPlayer(), event.table);
        }

    }

    @SubscribeEvent
    public void unLoadRocketPad(final RocketPadUnLoadEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        final Map<UUID, IRocketLaunchPad> map = SpaceNet.instance
                .getFakeSpaceSystem()
                .getRocketPadMap();
        map.remove(event.table.getPlayer());

    }

     @SubscribeEvent
    public void onSave(WorldEvent.Save event) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && data != null && event.getWorld().provider.getDimension() == 0) {
            data.setDirty(true);
        }
    }

    @SubscribeEvent
    public void onUnload(WorldEvent.Unload event) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && data != null && event.getWorld().provider.getDimension() == 0) {
            data.setDirty(true);
        }
    }




}
