package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.quarry_earth.BlockEntityEarthQuarryAnalyzer;
import com.denfop.componets.ComponentButton;
import com.denfop.containermenu.ContainerMenuEarthAnalyzer;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenEarthAnalyzer<T extends ContainerMenuEarthAnalyzer> extends ScreenMain<ContainerMenuEarthAnalyzer> {

    public ScreenEarthAnalyzer(ContainerMenuEarthAnalyzer guiContainer) {
        super(guiContainer);
        this.componentList.add(new ScreenWidget(this, 30, 30, EnumTypeComponent.WORK_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((BlockEntityEarthQuarryAnalyzer) this.getEntityBlock()).isAnalyzed() ? Localization.translate(
                                "turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((BlockEntityEarthQuarryAnalyzer) this.getEntityBlock()).isAnalyzed();
                    }
                })
        ));
    }


    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        if (this.container.base.isAnalyzed() && !this.container.base.fullAnalyzed()) {
            this.font.draw(poseStack, Localization.translate("earth_quarry.analyze"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56)
            );
        } else if (this.container.base.fullAnalyzed() && this.container.base.isAnalyzed()) {
            this.font.draw(poseStack, Localization.translate("earth_quarry.analyze1"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56)
            );
        } else if (this.container.base.fullAnalyzed() && !this.container.base.isAnalyzed()) {
            this.font.draw(poseStack, Localization.translate("earth_quarry.full_analyze"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56)
            );
        }
        poseStack.pushPose();
        poseStack.translate(10, 60, 10);
        poseStack.scale(0.75f, 0.75f, 1);
        this.font.draw(poseStack, Localization.translate("earth_quarry.block_col") + container.base.blockCol, 0, 0,
                ModUtils.convertRGBcolorToInt(56, 56, 56)
        );
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(10, 70, 10);
        poseStack.scale(0.75f, 0.75f, 1);
        this.font.draw(poseStack, Localization.translate("earth_quarry.block_ores") + container.base.blockOres, 0, 0,
                ModUtils.convertRGBcolorToInt(56, 56, 56)
        );
        poseStack.popPose();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);

    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(poseStack, partialTicks, mouseX, mouseY);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
