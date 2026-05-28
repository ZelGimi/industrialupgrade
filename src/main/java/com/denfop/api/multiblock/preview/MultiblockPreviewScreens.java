package com.denfop.api.multiblock.preview;


import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class MultiblockPreviewScreens {

    private MultiblockPreviewScreens() {
    }

    public static void open(Component title, MultiblockPreviewModel model) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null) {
            minecraft.setScreen(new MultiblockPreviewScreen(title, model));
        }
    }
}