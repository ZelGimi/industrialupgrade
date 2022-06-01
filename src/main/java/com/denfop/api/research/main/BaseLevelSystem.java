package com.denfop.api.research.main;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class BaseLevelSystem implements ILevelSystem {

    public final EntityPlayer player;
    public final int[] levels;
    public final int[] levels_points;
    public int[] levels_next;

    public BaseLevelSystem(EntityPlayer player) {
        this.player = player;
        this.levels = new int[EnumLeveling.values().length];
        this.levels_points = new int[EnumLeveling.values().length];
        this.levels_next = new int[EnumLeveling.values().length];
        this.init();

    }

    private void init() {
        final NBTTagCompound nbt = player.getEntityData();
        for (EnumLeveling leveling : EnumLeveling.values()) {
            this.levels[leveling.ordinal()] = nbt.getInteger("iu.level." + leveling.name().toLowerCase());
            this.levels_points[leveling.ordinal()] = nbt.getInteger("iu.level_points." + leveling.name().toLowerCase());
            this.levels_next[leveling.ordinal()] = ((this.levels_points[leveling.ordinal()] * 40 + 20) - this.levels[leveling.ordinal()]);
        }
    }

    @Override
    public int getLevel(final EnumLeveling enumLeveling) {
        return this.levels_points[enumLeveling.ordinal()];
    }

    @Override
    public void setLevel(final EnumLeveling enumLeveling, final int level) {
        this.levels[enumLeveling.ordinal()] = level;
        final NBTTagCompound nbt = player.getEntityData();
        nbt.setInteger("iu.level." + enumLeveling.name().toLowerCase(), this.levels[enumLeveling.ordinal()]);
        this.levels_points[enumLeveling.ordinal()] = getLevelPoint(enumLeveling);
        if (enumLeveling != EnumLeveling.BASE) {
            this.setOwnBaseLevel(enumLeveling, level);
        }
    }

    @Override
    public void addLevel(final EnumLeveling enumLeveling, final int level) {
        this.levels[enumLeveling.ordinal()] += level;
        final NBTTagCompound nbt = player.getEntityData();
        while (this.levels[enumLeveling.ordinal()] > (this.levels_points[enumLeveling.ordinal()] * 40 + 20)) {
            this.levels[enumLeveling.ordinal()] -= (this.levels_points[enumLeveling.ordinal()] * 40 + 20);
            this.levels_points[enumLeveling.ordinal()]++;
            this.levels_next[enumLeveling.ordinal()] = ((this.levels_points[enumLeveling.ordinal()] * 40 + 20) - this.levels[enumLeveling.ordinal()]);

        }
        nbt.setInteger("iu.level." + enumLeveling.name().toLowerCase(), this.levels[enumLeveling.ordinal()]);
        nbt.setInteger("iu.level_points." + enumLeveling.name().toLowerCase(), this.levels_points[enumLeveling.ordinal()]);
        if (enumLeveling != EnumLeveling.BASE && enumLeveling != EnumLeveling.PVP) {
            this.setOwnBaseLevel(enumLeveling, level);
        }
    }

    @Override
    public int getLevelPoint(final EnumLeveling enumLeveling) {
        return this.levels_points[enumLeveling.ordinal()];
    }

    @Override
    public double getBar(final EnumLeveling enumLeveling, final int length) {
        return Math.min(
                length * this.levels[enumLeveling.ordinal()] / (this.levels_points[enumLeveling.ordinal()] * 40 + 20),
                length
        );
    }


    @Override
    public void setOwnBaseLevel(EnumLeveling enumLeveling, int level) {
        if (EnumLeveling.PRACTICE == enumLeveling) {
            level /= 10;
        } else {
            level /= 5;
        }

        this.addLevel(EnumLeveling.BASE, level);
    }


}
