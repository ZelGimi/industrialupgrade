package com.denfop.api.space.upgrades.info;

import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class SpaceUpgradeItemInform {

    public static final Codec<SpaceUpgradeItemInform> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.xmap(EnumTypeUpgrade::valueOf, EnumTypeUpgrade::name).fieldOf("upgrade").forGetter(obj -> obj.upgrade),
            Codec.INT.fieldOf("number").forGetter(obj -> obj.number)
    ).apply(instance, SpaceUpgradeItemInform::new));
    public static final StreamCodec<FriendlyByteBuf, SpaceUpgradeItemInform> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeEnum(value.upgrade);
                buf.writeInt(value.number);
            },
            buf -> new SpaceUpgradeItemInform(buf.readEnum(EnumTypeUpgrade.class), buf.readInt())
    );
    public final EnumTypeUpgrade upgrade;
    public int number;


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
                return ChatFormatting.YELLOW + Localization.translate("iu.spaceupgrade.solar") + ChatFormatting.GREEN + ModUtils.getString((30 * this.number));
            case DRILL:
                return ChatFormatting.AQUA + Localization.translate("iu.spaceupgrade.drill") + ChatFormatting.GREEN + ModUtils.getString(this.number);
            case PROTECTION:
                return ChatFormatting.GOLD + Localization.translate("protect") + ChatFormatting.GREEN + ModUtils.getString(0.2 * this.number * 100) + "%";
            case COOLER:
                return ChatFormatting.LIGHT_PURPLE + Localization.translate("iu.spaceupgrade.cool") + ChatFormatting.GREEN + ModUtils.getString(
                        this.number * -37) + "C°";
            case ENGINE:
                return ChatFormatting.RED + Localization.translate("iu.spaceupgrade.engine") + ChatFormatting.GREEN + ModUtils.getString(
                        this.number * 12.5) + "%";
            case HEATER:
                return ChatFormatting.RED + Localization.translate("iu.spaceupgrade.heat") + ChatFormatting.GREEN + ModUtils.getString(350 * this.number) + "C°";
            case PRESSURE:
                return ChatFormatting.GOLD + Localization.translate("iu.spaceupgrade.pressure");
        }
        return "";
    }


}
