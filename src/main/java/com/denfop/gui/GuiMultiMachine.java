package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.recipe.InvSlotMultiRecipes;
import com.denfop.componets.ComponentProcessRender;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerMultiMachine;
import com.denfop.container.SlotInvSlot;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;

public class GuiMultiMachine extends GuiIU<ContainerMultiMachine> {

    private final ContainerMultiMachine container;
    private final GuiComponent process;
    private final List<ItemStack> itemStackList = new ArrayList<>();

    public GuiMultiMachine(ContainerMultiMachine container1) {
        super(container1, container1.base.getMachine().getComponent());
        this.container = container1;
        this.process = new GuiComponent(this, 0, 0, EnumTypeComponent.MULTI_PROCESS,
                new Component<>(new ComponentProcessRender(container1.base.multi_process, container1.base.getTypeMachine()))
        );
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 8, 48, EnumTypeComponent.ENERGY_CLASSIC,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 17, 48, EnumTypeComponent.ENERGY_RF_CLASSIC,
                new Component<>(this.container.base.energy2)
        ));
        if (this.container.base.getMachine().type != EnumTypeMachines.Centrifuge) {
            this.addComponent(new GuiComponent(this, 27, 47, EnumTypeComponent.COLD,
                    new Component<>(this.container.base.cold)
            ));
        }

        this.addComponent(new GuiComponent(this, 27, 63, EnumTypeComponent.WATER,
                new Component<>(this.container.base.tank)
        ));
        if (this.container.base.getMachine().type == EnumTypeMachines.Centrifuge) {
            this.addComponent(new GuiComponent(this, 27, 63, EnumTypeComponent.COLD,
                    new Component<>(this.container.base.heat)
            ));
        }
        this.addComponent(new GuiComponent(this, 34, 47, EnumTypeComponent.EXP,
                new Component<>(this.container.base.exp)
        ));
        isBlack = false;

        if (this.container.base.energy2.isRf()) {
            itemStackList.add(new ItemStack(IUItem.module7, 1, 4));
        }
        if (this.container.base.multi_process.quickly) {
            itemStackList.add(new ItemStack(IUItem.module_quickly));
        }
        if (this.container.base.multi_process.modulesize) {
            itemStackList.add(new ItemStack(IUItem.module_stack));
        }
        if (this.container.base.multi_process.modulestorage) {
            itemStackList.add(new ItemStack(IUItem.module_storage));
        }
        if (this.container.base.solartype != null) {
            itemStackList.add(new ItemStack(IUItem.module6, 1, this.container.base.solartype.meta));
        }

        if (this.container.base.cold.upgrade && this.container.base.getTypeMachine() != EnumTypeMachines.Centrifuge) {
            itemStackList.add(new ItemStack(IUItem.coolupgrade, 1, this.container.base.cold.meta));

        }
        xSize = 176 + 16;
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        this.drawForeground(mouseX, mouseY);
        int i = 0;
        for (Slot slot : this.container.inventorySlots) {
            if (slot instanceof SlotInvSlot) {
                int xX = slot.xPos;
                int yY = slot.yPos;
                SlotInvSlot slotInv = (SlotInvSlot) slot;
                if (slotInv.invSlot instanceof InvSlotMultiRecipes) {
                    this.process.setIndex(i);
                    this.process.setX(xX);
                    this.process.setY(yY + 19);
                    this.process.drawForeground(mouseX, mouseY);
                    i++;
                }

            }
        }
        i = 0;
        for (ItemStack stack : this.itemStackList) {
            new Area(this, 177, 5 + i * 18, 16, 16).withTooltip(stack.getDisplayName()).drawForeground(mouseX, mouseY);
            i++;
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {

        super.drawGuiContainerBackgroundLayer(f, x, y);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, 176, this.ySize);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("ic2", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        this.mc.getTextureManager().bindTexture(getTexture());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.drawBackground();
        int i = 0;
        for (Slot slot : this.container.inventorySlots) {
            if (slot instanceof SlotInvSlot) {
                int xX = slot.xPos;
                int yY = slot.yPos;
                SlotInvSlot slotInv = (SlotInvSlot) slot;
                if (slotInv.invSlot instanceof com.denfop.api.recipe.InvSlotMultiRecipes) {
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
            if (guiElement.isEnabled()) {
                guiElement.drawBackground(x - this.guiLeft, y - this.guiTop);
            }
        }

        i = 0;
        for (ItemStack stack : this.itemStackList) {

            RenderHelper.enableGUIStandardItemLighting();
            GL11.glPushMatrix();
            GL11.glColor4f(0.1F, 1, 0.1F, 1);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            this.zLevel = 100.0F;
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            itemRender.renderItemAndEffectIntoGUI(
                    stack,
                    j + 177,
                    k + 5 + i * 18
            );
            GL11.glEnable(GL11.GL_LIGHTING);
            GlStateManager.enableLighting();

            RenderHelper.enableStandardItemLighting();
            GL11.glColor4f(0.1F, 1, 0.1F, 1);
            GL11.glPopMatrix();
            i++;
        }

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main.png");
    }

}
