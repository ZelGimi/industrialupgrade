package com.denfop;

import ic2.core.IC2;
import ic2.core.util.LogCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class IULoot {

    public static void init() {
        new IULoot();
    }

    private IULoot() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        try {
            if (!event.getName().getResourceDomain().equals("minecraft")) {
                return;
            }

            if (this.getClass().getResource("/assets/industrialupgrade/loot_tables/" + event
                    .getName()
                    .getResourcePath() + ".json") == null) {
                return;
            }

            LootTable table = event.getLootTableManager().getLootTableFromLocation(new ResourceLocation(
                    Constants.MOD_ID,
                    event.getName().getResourcePath()
            ));
            if (table == null || table == LootTable.EMPTY_LOOT_TABLE) {
                return;
            }

            LootPool pool = table.getPool("industrialupgrade");
            if (pool == null) {
                return;
            }

            event.getTable().addPool(pool);
        } catch (Throwable var4) {
            IC2.log.warn(LogCategory.General, var4, "Error loading loot table %s.", event.getName().getResourcePath());
        }

    }

}
