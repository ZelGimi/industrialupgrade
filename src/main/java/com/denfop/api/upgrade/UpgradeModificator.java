package com.denfop.api.upgrade;


import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;

public class UpgradeModificator {

    public static final Codec<UpgradeModificator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(mod -> mod.itemstack),
            Codec.STRING.fieldOf("type").forGetter(mod -> mod.type)
    ).apply(instance, UpgradeModificator::new));
    public static final StreamCodec<FriendlyByteBuf, UpgradeModificator> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(buf);
                try {
                    EncoderHandler.encode(customPacketBuffer, value.itemstack);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                buf.writeUtf(value.type);
            },
            buf -> {
                try {
                    return new UpgradeModificator(
                            (Item) DecoderHandler.decode(new CustomPacketBuffer(buf)),
                            buf.readUtf()
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
    );
    public final Item itemstack;
    public final String type;


    public UpgradeModificator(Item stack, String type) {
        this.itemstack = stack;
        this.type = type;
    }

    public boolean matches(ItemStack stack) {
        return this.itemstack == stack.getItem();
    }

    public boolean matches(String type) {
        return this.type.equals(type);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpgradeModificator that = (UpgradeModificator) o;
        return this.itemstack == that.itemstack;
    }


}
