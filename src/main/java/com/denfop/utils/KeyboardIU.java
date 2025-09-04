package com.denfop.utils;


import com.denfop.api.IKeyboard;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

import java.util.*;

public class KeyboardIU implements IKeyboard {

    private final Map<Player, Set<KeyboardIU.Key>> playerKeys = new WeakHashMap<>();

    public KeyboardIU() {
    }

    public static boolean keyDown(KeyMapping keyMapping) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keyMapping.getKey().getValue());
    }

    public static boolean isKeyDown(int keyMapping) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keyMapping);
    }

    public boolean isJumpKeyDown(Player player) {
        return this.get(player, Key.JUMP);
    }

    public boolean isAltKeyDown(Player player) {
        return this.get(player, Key.ALT);
    }

    public boolean isArmorKey(Player player) {
        return this.get(player, Key.ARMOR);
    }

    public boolean isSneakKeyDown(Player player) {
        return player.isShiftKeyDown();
    }

    public boolean isForwardKeyDown(Player player) {
        return this.get(player, KeyboardIU.Key.FORWARD);
    }

    public boolean isChangeKeyDown(Player player) {
        return this.get(player, KeyboardIU.Key.CHANGE);
    }

    public boolean isVerticalMode(Player player) {
        return this.get(player, Key.VERTICALMODE);
    }

    public boolean isBootsMode(Player player) {
        return this.get(player, Key.BOOTS);
    }

    public boolean isLeggingsMode(Player player) {
        return this.get(player, Key.LEGGINGS);
    }


    public boolean isFlyModeKeyDown(Player player) {
        return this.get(player, KeyboardIU.Key.FLYMODE);
    }

    @Override
    public boolean isBlackListModeKeyDown(final Player player) {
        return this.get(player, Key.BLACKMODE);
    }

    @Override
    public boolean isBlackListModeViewKeyDown(final Player player) {
        return this.get(player, Key.BLACKLISTVIEWMODE);
    }


    public boolean isSaveModeKeyDown(Player player) {
        return this.get(player, Key.SAVEMODE);
    }

    public void sendKeyUpdate() {
    }

    public void processKeyUpdate(Player player, int keyState) {
        this.playerKeys.put(player, KeyboardIU.Key.fromInt(keyState));
    }

    public void removePlayerReferences(Player player) {
        this.playerKeys.remove(player);
    }

    private boolean get(Player player, KeyboardIU.Key key) {
        Set<KeyboardIU.Key> keys = this.playerKeys.get(player);
        return keys != null && keys.contains(key);
    }

    @Override
    public boolean isStreakKeyDown(final Player player) {
        return this.get(player, Key.STREAK);
    }

    public void register(RegisterKeyMappingsEvent event) {
    }


    public enum Key {
        CHANGE,
        BLACKLISTVIEWMODE,
        FLYMODE,
        VERTICALMODE,
        SAVEMODE,
        SHIFT,
        STREAK,
        BLACKMODE,
        FORWARD,
        JUMP,
        ARMOR,
        BOOTS,
        LEGGINGS,
        ALT;


        public static final KeyboardIU.Key[] keys = values();

        Key() {
        }

        public static int toInt(Iterable<KeyboardIU.Key> keySet) {
            int ret = 0;

            KeyboardIU.Key key;
            for (Iterator var2 = keySet.iterator(); var2.hasNext(); ret |= 1 << key.ordinal()) {
                key = (KeyboardIU.Key) var2.next();
            }

            return ret;
        }

        public static Set<KeyboardIU.Key> fromInt(int keyState) {
            Set<KeyboardIU.Key> ret = EnumSet.noneOf(KeyboardIU.Key.class);

            for (int i = 0; keyState != 0; keyState >>= 1) {
                if ((keyState & 1) != 0) {
                    ret.add(keys[i]);
                }

                ++i;
            }

            return ret;
        }
    }

}
