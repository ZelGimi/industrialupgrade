package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerSolarGeneratorEnergy;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSolarGeneratorEnergy extends GuiIU<ContainerSolarGeneratorEnergy> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/SunnariumGenerator.png"
    );
    private final ContainerSolarGeneratorEnergy container;
    private final String name;

    public GuiSolarGeneratorEnergy(ContainerSolarGeneratorEnergy container1) {
        super(container1, container1.base.getStyle());
        componentList.clear();
        invSlotList.add(container1.base.outputSlot);
        inventory = new GuiComponent(this, 7, 113, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOTS_UPGRADE,
                        new ArrayList<>(),
                        invSlotList
                ) {

                })
        );

        componentList.add(inventory);
        componentList.add(slots);
        this.ySize = 196;
        this.container = container1;
        this.name = Localization.translate(container1.base.getName());


    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        if (!this.isBlack) {
            this.drawXCenteredString((this.xSize / 2) + 15, 6, name, 4210752, false);
        } else {
            this.drawXCenteredString(this.xSize / 2 + 10, 6, name, ModUtils.convertRGBcolorToInt(216, 216, 216), false);
        }
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        handleUpgradeTooltip(mouseX, mouseY);
        String tooltip = "SE: " + ModUtils.getString(this.container.base.sunenergy.getEnergy());
        new Area(this, 123, 38, 146 - 123, 46 - 38).withTooltip(tooltip).drawForeground(mouseX, mouseY);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        fontRenderer.drawString(Localization.translate("gui.SuperSolarPanel.generating") +
                        ": " + (int) this.container.base.generation + Localization.translate("iu.machines_work_energy_type_se"),
                88, 60, ModUtils.convertRGBcolorToInt(0, 0, 0)
        );
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.solar_generator_info"));
            List<String> compatibleUpgrades = ListInformationUtils.solar;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(background);

        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        this.drawBackground();
        this.mc.getTextureManager().bindTexture(background);

        if (this.container.base.sunenergy.getEnergy() > 0.0D) {
            int i1 = (int) (24.0D * this.container.base.sunenergy.getFillRatio());
            drawTexturedModalRect(j + 123, k + 34, 176, 14, i1 + 1, 16);
        }

        this.fontRenderer.drawString(this.name, j + (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2 + 5, k + 6,
                4210752
        );
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(j + 3, k + 3, 0, 0, 10, 10);

        this.mc.getTextureManager().bindTexture(getTexture());
    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

}
