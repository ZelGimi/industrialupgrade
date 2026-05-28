package com.denfop.api.pollution.client;

import com.denfop.Constants;
import com.denfop.api.pollution.radiation.RadiationFoodHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.client.gui.GuiComponent.fill;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
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

        PoseStack graphics = event.getPoseStack();
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

    private static void renderRadiationBar(PoseStack graphics, int slotX, int slotY, ItemStack stack, float ratio) {
        int barWidth = Math.max(1, Math.round(BAR_WIDTH * Math.min(1.0F, ratio)));
        int color = getBarColor(ratio);

        int yOffset = stack.isBarVisible() ? 10 : 13;

        int x = slotX + 2;
        int y = slotY + yOffset;

        fill(graphics, x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0xFF000000);
        fill(graphics, x, y, x + barWidth, y + BAR_HEIGHT, color);
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