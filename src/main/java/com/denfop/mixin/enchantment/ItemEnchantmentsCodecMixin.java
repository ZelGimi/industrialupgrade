package com.denfop.mixin.enchantment;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEnchantments.class)
public abstract class ItemEnchantmentsCodecMixin {

    @Shadow
    @Final
    @Mutable
    public static StreamCodec<RegistryFriendlyByteBuf, ItemEnchantments> STREAM_CODEC;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void iu$replaceStreamCodec(final CallbackInfo ci) {
        STREAM_CODEC = StreamCodec.of(
                ItemEnchantmentsCodecMixin::iu$encode,
                ItemEnchantmentsCodecMixin::iu$decode
        );
    }

    private static void iu$encode(final RegistryFriendlyByteBuf buf, final ItemEnchantments value) {
        buf.writeVarInt(value.size());

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : value.entrySet()) {
            final Holder<Enchantment> holder = entry.getKey();

            final ResourceLocation enchantmentId = holder.unwrapKey()
                    .orElseThrow(() -> new IllegalStateException("Unbound enchantment holder: " + holder))
                    .location();

            buf.writeResourceLocation(enchantmentId);
            buf.writeVarInt(entry.getIntValue());
        }

        final boolean showInTooltip = ((ItemEnchantmentsAccessor) (Object) value).iu$getShowInTooltip();
        buf.writeBoolean(showInTooltip);
    }

    private static ItemEnchantments iu$decode(final RegistryFriendlyByteBuf buf) {
        final int size = buf.readVarInt();

        final RegistryAccess registryAccess = buf.registryAccess();
        final HolderLookup.RegistryLookup<Enchantment> lookup = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);

        final ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);

        for (int i = 0; i < size; i++) {
            final ResourceLocation enchantmentId = buf.readResourceLocation();
            final int level = buf.readVarInt();

            final ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, enchantmentId);
            final Holder<Enchantment> holder = lookup.getOrThrow(key);

            mutable.set(holder, level);
        }

        final boolean showInTooltip = buf.readBoolean();
        return mutable.toImmutable().withTooltip(showInTooltip);
    }
}