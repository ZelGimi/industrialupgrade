package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuQuantumMiner;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ScreenQuantumMiner<T extends ContainerMenuQuantumMiner> extends ScreenMain<ContainerMenuQuantumMiner> {

    public final ContainerMenuQuantumMiner container;

    public ScreenQuantumMiner(ContainerMenuQuantumMiner container1) {
        super(container1);
        this.container = container1;


        this.addComponent(new ScreenWidget(this, 138, 5, EnumTypeComponent.QUANTUM_ENERGY_HEIGHT,
                new WidgetDefault<>(this.container.base.qe)
        ));
        this.addComponent(new ScreenWidget(this, 4, 34, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container.base.progress)
        ));
    }


    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.quarryinformation"));
            List<String> compatibleUpgrades = ListInformationUtils.quarryinform;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }


    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);


    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, this.guiLeft + 3, guiTop + 3, 0, 0, 10, 10);
        bindTexture(getTexture());
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
