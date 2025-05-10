package com.denfop.utils;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class Keyboard {
    public static final int KEY_LSHIFT = InputConstants.KEY_LSHIFT;
    public static final int KEY_LEFT = InputConstants.KEY_LEFT;
    public static final int KEY_RIGHT = InputConstants.KEY_RIGHT;
    public static final int KEY_LCONTROL = InputConstants.KEY_LCONTROL;
    public static final int KEY_LALT = InputConstants.KEY_LALT;

    public static boolean isKeyDown(int keyMapping) {

        if (FMLEnvironment.dist.isDedicatedServer()) {

            return true;
        } else {

            return clientIsKeyDown(keyMapping);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean clientIsKeyDown(int keyMapping) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keyMapping);
    }
}
