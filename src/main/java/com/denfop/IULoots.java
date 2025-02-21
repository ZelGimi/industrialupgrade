package com.denfop;


import com.denfop.world.WorldBaseGen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;

public class IULoots {

    public static LootTable VOLCANO_TABLE;
    public static LootPool VOLCANO_LOOT_POOL;

    private IULoots() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void init() {
        new IULoots();
    }

    public List<LootEntry> getEntries(LootPool pool) {
        return ReflectionHelper.getPrivateValue(LootPool.class, pool, remapFieldName(LootPool.class, "field_186453_a"), null);
    }

    private String remapFieldName(Class<?> clazz, String fieldName) {
        String internalClassName = FMLDeobfuscatingRemapper.INSTANCE.unmap(Type.getInternalName(clazz));
        return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(internalClassName, fieldName, null);
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        try {
            if (event.getName().equals(LootTableList.GAMEPLAY_FISHING)) {


                LootTable loottable = event.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING_FISH);
                LootTable loottable1 = event.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING_JUNK);
                LootTable loottable2 = event
                        .getLootTableManager()
                        .getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING_TREASURE);
                List<LootPool> lootPools = new ArrayList<>();
                LootPool mainPool = loottable.getPool("main");
                if (mainPool != null) {
                    lootPools.add(mainPool);
                    int in = 1;
                    LootPool pool = loottable.getPool("pool" + in);
                    while (pool != null) {
                        lootPools.add(pool);
                        in++;
                        pool = loottable.getPool("pool" + in);
                    }
                }
                mainPool = loottable1.getPool("main");
                if (mainPool != null) {
                    lootPools.add(mainPool);
                    int in = 1;
                    LootPool pool = loottable1.getPool("pool" + in);
                    while (pool != null) {
                        lootPools.add(pool);
                        in++;
                        pool = loottable1.getPool("pool" + in);
                    }
                }
                mainPool = loottable2.getPool("main");
                if (mainPool != null) {
                    lootPools.add(mainPool);
                    int in = 1;
                    LootPool pool = loottable2.getPool("pool" + in);
                    while (pool != null) {
                        lootPools.add(pool);
                        in++;
                        pool = loottable2.getPool("pool" + in);
                    }
                }

                for (LootPool pool : lootPools) {
                    for (LootEntry entry : getEntries(pool)) {
                        if (entry instanceof LootEntryItem) {
                            LootEntryItem itemEntry = (LootEntryItem) entry;
                            Item item = itemEntry.item;
                            ItemStack itemstack = new ItemStack(item);
                            for (LootFunction lootfunction : itemEntry.functions) {
                                if (LootConditionManager.testAllConditions(lootfunction.getConditions(), WorldBaseGen.random,
                                        null
                                )) {
                                    itemstack = lootfunction.apply(itemstack, WorldBaseGen.random, null);
                                }
                            }
                            IUCore.fish_rodding.add(itemstack);
                        }
                    }
                }
            }
            if (!event.getName().getResourceDomain().equals("minecraft")) {
                if (event.getName().equals(IUCore.VOLCANO_LOOT_TABLE)) {
                    VOLCANO_TABLE = event.getTable();
                    LootPool pool = event.getTable().getPool("industrialupgrade");
                    VOLCANO_LOOT_POOL = pool;
                    event.getTable().addPool(pool);
                }
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
            if (table == LootTable.EMPTY_LOOT_TABLE) {
                return;
            }

            LootPool pool = table.getPool("industrialupgrade");

            event.getTable().addPool(pool);
        } catch (Throwable ignored) {
        }

    }

}
