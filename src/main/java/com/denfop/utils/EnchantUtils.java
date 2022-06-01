package com.denfop.utils;

import com.brandon3055.draconicevolution.magic.EnchantmentReaper;
import net.minecraft.item.ItemStack;

public class EnchantUtils {

    public static void addEnchant(ItemStack stack, int reaper) {
        stack.addEnchantment(EnchantmentReaper.instance, reaper);

    }

}
