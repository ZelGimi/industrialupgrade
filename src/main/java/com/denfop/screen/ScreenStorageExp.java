package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ButtonWidget;
import com.denfop.containermenu.ContainerMenuStorageExp;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;


public class ScreenStorageExp<T extends ContainerMenuStorageExp> extends ScreenMain<ContainerMenuStorageExp> {

    public final ContainerMenuStorageExp container;

    public ScreenStorageExp(ContainerMenuStorageExp container1) {
        super(container1);
        this.container = container1;
        this.addWidget(new ButtonWidget(this, 50, 61, 74, 16, container1.base, 0, Localization.translate("button.xpremove")));
        this.addWidget(new ButtonWidget(this, 50, 17, 74, 16, container1.base, 1, Localization.translate("button.xpadd")));

    }


    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        this.font.draw(poseStack, "Lvl:" + this.container.base.expirencelevel, 100, 51 - 5, 4210752);
        this.font.draw(poseStack, "Lvl:" + this.container.base.expirencelevel1, 31, 51 - 5, 4210752);


    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int xoffset = (this.width - this.imageWidth) / 2;
        int yoffset = (this.height - this.imageHeight) / 2;

        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common.png"));
        drawTexturedModalRect(poseStack, xoffset + 149, yoffset + 22, 18,
                34, 24, 55
        );
        drawTexturedModalRect(poseStack, xoffset + 7, yoffset + 22, 18,
                34, 24, 55
        );
        int chargeLevel = (int) (47.0F * Math.min(this.container.base.expirencelevel
                / 23926D, 1));
        int chargeLevel1 = 0;
        if (this.container.base.energy.getCapacity() > 2000000000) {
            chargeLevel1 = (int) (47.0F * Math.min(this.container.base.expirencelevel1
                    / 23926D, 1));
        }
        chargeLevel = Math.min(chargeLevel, 47);
        chargeLevel1 = Math.min(chargeLevel1, 47);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common.png"));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (chargeLevel > 0) {
            drawTexturedModalRect(poseStack, xoffset + 153, yoffset + 26 + 47 - chargeLevel, 50,
                    85 - chargeLevel, 12, chargeLevel
            );
        }

        if (chargeLevel1 > 0) {
            drawTexturedModalRect(poseStack, xoffset + 11, yoffset + 26 + 47 - chargeLevel1, 50,
                    85 - chargeLevel1, 12, chargeLevel1
            );
        }

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
