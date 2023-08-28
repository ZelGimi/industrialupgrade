package com.denfop.integration.projecte;

import com.denfop.IUItem;
import moze_intel.projecte.emc.EMCMapper;
import moze_intel.projecte.emc.SimpleStack;
import net.minecraft.item.ItemStack;

public class ProjectEIntegration {

    public static void init() {
        for (int i = 0; i < 3; i++) {
            addEmc(new ItemStack(IUItem.block, 1, i), 18000);
        }
        for (int i = 0; i < 17; i++) {
            addEmc(new ItemStack(IUItem.block1, 1, i), 18000);
        }
        for (int i = 0; i < 19; i++) {
            addEmc(new ItemStack(IUItem.nugget, 1, i), 222);
        }
        for (int i = 0; i < 19; i++) {
            addEmc(new ItemStack(IUItem.iuingot, 1, i), 2000);
        }
        for (int i = 0; i < 19; i++) {
            addEmc(new ItemStack(IUItem.plate, 1, i), 3000);
        }
        for (int i = 0; i < 19; i++) {
            addEmc(new ItemStack(IUItem.doubleplate, 1, i), 28000);
        }
        for (int i = 0; i < 19; i++) {
            addEmc(new ItemStack(IUItem.stik, 1, i), 3000);
        }
        for (int i = 0; i < 19; i++) {
            addEmc(new ItemStack(IUItem.casing, 1, i), 3000);
        }
        for (int i = 0; i < 19; i++) {
            addEmc(new ItemStack(IUItem.iudust, 1, i), 3000);
        }
        for (int i = 0; i < 19; i++) {
            addEmc(new ItemStack(IUItem.smalldust, 1, i), 333);
        }
        for (int i = 0; i < 19; i++) {
            addEmc(new ItemStack(IUItem.verysmalldust, 1, i), 37);
        }


        addEmc(new ItemStack(IUItem.alloysingot, 1, 0), 2384);
        addEmc(new ItemStack(IUItem.alloysingot, 1, 1), 4000);
        addEmc(new ItemStack(IUItem.alloysingot, 1, 2), 2128);
        addEmc(new ItemStack(IUItem.alloysingot, 1, 3), 2628);
        addEmc(new ItemStack(IUItem.alloysingot, 1, 4), 3024);
        addEmc(new ItemStack(IUItem.alloysingot, 1, 5), 6000);
        addEmc(new ItemStack(IUItem.alloysingot, 1, 6), 5024);
        addEmc(new ItemStack(IUItem.alloysingot, 1, 7), 5024);
        addEmc(new ItemStack(IUItem.alloysingot, 1, 8), 4000);
        addEmc(new ItemStack(IUItem.alloysingot, 1, 9), 2256);
    }

    public static void addEmc(ItemStack stack, long amount) {
        EMCMapper.emc.put(new SimpleStack(stack), amount);
    }

}
