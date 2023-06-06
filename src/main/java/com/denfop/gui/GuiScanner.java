package com.denfop.gui;

import com.denfop.api.gui.Component;
import com.denfop.api.gui.CustomButton;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerScanner;
import com.denfop.tiles.base.TileEntityScanner;
import ic2.core.init.Localization;
import ic2.core.util.Util;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiScanner extends GuiIU<ContainerScanner> {

    private static final ResourceLocation background = new ResourceLocation("ic2", "textures/gui/GUIScanner.png");
    private final String[] info = new String[9];

    public GuiScanner(final ContainerScanner container) {
        super(container, container.base.getStyle());
        componentList.clear();
        this.invSlotList.add(container.base.inputSlot);
        inventory = new GuiComponent(this, 7, 83, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.invSlotList))
        );

        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new GuiComponent(this, 12, 25, EnumTypeComponent.ENERGY_CLASSIC,
                new Component<>(this.container.base.energy)
        ));
        this.addElement((new CustomButton(this, 102, 49, 12, 12, 176, 57, background, this.createEventSender(0)))
                .withEnableHandler(() -> container.base.getState() == TileEntityScanner.State.COMPLETED || container.base.getState() == TileEntityScanner.State.TRANSFER_ERROR || container.base.getState() == TileEntityScanner.State.FAILED)
                .withTooltip("ic2.Scanner.gui.button.delete"));
        this.addElement((new CustomButton(this, 143, 49, 24, 12, 176, 69, background, this.createEventSender(1)))
                .withEnableHandler(() -> container.base.getState() == TileEntityScanner.State.COMPLETED || container.base.getState() == TileEntityScanner.State.TRANSFER_ERROR)
                .withTooltip("ic2.Scanner.gui.button.save"));
        this.info[1] = Localization.translate("ic2.Scanner.gui.info1");
        this.info[2] = Localization.translate("ic2.Scanner.gui.info2");
        this.info[3] = Localization.translate("ic2.Scanner.gui.info3");
        this.info[4] = Localization.translate("ic2.Scanner.gui.info4");
        this.info[5] = Localization.translate("ic2.Scanner.gui.info5");
        this.info[6] = Localization.translate("ic2.Scanner.gui.info6");
        this.info[7] = Localization.translate("ic2.Scanner.gui.info7");
        this.info[8] = Localization.translate("ic2.Scanner.gui.info8");
    }

    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        TileEntityScanner te = this.container.base;
        switch (te.getState()) {
            case IDLE:
                this.fontRenderer.drawString(Localization.translate("ic2.Scanner.gui.idle"), 10, 69, 15461152);
                break;
            case NO_STORAGE:
                this.fontRenderer.drawString(this.info[2], 10, 69, 15461152);
                break;
            case SCANNING:
                this.fontRenderer.drawString(this.info[1], 10, 69, 2157374);
                this.fontRenderer.drawString(te.getPercentageDone() + "%", 125, 69, 2157374);
                break;
            case NO_ENERGY:
                this.fontRenderer.drawString(this.info[3], 10, 69, 14094352);
                break;
            case ALREADY_RECORDED:
                this.fontRenderer.drawString(this.info[8], 10, 69, 14094352);
                break;
            case FAILED:
                this.fontRenderer.drawString(this.info[4], 10, 69, 2157374);
                this.fontRenderer.drawString(this.info[6], 110, 30, 14094352);
                break;
            case COMPLETED:
            case TRANSFER_ERROR:
                if (te.getState() == TileEntityScanner.State.COMPLETED) {
                    this.fontRenderer.drawString(this.info[4], 10, 69, 2157374);
                }

                if (te.getState() == TileEntityScanner.State.TRANSFER_ERROR) {
                    this.fontRenderer.drawString(this.info[7], 10, 69, 14094352);
                }

                this.fontRenderer.drawString(Util.toSiString(te.patternUu, 4) + "B UUM", 105, 25, 16777215);
                this.fontRenderer.drawString(Util.toSiString(te.patternEu, 4) + "EU", 105, 36, 16777215);
        }

    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        TileEntityScanner te = this.container.base;
        int scanningloop = te.getSubPercentageDoneScaled(66);
        if (scanningloop > 0) {
            this.drawTexturedModalRect(this.guiLeft + 30, this.guiTop + 20, 176, 14, scanningloop, 43);
        }

    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
