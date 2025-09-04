package com.denfop.items.energy;

import com.denfop.IUCore;
import com.denfop.api.windsystem.WindSystem;
import com.denfop.items.BaseEnergyItem;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.Localization;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemWindMeter extends BaseEnergyItem {
    public ItemWindMeter() {
        super(5000, 500, 1);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal(Localization.translate("iu.wind_meter.info")));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand p_41434_) {
        ItemStack stack = player.getItemInHand(p_41434_);


        if (world.isClientSide || world.dimension() != Level.OVERWORLD) {
            return InteractionResultHolder.pass(stack);
        }


        if (!ElectricItem.manager.canUse(stack, 10)) {
            return InteractionResultHolder.pass(stack);
        }


        ElectricItem.manager.use(stack, 10, player);


        IUCore.proxy.messagePlayer(
                player,
                Localization.translate("iu.wind_meter.info") + String.format(
                        "%.1f",
                        WindSystem.windSystem.getWind_Strength()
                ) + " m/s"
        );


        return InteractionResultHolder.success(stack);
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
            this.nameItem = "item." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }
}
