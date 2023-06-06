package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.gui.Area;
import com.denfop.componets.VanillaButton;
import com.denfop.container.ContainerElectricBlock;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiElectricBlock extends GuiIC2<ContainerElectricBlock> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/GUIElectricBlockEuRf.png"
    );
    private final ContainerElectricBlock container;
    private final String armorInv;
    private final String name;

    public GuiElectricBlock(ContainerElectricBlock container1) {
        super(container1);
        this.ySize = 196;
        this.container = container1;
        this.armorInv = Localization.translate("ic2.EUStorage.gui.info.armor");
        this.addElement((new VanillaButton(this, 152, 4, 20, 20, this.createEventSender(0)))
                .withIcon(() -> new ItemStack(Items.REDSTONE))
                .withTooltip(container.base::getStringRedstoneMode));
        this.name = Localization.translate(container.base.getName());
    }


    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

    @Override
    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 6,
                4210752
        );
        this.fontRenderer.drawString(this.armorInv, 8, this.ySize - 126 + 3, 4210752);
        String tooltip =
                "EU: " + ModUtils.getString(this.container.base.energy.getEnergy()) + "/" + ModUtils.getString(this.container.base.energy.getCapacity());
        new Area(this, 85 - 3, 38, 108 - 82, 46 - 38).withTooltip(tooltip).drawForeground(par1, par2);


        String output = Localization.translate(
                "ic2.EUStorage.gui.info.output",
                ModUtils.getString(EnergyNetGlobal.instance.getPowerFromTier(this.container.base.energy.getSourceTier())
                )
        );
        this.fontRenderer.drawString(output, 85, 70, 4210752);


        handleUpgradeTooltip(par1, par2);

    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(IC2.RESOURCE_DOMAIN, "textures/gui/infobutton.png"));
        drawTexturedModalRect(j + 3, k + 3, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(background);
        if (this.container.base.energy.getEnergy() > 0.0D) {
            int i1 = (int) (24.0F * this.container.base.getChargeLevel());
            drawTexturedModalRect(j + 79 + 6 - 2 - 1, k + 34, 176, 14, i1 + 1, 16);
        }

        this.elements.get(0).drawBackground(j, k);
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
