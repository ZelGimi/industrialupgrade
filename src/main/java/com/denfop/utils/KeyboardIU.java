package com.denfop.utils;


import com.denfop.api.IKeyboard;
import net.minecraft.entity.player.EntityPlayer;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class KeyboardIU implements IKeyboard {

    private final Map<EntityPlayer, Set<KeyboardIU.Key>> playerKeys = new WeakHashMap<>();

    public KeyboardIU() {
    }

    public boolean isJumpKeyDown(EntityPlayer player) {
        return this.get(player, Key.JUMP);
    }

    public boolean isArmorKey(EntityPlayer player) {
        return this.get(player, Key.ARMOR);
    }

    public boolean isSneakKeyDown(EntityPlayer player) {
        return player.isSneaking();
    }

    public boolean isForwardKeyDown(EntityPlayer player) {
        return this.get(player, KeyboardIU.Key.FORWARD);
    }


    public boolean isChangeKeyDown(EntityPlayer player) {
        return this.get(player, KeyboardIU.Key.CHANGE);
    }

    public boolean isVerticalMode(EntityPlayer player) {
        return this.get(player, Key.VERTICALMODE);
    }

    public boolean isBootsMode(EntityPlayer player) {
        return this.get(player, Key.BOOTS);
    }

    public boolean isLeggingsMode(EntityPlayer player) {
        return this.get(player, Key.LEGGINGS);
    }


    public boolean isFlyModeKeyDown(EntityPlayer player) {
        return this.get(player, KeyboardIU.Key.FLYMODE);
    }

    @Override
    public boolean isBlackListModeKeyDown(final EntityPlayer player) {
        return this.get(player, Key.BLACKMODE);
    }

    @Override
    public boolean isBlackListModeViewKeyDown(final EntityPlayer player) {
        return this.get(player, Key.BLACKLISTVIEWMODE);
    }


    public boolean isSaveModeKeyDown(EntityPlayer player) {
        return this.get(player, Key.SAVEMODE);
    }

    public void sendKeyUpdate() {
    }

    public void processKeyUpdate(EntityPlayer player, int keyState) {
        this.playerKeys.put(player, KeyboardIU.Key.fromInt(keyState));
    }

    public void removePlayerReferences(EntityPlayer player) {
        this.playerKeys.remove(player);
    }

    private boolean get(EntityPlayer player, KeyboardIU.Key key) {
        Set<KeyboardIU.Key> keys = this.playerKeys.get(player);
        return keys != null && keys.contains(key);
    }

    @Override
    public boolean isStreakKeyDown(final EntityPlayer player) {
        return this.get(player, Key.STREAK);
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
        LEGGINGS;


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
