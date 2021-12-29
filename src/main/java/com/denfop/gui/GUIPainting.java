package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.items.ItemPaints;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GUIPainting extends GuiIC2<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GUIPainting(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
    }


    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

        handleUpgradeTooltip1(par1, par2);
    }

    private void handleUpgradeTooltip1(int mouseX, int mouseY) {
        if (mouseX >= 165 && mouseX <= 175 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.paintinginformation"));
            List<String> compatibleUpgrades = getInformation();
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }


    private static List<String> getInformation() {
        List<String> ret = new ArrayList();
        ret.add(Localization.translate("iu.paintinginformation1"));
        ret.add(Localization.translate("iu.paintinginformation2"));
        ret.add(Localization.translate("iu.paintinginformation3"));
        ret.add(Localization.translate("iu.paintinginformation4"));
        ret.add(Localization.translate("iu.paintinginformation5"));


        return ret;
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);

        this.mc.getTextureManager().bindTexture(getTexture());
        if (this.container.base != null) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation(IC2.RESOURCE_DOMAIN, "textures/gui/infobutton.png"));
            this.drawTexturedModalRect(this.guiLeft + 165, this.guiTop, 0, 0, 10, 10);
            this.mc.getTextureManager().bindTexture(this.getTexture());
        }
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (14 * this.container.base.getProgress());

        if (chargeLevel > 0) {
            drawTexturedModalRect(this.guiLeft + 25, this.guiTop + 57 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        int down;
        ItemStack stack = null;
        if (this.container.base.inputSlotA.get(0) != null) {
            stack = this.container.base.inputSlotA.get(0).getItem() instanceof ItemPaints
                    ? this.container.base.inputSlotA.get(0) : this.container.base.inputSlotA.get(1);
        }
        if (stack == null) {
            down = 0;
        } else {
            down = 14 * (stack.getItemDamage() - 1);
        }

        if (progress > 0 && down >= 0) {
            drawTexturedModalRect(this.guiLeft + 75, this.guiTop + 35, 178, 33 + down, progress + 1, 13);
        }

    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIPainter.png");
    }

}
