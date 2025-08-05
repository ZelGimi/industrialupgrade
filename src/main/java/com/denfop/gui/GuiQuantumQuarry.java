package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerQuantumQuarry;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GuiQuantumQuarry<T extends ContainerQuantumQuarry> extends GuiIU<ContainerQuantumQuarry> {

    public final ContainerQuantumQuarry container;

    public GuiQuantumQuarry(ContainerQuantumQuarry container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.imageHeight += 60;
        this.imageWidth += 24;
        this.inventory.setY(this.inventory.getY() + 60);
        this.addElement(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));
        this.addComponent(new GuiComponent(this, 156 + 24, 5, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 138 + 24, 45, EnumTypeComponent.QUANTUM_ENERGY_HEIGHT,
                new Component<>(this.container.base.energy)
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

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip(par1, par2);

    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, guiLeft(), this.guiTop(), 0, 0, this.imageWidth, this.imageHeight);


    }

    protected void renderBg(GuiGraphics poseStack, float f, int x, int y) {
        super.renderBg(poseStack, f, x, y);
        bindTexture(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, guiLeft() + 3, guiTop() + 3, 0, 0, 10, 10);
        bindTexture(getTexture());

        String getblock = ModUtils.getString(this.container.base.getblock);
        draw(poseStack, getblock
                ,
                guiLeft() + 150 - getblock.length() + 15, guiTop() + 24, 4210752
        );

    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
