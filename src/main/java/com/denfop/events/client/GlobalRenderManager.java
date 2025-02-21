package com.denfop.events.client;

import com.denfop.gui.GuiIU;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class GlobalRenderManager {

    public GlobalRenderManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static long tick = 0;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onWorldTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player.getEntityWorld().isRemote && tick != event.player
                .getEntityWorld()
                .getWorldTime()) {
            tick = event.player.getEntityWorld().getWorldTime();
            GuiScreen guiScreen = Minecraft.getMinecraft().currentScreen;
            if (guiScreen instanceof GuiIU) {
                double ticks = 4;
                while (ticks > 0) {
                    ((GuiIU<?>) guiScreen).updateTickInterface();
                    ticks--;
                }

            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)

    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (player == null || mc.world == null) {
            return;
        }
        try {
            for (Map.Entry<Integer, Map<BlockPos, Function>> entry : globalRenders.entrySet()) {
                if (entry.getKey() == mc.player.getEntityWorld().provider.getDimension()) {
                    for (Function function : entry.getValue().values()) {
                        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
                        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
                        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

                        GlStateManager.pushMatrix();
                        GlStateManager.translate(-x, -y, -z);
                        function.apply(0);
                        GlStateManager.popMatrix();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ;
    }

    public static Map<Integer, Map<BlockPos, Function>> globalRenders = new HashMap<>();

    public static void addRender(World world, BlockPos pos, Function globalRender) {
        Map<BlockPos, Function> map = globalRenders.computeIfAbsent(world.provider.getDimension(), k -> new HashMap<>());
        if (!map.containsKey(pos)) {
            map.put(pos, globalRender);
        }
    }

    public static void removeRender(World world, BlockPos pos) {
        Map<BlockPos, Function> map = globalRenders.computeIfAbsent(world.provider.getDimension(), k -> new HashMap<>());
        map.remove(pos);
    }

}
