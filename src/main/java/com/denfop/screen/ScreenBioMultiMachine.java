package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.recipe.InventoryBioMultiRecipes;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.EnumTypeMachines;
import com.denfop.componets.ComponentBioProcessRender;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerMenuBioMultiMachine;
import com.denfop.containermenu.SlotInvSlot;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenBioMultiMachine<T extends ContainerMenuBioMultiMachine> extends ScreenMain<ContainerMenuBioMultiMachine> {

    private final ContainerMenuBioMultiMachine container;
    private final ScreenWidget process;

    public ScreenBioMultiMachine(ContainerMenuBioMultiMachine container1) {
        super(container1, EnumTypeStyle.BIO);
        this.container = container1;
        this.process = new ScreenWidget(this, 0, 0, EnumTypeComponent.BIO_MULTI_PROCESS,
                new WidgetDefault<>(new ComponentBioProcessRender(container1.base.multi_process, container1.base.getTypeMachine()))
        );
        this.addComponent(new ScreenWidget(this, 7, 64, EnumTypeComponent.BIO_FLUID,
                new WidgetDefault<>(this.container.base.bioFuel)
        ));
        imageWidth = 176 + 16;
        if (this.container.base.tank != null) {
            this.addComponent(new ScreenWidget(this, 27, 63, EnumTypeComponent.WATER,
                    new WidgetDefault<>(this.container.base.tank)
            ));
        }
        if (this.container.base.getMachine().type == EnumTypeMachines.Centrifuge) {
            this.addComponent(new ScreenWidget(this, 27, 63, EnumTypeComponent.COLD,
                    new WidgetDefault<>(this.container.base.heat)
            ));
        }
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("bio_machine.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 9; i++) {
                compatibleUpgrades.add(Localization.translate("bio_machine.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 100, mouseY, text);
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        this.drawForeground(poseStack, mouseX, mouseY);
        handleUpgradeTooltip(mouseX, mouseY);
        int i = 0;
        for (Slot slot : this.container.slots) {
            if (slot instanceof SlotInvSlot) {
                int xX = slot.x;
                int yY = slot.y;
                SlotInvSlot slotInv = (SlotInvSlot) slot;
                if (slotInv.inventory instanceof InventoryBioMultiRecipes) {
                    this.process.setIndex(i);
                    this.process.setX(xX);
                    this.process.setY(yY + 19);
                    this.process.drawForeground(poseStack, mouseX, mouseY);
                    i++;
                }

            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {

        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(getTexture());
        int j = guiLeft;
        int k = guiTop;
        drawTexturedModalRect(poseStack, j, k, 0, 0, 176, this.imageHeight);
        int xoffset = guiLeft;
        int yoffset = guiTop;
        this.drawBackground(poseStack);
        int i = 0;
        for (Slot slot : this.container.slots) {
            if (slot instanceof SlotInvSlot) {
                int xX = slot.x;
                int yY = slot.y;
                SlotInvSlot slotInv = (SlotInvSlot) slot;
                if (slotInv.inventory instanceof InventoryBioMultiRecipes) {
                    this.process.setIndex(i);
                    this.process.setX(xX);
                    this.process.setY(yY + 19);
                    this.process.drawBackground(poseStack, xoffset, yoffset);
                    i++;
                }
            }
        }
        bindTexture(getTexture());
        if (!this.isBlack) {
            this.drawXCenteredString(poseStack, 176 / 2, 6, Localization.translate(this.container.base.getName()), 4210752, false);
        } else {
            this.drawXCenteredString(poseStack,
                    176 / 2,
                    6,
                    Localization.translate(this.container.base.getName()),
                    ModUtils.convertRGBcolorToInt(216, 216, 216),
                    false
            );
        }


        for (final ScreenWidget guiElement : this.elements) {
            guiElement.drawBackground(poseStack, this.guiLeft, this.guiTop);

        }
        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guibio_machine.png");
    }

}
