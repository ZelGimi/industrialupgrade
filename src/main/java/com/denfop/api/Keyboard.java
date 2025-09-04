package com.denfop.api;


import net.minecraft.world.entity.player.Player;

public interface Keyboard {

    boolean isChangeKeyDown(final Player player);

    boolean isVerticalMode(final Player player);

    boolean isForwardKeyDown(Player player);

    boolean isJumpKeyDown(Player player);

    boolean isArmorKey(Player player);

    boolean isSneakKeyDown(Player player);

    boolean isFlyModeKeyDown(final Player player);

    boolean isBlackListModeKeyDown(final Player player);

    boolean isBlackListModeViewKeyDown(final Player player);

    boolean isStreakKeyDown(final Player player);

    boolean isBootsMode(Player player);

    boolean isLeggingsMode(Player player);


}
