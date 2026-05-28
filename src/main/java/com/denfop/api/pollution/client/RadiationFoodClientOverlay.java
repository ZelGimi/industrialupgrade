package com.denfop.api.pollution.client;

import com.denfop.Constants;
import com.denfop.api.pollution.radiation.RadiationFoodHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class RadiationFoodClientOverlay {

    private static final int BAR_WIDTH = 13;
    private static final int BAR_HEIGHT = 2;

    private RadiationFoodClientOverlay() {
    }

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen)) {
            return;
        }

        GuiGraphics graphics = event.getGuiGraphics();
        int left = screen.getGuiLeft();
        int top = screen.getGuiTop();

        for (Slot slot : screen.getMenu().slots) {
            if (slot == null || !slot.isActive()) {
                continue;
            }

            ItemStack stack = slot.getItem();
            if (stack.isEmpty() || !RadiationFoodHelper.hasRadiation(stack)) {
                continue;
            }

            float ratio = RadiationFoodHelper.getDoseRatio(stack);
            if (ratio <= 0.0F) {
                continue;
            }

            renderRadiationBar(
                    graphics,
                    left + slot.x,
                    top + slot.y,
                    stack,
                    ratio
            );
        }
    }

    private static void renderRadiationBar(GuiGraphics graphics, int slotX, int slotY, ItemStack stack, float ratio) {
        int barWidth = Math.max(1, Math.round(BAR_WIDTH * Math.min(1.0F, ratio)));
        int color = getBarColor(ratio);

        int yOffset = stack.isBarVisible() ? 10 : 13;

        int x = slotX + 2;
        int y = slotY + yOffset;

        graphics.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0xFF000000);
        graphics.fill(x, y, x + barWidth, y + BAR_HEIGHT, color);
    }

    private static int getBarColor(float ratio) {
        if (ratio >= 1.0F) {
            return 0xFFE23A2E;
        }
        if (ratio >= 0.75F) {
            return 0xFFFF7A1A;
        }
        if (ratio >= 0.45F) {
            return 0xFFE4D93A;
        }
        return 0xFF61D95E;
    }
}