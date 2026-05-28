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
import java.util.Objects;

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
                    EncoderHandler.encode(packetBuffer, value.items());
                    EncoderHandler.encode(packetBuffer, value.gridLayout());
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

                return new ReactorSchedule(type, level, name, generation, rad, itemsList, grid);
            }
    );

    public ReactorSchedule {
        items = copyStacks(items);
        gridLayout = gridLayout == null ? new ArrayList<>() : new ArrayList<>(gridLayout);
    }

    private static List<ItemStack> copyStacks(List<ItemStack> source) {
        List<ItemStack> result = new ArrayList<>();
        if (source == null) {
            return result;
        }

        for (ItemStack stack : source) {
            result.add(stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
        }
        return result;
    }

    private static boolean sameStack(ItemStack a, ItemStack b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.isEmpty() && b.isEmpty()) {
            return true;
        }
        if (a.isEmpty() != b.isEmpty()) {
            return false;
        }
        return a.getCount() == b.getCount() && ItemStack.isSameItemSameComponents(a, b);
    }

    private static boolean sameStackList(List<ItemStack> left, List<ItemStack> right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        if (left.size() != right.size()) {
            return false;
        }

        for (int i = 0; i < left.size(); i++) {
            if (!sameStack(left.get(i), right.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static int stackHash(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }
        return Objects.hash(
                stack.getItem(),
                stack.getCount(),
                stack.getComponents()
        );
    }

    private static int stackListHash(List<ItemStack> stacks) {
        if (stacks == null) {
            return 0;
        }

        int result = 1;
        for (ItemStack stack : stacks) {
            result = 31 * result + stackHash(stack);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ReactorSchedule other)) {
            return false;
        }

        return this.type == other.type
                && this.level == other.level
                && this.generation == other.generation
                && this.rad == other.rad
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.gridLayout, other.gridLayout)
                && sameStackList(this.items, other.items);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(this.type);
        result = 31 * result + Integer.hashCode(this.level);
        result = 31 * result + Objects.hashCode(this.name);
        result = 31 * result + Integer.hashCode(this.generation);
        result = 31 * result + Integer.hashCode(this.rad);
        result = 31 * result + Objects.hashCode(this.gridLayout);
        result = 31 * result + stackListHash(this.items);
        return result;
    }
}