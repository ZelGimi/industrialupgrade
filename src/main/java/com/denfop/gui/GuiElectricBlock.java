package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.gui.Area;
import com.denfop.container.ContainerElectricBlock;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiElectricBlock extends GuiCore<ContainerElectricBlock> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/GUIElectricBlockEuRf.png"
    );
    private final ContainerElectricBlock container;
    private final String armorInv;
    private final String name;

    public GuiElectricBlock(ContainerElectricBlock container1) {
        super(container1);
        this.ySize = 167;
        this.container = container1;
        this.armorInv = Localization.translate("EUStorage.gui.info.armor");
        this.name = Localization.translate(container.base.getName());
    }

    public void initGui() {
        super.initGui();


    }

    protected void actionPerformed(GuiButton guibutton) {


    }

    @Override
    protected ResourceLocation getTexture() {
        if (this.container.base.energy.getSourceTier() == 1) {
            return new ResourceLocation(
                    Constants.MOD_ID,
                    "textures/gui/GUIElectricBlockEuRf1.png"
            );
        }
        if (this.container.base.energy.getSourceTier() == 2) {
            return new ResourceLocation(
                    Constants.MOD_ID,
                    "textures/gui/GUIElectricBlockEuRf2.png"
            );
        }

        return new ResourceLocation(
                Constants.MOD_ID,
                "textures/gui/GUIElectricBlockEuRf.png"
        );
    }

    @Override
    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 6,
                4210752
        );

        String tooltip =
                "EF: " + ModUtils.getString(this.container.base.energy.getEnergy()) + "/" + ModUtils.getString(this.container.base.energy.getCapacity());
        new Area(this, 62, 27, 79, 22).withTooltip(tooltip).drawForeground(par1, par2);


        String output = Localization.translate(
                "EUStorage.gui.info.output",
                ModUtils.getString(EnergyNetGlobal.instance.getPowerFromTier(this.container.base.energy.getSourceTier())
                )
        );
        this.fontRenderer.drawString(output, 77, 17, 4210752);


        handleUpgradeTooltip(par1, par2);

    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(j + 3, k + 3, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(getTexture());
        if (this.container.base.energy.getEnergy() > 0.0D) {
            int i1 = (int) (78.0F * this.container.base.getChargeLevel());

            drawTexturedModalRect(j + 62, k + 27, 176, 0, i1 + 1, 22);
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
