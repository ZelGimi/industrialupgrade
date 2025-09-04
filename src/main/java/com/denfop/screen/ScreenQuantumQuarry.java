package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ImageInterfaceWidget;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuQuantumQuarry;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ScreenQuantumQuarry<T extends ContainerMenuQuantumQuarry> extends ScreenMain<ContainerMenuQuantumQuarry> {

    public final ContainerMenuQuantumQuarry container;

    public ScreenQuantumQuarry(ContainerMenuQuantumQuarry container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.imageHeight += 60;
        this.imageWidth += 24;
        this.inventory.setY(this.inventory.getY() + 60);
        this.addWidget(new ImageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight));
        this.addComponent(new ScreenWidget(this, 156 + 24, 5, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 138 + 24, 45, EnumTypeComponent.QUANTUM_ENERGY_HEIGHT,
                new WidgetDefault<>(this.container.base.energy)
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

    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip(par1, par2);

    }

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, guiLeft(), this.guiTop(), 0, 0, this.imageWidth, this.imageHeight);


    }

    protected void renderBg(PoseStack poseStack, float f, int x, int y) {
        super.renderBg(poseStack, f, x, y);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, guiLeft() + 3, guiTop() + 3, 0, 0, 10, 10);
        bindTexture(getTexture());

        String getblock = ModUtils.getString(this.container.base.getblock);
        font.draw(poseStack, getblock
                ,
                guiLeft() + 150 - getblock.length() + 15, guiTop() + 24, 4210752
        );

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
