package com.denfop.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;

public class AddFromLootTableModifier extends LootModifier {

    public static final MapCodec<AddFromLootTableModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            LootModifier.codecStart(instance)
                    .and(ResourceKey.codec(net.minecraft.core.registries.Registries.LOOT_TABLE)
                            .fieldOf("loot_table")
                            .forGetter(modifier -> modifier.lootTable))
                    .apply(instance, AddFromLootTableModifier::new)
    );
    public static DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AddFromLootTableModifier>> ADD_FROM_LOOT_TABLE;

    private final ResourceKey<LootTable> lootTable;

    public AddFromLootTableModifier(LootItemCondition[] conditions, ResourceKey<LootTable> lootTable) {
        super(conditions);
        this.lootTable = lootTable;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        LootTable table = context.getResolver().lookupOrThrow(Registries.LOOT_TABLE).get(this.lootTable).get().value();

        table.getRandomItemsRaw(context, generatedLoot::add);

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}