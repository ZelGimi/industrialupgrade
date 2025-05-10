package com.denfop.proxy;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.space.SpaceInit;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.items.IProperties;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import com.denfop.world.WorldBaseGen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.denfop.IUItem.panel_list;


public class CommonProxy {
    public static void sendPlayerMessage(Player player, String text) {
        if (!player.getLevel().isClientSide()) {
            IUCore.proxy.messagePlayer(player, text);
        }
    }

    public void messagePlayer(Player player, String message) {
        if (player != null) {
            player.displayClientMessage(Component.translatable(message), false);
        }
    }

    public void removePotion(LivingEntity entity, MobEffect potion) {
        entity.removeEffect(potion);
    }


    public Level getWorld(ResourceKey<Level> dim) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server != null && dim != null ? server.getLevel(dim) : null;
    }

    public void addProperties(IProperties properties) {

    }

    public List<IProperties> getPropertiesList() {
        return Collections.emptyList();
    }

    public Player getPlayerInstance() {
        return null;
    }


    public void init() {
        WorldBaseGen.initVein();
        List<Object> objectList = new ArrayList<>();
        objectList.add(EnumSolarPanels.QUARK_SOLAR_PANEL.genday * 4);
        objectList.add(EnumSolarPanels.QUARK_SOLAR_PANEL.gennight * 4);
        objectList.add(EnumSolarPanels.QUARK_SOLAR_PANEL.maxstorage * 16);
        objectList.add(EnumSolarPanels.QUARK_SOLAR_PANEL.producing * 4);
        objectList.add(14);
        panel_list.put(new ItemStack(IUItem.blockadmin.getItem()).getDescriptionId(), objectList);
        //   SolarEnergySystem.system = new SolarEnergySystem();
    }

    public void postInit() {
        TileEntityMultiCable.list = null;
        TileSolarPanel.list = null;


    }

    public void preInit() {
        TileBlockCreator.instance.buildBlocks();
        SpaceInit.init();
        UpgradeSystem.system.addModification();
    }
}
