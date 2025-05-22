package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerSteamSolidFluidMixer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSteamSolidFluidMixer extends GuiIU<ContainerSteamSolidFluidMixer> {

    public final ContainerSteamSolidFluidMixer container;

    public GuiSteamSolidFluidMixer(ContainerSteamSolidFluidMixer container1) {
        super(container1, EnumTypeStyle.STEAM);
        this.container = container1;

        this.addElement(TankGauge.createNormal(this, 34, 21, container.base.fluidTank1));
        this.addElement(TankGauge.createNormal(this, 66 + 45, 21, container.base.fluidTank2));
        this.addElement(TankGauge.createNormal(this, 66 + 75, 21, container.base.fluidTank3));
        this.addComponent(new GuiComponent(this, 7, 64, EnumTypeComponent.STEAM_FLUID,
                new Component<>(this.container.base.steam)
        ));
        this.addComponent(new GuiComponent(this, 10, 54, EnumTypeComponent.NULL,
                new Component<>(this.container.base.pressure)
        ));
        this.addComponent(new GuiComponent(this, 85, 45, EnumTypeComponent.STEAM_PROCESS,
                new Component<>(new ComponentProgress(this.container.base, 1,
                        (short) 0
                ) {
                    @Override
                    public double getBar() {
                        return container.base.getProgress();
                    }
                })
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("steam_machine.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 11; i++) {
                compatibleUpgrades.add(Localization.translate("steam_machine.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 60, mouseY, text);
        }
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);

    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2 + 15, 5, name, 4210752, false);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guisteam_machine.png");
    }

}
