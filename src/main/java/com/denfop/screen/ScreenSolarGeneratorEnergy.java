package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.containermenu.ContainerMenuSolarGeneratorEnergy;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ScreenSolarGeneratorEnergy<T extends ContainerMenuSolarGeneratorEnergy> extends ScreenMain<ContainerMenuSolarGeneratorEnergy> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/SunnariumGenerator.png".toLowerCase()
    );
    private final ContainerMenuSolarGeneratorEnergy container;
    private final String name;

    public ScreenSolarGeneratorEnergy(ContainerMenuSolarGeneratorEnergy container1) {
        super(container1, container1.base.getStyle());
        componentList.clear();


        this.container = container1;
        this.name = Localization.translate(container1.base.getName());


    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft(), this.guiTop(), 0, 0, this.imageWidth, this.imageHeight);
        String name = Localization.translate(this.container.base.getName());
        int textWidth = this.getStringWidth(name);
        float scale = 1.0f;


        if (textWidth > 120) {
            scale = 120f / textWidth;
        }


        poseStack.pushPose();
        poseStack.scale(scale, scale, 1.0f);


        int centerX = this.guiLeft() + this.imageWidth / 2;
        int textX = (int) ((centerX / scale) - (textWidth / 2.0f));
        int textY = (int) ((this.guiTop() + 6) / scale);


        this.font.draw(poseStack, name, textX, textY, 4210752);


        poseStack.popPose();
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        handleUpgradeTooltip(mouseX, mouseY);
        String tooltip = "SE: " + ModUtils.getString(this.container.base.sunenergy.getEnergy());
        new TooltipWidget(this, 66, 33, 53, 10).withTooltip(tooltip).drawForeground(poseStack, mouseX, mouseY);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        font.draw(poseStack, Localization.translate("gui.SuperSolarPanel.generating") +
                        ": " + (int) this.container.base.generation + Localization.translate("iu.machines_work_energy_type_se"),
                29, 61, ModUtils.convertRGBcolorToInt(13, 229, 34)
        );
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.solar_generator_info"));
            List<String> compatibleUpgrades = ListInformationUtils.solar;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        bindTexture(background);

        int j = guiLeft();
        int k = guiTop();

        this.drawBackground(poseStack);
        bindTexture(background);

        if (this.container.base.sunenergy.getEnergy() > 0.0D) {
            int i1 = (int) (52.0D * this.container.base.sunenergy.getFillRatio());
            drawTexturedModalRect(poseStack, j + 66, k + 33, 179, 3, i1 + 1, 10);
        }


        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, j + 3, k + 3, 0, 0, 10, 10);
    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

}
