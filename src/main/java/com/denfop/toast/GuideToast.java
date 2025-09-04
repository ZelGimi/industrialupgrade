package com.denfop.toast;

import com.denfop.api.guidebook.Quest;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;

public class GuideToast implements Toast {

    private final Quest quest;
    private boolean visible = false;

    public GuideToast(Quest quest) {
        this.quest = quest;
    }

    @Override
    public Visibility render(PoseStack pGuiGraphics, ToastComponent toastComponent, long time) {
        Minecraft mc = toastComponent.getMinecraft();


        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        toastComponent.blit(pGuiGraphics, 0, 0, 0, 0, this.width(), this.height());


        ItemStack icon = quest.icon;
        toastComponent.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(icon, 8, 8);
        toastComponent.getMinecraft().font.draw(pGuiGraphics, ChatFormatting.GOLD + Localization.translate("iu.quest.completed"), 30, 7, -1);
        toastComponent.getMinecraft().font.draw(pGuiGraphics, quest.getLocalizedName(), 30, 18, -1);
        return time >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }


}
