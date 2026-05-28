package com.denfop.datacomponent;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record ContainerItem(boolean open, int slot_inventory, List<ItemStack> listItem, int uid) {

    public static final ContainerItem EMPTY = new ContainerItem(false, -1, new ArrayList<>(), 0);
    public static final Codec<ItemStack> OVERSTACK_ITEM_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(ItemStack::getItem),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("count").forGetter(ItemStack::getCount),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemStack::getComponentsPatch)
    ).apply(instance, ContainerItem::createStack));
    public static final Codec<ItemStack> CUSTOM_ITEMSTACK_CODEC = Codec.either(
            Codec.STRING,
            Codec.either(OVERSTACK_ITEM_CODEC, ItemStack.CODEC)
    ).xmap(
            either -> {
                if (either.left().isPresent()) {
                    return ItemStack.EMPTY;
                }

                Either<ItemStack, ItemStack> inner = either.right().orElseThrow();

                if (inner.left().isPresent()) {
                    return inner.left().get();
                }

                return inner.right().orElse(ItemStack.EMPTY);
            },
            stack -> {
                if (stack == null || stack.isEmpty()) {
                    return Either.left("empty");
                }

                if (stack.getCount() > 99) {
                    return Either.right(Either.left(stack));
                }

                return Either.right(Either.right(stack));
            }
    );
    public static final Codec<ContainerItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("open").forGetter(ContainerItem::open),
            Codec.INT.fieldOf("slot_inventory").forGetter(ContainerItem::slot_inventory),
            CUSTOM_ITEMSTACK_CODEC.listOf().fieldOf("listItem").forGetter(ContainerItem::listItem),
            Codec.INT.fieldOf("uid").forGetter(ContainerItem::uid)
    ).apply(instance, ContainerItem::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStack> CUSTOM_ITEMSTACK_STREAM_CODEC = StreamCodec.of(
            (buf, stack) -> {
                if (stack == null || stack.isEmpty()) {
                    buf.writeBoolean(true);
                    return;
                }

                buf.writeBoolean(false);
                buf.writeResourceLocation(BuiltInRegistries.ITEM.getKey(stack.getItem()));
                buf.writeVarInt(stack.getCount());
                DataComponentPatch.STREAM_CODEC.encode(buf, stack.getComponentsPatch());
            },
            buf -> {
                boolean isEmpty = buf.readBoolean();
                if (isEmpty) {
                    return ItemStack.EMPTY;
                }

                ResourceLocation id = buf.readResourceLocation();
                Item item = BuiltInRegistries.ITEM.get(id);
                int count = buf.readVarInt();
                DataComponentPatch patch = DataComponentPatch.STREAM_CODEC.decode(buf);

                return createStack(item, count, patch);
            }
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ContainerItem> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeBoolean(value.open());
                buf.writeInt(value.slot_inventory());

                buf.writeVarInt(value.listItem().size());
                for (ItemStack stack : value.listItem()) {
                    CUSTOM_ITEMSTACK_STREAM_CODEC.encode(buf, stack);
                }

                buf.writeInt(value.uid());
            },
            buf -> {
                boolean open = buf.readBoolean();
                int slotInventory = buf.readInt();

                int size = buf.readVarInt();
                List<ItemStack> list = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    list.add(CUSTOM_ITEMSTACK_STREAM_CODEC.decode(buf));
                }

                int uid = buf.readInt();
                return new ContainerItem(open, slotInventory, list, uid);
            }
    );

    private static ItemStack createStack(final Item item, final int count, final DataComponentPatch patch) {
        if (item == null || item == Items.AIR || count <= 0) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(BuiltInRegistries.ITEM.wrapAsHolder(item), count, patch == null ? DataComponentPatch.EMPTY : patch);
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

    private static List<ItemStack> copyStacks(final List<ItemStack> source) {
        final List<ItemStack> result = new ArrayList<>(source.size());
        for (ItemStack stack : source) {
            result.add(stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
        }
        return result;
    }

    public static ContainerItem getContainer(ItemStack containerStack) {
        @Nullable ContainerItem containerItem = containerStack.get(DataComponentsInit.CONTAINER);
        if (containerItem == null) {
            containerItem = ContainerItem.EMPTY.updateOpen(containerStack, false);
            containerStack.set(DataComponentsInit.CONTAINER, containerItem);
        }
        return containerItem;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ContainerItem other)) {
            return false;
        }

        return this.open == other.open
                && this.slot_inventory == other.slot_inventory
                && this.uid == other.uid
                && sameStackList(this.listItem, other.listItem);
    }

    public ContainerItem updateOpen(ItemStack stack, boolean open) {
        ContainerItem containerItem = new ContainerItem(open, slot_inventory, copyStacks(listItem), uid);
        stack.set(DataComponentsInit.CONTAINER, containerItem);
        return containerItem;
    }

    public ContainerItem updateSlot(ItemStack stack, int slot_inventory) {
        ContainerItem containerItem = new ContainerItem(open, slot_inventory, copyStacks(listItem), uid);
        stack.set(DataComponentsInit.CONTAINER, containerItem);
        return containerItem;
    }

    public ContainerItem updateItems(ItemStack stack, List<ItemStack> listItem) {
        ContainerItem containerItem = new ContainerItem(open, slot_inventory, copyStacks(listItem), uid);
        stack.set(DataComponentsInit.CONTAINER, containerItem);
        return containerItem;
    }

    public ContainerItem updateUUID(ItemStack stack, int uid) {
        ContainerItem containerItem = new ContainerItem(open, slot_inventory, copyStacks(listItem), uid);
        stack.set(DataComponentsInit.CONTAINER, containerItem);
        return containerItem;
    }
}