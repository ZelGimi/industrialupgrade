package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.ScrollDirection;
import com.denfop.api.space.IBody;
import com.denfop.api.space.fakebody.Data;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.api.space.fakebody.SpaceOperation;
import com.denfop.api.space.rovers.api.IRovers;
import com.denfop.audio.EnumSound;
import com.denfop.network.packet.PacketChangeSpaceOperation;
import com.denfop.network.packet.PacketReturnRoversToPlanet;
import com.denfop.network.packet.PacketSendRoversToPlanet;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import static com.denfop.gui.GuiCore.bindTexture;

public class GuiExpeditionTableSpace extends GuiDefaultResearchTable {

    boolean hoverDelete;
    boolean hoverSend;
    boolean hoverBack;
    boolean hoverAuto;

    public GuiExpeditionTableSpace(GuiResearchTableSpace tileEntityResearchTableSpace) {
        super(tileEntityResearchTableSpace, 25, 25, 150, 150);
    }

    @Override
    public void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

        hoverDelete = false;
        hoverSend = false;
        hoverBack = false;
        hoverAuto = false;
        int guiLeft = this.x + offsetX1;
        int guiTop = this.y + offsetY1;
        if (par1 >= guiLeft + width - 20 && par2 <= guiLeft + width - 20 + 15 && par2 >= guiTop + 5 && par2 <= guiTop + 20) {
            hoverDelete = true;
        }
        IFakeBody fakeBody = this.getTile().container.base.fakeBody;
        if (fakeBody == null) {
            if (par1 >= guiLeft + 30 + 7 && par2 <= guiLeft + 30 + 7 + 103 * 0.75 && par2 >= guiTop + 55 && par2 <= guiTop + 55 + 20 * 0.75) {
                hoverSend = true;
            }
        } else {
            IRovers rover = fakeBody.getRover();
            int fluid = rover.getItem().getFluidHandler(rover.getItemStack()).drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE).getAmount();
            double energy = ElectricItem.manager.getCharge(rover.getItemStack());

            if (fakeBody.getTimerTo().canWork()) {
                new Area(this.tile, (int) (guiLeft + (width / 2 - (159 * 0.75) / 2)), guiTop + 25, (int) (159 * 0.75), (int) (16 * 0.75))
                        .withTooltip("Летит" + "\n" + "Время: " + fakeBody.getTimerTo().getDisplay() + "\n" + "Жидкость: " + fluid + "mb \n" + "Энергия: " + ModUtils.getString(energy) + "EF")
                        .drawForeground(poseStack, par1, par2);
            }
            if (fakeBody.getTimerFrom().canWork()) {
                new Area(this.tile, (int) (guiLeft + (width / 2 - (159 * 0.75) / 2)), guiTop + 25, (int) (159 * 0.75), (int) (16 * 0.75))
                        .withTooltip("Возвращается" + "\n" + "Время: " + fakeBody.getTimerFrom().getDisplay() + "\n" + "Жидкость: " + fluid + "mb \n" + "Энергия: " + ModUtils.getString(energy) + "EF")
                        .drawForeground(poseStack, par1, par2);
            }

