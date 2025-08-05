package com.denfop.utils;

import com.denfop.IUCore;
import com.denfop.network.packet.PacketKeys;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import java.util.EnumSet;
import java.util.Set;


public class KeyboardClient extends KeyboardIU {

    public static final KeyMapping changemode = new KeyMapping("Change mode key", InputConstants.KEY_G, "IndustrialUpgrade");
    public static final KeyMapping flymode = new KeyMapping("Fly Key", InputConstants.KEY_F, "IndustrialUpgrade");
    public static final KeyMapping blacklistviewmode = new KeyMapping("BlackList View Key", InputConstants.KEY_X, "IndustrialUpgrade");
    public static final KeyMapping verticalmode = new KeyMapping("Vertical Key", InputConstants.KEY_K, "IndustrialUpgrade");
    public static final KeyMapping savemode = new KeyMapping("Save move Key", InputConstants.KEY_L, "IndustrialUpgrade");
    public static final KeyMapping blackmode = new KeyMapping("BlackList Key", InputConstants.KEY_J, "IndustrialUpgrade");
    public static final KeyMapping streakmode = new KeyMapping("Streak Key", InputConstants.KEY_V, "IndustrialUpgrade");

    public static final KeyMapping armormode = new KeyMapping("Armor Key", InputConstants.KEY_M, "IndustrialUpgrade");
    public static final KeyMapping bootsmode = new KeyMapping("Boots Key", InputConstants.KEY_B, "IndustrialUpgrade");
    public static final KeyMapping leggingsmode = new KeyMapping("Leggings Key", InputConstants.KEY_N, "IndustrialUpgrade");
    public static final KeyMapping altmode = new KeyMapping("ALT Key", InputConstants.KEY_LALT, "IndustrialUpgrade");

    private final Minecraft mc = Minecraft.getInstance();
    private int lastKeyState = 0;

    public KeyboardClient() {

    }

    public void register(RegisterKeyMappingsEvent ClientRegistry) {
        ClientRegistry.register(changemode);
        ClientRegistry.register(flymode);
        ClientRegistry.register(blacklistviewmode);
        ClientRegistry.register(verticalmode);
        ClientRegistry.register(savemode);
        ClientRegistry.register(blackmode);
        ClientRegistry.register(streakmode);
        ClientRegistry.register(armormode);
        ClientRegistry.register(bootsmode);
        ClientRegistry.register(leggingsmode);
        ClientRegistry.register(altmode);
    }


    public void sendKeyUpdate(Level level) {
        Set<Key> keys = EnumSet.noneOf(Key.class);
        Screen currentScreen = Minecraft.getInstance().screen;
        if (currentScreen == null) {
            if (keyDown(changemode)) {
                keys.add(Key.CHANGE);
            }
            if (keyDown(flymode)) {
                keys.add(Key.FLYMODE);
            }
            if (keyDown(altmode)) {
                keys.add(Key.ALT);
            }
            if (keyDown(armormode)) {
                keys.add(Key.ARMOR);
            }
            if (keyDown(this.mc.options.keySprint)) {
                keys.add(Key.FORWARD);
            }
            if (keyDown(this.mc.options.keyJump)) {
                keys.add(Key.JUMP);
            }
            if (keyDown(verticalmode)) {
                keys.add(Key.VERTICALMODE);
            }
            if (keyDown(blacklistviewmode)) {
                keys.add(Key.BLACKLISTVIEWMODE);
            }
            if (keyDown(savemode)) {
                keys.add(Key.SAVEMODE);
            }
            if (keyDown(blackmode)) {
                keys.add(Key.BLACKMODE);
            }
            if (keyDown(streakmode)) {
                keys.add(Key.STREAK);
            }
            if (keyDown(bootsmode)) {
                keys.add(Key.BOOTS);
            }
            if (keyDown(leggingsmode)) {
                keys.add(Key.LEGGINGS);
            }
        }

        int currentKeyState = Key.toInt(keys);
        if (currentKeyState != this.lastKeyState) {
            new PacketKeys(currentKeyState, level.registryAccess());
            super.processKeyUpdate(IUCore.proxy.getPlayerInstance(), currentKeyState);
            this.lastKeyState = currentKeyState;
        }

    }

}
