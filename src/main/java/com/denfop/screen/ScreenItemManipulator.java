package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ImageInterfaceWidget;
import com.denfop.containermenu.ContainerMenuItemManipulator;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ScreenItemManipulator<T extends ContainerMenuItemManipulator> extends ScreenMain<ContainerMenuItemManipulator> {

    public final ContainerMenuItemManipulator container;

    public ScreenItemManipulator(ContainerMenuItemManipulator container1) {
        super(container1);
        this.container = container1;
        this.imageWidth = 220;
        this.imageHeight = 220;
        this.inventory.setX(7);
        this.inventory.setY(138);
        this.elements.add(new ImageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight));
        //      this.addWidget(new CustomButton(this,103,15,68,14,container1.base,0,Localization.translate("button.write")));
    }

    private static List<String> getInformation() {
        List<String> ret = new ArrayList<>();
        ret.add(Localization.translate("iu.moduleinformation1"));
        ret.add(Localization.translate("iu.moduleinformation2"));
        ret.add(Localization.translate("iu.moduleinformation3"));


        return ret;
    }

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack, this.imageWidth / 2, 4, name, 4210752, false);
    }


    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindTexture();
        int xoffset = guiLeft;
        int yoffset = guiTop;
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack, this.imageWidth / 2, 4, name, 4210752, false);

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
