package com.denfop.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class StreakLocation {

    public double posX;
    public double posY;
    public double posZ;
    public boolean isSprinting;
    public long lastTick;
    public float height;
    public double startU;
    public BlockPos pos;
    public float renderYawOffset;

    public StreakLocation(final EntityPlayer player) {
        this.update(player);
    }

    public void update(final EntityPlayer player) {
        this.posX = player.posX;
        this.posY = player.boundingBox.minY;
        this.posZ = player.posZ;
        this.isSprinting = player.isSprinting();
        this.lastTick = player.getEntityWorld().getWorldTime();
        this.height = player.height;
        this.pos = new BlockPos(this.posX, this.posY, this.posZ);
        this.renderYawOffset = player.rotationYaw;
    }

    public boolean hasSameCoords(final StreakLocation loc) {
        return loc.posX == this.posX && loc.posY == this.posY && loc.posZ == this.posZ && loc.height == this.height;
    }

}
