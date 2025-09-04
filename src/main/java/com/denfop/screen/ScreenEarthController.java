package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.quarry_earth.BlockEntityEarthQuarryController;
import com.denfop.componets.ComponentButton;
import com.denfop.containermenu.ContainerMenuEarthController;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenEarthController<T extends ContainerMenuEarthController> extends ScreenMain<ContainerMenuEarthController> {

    public ScreenEarthController(ContainerMenuEarthController guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 10, 65, EnumTypeComponent.ENERGY, new WidgetDefault<>(this.container.base.energy)));
        this.componentList.add(new ScreenWidget(this, 30, 30, EnumTypeComponent.WORK_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((BlockEntityEarthQuarryController) this.getEntityBlock()).work
                                ? Localization.translate("turn_off")
                                :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((BlockEntityEarthQuarryController) this.getEntityBlock()).work;
                    }
                })
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("quarry.guide.earth_quarry"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 10; i++) {
                compatibleUpgrades.add(Localization.translate("quarry.guide.earth_quarry" + i));
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
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        if (this.container.base.work) {
            if (this.container.base.indexChunk < this.container.base.max) {
                this.font.draw(poseStack,
                        Localization.translate("earth_quarry.controller_work"), 30, 55,
                        ModUtils.convertRGBcolorToInt(56, 56, 56)
                );
            }
        } else if (this.container.base.indexChunk == this.container.base.max) {
            this.font.draw(poseStack, Localization.translate("earth_quarry.send_work"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56)
            );
        }
        this.font.draw(poseStack, Localization.translate("earth_quarry.block_ores") + container.base.block_Col, 40, 70,
                ModUtils.convertRGBcolorToInt(56, 56, 56)
        );
        handleUpgradeTooltip(par1, par2);
    }


    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(poseStack, partialTicks, mouseX, mouseY);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, 10, 10);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
