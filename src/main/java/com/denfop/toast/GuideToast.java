package com.denfop.toast;

import com.denfop.Localization;
import com.denfop.api.guidebook.Quest;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.world.item.ItemStack;

public class GuideToast implements Toast {

    private final Quest quest;
    private boolean visible = false;
    public GuideToast(Quest quest){
        this.quest=quest;
    }
    @Override
    public Visibility render(GuiGraphics pGuiGraphics, ToastComponent toastComponent, long time) {
        Minecraft mc = toastComponent.getMinecraft();


        pGuiGraphics.blit(TEXTURE, 0, 0, 0, 0, this.width(), this.height());


        ItemStack icon = quest.icon;
        pGuiGraphics.renderFakeItem(icon, 8, 8);
        pGuiGraphics.drawString(toastComponent.getMinecraft().font, ChatFormatting.GOLD+ Localization.translate("iu.quest.completed"), 30, 7, -1, false);
        pGuiGraphics.drawString(toastComponent.getMinecraft().font,quest.getLocalizedName(), 30, 18, -1, false);
        return time >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }


}
