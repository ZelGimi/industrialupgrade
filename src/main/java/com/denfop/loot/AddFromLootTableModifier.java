package com.denfop.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.RegistryObject;

public class AddFromLootTableModifier extends LootModifier {

    public static final Codec<AddFromLootTableModifier> CODEC = RecordCodecBuilder.create(instance ->
            codecStart(instance)


                    .and(ResourceLocation.CODEC.fieldOf("loot_table").forGetter(modifier -> modifier.lootTable))
                    .apply(instance, AddFromLootTableModifier::new)
    );
    public static RegistryObject<Codec<AddFromLootTableModifier>> ADD_FROM_LOOT_TABLE;

    private final ResourceLocation lootTable;

    public AddFromLootTableModifier(LootItemCondition[] conditions, ResourceLocation lootTable) {
        super(conditions);
        this.lootTable = lootTable;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        LootTable table = context.getResolver().getLootTable(this.lootTable);

        table.getRandomItemsRaw(context, generatedLoot::add);

        return generatedLoot;
    }

    @Override
    public Codec<? extends AddFromLootTableModifier> codec() {
        return CODEC;
    }
}