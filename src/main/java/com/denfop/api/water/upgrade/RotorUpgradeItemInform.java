package com.denfop.api.water.upgrade;

import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class RotorUpgradeItemInform {

    public static final Codec<RotorUpgradeItemInform> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.xmap(EnumInfoRotorUpgradeModules::valueOf, EnumInfoRotorUpgradeModules::name).fieldOf("upgrade").forGetter(obj -> obj.upgrade),
            Codec.INT.fieldOf("number").forGetter(obj -> obj.number)
    ).apply(instance, RotorUpgradeItemInform::new));
    public static final StreamCodec<FriendlyByteBuf, RotorUpgradeItemInform> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeEnum(value.upgrade);
                buf.writeInt(value.number);
            },
            buf -> new RotorUpgradeItemInform(buf.readEnum(EnumInfoRotorUpgradeModules.class), buf.readInt())
    );
    public final EnumInfoRotorUpgradeModules upgrade;
    public int number;


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
                return ChatFormatting.LIGHT_PURPLE + Localization.translate("water.auto_info");
            case WIND_I:
            case WIND_II:
            case WIND_III:
                return ChatFormatting.BLUE + Localization.translate("water.water_speed_info") + ChatFormatting.GREEN + (int) this.upgrade.getCoef() + " m/s";
            case REPAIR_II:
            case REPAIR_I:
            case REPAIR_III:
                return ChatFormatting.DARK_PURPLE + Localization.translate("wind.rotor_repair_info") + ChatFormatting.GREEN + (int) this.upgrade.getCoef() + " " + Localization.translate(
                        "iu.seconds");
            case OCEAN:
                return ChatFormatting.LIGHT_PURPLE + Localization.translate("water.ocean_info");


        }
        return "";
    }


}
