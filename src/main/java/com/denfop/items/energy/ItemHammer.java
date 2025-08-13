package com.denfop.items.energy;

import com.denfop.Localization;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemHammer extends ItemToolIU {
    public ItemHammer() {
        super(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_339594_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("iu.ferromanganese.hammer.info")));
    }
}
