package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.register.RegisterOreDict;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FurnaceRecipes {

    public static void recipe() {

        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 0), new ItemStack(IUItem.iuingot, 1, 0), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 1), new ItemStack(IUItem.iuingot, 1, 1), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 2), new ItemStack(IUItem.iuingot, 1, 2), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 3), new ItemStack(IUItem.iuingot, 1, 3), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 4), new ItemStack(IUItem.iuingot, 1, 6), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 5), new ItemStack(IUItem.iuingot, 1, 7), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 6), new ItemStack(IUItem.iuingot, 1, 8), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 7), new ItemStack(IUItem.iuingot, 1, 9), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 8), new ItemStack(IUItem.iuingot, 1, 10), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 9), new ItemStack(IUItem.iuingot, 1, 11), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 10), new ItemStack(IUItem.iuingot, 1, 12), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 11), new ItemStack(IUItem.iuingot, 1, 14), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 12), new ItemStack(IUItem.iuingot, 1, 15), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 13), new ItemStack(IUItem.iuingot, 1, 16), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 14), new ItemStack(IUItem.iuingot, 1, 17), 5);
        GameRegistry.addSmelting(new ItemStack(IUItem.ore, 1, 15), new ItemStack(IUItem.iuingot, 1, 18), 5);

        for (int i = 0; i < RegisterOreDict.itemNames().size(); i++) {
            if (i != 4 && i != 5 && i != 13) {
                GameRegistry.addSmelting(new ItemStack(IUItem.crushed, 1, i), new ItemStack(IUItem.iuingot, 1, i), 5);
            }
        }
        for (int i = 0; i < RegisterOreDict.itemNames().size(); i++) {
            GameRegistry.addSmelting(new ItemStack(IUItem.iudust, 1, i), new ItemStack(IUItem.iuingot, 1, i), 5);
        }

        for (int i = 0; i < RegisterOreDict.itemNames1().size(); i++) {
            GameRegistry.addSmelting(new ItemStack(IUItem.alloysdust, 1, i), new ItemStack(IUItem.alloysingot, 1, i), 5);
        }

        GameRegistry.addSmelting(new ItemStack(IUItem.toriyore, 1), new ItemStack(IUItem.toriy, 1), 5);
        for (int i = 0; i < 3; i++) {
            GameRegistry.addSmelting(new ItemStack(IUItem.radiationore, 1, i), new ItemStack(IUItem.radiationresources, 1, i), 5);
        }


    }

}
