package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gasvein.TypeGas;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerGasWellAnalyzer;
import com.denfop.tiles.gaswell.TileEntityGasWellAnalyzer;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class GuiGasWellAnalyzer<T extends ContainerGasWellAnalyzer> extends GuiIU<ContainerGasWellAnalyzer> {

    public GuiGasWellAnalyzer(ContainerGasWellAnalyzer guiContainer) {
        super(guiContainer);
        this.componentList.add(new GuiComponent(this, 30, 30, EnumTypeComponent.WORK_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((TileEntityGasWellAnalyzer) this.getEntityBlock()).work ? Localization.translate(
                                "turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileEntityGasWellAnalyzer) this.getEntityBlock()).work;
                    }
                })
        ));

    }



    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        if (this.container.base.vein != null && !container.base.vein.isFind()) {
            if (this.container.base.progress < 1200) {
                this.font.draw(poseStack,
                        (this.container.base.progress * 100 / 1200) + "%",
                        69,
                        34,
                        ModUtils.convertRGBcolorToInt(13, 229, 34)
                );


            }
        } else if (this.container.base.vein != null && container.base.vein.isFind() && container.base.vein.getType() == TypeGas.NONE) {
            this.font.draw(poseStack,
                    Localization.translate("earth_quarry.error"),
                    69,
                    34,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
        } else if (this.container.base.vein != null && container.base.vein.isFind() && container.base.vein.getType() != TypeGas.NONE) {
            List<FormattedCharSequence> lines = font.split(net.minecraft.network.chat.Component.literal(Localization.translate("earth_quarry.send_work")), this.imageWidth - 69 - 5);
            int offsetY = 0;
            for (FormattedCharSequence line : lines) {
                font.draw(poseStack, line, 69, 34 + offsetY,    ModUtils.convertRGBcolorToInt(13, 229, 34));
                offsetY += font.lineHeight;
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack,final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);

    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack,final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(poseStack,partialTicks, mouseX, mouseY);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
