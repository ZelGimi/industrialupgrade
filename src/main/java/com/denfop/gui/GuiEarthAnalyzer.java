package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerEarthAnalyzer;
import com.denfop.tiles.quarry_earth.TileEntityEarthQuarryAnalyzer;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiEarthAnalyzer<T extends ContainerEarthAnalyzer> extends GuiIU<ContainerEarthAnalyzer> {

    public GuiEarthAnalyzer(ContainerEarthAnalyzer guiContainer) {
        super(guiContainer);
        this.componentList.add(new GuiComponent(this, 30, 30, EnumTypeComponent.WORK_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((TileEntityEarthQuarryAnalyzer) this.getEntityBlock()).isAnalyzed() ? Localization.translate(
                                "turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileEntityEarthQuarryAnalyzer) this.getEntityBlock()).isAnalyzed();
                    }
                })
        ));
    }



    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        if (this.container.base.isAnalyzed() && !this.container.base.fullAnalyzed()) {
            draw(poseStack, Localization.translate("earth_quarry.analyze"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56)
            );
        } else if (this.container.base.fullAnalyzed() && this.container.base.isAnalyzed()) {
            draw (poseStack, Localization.translate("earth_quarry.analyze1"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56)
            );
        } else if (this.container.base.fullAnalyzed() && !this.container.base.isAnalyzed()) {
            draw(poseStack, Localization.translate("earth_quarry.full_analyze"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56)
            );
        }
        PoseStack pose = poseStack.pose();
        pose.pushPose();
        pose.translate(10, 60,10);
        pose.scale(0.75f,0.75f,1);
        draw(poseStack, Localization.translate("earth_quarry.block_col") + container.base.blockCol, 0, 0,
                ModUtils.convertRGBcolorToInt(56, 56, 56)
        );
        pose.popPose();
        pose.pushPose();
        pose.translate(10, 70,10);
        pose.scale(0.75f,0.75f,1);
        draw(poseStack, Localization.translate("earth_quarry.block_ores") + container.base.blockOres, 0, 0,
                ModUtils.convertRGBcolorToInt(56, 56, 56)
        );
        pose.popPose();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack,final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);

    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack,final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(poseStack,partialTicks, mouseX, mouseY);
     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