            if (par1 >= guiLeft + 30 + 7 && par2 <= guiLeft + 30 + 7 + 103 * 0.75 && par2 >= guiTop + 55 && par2 <= guiTop + 55 + 20 * 0.75) {
                hoverBack = true;
            }
            if (par1 >= guiLeft + 30 + 7 && par2 <= guiLeft + 30 + 7 + 103 * 0.75 && par2 >= guiTop + 35 + 55 && par2 <= guiTop + 35 + 55 + 20 * 0.75) {
                hoverAuto = true;
            }
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        this.getTile().drawString(poseStack, Localization.translate("iu.space.expedition.title"), getTile().guiLeft + this.x + offsetX1 + width / 2 - tile.getStringWidth(Localization.translate("iu.space.expedition.title")) / 2, getTile().guiTop + this.y + offsetY1 + 5, ModUtils.convertRGBAcolorToInt(255, 255, 255));
        IBody planet = this.getTile().focusedPlanet;
        PoseStack pose = poseStack.pose();
        int guiLeft = getTile().guiLeft + this.x + offsetX1;
        int guiTop = getTile().guiTop + this.y + offsetY1;
        pose.pushPose();
        pose.translate(guiLeft + width - 20, guiTop + 5, 20);
        bindTexture(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other.png"));
        pose.scale(0.5f, 0.5f, 1);
        if (!hoverDelete)
            getTile().drawTexturedModalRect(poseStack, 0, 0, 139, 32, 30, 30);
        else
            getTile().drawTexturedModalRect(poseStack, 0, 0, 170, 32, 30, 30);
        pose.popPose();
        Data data = getTile().getContainer().base.dataMap.get(planet);
        IFakeBody fakeBody = this.getTile().container.base.fakeBody;
        if (fakeBody == null) {
            SpaceOperation spaceOperation = this.getTile().getContainer().base.getSpaceBody().get(this.getTile().focusedPlanet);
            if (spaceOperation == null) {
                getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other2.png"), (int) (guiLeft + width / 2 - (159 * 0.75) / 2), guiTop + 25, 20, 0.75f, 0.75f, 0, 34, 159, 16);
                getTile().drawCenteredText(poseStack, Localization.translate("iu.space.expedition.no_expedition"), (float) (guiLeft + width / 2), guiTop + 25 + 4, 20, 0.5f, ModUtils.convertRGBAcolorToInt(0, 0, 0));

            } else {
                switch (spaceOperation.getOperation()) {
                    case FAIL -> {
                        getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other2.png"), (int) (guiLeft + width / 2 - (159 * 0.75) / 2), guiTop + 25, 20, 0.75f, 0.75f, 0, 17, 159, 16);
                        getTile().drawCenteredText(poseStack, Localization.translate("iu.space.expedition.failed"), (float) (guiLeft + width / 2), guiTop + 25 + 4, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

                    }
                    case SUCCESS -> {
                        getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other2.png"), (int) (guiLeft + width / 2 - (159 * 0.75) / 2), guiTop + 25, 20, 0.75f, 0.75f, 0, 0, 159, 16);
                        getTile().drawCenteredText(poseStack, Localization.translate("iu.space.expedition.success"), (float) (guiLeft + width / 2), guiTop + 25 + 4, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

                    }
                }
            }
        } else {
            getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other2.png"), (int) (guiLeft + width / 2 - (159 * 0.75) / 2), guiTop + 25, 20, 0.75f, 0.75f, 0, 0, 159, 16);
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space.expedition.in_progress"), (float) (guiLeft + width / 2), guiTop + 25 + 4, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

        }
        if (fakeBody == null) {
            if (!hoverSend) {
                getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30 + 7, guiTop + 55, 0, 0.75f, 0.75f, 0, 105, 103, 20);
            } else {
                getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30 + 7, guiTop + 55, 0, 0.75f, 0.75f, 0, 125, 103, 20);

            }
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space.expedition.send"), guiLeft + 30 + 7 + (102 / 2f) * 0.75f, guiTop + 55 + 5, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));
        } else {
            if (!hoverBack) {
                getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30 + 7, guiTop + 55, 0, 0.75f, 0.75f, 0, 105, 103, 20);
            } else {
                getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30 + 7, guiTop + 55, 0, 0.75f, 0.75f, 0, 125, 103, 20);

            }
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space.expedition.return"), guiLeft + 30 + 7 + (102 / 2f) * 0.75f, guiTop + 55 + 5, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

            if (!hoverAuto) {
                getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30 + 7, guiTop + 55 + 35, 0, 0.75f, 0.75f, 0, 105, 103, 20);
            } else {
                getTile().drawRect(poseStack, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_space_other.png"), guiLeft + 30 + 7, guiTop + 55 + 35, 0, 0.75f, 0.75f, 0, 125, 103, 20);

            }
            getTile().drawCenteredText(poseStack, Localization.translate("iu.space.auto_" + !fakeBody.getSpaceOperation().getAuto()), guiLeft + 30 + 7 + (102 / 2f) * 0.75f, guiTop + 55 + 35 + 5, 20, 0.5f, ModUtils.convertRGBAcolorToInt(255, 255, 255));

        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, ScrollDirection direction) {
        return super.mouseScrolled(mouseX, mouseY, direction);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        if (hoverDelete) {
            this.getTile().defaultResearchGuis.remove(this);
            getTile().getContainer().player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
            return true;

        }
        if (hoverSend) {
            new PacketSendRoversToPlanet(
                    this.getTile().container.base,
                    this.getTile().container.player,
                    this.getTile().focusedPlanet
            );
            getTile().getContainer().player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
        }
        if (hoverBack) {

            new PacketReturnRoversToPlanet(this.getTile().container.base, this.getTile().container.player, this.getTile().focusedPlanet);
            getTile().getContainer().player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
        }
        if (hoverAuto) {
            new PacketChangeSpaceOperation(
                    this.getTile().container.player,
                    this.getTile().focusedPlanet
            );
            getTile().getContainer().player.playSound(EnumSound.button.getSoundEvent(), 0.5F, 1);
        }
        return super.mouseClicked(mouseX, mouseY);

    }
}
