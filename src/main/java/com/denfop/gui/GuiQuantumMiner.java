package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerQuantumMiner;
import com.denfop.utils.ListInformationUtils;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GuiQuantumMiner extends GuiIU<ContainerQuantumMiner> {

    public final ContainerQuantumMiner container;

    public GuiQuantumMiner(ContainerQuantumMiner container1) {
        super(container1);
        this.container = container1;


        this.addComponent(new GuiComponent(this, 138, 5, EnumTypeComponent.QUANTUM_ENERGY_HEIGHT,
                new Component<>(this.container.base.qe)
        ));
        this.addComponent(new GuiComponent(this, 4, 34, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.progress)
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

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);


    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);


    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft + 3, guiTop + 3, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(getTexture());
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
