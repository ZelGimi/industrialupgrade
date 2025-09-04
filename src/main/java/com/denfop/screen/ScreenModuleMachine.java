package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ButtonWidget;
import com.denfop.containermenu.ContainerMenuModuleMachine;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@OnlyIn(Dist.CLIENT)
public class ScreenModuleMachine<T extends ContainerMenuModuleMachine> extends ScreenMain<ContainerMenuModuleMachine> {

    public final ContainerMenuModuleMachine container;

    public ScreenModuleMachine(ContainerMenuModuleMachine container1) {
        super(container1);
        this.container = container1;
        this.imageHeight = 178;
        this.inventory.setX(7);
        this.inventory.setY(96);
        this.addWidget(new ButtonWidget(this, 103, 15, 68, 14, container1.base, 0, Localization.translate("button.write")));
    }

    private static List<String> getInformation() {
        List<String> ret = new ArrayList<>();
        ret.add(Localization.translate("iu.moduleinformation1"));
        ret.add(Localization.translate("iu.moduleinformation2"));
        ret.add(Localization.translate("iu.moduleinformation3"));


        return ret;
    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft(), this.guiTop(), 0, 0, this.imageWidth, this.imageHeight);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack, this.imageHeight / 2, 4, Component.literal(name), 4210752, false);
    }


    protected void renderBg(GuiGraphics poseStack, float f, int x, int y) {
        super.renderBg(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int xoffset = (this.width - this.imageWidth) / 2;
        int yoffset = (this.height - this.imageHeight) / 2;

        bindTexture();
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        this.drawTexturedModalRect(poseStack, xoffset + 3, yoffset + 3, 0, 0, 10, 10);
        bindTexture();

    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

        handleUpgradeTooltip(par1, par2);
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.moduleinformation"));
            List<String> compatibleUpgrades = getInformation();
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

}
