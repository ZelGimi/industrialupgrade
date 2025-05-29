package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.container.ContainerFacadeBlock;
import com.denfop.utils.ListInformationUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiFacadeBlock<T extends ContainerFacadeBlock> extends GuiIU<ContainerFacadeBlock> {

    public GuiFacadeBlock(final ContainerFacadeBlock container) {
        super(container);
        this.addElement(new CustomButton(this, 5, 40, 60, 15, container.base, 0, Localization.translate("button.write")));
        this.addElement(new CustomButton(
                this,
                90,
                40,
                60,
                15,
                container.base,
                1,
                Localization.translate("facademechanism_button")
        ));

    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack,mouseX, mouseY);
        handleUpgradeTooltip(mouseX, mouseY);
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            List<String> compatibleUpgrades = ListInformationUtils.facade;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer( poseStack,partialTicks, mouseX, mouseY);
        int h = guiLeft;
        int k = guiTop;
       bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect( poseStack,h + 3, k + 3, 0, 0, 10, 10);
        bindTexture(getTexture());

    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
