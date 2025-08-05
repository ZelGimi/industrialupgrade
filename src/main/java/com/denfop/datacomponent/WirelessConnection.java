package com.denfop.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.denfop.datacomponent.DataComponentsInit.WIRELESS;

public record WirelessConnection(
        ResourceLocation world,
        int tier,
        int x,
        int y,
        int z,
        boolean change
) {
    public static final Codec<WirelessConnection> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("world").forGetter(WirelessConnection::world),
                    Codec.INT.fieldOf("tier").forGetter(WirelessConnection::tier),
                    Codec.INT.fieldOf("x").forGetter(WirelessConnection::x),
                    Codec.INT.fieldOf("y").forGetter(WirelessConnection::y),
                    Codec.INT.fieldOf("z").forGetter(WirelessConnection::z),
                    Codec.BOOL.fieldOf("change").forGetter(WirelessConnection::change)
            ).apply(instance, WirelessConnection::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, WirelessConnection> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeResourceLocation(value.world());
                buf.writeInt(value.tier());
                buf.writeInt(value.x());
                buf.writeInt(value.y());
                buf.writeInt(value.z());
                buf.writeBoolean(value.change());
            },
            buf -> new WirelessConnection(
                    buf.readResourceLocation(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readBoolean()
            )
    );
    public static WirelessConnection EMPTY = new WirelessConnection(Level.OVERWORLD.location(), 14, 0, 0, 0, false);

    public WirelessConnection updateChange(ItemStack stack, boolean change) {
        WirelessConnection wirelessConnection = new WirelessConnection(world, tier, x, y, z, change);
        stack.set(WIRELESS, wirelessConnection);
        return wirelessConnection;
    }
}
