package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockNuclearBomb;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.utils.Localization;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemBlockNuclearBomb extends ItemBlockCore<BlockNuclearBomb.Type> {
    public ItemBlockNuclearBomb(BlockCore p_40565_, BlockNuclearBomb.Type element) {
        super(p_40565_, element, new Properties(), IUCore.IUTab);
    }

    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem = "block.industrialupgrade.nuclear_bomb";
        }

        return this.nameItem;
    }

    @Override
    public void appendHoverText(ItemStack p_40572_, TooltipContext p_339655_, List<Component> p_40574_, TooltipFlag p_40575_) {
        super.appendHoverText(p_40572_, p_339655_, p_40574_, p_40575_);
        p_40574_.add(Component.literal(Localization.translate("iu.nuclear_bomb.info")));
    }
}
