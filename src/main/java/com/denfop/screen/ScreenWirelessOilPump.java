package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.vein.common.Type;
import com.denfop.api.vein.common.VeinBase;
import com.denfop.api.widget.*;
import com.denfop.containermenu.ContainerMenuWirelessOilPump;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenWirelessOilPump<T extends ContainerMenuWirelessOilPump> extends ScreenMain<ContainerMenuWirelessOilPump> {

    private final ItemStack stack;

    public ScreenWirelessOilPump(ContainerMenuWirelessOilPump guiContainer) {
        super(guiContainer);
        this.stack = new ItemStack(IUItem.oilblock.getItem());
        this.addWidget(TankWidget.createNormal(this, this.imageWidth / 2 - 10, 20, (guiContainer.base).fluidTank));
        this.addComponent(new ScreenWidget(this, 10, (this.imageHeight - 80) / 2,
                EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>((this.container.base).energy)
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.wireless_oil_vein.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 6; i++) {
                compatibleUpgrades.add(Localization.translate("iu.wireless_oil_vein.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 120, mouseY, text);
        }
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip(par1, par2);
        for (int i = 0, j = 0; i < 4; i++) {
            if (!this.container.base.invslot.get(i).isEmpty() && j < this.container.base.veinList.size()) {
                VeinBase vein = this.container.base.veinList.get(j);
                j++;
                int col = vein.getCol();
                int colmax = vein.getMaxCol();
                boolean isOil = vein.getType() == Type.OIL;
                String name_vein;
                name_vein = Localization.translate("iu.fluidneft");
                new TooltipWidget(this, 130, 8 + i * 18, 18, 18)
                        .withTooltip(name_vein + " " + col + (isOil ? "mb" : "") + "/" + colmax + (
                                isOil
                                        ?
                                        "mb"
                                        : "") + "\n" + "x: " + (vein.getChunk().x << 4) + " z: " + (vein.getChunk().z << 4))
                        .drawForeground(poseStack, par1, par2);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        this.bindTexture();
        for (int i = 0, j = 0; i < 4; i++) {
            if (!this.container.base.invslot.get(i).isEmpty()) {
                new ItemStackWidget(this, 130, 8 + i * 18, () -> stack).drawBackground(poseStack, guiLeft, guiTop);
            }
        }
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, this.guiLeft + 3, guiTop + 3, 0, 0, 10, 10);
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
