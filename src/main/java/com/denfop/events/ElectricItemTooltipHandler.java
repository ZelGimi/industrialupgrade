package com.denfop.events;


import com.denfop.Constants;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.KeyboardIU;
import com.denfop.utils.Localization;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ElectricItemTooltipHandler {

    public ElectricItemTooltipHandler() {
        MinecraftForge.EVENT_BUS.register(this);
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
