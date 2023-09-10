package com.denfop.integration.mets;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class METSIntegration {

    public static void init() {


    }

    private static ItemStack getAllTypeStack(Item item) {
        return new ItemStack(item, 1, 32767);
    }

}
