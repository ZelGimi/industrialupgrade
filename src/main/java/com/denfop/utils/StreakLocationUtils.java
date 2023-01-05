package com.denfop.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class StreakLocationUtils {

    public double posX;
    public double posY;
    public double posZ;
    public boolean isSprinting;
    public long lastTick;
    public float height;
    public double startU;
    public BlockPos pos;
    public float renderYawOffset;

    public StreakLocationUtils(final EntityPlayer player) {
        this.update(player);
    }

    public void update(final EntityPlayer player) {
        this.posX = player.posX;
        this.posY = player.getEntityBoundingBox().minY;
        this.posZ = player.posZ;
        this.isSprinting = player.isSprinting();
        this.lastTick = player.getEntityWorld().getWorldTime();
        this.height = player.height;
        this.pos = player.getPosition();
        this.renderYawOffset = player.renderYawOffset;
    }

    public boolean hasSameCoords(final StreakLocationUtils loc) {
        return loc.posX == this.posX && loc.posY == this.posY && loc.posZ == this.posZ && loc.height == this.height;
    }

}
