package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerApiary;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiApiary<T extends ContainerApiary> extends GuiIU<ContainerApiary> {


    public ContainerApiary container;

    public GuiApiary(ContainerApiary container1) {
        super(container1);
        this.container = container1;
        componentList.clear();
        this.imageHeight = 207;
        this.addElement(new AdvArea(this, 10, 21, 21, 69).withTooltip(() -> ModUtils.getString(container1.base.food)));
        this.addElement(new AdvArea(this, 154, 21, 165, 69).withTooltip(() -> ModUtils.getString(container1.base.royalJelly)));
        this.addElement(new AdvArea(this, 37, 22, 50, 32).withTooltip(() -> ModUtils.getString(container1.base.workers)));
        this.addElement(new AdvArea(this, 37, 35, 50, 49).withTooltip(() -> ModUtils.getString(container1.base.doctors)));
        this.addElement(new AdvArea(this, 37, 51, 50, 64).withTooltip(() -> ModUtils.getString(container1.base.builders)));
        this.addElement(new AdvArea(this, 37, 67, 50, 80).withTooltip(() -> ModUtils.getString(container1.base.attacks)));
        this.addElement(new AdvArea(this, 37, 83, 50, 94).withTooltip(() -> ModUtils.getString(container1.base.ill)));
        this.addElement(new AdvArea(this,
                37,
                97,
                50,
                109).withTooltip(() -> ModUtils.getString(container1.base.birth) + "/" + ModUtils.getString(container1.base.death)));

    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.bee.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 21; i++) {
                compatibleUpgrades.add(Localization.translate("iu.bee.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 160, mouseY, text);
        }
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer( poseStack,par1, par2);
        handleUpgradeTooltip(par1, par2);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiapiary.png");
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture();
        switch (this.container.base.task) {
            case 0:
                this.drawTexturedModalRect(poseStack, this.guiLeft + 60, this.guiTop + 24, 249, 1, 6, 6);
                break;
            case 1:
                this.drawTexturedModalRect(poseStack, this.guiLeft + 60, this.guiTop + 71, 249, 1, 6, 6);
                break;
            case 2:
                this.drawTexturedModalRect(poseStack, this.guiLeft + 60, this.guiTop + 39, 249, 1, 6, 6);
                break;
            case 3:
                this.drawTexturedModalRect(poseStack, this.guiLeft + 60, this.guiTop + 55, 249, 1, 6, 6);
                break;
        }
        if (this.container.base.deathTask == 1) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 60, this.guiTop + 101, 249, 1, 6, 6);
        }

        if (this.container.base.illTask == 1) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 60, this.guiTop + 86, 249, 1, 6, 6);
        }
        int renderHeight = (int) (49 * ModUtils.limit(
                this.container.base.food / (this.container.base.maxFood * 1D),
                0.0D,
                1.0D
        ));
        this.drawTexturedModalRect(
                poseStack, this.guiLeft + 10,
                this.guiTop + 20 + 50 - renderHeight,
                236,
                50 - renderHeight,
                12,
                renderHeight
        );
        renderHeight = (int) (49 * ModUtils.limit(
                this.container.base.royalJelly / (this.container.base.maxJelly * 1D),
                0.0D,
                1.0D
        ));
        this.drawTexturedModalRect(
                poseStack, this.guiLeft + 154,
                this.guiTop + 20 + 50 - renderHeight,
                223,
                50 - renderHeight,
                12,
                renderHeight
        );
        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }

}
