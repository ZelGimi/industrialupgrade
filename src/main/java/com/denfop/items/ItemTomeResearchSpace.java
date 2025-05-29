package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.Data;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemTomeResearchSpace extends Item implements IItemTab {
    private String nameItem;

    public ItemTomeResearchSpace() {
        super(new Properties().stacksTo(1));
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag advanced) {
        tooltip.add(Component.literal(Localization.translate("iu.tome_research1")));
        tooltip.add(Component.literal(Localization.translate("iu.tome_research2")));
    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }
    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "iu.tome_research";
        }

        return this.nameItem;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(
            @Nonnull final Level world,
            @Nonnull final Player player,
            @Nonnull final InteractionHand hand
    ) {
        ItemStack stack;
        if (!world.isClientSide) {
            stack = player.getItemInHand(hand);
            final CompoundTag nbt = stack.getOrCreateTag();
            if (player.isShiftKeyDown()) {
                nbt.putUUID("uuid", player.getUUID());
            } else {
                final UUID uuid = nbt.getUUID("uuid");
                final Map<IBody, Data> data = SpaceNet.instance
                        .getFakeSpaceSystem()
                        .getDataFromUUID(uuid);
                if (data != null && !data.isEmpty()) {
                    SpaceNet.instance.getFakeSpaceSystem().copyData(data, player.getUUID());
                }
            }
            return InteractionResultHolder.success(stack);
        }
        return super.use(world, player, hand);
    }

}
