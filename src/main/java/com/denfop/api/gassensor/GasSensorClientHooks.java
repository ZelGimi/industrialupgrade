package com.denfop.api.gassensor;


import com.denfop.screen.GasSensorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public final class GasSensorClientHooks {

    private GasSensorClientHooks() {
    }

    public static void open(ItemStack stack) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }
        minecraft.setScreen(new GasSensorScreen(stack));
    }
}