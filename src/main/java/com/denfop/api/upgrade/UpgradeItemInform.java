package com.denfop.api.upgrade;

import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import net.minecraft.util.text.TextFormatting;

public class UpgradeItemInform {

    public final EnumInfoUpgradeModules upgrade;
    public final int number;

    public UpgradeItemInform(EnumInfoUpgradeModules modules, int number) {
        this.upgrade = modules;
        this.number = number;

    }

    public boolean matched(EnumInfoUpgradeModules modules) {
        return this.upgrade.name.equals(modules.name);
    }

    public int getInformation(EnumInfoUpgradeModules modules) {
        if (this.upgrade.name.equals(modules.name)) {
            return this.number;
        }
        return 0;
    }

    public String getName() {
        switch (this.upgrade) {
            case GENDAY:
                return TextFormatting.YELLOW + Localization.translate("genday") + TextFormatting.GREEN + ModUtils.getString((0.05 * this.number) * 100) + "%";
            case GENNIGHT:
                return TextFormatting.AQUA + Localization.translate("gennight") + TextFormatting.GREEN + ModUtils.getString(0.05 * this.number * 100) + "%";
            case PROTECTION:
                return TextFormatting.GOLD + Localization.translate("protect") + TextFormatting.GREEN + ModUtils.getString(0.2 * this.number * 100) + "%";
            case EFFICIENCY:
                return TextFormatting.LIGHT_PURPLE + Localization.translate("speed") + TextFormatting.GREEN + ModUtils.getString(
                        0.2 * this.number * 100) + "%";
            case BOWENERGY:
                return TextFormatting.RED + Localization.translate("bowenergy") + TextFormatting.GREEN + ModUtils.getString(0.1 *
                        this.number * 100) + "%";
            case SABERENERGY:
                return TextFormatting.RED + Localization.translate("saberenergy") + TextFormatting.GREEN + ModUtils.getString(0.15 * this.number * 100) + "%";
            case DIG_DEPTH:
                return TextFormatting.AQUA + Localization.translate("depth") + TextFormatting.GREEN + this.number;
            case FIRE_PROTECTION:
                return TextFormatting.GOLD + Localization.translate("fireResistance");
            case WATER:
                return TextFormatting.GOLD + Localization.translate("waterBreathing");
            case SPEED:
                return TextFormatting.GOLD + Localization.translate("moveSpeed");
            case JUMP:
                return TextFormatting.GOLD + Localization.translate("jump");
            case BOWDAMAGE:
                return TextFormatting.DARK_GREEN + Localization.translate("bowdamage") + TextFormatting.GREEN + ModUtils.getString(
                        (0.25 * this.number) * 100) + "%";
            case SABER_DAMAGE:
                return TextFormatting.DARK_AQUA + Localization.translate("saberdamage") + TextFormatting.GREEN + ModUtils.getString(
                        0.15 * this.number * 100) + "%";
            case AOE_DIG:
                return TextFormatting.BLUE + Localization.translate("aoe") + TextFormatting.GREEN + this.number;
            case FLYSPEED:
                return TextFormatting.DARK_PURPLE + Localization.translate("speedfly") + TextFormatting.GREEN + ModUtils.getString(
                        ((0.1 * this.number) / 0.2) * 100) + "%";
            case STORAGE:
                return TextFormatting.BLUE + Localization.translate("storage") + TextFormatting.GREEN + ModUtils.getString(0.05 * this.number
                        * 100) + "%";
            case ENERGY:
                return TextFormatting.RED + Localization.translate("energy_less_use") + TextFormatting.GREEN + ModUtils.getString(
                        0.25 * this.number * 100) + "%";
            case VAMPIRES:
                return TextFormatting.RED + Localization.translate("vampires") + TextFormatting.GREEN + ModUtils.getString(this.number);
            case RESISTANCE:
                return TextFormatting.GOLD + Localization.translate("resistance") + TextFormatting.GREEN + ModUtils.getString(this.number);
            case POISON:
                return TextFormatting.GREEN + Localization.translate("poison");
            case WITHER:
                return TextFormatting.BLUE + Localization.translate("wither");
            case SILK_TOUCH:
                return TextFormatting.WHITE + Localization.translate("silk");
            case INVISIBILITY:
                return TextFormatting.WHITE + Localization.translate("invisibility");
            case LOOT:
                return TextFormatting.WHITE + Localization.translate("loot") + TextFormatting.GREEN + ModUtils.getString(this.number);
            case FIRE:
                return TextFormatting.WHITE + Localization.translate("fire") + TextFormatting.GREEN + ModUtils.getString(this.number);
            case REPAIRED:
                return TextFormatting.WHITE + Localization.translate("repaired") + TextFormatting.GREEN + 0.001 * this.number + "%";
            case LUCKY:
                return TextFormatting.BLUE + Localization.translate("lucky") + TextFormatting.GREEN + this.number;
            case EFFICIENT:
                return TextFormatting.BLUE + Localization.translate("efficient") + TextFormatting.GREEN + (1 + (this.number - 1) * 2);
            case SMELTER:
                return TextFormatting.GRAY + Localization.translate("iu.smelter");
            case NIGTHVISION:
                return TextFormatting.BLUE + Localization.translate("iu.nightvision");
            case THORNS:
                return TextFormatting.GRAY + Localization.translate("iu.thorns") + TextFormatting.GREEN + this.number;
            case EXPERIENCE:
                return TextFormatting.GREEN + Localization.translate("iu.experience") + TextFormatting.GOLD + this.number * 50 + "%";
            case BLINDNESS:
                return TextFormatting.GRAY + Localization.translate("iu.blindness");
            case PROTECTION_ARROW:
                return TextFormatting.RED + Localization.translate("iu.protection_arrow") + TextFormatting.DARK_PURPLE + (1 + (this.number - 1) * 2);
            case FALLING_DAMAGE:
                return TextFormatting.WHITE + Localization.translate("iu.falling_damage") + TextFormatting.DARK_GREEN + this.number * 25 + "%";
            case MACERATOR:
                return TextFormatting.DARK_PURPLE + Localization.translate("iu.macerator");
            case COMB_MACERATOR:
                return TextFormatting.DARK_PURPLE + Localization.translate("iu.comb_macerator");
            case RANDOM:
                return TextFormatting.GREEN + Localization.translate("iu.random") + 0.001 * this.number + "%";
            case HUNGRY:
                return TextFormatting.DARK_RED + Localization.translate("iu.hungry");
            case GENERATOR:
                return TextFormatting.DARK_AQUA + Localization.translate("iu.generator");
            case FLY:
                return TextFormatting.DARK_PURPLE + Localization.translate("iu.fly_mode");
            case SIZE:
                return TextFormatting.AQUA + Localization.translate("iu.size_mode") + this.number;
            case BAGS:
                return TextFormatting.GOLD + Localization.translate("iu.bags_mode");
            case LAPPACK_ENERGY:
                return TextFormatting.LIGHT_PURPLE + Localization.translate("iu.lappack_mode") + 0.005 * this.number + "%";
            case PURIFIER:
                return TextFormatting.GRAY + Localization.translate("iu.purifier_mode");
            case LATEX:
                return TextFormatting.GOLD + Localization.translate("iu.latex_mode");
            case WRENCH:
                return TextFormatting.YELLOW + Localization.translate("iu.wrench_mode");

        }
        return "";
    }


}
