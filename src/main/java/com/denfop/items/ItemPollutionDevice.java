package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.pollution.component.LevelPollution;
import com.denfop.client.pollution.PollutionAnalyzerClientHooks;
import com.denfop.utils.Localization;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemPollutionDevice extends Item {
    private String nameItem;

    public ItemPollutionDevice() {
        super(new Properties().tab(IUCore.EnergyTab).stacksTo(1).setNoRepair());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal(Localization.translate("iu.pollution_scanner.info")));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (world.isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PollutionAnalyzerClientHooks.open(stack.copy()));
        }

        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
    }

    private void sendPollutionMessage(Player player, LevelPollution level, String messageKey) {
        switch (level) {
            case LOW:
            case VERY_LOW:
            case MEDIUM:
            case HIGH:
            case VERY_HIGH:
                IUCore.proxy.messagePlayer(player, Localization.translate(messageKey + "." + level.name().toLowerCase()));
                break;
        }
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem;
    }

}
