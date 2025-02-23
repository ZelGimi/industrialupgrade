package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.api.vein.Type;
import com.denfop.api.vein.Vein;
import com.denfop.container.ContainerWirelessMineralQuarry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiWirelessMineralQuarry extends GuiIU<ContainerWirelessMineralQuarry> {


    public GuiWirelessMineralQuarry(ContainerWirelessMineralQuarry guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 10, (this.ySize - 80) / 2,
                EnumTypeComponent.ENERGY,
                new Component<>((this.container.base).energy)
        ));
    }
    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.wireless_mineral_vein.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 6; i++) {
                compatibleUpgrades.add(Localization.translate("iu.wireless_mineral_vein.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX-120, mouseY, text);
        }
    }
    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        for (int i = 0, j = 0; i < 4; i++) {
            if (!this.container.base.invslot.get(i).isEmpty() && j < this.container.base.veinList.size()) {
                Vein vein = this.container.base.veinList.get(j);

                int col = vein.getCol();
                int colmax = vein.getMaxCol();
                boolean isOil = vein.getType() == Type.OIL;
                String name_vein;
                name_vein = this.container.base.itemStacks.get(j).getDisplayName();
                j++;
                new Area(this, 130, 8 + i * 18, 18, 18)
                        .withTooltip(name_vein + " " + col + (isOil ? "mb" : "") + "/" + colmax + (
                                isOil
                                        ?
                                        "mb"
                                        : "") + "\n" + "x: " + (vein.getChunk().x << 4) + " z: " + (vein.getChunk().z << 4))
                        .drawForeground(par1, par2);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        for (int i = 0, j = 0; i < 4; i++) {
            if (!this.container.base.invslot.get(i).isEmpty() && j < this.container.base.veinList.size()) {

                final int finalJ = j;
                ItemStack stack = this.container.base.itemStacks.get(finalJ);
                stack.setCount(1);
                new ItemStackImage(this, 130, 8 + i * 18, () -> stack).drawBackground(
                        guiLeft,
                        guiTop
                );
                j++;
            }
        }
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft + 3, guiTop + 3, 0, 0, 10, 10);
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
