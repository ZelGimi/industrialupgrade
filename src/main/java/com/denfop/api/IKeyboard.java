package com.denfop.api;

import net.minecraft.entity.player.EntityPlayer;

public interface IKeyboard {

    boolean isChangeKeyDown(final EntityPlayer player);

    boolean isVerticalMode(final EntityPlayer player);

    boolean isForwardKeyDown(EntityPlayer player);

    boolean isJumpKeyDown(EntityPlayer player);

    boolean isArmorKey(EntityPlayer player);

    boolean isSneakKeyDown(EntityPlayer player);

    boolean isFlyModeKeyDown(final EntityPlayer player);

    boolean isBlackListModeKeyDown(final EntityPlayer player);

    boolean isBlackListModeViewKeyDown(final EntityPlayer player);

    boolean isStreakKeyDown(final EntityPlayer player);

    boolean isBootsMode(EntityPlayer player);

    boolean isLeggingsMode(EntityPlayer player);


}
