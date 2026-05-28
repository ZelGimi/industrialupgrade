package com.denfop.items.space;



import com.denfop.config.ModConfig;
import com.denfop.items.BaseEnergyItem;
import com.denfop.items.space.teleport.SpaceTeleportController;
import com.denfop.network.packet.PacketOpenPlanetaryTranslocatorScreen;
import com.denfop.utils.Localization;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemPlanetaryTranslocator extends BaseEnergyItem {

    public ItemPlanetaryTranslocator() {
        super(20_000_000D, ModConfig.itemDouble("planetary_translocator_transfer_limit", 8192.0D), 5);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        SpaceTeleportController.ensureItemUuid(stack);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            new PacketOpenPlanetaryTranslocatorScreen(serverPlayer, stack);
            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_339594_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("iu.space.tp.item.info1")));
        p_41423_.add(Component.literal(Localization.translate("iu.space.tp.item.info2")));
        p_41423_.add(Component.literal(Localization.translate("iu.space.tp.item.info3")));
    }


    @Override
    protected String getOrCreateDescriptionId() {
        this.nameItem = "iu.space.planetary_translocator";
        return this.nameItem;
    }
}