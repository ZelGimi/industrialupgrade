package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.base.BlockEntityScanner;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuScanner;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenScanner<T extends ContainerMenuScanner> extends ScreenMain<ContainerMenuScanner> {

    private static final ResourceLocation background = new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIScanner.png".toLowerCase());
    private final String[] info = new String[9];

    public ScreenScanner(final ContainerMenuScanner container) {
        super(container, container.base.getStyle());
        componentList.clear();
        this.inventoryList.add(container.base.inputSlot);
        inventory = new ScreenWidget(this, 7, 83, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.inventoryList))
        );

        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new ScreenWidget(this, 12, 26, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.getContainer().base.energy)
        ));
        this.info[1] = Localization.translate("Scanner.gui.info1");
        this.info[2] = Localization.translate("Scanner.gui.info2");
        this.info[3] = Localization.translate("Scanner.gui.info3");
        this.info[4] = Localization.translate("Scanner.gui.info4");
        this.info[5] = Localization.translate("Scanner.gui.info5");
        this.info[6] = Localization.translate("Scanner.gui.info6");
        this.info[7] = Localization.translate("Scanner.gui.info7");
        this.info[8] = Localization.translate("Scanner.gui.info8");
    }

    @Override
    protected void mouseClicked(int i, int j, final int k) {
        super.mouseClicked(i, j, k);
        i -= this.guiLeft;
        j -= this.guiTop;
        if (container.base.getState() == BlockEntityScanner.State.COMPLETED || container.base.getState() == BlockEntityScanner.State.TRANSFER_ERROR || container.base.getState() == BlockEntityScanner.State.FAILED) {
            if (i >= 102 && i <= 102 + 12 && j >= 49 && j <= 49 + 12) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                new PacketUpdateServerTile(this.container.base, 0);
            }
        }
        if (container.base.getState() == BlockEntityScanner.State.COMPLETED || container.base.getState() == BlockEntityScanner.State.TRANSFER_ERROR || container.base.getState() == BlockEntityScanner.State.FAILED) {
            if (i >= 143 && i <= 143 + 24 && j >= 49 && j <= 49 + 24) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                new PacketUpdateServerTile(this.container.base, 1);
            }
        }
    }

    protected void drawForegroundLayer(PoseStack poseStack, int mouseX, int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        BlockEntityScanner te = this.container.base;

        switch (te.getState()) {
            case IDLE:
                this.font.draw(poseStack, Localization.translate("Scanner.gui.idle"), 10, 69, 15461152);
                break;
            case NO_STORAGE:
                this.font.draw(poseStack, this.info[2], 10, 69, 15461152);
                break;
            case SCANNING:
                this.font.draw(poseStack, this.info[1], 10, 69, 2157374);
                this.font.draw(poseStack, te.getPercentageDone() + "%", 125, 69, 2157374);
                break;
            case NO_ENERGY:
                this.font.draw(poseStack, this.info[3], 10, 69, 14094352);
                break;
            case ALREADY_RECORDED:
                this.font.draw(poseStack, this.info[8], 10, 69, 14094352);
                break;
            case FAILED:
                this.font.draw(poseStack, this.info[4], 10, 69, 2157374);
                this.font.draw(poseStack, this.info[6], 110, 30, 14094352);
                break;
            case COMPLETED:
            case TRANSFER_ERROR:
                if (te.getState() == BlockEntityScanner.State.COMPLETED) {
                    this.font.draw(poseStack, this.info[4], 10, 69, 2157374);
                }

                if (te.getState() == BlockEntityScanner.State.TRANSFER_ERROR) {
                    this.font.draw(poseStack, this.info[7], 10, 69, 14094352);
                }

                this.font.draw(poseStack, ModUtils.getString(te.patternUu) + "B UUM", 105, 25, 16777215);
                this.font.draw(poseStack, ModUtils.getString(te.patternEu) + "EF", 105, 36, 16777215);
        }
        if (container.base.getState() == BlockEntityScanner.State.COMPLETED || container.base.getState() == BlockEntityScanner.State.TRANSFER_ERROR || container.base.getState() == BlockEntityScanner.State.FAILED) {
            new TooltipWidget(this, 102, 49, 12, 12).withTooltip("Scanner.gui.button.delete").drawForeground(poseStack, mouseX, mouseY);
        }
        if (container.base.getState() == BlockEntityScanner.State.COMPLETED || container.base.getState() == BlockEntityScanner.State.TRANSFER_ERROR || container.base.getState() == BlockEntityScanner.State.FAILED) {
            new TooltipWidget(this, 143, 49, 24, 12).withTooltip("Scanner.gui.button.save").drawForeground(poseStack, mouseX, mouseY);
        }
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawBackgroundAndTitle(poseStack, partialTicks, mouseX, mouseY);
        if (this.container.base instanceof BlockEntityUpgrade) {
            bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
        this.bindTexture();
        if (container.base.getState() == BlockEntityScanner.State.COMPLETED || container.base.getState() == BlockEntityScanner.State.TRANSFER_ERROR || container.base.getState() == BlockEntityScanner.State.FAILED) {
            this.drawTexturedRect(poseStack, 102, 49, 12.0D, 12.0D, 176, 57);

        }
        if (container.base.getState() == BlockEntityScanner.State.COMPLETED || container.base.getState() == BlockEntityScanner.State.TRANSFER_ERROR) {
            this.drawTexturedRect(poseStack, 143, 49, 24, 12.0D, 176, 69);
        }

        if (container.base.getState() == BlockEntityScanner.State.COMPLETED || container.base.getState() == BlockEntityScanner.State.TRANSFER_ERROR || container.base.getState() == BlockEntityScanner.State.FAILED) {
            new TooltipWidget(this, 102, 49, 12, 12).withTooltip("Scanner.gui.button.delete").drawBackground(poseStack, mouseX, mouseY);
        }
        if (container.base.getState() == BlockEntityScanner.State.COMPLETED || container.base.getState() == BlockEntityScanner.State.TRANSFER_ERROR || container.base.getState() == BlockEntityScanner.State.FAILED) {
            new TooltipWidget(this, 143, 49, 24, 12).withTooltip("Scanner.gui.button.save").drawBackground(poseStack, mouseX, mouseY);
        }
        this.bindTexture();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BlockEntityScanner te = this.container.base;
        int scanningloop = te.getSubPercentageDoneScaled(66);
        if (scanningloop > 0) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 30, this.guiTop + 20, 176, 14, scanningloop, 43);
        }
        for (ScreenWidget element : this.componentList) {
            element.drawBackground(poseStack, this.guiLeft, this.guiTop);
        }

    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
