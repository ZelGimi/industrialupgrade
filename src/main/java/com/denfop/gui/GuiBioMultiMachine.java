package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.recipe.InvSlotBioMultiRecipes;
import com.denfop.api.recipe.InvSlotSteamMultiRecipes;
import com.denfop.componets.ComponentBioProcessRender;
import com.denfop.componets.ComponentSteamProcessRender;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerBioMultiMachine;
import com.denfop.container.ContainerSteamMultiMachine;
import com.denfop.container.SlotInvSlot;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import com.denfop.utils.ModUtils;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiBioMultiMachine extends GuiIU<ContainerBioMultiMachine> {

    private final ContainerBioMultiMachine container;
    private final GuiComponent process;

    public GuiBioMultiMachine(ContainerBioMultiMachine container1) {
        super(container1, EnumTypeStyle.BIO);
        this.container = container1;
        this.process = new GuiComponent(this, 0, 0, EnumTypeComponent.BIO_MULTI_PROCESS,
                new Component<>(new ComponentBioProcessRender(container1.base.multi_process, container1.base.getTypeMachine()))
        );
        this.addComponent(new GuiComponent(this, 7, 64, EnumTypeComponent.BIO_FLUID,
                new Component<>(this.container.base.bioFuel)
        ));
        xSize = 176 + 16;
        if (this.container.base.tank != null)
            this.addComponent(new GuiComponent(this, 27, 63, EnumTypeComponent.WATER,
                    new Component<>(this.container.base.tank)
            ));
        if (this.container.base.getMachine().type == EnumTypeMachines.Centrifuge) {
            this.addComponent(new GuiComponent(this, 27, 63, EnumTypeComponent.COLD,
                    new Component<>(this.container.base.heat)
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

            this.drawTooltip(mouseX-100, mouseY, text);
        }
    }
    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        this.drawForeground(mouseX, mouseY);
        handleUpgradeTooltip(mouseX,mouseY);
        int i = 0;
        for (Slot slot : this.container.inventorySlots) {
            if (slot instanceof SlotInvSlot) {
                int xX = slot.xPos;
                int yY = slot.yPos;
                SlotInvSlot slotInv = (SlotInvSlot) slot;
                if (slotInv.invSlot instanceof InvSlotBioMultiRecipes) {
                    this.process.setIndex(i);
                    this.process.setX(xX);
                    this.process.setY(yY + 19);
                    this.process.drawForeground(mouseX, mouseY);
                    i++;
                }

            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {

        super.drawGuiContainerBackgroundLayer(f, x, y);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, 176, this.ySize);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.drawBackground();
        int i = 0;
        for (Slot slot : this.container.inventorySlots) {
            if (slot instanceof SlotInvSlot) {
                int xX = slot.xPos;
                int yY = slot.yPos;
                SlotInvSlot slotInv = (SlotInvSlot) slot;
                if (slotInv.invSlot instanceof com.denfop.api.recipe.InvSlotBioMultiRecipes) {
                    this.process.setIndex(i);
                    this.process.setX(xX);
                    this.process.setY(yY + 19);
                    this.process.drawBackground(xoffset, yoffset);
                    i++;
                }
            }
        }
        this.mc.getTextureManager().bindTexture(getTexture());
        if (!this.isBlack) {
            this.drawXCenteredString(176 / 2, 6, Localization.translate(this.container.base.getName()), 4210752, false);
        } else {
            this.drawXCenteredString(
                    176 / 2,
                    6,
                    Localization.translate(this.container.base.getName()),
                    ModUtils.convertRGBcolorToInt(216, 216, 216),
                    false
            );
        }


        for (final GuiElement<?> guiElement : this.elements) {
            guiElement.drawBackground(x - this.guiLeft, y - this.guiTop);

        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guibio_machine.png");
    }

}
