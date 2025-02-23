package com.denfop.api.space.upgrades.info;

import com.denfop.Localization;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.utils.ModUtils;
import net.minecraft.util.text.TextFormatting;

public class SpaceUpgradeItemInform {

    public final EnumTypeUpgrade upgrade;
    public final int number;

    public SpaceUpgradeItemInform(EnumTypeUpgrade modules, int number) {
        this.upgrade = modules;
        this.number = number;

    }

    public boolean matched(EnumTypeUpgrade modules) {
        return this.upgrade.getUpgrade().equals(modules.getUpgrade());
    }

    public int getInformation(EnumTypeUpgrade modules) {
        if (this.upgrade.getUpgrade().equals(modules.getUpgrade())) {
            return this.number;
        }
        return 0;
    }

    public String getName() {
        switch (this.upgrade) {
            case SOLAR:
                return TextFormatting.YELLOW + Localization.translate("iu.spaceupgrade.solar") + TextFormatting.GREEN + ModUtils.getString((30 * this.number));
            case DRILL:
                return TextFormatting.AQUA + Localization.translate("iu.spaceupgrade.drill") + TextFormatting.GREEN + ModUtils.getString(this.number);
            case PROTECTION:
                return TextFormatting.GOLD + Localization.translate("protect") + TextFormatting.GREEN + ModUtils.getString(0.2 * this.number * 100) + "%";
            case COOLER:
                return TextFormatting.LIGHT_PURPLE + Localization.translate("iu.spaceupgrade.cool") + TextFormatting.GREEN + ModUtils.getString(
                         this.number * -50)  + "C°";
            case ENGINE:
                return TextFormatting.RED + Localization.translate("iu.spaceupgrade.engine") + TextFormatting.GREEN + ModUtils.getString(
                        this.number * 12.5) + "%";
            case HEATER:
                return TextFormatting.RED + Localization.translate("iu.spaceupgrade.heat") + TextFormatting.GREEN + ModUtils.getString(200 * this.number) + "C°";
            case PRESSURE:
                return TextFormatting.GOLD + Localization.translate("iu.spaceupgrade.pressure");
        }
        return "";
    }


}
