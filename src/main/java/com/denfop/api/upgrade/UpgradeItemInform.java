package com.denfop.api.upgrade;

import com.denfop.Localization;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.utils.ModUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class UpgradeItemInform {

    public static final Codec<UpgradeItemInform> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.xmap(EnumInfoUpgradeModules::valueOf, EnumInfoUpgradeModules::name).fieldOf("upgrade").forGetter(obj -> obj.upgrade),
            Codec.INT.fieldOf("number").forGetter(obj -> obj.number)
    ).apply(instance, UpgradeItemInform::new));
    public static final StreamCodec<FriendlyByteBuf, UpgradeItemInform> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeEnum(value.upgrade);
                buf.writeInt(value.number);
            },
            buf -> new UpgradeItemInform(buf.readEnum(EnumInfoUpgradeModules.class), buf.readInt())
    );
    public final EnumInfoUpgradeModules upgrade;
    public int number;


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
                return ChatFormatting.YELLOW + Localization.translate("genday") + ChatFormatting.GREEN + ModUtils.getString((0.05 * this.number) * 100) + "%";
            case GENNIGHT:
                return ChatFormatting.AQUA + Localization.translate("gennight") + ChatFormatting.GREEN + ModUtils.getString(0.05 * this.number * 100) + "%";
            case PROTECTION:
                return ChatFormatting.GOLD + Localization.translate("protect") + ChatFormatting.GREEN + ModUtils.getString(0.2 * this.number * 100) + "%";
            case EFFICIENCY:
                return ChatFormatting.LIGHT_PURPLE + Localization.translate("speed") + ChatFormatting.GREEN + ModUtils.getString(
                        0.2 * this.number * 100) + "%";
            case BOWENERGY:
                return ChatFormatting.RED + Localization.translate("bowenergy") + ChatFormatting.GREEN + ModUtils.getString(0.1 *
                        this.number * 100) + "%";
            case SABERENERGY:
                return ChatFormatting.RED + Localization.translate("saberenergy") + ChatFormatting.GREEN + ModUtils.getString(0.15 * this.number * 100) + "%";
            case DIG_DEPTH:
                return ChatFormatting.AQUA + Localization.translate("depth") + ChatFormatting.GREEN + this.number;
            case FIRE_PROTECTION:
                return ChatFormatting.GOLD + Localization.translate("fireResistance");
            case WATER:
                return ChatFormatting.GOLD + Localization.translate("waterBreathing");
            case SPEED:
                return ChatFormatting.GOLD + Localization.translate("moveSpeed");
            case JUMP:
                return ChatFormatting.GOLD + Localization.translate("jump");
            case BOWDAMAGE:
                return ChatFormatting.DARK_GREEN + Localization.translate("bowdamage") + ChatFormatting.GREEN + ModUtils.getString(
                        (0.25 * this.number) * 100) + "%";
            case SABER_DAMAGE:
                return ChatFormatting.DARK_AQUA + Localization.translate("saberdamage") + ChatFormatting.GREEN + ModUtils.getString(
                        0.15 * this.number * 100) + "%";
            case AOE_DIG:
                return ChatFormatting.BLUE + Localization.translate("aoe") + ChatFormatting.GREEN + this.number;
            case FLYSPEED:
                return ChatFormatting.DARK_PURPLE + Localization.translate("speedfly") + ChatFormatting.GREEN + ModUtils.getString(
                        ((0.1 * this.number) / 0.2) * 100) + "%";
            case STORAGE:
                return ChatFormatting.BLUE + Localization.translate("storage") + ChatFormatting.GREEN + ModUtils.getString(0.05 * this.number
                        * 100) + "%";
            case ENERGY:
                return ChatFormatting.RED + Localization.translate("energy_less_use") + ChatFormatting.GREEN + ModUtils.getString(
                        0.25 * this.number * 100) + "%";
            case VAMPIRES:
                return ChatFormatting.RED + Localization.translate("vampires") + ChatFormatting.GREEN + ModUtils.getString(this.number);
            case RESISTANCE:
                return ChatFormatting.GOLD + Localization.translate("resistance") + ChatFormatting.GREEN + ModUtils.getString(this.number);
            case POISON:
                return ChatFormatting.GREEN + Localization.translate("poison");
            case WITHER:
                return ChatFormatting.BLUE + Localization.translate("wither");
            case SILK_TOUCH:
                return ChatFormatting.WHITE + Localization.translate("silk");
            case INVISIBILITY:
                return ChatFormatting.WHITE + Localization.translate("invisibility");
            case LOOT:
                return ChatFormatting.WHITE + Localization.translate("loot") + ChatFormatting.GREEN + ModUtils.getString(this.number);
            case FIRE:
                return ChatFormatting.WHITE + Localization.translate("fire") + ChatFormatting.GREEN + ModUtils.getString(this.number);
            case REPAIRED:
                return ChatFormatting.WHITE + Localization.translate("repaired") + ChatFormatting.GREEN + 0.001 * this.number + "%";
            case LUCKY:
                return ChatFormatting.BLUE + Localization.translate("lucky") + ChatFormatting.GREEN + this.number;
            case EFFICIENT:
                return ChatFormatting.BLUE + Localization.translate("efficient") + ChatFormatting.GREEN + (1 + (this.number - 1) * 2);
            case SMELTER:
                return ChatFormatting.GRAY + Localization.translate("iu.smelter");
            case NIGTHVISION:
                return ChatFormatting.BLUE + Localization.translate("iu.nightvision");
            case THORNS:
                return ChatFormatting.GRAY + Localization.translate("iu.thorns") + ChatFormatting.GREEN + this.number;
            case EXPERIENCE:
                return ChatFormatting.GREEN + Localization.translate("iu.experience") + ChatFormatting.GOLD + this.number * 50 + "%";
            case BLINDNESS:
                return ChatFormatting.GRAY + Localization.translate("iu.blindness");
            case PROTECTION_ARROW:
                return ChatFormatting.RED + Localization.translate("iu.protection_arrow") + ChatFormatting.DARK_PURPLE + (1 + (this.number - 1) * 2);
            case FALLING_DAMAGE:
                return ChatFormatting.WHITE + Localization.translate("iu.falling_damage") + ChatFormatting.DARK_GREEN + this.number * 25 + "%";
            case MACERATOR:
                return ChatFormatting.DARK_PURPLE + Localization.translate("iu.macerator");
            case COMB_MACERATOR:
                return ChatFormatting.DARK_PURPLE + Localization.translate("iu.comb_macerator");
            case RANDOM:
                return ChatFormatting.GREEN + Localization.translate("iu.random") + 0.001 * this.number + "%";
            case HUNGRY:
                return ChatFormatting.DARK_RED + Localization.translate("iu.hungry");
            case GENERATOR:
                return ChatFormatting.DARK_AQUA + Localization.translate("iu.generator");
            case FLY:
                return ChatFormatting.DARK_PURPLE + Localization.translate("iu.fly_mode");
            case SIZE:
                return ChatFormatting.AQUA + Localization.translate("iu.size_mode") + this.number;
            case BAGS:
                return ChatFormatting.GOLD + Localization.translate("iu.bags_mode");
            case LAPPACK_ENERGY:
                return ChatFormatting.LIGHT_PURPLE + Localization.translate("iu.lappack_mode") + 0.005 * this.number + "%";
            case PURIFIER:
                return ChatFormatting.GRAY + Localization.translate("iu.purifier_mode");
            case LATEX:
                return ChatFormatting.GOLD + Localization.translate("iu.latex_mode");
            case WRENCH:
                return ChatFormatting.YELLOW + Localization.translate("iu.wrench_mode");

        }
        return "";
    }


}
