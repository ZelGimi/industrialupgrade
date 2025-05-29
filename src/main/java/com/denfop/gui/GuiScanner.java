package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerScanner;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.base.TileScanner;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiScanner<T extends ContainerScanner> extends GuiIU<ContainerScanner> {

    private static final ResourceLocation background = new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIScanner.png".toLowerCase());
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
        this.addComponent(new GuiComponent(this, 12, 26, EnumTypeComponent.ENERGY,
                new Component<>(this.getContainer().base.energy)
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
        if (container.base.getState() == TileScanner.State.COMPLETED || container.base.getState() == TileScanner.State.TRANSFER_ERROR || container.base.getState() == TileScanner.State.FAILED) {
            if (i >= 102 && i <= 102 + 12 && j >= 49 && j <= 49 + 12) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                new PacketUpdateServerTile(this.container.base, 0);
            }
        }
        if (container.base.getState() == TileScanner.State.COMPLETED || container.base.getState() == TileScanner.State.TRANSFER_ERROR || container.base.getState() == TileScanner.State.FAILED) {
            if (i >= 143 && i <= 143 + 24 && j >= 49 && j <= 49 + 24) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                new PacketUpdateServerTile(this.container.base, 1);
            }
        }
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int mouseX, int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        TileScanner te = this.container.base;

        switch (te.getState()) {
            case IDLE:
              draw(poseStack, Localization.translate("Scanner.gui.idle"), 10, 69, 15461152);
                break;
            case NO_STORAGE:
              draw(poseStack, this.info[2], 10, 69, 15461152);
                break;
            case SCANNING:
                draw(poseStack, this.info[1], 10, 69, 2157374);
               draw(poseStack, te.getPercentageDone() + "%", 125, 69, 2157374);
                break;
            case NO_ENERGY:
              draw(poseStack, this.info[3], 10, 69, 14094352);
                break;
            case ALREADY_RECORDED:
               draw(poseStack, this.info[8], 10, 69, 14094352);
                break;
            case FAILED:
               draw(poseStack, this.info[4], 10, 69, 2157374);
               draw(poseStack, this.info[6], 110, 30, 14094352);
                break;
            case COMPLETED:
            case TRANSFER_ERROR:
                if (te.getState() == TileScanner.State.COMPLETED) {
                   draw(poseStack, this.info[4], 10, 69, 2157374);
                }

                if (te.getState() == TileScanner.State.TRANSFER_ERROR) {
                    draw(poseStack, this.info[7], 10, 69, 14094352);
                }

               draw(poseStack, ModUtils.getString(te.patternUu) + "B UUM", 105, 25, 16777215);
               draw(poseStack, ModUtils.getString(te.patternEu) + "EF", 105, 36, 16777215);
        }
        if (container.base.getState() == TileScanner.State.COMPLETED || container.base.getState() == TileScanner.State.TRANSFER_ERROR || container.base.getState() == TileScanner.State.FAILED) {
            new Area(this, 102, 49, 12, 12).withTooltip("Scanner.gui.button.delete").drawForeground(poseStack, mouseX, mouseY);
        }
        if (container.base.getState() == TileScanner.State.COMPLETED || container.base.getState() == TileScanner.State.TRANSFER_ERROR || container.base.getState() == TileScanner.State.FAILED) {
            new Area(this, 143, 49, 24, 12).withTooltip("Scanner.gui.button.save").drawForeground(poseStack, mouseX, mouseY);
        }
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawBackgroundAndTitle(poseStack, partialTicks, mouseX, mouseY);
        if (this.container.base instanceof IUpgradableBlock) {
            bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
        this.bindTexture();
        if (container.base.getState() == TileScanner.State.COMPLETED || container.base.getState() == TileScanner.State.TRANSFER_ERROR || container.base.getState() == TileScanner.State.FAILED) {
            this.drawTexturedRect(poseStack, 102, 49, 12.0D, 12.0D, 176, 57);

        }
        if (container.base.getState() == TileScanner.State.COMPLETED || container.base.getState() == TileScanner.State.TRANSFER_ERROR) {
            this.drawTexturedRect(poseStack, 143, 49, 24, 12.0D, 176, 69);
        }

        if (container.base.getState() == TileScanner.State.COMPLETED || container.base.getState() == TileScanner.State.TRANSFER_ERROR || container.base.getState() == TileScanner.State.FAILED) {
            new Area(this, 102, 49, 12, 12).withTooltip("Scanner.gui.button.delete").drawBackground(poseStack, mouseX, mouseY);
        }
        if (container.base.getState() == TileScanner.State.COMPLETED || container.base.getState() == TileScanner.State.TRANSFER_ERROR || container.base.getState() == TileScanner.State.FAILED) {
            new Area(this, 143, 49, 24, 12).withTooltip("Scanner.gui.button.save").drawBackground(poseStack, mouseX, mouseY);
        }
        this.bindTexture();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        TileScanner te = this.container.base;
        int scanningloop = te.getSubPercentageDoneScaled(66);
        if (scanningloop > 0) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 30, this.guiTop + 20, 176, 14, scanningloop, 43);
        }
        for (GuiComponent element : this.componentList) {
            element.drawBackground(poseStack, this.guiLeft, this.guiTop);
        }

    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
