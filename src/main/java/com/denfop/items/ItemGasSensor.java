package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.gassensor.GasSensorClientHooks;
import com.denfop.network.DistExecutor;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.Localization;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;

import java.util.List;

public class ItemGasSensor extends Item implements IItemTab {
    private String nameItem;

    public ItemGasSensor() {
        super(new Item.Properties().stacksTo(1).setNoRepair());
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_339594_, p_41423_, p_41424_);

        p_41423_.add(Component.literal(Localization.translate("iu.gas_sensor.info")));
        p_41423_.add(Component.literal(Localization.translate("iu.gas_sensor.info1")));
        p_41423_.add(Component.literal(Localization.translate("iu.gas_sensor.info2")));
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
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.dimension() != Level.OVERWORLD) {
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        }

        if (level.isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> GasSensorClientHooks.open(stack));
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

}
