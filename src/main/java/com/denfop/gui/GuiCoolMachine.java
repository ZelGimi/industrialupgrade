package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerCoolMachine;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiCoolMachine extends GuiIU<ContainerCoolMachine> {

    public ContainerCoolMachine container;
    public String name;

    public GuiCoolMachine(ContainerCoolMachine guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.componentList.clear();
        this.name = Localization.translate(guiContainer.base.getName());
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.cool_storage.info_main"));
            List<String> compatibleUpgrades = ListInformationUtils.cooling;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 53 && x <= 63 && y >= 54 && y <= 64) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if (x >= 73 && x <= 83 && y >= 54 && y <= 64) {
            new PacketUpdateServerTile(this.container.base, 1);
        }
        if (x >= 144 && x <= 157 && y >= 33 && y <= 45) {
            new PacketUpdateServerTile(this.container.base, 2);
        }
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 6, 4210752);
        String temp = ("-" + this.container.base.cold.getEnergy() + "°C" + "/-" + this.container.base.max);

        new AdvArea(this, 53, 42, 83, 53).withTooltip(temp).drawForeground(par1, par2);
        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EF";
        new AdvArea(this, 113, 21, 124, 70)
                .withTooltip(tooltip2)
                .drawForeground(par1, par2);
        new AdvArea(this, 144, 33, 157, 45).withTooltip(this.container.base.work ? Localization.translate("turn_off") :
                Localization.translate("turn_on")).drawForeground(par1, par2);

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);
        this.drawBackground();
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(xOffset + 3, yOffset + 3, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(getTexture());
        int temperature = (int) (38 * this.container.base.cold.getEnergy() / this.container.base.max);
        if (temperature > 0) {
            drawTexturedModalRect(this.guiLeft + 53, this.guiTop + 42, 176, 104, temperature + 1, 11);
        }
        int chargeLevel = (int) (48.0F * this.container.base.energy.getFillRatio());

        if (chargeLevel > 0) {
            drawTexturedModalRect(xOffset + 113, yOffset + 22 + 48 - chargeLevel, 176,
                    115 + 48 - chargeLevel, 12, chargeLevel
            );
        }
        if (this.container.base.work) {
            drawTexturedModalRect(xOffset + 143, yOffset + 32, 199,
                    55, 239 - 224, 84 - 70
            );
        }

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guienergycolling.png");


    }


}
