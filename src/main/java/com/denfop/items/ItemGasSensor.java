package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.gassensor.GasSensorClientHooks;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.Localization;
import net.minecraft.Util;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemGasSensor extends Item implements IItemTab {

    private String nameItem;

    public ItemGasSensor() {
        super(new Item.Properties().stacksTo(1).setNoRepair());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal(Localization.translate("iu.gas_sensor.info")));
        pTooltipComponents.add(Component.literal(Localization.translate("iu.gas_sensor.info1")));
        pTooltipComponents.add(Component.literal(Localization.translate("iu.gas_sensor.info2")));
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }

    @Override
    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(this)));
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