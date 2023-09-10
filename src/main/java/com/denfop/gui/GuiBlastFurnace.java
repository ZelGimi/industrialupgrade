package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerBlastFurnace;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiBlastFurnace extends GuiIU<ContainerBlastFurnace> {

    public final ContainerBlastFurnace container;

    public GuiBlastFurnace(ContainerBlastFurnace container1) {
        super(container1);
        this.container = container1;
        componentList.clear();

        this.addComponent(new GuiComponent(this, 60, 67, EnumTypeComponent.HEAT,
                new Component<>(this.container.base.heat)
        ));

        this.addComponent(new GuiComponent(this, 159, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.blast_furnace_recipe.info"));
            List<String> compatibleUpgrades = ListInformationUtils.blast_furnace;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        if (container.base.tank1 != null) {
            TankGauge.createNormal(this, 6, 5, container.base.tank1).drawForeground(par1, par2);
        }
        TankGauge.createNormal(this, 27, 5, container.base.tank).drawForeground(par1, par2);


        String temp =
                ModUtils.getString(this.container.base.bar * 100000D) + " Pa";
        new AdvArea(this, 136, 63, 164, 66).withTooltip(temp).drawForeground(par1, par2);
        new AdvArea(this, 136, 69, 146, 79).withTooltip("+1").drawForeground(par1, par2);
        new AdvArea(this, 154, 69, 164, 79).withTooltip("-1").drawForeground(par1, par2);
        new AdvArea(this, 80, 35, 101, 49)
                .withTooltip(Localization.translate("gui.MolecularTransformer.progress") + ": " + (int) (Math.min(
                        this.container.base.getProgress() / 3600D,
                        1D
                ) * 100) + "%")
                .drawForeground(par1, par2);


    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 136 && x <= 146 && y >= 69 && y <= 79) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if (x >= 154 && x <= 164 && y >= 69 && y <= 79) {
            new PacketUpdateServerTile(this.container.base, 1);
        }

    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2 + 20, 6, name, 4210752, false);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());

        int progress = (int) (24.0F * this.container.base.getProgress() / 3600);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 79, yoffset + 34, 176, 14, progress + 1, 16);
        }
        if (container.base.tank1 != null) {
            TankGauge.createNormal(this, 6, 5, container.base.tank1).drawBackground(xoffset, yoffset);
        }
        TankGauge.createNormal(this, 27, 5, container.base.tank).drawBackground(xoffset, yoffset);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(xoffset + 3, yoffset + 3, 0, 0, 10, 10);

        this.mc.getTextureManager().bindTexture(getTexture());

        int bar = (int) ((Math.min(this.container.base.bar * 1D, 5D) / 5D) * 27D);
        if (bar > 0) {
            drawTexturedModalRect(this.guiLeft + 136, this.guiTop + 63, 177, 104, bar + 1, 3);
        }

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiblastfurnace.png");
    }

}
