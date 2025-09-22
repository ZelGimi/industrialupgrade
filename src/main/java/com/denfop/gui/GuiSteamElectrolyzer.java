package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.SteamImageInterface;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerSteamElectrolyzer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSteamElectrolyzer extends GuiIU<ContainerSteamElectrolyzer> {

    private static final ResourceLocation background;

    static {
        background = new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIElectolyzer.png");
    }

    public ContainerSteamElectrolyzer container;

    public GuiSteamElectrolyzer(ContainerSteamElectrolyzer container1) {
        super(container1, EnumTypeStyle.STEAM);
        this.container = container1;
        componentList.clear();
        inventory = new GuiComponent(this, 7, 119, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );

        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );
        componentList.add(inventory);
        componentList.add(slots);

        this.addComponent(new GuiComponent(this, 98, 60, EnumTypeComponent.STEAM_ENERGY_HEIGHT,
                new Component<>(this.container.base.ampere)
        ));
        this.ySize = 200;
        this.addElement(new SteamImageInterface(this, 0, 0, this.xSize, this.ySize));
        this.addElement(TankGauge.createNormal(this, 12, 44, container.base.getFluidTank(0)));
        this.addElement(TankGauge.createNormal(this, 74, 6, container.base.getFluidTank(1)));
        this.addElement(TankGauge.createNormal(this, 74, 63, container.base.getFluidTank(2)));

        this.addComponent(new GuiComponent(this, 45, 85, EnumTypeComponent.STEAM_PROCESS,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 45, 45, EnumTypeComponent.STEAM_PROCESS,
                new Component<>(new ComponentEmpty())
        ));

        this.addComponent(new GuiComponent(this, 13, 20, EnumTypeComponent.STEAM_FLUID,
                new Component<>(this.container.base.steam)
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

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);


    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);

    }

}
