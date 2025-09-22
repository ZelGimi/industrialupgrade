package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.register.RegisterOreDictionary;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FurnaceRecipes {

    public static void recipe() {

        GameRegistry.addSmelting(new ItemStack(IUItem.blockResource, 1, 11), new ItemStack(IUItem.cultivated_peat_balls), 5);
        for (int i = 0; i < 30; i++) {
            if (i < 16) {
                GameRegistry.addSmelting(new ItemStack(IUItem.space_cobblestone, 1, i), new ItemStack(IUItem.space_stone, 1, i),
                        1
                );
            } else {
                GameRegistry.addSmelting(new ItemStack(IUItem.space_cobblestone1, 1, i - 16), new ItemStack(IUItem.space_stone1,
                                1, i - 16
                        ),
                        1
                );
            }

        }
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 0), new ItemStack(IUItem.iuingot, 1, 0), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 1), new ItemStack(IUItem.iuingot, 1, 1), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 2), new ItemStack(IUItem.iuingot, 1, 2), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 3), new ItemStack(IUItem.iuingot, 1, 3), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 4), new ItemStack(IUItem.iuingot, 1, 6), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 5), new ItemStack(IUItem.iuingot, 1, 7), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 6), new ItemStack(IUItem.iuingot, 1, 8), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 7), new ItemStack(IUItem.iuingot, 1, 9), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 8), new ItemStack(IUItem.iuingot, 1, 10), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 9), new ItemStack(IUItem.iuingot, 1, 11), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 10), new ItemStack(IUItem.iuingot, 1, 12), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 11), new ItemStack(IUItem.iuingot, 1, 14), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 12), new ItemStack(IUItem.iuingot, 1, 15), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 13), new ItemStack(IUItem.iuingot, 1, 16), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 14), new ItemStack(IUItem.iuingot, 1, 17), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore, 1, 15), new ItemStack(IUItem.iuingot, 1, 18), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore2, 1, 3), new ItemStack(IUItem.iuingot, 1, 25), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore2, 1, 4), new ItemStack(IUItem.iuingot, 1, 26), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore2, 1, 5), new ItemStack(IUItem.iuingot, 1, 27), 5.0F);
        GameRegistry.addSmelting(IUItem.leadOre, IUItem.leadIngot, 5.0F);
        GameRegistry.addSmelting(IUItem.copperOre, IUItem.copperIngot, 5.0F);
        GameRegistry.addSmelting(IUItem.tinOre, IUItem.tinIngot, 5.0F);
        for (int k = 0; k < 15; k++) {
            GameRegistry.addSmelting(new ItemStack((Block) IUItem.ore3, 1, k), new ItemStack(IUItem.iuingot, 1, 28 + k), 5.0F);
        }

        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore1, 1, 1), new ItemStack(IUItem.iuingot, 1, 1), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore3, 1, 0), new ItemStack(IUItem.iuingot, 1, 11), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore, 1, 14), new ItemStack(IUItem.iuingot, 1, 7), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore, 1, 13), new ItemStack(IUItem.iuingot, 1, 7), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore2, 1, 11), new ItemStack(IUItem.iuingot, 1, 42), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore1, 1, 13), new ItemStack(Items.GOLD_INGOT), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore, 1, 1), new ItemStack(IUItem.iuingot, 1, 29), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore2, 1, 4), new ItemStack(IUItem.iuingot, 1, 41), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore2, 1, 7), new ItemStack(IUItem.iuingot, 1, 24), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore2, 1, 8), new ItemStack(IUItem.iuingot, 1, 31), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore2, 1, 0), new ItemStack(IUItem.iuingot, 1, 6), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore, 1, 12), new ItemStack(IUItem.iuingot, 1, 9), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore2, 1, 5), new ItemStack(IUItem.iuingot, 1, 42), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore, 1, 11), new ItemStack(IUItem.iuingot, 1, 22), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore, 1, 8), new ItemStack(IUItem.iuingot, 1, 28), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore, 1, 7), new ItemStack(Items.IRON_INGOT), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore3, 1, 1), new ItemStack(IUItem.iuingot, 1, 25), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore, 1, 15), new ItemStack(IUItem.iuingot, 1, 16), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore1, 1, 14), new ItemStack(IUItem.iuingot, 1, 14), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore1, 1, 0), new ItemStack(IUItem.iuingot, 1, 0), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore2, 1, 14), new ItemStack(IUItem.iuingot, 1, 37), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore2, 1, 13), new ItemStack(IUItem.iuingot, 1, 17), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore1, 1, 7), new ItemStack(IUItem.iuingot, 1, 17), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore1, 1, 4), new ItemStack(IUItem.iuingot, 1, 3), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore, 1, 9), new ItemStack(IUItem.iuingot, 1, 15), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore, 1, 2), new ItemStack(IUItem.iuingot, 1, 27), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore2, 1, 1), new ItemStack(IUItem.iuingot, 1, 11), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore3, 1, 2), new ItemStack(IUItem.iuingot, 1, 2), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore1, 1, 12), new ItemStack(IUItem.iuingot, 1, 30), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore2, 1, 15), new ItemStack(IUItem.iuingot, 1, 38), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore, 1, 6), new ItemStack(IUItem.iuingot, 1, 21), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore3, 1, 5), new ItemStack(IUItem.iuingot, 1, 8), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore3, 1, 6), new ItemStack(IUItem.iuingot, 1, 10), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore3, 1, 7), new ItemStack(IUItem.iuingot, 1, 39), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore3, 1, 8), new ItemStack(IUItem.iuingot, 1, 34), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore3, 1, 10), new ItemStack(IUItem.iuingot, 1, 40), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore2, 1, 9), new ItemStack(IUItem.iuingot, 1, 33), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore2, 1, 10), new ItemStack(IUItem.iuingot, 1, 26), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore3, 1, 9), new ItemStack(IUItem.iuingot, 1, 36), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore3, 1, 3), new ItemStack(IUItem.iuingot, 1, 32), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore3, 1, 11), new ItemStack(IUItem.iuingot, 1, 12), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore3, 1, 4), new ItemStack(IUItem.iuingot, 1, 35), 5.0F);
        GameRegistry.addSmelting(new ItemStack((Block) IUItem.space_ore1, 1, 10), new ItemStack(Items.EMERALD), 5.0F);


        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 0), new ItemStack(IUItem.iuingot, 1, 0), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 1), new ItemStack(IUItem.iuingot, 1, 1), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 2), new ItemStack(IUItem.iuingot, 1, 2), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 3), new ItemStack(IUItem.iuingot, 1, 3), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 4), new ItemStack(IUItem.iuingot, 1, 6), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 5), new ItemStack(IUItem.iuingot, 1, 7), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 6), new ItemStack(IUItem.iuingot, 1, 8), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 7), new ItemStack(IUItem.iuingot, 1, 9), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 8), new ItemStack(IUItem.iuingot, 1, 10), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 9), new ItemStack(IUItem.iuingot, 1, 11), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 10), new ItemStack(IUItem.iuingot, 1, 12), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 11), new ItemStack(IUItem.iuingot, 1, 14), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 12), new ItemStack(IUItem.iuingot, 1, 15), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 13), new ItemStack(IUItem.iuingot, 1, 16), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 14), new ItemStack(IUItem.iuingot, 1, 17), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 15), new ItemStack(IUItem.iuingot, 1, 18), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 22), new ItemStack(IUItem.iuingot, 1, 25), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 23), new ItemStack(IUItem.iuingot, 1, 26), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 24), new ItemStack(IUItem.iuingot, 1, 27), 5);

        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 18), new ItemStack(Items.IRON_INGOT), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 17), new ItemStack(Items.GOLD_INGOT), 5);


        GameRegistry.addSmelting(new ItemStack(IUItem.space_ore1, 1, 15), new ItemStack(IUItem.iuingot, 1, 43), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.space_ore2, 1, 3), new ItemStack(IUItem.iuingot, 1, 44), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.space_ore, 1, 0), new ItemStack(IUItem.iuingot, 1, 45), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.space_ore2, 1, 6), new ItemStack(IUItem.iuingot, 1, 46), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.space_ore2, 1, 2), new ItemStack(IUItem.iuingot, 1, 47), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.space_ore, 1, 10), new ItemStack(IUItem.iuingot, 1, 48), 5);


        for (int i = 0; i < 15; i++) {
            GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, i + 25), new ItemStack(IUItem.iuingot, 1, 28 + i), 5);

        }
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 19), IUItem.leadIngot, 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 16), IUItem.copperIngot, 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.rawMetals, 1, 20), IUItem.tinIngot, 5);


        GameRegistry.addSmelting(IUItem.rawcrystalmemory, new ItemStack(IUItem.crystalMemory), 5);
        GameRegistry.addSmelting(IUItem.latex, IUItem.rubber, 5);


        for (int i = 0; i < RegisterOreDictionary.itemNames1().size(); i++) {
            GameRegistry.addSmelting(new ItemStack(IUItem.alloysdust, 1, i), new ItemStack(IUItem.alloysingot, 1, i), 5);
        }


    }

}
