package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerSteamHandlerHeavyOre;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSteamHandlerHeavyOre extends GuiIU<ContainerSteamHandlerHeavyOre> {

    public final ContainerSteamHandlerHeavyOre container;

    public GuiSteamHandlerHeavyOre(ContainerSteamHandlerHeavyOre container1) {
        super(container1, EnumTypeStyle.STEAM);
        this.container = container1;

        this.addComponent(new GuiComponent(this, 70, 36, EnumTypeComponent.STEAM_PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
        this.addComponent(new GuiComponent(this, 7, 64, EnumTypeComponent.STEAM_FLUID,
                new Component<>(this.container.base.steam)
        ));
        this.addComponent(new GuiComponent(this, 9, 50, EnumTypeComponent.NULL,
                new Component<>(this.container.base.pressure)
        ));
        this.addComponent(new GuiComponent(this, 61, 61, EnumTypeComponent.HEAT,
                new Component<>(container1.base.heat)
        ));
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2, 2, name, 4210752, false);
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

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.bindTexture();

        int progress = (int) ((218 - 178) * this.container.base.componentProgress.getBar());

        if (progress > 0) {
            drawTexturedModalRect(this.guiLeft + 62, this.guiTop + 37, 178, 34, progress + 1, 14);
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);

    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisteam_machine.png");
    }

}
