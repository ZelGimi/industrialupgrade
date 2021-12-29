package com.denfop.utils;

import com.denfop.IUCore;
import ic2.core.IC2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class KeyboardClient extends KeyboardIU {

    public static final KeyBinding changemode = new KeyBinding("Change mode key", Keyboard.KEY_G, "IndustrialUpgrade");
    public static final KeyBinding flymode = new KeyBinding("Fly Key", Keyboard.KEY_F, "IndustrialUpgrade");
    public static final KeyBinding streakmode = new KeyBinding("Change Streak Key", Keyboard.KEY_X, "IndustrialUpgrade");
    public static final KeyBinding verticalmode = new KeyBinding("Vertical Key", Keyboard.KEY_K, "IndustrialUpgrade");
    public static final KeyBinding savemode = new KeyBinding("Save move Key", Keyboard.KEY_L, "IndustrialUpgrade");
    public static final KeyBinding blackmode = new KeyBinding("BlackList Key", Keyboard.KEY_J, "IndustrialUpgrade");

    private int lastKeyState = 0;

    public KeyboardClient() {
        ClientRegistry.registerKeyBinding(changemode);
        ClientRegistry.registerKeyBinding(flymode);
        ClientRegistry.registerKeyBinding(streakmode);
        ClientRegistry.registerKeyBinding(verticalmode);
        ClientRegistry.registerKeyBinding(savemode);
        ClientRegistry.registerKeyBinding(blackmode);
    }

    public void sendKeyUpdate() {
        Set<Key> keys = EnumSet.noneOf(Key.class);
        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (currentScreen == null) {
            if (GameSettings.isKeyDown(changemode)) {
                keys.add(Key.CHANGE);
            }
            if (GameSettings.isKeyDown(flymode)) {
                keys.add(Key.FLYMODE);
            }
            if (GameSettings.isKeyDown(verticalmode)) {
                keys.add(Key.VERTICALMODE);
            }
            if (GameSettings.isKeyDown(streakmode)) {
                keys.add(Key.STREAKMODE);
            }
            if (GameSettings.isKeyDown(savemode)) {
                keys.add(Key.SAVEMODE);
            }
            if (GameSettings.isKeyDown(blackmode)) {
                keys.add(Key.BLACKMODE);
            }
        }

        int currentKeyState = Key.toInt(keys);
        if (currentKeyState != this.lastKeyState) {
            IUCore.network.get(false).initiateKeyUpdate(currentKeyState);
            super.processKeyUpdate(IC2.platform.getPlayerInstance(), currentKeyState);
            this.lastKeyState = currentKeyState;
        }

    }

}
