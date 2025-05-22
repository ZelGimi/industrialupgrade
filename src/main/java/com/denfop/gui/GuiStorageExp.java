package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.container.ContainerStorageExp;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.IOException;


public class GuiStorageExp extends GuiIU<ContainerStorageExp> {

    public final ContainerStorageExp container;

    public GuiStorageExp(ContainerStorageExp container1) {
        super(container1);
        this.container = container1;
        this.addElement(new CustomButton(this, 50, 61, 74, 16, container1.base, 0, Localization.translate("button.xpremove")));
        this.addElement(new CustomButton(this, 50, 17, 74, 16, container1.base, 1, Localization.translate("button.xpadd")));

    }

    public void initGui() {
        super.initGui();


    }

    protected void actionPerformed(@Nonnull GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);

    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString("Lvl:" + this.container.base.expirencelevel, 100, 51 - 5, 4210752);
        this.fontRenderer.drawString("Lvl:" + this.container.base.expirencelevel1, 31, 51 - 5, 4210752);


    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;

        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common.png"));
        drawTexturedModalRect(xoffset + 149, yoffset + 22, 18,
                34, 24, 55
        );
        drawTexturedModalRect(xoffset + 7, yoffset + 22, 18,
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
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 153, yoffset + 26 + 47 - chargeLevel, 50,
                    85 - chargeLevel, 12, chargeLevel
            );
        }

        if (chargeLevel1 > 0) {
            drawTexturedModalRect(xoffset + 11, yoffset + 26 + 47 - chargeLevel1, 50,
                    85 - chargeLevel1, 12, chargeLevel1
            );
        }

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
