package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.geothermalpump.BlockEntityGeothermalController;
import com.denfop.componets.ComponentButton;
import com.denfop.containermenu.ContainerMenuGeothermalController;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenGeothermalController<T extends ContainerMenuGeothermalController> extends ScreenMain<ContainerMenuGeothermalController> {

    private boolean hover = false;

    public ScreenGeothermalController(ContainerMenuGeothermalController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.componentList.add(new ScreenWidget(this, 70, 35, 24,
                24,
                new WidgetDefault<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((BlockEntityGeothermalController) this.getEntityBlock()).work ? Localization.translate(
                                "turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((BlockEntityGeothermalController) this.getEntityBlock()).work;
                    }
                })
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("guide.geothermalpump"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 4; i++) {
                compatibleUpgrades.add(Localization.translate("guide.geothermalpump" + i));
            }
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
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

        handleUpgradeTooltip(par1, par2);
        if (!this.container.base.work) {
            this.hover = par1 >= 70 && par1 <= 70 + 24 && par2 >= 35 && par2 <= 35 + 24;
        } else {
            this.hover = false;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(poseStack, partialTicks, mouseX, mouseY);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture();
        drawTexturedModalRect(poseStack, this.guiLeft + 70,
                this.guiTop + 35
                , 231, 1, 24, 24
        );
        if (this.container.base.work) {
            drawTexturedModalRect(poseStack, this.guiLeft + 70,
                    this.guiTop + 35
                    , 231, 51, 24, 24
            );
        }
        if (this.hover) {
            drawTexturedModalRect(poseStack, this.guiLeft + 70,
                    this.guiTop + 35
                    , 231, 26, 24, 24
            );
        }
        bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, 10, 10);
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guigeothermalpump.png");
    }

}
