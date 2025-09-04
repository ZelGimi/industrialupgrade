package com.denfop.api.windsystem.upgrade;

import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;

public class RotorUpgradeItemInform {

    public final EnumInfoRotorUpgradeModules upgrade;
    public final int number;

    public RotorUpgradeItemInform(EnumInfoRotorUpgradeModules modules, int number) {
        this.upgrade = modules;
        this.number = number;

    }

    public boolean matched(EnumInfoRotorUpgradeModules modules) {
        return this.upgrade.name.equals(modules.name);
    }

    public int getInformation(EnumInfoRotorUpgradeModules modules) {
        if (this.upgrade.name.equals(modules.name)) {
            return this.number;
        }
        return 0;
    }

    public String getName() {
        switch (this.upgrade) {
            case STRENGTH_I:
            case STRENGTH_II:
            case STRENGTH_III:
                return ChatFormatting.YELLOW + Localization.translate("wind.strength_info") + ChatFormatting.GREEN + ModUtils.getString(
                        (this.upgrade.getCoef() * this.number) * 100) + "%";
            case POWER_I:
            case POWER_II:
            case POWER_III:
                return ChatFormatting.AQUA + Localization.translate("wind.power_info") + ChatFormatting.GREEN + ModUtils.getString(
                        this.upgrade.getCoef() * this.number * 100) + "%";
            case EFFICIENCY_I:
            case EFFICIENCY_II:
            case EFFICIENCY_III:
                return ChatFormatting.GOLD + Localization.translate("wind.efficiency_info") + ChatFormatting.GREEN + ModUtils.getString(
                        this.upgrade.getCoef() * this.number * 100) + "%";
            case AUTO:
                return ChatFormatting.LIGHT_PURPLE + Localization.translate("wind.auto_info");
            case MIN:
                return ChatFormatting.RED + Localization.translate("wind.min_info");
            case WIND_I:
            case WIND_II:
            case WIND_III:
                return ChatFormatting.BLUE + Localization.translate("wind.wind_speed_info") + ChatFormatting.GREEN + (int) this.upgrade.getCoef() + " m/s";
            case WIND_POWER_II:
            case WIND_POWER_I:
            case WIND_POWER_III:
                return ChatFormatting.DARK_AQUA + Localization.translate("wind.wind_power_info") + ChatFormatting.GREEN + (int) this.upgrade.getCoef();
            case REPAIR_II:
            case REPAIR_I:
            case REPAIR_III:
                return ChatFormatting.DARK_PURPLE + Localization.translate("wind.rotor_repair_info") + ChatFormatting.GREEN + (int) this.upgrade.getCoef() + " " + Localization.translate(
                        "iu.seconds");

        }
        return "";
    }


}
