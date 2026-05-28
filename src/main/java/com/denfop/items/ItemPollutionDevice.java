package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.client.pollution.PollutionAnalyzerClientHooks;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.Localization;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemPollutionDevice extends Item implements IItemTab {

    private String nameItem;

    public ItemPollutionDevice() {
        super(new Properties().stacksTo(1).setNoRepair());
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level level,
            List<Component> tooltip,
            TooltipFlag isAdvanced
    ) {
        super.appendHoverText(stack, level, tooltip, isAdvanced);
        tooltip.add(Component.literal(Localization.translate("iu.pollution_scanner.info")));
        tooltip.add(Component.literal(Localization.translate("iu.pollution_analyzer.item_hint")));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (world.isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PollutionAnalyzerClientHooks.open(stack.copy()));
        }

        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            int index = pathBuilder.indexOf(targetString);
            while (index != -1) {
                pathBuilder.replace(index, index + targetString.length(), replacement);
                index = pathBuilder.indexOf(targetString, index + replacement.length());
            }
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem;
    }
}