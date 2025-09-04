package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.containermenu.ContainerMenuElectricBlock;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenElectricBlock<T extends ContainerMenuElectricBlock> extends ScreenMain<ContainerMenuElectricBlock> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/GUIElectricBlockEuRf.png".toLowerCase()
    );
    private final ContainerMenuElectricBlock container;
    private final String armorInv;
    private final String name;

    public ScreenElectricBlock(ContainerMenuElectricBlock container1) {
        super(container1);
        this.imageHeight = 167;
        this.componentList.clear();
        this.container = container1;

        this.armorInv = Localization.translate("EUStorage.gui.info.armor");
        this.name = Localization.translate(container.base.getName());
    }


    @Override
    protected ResourceLocation getTexture() {
        if (this.container.base.energy.getSourceTier() == 1) {
            return new ResourceLocation(
                    Constants.MOD_ID,
                    "textures/gui/GUIElectricBlockEuRf1.png".toLowerCase()
            );
        }
        if (this.container.base.energy.getSourceTier() == 2) {
            return new ResourceLocation(
                    Constants.MOD_ID,
                    "textures/gui/GUIElectricBlockEuRf2.png".toLowerCase()
            );
        }

        return new ResourceLocation(
                Constants.MOD_ID,
                "textures/gui/GUIElectricBlockEuRf.png".toLowerCase()
        );
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        this.font.draw(poseStack, this.name, (float) (this.imageWidth - this.getStringWidth(this.name)) / 2, 6,
                4210752
        );

        String tooltip =
                "EF: " + ModUtils.getString(this.container.base.energy.getEnergy()) + "/" + ModUtils.getString(this.container.base.energy.getCapacity());
        new TooltipWidget(this, 62, 27, 79, 22).withTooltip(tooltip).drawForeground(poseStack, par1, par2);


        String output = Localization.translate(
                "EUStorage.gui.info.output",
                ModUtils.getString(EnergyNetGlobal.instance.getPowerFromTier(this.container.base.energy.getSourceTier())
                )
        );
        this.font.draw(poseStack, output, 77, 17, 4210752);


        handleUpgradeTooltip(par1, par2);

    }


    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(getTexture());
        int j = guiLeft;
        int k = guiTop;
        drawTexturedModalRect(poseStack, j, k, 0, 0, this.imageWidth, this.imageHeight);

        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, j + 3, k + 3, 0, 0, 10, 10);
        bindTexture(getTexture());
        if (this.container.base.energy.getEnergy() > 0.0D) {
            int i1 = (int) (78.0F * this.container.base.getChargeLevel());

            drawTexturedModalRect(poseStack, j + 62, k + 27, 176, 0, i1 + 1, 22);
        }
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.electricstorageinformation"));
            List<String> compatibleUpgrades = ListInformationUtils.storageinform;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

}
