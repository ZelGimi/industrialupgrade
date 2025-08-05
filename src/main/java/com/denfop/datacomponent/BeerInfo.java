package com.denfop.datacomponent;

import com.denfop.api.brewage.EnumBeerVariety;
import com.denfop.api.brewage.EnumTimeVariety;
import com.denfop.api.brewage.EnumWaterVariety;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record BeerInfo(
        EnumWaterVariety waterVariety,
        EnumTimeVariety timeVariety,
        EnumBeerVariety beerVariety,
        int amount
) {
    public static final BeerInfo DEFAULT = new BeerInfo(
            EnumWaterVariety.values()[0],
            EnumTimeVariety.values()[0],
            EnumBeerVariety.values()[0],
            5
    );
    public static final Codec<BeerInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.xmap(i -> EnumWaterVariety.values()[i], Enum::ordinal).fieldOf("waterVariety").forGetter(BeerInfo::waterVariety),
            Codec.INT.xmap(i -> EnumTimeVariety.values()[i], Enum::ordinal).fieldOf("timeVariety").forGetter(BeerInfo::timeVariety),
            Codec.INT.xmap(i -> EnumBeerVariety.values()[i], Enum::ordinal).fieldOf("beerVariety").forGetter(BeerInfo::beerVariety),
            Codec.INT.fieldOf("amount").forGetter(BeerInfo::amount)
    ).apply(instance, BeerInfo::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BeerInfo> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeByte(value.waterVariety().ordinal());
                buf.writeByte(value.timeVariety().ordinal());
                buf.writeByte(value.beerVariety().ordinal());
                buf.writeByte(value.amount());
            },
            buf -> new BeerInfo(
                    EnumWaterVariety.values()[buf.readByte()],
                    EnumTimeVariety.values()[buf.readByte()],
                    EnumBeerVariety.values()[buf.readByte()],
                    buf.readByte()
            )
    );

    public BeerInfo updateAmount(ItemStack stack) {
        BeerInfo beerInfo = new BeerInfo(waterVariety, timeVariety, beerVariety, amount - 1);
        if (amount == 0) {
            stack.remove(DataComponentsInit.BEER);
            return null;
        } else {
            stack.set(DataComponentsInit.BEER, beerInfo);
            return beerInfo;
        }

    }
}

