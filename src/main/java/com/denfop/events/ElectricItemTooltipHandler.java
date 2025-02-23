package com.denfop.events;


import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.Localization;
import com.denfop.api.item.IEnergyItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ElectricItemTooltipHandler {

    public ElectricItemTooltipHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void drawTooltips(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && stack.getItem() instanceof IEnergyItem) {
            String tooltip = ElectricItem.manager.getToolTip(stack);
            if (!tooltip.isEmpty()) {
                event.getToolTip().add(tooltip);
                if (Keyboard.isKeyDown(42)) {
                    event.getToolTip().add(Localization.translate(
                            Constants.ABBREVIATION + ".item.tooltip.PowerTier",
                            ElectricItem.manager.getTier(stack)
                    ));
                }
            }
        }

    }

}
