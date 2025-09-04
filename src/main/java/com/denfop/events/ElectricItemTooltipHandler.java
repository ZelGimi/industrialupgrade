package com.denfop.events;


import com.denfop.Constants;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.KeyboardIU;
import com.denfop.utils.Localization;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@OnlyIn(Dist.CLIENT)
public class ElectricItemTooltipHandler {

    public ElectricItemTooltipHandler() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void drawTooltips(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && stack.getItem() instanceof EnergyItem) {
            String tooltip = ElectricItem.manager.getToolTip(stack);
            if (!tooltip.isEmpty()) {
                event.getToolTip().add(Component.literal(tooltip));
                if (KeyboardIU.isKeyDown(42)) {
                    event.getToolTip().add(Component.literal(Localization.translate(
                            Constants.ABBREVIATION + ".item.tooltip.PowerTier",
                            ElectricItem.manager.getTier(stack)
                    )));
                }
            }
        }

    }

}
