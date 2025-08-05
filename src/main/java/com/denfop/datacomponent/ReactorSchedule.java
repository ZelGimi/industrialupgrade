package com.denfop.datacomponent;

import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public record ReactorSchedule(
        int type,
        int level,
        String name,
        int generation,
        int rad,
        List<ItemStack> items,
        List<Integer> gridLayout
) {
    public static final Codec<ReactorSchedule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("type").forGetter(ReactorSchedule::type),
            Codec.INT.fieldOf("level").forGetter(ReactorSchedule::level),
            Codec.STRING.fieldOf("name").forGetter(ReactorSchedule::name),
            Codec.INT.fieldOf("generation").forGetter(ReactorSchedule::generation),
            Codec.INT.fieldOf("rad").forGetter(ReactorSchedule::rad),
            ContainerItem.CUSTOM_ITEMSTACK_CODEC.listOf().fieldOf("items").forGetter(ReactorSchedule::items),
            Codec.INT.listOf().fieldOf("grid").forGetter(ReactorSchedule::gridLayout)
    ).apply(instance, ReactorSchedule::new));


    public static final StreamCodec<RegistryFriendlyByteBuf, ReactorSchedule> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeInt(value.type());
                buf.writeInt(value.level());
                buf.writeUtf(value.name());
                buf.writeInt(value.generation());
                buf.writeInt(value.rad());

                CustomPacketBuffer packetBuffer = new CustomPacketBuffer(buf);
                try {
                    EncoderHandler.encode(packetBuffer, value.items);
                    EncoderHandler.encode(packetBuffer, value.gridLayout);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            },
            buf -> {
                int type = buf.readInt();
                int level = buf.readInt();
                String name = buf.readUtf();
                int generation = buf.readInt();
                int rad = buf.readInt();
                CustomPacketBuffer packetBuffer = new CustomPacketBuffer(buf);
                List<ItemStack> itemsList = new ArrayList<>();
                List<Integer> grid = new ArrayList<>();
                try {
                    itemsList = (List<ItemStack>) DecoderHandler.decode(packetBuffer);
                    grid = (List<Integer>) DecoderHandler.decode(packetBuffer);
                } catch (Exception ignored) {
                }
                ;
                return new ReactorSchedule(type, level, name, generation, rad, itemsList, grid);
            }
    );


}
