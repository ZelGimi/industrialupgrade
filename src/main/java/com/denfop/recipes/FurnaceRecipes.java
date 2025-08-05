package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.datagen.furnace.FurnaceProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FurnaceRecipes {
    public static void recipe() {
        FurnaceProvider.addSmelting(new ItemStack(IUItem.blockResource.getItem(11), 1), new ItemStack(IUItem.cultivated_peat_balls.getItem()), 5);
        for (int i = 0; i < 30; i++) {
            if (i < 16) {
                FurnaceProvider.addSmelting(new ItemStack(IUItem.space_cobblestone.getItem(i), 1), new ItemStack(IUItem.space_stone.getItem(i), 1),
                        0.1f);
            } else {
                FurnaceProvider.addSmelting(new ItemStack(IUItem.space_cobblestone1.getItem(i - 16), 1), new ItemStack(IUItem.space_stone1.getItem(i - 16),
                                1),
                        0.1f);
            }

        }
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(0), 1), new ItemStack(IUItem.iuingot.getStack(0), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(1), 1), new ItemStack(IUItem.iuingot.getStack(1), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(2), 1), new ItemStack(IUItem.iuingot.getStack(2), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(3), 1), new ItemStack(IUItem.iuingot.getStack(3), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(4), 1), new ItemStack(IUItem.iuingot.getStack(6), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(5), 1), new ItemStack(IUItem.iuingot.getStack(7), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(6), 1), new ItemStack(IUItem.iuingot.getStack(8), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(7), 1), new ItemStack(IUItem.iuingot.getStack(9), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(8), 1), new ItemStack(IUItem.iuingot.getStack(10), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(9), 1), new ItemStack(IUItem.iuingot.getStack(11), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(10), 1), new ItemStack(IUItem.iuingot.getStack(12), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(11), 1), new ItemStack(IUItem.iuingot.getStack(14), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(12), 1), new ItemStack(IUItem.iuingot.getStack(15), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(13), 1), new ItemStack(IUItem.iuingot.getStack(16), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(14), 1), new ItemStack(IUItem.iuingot.getStack(17), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore.getItem(15), 1), new ItemStack(IUItem.iuingot.getStack(18), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore2.getItem(3), 1), new ItemStack(IUItem.iuingot.getStack(25), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore2.getItem(4), 1), new ItemStack(IUItem.iuingot.getStack(26), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.ore2.getItem(5), 1), new ItemStack(IUItem.iuingot.getStack(27), 1), 0.6f);
        FurnaceProvider.addSmelting(IUItem.leadOre, IUItem.leadIngot, 0.6f);
        FurnaceProvider.addSmelting(IUItem.tinOre, IUItem.tinIngot, 0.6f);
        for (int k = 0; k < 15; k++) {
            FurnaceProvider.addSmelting(new ItemStack(IUItem.ore3.getItem(k), 1), new ItemStack(IUItem.iuingot.getStack(28 + k), 1), 0.6f);
        }

        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore1.getItem(1), 1), new ItemStack(IUItem.iuingot.getStack(1), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore3.getItem(0), 1), new ItemStack(IUItem.iuingot.getStack(11), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore.getItem(14), 1), new ItemStack(IUItem.iuingot.getStack(7), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore.getItem(13), 1), new ItemStack(IUItem.iuingot.getStack(7), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(11), 1), new ItemStack(IUItem.iuingot.getStack(42), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore1.getItem(13), 1), new ItemStack(Items.GOLD_INGOT), 5.0F);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore.getItem(1), 1), new ItemStack(IUItem.iuingot.getStack(29), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(4), 1), new ItemStack(IUItem.iuingot.getStack(41), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(7), 1), new ItemStack(IUItem.iuingot.getStack(24), 1), 5.0F);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(8), 1), new ItemStack(IUItem.iuingot.getStack(31), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(0), 1), new ItemStack(IUItem.iuingot.getStack(6), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore.getItem(12), 1), new ItemStack(IUItem.iuingot.getStack(9), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(5), 1), new ItemStack(IUItem.iuingot.getStack(42), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore.getItem(11), 1), new ItemStack(IUItem.iuingot.getStack(22), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore.getItem(8), 1), new ItemStack(IUItem.iuingot.getStack(28), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore.getItem(7), 1), new ItemStack(Items.IRON_INGOT), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore3.getItem(1), 1), new ItemStack(IUItem.iuingot.getStack(25), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore.getItem(15), 1), new ItemStack(IUItem.iuingot.getStack(16), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore1.getItem(14), 1), new ItemStack(IUItem.iuingot.getStack(14), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore1.getItem(0), 1), new ItemStack(IUItem.iuingot.getStack(0), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(14), 1), new ItemStack(IUItem.iuingot.getStack(37), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(13), 1), new ItemStack(IUItem.iuingot.getStack(17), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore1.getItem(7), 1), new ItemStack(IUItem.iuingot.getStack(17), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore1.getItem(4), 1), new ItemStack(IUItem.iuingot.getStack(3), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore.getItem(9), 1), new ItemStack(IUItem.iuingot.getStack(15), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore.getItem(2), 1), new ItemStack(IUItem.iuingot.getStack(27), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(1), 1), new ItemStack(IUItem.iuingot.getStack(11), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore3.getItem(2), 1), new ItemStack(IUItem.iuingot.getStack(2), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore1.getItem(12), 1), new ItemStack(IUItem.iuingot.getStack(30), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(15), 1), new ItemStack(IUItem.iuingot.getStack(38), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore.getItem(6), 1), new ItemStack(Items.COPPER_INGOT), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore3.getItem(5), 1), new ItemStack(IUItem.iuingot.getStack(8), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore3.getItem(6), 1), new ItemStack(IUItem.iuingot.getStack(10), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore3.getItem(7), 1), new ItemStack(IUItem.iuingot.getStack(39), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore3.getItem(8), 1), new ItemStack(IUItem.iuingot.getStack(34), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore3.getItem(10), 1), new ItemStack(IUItem.iuingot.getStack(40), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(9), 1), new ItemStack(IUItem.iuingot.getStack(33), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(10), 1), new ItemStack(IUItem.iuingot.getStack(26), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore3.getItem(9), 1), new ItemStack(IUItem.iuingot.getStack(36), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore3.getItem(3), 1), new ItemStack(IUItem.iuingot.getStack(32), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore3.getItem(11), 1), new ItemStack(IUItem.iuingot.getStack(12), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore3.getItem(4), 1), new ItemStack(IUItem.iuingot.getStack(35), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore1.getItem(10), 1), new ItemStack(Items.EMERALD), 5.0F);

        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(0)), new ItemStack(IUItem.iuingot.getItemFromMeta(0)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(1)), new ItemStack(IUItem.iuingot.getItemFromMeta(1)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(2)), new ItemStack(IUItem.iuingot.getItemFromMeta(2)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(3)), new ItemStack(IUItem.iuingot.getItemFromMeta(3)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(4)), new ItemStack(IUItem.iuingot.getItemFromMeta(6)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(5)), new ItemStack(IUItem.iuingot.getItemFromMeta(7)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(6)), new ItemStack(IUItem.iuingot.getItemFromMeta(8)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(7)), new ItemStack(IUItem.iuingot.getItemFromMeta(9)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(8)), new ItemStack(IUItem.iuingot.getItemFromMeta(10)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(9)), new ItemStack(IUItem.iuingot.getItemFromMeta(11)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(10)), new ItemStack(IUItem.iuingot.getItemFromMeta(12)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(11)), new ItemStack(IUItem.iuingot.getItemFromMeta(14)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(12)), new ItemStack(IUItem.iuingot.getItemFromMeta(15)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(13)), new ItemStack(IUItem.iuingot.getItemFromMeta(16)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(14)), new ItemStack(IUItem.iuingot.getItemFromMeta(17)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(15)), new ItemStack(IUItem.iuingot.getItemFromMeta(18)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(22)), new ItemStack(IUItem.iuingot.getItemFromMeta(25)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(23)), new ItemStack(IUItem.iuingot.getItemFromMeta(26)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getItemFromMeta(24)), new ItemStack(IUItem.iuingot.getItemFromMeta(27)), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore1.getItem(15), 1), new ItemStack(IUItem.iuingot.getStack(43), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(3), 1), new ItemStack(IUItem.iuingot.getStack(44), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore.getItem(0), 1), new ItemStack(IUItem.iuingot.getStack(45), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(6), 1), new ItemStack(IUItem.iuingot.getStack(46), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore2.getItem(2), 1), new ItemStack(IUItem.iuingot.getStack(47), 1), 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.space_ore.getItem(10), 1), new ItemStack(IUItem.iuingot.getStack(48), 1), 0.6f);


        for (int i = 0; i < 15; i++) {
            FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getStack(i + 25), 1), new ItemStack(IUItem.iuingot.getStack(28 + i), 1), 0.6f);

        }
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getStack(19), 1), IUItem.leadIngot, 0.6f);
        FurnaceProvider.addSmelting(new ItemStack(IUItem.rawMetals.getStack(20), 1), IUItem.tinIngot, 0.6f);


        FurnaceProvider.addSmelting(IUItem.rawcrystalmemory, new ItemStack(IUItem.crystalMemory.getItem()), 0.1f);
        FurnaceProvider.addSmelting(IUItem.latex, IUItem.rubber, 0.1f);
        FurnaceProvider.addSmelting(  ItemStackHelper.fromData(IUItem.crafting_elements, 1, 773),  ItemStackHelper.fromData(IUItem.crafting_elements, 1, 772), 0.1f);
        FurnaceProvider.addSmelting(  ItemStackHelper.fromData(IUItem.crafting_elements, 1, 770),  ItemStackHelper.fromData(IUItem.crafting_elements, 1, 771), 0.1f);


        for (int i = 0; i < 32; i++) {
            FurnaceProvider.addSmelting(new ItemStack(IUItem.alloysdust.getStack(i), 1), new ItemStack(IUItem.alloysingot.getStack(i), 1), 0.6f);
        }

    }
}
