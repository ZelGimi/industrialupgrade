package com.denfop.items.energy;


import com.denfop.config.ModConfig;
import com.denfop.utils.Localization;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemHammer extends ItemToolIU {
    public ItemHammer() {
        super(ModConfig.itemDouble("hammer_durability", 2.0D), -3, BlockTags.MINEABLE_WITH_PICKAXE);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal(Localization.translate("iu.ferromanganese.hammer.info")));
    }
}
