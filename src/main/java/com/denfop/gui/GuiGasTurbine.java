package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerGasTurbineController;
import com.denfop.tiles.gasturbine.TileEntityGasTurbineController;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiGasTurbine<T extends ContainerGasTurbineController> extends GuiIU<ContainerGasTurbineController> {

    public GuiGasTurbine(ContainerGasTurbineController guiContainer) {
        super(guiContainer);
        this.componentList.add(new GuiComponent(this, 30, 45, EnumTypeComponent.WORK_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((TileEntityGasTurbineController) this.getEntityBlock()).work ? Localization.translate(
                                "turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileEntityGasTurbineController) this.getEntityBlock()).work;
                    }
                })
        ));
        this.addElement(TankGauge.createNormal(this, this.imageWidth / 2 - 10, 20, guiContainer.base.tank.getTank()));
        this.addComponent(new GuiComponent(this, 120, 30, EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy.getEnergy())
        ));
        this.addComponent(new GuiComponent(this, 99, 40, EnumTypeComponent.FLUID_PART1,
                new Component<>(new ComponentEmpty())
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("quarry.guide.gasturbine"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 4; i++) {
                compatibleUpgrades.add(Localization.translate("quarry.guide.gasturbine" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 120, mouseY, text);
        }
    }



    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

        handleUpgradeTooltip(par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer( poseStack, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack,final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle( poseStack,partialTicks, mouseX, mouseY);

       RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
     bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack,this.guiLeft, this.guiTop, 0, 0, 10, 10);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
