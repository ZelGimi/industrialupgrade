package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockNuclearBomb;
import com.denfop.blocks.ItemBlockCore;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemBlockNuclearBomb extends ItemBlockCore<BlockNuclearBomb.Type> {
    public ItemBlockNuclearBomb(BlockCore p_40565_, BlockNuclearBomb.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.ReactorsTab));
    }
    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem ="block.industrialupgrade.nuclear_bomb";
        }

        return this.nameItem;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(Component.literal(Localization.translate( "iu.nuclear_bomb.info")));
    }
}
