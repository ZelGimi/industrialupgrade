package com.denfop.api.windsystem.upgrade;

import com.denfop.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.util.text.TextFormatting;

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
                return TextFormatting.YELLOW + Localization.translate("wind.strength_info") + TextFormatting.GREEN + ModUtils.getString(
                        (this.upgrade.getCoef() * this.number) * 100) + "%";
            case POWER_I:
            case POWER_II:
            case POWER_III:
                return TextFormatting.AQUA + Localization.translate("wind.power_info") + TextFormatting.GREEN + ModUtils.getString(
                        this.upgrade.getCoef() * this.number * 100) + "%";
            case EFFICIENCY_I:
            case EFFICIENCY_II:
            case EFFICIENCY_III:
                return TextFormatting.GOLD + Localization.translate("wind.efficiency_info") + TextFormatting.GREEN + ModUtils.getString(
                        this.upgrade.getCoef() * this.number * 100) + "%";
            case AUTO:
                return TextFormatting.LIGHT_PURPLE + Localization.translate("wind.auto_info");
            case MIN:
                return TextFormatting.RED + Localization.translate("wind.min_info");
            case WIND_I:
            case WIND_II:
            case WIND_III:
                return TextFormatting.BLUE + Localization.translate("wind.wind_speed_info") + TextFormatting.GREEN + (int) this.upgrade.getCoef() + " m/s";
            case WIND_POWER_II:
            case WIND_POWER_I:
            case WIND_POWER_III:
                return TextFormatting.DARK_AQUA + Localization.translate("wind.wind_power_info") + TextFormatting.GREEN + (int) this.upgrade.getCoef();
            case REPAIR_II:
            case REPAIR_I:
            case REPAIR_III:
                return TextFormatting.DARK_PURPLE + Localization.translate("wind.rotor_repair_info") + TextFormatting.GREEN + (int) this.upgrade.getCoef() + " " + Localization.translate(
                        "iu.seconds");

        }
        return "";
    }


}
