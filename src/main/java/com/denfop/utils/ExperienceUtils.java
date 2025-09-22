package com.denfop.utils;

import net.minecraft.entity.player.EntityPlayer;

public class ExperienceUtils {

    public ExperienceUtils() {
    }

    public static int getPlayerXP(EntityPlayer player) {
        return (int) ((float) getExperienceForLevel(player.experienceLevel) + player.experience * (float) player.xpBarCap());
    }

    public static void addPlayerXP(EntityPlayer player, int amount) {
        player.addScore(amount);
        int i = Integer.MAX_VALUE - player.experienceTotal;

        if (amount > i) {
            amount = i;
        }

        player.experience += (float) amount / (float) player.xpBarCap();

        for (player.experienceTotal += amount; player.experience >= 1.0F; player.experience /= (float) player.xpBarCap()) {
            player.experience = (player.experience - 1.0F) * (float) player.xpBarCap();
            player.addExperienceLevel(1);
        }
    }

    public static int addPlayerXP1(EntityPlayer player, int amount) {

        if (((double) getPlayerXP(player) + (double) amount) < Integer.MAX_VALUE) {
            int experience = getPlayerXP(player) + amount;
            player.experienceTotal = experience;
            player.experienceLevel = getLevelForExperience(experience);
            int expForLevel = getExperienceForLevel(player.experienceLevel);
            player.experience = (float) (experience - expForLevel) / (float) player.xpBarCap();
            return 0;
        } else {
            int temp = Integer.MAX_VALUE - getPlayerXP(player);
            int experience = Integer.MAX_VALUE - 1;

            player.experienceLevel = getLevelForExperience(experience);
            int expForLevel = getExperienceForLevel(player.experienceLevel);
            player.experience = (float) (experience - expForLevel) / (float) player.xpBarCap();
            return temp;
        }

    }

    public static int removePlayerXP(EntityPlayer player, int maxStorage, int storage) {
        int experience = getPlayerXP(player);
        if (experience > 0) {
            int tmp = maxStorage - storage;

            storage += Math.min(experience, tmp);
            experience -= Math.min(experience, tmp);

        }
        player.experienceTotal = experience;
        player.experienceLevel = getLevelForExperience(experience);
        int expForLevel = getExperienceForLevel(player.experienceLevel);
        player.experience = (float) (experience - expForLevel) / (float) player.xpBarCap();
        return storage;
    }

    public static double removePlayerXP(EntityPlayer player, double maxStorage, double storage) {
        int experience = getPlayerXP(player);
        if (experience > 0) {
            double tmp = maxStorage - storage;

            storage += Math.min(experience, tmp);
            experience -= Math.min(experience, tmp);

        }
        player.experienceTotal = experience;
        player.experienceLevel = getLevelForExperience(experience);
        int expForLevel = getExperienceForLevel(player.experienceLevel);
        player.experience = (float) (experience - expForLevel) / (float) player.xpBarCap();
        return storage;
    }

    public static int getExperienceForLevel(int level) {
        return level == 0
                ? 0
                : (level > 0 && level < 16 ? level * 17 : (level > 15 && level < 31 ? (int) (1.5D * Math.pow(
                        level,
                        2.0D
                ) - 29.5D * (double) level + 360.0D) : (int) (3.5D * Math.pow(
                        level,
                        2.0D
                ) - 151.5D * (double) level + 2220.0D)));
    }

    public static int getLevelForExperience(int experience) {
        if (experience <= 0) {
            return 0;
        }
        int i = 0;
        while (getExperienceForLevel(i) <= experience) {
            ++i;
        }

        return i - 1;
    }

}
