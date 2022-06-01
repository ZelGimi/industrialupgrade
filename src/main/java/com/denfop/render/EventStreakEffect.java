package com.denfop.render;


import com.denfop.utils.StreakLocationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventStreakEffect {

    private static final Map<String, ArrayList<StreakLocationUtils>> playerLoc = new HashMap<>();

    private static final HashMap<String, EntityStreak> streaks = new HashMap<>();

    private WorldClient worldInstance;

    public static ArrayList<StreakLocationUtils> getPlayerStreakLocationInfo(EntityPlayer player) {
        ArrayList<StreakLocationUtils> loc = playerLoc.computeIfAbsent(player.getName(), k -> new ArrayList<>());
        if (loc.size() < 20) {
            for (int i = 0; i < 20 - loc.size(); i++) {
                loc.add(0, new StreakLocationUtils(player));
            }
        } else if (loc.size() > 20) {
            loc.remove(0);
        }
        return loc;
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {

            Iterator<Map.Entry<String, EntityStreak>> iterator = streaks.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, EntityStreak> e = iterator.next();
                EntityStreak streak = e.getValue();
                if (streak.parent != null) {
                    if (streak.parent.isDead) {
                        streak.setDead();
                        iterator.remove();
                        continue;
                    }
                    updatePos(streak);
                }
            }
        }
    }

    @SubscribeEvent
    public void worldTick(TickEvent.ClientTickEvent event) {
        WorldClient world = (Minecraft.getMinecraft()).world;
        if (event.phase == TickEvent.Phase.END && world != null) {
            if (this.worldInstance != world) {
                this.worldInstance = world;
                streaks.clear();
            }
            Iterator<Map.Entry<String, EntityStreak>> ite = streaks.entrySet().iterator();
            while (ite.hasNext()) {
                Map.Entry<String, EntityStreak> e = ite.next();
                EntityStreak streak = e.getValue();
                if (streak.getEntityWorld().provider.getDimension() != world.provider.getDimension()
                        || world.getWorldTime() - streak.lastUpdate > 10L) {
                    streak.setDead();
                    ite.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == Side.CLIENT && event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            Minecraft mc = Minecraft.getMinecraft();
            if (player.getEntityWorld().getPlayerEntityByName(player.getName()) != player) {
                return;
            }
            WorldClient world = mc.world;
            EntityStreak hat = streaks.get(player.getName());
            if (hat == null || hat.isDead) {
                if (player.getName().equalsIgnoreCase(mc.player.getName())) {
                    for (Map.Entry<String, EntityStreak> e : streaks.entrySet()) {
                        e.getValue().setDead();
                    }
                }
                hat = new EntityStreak(world, player);
                streaks.put(player.getName(), hat);
                world.spawnEntity(hat);
            }
            ArrayList<StreakLocationUtils> loc = getPlayerStreakLocationInfo(player);
            StreakLocationUtils oldest = loc.get(0);
            loc.remove(0);
            loc.add(oldest);
            oldest.update(player);
            StreakLocationUtils newest = loc.get(loc.size() - 2);
            double distX = newest.posX - oldest.posX;
            double distZ = newest.posZ - oldest.posZ;

            for (newest.startU += Math.sqrt(distX * distX + distZ * distZ)
                    / newest.height; oldest.startU > 1.0D; oldest.startU--)
                ;
        }
    }

    private void updatePos(EntityStreak streak) {
        streak.lastTickPosX = streak.parent.lastTickPosX;
        streak.lastTickPosY = streak.parent.lastTickPosY;
        streak.lastTickPosZ = streak.parent.lastTickPosZ;
        streak.prevPosX = streak.parent.prevPosX;
        streak.prevPosY = streak.parent.prevPosY;
        streak.prevPosZ = streak.parent.prevPosZ;
        streak.posX = streak.parent.posX;
        streak.posY = streak.parent.posY;
        streak.posZ = streak.parent.posZ;
    }

}
