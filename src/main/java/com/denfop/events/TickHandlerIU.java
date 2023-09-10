package com.denfop.events;

import com.denfop.IUCore;
import com.denfop.network.WorldData;
import com.denfop.world.IWorldTickCallback;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickHandlerIU {


    public static void requestSingleWorldTick(World world, IWorldTickCallback callback) {
        WorldData.get(world).singleUpdates.add(callback);

    }

    private static void processUpdates(World world, WorldData worldData) {

        IWorldTickCallback callback;
        for (; (callback = worldData.singleUpdates.poll()) != null; callback.onTick(world)) {

        }


    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        World world = event.world;
        WorldData worldData = WorldData.get(world, false);
        if (worldData != null) {
            if (event.phase == TickEvent.Phase.START) {
                processUpdates(world, worldData);

            } else {
                if (world.isRemote) {
                    IUCore.network.getClient().onTickEnd(worldData);
                } else {
                    IUCore.network.getServer().onTickEnd(worldData);
                }
            }

        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            IUCore.keyboard.sendKeyUpdate();
            World world = IUCore.proxy.getPlayerWorld();
            if (world != null) {
                processUpdates(world, WorldData.get(world));

            }
        }

    }

}
